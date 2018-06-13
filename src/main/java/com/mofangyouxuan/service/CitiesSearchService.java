package com.mofangyouxuan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mofangyouxuan.utils.HttpUtils;

/**
 * 查询中国的城市信息：包括省、市、县 三级
 * @author jeekhan
 *
 */
@Component
public class CitiesSearchService {
	
	private static String mfyxServerUrl;
	private static String cityGetCitiesUrl;
	private static String cityGetProincesUrl;
	private static String cityGetAreasUrl;
	
	@Value("${mfyx.mfyx-server-url}")
	public void setMfyxServerUrl(String url) {
		mfyxServerUrl = url;
	}
	@Value("${mfyx.city-get-provinces-url}")
	public void setCityGetProincesUrl(String url) {
		cityGetProincesUrl = url;
	}
	@Value("${mfyx.city-get-cities-url}")
	public void setCityGetCitiesUrl(String url) {
		cityGetCitiesUrl = url;
	}
	@Value("${mfyx.city-get-areas-url}")
	public void setCityGetAreasUrl(String url) {
		cityGetAreasUrl = url;
	}
	 
			  
	/**
	 * 获取所有省份信息
	 * @return
	 */
	public static String getProvinces() {
		String strRet = HttpUtils.doGet(mfyxServerUrl + cityGetProincesUrl);
		return strRet;
	}

	/**
	 * 获取指定省份下的所有地级市
	 * @param provCode
	 * @return
	 */
	public static String getCities(String provCode) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + cityGetCitiesUrl + provCode);
		return strRet;
	}
	
	/**
	 * 获取指定地级市下的所有县
	 * @param cityCode
	 * @return
	 */
	public static String getAreas(String cityCode) {
		String strRet = HttpUtils.doGet(mfyxServerUrl + cityGetAreasUrl + cityCode);
		return strRet;
	}
}
