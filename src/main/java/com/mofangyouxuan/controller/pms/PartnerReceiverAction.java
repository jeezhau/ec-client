package com.mofangyouxuan.controller.pms;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.Receiver;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.ReceiverService;

/**
 * 收货人信息管理
 * 1、每位合作伙伴的收货人信息数量有限制；
 * 2、每位合作伙伴都有一个默认收货人信息，第一个系统自动设为默认；
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/preceiver")
@SessionAttributes({"partnerStaff","partnerBindVip","myPartner"})
public class PartnerReceiverAction {
	
	/**
	 * 获取收货人信息管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getIndex(ModelMap map) {
		PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "redirect:/partner/manage" ;
		}
		
		map.put("sys_func", "preceiver");
		return "preceiver/page-preceiver-manage";
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
		PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
		try {
			jsonRet = ReceiverService.getDefault(myPartner.getPartnerId(),"2");
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
		PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
		try {
			jsonRet = ReceiverService.getByUser(myPartner.getPartnerId(),"2");
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
	public Object setDefault(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@SessionAttribute("partnerPasswd")String partnerPasswd,
			@PathVariable("recvId")Long recvId,ModelMap map,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		try {
			//权限检查
			String errmsg;
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("preceiver")){
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}
			
			jsonRet = ReceiverService.setDefault(myPartner.getPartnerId(), recvId);
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
	public Object save(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@SessionAttribute("partnerPasswd")String partnerPasswd,
			@Valid Receiver receiver,BindingResult result,ModelMap map,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
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
			
			//权限检查
			String errmsg;
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("preceiver")){
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}
			
			receiver.setUserId(myPartner.getPartnerId());
			receiver.setRecvType("2");
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
	public Object delete(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@SessionAttribute("partnerPasswd")String partnerPasswd,
			@PathVariable("recvId")Long recvId,ModelMap map,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		try {
			//权限检查
			String errmsg;
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("preceiver")){
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					errmsg = "您没有权限执行该操作！！";
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", errmsg);
					return jsonRet;
				}
			}
			
			jsonRet = ReceiverService.delete(recvId, myPartner.getPartnerId());
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
