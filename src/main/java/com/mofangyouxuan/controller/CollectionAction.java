package com.mofangyouxuan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.service.CollectionService;

@Controller
@RequestMapping("/collection")
@SessionAttributes({"userBasic"})
public class CollectionAction {
	
	/**
	 * 获取用户收藏管理主页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getMgrIndex(ModelMap map) {
		
		return "user/page-collection-manage";
	}
	
	/**
	 * 添加收藏
	 * @param collType
	 * @param relId
	 * @param map
	 * @return {errcode,errmsg}
	 */
	@RequestMapping("/add/{collType}/{relId}")
	@ResponseBody
	public String add(@PathVariable("collType")Integer collType,
			@PathVariable("relId")Long relId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic userBasic = (UserBasic) map.get("userBasic");
			if(collType != 1 && collType != 2) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "收藏类型：取值不正确！" );
			}
			jsonRet = CollectionService.add(userBasic.getUserId(), collType, relId);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "系统访问出现异常！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 删除收藏
	 * @param collType
	 * @param relId
	 * @param map
	 * @return {errcode,errmsg}
	 */
	@RequestMapping("/delete/{collType}/{relId}")
	@ResponseBody
	public String delete(@PathVariable("collType")Integer collType,
			@PathVariable("relId")Long relId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic userBasic = (UserBasic) map.get("userBasic");
			if(collType != 1 && collType != 2) {
				jsonRet.put("errcode", -1);
				jsonRet.put("errmsg", "收藏类型：取值不正确！" );
			}
			jsonRet = CollectionService.delete(userBasic.getUserId(), collType, relId);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "系统访问出现异常！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 统计所有收藏收藏
	 * 
	 * @return {errcode,errmsg,cnt}
	 */
	@RequestMapping("/count")
	@ResponseBody
	public Object countAll(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic userBasic = (UserBasic) map.get("userBasic");
			jsonRet = CollectionService.getCnt(userBasic.getUserId());
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "系统访问出现异常！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
	/**
	 * 获取所有收藏收藏
	 * 
	 * @return {errcode,errmsg,datas:[{...},...]}
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public Object getAll(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic userBasic = (UserBasic) map.get("userBasic");
			jsonRet = CollectionService.getAll(userBasic.getUserId());
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "系统访问出现异常！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}

}
