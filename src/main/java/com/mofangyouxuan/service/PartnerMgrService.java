package com.mofangyouxuan.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerSettle;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.ObjectToMap;
import com.mofangyouxuan.utils.PageCond;

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
	private static String partnerReviewUrl;
	private static String partnerChangeUpUrl;
	private static String partnerCertUploadUrl;
	private static String partnerCertShowUrl;
	private static String partnerGetAllUrl;
	
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
	@Value("${mfyx.partner-cert-upload-url}")
	public void setPartnerCertUploadUrl(String url) {
		PartnerMgrService.partnerCertUploadUrl = url;
	}
	@Value("${mfyx.partner-cert-show-url}")
	public void setPartnerCertShowUrl(String url) {
		PartnerMgrService.partnerCertShowUrl = url;
	}

	@Value("${mfyx.mypartners-review-url}")
	public void setPartnerReviewUrl(String url) {
		PartnerMgrService.partnerReviewUrl = url;
	}
	@Value("${mfyx.mypartners-change-up-url}")
	public void setPartnerChangeUpUrl(String url) {
		PartnerMgrService.partnerChangeUpUrl = url;
	}
	@Value("${mfyx.mypartners-get-all-url}")
	public void setPartnerGetAllUrl(String url) {
		PartnerMgrService.partnerGetAllUrl = url;
	}
	
	/**
	 * 根据绑定会员获取合作伙伴信息
	 * @param vipId
	 * @return {errcode,errmsg,partner,settle}
	 */
	public static JSONObject getPartnerByVip(Integer vipId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetByVipUrl + vipId);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据ID获取合作伙伴信息
	 * @param partnerId
	 * @return
	 */
	public static JSONObject getPartnerSettleById(Integer partnerId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetByIdUrl + partnerId);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据ID获取合作伙伴信息
	 * @param partnerId
	 * @return
	 */
	public static PartnerBasic getPartnerById(Integer partnerId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetByIdUrl + partnerId);
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("partner")) {
				PartnerBasic partner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
				return partner;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建合作伙伴
	 * @param basic
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String create(PartnerBasic basic,PartnerSettle settle,String passwd) throws IllegalArgumentException, IllegalAccessException {
		String url = mfyxServerUrl + partnerBasicCreateUrl;
		Map<String, Object> params1 = new HashMap<String,Object>();
		String[] excludeFields1 = {"updateTime","freviewLog","freviewOpr","freviewTime","lreviewLog","lreviewOpr","lreviewTime","status","certDir","scoreLogis","scoreServ","scoreGoods"};
		params1 = ObjectToMap.object2Map(basic,excludeFields1,false);
		params1.put("passwd", passwd);
		String[] excludeFields2 = {"serviceFeeRate","shareProfitRate"};
		Map<String, Object> params2 = new HashMap<String,Object>();
		params2 = ObjectToMap.object2Map(settle,excludeFields2,false);
		params1.putAll(params2);
		
		params1.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params1);
		return strRet;
	}
	
	/**
	 * 更新合作伙伴
	 * @param basic
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static String update(PartnerBasic basic,PartnerSettle settle,String passwd) {
		String url = mfyxServerUrl + partnerBasicUpdateUrl;
		Map<String, Object> params1 = new HashMap<String,Object>();
		String[] excludeFields1 = {"updateTime","freviewLog","freviewOpr","freviewTime","lreviewLog","lreviewOpr","lreviewTime","status","certDir","scoreLogis","scoreServ","scoreGoods"};
		params1 = ObjectToMap.object2Map(basic,excludeFields1,false);
		params1.put("passwd", passwd);
		String[] excludeFields2 = {"serviceFeeRate","shareProfitRate"};
		Map<String, Object> params2 = new HashMap<String,Object>();
		params2 = ObjectToMap.object2Map(settle,excludeFields2,false);
		params1.putAll(params2);
		
		params1.put("passwd", passwd);
		String strRet = HttpUtils.doPost(url, params1);
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
	
	/**
	 * 变更上级合作伙伴
	 * 1、合作伙伴上级变更其下级；
	 * 2、顶级合作伙伴为其变更；
	 * @param partnerId	被操作的合作伙伴ID
	 * @param newUpId	新的上级ID
	 * @param oldUpId	旧的上级ID
	 * @param oprPartnerId		操作者合作伙伴
	 * @param operator	操作者
	 * @param passwd
	 * @return
	 * @return {errcode:0,errmsg:'ok'}
	 */
	public static JSONObject changeUp(Integer partnerId,Integer newUpId,
			Integer oprPartnerId,Integer operator,String passwd) {
		Map<String,Object> params = new HashMap<String,Object>();
		JSONObject jsonRet = new JSONObject();
		try {
			params.put("partnerId", partnerId);
			params.put("newUpId", newUpId);
			params.put("oprPartnerId", oprPartnerId);
			params.put("operator", operator);
			params.put("passwd", passwd);
			String url = mfyxServerUrl + partnerChangeUpUrl;
			String strRet = HttpUtils.doPost(url, params);
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
	 * 合作伙伴审核与抽查
	 * 上级审批下级信息
	 * 1、上级可对下级进行审核；
	 * 2、顶级对所有合作伙伴进行最终审核；
	 * 3、仅顶级审核通过后才算通过；
	 * @param partnerId	待审批合作伙伴ID
	 * @param review 	审批意见
	 * @param result 	审批结果：S-通过，R-拒绝
	 * @param rewPartnerId	审核者合作伙伴ID
	 * @param operator	审批人ID，为上级合作伙伴的员工用户ID
	 * @param passwd		审批人操作密码
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException
	 */
	public static JSONObject review(Integer partnerId,String review,String result,
			Integer rewPartnerId,Integer operator,String passwd){
		Map<String,Object> params = new HashMap<String,Object>();
		JSONObject jsonRet = new JSONObject();
		try {
			params.put("partnerId", partnerId);
			params.put("review", review);
			params.put("result", result);
			params.put("rewPartnerId", rewPartnerId);
			params.put("operator", operator);
			params.put("passwd", passwd);
			String url = mfyxServerUrl + partnerReviewUrl;
			String strRet = HttpUtils.doPost(url, params);
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
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{partnerId,pbTp,upPartnerId,country,province,city,area,busiName,legalPername,legalPeridno,compType,compName,licenceNo,phone,status,beginUpdateTime,endUpdateTime}
	 * @param jsonPageCond		分页信息:{begin, pageSize}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	public static JSONObject getAll(Integer upPartnerId,JSONObject search,PageCond pageCond) {
		String url = mfyxServerUrl + partnerGetAllUrl;
		url = url.replace("{upPartnerId}", upPartnerId +"");
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

}
