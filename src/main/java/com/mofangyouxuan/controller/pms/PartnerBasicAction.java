package com.mofangyouxuan.controller.pms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerSettle;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.service.PartnerStaffService;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;
import com.mofangyouxuan.utils.SignUtils;

/**
 * 合作伙伴基础信息管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/partner")
public class PartnerBasicAction {
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	private Integer openPartnerNeedScore = 1000; //开通合作伙伴需要多少积分
	private String[] certTypeArr = {"logo","idcard1","idcard2","licence","agreement"}; 	//当前支持的证件类型
	
	/**
	 * 选择合作伙伴登录
	 * 1、登录之后获取会员信息；
	 * 2、登录以后获取员工信息；
	 * 3、保存session：partnerUserTP,partnerPasswd,partnerStaff,partnerBindVip,myPartner
	 * @param userIp		用户类型
	 * @param userId		用户ID
	 * @param partnerId	合作伙伴ID
	 * @param passwd		会员密码或员工密码
	 * @param map
	 * @return
	 */
	@RequestMapping("/login")
	public String login(String userTp,Integer userId,Integer partnerId,String passwd,
			HttpServletRequest request,HttpSession session,ModelMap map) {
		if(userTp == null || userId == null || passwd == null) {
			return "partner/page-partner-login";
		}
		map.put("userTp", userTp);
		map.put("userId", userId);
		map.put("partnerId", partnerId);
		
		if(!"bindVip".equals(userTp) && !"staff".equals(userTp)) {
			map.put("errmsg", "用户类型不正确！");
			return "partner/page-partner-login";
		}
		if("staff".equals(userTp) && partnerId == null) {
			map.put("errmsg", "合作伙伴不可为空！");
			return "partner/page-partner-login";
		}
		if(passwd.length()<6 || passwd.length()>20) {
			map.put("errmsg", "密码长度6-20字符！");
			return "partner/page-partner-login";
		}
		
		UserBasic userBasic = UserService.getUserBasicById(userId);
		VipBasic vipBasic = VipService.getVipBasic(userId);
		if(userBasic == null || vipBasic == null || 
				!"1".equals(userBasic.getStatus()) || !"1".equals(vipBasic.getStatus())) { //正常用户
			try {
				String signPwd = SignUtils.encodeSHA256Hex(passwd);
				if("bindVip".equals(userTp)) {
					JSONObject jsonRet = PartnerMgrService.getPartnerByVip(userId);
					if(!jsonRet.containsKey("partner")) {
						//map.put("errmsg", "系统中没有该会员绑定的合作伙伴信息！");
						//return "partner/page-partner-login";
					}else {
						PartnerBasic myPartner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
						PartnerSettle mySettle = null;
						if(jsonRet.containsKey("settle")) {
							mySettle = JSONObject.toJavaObject(jsonRet.getJSONObject("settle"), PartnerSettle.class);
						}
						if(signPwd.equals(vipBasic.getPasswd())) {
							session.setAttribute("myPartner", myPartner); //保存session
							session.setAttribute("mySettle", mySettle); //保存session
							session.setAttribute("partnerBindVip", vipBasic); //保存session
							session.setAttribute("partnerUserTP", userTp); //保存session
							session.setAttribute("partnerPasswd", passwd); //保存session
							return "redirect:" + localServerUrl + "/partner/manage";
						}else {
							map.put("errmsg", "会员ID与密码不正确！");
							return "partner/page-partner-login";
						}
					}
				}else {//员工
					JSONObject jsonRet = PartnerMgrService.getPartnerSettleById(partnerId);
					if(!jsonRet.containsKey("partner")) {//成功
						map.put("errmsg", "系统中没有该合作伙伴信息！");
						return "partner/page-partner-login";
					}
					PartnerBasic myPartner = JSONObject.toJavaObject(jsonRet.getJSONObject("partner"), PartnerBasic.class);
					PartnerSettle mySettle = null;
					if(jsonRet.containsKey("settle")) {
						mySettle = JSONObject.toJavaObject(jsonRet.getJSONObject("settle"), PartnerSettle.class);
					}
					PartnerStaff staff = PartnerStaffService.getStaffByUserId(partnerId, userId);
					if(staff == null) {
						map.put("errmsg", "系统中没有该合作伙伴员工信息！");
						return "partner/page-partner-login";
					}
					if(!signPwd.equals(staff.getPasswd())) {
						map.put("errmsg", "用户ID与密码不正确！");
						return "partner/page-partner-login";
					}
					session.setAttribute("myPartner", myPartner); //保存session
					session.setAttribute("mySettle", mySettle); //保存session
					session.setAttribute("partnerStaff", staff); //保存session
					session.setAttribute("partnerUserTP", userTp); //保存session
					session.setAttribute("partnerPasswd", passwd); //保存session
					
					return "redirect:" + localServerUrl + "/partner/manage";
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("errmsg", "出现系统异常，异常信息：" + e.getMessage());
			}
		}else {
			map.put("errmsg", "系统中没有该正常用户或会员！" );
		}
		
		return "partner/page-partner-login";
	}
	
	/**
	 * 退出登录
	 * 清除session：partnerUserTP,partnerPasswd,partnerStaff,partnerBindVip,myPartner
	 * @param userIp		用户类型
	 * @param userId		用户ID
	 * @param partnerId	合作伙伴ID
	 * @param passwd		会员密码或员工密码
	 * @param map
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session,ModelMap map) {
		session.removeAttribute("myPartner"); 
		session.removeAttribute("partnerBindVip"); 
		session.removeAttribute("partnerUserTP"); 
		session.removeAttribute("partnerPasswd");
		session.removeAttribute("partnerStaff"); 
		return "partner/page-partner-login";
		
	}
	
	/**
	 * 获取合作伙伴管理首页
	 * @return
	 */
	@RequestMapping("/manage")
	public String getManage(@SessionAttribute("partnerUserTP")String partnerUserTP,
			String errmsg,
			HttpSession session,ModelMap map) {
		
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		map.put("partnerUserTP", partnerUserTP);
		map.put("partnerBindVip", partnerBindVip);
		map.put("partnerStaff", partnerStaff);
		map.put("myPartner", myPartner);
		if(errmsg != null && errmsg.length()>0) {
			map.put("errmsg", errmsg);
		}
		map.put("sys_func", "partner-manage");
		return "partner/page-partner-manage";
	}
	
	/**
	 * 获取合作信息编辑页面
	 * @return
	 */
	@RequestMapping("/edit")
	public String editBasic(@SessionAttribute("partnerUserTP")String partnerUserTP,
			HttpSession session,ModelMap map) {
		
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		PartnerSettle mySettle = (PartnerSettle) session.getAttribute("mySettle");
		if("bindVip".equals(partnerUserTP) && myPartner == null) {
			String needScore = SysParam.getSysParam("partner_open_need_socre");
			try {
				openPartnerNeedScore = new Integer(needScore);
			}catch(Exception e) {
				;
			}
			if(partnerBindVip.getScores() < openPartnerNeedScore) {
				map.put("errmsg", "您的当前会员积分还不够开通合作伙伴，开通需要：" + openPartnerNeedScore + "会员积分！");
				return "partner/page-partner-manage";
			}
		}
		map.put("partnerUserTP", partnerUserTP);
		map.put("partnerBindVip", partnerBindVip);
		map.put("partnerStaff", partnerStaff);
		map.put("myPartner", myPartner);
		map.put("mySettle", mySettle);
		map.put("sys_func", "partner-index");
		return "partner/page-partner-edit";
	}
	
	
	/**
	 * 保存合作伙伴信息
	 * 
	 * @param basic	合作伙伴信息
	 * @param result 字段验证结果
	 * @param passwd 会员密码
	 * @return {errcode:0,errmsg:"ok"}
	 * @throws JSONException 
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public String saveBasic(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			@Valid PartnerBasic basic,BindingResult result1,
			@Valid PartnerSettle settle,BindingResult result2,
			HttpSession session,ModelMap map) {
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		
		JSONObject jsonRet = new JSONObject();
		try {
			//信息验证结果处理
			StringBuilder errSb = new StringBuilder();
			if(result1.hasErrors()){
				List<ObjectError> list = result1.getAllErrors();
				for(ObjectError e :list){
					errSb.append(e.getDefaultMessage());
				}
			}
			if(result2.hasErrors()){
				List<ObjectError> list = result2.getAllErrors();
				for(ObjectError e :list){
					errSb.append(e.getDefaultMessage());
				}
			}
			if(errSb.length()>0) {
				jsonRet.put("errmsg", errSb.toString());
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
			}
			
			basic.setPartnerId(null);
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getUserId() == null
						|| partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("basic")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				basic.setVipId(myPartner.getVipId());
				updateOpr = partnerStaff.getUserId();
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				basic.setVipId(partnerBindVip.getVipId());
				updateOpr = partnerBindVip.getVipId();
			}
			basic.setUpdateOpr(updateOpr);
			basic.setUpdateTime(new Date());
			basic.setFreviewOpr(null);
			basic.setFreviewTime(null);
			basic.setFreviewLog(null);
			basic.setLreviewLog(null);
			basic.setLreviewOpr(null);
			basic.setLreviewTime(null);
			String strRet = "";
			if(myPartner == null || myPartner.getPartnerId() == null) {
				basic.setPartnerId(null);
				strRet = PartnerMgrService.create(basic,settle,partnerPasswd);
			}else {
				basic.setPartnerId(myPartner.getPartnerId());
				strRet = PartnerMgrService.update(basic,settle,partnerPasswd);
			}
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {//更新成功
				basic.setStatus("0");
				basic.setPartnerId(retObj.getInteger("partnerId"));
				session.setAttribute("myPartner", basic); //更新session
				session.setAttribute("mySettle", settle);
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
	public String changeStatus(@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			HttpSession session,ModelMap map) {
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		JSONObject jsonRet = new JSONObject();
		try {
			//数据检查
			if(myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴！");
				return jsonRet.toString();
			}
			String oldStatus = myPartner.getStatus();
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
			Integer updateOpr = null;
			//权限检查
			if("staff".equals(partnerUserTP)) {
				if(partnerStaff == null || partnerStaff.getTagList() == null || !partnerStaff.getTagList().contains("basic")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
			}else {
				if(partnerBindVip == null || !partnerBindVip.getVipId().equals(myPartner.getVipId()) || !"1".equals(partnerBindVip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerBindVip.getVipId();
			}
			String strRet = PartnerMgrService.changeStatus(myPartner.getPartnerId(), updateOpr,partnerPasswd);
			JSONObject retObj = JSONObject.parseObject(strRet);
			if(retObj.containsKey("errcode") && retObj.getIntValue("errcode") == 0) {
				myPartner.setStatus(newStatus);
				session.setAttribute("myPartner", myPartner); //更新session
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
			@RequestParam(value="image")MultipartFile image,
			@SessionAttribute("partnerUserTP")String partnerUserTP,@SessionAttribute("partnerPasswd")String partnerPasswd,
			HttpSession session,ModelMap map) {
		
		VipBasic partnerBindVip = (VipBasic) session.getAttribute("partnerBindVip");
		PartnerStaff partnerStaff = (PartnerStaff) session.getAttribute("partnerStaff");
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		JSONObject jsonRet = new JSONObject();
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
			//数据与权限检查
			Integer updateOpr = null;
			Integer bindVip = null;
			if("staff".equals(partnerUserTP)) {
				if(myPartner == null || myPartner.getPartnerId() == null || partnerStaff == null || partnerStaff.getTagList() == null || 
						!partnerStaff.getTagList().contains("basic")){
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerStaff.getUserId();
				bindVip = myPartner.getVipId();
			}else {
				if(partnerBindVip == null || !"1".equals(partnerBindVip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "您没有权限执行该操作！！");
					return jsonRet.toString();
				}
				updateOpr = partnerBindVip.getVipId();
				bindVip = partnerBindVip.getVipId();
			}
			File dir = new File(this.tmpFileDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			tmpImg = new File(dir,image.getOriginalFilename()); //生成临时文件
			FileUtils.copyInputStreamToFile(image.getInputStream(), tmpImg);
			
			String ret = PartnerMgrService.uploadCert(bindVip,tmpImg, certType,updateOpr,partnerPasswd);
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
			File file = PartnerMgrService.showCert(partner.getPartnerId(), certType);
			if(file == null) {
				return;
			}
			InputStream is = new FileInputStream(file);
			String filename = file.getName();
			response.setContentType("image/*");
			response.addHeader("filename", filename);
			OutputStream os = response.getOutputStream(); 
			byte[] buff = new byte[1024];
			int len = 0;
			while((len=is.read(buff))>0) {
				os.write(buff, 0, len);
			}
			os.flush();
			os.close();
			is.close();
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
