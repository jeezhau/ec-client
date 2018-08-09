package com.mofangyouxuan.controller.pms;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.AppraiseService;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.utils.PageCond;

/**
 * 审核管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/review")
public class ReviewAction {
	
	
	/**
	 * 管理主页面
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/manage/{mode}")
	public String getIndex(@PathVariable("mode")String mode,
			@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			ModelMap map,HttpSession session){
		if(!"appraise".equals(mode) && !"goods".equals(mode)) {
			mode = "goods";
		}
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
			map.put("errmsg", "您还未开通合作伙伴功能！");
			return "redirect:/partner/manage";
		}
		//权限检查
		if("staff".equals(partnerUserTP)) {
			if(partnerStaff == null || partnerStaff.getTagList() == null || 
					(!partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewappr.getValue()) && !partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewgds.getValue()))){
				map.put("errmsg", "您还未开通信息审核功能！");
				return "redirect:/partner/manage";
			}
		}else {
			if(partnerBindVip == null || !partnerBindVip.getVipId().equals(myPartner.getVipId()) || !"1".equals(partnerBindVip.getStatus())) {
				map.put("errmsg", "您还未开通信息审核功能！");
				return "redirect:/partner/manage";
			}
		}
		map.put("mode", mode);
		map.put("sys_func", "review");
		
		return "review/page-review-manage";
	}
	
	/**
	 * 获取待审核的评论信息
	 * @param reviewStatus	审批状态：4review,normal
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/list/appraise")
	@ResponseBody
	public String getAllAppr(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="reviewStatus",required=true)String reviewStatus,PageCond pageCond,
			HttpSession session,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
			jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
			jsonRet.put("errmsg", "您还未开通合作伙伴功能！");
			return jsonRet.toJSONString();
		}
		//权限检查
		if("staff".equals(partnerUserTP)) {
			if(partnerStaff == null || partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewappr.getValue())){
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您没有权限执行该操作！！");
				return jsonRet.toString();
			}
		}else {
			if(partnerBindVip == null || !partnerBindVip.getVipId().equals(myPartner.getVipId()) || !"1".equals(partnerBindVip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您没有权限执行该操作！！");
				return jsonRet.toString();
			}
		}
		try {
			JSONObject params = new JSONObject();
			if(!SysParam.getSyspartnerId().equals(myPartner.getPartnerId())) {
				params.put("upPartnerId", myPartner.getPartnerId());
			}
			if("4review".equals(reviewStatus)) {//待审核
				params.put("status", "0");
			}else if("normal".equals(reviewStatus)){
				params.put("status", "S");
			}else if("refuse".equals(reviewStatus)){
				params.put("status", "R");
			}else {
				params.put("status", "0,S,R");
			}
			params.put("object", "1");
			jsonRet = AppraiseService.searchApprs(params.toJSONString(),JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取订单信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
		
	}

	/**
	 * 获取下级的所有商品信息
	 * @param reviewStatus	审批状态：4review,normal,refuse
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/list/goods")
	@ResponseBody
	public String getAllGoods(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="reviewStatus",required=true)String reviewStatus,PageCond pageCond,
			HttpSession session,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus())) ){
			jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
			jsonRet.put("errmsg", "您还未开通合作伙伴功能！");
			return jsonRet.toJSONString();
		}
		//权限检查
		if("staff".equals(partnerUserTP)) {
			if(partnerStaff == null || partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewgds.getValue())){
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您没有权限执行该操作！！");
				return jsonRet.toString();
			}
		}else {
			if(partnerBindVip == null || !partnerBindVip.getVipId().equals(myPartner.getVipId()) || !"1".equals(partnerBindVip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您没有权限执行该操作！！");
				return jsonRet.toString();
			}
		}
		try {
			JSONObject params = new JSONObject();
			if(!SysParam.getSyspartnerId().equals(myPartner.getPartnerId())) {
				params.put("upPartnerId", myPartner.getPartnerId());
			}
			params.put("isSelf", true);
			if("4review".equals(reviewStatus)) {//待审核
				params.put("reviewResult", "0");
			}else if("normal".equals(reviewStatus)){
				params.put("reviewResult", "S");
			}else if("refuse".equals(reviewStatus)){
				params.put("reviewResult", "R");
			}
			JSONObject sortParams = new JSONObject();
			sortParams.put("time", "1#1");
			//{errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
			jsonRet = GoodsService.searchGoods(false,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取商品信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 审核商品
	 * 
	 * @param goodsId	被审核商品
	 * @param result 审核结果
	 * @param review 审核意见
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/submit/goods",method=RequestMethod.POST)
	@ResponseBody
	public String reviewGoods(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="goodsId",required=true)Long goodsId,
			@RequestParam(value="review",required=true)String review,
			@RequestParam(value="result",required=true)String result,
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
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewgds.getValue())){
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
			jsonRet = GoodsService.review(goodsId, review, result, myPartner.getPartnerId(), updateOpr, partnerPasswd);
		
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 审核订单评价
	 * 
	 * @param orderId	被审核的订单评价
	 * @param result 	审核结果
	 * @param review 	审核意见
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/submit/appraise",method=RequestMethod.POST)
	@ResponseBody
	public String reviewAppraise(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@RequestParam(value="orderId",required=true)String orderId,
			@RequestParam(value="review",required=true)String review,
			@RequestParam(value="result",required=true)String result,
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
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains(PartnerStaff.TAG.reviewgds.getValue())){
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
			jsonRet = AppraiseService.reviewAppr(orderId, review, result, myPartner.getPartnerId(), updateOpr, partnerPasswd);
		
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
}
