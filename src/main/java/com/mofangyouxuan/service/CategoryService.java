package com.mofangyouxuan.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.ObjectToMap;

/**
 * 合作伙伴管理
 * @author jeekhan
 *
 */
@Component
public class CategoryService {
	
	private static String tmpFileDir;
	private static String mfyxServerUrl;
	private static String categoryAddUrl;
	private static String categoryUpdateUrl;
	private static String categoryImgUploadUrl;
	private static String categoryImgShowUrl;
	private static String categoryGetAllUrl;
	
	@Value("${sys.tmp-file-dir}")
	public void setTmpFileDir(String url) {
		tmpFileDir = url;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		CategoryService.mfyxServerUrl = mfyxServerUrl;
	}
	@Value("${mfyx.category-add-url}")
	public  void setAategoryAddUrl(String url) {
		CategoryService.categoryAddUrl = url;
	}
	@Value("${mfyx.category-update-url}")
	public  void setCategoryUpdateUrlUrl(String url) {
		CategoryService.categoryUpdateUrl = url;
	}	
	@Value("${mfyx.category-upload-img}")
	public void setCategoryImgUploadUrl(String url) {
		CategoryService.categoryImgUploadUrl = url;
	}
	@Value("${mfyx.category-show-img}")
	public  void setCategoryImgShowUrl(String url) {
		CategoryService.categoryImgShowUrl = url;
	}
	@Value("${mfyx.category-getall-url}")
	public  void setCategoryGetAllUrl(String url) {
		CategoryService.categoryGetAllUrl = url;
	}
	
	
	/**
	 * 新增分类
	 * @param cat
	 * @param operator
	 * @param passwd
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject create(Category cat,Integer operator,String passwd) {
		String url = mfyxServerUrl + categoryAddUrl;
		JSONObject jsonRet = new JSONObject();
		try {
			Map<String, Object> params1 = new HashMap<String,Object>();
			String[] excludeFields1 = {"updateTime","imgPath"};
			params1 = ObjectToMap.object2Map(cat,excludeFields1,false);
			params1.put("passwd", passwd);
			params1.put("operator", operator);
			String strRet = HttpUtils.doPost(url, params1);
			jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errmsg", "系统错误：" + strRet);
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errmsg", "系统异常：" + e.getMessage());
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
		}
		return jsonRet;
	}
	
	/**
	 * 更新分类
	 * @param basic
	 * @param cat
	 * @param operator
	 * @param passwd
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject update(Category cat,Integer operator,String passwd) {
		String url = mfyxServerUrl + categoryUpdateUrl;
		JSONObject jsonRet = new JSONObject();
		try {
			Map<String, Object> params1 = new HashMap<String,Object>();
			String[] excludeFields1 = {"updateTime","imgPath"};
			params1 = ObjectToMap.object2Map(cat,excludeFields1,false);
			params1.put("passwd", passwd);
			params1.put("operator", operator);
			String strRet = HttpUtils.doPost(url, params1);
			jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errmsg", "系统错误：" + strRet);
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errmsg", "系统异常：" + e.getMessage());
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
		}
		return jsonRet;
	}
	
	
	/**
	 * 分类图片上传
	 * @param catId
	 * @param imageFile
	 * @param operator
	 * @param passwd
	 * @return {errcode:0,errmsg:""}
	 */
	public static JSONObject uploadCert(Integer catId,File imageFile,Integer operator,String passwd) {
		String url = mfyxServerUrl + categoryImgUploadUrl;
		url = url.replace("{catId}", catId+"");
		JSONObject jsonRet = new JSONObject();
		try {
			Map<String,String> paramPairs = new HashMap<String,String>();
			paramPairs.put("operator", operator+"");
			paramPairs.put("passwd", passwd);
			String strRet = HttpUtils.uploadFile(url, imageFile, "image", paramPairs);
			jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errmsg", "系统错误：" + strRet);
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errmsg", "系统异常：" + e.getMessage());
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
		}
		return jsonRet;
	}
	
	/**
	 * 获取分类图片
	 * @param catId
	 * @return file
	 */
	public static File showImg(Integer catId) {
		String url = mfyxServerUrl + categoryImgShowUrl;
		url = url.replace("{catId}", catId+"");
		File file = HttpUtils.downloadFile(tmpFileDir,url);
		return file;
	}
	
	
	/**
	 * 查询指定查询条件的信息；
	 * @param jsonSearchParams	查询条件:{categoryName,keywords,isCwide,statue,parentId}
	 * @return {errcode:0,errmsg:"ok",categories:[{}...]} 
	 */
	public static List<Category> getAll(JSONObject search) {
		String url = mfyxServerUrl + categoryGetAllUrl;
		Map<String, Object> params = new HashMap<String,Object>();
		if(search == null) {
			search = new JSONObject();
		}
		params.put("jsonSearchParams", search.toJSONString());
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("categories")) {
				return JSONArray.parseArray(jsonRet.getJSONArray("categories").toJSONString(), Category.class);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
