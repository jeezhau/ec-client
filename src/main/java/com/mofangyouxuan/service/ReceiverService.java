package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.dto.Receiver;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.ObjectToMap;

/**
 * 收货人信息接口调用处理
 * @author jeekhan
 *
 */
@Component
public class ReceiverService {
	
	private static String mfyxServerUrl;
	private static String receiverGetDefaultUrl;
	private static String receiverGetByUserUrl;
	private static String receiverSaveUrl;
	private static String receiverDeleteUrl;
	private static String receiverSetDefaultUrl;

	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		ReceiverService.mfyxServerUrl = mfyxServerUrl;
	}
	@Value("${mfyx.receiver-getdefault-url}")
	public void setReceiverGetDefaultUrl(String url) {
		receiverGetDefaultUrl = url ;
	}
	@Value("${mfyx.receiver-getbyuser-url}")
	public void setReceiverGetByUserUrl(String url) {
		receiverGetByUserUrl = url;
	}
	@Value("${mfyx.receiver-save-url}")
	public void setReceiverSaveUrl(String url) {
		receiverSaveUrl = url;
	}
	@Value("${mfyx.receiver-delete-url}")
	public void setReceiverDeleteUrl(String url) {
		receiverDeleteUrl = url;
	}
	@Value("${mfyx.receiver-setdefault-url}")
	public void setReceiverSetDefaultUrl(String url) {
		receiverSetDefaultUrl = url;
	}


	/**
	 * 获取指定用户的默认收货人信息
	 * @param userId
	 * @return {errcode:0,errmsg:"ok",receiver:{...}}
	 */
	public static JSONObject getDefault(Integer userId) {
		String url = mfyxServerUrl + receiverGetDefaultUrl;
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
	 * 获取指定用户的所有收货人信息
	 * @param userId
	 * @return {errcode:0,errmsg:"ok",datas:[{...},{...},...]}
	 */
	public static JSONObject getByUser(Integer userId) {
		String url = mfyxServerUrl + receiverGetByUserUrl;
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
	 * 添加收货人信息
	 * @param redeiver
	 * @return {errcode:0,errmsg:"ok",recvId:111}
	 * 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static JSONObject save(Receiver receiver) throws IllegalArgumentException, IllegalAccessException {
		String url = mfyxServerUrl + receiverSaveUrl;
		url = url.replace("{userId}", receiver.getUserId()+"");
		Map<String, Object> params = new HashMap<String,Object>();
		String[] excludeFields = {};
		params = ObjectToMap.object2Map(receiver,excludeFields,false);
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
	 * 设置默认收货人信息
	 * @param recvId
	 * @return {"errcode":0,"errmsg":"ok"}
	 */
	public static JSONObject setDefault(Integer userId,Long recvId) {
		String url = mfyxServerUrl + receiverSetDefaultUrl;
		url = url.replace("{userId}", userId+"");
		url = url.replace("{recvId}", recvId+"");
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
	 * 删除指定ID收货人信息
	 * @param recvId
	 * @param userId
	 * @return {errcode:0,errmsg:"ok"}
	 */
	public static JSONObject delete(Long recvId,Integer userId) {
		String url = mfyxServerUrl + receiverDeleteUrl;
		url = url.replace("{userId}", userId+"");
		url = url.replace("{recvId}", recvId+"");
		String strRet = HttpUtils.doGet(url);
		JSONObject jsonRet = null;
		try {
			jsonRet = JSONObject.parseObject(strRet);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jsonRet;
	}

}
