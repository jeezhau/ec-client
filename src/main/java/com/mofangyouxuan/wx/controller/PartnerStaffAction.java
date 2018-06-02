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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.UserBasic;
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
@SessionAttributes({"clientPF"})
public class PartnerStaffAction {

	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	
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
			@RequestParam(value="passwd",required=true)String passwd,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) session.getAttribute("userBasic");
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");//我的合作伙伴
			if(user == null || user.getUserId() == null || !"1".equals(user.getStatus()) || 
					myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errmsg", "您当前不可进行员工管理！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
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
			//密码检查
			if(passwd == null || passwd.length()<6 || passwd.length()>20) {
				jsonRet.put("errmsg", "操作密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
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
			staff.setUpdateOpr(user.getUserId()); //设置当前用户为操作员
			jsonRet = PartnerStaffService.saveStaff(myPartner.getPartnerId(), staff, passwd);
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
	public String deleteStaff(@RequestParam(value="rootPasswd",required=true)String rootPasswd,
			@RequestParam(value="userId",required=true)Integer userId,HttpSession session) {
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
			//密码检查
			if(rootPasswd == null || rootPasswd.length()<6 || rootPasswd.length()>20) {
				jsonRet.put("errmsg", "会员密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//数据保存
			jsonRet = PartnerStaffService.deleteStaff(myPartner.getPartnerId(), rootPasswd, userId);
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
	 * @param rootPasswd		超级管理人员的密码：合作伙伴绑定会员的密码
	 * @param userId			被重设密码的员工
	 * @param newPasswd		新密码
	 * @return
	 */
	@RequestMapping("/resetpwd")
	@ResponseBody
	public String resetPwd(@RequestParam(value="rootPasswd",required=true)String rootPasswd,
			@RequestParam(value="userId",required=true)Integer userId,
			@RequestParam(value="newPasswd",required=true)String newPasswd,HttpSession session) {
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
			if(newPasswd == null || newPasswd.length()<6 || newPasswd.length()>20) {
				jsonRet.put("errmsg", "新操作密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			//超级密码检查
			if(rootPasswd == null || rootPasswd.length()<6 || rootPasswd.length()>20) {
				jsonRet.put("errmsg", "会员密码：长度范围【6-20字符】！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			
			jsonRet = PartnerStaffService.resetPwd(myPartner.getPartnerId(), rootPasswd, userId, newPasswd);
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
	public String updatePwd(@RequestParam(value="userId",required=true)Integer userId,
			@RequestParam(value="oldPasswd",required=true)String oldPasswd,
			@RequestParam(value="newPasswd",required=true)String newPasswd,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) session.getAttribute("userBasic");
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");//我的合作伙伴
			if(user == null || user.getUserId() == null || !"1".equals(user.getStatus()) ||
					myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errmsg", "您当前不可进行员工管理！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
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
			
			jsonRet = PartnerStaffService.updatePwd(myPartner.getPartnerId(), oldPasswd, userId, newPasswd);
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
			@RequestParam(value="passwd",required=true)String passwd,
			@RequestParam(value="image")MultipartFile image,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		File tmpImg = null;
		try {
			UserBasic user = (UserBasic) session.getAttribute("userBasic");
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");//我的合作伙伴
			if(user == null || user.getUserId() == null || !"1".equals(user.getStatus()) ||
					myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errmsg", "您当前不可进行员工管理！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
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
			jsonRet = PartnerStaffService.uploadImg(myPartner.getPartnerId(), tmpImg, mode, user.getUserId(), passwd);
			
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
	 * 2、客服二维码保存名称：[userid]_qrcode.xxx;
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
	public Object getAllByPartner(String jsonSearchParams,PageCond pageCond,HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) session.getAttribute("userBasic");
			PartnerBasic myPartner = (PartnerBasic) session.getAttribute("myPartner");//我的合作伙伴
			if(user == null || user.getUserId() == null || !"1".equals(user.getStatus()) ||
					myPartner == null || myPartner.getPartnerId() == null) {
				jsonRet.put("errmsg", "您当前不可进行员工查询！");
				jsonRet.put("errcode", ErrCodes.COMMON_PARAM_ERROR);
				return jsonRet.toString();
			}
			JSONObject search = JSONObject.parseObject(jsonSearchParams);
			jsonRet = PartnerStaffService.getPartnersAll(myPartner.getPartnerId(), search, pageCond);
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	
}
