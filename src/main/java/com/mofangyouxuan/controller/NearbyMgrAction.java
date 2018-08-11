package com.mofangyouxuan.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Position;
import com.mofangyouxuan.service.CategoryService;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 用户周边功能管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/nearby")
@SessionAttributes({"sys_func","vipBasic","partnerBasic","nbtopcats","receiverPosition"})
public class NearbyMgrAction {
	
	/**
	 * 获取周边功能首页
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/index")
	public String getIndex(ModelMap map,HttpServletRequest request) {
		List<Category> categories = (List<Category>) map.get("nbtopcats");
		if(categories == null) {
			JSONObject search = new JSONObject();
			search.put("status", "1");
			search.put("parentId", 0);
			categories = CategoryService.getAll(search);
			map.put("nbtopcats", categories);
		}
		map.put("sys_func", "nearby");
		return "shop/page-nearby-index";
	}

	/**
	 * 查询商品信息
	 * @param categoryId
	 * @param keywords
	 * @param pageCond
	 * @param city
	 * @param area
	 * @param lat	纬度
	 * @param lng	经度
	 * @return
	 */
	@RequestMapping("/getall/goods")
	@ResponseBody
	public String getAllGoods(Integer categoryId,String keywords,PageCond pageCond,
			@RequestParam(value="province",required=true)String province,
			@RequestParam(value="city",required=true)String city,
			@RequestParam(value="area",required=true)String area,
			@RequestParam(value="town",required=false)String town,
			@RequestParam(value="lat",required=true)BigDecimal lat,
			@RequestParam(value="lng",required=true)BigDecimal lng,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			Position receiverPosition = new Position();
			receiverPosition.setProvince(province);
			receiverPosition.setArea(area);
			receiverPosition.setCity(city);
			receiverPosition.setLat(lat);
			receiverPosition.setLng(lng);
			map.put("receiverPosition", receiverPosition);
			//keywords,category,dispatchMode,city,postageId,currUserLocX,currUserLocY
			JSONObject params = new JSONObject();
			params.put("isSelf", false);
			if(categoryId != null && categoryId>0) {
				params.put("categoryId", categoryId);	
			}
			if(keywords != null && keywords.trim().length()>1) {
				params.put("keywords", keywords.trim());	
			}
			params.put("city", city);
			params.put("currUserLocX", lng);
			params.put("currUserLocY", lat);
			JSONObject sortParams = new JSONObject();
			sortParams.put("dist", "1#1");
			sortParams.put("sale", "2#1");
			sortParams.put("time", "3#1");
			
			//{errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
			jsonRet = GoodsService.searchGoods(true,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{province,city,area,keywords}
	 * @param pageCond		分页信息:{begin, pageSize}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall/shop")
	@ResponseBody
	public Object getAll(Integer categoryId,String keywords,PageCond pageCond,
			@RequestParam(value="province",required=true)String province,
			@RequestParam(value="city",required=true)String city,
			@RequestParam(value="area",required=true)String area,
			@RequestParam(value="town",required=false)String town,
			@RequestParam(value="lat",required=true)BigDecimal lat,
			@RequestParam(value="lng",required=true)BigDecimal lng,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			Position receiverPosition = new Position();
			receiverPosition.setProvince(province);
			receiverPosition.setArea(area);
			receiverPosition.setCity(city);
			receiverPosition.setLat(lat);
			receiverPosition.setLng(lng);
			map.put("receiverPosition", receiverPosition);
			JSONObject params = new JSONObject();
			params.put("status", "S");
			params.put("pbTp", "1");
			if(keywords != null && keywords.trim().length()>1) {
				params.put("keywords", keywords.trim());	
			}
			params.put("city", city);
			params.put("currUserLocX", lng);
			params.put("currUserLocY", lat);
			JSONObject sortParams = new JSONObject();
			sortParams.put("dist", "1#1");
			sortParams.put("time", "2#1");
			jsonRet = PartnerMgrService.getAll(SysParam.getSyspartnerId(), params, sortParams,pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
}
