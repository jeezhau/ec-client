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

	
}
