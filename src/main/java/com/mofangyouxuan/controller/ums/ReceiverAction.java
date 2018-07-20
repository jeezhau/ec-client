package com.mofangyouxuan.controller.ums;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Receiver;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.service.ReceiverService;

/**
 * 收货人信息管理
 * 1、每位用户的收货人信息数量有限制；
 * 2、每位用户都有一个默认收货人信息，第一个系统自动设为默认；
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/receiver")
@SessionAttributes({"userBasic"})
public class ReceiverAction {
	
	/**
	 * 获取收货人信息管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getIndex(ModelMap map) {
		
		return "user/page-receiver-manage";
	}
	
	/**
	 * 获取指定用户的默认收货人信息
	 * 
	 * @return {errcode:0,errmsg:"ok",receiver:{...}}
	 */
	@RequestMapping("/getdefault")
	@ResponseBody
	public Object getDefault(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		try {
			jsonRet = ReceiverService.getDefault(user.getUserId(),"1");
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取系统数据失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}

	/**
	 * 获取指定用户的所有收货人信息
	 * 
	 * @return {errcode:0,errmsg:"ok",datas:[{...},{...},...]}
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public Object getAllByUser(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		try {
			jsonRet = ReceiverService.getByUser(user.getUserId(),"1");
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取系统数据失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 设置默认收货人信息
	 * @param recvId
	 * @return {"errcode":0,"errmsg":"ok"}
	 */
	@RequestMapping("/setdefault/{recvId}")
	@ResponseBody
	public Object setDefault(@PathVariable("recvId")Long recvId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		try {
			jsonRet = ReceiverService.setDefault(user.getUserId(), recvId);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取系统数据失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	/**
	 * 保存新收货人信息
	 * id为0则新增，否则修改
	 * @param receiver
	 * @param result
	 * @return {errcode:0,errmsg:"ok",recvId:111}
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(@Valid Receiver receiver,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		try { 
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.RECEIVER_PARAM_ERROR);
				return jsonRet.toString();
			}
			receiver.setUserId(user.getUserId());
			receiver.setRecvType("1");
			jsonRet = ReceiverService.save(receiver);
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取系统数据失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}
	
	
	/**
	 * 删除指定ID收货人信息
	 * @param recvId
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 */
	@RequestMapping("/delete/{recvId}")
	@ResponseBody
	public Object delete(@PathVariable("recvId")Long recvId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		try {
			jsonRet = ReceiverService.delete(recvId, user.getUserId());
			if(jsonRet == null) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "获取系统数据失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toJSONString();
	}

}
