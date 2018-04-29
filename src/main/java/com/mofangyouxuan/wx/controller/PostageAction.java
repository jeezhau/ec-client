package com.mofangyouxuan.wx.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.Postage;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;
import com.mofangyouxuan.wx.utils.ObjectToMap;

/**
 * 邮费相关管理服务
 * 1、已被使用中的模版可被编辑修改，但在提交之前给出提示，确认后方可提交后台；
 * 2、已被使用的模版不可删除；
 * 3、同一个合作伙伴下不可有两个同名的模版；
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/postage")
@SessionAttributes({"vipBasic","userBasic","partnerBasic"})
public class PostageAction {
	
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	@Value("${mfyx.mfyx-server-url}")
	private String mfyxServerUrl;
	
	@Value("${mfyx.postage-get-url}")
	private String postageGetUrl;
	
	@Value("${mfyx.postage-getusingcnt-url}")
	private String postageGetusingcntUrl;
	
	@Value("${mfyx.postage-getbypartner-url}")
	private String postageGetbypartnerUrl;
	
	@Value("${mfyx.postage-add-url}")
	private String postageAddUrl;
	
	@Value("${mfyx.postage-update-url}")
	private String postageUpdateUrl;
	
	@Value("${mfyx.postage-delete-url}")
	private String postageDeleteUrl;
	
	/**
	 * 获取邮费模板管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/index")
	public String getMgrIndex(ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		if(partner == null) {
			map.put("errmsg", "您还未开通合作伙伴！");
			return "forward:/user/index/vip" ;
		}
		
		return "postage/page-postage-manage";
	}

	/**
	 * 获取运费模板编辑页面
	 * @param postageId
	 * @param map
	 * @return
	 */
	@RequestMapping("/edit/{postageId}")
	public String editPostageTpl(@PathVariable("postageId")Long postageId,ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
		if(partner == null) {
			map.put("errmsg", "您还未开通合作伙伴！");
			return "forward:/user/index/vip" ;
		}
		Postage postage = null;
		if(postageId>0) {//修改
			try {
				JSONObject obj = (JSONObject) this.getById(postageId, map);
				if(obj.containsKey("postage")) {
					postage = JSONObject.toJavaObject(obj.getJSONObject("postage"), Postage.class);
					map.put("postage", postage);
				}
			}catch(Exception e){
				e.printStackTrace();
				map.put("errmsg", "获取运费模版信息失败，出现系统异常，异常信息：" + e.getMessage());
			}
		}else {
			postage = new Postage();
			postage.setPostageId(0l);
			map.put("postage", postage);
		}
		return "postage/page-postage-edit";
	}
	
	/**
	 * 获取指定ID的邮费模板信息
	 * @param postageId
	 * @param map
	 * @return {errcode:0,errmsg:"ok",postage:{}}
	 */
	@RequestMapping("/get/{postageId}")
	@ResponseBody
	public JSONObject getById(@PathVariable("postageId")Long postageId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vipBasic = (VipBasic) map.get("vipBasic");
			if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您尚未激活会员账户功能！");
				return jsonRet;
			}
			PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet;
			}
			String url = this.mfyxServerUrl + this.postageGetUrl + postageId;
			String strRet = HttpUtils.doGet(url);
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet;
	}
	
	/**
	 * 获取合作伙伴的所有的邮费模板信息
	 * @param map
	 * @return {errcode:0,errmsg:"ok",postages:[{...},{}]}
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public Object getAllByPartner(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vipBasic = (VipBasic) map.get("vipBasic");
			if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您尚未激活会员账户功能！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			String url = this.mfyxServerUrl + this.postageGetbypartnerUrl + partner.getPartnerId();
			String strRet = HttpUtils.doGet(url);
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet;
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}	
	
	/**
	 * 获取指定模版正被使用的次数
	 * @param postageId
	 * @param map
	 * @return {errcode:0,errmsg:"ok",cnt:0}
	 */
	@RequestMapping("/getusingcnt/{postageId}")
	@ResponseBody
	public String getUsingCnt(@PathVariable("postageId")Integer postageId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vipBasic = (VipBasic) map.get("vipBasic");
			if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您尚未激活会员账户功能！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			String url = this.mfyxServerUrl + this.postageGetusingcntUrl + partner.getPartnerId();
			String strRet = HttpUtils.doGet(url);
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 保存模板信息编辑
	 * @param map
	 * @return {errcode:0,errmsg:"ok","partnerId"}
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String savePostage(@Valid Postage postage,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vipBasic = (VipBasic) map.get("vipBasic");
			if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您尚未激活会员账户功能！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			//信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.POSTAGE_PARAM_ERROR);
				return jsonRet.toString();
			}
			//其他验证
			String isCityWide = postage.getIsCityWide();
			String isFree = postage.getIsFree();
			StringBuilder sb = new StringBuilder();
			if(!"1".equals(isFree)) {//非无条件免邮
				Integer firstWeight = postage.getFirstWeight();
				BigDecimal firstWPrice = postage.getFirstWPrice();
				Integer additionWeight = postage.getAdditionWeight();
				BigDecimal additionWPrice = postage.getAdditionWPrice();
				if(firstWeight == null || 
						firstWPrice == null ||
						additionWeight == null ||
						additionWPrice == null) {
					sb.append(" 首重、首重价格、续重、续重价格：不可为空！");
				}
				if(isFree.contains("2")) {//重量限制免邮
					Integer freeWeight = postage.getFreeWeight();
					if(freeWeight == null) {
						sb.append(" 免邮重量：不可为空，最小值1(kg)！");
					}
				}
				if(isFree.contains("3")) {//金额限制免邮
					BigDecimal freeAmount = postage.getFreeAmount();
					if(freeAmount == null) {
						sb.append(" 免邮金额：不可为空，最小值1(元)！");
					}
				}
				if("0".equals(isCityWide)) {//全国
					String provLimit = postage.getProvLimit();
					if(provLimit == null || provLimit.length()<2) {
						sb.append(" 配送省份： 不可为空！");
					}
					if(provLimit.contains("全国")) {
						postage.setProvLimit("全国");
					}
				}else {//同城
					if(isFree.contains("4")) {//距离限制免邮
						Integer freeDist = postage.getFreeDist();
						if(freeDist == null) {
							sb.append(" 免邮距离：不可为空，最小值1(km)！");
						}
					}
					Integer firstDist = postage.getFirstDist();
					BigDecimal firstDPrice = postage.getFirstDPrice();
					Integer additionDist = postage.getAdditionDist();
					BigDecimal additionDPrice = postage.getAdditionDPrice();
					if(firstDist == null || 
							firstDPrice == null ||
							additionDist == null ||
							additionDPrice == null) {
						sb.append(" 首距、首距价格、续距、续距价格：不可为空！");
					}
				}
			}
			if("0".equals(isCityWide)) {//全国
				String provLimit = postage.getProvLimit();
				if(provLimit == null || provLimit.length()<2) {
					sb.append(" 配送省份： 不可为空！");
				}
				if(provLimit.contains("全国")) {
					postage.setProvLimit("全国");
				}
			}else {//同城
				Integer distLimit = postage.getDistLimit();
				if(distLimit == null) {
					sb.append(" 配送距离： 不可为空！");
				}
			}

			if(sb.length()>0) {
				jsonRet.put("errcode", ErrCodes.POSTAGE_PARAM_ERROR);
				jsonRet.put("errmsg", sb.toString());
				return jsonRet.toString();
			}
			//数据处理
			String[] excludeFields = {"status","updateTime"};
			postage.setPartnerId(partner.getPartnerId());
			Map<String,Object> params = ObjectToMap.object2Map(postage, excludeFields, true);
			params.put("currVipId", vipBasic.getVipId());
			String url = this.mfyxServerUrl + this.postageAddUrl; //添加
			if(postage.getPostageId()>0) { //更新
				url = this.mfyxServerUrl + this.postageUpdateUrl;
			}
			String strRet = HttpUtils.doPost(url, params);
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}	
	
	/**
	 * 删除模板信息编辑
	 * @param map
	 * @return {errcode:0,errmsg:"ok"}
	 */
	@RequestMapping("/delete/{postageId}")
	@ResponseBody
	public String deletePostage(@PathVariable("postageId")Long postageId,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vipBasic = (VipBasic) map.get("vipBasic");
			if(vipBasic == null || !"1".equals(vipBasic.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您尚未激活会员账户功能！");
				return jsonRet.toString();
			}
			PartnerBasic partner = (PartnerBasic) map.get("partnerBasic");
			if(partner == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			//其他验证
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("postageId", postageId);
			params.put("currVipId", vipBasic.getVipId());
			String url = this.mfyxServerUrl + this.postageDeleteUrl;
			String strRet = HttpUtils.doPost(url, params);
			jsonRet = JSONObject.parseObject(strRet);
			return jsonRet.toJSONString();
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
}
