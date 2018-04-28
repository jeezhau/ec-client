package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;

/**
 * 邮费相关管理服务
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/postage")
@SessionAttributes({"vipBasic","userBasic","partnerBasic"})
public class PostageAction {
	
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
	public String editPostageTpl(@PathVariable("postageId")Integer postageId,ModelMap map) {
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
		
		return "postage/page-postage-edit";
	}
	
	
}
