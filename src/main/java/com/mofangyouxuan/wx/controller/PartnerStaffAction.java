package com.mofangyouxuan.wx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.PartnerStaffService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 合作伙伴员工管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/pstaff")
@SessionAttributes({"sys_func"})
public class PartnerStaffAction {

	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
	
	/**
	 * 获取合作伙伴员工管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getManage(ModelMap map,HttpSession session) {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "forward:/partner/manage" ;
		}
		
		map.put("sys_func", "partner-pstaff");
		return "pstaff/page-pstaff-manage";
	}
	
	/**
	 * 获取合作伙伴员工管理页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/edit/{userId}")
	public String beginEdit(@PathVariable("userId")Integer userId,
			ModelMap map,HttpSession session) {
		PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
		String partnerUserTP = (String) session.getAttribute("partnerUserTP");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "forward:/partner/manage" ;
		}
		PartnerStaff staff = null;
		if(!"bindVip".equals(partnerUserTP)) {//员工自己修改
			staff = (PartnerStaff) session.getAttribute("partnerStaff");
		}else {
			if(userId > 0) {
				staff = this.getStaffByUser(myPartner.getPartnerId(), userId);
			}else {
				staff = new PartnerStaff();
				staff.setRecId(0l);
				staff.setPartnerId(myPartner.getPartnerId());
			}
		}
		if(staff == null) {
			map.put("errmsg", "获取员工信息失败，请稍后再试！");
		}else {
			map.put("staff", staff);
			map.put("partnerUserTP", partnerUserTP);
		}
		map.put("sys_func", "partner-pstaff");
		return "pstaff/page-pstaff-edit";
	}
	
	/**
	 * 合作伙伴员工基本信息保存
	 * 1、新增时redId为0，新员工的初始密码不可为空；
	 * 2、修改时不修改原来的密码、头像、客服二维码；
	 * @param partnerId
	 * @param staff
	 * @param result
	 * @param passwd		员工密码或合作伙伴绑定的会员的密码
	 * @return
	 */
	@RequestMapping("/saveStaff")
	@ResponseBody
	public Object saveStaff(@Valid PartnerStaff staff,BindingResult result,
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
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			String partnerUserTP = (String)  session.getAttribute("partnerUserTP");
			String partnerPasswd = (String)  session.getAttribute("partnerPasswd");
			VipBasic vip = (VipBasic)  session.getAttribute("partnerBindVip");
			PartnerStaff myStaff = (PartnerStaff)  session.getAttribute("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			Integer updateOpr = null;
			if("bindVip".equals(partnerUserTP)) {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的会员信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = vip.getVipId();
			}else {
				if(myStaff == null || myStaff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = myStaff.getUserId();
			}
			//数据处理保存
			if(staff.getRecId() == 0) { //新增
				String defaultPwd = staff.getPasswd();
				if(defaultPwd == null || defaultPwd.length()<6 || defaultPwd.length()>20) {
					jsonRet.put("errmsg", "新员工的初始操作密码：长度范围【6-20字符】！");
					jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
					return jsonRet.toString();
				}
				staff.setPasswd(defaultPwd);
			}else { //修改
				staff.setHeadimgurl(null);
				staff.setKfQrcodeUrl(null);
				staff.setPasswd(null); 	//使用旧的密码
			}
			staff.setUpdateOpr(updateOpr); //设置当前用户为操作员
			jsonRet = PartnerStaffService.saveStaff(myPartner.getPartnerId(), staff, partnerPasswd);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 超级管理人员删除员工
	 * @param rootPasswd		超级管理人员的密码：合作伙伴绑定会员的密码
	 * @param userId			被删除的员工
	 * @return
	 */
	@RequestMapping("/deleteStaff")
	@ResponseBody
	public String deleteStaff(@RequestParam(value="userId",required=true)Integer userId,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			VipBasic vip = (VipBasic) session.getAttribute("vipBasic");
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");//我的合作伙伴
			if(vip == null || vip.getVipId() == null || !"1".equals(vip.getStatus()) ||
					myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errmsg", "您当前不可进行员工管理！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			String partnerPasswd = (String) session.getAttribute("partnerPasswd");
			//数据保存
			jsonRet = PartnerStaffService.deleteStaff(myPartner.getPartnerId(), partnerPasswd, userId);
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 超级管理人员重置密码
	 * @param userId			被重设密码的员工
	 * @param newPasswd		新密码
	 * @return
	 */
	@RequestMapping("/resetpwd")
	@ResponseBody
	public String resetPwd(@RequestParam(value="userId",required=true)Integer userId,
			@RequestParam(value="newPasswd",required=true)String newPasswd,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			String partnerUserTP = (String)  session.getAttribute("partnerUserTP");
			String partnerPasswd = (String)  session.getAttribute("partnerPasswd");
			VipBasic vip = (VipBasic)  session.getAttribute("partnerBindVip");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			if("bindVip".equals(partnerUserTP)) {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的会员信息失败！");
					return jsonRet.toJSONString();
				}
			}else {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "该功能由超级管理员进行管理！");
				return jsonRet.toJSONString();
			}
			if(newPasswd == null || newPasswd.length()<6 || newPasswd.length()>20) {
				jsonRet.put("errmsg", "新操作密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//超级密码检查
			jsonRet = PartnerStaffService.resetPwd(myPartner.getPartnerId(), partnerPasswd, userId, newPasswd);
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 使用原密码修改
	 * @param userId
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	@RequestMapping("/updpwd")
	@ResponseBody
	public String updatePwd(@RequestParam(value="oldPasswd",required=true)String oldPasswd,
			@RequestParam(value="newPasswd",required=true)String newPasswd,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			String partnerUserTP = (String)  session.getAttribute("partnerUserTP");
			PartnerStaff myStaff = (PartnerStaff)  session.getAttribute("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			Integer updateOpr = null;
			if("bindVip".equals(partnerUserTP)) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "该功能由员工进行自我管理！");
				return jsonRet.toJSONString();
			}else {
				if(myStaff == null || myStaff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = myStaff.getUserId();
			}
			if(newPasswd == null || newPasswd.length()<6 || newPasswd.length()>20) {
				jsonRet.put("errmsg", "新操作密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			if(oldPasswd == null || oldPasswd.length()<6 || oldPasswd.length()>20) {
				jsonRet.put("errmsg", "原操作密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			jsonRet = PartnerStaffService.updatePwd(myPartner.getPartnerId(), oldPasswd, updateOpr, newPasswd);
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	/**
	 * 头像与客服二维码上传：员工自我维护
	 * @param image		照片
	 * @param mode	照片类型：headimg、kfqrcode
	 * @param userId		当前操作用户
	 * @param passwd		操作密码
	 * @return {errcode:0,errmsg:"",filename:''}
	 */
	@RequestMapping("/upload/{mode}")
	@ResponseBody
	public String uploadImg(@PathVariable("mode")String mode,
			@RequestParam(value="image")MultipartFile image,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		File tmpImg = null;
		try {
			//数据检查与权限校验
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");
			String partnerUserTP = (String)  session.getAttribute("partnerUserTP");
			String partnerPasswd = (String)  session.getAttribute("partnerPasswd");
			PartnerStaff myStaff = (PartnerStaff)  session.getAttribute("partnerStaff");
			if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "您还未开通合作伙伴或状态限制！");
				return jsonRet.toJSONString();
			}
			Integer updateOpr = null;
			if("bindVip".equals(partnerUserTP)) {
				jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
				jsonRet.put("errmsg", "该功能由员工进行自我管理！");
				return jsonRet.toJSONString();
			}else {
				if(myStaff == null || myStaff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = myStaff.getUserId();
			}
			int limitSize = 3*1024*1024; //3M
			if(!"headimg".equals(mode) && !"kfqrcode".equals(mode)) {
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				jsonRet.put("errmsg", "系统访问参数不正确！");
				return jsonRet.toString();
			}
			if(image == null || image.isEmpty()) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "图片信息不可为空！");
				return jsonRet.toString();
			}
			//文件判断
			String imgType = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')+1);
			if(!"jpg".equalsIgnoreCase(imgType) && !"jpeg".equalsIgnoreCase(imgType) && !"png".equalsIgnoreCase(imgType)) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "图片文件必须是jpg,jpeg,png格式！");
				return jsonRet.toString();
			}
			if(image.getSize() > limitSize) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "图片文件不可大于3M！");
				return jsonRet.toString();
			}
			File dir = new File(this.tmpFileDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			tmpImg = new File(dir,image.getOriginalFilename()); //生成临时文件
			FileUtils.copyInputStreamToFile(image.getInputStream(), tmpImg);
			jsonRet = PartnerStaffService.uploadImg(myPartner.getPartnerId(), tmpImg, mode, updateOpr, partnerPasswd);
			
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}finally {
			if(tmpImg != null) {
				tmpImg.delete();
			}
		}
		return jsonRet.toString();
	}
	
	/**
	 * 头像与客服二维码显示
	 * 保存相对路径：staff
	 * 1、头像保存名称：[userid]_headimg.xxx；
	 * 2、客服二维码保存名称：[userid]_kfqrcode.xxx;
	 * @param partnerId
	 * @param userId
	 * @param mode
	 * @param out
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/{partnerId}/show/{userId}/{mode}")
	public void showImg(@PathVariable("partnerId")Integer partnerId,
			@PathVariable("userId")Integer userId,
			@PathVariable("mode")String mode,
			OutputStream out,HttpServletRequest request,HttpServletResponse response) throws IOException {
		if(!"headimg".equals(mode) && !"kfqrcode".equals(mode)) {
			return;
		}
		try {
			File file = PartnerStaffService.showImg(partnerId, mode, userId);
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
	
	
	
	/**
	 * 查询指定查询条件、排序条件、分页条件的信息；
	 * @param jsonSearchParams	查询条件:{recId,userId,staffId,nickname,email,phone,isKf,tagId,updateOpr,status,beginUpdateTime,endUpdateTime}
	 * @param pageCond		分页信息:{begin:, pageSize:}
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
			if("bindVip".equals(partnerUserTP)) {
				if(vip == null || !"1".equals(vip.getStatus())) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的会员信息失败！");
					return jsonRet.toJSONString();
				}
			}else {
				if(myStaff == null || myStaff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				search.put("userId", myStaff.getUserId());
			}
			jsonRet = PartnerStaffService.getPartnersAll(myPartner.getPartnerId(), search, pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
	private PartnerStaff getStaffByUser(Integer partnerId,Integer userId) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = new JSONObject();
			search.put("userId", userId);
			jsonRet = PartnerStaffService.getPartnersAll(partnerId, search, new PageCond(0,1));
			if(jsonRet.containsKey("datas")) {
				List<PartnerStaff> list = JSONArray.parseArray(jsonRet.getJSONArray("datas").toJSONString(), PartnerStaff.class);
				if(list != null && list.size()>0) {
					return list.get(0);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return null;
	}
}
