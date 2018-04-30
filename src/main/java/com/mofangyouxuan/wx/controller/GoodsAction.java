package com.mofangyouxuan.wx.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 商品新获取 
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/goods")
@SessionAttributes({"isDayFresh","sys_func","vipBasic","partnerBasic","categories"})
public class GoodsAction {
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	/**
	 * 获取商品管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getMgrIndex(ModelMap map) {
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		if(partner == null) {
			map.put("errmsg", "您尚未开通合作伙伴功能！")	;
			return "forward:/user/index/vip" ;
		}
		if(!"0".equals(partner.getStatus()) && !"S".equals(partner.getStatus())  && !"C".equals(partner.getStatus()) && !"R".equals(partner.getStatus())) {
			map.put("errmsg", "您当前的合作伙伴状态有误，不可进行商品管理！")	;
			return "forward:/user/index/vip" ;
		}
		map.put("sys_func", "partner-goods");
		return "goods/page-goods-manage";
	}
	

	
	/**
	 * 获取商品编辑页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/edit/{goodsId}")
	public String editGoods(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		if(partner == null) {
			map.put("errmsg", "您尚未开通合作伙伴功能！")	;
			return "forward:/user/index/vip" ;
		}
		if(!"0".equals(partner.getStatus()) && !"S".equals(partner.getStatus())  && !"C".equals(partner.getStatus()) && !"R".equals(partner.getStatus())) {
			map.put("errmsg", "您当前的合作伙伴状态有误，不可进行商品管理！")	;
			return "forward:/user/index/vip" ;
		}
		//
		Goods goods = null;
		if(goodsId != 0) {
			//获取已有指定商品
			JSONObject ret = GoodsService.getGoods(goodsId);
			if(ret == null || !ret.containsKey("goods")) {
				map.put("errmsg", "没有获取到该指定商品信息！");
				return "forward:/goods/manage";
			}else {
				goods = JSONObject.toJavaObject(ret.getJSONObject("goods"),Goods.class);
			}
		}else {
			goods = new Goods();
			goods.setGoodsId(0l);
		}
		map.put("goods", goods);
		map.put("sys_func", "partner-goods");
		
		return "goods/page-goods-edit";
	}
	
	/**
	 * 保存商户信息
	 * @param goods
	 * @param result
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String saveGoods(@Valid Goods goods,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic)map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该合作伙伴信息！");
				return jsonRet.toString();
			}
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				return jsonRet.toString();
			}
			//其他验证
			String isCityWide = goods.getIsCityWide();
			Integer limitCnt = goods.getLimitedNum();
			StringBuilder sb = new StringBuilder();
			if("0".equals(isCityWide)) {//全国
				String provLimit = goods.getProvLimit();
				if(provLimit == null || provLimit.length()<2) {
					sb.append(" 销售省份： 不可为空！");
				}
				if(provLimit.contains("全国")) {
					goods.setProvLimit("全国");
				}
			}else {//同城
				Integer distLimit = goods.getDistLimit();
				if(distLimit == null) {
					sb.append(" 销售距离范围： 不可为空！");
				}
			}
			if(limitCnt > 0) {
				String begin = goods.getBeginTime();
				String end = goods.getEndTime();
				if(begin == null| end == null) {
					sb.append("限购开始时间、限购结束时间：不可为空！");
				}
			}
			if(sb.length()>0) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", sb.toString());
				return jsonRet.toString();
			}
			//数据检查
			if(!"0".equals(partner.getStatus()) && !"S".equals(partner.getStatus())  && !"C".equals(partner.getStatus()) && !"R".equals(partner.getStatus())) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "您当前的合作伙伴状态有误，不可进行商品管理！");
				return jsonRet.toString();
			}
			//数据处理
			goods.setPartnerId(partner.getPartnerId());
			if(goods.getGoodsId() == 0) {
				jsonRet = GoodsService.addGoods(goods,vip.getVipId());
			}else {
				jsonRet = GoodsService.updateGoods(goods,vip.getVipId());
			}
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "保存商品信息出现错误！");
			}
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 获取合作伙伴的所有商品
	 * @param jsonParams
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public String getOwnAll(String status,String reviewResult,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic)map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该合作伙伴信息！");
				return jsonRet.toString();
			}
			JSONObject params = new JSONObject();
			params.put("partnerId", partner.getPartnerId());
			params.put("isSelf", true);
			if(status != null) {
				params.put("status", status);	
			}
			if(reviewResult != null) {
				params.put("reviewResult", reviewResult);	
			}
			JSONObject sortParams = new JSONObject();
			sortParams.put("time", "1#1");
			//{errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
			jsonRet = GoodsService.searchGoods(params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 获取商品详细信息
	 * @param goodsId
	 * @return {"errcode":-1,"errmsg":"错误信息",goods:{...}} 
	 */
	@RequestMapping("/get/{goodsId}")
	@ResponseBody
	public String getById(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		jsonRet = GoodsService.getGoods(goodsId);
		if(jsonRet == null) {
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "获取商品详情失败！");
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 变更商品的状态：上架、下架
	 * @param goodsIds
	 * @param newStatus
	 * @return 
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public String changeStatus(@RequestParam(value="goodsIds",required=true)String goodsIds,
			@RequestParam(value="newStatus",required=true)String newStatus,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic)map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该合作伙伴信息！");
				return jsonRet.toString();
			}
			//数据检查
			if(!"0".equals(partner.getStatus()) && !"S".equals(partner.getStatus())  && !"C".equals(partner.getStatus()) && !"R".equals(partner.getStatus())) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "您当前的合作伙伴状态有误，不可进行商品管理！");
				return jsonRet.toString();
			}
			//数据处理
			jsonRet = GoodsService.changeStatus(goodsIds, partner.getPartnerId(), newStatus);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "批量变更商品状态出现错误！");
			}
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 修改商户的库存
	 * @param goodsId
	 * @param newCnt
	 * @return
	 */
	@RequestMapping("/changeStock")
	@ResponseBody
	public String changeStock(@RequestParam(value="goodsId",required=true)Long goodsId,
			@RequestParam(value="newCnt",required=true)Integer newCnt,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) map.get("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic)map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该合作伙伴信息！");
				return jsonRet.toString();
			}
			//数据检查
			if(!"0".equals(partner.getStatus()) && !"S".equals(partner.getStatus())  && !"C".equals(partner.getStatus()) && !"R".equals(partner.getStatus())) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "您当前的合作伙伴状态有误，不可进行商品管理！");
				return jsonRet.toString();
			}
			//数据处理
			jsonRet = GoodsService.changeStock(partner.getPartnerId(), goodsId, newCnt);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "保存商品信息出现错误！");
			}
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取商品分类
	 * @param map
	 * @return {errcode:0,errmsg:"",categories:[{},{},...]}
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/categories")
	@ResponseBody
	public String getCategories(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		List<Category> categories = (List<Category>) map.get("categories");
		if(categories == null) {
			categories = GoodsService.getCategories();
		}
		jsonRet.put("errcode", 0);
		jsonRet.put("errmsg", "ok");
		jsonRet.put("categories", categories);
		return jsonRet.toJSONString();
	}
	
}


