package com.mofangyouxuan.wx.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.CashApply;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.CashApplyService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 合作伙伴员工管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/cash")
@SessionAttributes({"sys_func"})
public class CashApplyAction {

	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	
	/**
	 * 获取会员提现管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getManage(ModelMap map,HttpSession session) {
		VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus()) ) {
			map.put("errmsg", "您还未开通会员或状态限制！");
			return "forward:/user/index/vip" ;
		}
		
		map.put("sys_func", "user");
		return "cash/page-cash-manage";
	}
	
	/**
	 * 显示提现申请记录
	 * @param map
	 * @return
	 */
	@RequestMapping("/show")
	public String showApply(
			ModelMap map,HttpSession session) {
		VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus()) ) {
			map.put("errmsg", "您还未开通会员或状态限制！");
			return "forward:/user/index/vip" ;
		}
		map.put("vipBasic", vip);
		map.put("sys_func", "user");
		return "cash/page-cash-show";
	}
	
	/**
	 * 
	 * @param apply
	 * @param result
	 * @param passwd
	 * @param session
	 * @return
	 */
	@RequestMapping("/apply/submit")
	@ResponseBody
	public Object saveApply(@Valid CashApply apply,BindingResult result,
			@RequestParam(value="passwd",required=true)String passwd,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据验证
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据检查与权限校验
			VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通会员或状态限制！");
				return jsonRet.toJSONString();
			}
			if(passwd == null || passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errmsg", "会员密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			Integer applyOpr = vip.getVipId();
			apply.setCashAmount(apply.getCashAmount()*100); //转化为分
			apply.setApplyOpr(applyOpr); 
			apply.setVipId(vip.getVipId());
			jsonRet = CashApplyService.cashApply(apply, passwd);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 删除提现申请
	 * @param applyId		申请ID
	 * @param passwd			会员密码
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String deleteApply(@RequestParam(value="applyId",required=true)String applyId,
			@RequestParam(value="passwd",required=true)String passwd,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据检查与权限校验
			VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通会员或状态限制！");
				return jsonRet.toJSONString();
			}
			if(passwd == null || passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errmsg", "会员密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据保存
			jsonRet = CashApplyService.deleteApply(vip.getVipId(), passwd, applyId);
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
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
	public Object getAll(String jsonSearchParams,PageCond pageCond,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = JSONObject.parseObject(jsonSearchParams);
			if(search == null) {
				search = new JSONObject();
			}
			//数据检查与权限校验
			VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通会员或状态限制！");
				return jsonRet.toJSONString();
			}
			
			search.put("vipId", vip.getVipId());
			jsonRet = CashApplyService.getAll(search, pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
}
