package com.mofangyouxuan.wx.controller;

import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.GoodsSpec;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 商品管理功能 
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
		
		Goods goods = null;
		try {
			if(goodsId != 0) {
				//获取已有指定商品
				JSONObject ret = GoodsService.getGoods(false,goodsId,true);
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
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
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
			//规格检查
			List<GoodsSpec> specList = JSONArray.parseArray(goods.getSpecDetail(), GoodsSpec.class);
			if(specList == null || specList.size()<1 || specList.size()>30) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "规格信息记录数量为1-30条！");
				return jsonRet.toString();
			}
			StringBuilder sb = new StringBuilder();
			BigDecimal priceLowest = new BigDecimal(99999999.99);
			Integer stockSum = 0;
			for(GoodsSpec spec:specList) {
				if(spec == null) {
					sb.append("记录不可为空！");
				}else {
					if(spec.getName() == null || spec.getName().length()==0 || spec.getName().length()>20){
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】规格名称不合规，须为1-20字符！");
					}
					if(spec.getVal() == null || spec.getVal()<1 || spec.getVal()>999999) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】数量值不合规，须为1-999999的整数值！");
					}
					if(spec.getUnit() == null || spec.getUnit().length()<1 || spec.getUnit().length()>5) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】单位不合规，长度须为1-5字符！");
					}
					if(spec.getPrice() == null || spec.getPrice().doubleValue()<0 || spec.getPrice().doubleValue()>99999999.99) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】单价不合规，须为0-99999999.99的数值！");
					}else {
						if(priceLowest.compareTo(spec.getPrice()) > 0) {
							priceLowest = spec.getPrice();
						}
					}
					if(spec.getStock() == null || spec.getStock()<0 || spec.getStock()>999999) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】库存不合规，须为0-999999的整数值！");
					}else {
						stockSum += spec.getStock();
					}
				}
			}
			if(sb.length()>0) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "规格信息不合规：" + sb.toString());
				return jsonRet.toString();
			}
			for(int i=0;i<specList.size();i++) {
				for(int j=i+1;j<specList.size();j++) {
					if(specList.get(i).getName().equals(specList.get(j).getName())) {
						jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
						jsonRet.put("errmsg", "规格信息中不可出现同规格名称的记录！");
						return jsonRet.toString();
					}
				}
			}
			goods.setPriceLowest(priceLowest);
			goods.setStockSum(stockSum);
			//其他验证
			Integer limitCnt = goods.getLimitedNum();
			sb = new StringBuilder();
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
	 * 合作伙伴自己获取所有商品
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
			jsonRet = GoodsService.searchGoods(false,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 商家自己获取商品详细信息
	 * @param goodsId
	 * @return {"errcode":-1,"errmsg":"错误信息",goods:{...}} 
	 */
	@RequestMapping("/getown/{goodsId}")
	@ResponseBody
	public String getOwnById(@PathVariable("goodsId")Long goodsId,ModelMap map) {
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
			jsonRet = GoodsService.getGoods(false,goodsId,true);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取商品详情失败！");
			}
			if(jsonRet.containsKey("goods")) {
				Goods goods = JSONObject.toJavaObject(jsonRet.getJSONObject("goods"),Goods.class);
				if(!partner.getPartnerId().equals(goods.getPartnerId())) {//不是自己
					jsonRet = new JSONObject();
					jsonRet.put("errcode", ErrCodes.GOODS_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限查询该商品信息！");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 任何人获取商品详细信息
	 * @param goodsId
	 * @return {"errcode":-1,"errmsg":"错误信息",goods:{...}} 
	 */
	@RequestMapping("/get/{goodsId}")
	@ResponseBody
	public String getByIdWithPartner(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = GoodsService.getGoods(true,goodsId,false);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取商品详情失败！");
			}
			Goods goods = JSONObject.toJavaObject(jsonRet.getJSONObject("goods"),Goods.class);
			if("1".equals(goods.getStatus()) && "1".equals(goods.getReviewResult())) {
				jsonRet.put("errcode", ErrCodes.GOODS_STATUS_ERROR);
				jsonRet.put("errmsg", "该商品当前不可查询详情！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 任何人获取指定商品的详细信息并展示，包含展示部分合作伙伴信息
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/show/{goodsId}")
	public String showGoods(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		Goods goods = null;
		try {
			JSONObject obj = GoodsService.getGoods(true,goodsId,true);
			if(obj == null || !obj.containsKey("goods")) {
				map.put("errmsg", "获取商品详情失败！");
			}else {
				goods = JSONObject.toJavaObject(obj.getJSONObject("goods"),Goods.class);
				if(partner.getPartnerId().equals(goods.getPartnerId())) {//自己
					map.put("goods", goods);
				}else { //其他人
					if("S".equals(goods.getPartner().getStatus()) && 
							"1".equals(goods.getStatus()) && "1".equals(goods.getReviewResult())) {
						map.put("goods", goods);
					}else {
						map.put("errmsg", "该商品当前不可访问！");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "goods/page-goods-show";
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
	 * 修改商户规格与库存
	 * @param goodsId
	 * @param specDetail
	 * @return
	 */
	@RequestMapping("/changeSpec")
	@ResponseBody
	public String changeSpec(@RequestParam(value="goodsId",required=true)Long goodsId,
			@RequestParam(value="specDetail",required=true)String specDetail,ModelMap map) {
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
			List<GoodsSpec> specList = JSONArray.parseArray(specDetail, GoodsSpec.class);
			if(specList == null || specList.size()<1 || specList.size()>30) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "规格信息记录数量为1-30条！");
				return jsonRet.toString();
			}
			StringBuilder sb = new StringBuilder();
			for(GoodsSpec spec:specList) {
				if(spec == null) {
					sb.append("记录不可为空！");
				}else {
					if(spec.getName() == null || spec.getName().length()==0 || spec.getName().length()>20){
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】规格名称不合规，须为1-20字符！");
					}
					if(spec.getVal() == null || spec.getVal()<1 || spec.getVal()>999999) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】数量值不合规，须为1-999999的整数值！");
					}
					if(spec.getUnit() == null || spec.getUnit().length()<1 || spec.getUnit().length()>5) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】单位不合规，长度须为1-5字符！");
					}
					if(spec.getPrice() == null || spec.getPrice().doubleValue()<0 || spec.getPrice().doubleValue()>99999999.99) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】单价不合规，须为0-99999999.99的数值！");
					}
					if(spec.getStock() == null || spec.getStock()<0 || spec.getStock()>999999) {
						sb.append("记录【" + JSONObject.toJSONString(spec) + "】库存不合规，须为0-999999的整数值！");
					}
				}
			}
			if(sb.length()>0) {
				jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
				jsonRet.put("errmsg", "规格信息不合规：" + sb.toString());
				return jsonRet.toString();
			}
			for(int i=0;i<specList.size();i++) {
				for(int j=i+1;j<specList.size();j++) {
					if(specList.get(i).getName().equals(specList.get(j).getName())) {
						jsonRet.put("errcode", ErrCodes.GOODS_PARAM_ERROR);
						jsonRet.put("errmsg", "规格信息中不可出现同规格名称的记录！");
						return jsonRet.toString();
					}
				}
			}
			//数据处理
			jsonRet = GoodsService.changeSpec(partner.getPartnerId(), goodsId, specDetail);
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


