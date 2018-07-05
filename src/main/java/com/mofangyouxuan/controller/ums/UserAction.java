package com.mofangyouxuan.controller.ums;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
import com.mofangyouxuan.common.SysParam;
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.CollectionService;
import com.mofangyouxuan.service.UserService;
import com.mofangyouxuan.service.VipService;

/**
 * 个人中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes({"clientPF","openId","vipBasic","userBasic","isDayFresh","sys_func"})
public class UserAction {
	
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	
	private String regexpPhone = "1[3-9]\\d{9}";
	private String regexpEmail = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	
	/**
	 * 用户管理主页面-我之基本
	 * 重新获取用户信息
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/index/{mode}")
	public String getIndex(@PathVariable("mode")String mode,ModelMap map,HttpServletRequest request){
		UserBasic user = (UserBasic) map.get("userBasic");
		if(!"basic".equals(mode) && !"vip".equals(mode)) {
			mode = "basic";
		}
		//重新获取用户基本信息，会员信息
		String openId = (String)map.get("openId");
		UserBasic userBasic = UserService.getUserBasic(openId);
		if(userBasic != null) {
			map.put("userBasic", userBasic);
		}
		VipBasic vipBasic = VipService.getVipBasic(user.getUserId());
		if(vipBasic != null) {
			map.put("vipBasic", vipBasic);
		}
		JSONObject jsonCnt = CollectionService.getCnt(user.getUserId());
		if(jsonCnt != null && jsonCnt.containsKey("cnt") && jsonCnt.getIntValue("cnt")>0) {
			map.put("collCnt", jsonCnt.getIntValue("cnt"));
		}
		map.put("mode", mode);
		map.put("sys_func", "user");
		
		map.put("isFirstWxPage", request.getAttribute("isFirstWxPage"));
		return "user/page-user-index";
		
	}
	
	
	/**
	 * 根据OPENID获取用户基本信息
	 * @param openId
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/basic/get")
	@ResponseBody
	public Object getUserBasci(ModelMap map)   {
		String openId = (String) map.get("openId");
		JSONObject jsonRet = new JSONObject();
		UserBasic basic = UserService.getUserBasic(openId);
		if(basic != null) {//成功
			jsonRet.put("errcode", 0);
			jsonRet.put("errmsg", "ok");
			jsonRet.put("datas", JSONObject.toJSON(basic));
			map.put("userBasic", basic);
		}else {
			jsonRet.put("errcode", -1);
			jsonRet.put("errmsg", "获取用户基本信息失败！");
		}
		return jsonRet.toString();
	}

	/**
	 * 获取更新基本信息页面
	 * @return
	 */
	@RequestMapping("/basic/edit")
	public String editBasic(ModelMap map) {
		map.put("localServerUrl", localServerUrl);
		return "user/page-basic-edit";
	}
	
	/**
	 * 将收到的修改请求发送至服务中心
	 * @param userBasic
	 * @param result
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value="/basic/update",method=RequestMethod.POST)
	@ResponseBody
	public String updateBasic(@Valid UserBasic basic,BindingResult result,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
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
			
			basic.setOpenId((String) map.get("openId"));
//			if(openId == null || openId.length()<6) {
//				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
//				jsonRet.put("errmsg", " openid: not null and length range is 6-100. ");
//				return jsonRet.toString();
//			}
			return UserService.updateUserBasic(basic);
		}catch(Exception e) {
			//数据处理
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
		
	}
	
	/**
	 * 获取用户推广信息
	 * 1、取已推广用户数量；
	 * 2、获取最新推广二维码；
	 * @param create		是否强制重新生成
	 * @param map
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping("/spread")
	public String getSpread(String create,ModelMap map)  {
		String openId = (String)map.get("openId");
		if(create != null && "1".equals(create.trim())){
			create = "1";
		}else {
			create = "0";
		}
		JSONObject ret = UserService.getSpreadQrCode(openId,create);
		if(ret.containsKey("errcode") && ret.getIntValue("errcode") == 0) {
			map.put("showurl", ret.getString("showurl"));
			map.put("count", ret.getIntValue("count"));
		}else {
			map.put("errmsg", ret.getString("errmsg"));
		}
		map.put("sysparam_spread_user_profit_ratio", SysParam.getSysParam("spread_user_profit_ratio"));
		map.put("sysparam_spread_per_user_score", SysParam.getSysParam("spread_per_user_score"));
		map.put("sysparam_vip_activate_need_score", SysParam.getSysParam("vip_activate_need_score"));
		map.put("sysparam_trade_score_one_yuan", SysParam.getSysParam("trade_score_one_yuan"));
		
		return "user/page-spread-qrcode";
		
	}
	
	
	
	/**
	 * 头像上传
	 * @param image		照片,jpg格式
	 * @return
	 */
	@RequestMapping("/headimg/upload")
	@ResponseBody
	public String uploadHeadimg(@RequestParam(value="image")MultipartFile image,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic)map.get("userBasic");
		File tmpImg = null;
		try {
			if(image == null || image.isEmpty()) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "头像照片信息不可为空！");
				return jsonRet.toString();
			}
			//文件类型判断
			String imgType = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')+1);
			if(!"jpg".equalsIgnoreCase(imgType) && !"jpeg".equalsIgnoreCase(imgType) && !"png".equalsIgnoreCase(imgType)) {
				jsonRet.put("errcode", -888);
				jsonRet.put("errmsg", "头像图片文件必须是jpg,jpeg,png格式！");
				return jsonRet.toString();
			}
			
			//数据检查
			if(user == null || !"1".equals(user.getStatus()) ) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该用户！");
				return jsonRet.toString();
			}
			File dir = new File(this.tmpFileDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			tmpImg = new File(dir,image.getOriginalFilename()); //生成临时文件
			FileUtils.copyInputStreamToFile(image.getInputStream(), tmpImg);
			
			jsonRet = UserService.uploadHeadImg(tmpImg, user.getUserId());
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
				jsonRet.put("errmsg", "头像上传失败！");
			}else {
				if(jsonRet.containsKey("filename")) {
					user.setHeadimgurl(jsonRet.getString("filename"));
					map.put("userBasic", user);
				}
			}
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
	 * 显示头像图片
	 * @param userId
	 * @return
	 */
	@RequestMapping("/headimg/show/{userId}")
	public void showCert(@PathVariable(value="userId",required=true)Integer userId,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		try {
			File file = UserService.showHeadimg(userId);
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
	
	/**
	 * 获取用户设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/setting")
	public String getSettingIndex(ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		if(user == null || !"1".equals(user.getStatus())) {
			map.put("errmsg", "系统中没有该用户信息！");
		}
		map.put("sys_func", "user");
		return "user/page-setting-index";
	}
	
	
	/**
	 * 获取手机设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/phone/manage")
	public String getPhoneMgr(ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		if(user == null || !"1".equals(user.getStatus())) {
			map.put("errmsg", "系统中没有该用户信息！");
		}
		
		return "user/page-setting-phone";
	}

	
	/**
	 * 获取邮箱设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/email/manage")
	public String getEmailMgr(ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		if(user == null || !"1".equals(user.getStatus())) {
			map.put("errmsg", "系统中没有该用户信息！");
		}
		
		return "user/page-setting-email";
	}

	/**
	 * 获取密码设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/passwd/manage")
	public String getPasswdMgr(ModelMap map) {
		UserBasic user = (UserBasic) map.get("userBasic");
		if(user == null || !"1".equals(user.getStatus())) {
			map.put("errmsg", "系统中没有该用户信息！");
		}
		
		return "user/page-setting-passwd";
	}
	
	/**
	 * 重置登录密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/setting/resetpwd",method=RequestMethod.POST)
	@ResponseBody
	public String resetPwd(@RequestParam(value="type",required=true)String type,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		UserBasic user = (UserBasic) map.get("userBasic");
		try {
			//数据验证
			type = type.trim();
			if(!type.matches("[12]")) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码的重置媒介类型不正确（1-手机，2-邮箱）！");
				return jsonRet.toString();
			}
			if(user == null || !"1".equals(user.getStatus())) {
				jsonRet.put("errcode", ErrCodes.USER_NO_EXISTS);
				jsonRet.put("errmsg", "系统中没有该用户！");
				return jsonRet.toString();
			}
			if("1".equals(type) && (user.getPhone() == null || user.getPhone().length()<11)){
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未绑定手机号，请先完成手机号的绑定！");
				return jsonRet.toString();
			}
			if("2".equals(type) && (user.getEmail() == null || user.getEmail().length()<3)){
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "您还未绑定邮箱，请先完成邮箱的绑定！");
				return jsonRet.toString();
			}
			jsonRet = UserService.resetPwd(user.getUserId(), type);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "更新用户登录密码信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}


	/**
	 * 更新登录密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/setting/updpwd",method=RequestMethod.POST)
	@ResponseBody
	public String updPasswd(@RequestParam(value="oldPwd",required=true)String oldPwd,
			@RequestParam(value="newPwd",required=true)String newPwd,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			if(user == null || !"1".equals(user.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该用户！");
				return jsonRet.toJSONString();
			}
			oldPwd = oldPwd.trim();
			if(oldPwd.length()<6 || oldPwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "原密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			newPwd = newPwd.trim();
			if(newPwd.length()<6 || newPwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			jsonRet = UserService.updPwd(user.getUserId(), oldPwd, newPwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "更新会员密码信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}


	/**
	 * 更新绑定手机号
	 * @param oldVeriCode
	 * @param newPhone
	 * @param newVeriCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/setting/updphone",method=RequestMethod.POST)
	@ResponseBody
	public Object updPhone(String oldVeriCode,@RequestParam(value="newPhone",required=true)String newPhone,
			@RequestParam(value="newVeriCode",required=true)String newVeriCode,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			if(user == null || !"1".equals(user.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该用户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			if(user.getPhone() != null && user.getPhone().length()>=11) {
				if(oldVeriCode == null) {
					oldVeriCode = "";
				}
				oldVeriCode = oldVeriCode.trim();
				if(oldVeriCode.length() != 6 ) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "原手机号短信验证码为6位字符！");
					return jsonRet.toString();
				}
			}
			newPhone = newPhone.trim();
			if(!newPhone.matches(regexpPhone)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","新手机号格式不正确！");
				return jsonRet;
			}
			newVeriCode = newVeriCode.trim();
			if(newVeriCode.length() != 6 ) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新手机号短信验证码为6位字符！");
				return jsonRet.toString();
			}
			jsonRet = UserService.updPhone(user.getUserId(), oldVeriCode, newPhone, newVeriCode);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "绑定手机号失败！");
			}else {
				if(jsonRet.getIntValue("errcode") == 0) {
					user.setPhone(newPhone);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	

	/**
	 * 更新绑定邮箱
	 * @param oldVeriCode
	 * @param newEmail
	 * @param newVeriCode
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/setting/updemail",method=RequestMethod.POST)
	@ResponseBody
	public Object updEmail(String oldVeriCode,
			@RequestParam(value="newEmail",required=true)String newEmail,
			@RequestParam(value="newVeriCode",required=true)String newVeriCode,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			UserBasic user = (UserBasic) map.get("userBasic");
			if(user == null || !"1".equals(user.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "系统中没有该用户！");
				return jsonRet.toJSONString();
			}
			//数据格式验证
			if(user.getEmail() != null && user.getEmail().length()>=3) {
				if(oldVeriCode == null) {
					oldVeriCode = "";
				}
				oldVeriCode = oldVeriCode.trim();
				if(oldVeriCode.length() != 6 ) {
					jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
					jsonRet.put("errmsg", "原邮箱验证码为6位字符！");
					return jsonRet.toString();
				}
			}
			newEmail = newEmail.trim();
			if(!newEmail.matches(regexpEmail)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","新邮箱格式不正确！");
				return jsonRet;
			}
			newVeriCode = newVeriCode.trim();
			if(newVeriCode.length() != 6 ) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "新邮箱验证码为6位字符！");
				return jsonRet.toString();
			}
			jsonRet = UserService.updEmail(user.getUserId(), oldVeriCode, newEmail, newVeriCode);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "绑定邮箱失败！");
			}else {
				if(jsonRet.getIntValue("errcode") == 0) {
					user.setEmail(newEmail);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 申请手机短信验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/vericode/phone/apply",method=RequestMethod.POST)
	@ResponseBody
	public Object applyPhoneVeriCode(@RequestParam(value="phone",required=true)String phone,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
//			UserBasic user = (UserBasic) map.get("userBasic");
//			if(user == null || !"1".equals(user.getStatus())) {
//				jsonRet.put("errmsg", "系统中没有该用户！");
//				jsonRet.put("errcode", -1);
//				return jsonRet;
//			}
			if(!phone.matches(regexpPhone)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","手机号格式不正确！");
				return jsonRet;
			}
			jsonRet = UserService.getPhoneVeriCode(phone);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取短信验证码失败！");
			}
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 申请邮箱短信验证码
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/vericode/email/apply",method=RequestMethod.POST)
	@ResponseBody
	public Object applyEmailVeriCode(@RequestParam(value="email",required=true)String email,
			ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
//			UserBasic user = (UserBasic) map.get("userBasic");
//			if(user == null || !"1".equals(user.getStatus())) {
//				jsonRet.put("errmsg", "您当前还未开通会员账户！");
//				jsonRet.put("errcode", -1);
//				return jsonRet;
//			}
			if(!email.matches(regexpEmail)) {
				jsonRet.put("errcode","-1");
				jsonRet.put("errmsg","邮箱格式不正确！");
				return jsonRet;
			}
			jsonRet = UserService.getEmialVeriCode(email);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取邮箱验证码失败！");
			}
		}catch(Exception e) {
			//数据处理
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
}

