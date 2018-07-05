package com.mofangyouxuan.controller.pms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.ComplainService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 合作伙伴投诉与处理管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/pcomplain")
public class PartnerComplainAction {
	
	/**
	 * 系统投诉管理主页
	 * 1、mode-sys:主要用于投诉处理与回访；
	 * 2、mode-partner:主要用于合作伙伴投诉上级；
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/{mode}/manage")
	public String getManage(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("mode")String mode,
			HttpSession session,ModelMap map) throws UnsupportedEncodingException {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		String errmsg = null;
		//权限检查
		if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
			errmsg = "您的合作伙伴未处于正常状态！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		if("sys".equals(mode) ) {
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue())))){
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
		}else if("partner".equals(mode)) {
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.complain4p.getValue())))){
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
		}else {
			errmsg = "系统中没有功能！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		map.put("mode", mode);
		if("sys".equals(mode) ) {
			map.put("sys_func", "complain");
		}else {
			map.put("sys_func", "complain4p");
		}
		return "pcomplain/page-pcomplain-manage";
	}
	
	
	/**
	 * 获取投诉信息的详情
	 * 1、可用于添加处理与回访信息；
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/show/{mode}/{logId}")
	public String showComplain(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("logId")Integer logId,@PathVariable("mode")String mode,
			HttpSession session,ModelMap map) throws UnsupportedEncodingException {
		if(!"detail".equals(mode) && !"deal".equals(mode) && !"revisit".equals(mode)) {
			mode = "detail";
		}
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		//权限检查
		String errmsg = null;
		if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
			errmsg = "您的合作伙伴未处于正常状态！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		if("deal".equals(mode) || "revisit".equals(mode)) {
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue())))){
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
		}else if("detail".equals(mode)) {
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.complain4p.getValue())))){
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
		}else {
			errmsg = "系统中没有功能！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		map.put("logId", logId);
		map.put("mode", mode);
		return "pcomplain/page-pcomplain-show";
	}
	
	/**
	 * 保存投诉处理信息
	 * 1、系统合作伙伴员工完成该处理；
	 * @return {errmsg,errcode}
	 */
	@RequestMapping(value="/deal",method=RequestMethod.POST)
	@ResponseBody
	public Object dealComplain(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="logId",required=true)Integer logId,
			@RequestParam(value="content",required=true)String content,
			HttpSession session,ModelMap map) {
		VipBasic vip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		JSONObject jsonRet = new JSONObject();
		try {
			if(content.length()>1000) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "处理结果：最长1000字符！");
				return jsonRet.toString();
			}
			//数据检查
			if(myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			
			Integer updateOpr = null;
			//权限检查
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			if("staff".equals(partnerUserTP)) {
				if(partnerStaff == null || partnerStaff.getTagList() == null || 
					!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue())){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				updateOpr = vip.getVipId();
			}
			jsonRet = ComplainService.dealComplain(updateOpr, partnerPasswd, logId, content);
			
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 保存投诉回访信息
	 * 1、系统合作伙伴员工完成该处理；
	 * @return {errmsg,errcode}
	 */
	@RequestMapping(value="/revisit",method=RequestMethod.POST)
	@ResponseBody
	public Object revisitComplain(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="logId",required=true)Integer logId,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="result",required=true)String result,
			HttpSession session,ModelMap map) {
		VipBasic vip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		JSONObject jsonRet = new JSONObject();
		try {
			if(content.length()>1000) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "回访内容：最长1000字符！");
				return jsonRet.toString();
			}
			if(!"0".equals(result) && !"2".equals(result)) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "回访结果：取值为【0-须再处理，2-完成】！");
				return jsonRet.toString();
			}
			//数据检查
			if(myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			
			Integer updateOpr = null;
			//权限检查
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			if("staff".equals(partnerUserTP)) {
				if(partnerStaff == null || partnerStaff.getTagList() == null || 
					!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue())){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				updateOpr = vip.getVipId();
			}
			
			jsonRet = ComplainService.revisitComplain(updateOpr, partnerPasswd, logId, content,result);
			
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 获取投诉信息的详情
	 * 1、可用于添加处理与回访信息；
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/edit/{cplanId}")
	public String editComplain4P(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("cplanId")Integer cplanId,
			@RequestParam("oprFlag")String oprFlag,
			HttpSession session,ModelMap map) throws UnsupportedEncodingException {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		//权限检查
		String errmsg = null;
		if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
			errmsg = "您的合作伙伴未处于正常状态！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		if("staff".equals(partnerUserTP) &&  (partnerStaff == null || partnerStaff.getTagList() == null || 
				!partnerStaff.getTagList().contains(PartnerStaff.TAG.complain4p.getValue()))){
			errmsg = "您没有权限执行该操作！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		map.put("oprFlag", oprFlag);
		map.put("cplanId", cplanId);
		map.put("sys_func", "complain4p");
		return "pcomplain/page-pcomplain-edit";
	}


	/**
	 * 保存用户的合作伙伴投诉信息
	 * 1、合作伙伴投诉上级
	 * @return {errmsg,errcode}
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public Object saveComplain4P(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="partnerId",required=true)Integer partnerId,
			@RequestParam(value="content",required=true)String content,
			@RequestParam(value="cplanId",required=true)Integer cplanId,
			@RequestParam(value="phone",required=true)String phone,
			HttpSession session,ModelMap map ) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		try {
			if(partnerId < 1) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "订单ID不正确！");
				return jsonRet.toString();
			}
			content = content.trim();
			if(content.length()<10 || content.length()>1000) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉内容长度为10-1000位字符！");
				return jsonRet.toString();
			}
			if(cplanId <0) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			Integer updateOpr = null;
			//权限检查
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			if("staff".equals(partnerUserTP)) {
				if(partnerStaff == null || partnerStaff.getTagList() == null || 
					!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue())){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				updateOpr = vip.getVipId();
			}
			jsonRet = ComplainService.savePartnerComplain(myPartner.getPartnerId(), cplanId, content, partnerId, phone,updateOpr,partnerPasswd);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 删除用户的合作伙伴投诉信息
	 * 
	 * @param cplanId
	 * @return
	 */
	@RequestMapping(value="/delete/{cplanId}",method=RequestMethod.POST)
	@ResponseBody
	public Object deleteComplain4P(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@PathVariable(value="cplanId",required=true)Integer cplanId,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		try {
			if(cplanId < 1) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "投诉ID不正确！");
				return jsonRet.toString();
			}
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			Integer updateOpr = null;
			//权限检查
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			if("staff".equals(partnerUserTP)) {
				if(partnerStaff == null || partnerStaff.getTagList() == null || 
					!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue())){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				updateOpr = vip.getVipId();
			}
			jsonRet = ComplainService.deletePartnerComplain(myPartner.getPartnerId(), cplanId,updateOpr,partnerPasswd);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 获取所有投诉信息
	 * 
	 * @param searchParams	查询参数{cplanId,goodsId, partnerId,status,phone,beginCreateTime,endCreateTime,beginDealTime,endDealTime,beginRevisitTime,endRevisitTime}
	 * @param sortsParams	排序参数{createTime:"N#0/1",dealTime:"N#0/1",revisitTime:"N#0/1"}
	 * @param pageCond	分页信息{begin:, pageSize:}
	 * @return
	 */
	@RequestMapping(value="/{mode}/getall",method=RequestMethod.POST)
	@ResponseBody
	public Object getAll(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("mode")String mode,
			String searchParams,String sortsParams,
			PageCond pageCond,HttpSession session) {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = JSONObject.parseObject(searchParams);
			if(search == null) {
				search = new JSONObject();
			}
			if(myPartner == null || (!"S".equals(myPartner.getStatus()) && !"C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您的合作伙伴未处于正常状态！！");
				return jsonRet;
			}
			
			if("sys".equals(mode)) {
				if("staff".equals(partnerUserTP) && (partnerStaff == null || 
						(!partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainDeal.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue()))) ){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该该操作！");
					return jsonRet;
				}
			}else if("partner".equals(mode)) {
				if("staff".equals(partnerUserTP) && (partnerStaff == null || 
						(!partnerStaff.getTagList().contains(PartnerStaff.TAG.complain4p.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.ComplainRevisit.getValue()))) ){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该该操作！");
					return jsonRet;
				}
				search.put("cpType", "2");
				search.put("oprPid", myPartner.getPartnerId());
			}else {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "系统中没有该功能！");
				return jsonRet;
			}
			JSONObject sorts = JSONObject.parseObject(sortsParams);
			if(sorts == null) {
				sorts = new JSONObject();
				sorts.put("createTime", "1#1");
			}
			jsonRet = ComplainService.getAll(search,sorts, pageCond);
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	
}

