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
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.ObjectToMap;
import com.mofangyouxuan.utils.PageCond;

/**
 * 合作伙伴员工服务调用处理
 * @author jeekhan
 *
 */
@Component
public class PartnerStaffService {
	private static String tmpFileDir;
	private static String mfyxServerUrl;
	private static String pstaffGetAllUrl;
	private static String pstaffSaveUrl;
	private static String pstaffDeleteUrl;
	private static String pstaffUpdatePwdUrl;
	private static String pstaffResetPwdUrl;
	private static String pstaffUploadUrl;
	private static String pstaffShowUrl;
	
	@Value("${sys.tmp-file-dir}")
	public void setTmpFileDir(String url) {
		tmpFileDir = url;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		PartnerStaffService.mfyxServerUrl = mfyxServerUrl;
	}

	@Value("${mfyx.pstaff-getall-url}")
	public void setPstaffGetAllUrl(String url) {
		pstaffGetAllUrl = url;
	}
	@Value("${mfyx.pstaff-save-url}")
	public void setPstaffSaveUrl(String url) {
		pstaffSaveUrl = url;
	}
	@Value("${mfyx.pstaff-delete-url}")
	public void setPstaffDeleteUrl(String url) {
		pstaffDeleteUrl = url;
	}
	@Value("${mfyx.pstaff-updatepwd-url}")
	public void setPstaffUpdatePwdUrl(String url) {
		pstaffUpdatePwdUrl = url;
	}
	@Value("${mfyx.pstaff-resetpwd-url}")
	public void setPstaffResetPwdUrl(String url) {
		pstaffResetPwdUrl = url;
	}
	@Value("${mfyx.pstaff-upload-url}")
	public void setPstaffuploadUrl(String url) {
		pstaffUploadUrl = url;
	}
	@Value("${mfyx.pstaff-show-url}")
	public void setPstaffShowUrl(String url) {
		pstaffShowUrl = url;
	}

	/**
	 * 保存用户的订单投诉信息
	 * @param partnerId
	 * @param staff
	 * @param oprId
	 * @param passwd
	 * @return {errcode,errmsg}
	 */
	public static JSONObject saveStaff(Integer partnerId,PartnerStaff staff,String passwd) {
		String url = mfyxServerUrl + pstaffSaveUrl;
		url = url.replace("{partnerId}", partnerId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"updateTime","status"};
		params = ObjectToMap.object2Map(staff,excludeFields,false);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	

	/**
	 * 删除合作伙伴的员工
	 * @param partnerId
	 * @param passwd
	 * @param userId
	 * @return {errcode,errmsg}
	 */
	public static JSONObject deleteStaff(Integer partnerId,String passwd,Integer userId) {
		String url = mfyxServerUrl + pstaffDeleteUrl;
		url = url.replace("{partnerId}", partnerId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	

	/**
	 * 重设员工的密码
	 * @param partnerId
	 * @param rootPasswd
	 * @param userId
	 * @param newPasswd
	 * @return {errcode,errmsg}
	 */
	public static JSONObject resetPwd(Integer partnerId,String rootPasswd,
			Integer userId,String newPasswd) {
		String url = mfyxServerUrl + pstaffResetPwdUrl;
		url = url.replace("{partnerId}", partnerId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("rootPasswd", rootPasswd);
		params.put("newPasswd", newPasswd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
	/**
	 * 修改密码
	 * @param partnerId
	 * @param oldPasswd
	 * @param userId
	 * @param newPasswd
	 * @return {errcode,errmsg}
	 */
	public static JSONObject updatePwd(Integer partnerId,String oldPasswd,
			Integer userId,String newPasswd) {
		String url = mfyxServerUrl + pstaffUpdatePwdUrl;
		url = url.replace("{partnerId}", partnerId +"");
	
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		params.put("oldPasswd", oldPasswd);
		params.put("newPasswd", newPasswd);
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
		
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{recId,userId,staffId,nickname,email,phone,isKf,tagId,updateOpr,status,beginUpdateTime,endUpdateTime}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject getPartnersAll(Integer partnerId,JSONObject search,PageCond pageCond) {
		String url = mfyxServerUrl + pstaffGetAllUrl;
		url = url.replace("{partnerId}", partnerId +"");
		Map<String, Object> params = new HashMap<String,Object>();
		if(search == null) {
			search = new JSONObject();
		}
		if(pageCond == null) {
			pageCond = new PageCond(0,30);
		}
		params.put("jsonSearchParams", search.toJSONString());
		params.put("jsonPageCond", JSONObject.toJSONString(pageCond));
		String strRet = HttpUtils.doPost(url, params);
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
	public static PartnerStaff getStaffByUserId(Integer partnerId,Integer userId) {
		try {
			JSONObject search = new JSONObject();
			search.put("userId", userId);
			PageCond pageCond = new PageCond(0,1);
			JSONObject jsonRet = getPartnersAll(partnerId,search,pageCond);
			if(jsonRet.containsKey("datas")) {
				List<PartnerStaff> list = JSONArray.parseArray(jsonRet.getJSONArray("datas").toJSONString(), PartnerStaff.class);
				if(list.size()>0) {
					return list.get(0);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 照片上传
	 * 
	 * @param partnerId
	 * @param imageFile
	 * @param mode		照片类型：headimg，kfqrcode
	 * @param userId		员工的系统ID
	 * @param passwd		员工的操作密码
	 * @return {errcode,errmsg,filename}
	 */
	public static JSONObject uploadImg(Integer partnerId,File imageFile,String mode,Integer userId,String passwd) {
		String url = mfyxServerUrl + pstaffUploadUrl;
		url = url.replace("{partnerId}", partnerId +"");
		url = url.replace("{mode}", mode);
		Map<String,String> paramPairs = new HashMap<String,String>();
		paramPairs.put("userId", "" + userId);
		paramPairs.put("passwd", "" + passwd);
		String strRet = HttpUtils.uploadFile(url, imageFile, "image", paramPairs);;
		JSONObject jsonRet = new JSONObject();
		try {
			jsonRet = JSONObject.parseObject(strRet);
			if(!jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "出现系统访问错误，错误信息：" + strRet);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}

	/**
	 * 显示照片
	 * @param partnerId
	 * @param mode
	 * @param userId
	 * @return file
	 */
	public static File showImg(Integer partnerId,String mode,Integer userId) {
		String url = mfyxServerUrl + pstaffShowUrl;
		url = url.replace("{partnerId}", partnerId +"");
		url = url.replace("{userId}", userId + "");
		url = url.replace("{mode}", mode);
		File file = HttpUtils.downloadFile(tmpFileDir,url);
		return file;
	}

}
