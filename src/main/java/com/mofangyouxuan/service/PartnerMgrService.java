package com.mofangyouxuan.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.ObjectToMap;

/**
 * 合作伙伴管理
 * @author jeekhan
 *
 */
@Component
public class PartnerMgrService {
	
	private static String tmpFileDir;
	private static String mfyxServerUrl;
	private static String partnerBasicGetByVipUrl;
	private static String partnerBasicGetByIdUrl;
	private static String partnerBasicCreateUrl;
	private static String partnerBasicUpdateUrl;
	private static String partnerChangeStatusUrl;
	//private static String partnerReviewUrl;
	private static String partnerCertUploadUrl;
	private static String partnerCertShowUrl;
	
	@Value("${sys.tmp-file-dir}")
	public void setTmpFileDir(String url) {
		tmpFileDir = url;
	}
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		PartnerMgrService.mfyxServerUrl = mfyxServerUrl;
	}
	@Value("${mfyx.partner-get-byvip-url}")
	public  void setPartnerBasicGetByVipUrl(String url) {
		PartnerMgrService.partnerBasicGetByVipUrl = url;
	}
	@Value("${mfyx.partner-get-byid-url}")
	public  void setPartnerBasicGetByIdUrl(String url) {
		PartnerMgrService.partnerBasicGetByIdUrl = url;
	}	
	@Value("${mfyx.partner-create-url}")
	public void setPartnerBasicCreateUrl(String url) {
		PartnerMgrService.partnerBasicCreateUrl = url;
	}
	@Value("${mfyx.partner-update-url}")
	public  void setPartnerBasicUpdateUrl(String url) {
		PartnerMgrService.partnerBasicUpdateUrl = url;
	}
	@Value("${mfyx.partner-change-status-url}")
	public  void setPartnerChangeStatusUrl(String url) {
		PartnerMgrService.partnerChangeStatusUrl = url;
	}
//	@Value("${mfyx.partner-review-url}")
//	public void setPartnerReviewUrl(String url) {
//		PartnerMgrService.partnerReviewUrl = url;
//	}
	@Value("${mfyx.partner-cert-upload-url}")
	public void setPartnerCertUploadUrl(String url) {
		PartnerMgrService.partnerCertUploadUrl = url;
	}
	@Value("${mfyx.partner-cert-show-url}")
	public void setPartnerCertShowUrl(String url) {
		PartnerMgrService.partnerCertShowUrl = url;
	}

	/**
	 * 根据绑定会员获取合作伙伴信息
	 * @param vipId
	 * @return
	 */
	public static PartnerBasic getPartnerByVip(Integer vipId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetByVipUrl + vipId);
		PartnerBasic partner = null;
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("partner")) {//成功
				partner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
				return partner;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return partner;
	}
	
	/**
	 * 根据ID获取合作伙伴信息
	 * @param partnerId
	 * @return
	 */
	public static PartnerBasic getPartnerById(Integer partnerId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetByIdUrl + partnerId);
		PartnerBasic partner = null;
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("partner")) {//成功
				partner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
				return partner;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return partner;
	}

	/**
	 * 创建合作伙伴
	 * @param basic
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String create(PartnerBasic basic,String passwd) throws IllegalArgumentException, IllegalAccessException {
		String url = mfyxServerUrl + partnerBasicCreateUrl;
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"updateTime","reviewLog","reviewOpr","reviewTime","status","certDir","scoreLogis","scoreServ","scoreGoods"};
		params = ObjectToMap.object2Map(basic,excludeFields,false);
		params.put("passwd", passwd);
	
		String strRet = HttpUtils.doPost(url, params);
		return strRet;
	}
	
	/**
	 * 更新合作伙伴
	 * @param basic
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static String update(PartnerBasic basic,String passwd) {
		String url = mfyxServerUrl + partnerBasicUpdateUrl;
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {"updateTime","reviewLog","reviewOpr","reviewTime","status","certDir","scoreLogis","scoreServ","scoreGoods"};
		params = ObjectToMap.object2Map(basic,excludeFields,false);
		params = ObjectToMap.object2Map(basic,excludeFields,false);
		params.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params);
		return strRet;
	}
	
	/**
	 * 更新合作伙伴状态:主动关店与开店
	 * @param partnerId
	 * @param currUserId
	 * @return {errcode:0,errmsg:'ok'}
	 */
	public static String changeStatus(Integer partnerId,Integer currUserId,String passwd) {
		JSONObject json = new JSONObject();
		json.put("partnerId", partnerId);
		json.put("currUserId", currUserId);
		json.put("passwd", passwd);
		String url = mfyxServerUrl + partnerChangeStatusUrl;
		String strRet = HttpUtils.doPost(url, json.toString());
		return strRet;
	}
	
	
	/**
	 * 证件照片上传
	 * @param bindVipId
	 * @param imageFile
	 * @param certType
	 * @param userId
	 * @return {errcode:0,errmsg:""}
	 */
	public static String uploadCert(Integer bindVipId,File imageFile,String certType,Integer userId,String passwd) {
		String url = mfyxServerUrl + partnerCertUploadUrl;
		Map<String,String> paramPairs = new HashMap<String,String>();
		paramPairs.put("certType", certType);
		paramPairs.put("bindVipId", "" + bindVipId);
		paramPairs.put("userId", userId+"");
		paramPairs.put("passwd", passwd);
		String strRet = HttpUtils.uploadFile(url, imageFile, "image", paramPairs);
		return strRet;
	}
	
	/**
	 * 获取证件照
	 * @param certType
	 * @param userId
	 * @return [InputSteam,filename]
	 */
	public static File showCert(Integer partnerId,String certType) {
		String url = mfyxServerUrl + partnerCertShowUrl + partnerId + "/" + certType;
		File file = HttpUtils.downloadFile(tmpFileDir,url);
		return file;
	}

}
