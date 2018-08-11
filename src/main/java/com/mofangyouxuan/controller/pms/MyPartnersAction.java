package com.mofangyouxuan.controller.pms;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerSettle;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.utils.PageCond;

/**
 * “我”推广的合作伙伴
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/mypartners")
@SessionAttributes({"SYS_PARTNERID"})
public class MyPartnersAction {
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	
	/**
	 * 获取的推广合作伙伴管理
	 * @return
	 */
	@RequestMapping("/manage")
	public String getManage(@SessionAttribute("partnerUserTP")String partnerUserTP,
			HttpSession session,ModelMap map) {
		
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "redirect:/partner/manage" ;
		}
		map.put("myPartner", myPartner);
		map.put("sys_func", "mypartners");
		return "mypartners/page-mypartners-manage";
		
	}
	
	/**
	 * 获取合作信息查看页面
	 * @return
	 */
	@RequestMapping("/detail/{partnerId}/{mode}")
	public String showPartner(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("partnerId")Integer partnerId,@PathVariable("mode")String mode,
			HttpSession session,ModelMap map) {
		
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		map.put("sys_func", "mypartners");
		//权限检查
		if("staff".equals(partnerUserTP)) {
			if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
					|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("mypartners")){
				map.put("errmsg", "您没有权限执行该操作！！");
				return "partner/page-partner-detail";
			}
		}else {
			if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
				map.put("errmsg", "您没有权限执行该操作！！");
				return "partner/page-partner-detail";
			}
		}
		if(!"show".equals(mode) && !"review".equals(mode) && !"changeUp".equals(mode)) {
			mode = "show";
		}
		map.put("mode",  mode);
		JSONObject jsonRet = PartnerMgrService.getPartnerSettleById(partnerId);
		if(jsonRet == null || !jsonRet.containsKey("partner")) {
			map.put("errmsg", "系统中没有该合作伙伴信息！");
		}else {
			PartnerBasic partner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
			if(!myPartner.getPartnerId().equals(partner.getUpPartnerId()) && 
					!myPartner.getPartnerId().equals(SysParam.getSyspartnerId()) ){
				map.put("errmsg", "您没有权限查看该合作伙伴信息！");
			}else {
				if(jsonRet.containsKey("settle")) {
					PartnerSettle settle = JSONObject.toJavaObject(jsonRet.getJSONObject("settle"), PartnerSettle.class);
					map.put("settle", settle);
				}
				map.put("partner", partner);
			}
		}
		
		return "partner/page-partner-detail";
		
	}
	
	
	/**
	 * 审核合作伙伴
	 * 
	 * @param partnerId	被审核合作伙伴
	 * @param result 审核结果
	 * @param review 审核意见
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/review",method=RequestMethod.POST)
	@ResponseBody
	public String review(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="partnerId",required=true)Integer partnerId,
			@RequestParam(value="review",required=true)String review,
			@RequestParam(value="result",required=true)String result,
			@Valid PartnerSettle settle,BindingResult br,
			HttpSession session) {
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		
		JSONObject jsonRet = new JSONObject();
		try {
			if(!"S".equals(result) && !"R".equals(result)) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "审批结果不正确（S-通过，R-拒绝）！");
				return jsonRet.toString();
			}
			if(review == null || review.length()<2 || review.length()>600) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "审批意见：长度2-600字符！");
				return jsonRet.toString();
			}
			//信息验证结果处理
			StringBuilder errSb = new StringBuilder();
			if(br.hasErrors()){
				List<ObjectError> list = br.getAllErrors();
				for(ObjectError e :list){
					errSb.append(e.getDefaultMessage());
				}
			}
			if(errSb.length()>0) {
				jsonRet.put("errmsg", errSb.toString());
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
			}
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("mypartners")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerBindVip.getVipId();
			}
			jsonRet = PartnerMgrService.review(partnerId, review, result, myPartner.getPartnerId(), updateOpr, partnerPasswd,settle);
		
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 变更合作伙伴的归属上级
	 * 1、上级主动变更；
	 * 2、顶级变更；
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping("/changeUp")
	@ResponseBody
	public String changeUp(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="partnerId",required=true)Integer partnerId,
			@RequestParam(value="newUpId",required=true)Integer newUpId,
			HttpSession session) {
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		JSONObject jsonRet = new JSONObject();
		try {
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("mypartners")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerBindVip.getVipId();
			}
			jsonRet = PartnerMgrService.changeUp(partnerId, newUpId, myPartner.getPartnerId(), updateOpr, partnerPasswd);
		
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{partnerId,pbTp,upPartnerId,country,province,city,area,busiName,legalPername,legalPeridno,compType,compName,licenceNo,phone,status,beginUpdateTime,endUpdateTime}
	 * @param pageCond		分页信息:{begin, pageSize}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public Object getAll(String jsonSearchParams,PageCond pageCond,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = JSONObject.parseObject(jsonSearchParams);
			if(search == null) {
				search = new JSONObject();
			}
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			String partnerUserTP = (String)  session.getAttribute("partnerUserTP");
			VipBasic vip = (VipBasic)  session.getAttribute("partnerBindVip");
			PartnerStaff myStaff = (PartnerStaff)  session.getAttribute("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || myStaff == null || myStaff.getUserId() == null
						|| myStaff.getTagList() == null || !myStaff.getTagList().contains("mypartners")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
			}else {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
			}
			JSONObject sortParams = new JSONObject();
			sortParams.put("time", "1#1");
			jsonRet = PartnerMgrService.getAll(myPartner.getPartnerId(), search, sortParams,pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
}
