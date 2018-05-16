package com.mofangyouxuan.wx.controller;

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
import com.mofangyouxuan.dto.UserBasic;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.UserVipService;
import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 个人中心管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/user")
@SessionAttributes({"openId","vipBasic","userBasic","isDayFresh","sys_func"})
public class UserVipAction {
	
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	@Value("${sys.local-server-url}")
	private String localServerUrl;
	
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
	public String getIndex(@PathVariable("mode")String mode,ModelMap map){
		//UserBasic user = (UserBasic) map.get("userBasic");
		if(!"basic".equals(mode) && !"vip".equals(mode)) {
			mode = "basic";
		}
		//重新获取用户基本信息，会员信息
		String openId = (String)map.get("openId");
		UserBasic userBasic = UserVipService.getUserBasic(openId);
		if(userBasic != null) {
			map.put("userBasic", userBasic);
		}
		VipBasic vipBasic = UserVipService.getVipBasic(openId);
		if(vipBasic != null) {
			map.put("vipBasic", vipBasic);
		}
		map.put("mode", mode);
		map.put("sys_func", "user");
		
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
		UserBasic basic = UserVipService.getUserBasic(openId);
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
			return UserVipService.updateUserBasic(basic);
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
		JSONObject ret = UserVipService.getSpreadQrCode(openId,create);
		if(ret.containsKey("errcode") && ret.getIntValue("errcode") == 0) {
			map.put("showurl", ret.getString("showurl"));
			map.put("count", ret.getIntValue("count"));
		}else {
			map.put("errmsg", ret.getString("errmsg"));
		}
		return "user/page-spread-qrcode";
	}
	
	/**
	 * 获取资金流水显示页面
	 * @param map
	 * @return
	 */
	@RequestMapping("/flow/show")
	public String showFlow(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "user/page-show-change-flow";
	}
	
	
	/**
	 * 会员资金流水查询
	 * @param jsonParams  查询条件：{vipId,changeType, amountDown,amountUp,beginCrtTime,endCrtTime,beginSumTime,endSumTime,sumFlag,reason,createOpr}
	 * @param map
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/flow/getall")
	@ResponseBody
	public String getAllFlow(String jsonParams,PageCond pageCond,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			
			JSONObject params = JSONObject.parseObject(jsonParams);
			params.put("vipId", vip.getVipId());

			jsonRet = UserVipService.searchFlows(JSONObject.toJSONString(params), JSONObject.toJSONString(pageCond));
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取资金流水信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取账户设置首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/vipset")
	public String setActIndex(ModelMap map) {
		VipBasic vip = (VipBasic) map.get("vipBasic");
		if(vip == null || !"1".equals(vip.getStatus())) {
			map.put("errmsg", "您当前还未开通会员账户！");
		}
		
		return "user/page-vip-setting";
	}
	
	/**
	 * 更新资金操作密码
	 * @param pwd
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/vipset/updpwd",method=RequestMethod.POST)
	@ResponseBody
	public String updPwd(@RequestParam(value="pwd",required=true)String pwd,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		VipBasic vip = (VipBasic) map.get("vipBasic");
		try {
			if(vip == null || !"1".equals(vip.getStatus())) {
				jsonRet.put("errcode", ErrCodes.VIP_NO_USER);
				jsonRet.put("errmsg", "您当前还未开通会员账户！");
				return jsonRet.toJSONString();
			}
			pwd = pwd.trim();
			if(pwd.length()<6 || pwd.length()>20) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "密码长度为6-20位字符！");
				return jsonRet.toString();
			}
			jsonRet = UserVipService.updPayPwd(vip.getVipId(), pwd);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取资金流水信息失败！");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 更新会员资金操作密码
	 * @param vipId
	 * @param passwd
	 * @return
	 */
	@RequestMapping(value="/vipset/updact",method=RequestMethod.POST)
	@ResponseBody
	public Object updAccount(@RequestParam(value="vipId",required=true)Integer vipId,
			@RequestParam(value="actNm",required=true)String accountName,
			@RequestParam(value="actNo",required=true)String accountNo,
			@RequestParam(value="actBlk",required=true)String accountBank) {
		JSONObject jsonRet = new JSONObject();
		try {
			accountName = accountName.trim();
			accountNo = accountNo.trim();
			accountBank = accountBank.trim();
			if(accountName.length()<2 || accountName.length()>100) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "账户名长度为2-100位字符！");
				return jsonRet.toString();
			}
			if(accountNo.length()<3 || accountNo.length()>30) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "账户号长度为3-30位字符！");
				return jsonRet.toString();
			}
			if(accountBank.length()<2 || accountBank.length()>100) {
				jsonRet.put("errcode", ErrCodes.USER_PARAM_ERROR);
				jsonRet.put("errmsg", "开户行名称长度为2-100位字符！");
				return jsonRet.toString();
			}
			jsonRet = UserVipService.updAccount(vipId, accountName, accountNo, accountBank);
			if(jsonRet == null || !jsonRet.containsKey("errcode")) {
				jsonRet = new JSONObject();
				jsonRet.put("errcode", ErrCodes.COMMON_DB_ERROR);
				jsonRet.put("errmsg", "获取资金流水信息失败！");
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
			
			jsonRet = UserVipService.uploadHeadImg(tmpImg, user.getUserId());
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
			File file = UserVipService.showHeadimg(userId);
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
