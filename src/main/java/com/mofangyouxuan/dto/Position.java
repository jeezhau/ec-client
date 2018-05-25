package com.mofangyouxuan.dto;

import java.math.BigDecimal;

public class Position {

	private String province; // 省份

	private String city; // 地址城市

	private String area; // 区县级城市

	private String town; // 镇街道

	private BigDecimal lng; // 经度

	private BigDecimal lat; // 纬度

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	
}
