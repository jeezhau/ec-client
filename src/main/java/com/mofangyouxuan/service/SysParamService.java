package com.mofangyouxuan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.utils.HttpUtils;

/**
 * 系统参数接口调用处理
 * @author jeekhan
 *
 */
@Component
public class SysParamService {
	
	private static String mfyxServerUrl;
	private static String sysParamGetAllUrl;

	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String mfyxServerUrl) {
		SysParamService.mfyxServerUrl = mfyxServerUrl;
	}

	@Value("${mfyx.sysparam-getall-url}")
	public void setSysParamGetAllUrl(String url) {
		sysParamGetAllUrl = url;
	}
	
	
	/**
	 * 获取系统所有参数
	 * @param userId
	 * @return map<param_name,param_value>
	 */
	public static Map<String,String> getAll() {
		String url = mfyxServerUrl + sysParamGetAllUrl;
		String strRet = HttpUtils.doGet(url);
		Map<String,String> map = new HashMap<String,String>();
		try {
			JSONObject jsonRet = JSONObject.parseObject(strRet);
			if(jsonRet != null && jsonRet.containsKey("datas")) {
				JSONArray datas = jsonRet.getJSONArray("datas");
				if(datas != null) {
					for(int i=0;i<datas.size();i++) {
						JSONObject obj = datas.getJSONObject(i);
						map.put(obj.getString("paramName"), obj.getString("paramValue"));
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}

