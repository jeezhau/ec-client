package com.mofangyouxuan.wx.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 商城管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/shop")
@SessionAttributes({"isDayFresh","sys_func","categories"})
public class ShopAction {
	
	/**
	 * 获取商城管理主页
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		List<Category> categories = (List<Category>) map.get("categories");
		if(categories == null) {
			categories = GoodsService.getCategories();
			map.put("categories", categories);
		}

		map.put("isDayFresh", "0");	//系统默认为访问商城管理
		map.put("sys_func", "shop");
		
		return "shop/page-shop-index";
	}
	
	@RequestMapping("/getall")
	@ResponseBody
	public String getShopAllGoods(Integer categoryId,String keywords,PageCond pageCond) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			params.put("isSelf", false);
			if(categoryId != null && categoryId>0) {
				params.put("categoryId", categoryId);	
			}
			if(keywords != null && keywords.trim().length()>1) {
				params.put("keywords", keywords.trim());	
			}
			JSONObject sortParams = new JSONObject();
			sortParams.put("sale", "1#1");
			sortParams.put("time", "2#1");
			//{errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
			jsonRet = GoodsService.searchGoods(true,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	@RequestMapping("/mcht/getall/{partnerId}")
	@ResponseBody
	public String getMchtAllGoods(@PathVariable("partnerId")Integer partnerId,PageCond pageCond) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			params.put("isSelf", false);
			params.put("partnerId", partnerId);	
			JSONObject sortParams = new JSONObject();
			sortParams.put("sale", "1#1");
			sortParams.put("time", "2#1");
			jsonRet = GoodsService.searchGoods(true,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	
}
