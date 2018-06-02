package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.ObjectToMap;

/**
 * 商品管理服务
 * @author jeekhan
 *
 */
@Component
public class GoodsService {
	private static String mfyxServerUrl;
	private static String goodsAddUrl;
	private static String goodsUpdateUrl;
	private static String goodsGetUrl;
	private static String goodsGetallNopartnerUrl;
	private static String goodsGetallWithPartnerUrl;
	private static String goodsChangeStatusUrl;
	private static String goodsChangeSpecUrl;
	private static String goodsCategoryUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		GoodsService.mfyxServerUrl = mfyxServerUrl;
	}
	
	@Value("${mfyx.goods-add-url}")
	public void setGoodsAddUrl(String goodsAddUrl) {
		GoodsService.goodsAddUrl = goodsAddUrl;
	}
	@Value("${mfyx.goods-update-url}")
	public void setGoodsUpdateUrl(String goodsUpdateUrl) {
		GoodsService.goodsUpdateUrl = goodsUpdateUrl;
	}
	@Value("${mfyx.goods-get-url}")
	public void setGoodsGetRrl(String url) {
		GoodsService.goodsGetUrl = url;
	}
	@Value("${mfyx.goods-getall-nopartner-url}")
	public void setGoodsGetallNopartnerUrl(String url) {
		GoodsService.goodsGetallNopartnerUrl = url;
	}
	@Value("${mfyx.goods-getall-withpartner-url}")
	public void setGoodsGetallWithPartnerUrl(String url) {
		GoodsService.goodsGetallWithPartnerUrl = url;
	}
	@Value("${mfyx.goods-change-status-url}")
	public void setGoodsChangeStatusUrl(String goodsChangeStatusUrl) {
		GoodsService.goodsChangeStatusUrl = goodsChangeStatusUrl;
	}
	@Value("${mfyx.goods-change-spec-url}")
	public void setGoodsChangeSpecUrl(String goodsChangeSpecUrl) {
		GoodsService.goodsChangeSpecUrl = goodsChangeSpecUrl;
	}
	@Value("${mfyx.goods-category-url}")
	public void setGoodsCategoryUrl(String goodsCategoryUrl) {
		GoodsService.goodsCategoryUrl = goodsCategoryUrl;
	}
	
	/**
	 * 
	 * @param needPartner 是否包含合作伙伴数据
	 * @param goodsId
	 * @return {"errcode":-1,"errmsg":"错误信息",goods:{...}} 
	 */
	public static JSONObject getGoods(boolean needPartner,Long goodsId,boolean isSelf) {
		String np = needPartner ? "1" : "0";
		String self = isSelf ? "1" : "0";;
		String url = mfyxServerUrl + goodsGetUrl ;
		url = url.replace("{goodsId}", goodsId+"") ;
		url = url.replace("{isSelf}",self) ;
		url = url.replace("{needPartner}", np+"") ;
		String strRet = HttpUtils.doGet(url);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}
	
	/**
	 * @param needPartner 是否包含合作伙伴数据
	 * @param jsonSearchParams
	 * @param jsonSortParams
	 * @param jsonPageCond
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject searchGoods(boolean needPartner,String jsonSearchParams,String jsonSortParams,String jsonPageCond) {
		String url = mfyxServerUrl + (needPartner==true?goodsGetallWithPartnerUrl:goodsGetallNopartnerUrl);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("jsonSearchParams", jsonSearchParams);
		params.put("jsonSortParams", jsonSortParams);
		params.put("jsonPageCond", jsonPageCond);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param goodsIds
	 * @param partnerId
	 * @param newStatus
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject changeStatus(String goodsIds,Integer partnerId,String newStatus,Integer updateOpr,String passwd) {
		String url = mfyxServerUrl + goodsChangeStatusUrl;
		url = url.replace("{partnerId}", partnerId+"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("goodsIds", goodsIds);
		params.put("newStatus", newStatus);
		params.put("currUserId", updateOpr);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param partnerId
	 * @param goodsId
	 * @param specDetail
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject changeSpec(Integer partnerId,Long goodsId,String specDetail,Integer updateOpr,String passwd) {
		String url = mfyxServerUrl + goodsChangeSpecUrl;
		url = url.replace("{partnerId}", partnerId+"");
		url = url.replace("{goodsId}", goodsId+"");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("specDetail", specDetail);
		params.put("currUserId", updateOpr);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param goods
	 * @return {errcode:0,errmsg:'ok',goodsId:111}
	 */
	public static JSONObject addGoods(Goods goods,Integer partnerId,String passwd) {
		String url = mfyxServerUrl + goodsAddUrl;
		url = url.replace("{partnerId}", partnerId+"");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"updateTime","reviewLog","reviewOpr","reviewTime","reviewResult"};
		params = ObjectToMap.object2Map(goods,excludeFields,false);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param goods
	 * @return {errcode:0,errmsg:'ok',goodsId:111}
	 */
	public static JSONObject updateGoods(Goods goods,Integer partnerId,String passwd) {
		String url = mfyxServerUrl + goodsUpdateUrl;
		url = url.replace("{partnerId}", partnerId+"");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"updateTime","reviewLog","reviewOpr","reviewTime","reviewResult"};
		params = ObjectToMap.object2Map(goods,excludeFields,false);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return 
	 */
	public static List<Category> getCategories(){
		String url = mfyxServerUrl + goodsCategoryUrl;
		String strRet = HttpUtils.doGet(url);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("errcode") && jsonRet.getIntValue("errcode") ==0) {
				return JSONArray.parseArray(jsonRet.getJSONArray("categories").toJSONString(), Category.class);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
