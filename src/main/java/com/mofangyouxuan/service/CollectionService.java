package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.utils.HttpUtils;

/**
 * 用户收藏口调用处理
 * @author jeekhan
 *
 */
@Component
public class CollectionService {
	
	private static String mfyxServerUrl;
	private static String collectionGetCntUrl;
	private static String collectionGetAllUrl;
	private static String collectionAddUrl;
	private static String collectionDeleteUrl;

	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		CollectionService.mfyxServerUrl = mfyxServerUrl;
	}
	@Value("${mfyx.collection-getcnt-url}")
	public void setCollectionGetCntUrl(String url) {
		collectionGetCntUrl = url ;
	}
	@Value("${mfyx.collection-getall-url}")
	public void setCollectionGetAllUrl(String url) {
		collectionGetAllUrl = url;
	}
	@Value("${mfyx.collection-add-url}")
	public void setCollectionAddUrl(String url) {
		collectionAddUrl = url;
	}
	@Value("${mfyx.collection-delete-url}")
	public void setCollectionDeleteUrl(String url) {
		collectionDeleteUrl = url;
	}



	/**
	 * 获取指定用户的收藏数量
	 * @param userId
	 * @return {errcode:0,errmsg:"ok",cnt:}
	 */
	public static JSONObject getCnt(Integer userId) {
		String url = mfyxServerUrl + collectionGetCntUrl;
		url = url.replace("{userId}", userId+"");
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
	 * 获取指定用户的所有收藏信息
	 * @param userId
	 * @return {errcode:0,errmsg:"ok",datas:[{...},{...},...]}
	 */
	public static JSONObject getAll(Integer userId) {
		String url = mfyxServerUrl + collectionGetAllUrl;
		url = url.replace("{userId}", userId+"");
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
	 * 添加收藏信息
	 * 
	 * @param userId
	 * @param collType
	 * @param relId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject add(Integer userId,Integer collType,Long relId){
		String url = mfyxServerUrl + collectionAddUrl;
		url = url.replace("{userId}", userId+"");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("collType", collType);
		params.put("relId", relId);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}
	
	
	/**
	 * 删除收藏信息
	 * @param userId
	 * @param collType
	 * @param relId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject delete(Integer userId,Integer collType,Long relId) {
		String url = mfyxServerUrl + collectionDeleteUrl;
		url = url.replace("{userId}", userId+"");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("collType", collType);
		params.put("relId", relId);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}

}
