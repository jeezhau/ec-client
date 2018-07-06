package com.mofangyouxuan.controller.pms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.CashApply;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.CashApplyService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 提现处理管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/pcash")
@SessionAttributes({"sys_func"})
public class CashApplyDealAction {

	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	
	/**
	 * 获取提现处理管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getManage(@SessionAttribute("partnerUserTP")String partnerUserTP,
			HttpSession session,ModelMap map) throws UnsupportedEncodingException {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		String errmsg = null;
		//权限检查
		if(myPartner == null || !myPartner.getPartnerId().equals(SysParam.getSyspartnerId())) {
			errmsg = "您没有权限执行该操作！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
				(!partnerStaff.getTagList().contains(PartnerStaff.TAG.CashapplyDeal.getValue()) ))){
			errmsg = "您没有权限执行该操作！！";
			return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
		}
		map.put("sys_func", "CashapplyDeal");
		return "pcash/page-pcash-manage";
	}
	
	/**
	 * 显示提现申请记录
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/show/{mode}/{applyId}")
	public String showApply(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@PathVariable("mode")String mode,@PathVariable("applyId")String applyId,
			ModelMap map,HttpSession session) throws UnsupportedEncodingException {
		try {
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
			String errmsg = null;
			if(!"accept".equals(mode) && !"finish".equals(mode) && !"detail".equals(mode)) {
				errmsg = "系统没有功能！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
			if(myPartner == null || !myPartner.getPartnerId().equals(SysParam.getSyspartnerId())) {
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.CashapplyDeal.getValue()) ))){
				errmsg = "您没有权限执行该操作！！";
				return "redirect:/partner/manage" + "?errmsg=" + URLEncoder.encode(errmsg, "utf8") ;
			}
			//获取数据
			JSONObject search = new JSONObject();
			search.put("applyId", applyId);
			JSONObject jsonRet = CashApplyService.getAll(search, new PageCond(0,1));
			if(jsonRet == null || !jsonRet.containsKey("datas")) {
				map.put("errmsg", "没有获取到提现申请信息！" );
			}else {
				CashApply apply = JSONObject.toJavaObject(jsonRet.getJSONArray("datas").getJSONObject(0), CashApply.class);
				map.put("apply", apply);
				VipBasic vip = VipService.getVipBasic(apply.getVipId());
				map.put("vip", vip);
			}
		}catch(Exception e) {
			map.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		map.put("mode", mode);
		return "pcash/page-pcash-show";
	}
	
	/**
	 * 提交处理结果
	 * @param apply
	 * @param result
	 * @param passwd
	 * @param session
	 * @return
	 */
	@RequestMapping("/deal")
	@ResponseBody
	public Object saveDeal(@SessionAttribute("partnerUserTP")String partnerUserTP,
			@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="vipId",required=true)Integer vipId,
			@RequestParam(value="applyId",required=true)String applyId,
			@RequestParam(value="result",required=true)String result,
			@RequestParam(value="memo",required=true)String memo,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		try {
			//数据验证
			if(!"1".equals(result) && !"S".equals(result) && !"F".equals(result)){
				jsonRet.put("errmsg", "结果类型：取值不正确！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(memo ==null || memo.length()<2 || memo.length()>1000) {
				jsonRet.put("errmsg", "备注：最长1000字符！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据检查与权限校验
			Integer updateOpr = null;
			if(myPartner == null || !myPartner.getPartnerId().equals(SysParam.getSyspartnerId())) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您没有权限执行该操作！！");
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
			jsonRet = CashApplyService.updaeStat(vipId, applyId, result, memo, updateOpr, partnerPasswd);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{applyId,vipId,cashType,accountType,idNo,accountNo,accountName,accountBank,channelType,applyOpr,updateOpr,status,beginApplyTime,endApplyTime}
	 * @param jsonPageCond		分页信息:{begin:, pageSize:}
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public Object getAll(@SessionAttribute("partnerUserTP")String partnerUserTP,
			String jsonSearchParams,PageCond pageCond,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = JSONObject.parseObject(jsonSearchParams);
			if(search == null) {
				search = new JSONObject();
			}
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
			String errmsg = null;
			if(myPartner == null || !myPartner.getPartnerId().equals(SysParam.getSyspartnerId())) {
				errmsg = "您没有权限执行该操作！！";
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", errmsg);
				return jsonRet;
			}
			if("staff".equals(partnerUserTP) && (partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.CashapplyDeal.getValue()) ))){
				errmsg = "您没有权限执行该操作！！";
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", errmsg);
				return jsonRet;
			}
			jsonRet = CashApplyService.getAll(search, pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
}
