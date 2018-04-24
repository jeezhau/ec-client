package com.mofangyouxuan.wx.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mofangyouxuan.service.CitiesSearchService;

/**
 * 城市信息查询
 * @author jeekhan
 *
 */
@RestController
@RequestMapping("/city")
public class CitiesSearchAction {
	
	/**
	 * 获取所有省份信息
	 * @return
	 */
	@RequestMapping("/province/getall")
	public Object getAllProvinces() {
		String ret = CitiesSearchService.getProvinces();
		return ret;
	}
	
	/**
	 * 获取城市信息
	 * @return
	 */
	@RequestMapping("/city/getbyprov/{provCode}")
	public Object getCities(@PathVariable("provCode")String provCode) {
		String ret = CitiesSearchService.getCities(provCode);
		return ret;
	}
	
	/**
	 * 获取县信息
	 * @return
	 */
	@RequestMapping("/area/getbycity/{cityCode}")
	public Object getAreas(@PathVariable("cityCode")String cityCode) {
		String ret = CitiesSearchService.getAreas(cityCode);
		return ret;
	}
	
	

}
