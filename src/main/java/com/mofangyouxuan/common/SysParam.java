package com.mofangyouxuan.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mofangyouxuan.service.SysParamService;


@Component
public class SysParam {
	
	private static Map<String,String> SYS_Param_Map = null;
	private static Long lastUpdTime = null; 	//上次更新时间
	
	public static String localServerName;
	
	@Value("${sys.local-server-url}")
	public void setLocalServerName(String serverName) {
		localServerName = serverName;
	}
	
	//系统顶级合作伙伴
	private static Integer sysParternerId = 1000;
	public static Integer getSyspartnerId() {
		return sysParternerId;
	}
	
	/**
	 * 获取系统配置参数
	 * @param paramName
	 * @return
	 */
	public static String getSysParam(String paramName) {
		Map<String,String> sysParamMap = null;
		Long curr = System.currentTimeMillis()/1000/60/60; //小时
		if(lastUpdTime == null || curr-lastUpdTime>3) {
			sysParamMap = SysParamService.getAll();
			if(sysParamMap != null) {
				lastUpdTime = curr;
				SYS_Param_Map = sysParamMap;
			}
		}
		if(SYS_Param_Map != null) {
			if(SYS_Param_Map.containsKey(paramName)) {
				return SYS_Param_Map.get(paramName);
			}
		}
		return null;
	}

}
