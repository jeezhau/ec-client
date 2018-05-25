package com.mofangyouxuan.wx.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.PartnerMgrService;

/**
 * 合作伙伴管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/partner")
@SessionAttributes({"vipBasic","userBasic","partnerBasic","webAuth","jsapiTicket"})
public class PartnerAction {
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	private String[] certTypeArr = {"logo","idcard1","idcard2","licence"}; 	//当前支持的证件类型
	
	/**
	 * 获取合作伙伴管理首页
	 * @return
	 */
	@RequestMapping("/index")
	public String getIndex(ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		PartnerBasic partner = PartnerMgrService.getPartnerByVip(vipBasic.getVipId());
		if(partner != null) {
			map.put("partnerBasic", partner);
		}
		map.put("sys_func", "partner-index");
		return "partner/page-partner-index";
	}
	
	/**
	 * 获取合作信息编辑页面
	 * @return
	 */
	@RequestMapping("/edit")
	public String editBasic(ModelMap map) {
		VipBasic vipBasic = (VipBasic) map.get("vipBasic");
		if(vipBasic == null) {
			return "error/page-no-user";
		}else if(!"1".equals(vipBasic.getStatus())) {
			map.put("errmsg", "您尚未激活会员账户功能！")	;
			return "forward:/user/index/vip" ;
		}
		PartnerBasic partner = PartnerMgrService.getPartnerByVip(vipBasic.getVipId());
		if(partner != null) {
			map.put("partnerBasic", partner);
		}
		map.put("sys_func", "partner-index");
		
		return "partner/page-partner-edit";
	}
	

	/**
	 * 获取合作伙伴的商户展示页面信息
	 * 【权限人】
	 * 任何人
	 * @param partnerId
	 * @param map
	 * @return
	 */
	@RequestMapping("/mcht/{partnerId}")
	public String getMcht(@PathVariable("partnerId")Integer partnerId,ModelMap map) {

		PartnerBasic partner = PartnerMgrService.getPartnerById(partnerId);
		if(partner == null || !"S".equals(partner.getStatus())) {
			map.put("errmsg", "系统中没有该商户信息！");
		}else {
			map.put("mcht", partner);
		}
		return "partner/page-partner-mcht";
	}
	
	/**
	 * 根据合作伙伴绑定的用户获取合作伙伴信息
	 * 
	 * @return {errcode:-1,errmsg:"错误信息"} 或 {合作伙伴的所有字段}
	 * @throws JSONException 
	 */
	@RequestMapping("/get")
	@ResponseBody
	public Object getPartner(ModelMap map){
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic)map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus()) ) {
			jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
			jsonRet.put("errmsg", "系统中没有该会员或未激活！");
			return jsonRet.toString();
		}
		
		PartnerBasic partner = null;
		try{
			partner = PartnerMgrService.getPartnerByVip(vip.getVipId());
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(partner == null) {
			jsonRet.put("errcode", ErrCodes.PARTNER_NO_EXISTS);
			jsonRet.put("errmsg", "获取合作伙伴信息失败！");
			return jsonRet.toString();
		}
		return partner;
	}
	
	/**
	 * 保存合作伙伴信息
	 * 
	 * @param basic	合作伙伴信息
	 * @param result 字段验证结果
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public String saveBasic(@Valid PartnerBasic basic,BindingResult result,ModelMap map){
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic)map.get("vipBasic");
		PartnerBasic oldPartner = (PartnerBasic)map.get("partnerBasic");
		try {
			//用户信息验证结果处理
			if(result.hasErrors()){
				StringBuilder sb = new StringBuilder();
				List<ObjectError> list = result.getAllErrors();
				for(ObjectError e :list){
					sb.append(e.getDefaultMessage());
				}
				jsonRet.put("errmsg", sb.toString());
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据检查
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			//数据处理
			if(oldPartner != null && oldPartner.getPartnerId() != null) {
				basic.setPartnerId(oldPartner.getPartnerId());
			}else {
				basic.setPartnerId(null);
			}
			basic.setVipId(vip.getVipId());
			basic.setUpdateTime(new Date());
			basic.setReviewLog("");
			basic.setReviewOpr(null);
			basic.setReviewTime(null);
			String strRet = "";
			if(oldPartner == null || oldPartner.getPartnerId() == null) {
				strRet = PartnerMgrService.create(basic);
			}else {
				strRet = PartnerMgrService.update(basic);
			}
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {//更新成功
				basic.setStatus("0");
				basic.setPartnerId(retObj.getInteger("partnerId"));
				map.put("partnerBasic", basic);
			}
			return strRet;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 变更合作伙伴状态：关闭、打开
	 * 
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping("/changeStatus")
	public String changeStatus(ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			PartnerBasic old = (PartnerBasic) map.get("partnerBasci");
			//数据检查
			if(old == null ) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			String oldStatus = old.getStatus();
			if(!"S".equals(oldStatus) && !"C".equals(oldStatus)) { //正常或关闭
				jsonRet.put("errcode", ErrCodes.PARTNER_STATUS_ERROR);
				jsonRet.put("errmsg", "您不可变更当前状态！！");
				return jsonRet.toString();
			}
			String newStatus = "";
			if("S".equals(oldStatus)) {
				newStatus = "C";
			}else {
				newStatus = "S";
			}
			String strRet = PartnerMgrService.changeStatus(old.getPartnerId(), old.getVipId());
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {
				old.setStatus(newStatus);
				map.put("partnerBasic", old);
			}
			return strRet;
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 证件照上传
	 * @param certType	证件类型
	 * @param image		照片,jpg格式
	 * @param currUserId	当前操作用户
	 * @return
	 */
	@RequestMapping("/cert/upload")
	@ResponseBody
	public String uploadCert(@RequestParam(value="certType",required=true)String certType,
			@RequestParam(value="image")MultipartFile image,ModelMap map) {
		
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic)map.get("vipBasic");
		File tmpImg = null;
		try {
			if(image == null || image.isEmpty()) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "证件照片信息不可为空！");
				return jsonRet.toString();
			}
			//文件类型判断
			String imgType = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')+1);
			if(!"jpg".equalsIgnoreCase(imgType) && !"jpeg".equalsIgnoreCase(imgType) && !"png".equalsIgnoreCase(imgType)) {
				jsonRet.put("errcode", -888);
				jsonRet.put("errmsg", "证件图片文件必须是jpg,jpeg,png格式！");
				return jsonRet.toString();
			}
			//证件类型判断
			boolean flag = false;
			for(String tp:certTypeArr) {
				if(tp.equals(certType)) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				jsonRet.put("errcode", -888);
				jsonRet.put("errmsg", "证件类型只可是：" + Arrays.toString(certTypeArr) + "！");
				return jsonRet.toString();
			}
			//数据检查
			if(vip == null || !"1".equals(vip.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该会员或未激活！");
				return jsonRet.toString();
			}
			File dir = new File(this.tmpFileDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			tmpImg = new File(dir,image.getOriginalFilename()); //生成临时文件
			FileUtils.copyInputStreamToFile(image.getInputStream(), tmpImg);
			
			String ret = PartnerMgrService.uploadCert(tmpImg, certType,vip.getVipId());
			jsonRet = JSONObject.parseObject(ret);
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", -777);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}finally {
			if(tmpImg != null) {
				tmpImg.delete();
			}
		}
		return jsonRet.toString();
	}
	
	/**
	 * 显示证件图片
	 * @param certType
	 * @return
	 */
	@RequestMapping("/cert/show/{certType}/{partnerId}")
	public void showCert(@PathVariable(value="certType",required=true)String certType,
			@PathVariable(value="partnerId",required=true)Integer partnerId,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		try {
			PartnerBasic partner = PartnerMgrService.getPartnerById(partnerId);
			if(partner == null) {
				return;
			}
			//证件类型判断
			boolean flag = false;
			for(String tp:certTypeArr) {
				if(tp.equals(certType)) {
					flag = true;
					break;
				}
			}
			if(!flag) {
				return;
			}
			File file = PartnerMgrService.showCert(partner.getVipId(), certType);
			if(file == null || !file.exists()) {
				return;
			}
			BufferedImage image = ImageIO.read(file);
			response.setContentType("image/*");
			OutputStream os = response.getOutputStream();  
			String type = file.getName().substring(file.getName().lastIndexOf('.')+1);
			ImageIO.write(image, type, os); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
