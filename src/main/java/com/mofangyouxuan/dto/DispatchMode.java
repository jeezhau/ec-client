package com.mofangyouxuan.dto;

public enum DispatchMode {
	NULL("未定义"),OFFICAL("官方统一配送"),MCHT("商家自配"),EXPRESS("快递配送"),SLEFTAKE("客户自取");
	
	private String description;
	
	private DispatchMode(String desc) {
		this.description = desc;
	}
	
	public String getDescription() {
		return this.description;
	}

}
