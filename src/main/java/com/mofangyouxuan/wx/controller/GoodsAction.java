package com.mofangyouxuan.wx.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;
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
	
	/**
	 * 获取商品管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/mgr")
	public String getGoodMgrIndex(ModelMap map) {
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
		return "goods/page-goods-mgr";
	}
	
	
	/**
	 * 获取合作伙伴的所有商品
	 * @param partnerId
	 * @param map
	 * @return
	 */
	@RequestMapping("/getall/bypartner/{partnerId}")
	public String getAllByPartner(Integer partnerId,ModelMap map) {
		
		
		
		return null;
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
		if(goodsId != 0) {
			//获取已有指定商品
		}else {
			
		}
		return "goods/page-goods-edit";
	}
	
	/**
	 * 保存商户信息
	 * @param goods
	 * @param result
	 * @return
	 */
	public String saveGoods(@Valid Goods goods,BindingResult result) {
		
		return null;
	}
	
	
	/**
	 * 获取商品详细信息
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/detail/{goodsId}")
	public String getDetail(@PathVariable("goodsId")String goodsId,ModelMap map) {
		
		return "goods/page-goods-detail";
	}
	
	/**
	 * 根据关键字查询商品信息
	 * 返回满足匹配的前100条信息
	 * @param keywords
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public String searchAll(String keywords) {
		
		return "";
	}
	
	/**
	 * 获取指定分类的商品数据
	 * @param category 类别名称	
	 * @param pageCond 分页信息 
	 * @return
	 */
	@RequestMapping("/getall/bycat/{category}")
	@ResponseBody
	public String getAllByCat(@PathVariable("category")String category,PageCond pageCond) {
		
		
		return "";
	}
	
	/**
	 * 获取所有商品数据
	 * @param pageCond 分页信息	
	 * @return
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public String getAll(PageCond pageCond) {
		
		
		return "";
	}
	
	/**
	 * 变更商品的状态：上架、下架
	 * @param goodsIds
	 * @param newStatus
	 * @return
	 */
	public String changeStatus(String goodsIds,String newStatus) {
		
		return null;
	}
	
	/**
	 * 修改商户的库存
	 * @param goodsId
	 * @param newCnt
	 * @return
	 */
	public String changeStock(Long goodsId,Integer newCnt) {
		
		return null;
	}
	
	
}


