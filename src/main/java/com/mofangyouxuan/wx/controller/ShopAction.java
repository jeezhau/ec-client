package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;
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
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		
		map.put("isDayFresh", "0");	//系统默认为访问商城管理
		map.put("sys_func", "shop");
		
		return "shop/page-shop-index";
	}
	
	@RequestMapping("/getall")
	@ResponseBody
	public String getAllGoods(Integer categoryId,String keywords,PageCond pageCond) {
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
	
}
