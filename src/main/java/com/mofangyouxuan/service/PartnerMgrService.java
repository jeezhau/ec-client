package com.mofangyouxuan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 合作伙伴管理
 * @author jeekhan
 *
 */
@Component
public class PartnerMgrService {
	
	private static String mfyxServerUrl;
	private static String partnerBasicGetUrl;
	private static String partnerBasicCreateUrl;
	private static String partnerBasicUpdateUrl;
	private static String partnerChangeStatusUrl;
	private static String partnerReviewUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		PartnerMgrService.mfyxServerUrl = mfyxServerUrl;
	}
	@Value("${mfyx.partner-get-url}")
	public  void setPartnerBasicGetUrl(String partnerBasicGetUrl) {
		PartnerMgrService.partnerBasicGetUrl = partnerBasicGetUrl;
	}
	@Value("${mfyx.partner-create-url}")
	public void setPartnerBasicCreateUrl(String partnerBasicCreateUrl) {
		PartnerMgrService.partnerBasicCreateUrl = partnerBasicCreateUrl;
	}
	@Value("${mfyx.partner-update-url}")
	public  void setPartnerBasicUpdateUrl(String partnerBasicUpdateUrl) {
		PartnerMgrService.partnerBasicUpdateUrl = partnerBasicUpdateUrl;
	}
	@Value("${mfyx.partner-change-status-url}")
	public  void setPartnerChangeStatusUrl(String partnerChangeStatusUrl) {
		PartnerMgrService.partnerChangeStatusUrl = partnerChangeStatusUrl;
	}
	@Value("${mfyx.partner-review-url}")
	public static void setPartnerReviewUrl(String partnerReviewUrl) {
		PartnerMgrService.partnerReviewUrl = partnerReviewUrl;
	}
	
	/**
	 * 根据绑定用户获取合作伙伴信息
	 * @param userId
	 * @return
	 */
	public static PartnerBasic getPartner(Integer userId) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + partnerBasicGetUrl + userId);
		PartnerBasic partner = null;
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet.containsKey("id")) {//成功
				partner = JSONObject.parseObject(strRet, PartnerBasic.class);
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
	 */
	public static String create(PartnerBasic basic) {
		String url = mfyxServerUrl + partnerBasicCreateUrl;
		String strRet = HttpUtils.doPost(url, JSONObject.toJSON(basic));
		return strRet;
	}
	
	/**
	 * 更新合作伙伴
	 * @param basic
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static String update(PartnerBasic basic) {
		String url = mfyxServerUrl + partnerBasicUpdateUrl;
		String strRet = HttpUtils.doPost(url, JSONObject.toJSON(basic));
		return strRet;
	}
	
	/**
	 * 更新合作伙伴状态
	 * @param partnerId
	 * @param currUserId
	 * @return
	 */
	public static String changeStatus(Integer partnerId,Integer currUserId) {
		JSONObject json = new JSONObject();
		json.put("partnerId", partnerId);
		json.put("currUserId", currUserId);
		String url = mfyxServerUrl + partnerChangeStatusUrl;
		String strRet = HttpUtils.doPost(url, JSONObject.toJSON(json));
		return strRet;
	}
	
	/**
	 * 合作伙伴审批
	 * @param partnerId
	 * @param currUserId
	 * @param review
	 * @param result
	 * @return
	 */
	public static String review(Integer partnerId,Integer currUserId,String review,String result) {
		JSONObject json = new JSONObject();
		json.put("partnerId", partnerId);
		json.put("currUserId", currUserId);
		json.put("review", review);
		json.put("result", result);
		String url = mfyxServerUrl + partnerReviewUrl;
		String strRet = HttpUtils.doPost(url, JSONObject.toJSON(json));
		return strRet;
	}

}
