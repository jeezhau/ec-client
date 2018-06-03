package com.mofangyouxuan.wx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.wx.utils.HttpUtils;

/**
 * 合作伙伴图库管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/pimage")
@SessionAttributes({"sys_func","partnerUserTP","partnerPasswd","partnerStaff","partnerBindVip","myPartner"})
public class PartnerImageAction {
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	@Value("${mfyx.mfyx-server-url}")
	private String mfyxServerUrl;
	@Value("${mfyx.pimage-file-upload-url}")
	private String imageUplodFileUrl;
	@Value("${mfyx.pimage-folder-create-url}")
	private String imageCreateFolderUrl;
	@Value("${mfyx.pimage-folder-list-url}")
	private String imageListFilesUrl;
	@Value("${mfyx.pimage-file-show-url}")
	private String imageShowFileurl;
	
	/**
	 * 获取图库管理首页
	 * @param map
	 * @return
	 */
	@RequestMapping("/manage")
	public String getMangeIndex(ModelMap map) {
		PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
		if(myPartner == null || !("S".equals(myPartner.getStatus()) || "C".equals(myPartner.getStatus()))) {
			map.put("errmsg", "您还未开通合作伙伴或状态限制！");
			return "forward:/partner/manage" ;
		}
		map.put("sys_func", "partner-image");
		return "pimage/page-image-manage";
	}
	
	
	/**
	 * 图片上传
	 * @param folderPath		所属文件夹 Home/.....
	 * @param image		照片,jpg格式
	 * @return {errcode:0,errmsg:"",filename:''}
	 */
	@RequestMapping("/file/upload")
	@ResponseBody
	public String uploadFile(@RequestParam(value="folderPath",required=true)String folderPath,
			@RequestParam(value="image")MultipartFile image,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		File tmpFile = null;
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			String partnerUserTP = (String) map.get("partnerUserTP");
			String partnerPasswd = (String) map.get("partnerPasswd");
			VipBasic vip = (VipBasic) map.get("partnerBindVip");
			PartnerStaff staff = (PartnerStaff) map.get("partnerStaff");
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
				if(staff == null || staff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = staff.getUserId();
			}
			//文件类型判断
			if(image == null || image.isEmpty()) {
				jsonRet.put("errcode", ErrCodes.PARTNER_PARAM_ERROR);
				jsonRet.put("errmsg", "图片信息不可为空！");
				return jsonRet.toString();
			}
			String imgType = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')+1);
			if(!"jpg".equalsIgnoreCase(imgType) && !"jpeg".equalsIgnoreCase(imgType) && !"png".equalsIgnoreCase(imgType)) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "图片文件必须是jpg,jpeg,png格式！");
				return jsonRet.toString();
			}
			if(!folderPath.startsWith("Home")) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "文件归属目录须是 Home/.... ！");
				return jsonRet.toString();
			}
			
			//数据处理
			String url = this.mfyxServerUrl + this.imageUplodFileUrl;
			url = url.replace("{partnerId}", myPartner.getPartnerId()+"");
			Map<String,String> params = new HashMap<String,String>();
			params.put("folderPath", folderPath);
			params.put("currUserId", "" + updateOpr);
			params.put("passwd", partnerPasswd);
			File dir = new File(this.tmpFileDir);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			tmpFile = new File(dir,image.getOriginalFilename()); //生成临时文件
			FileUtils.copyInputStreamToFile(image.getInputStream(), tmpFile);
			String strRet = HttpUtils.uploadFile(url, tmpFile, "image", params);
			return strRet;
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}finally {
			if(tmpFile != null) {
				tmpFile.delete();
			}
		}
		return jsonRet.toString();
		
	}
	
	/**
	 * 新建文件夹
	 * @param upFolder	上级文件路径
	 * @param folderName	新建文件夹名称
	 * @return
	 */
	@RequestMapping("/folder/create")
	@ResponseBody
	public Object createFolder(@RequestParam(value="upFolderPath",required=true)String upFolderPath,
			@RequestParam(value="folderName",required=true)String folderName,
			ModelMap map){
		JSONObject jsonRet = new JSONObject();
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			String partnerUserTP = (String) map.get("partnerUserTP");
			String partnerPasswd = (String) map.get("partnerPasswd");
			VipBasic vip = (VipBasic) map.get("partnerBindVip");
			PartnerStaff staff = (PartnerStaff) map.get("partnerStaff");
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
				if(staff == null || staff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = staff.getUserId();
			}
			folderName = folderName.trim();
			if(!folderName.matches("^[a-zA-Z0-9_\u4e00-\u9fa5]{2,10}$")) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "新建文件夹名称长度为2-10个子母、数字、汉字字符！ " );
				return jsonRet.toString();
			}
			if("Home".equals(folderName)) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "新建文件夹名称不可是保留词汇：Home ！" );
				return jsonRet.toString();

			}
			if(!upFolderPath.startsWith("Home")) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "文件夹归属目录(上级目录)须是 Home/.... ！");
				return jsonRet.toString();
			}
			
			//数据处理
			String url = this.mfyxServerUrl + this.imageCreateFolderUrl;
			url = url.replace("{partnerId}",myPartner.getPartnerId() + "");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("upFolderPath", upFolderPath);
			params.put("folderName", folderName);
			params.put("currUserId", "" + updateOpr);
			params.put("passwd", partnerPasswd);
			String strRet = HttpUtils.doPost(url, params);
			return strRet;
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 列出文件夹下的所有文件
	 * @param folderName
	 * @param currUserId
	 * @return {errcode:0,errmsg:'ok',files:[]}
	 */
	@RequestMapping("/folder/list")
	@ResponseBody
	public Object listFiles(@RequestParam(value="folderPath",required=true)String folderPath,ModelMap map) {
		JSONObject jsonRet = new JSONObject();
		try {
			PartnerBasic myPartner = (PartnerBasic) map.get("myPartner");
			String partnerUserTP = (String) map.get("partnerUserTP");
			String partnerPasswd = (String) map.get("partnerPasswd");
			VipBasic vip = (VipBasic) map.get("partnerBindVip");
			PartnerStaff staff = (PartnerStaff) map.get("partnerStaff");
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
				if(staff == null || staff.getPartnerId() == null) {
					jsonRet.put("errcode", ErrCodes.COMMON_PRIVILEGE_ERROR);
					jsonRet.put("errmsg", "系统获取您的员工信息失败！");
					return jsonRet.toJSONString();
				}
				updateOpr = staff.getUserId();
			}
			if(!folderPath.startsWith("Home")) {
				jsonRet.put("errcode", ErrCodes.IMAGE_PARAM_ERROR);
				jsonRet.put("errmsg", "文件夹目录须是 Home/.... ！");
				return jsonRet.toString();
			}
			//数据处理
			String url = this.mfyxServerUrl + this.imageListFilesUrl;
			url = url.replace("{partnerId}",myPartner.getPartnerId() + "");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("folderPath", folderPath);
			params.put("currUserId", "" + updateOpr);
			params.put("passwd",partnerPasswd);
			String strRet = HttpUtils.doPost(url, params);
			return strRet;
		} catch (Exception e) {
			e.printStackTrace();
			jsonRet = new JSONObject();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 显示合作伙伴图库图片
	 * @param filename
	 * @return
	 */
	@RequestMapping("/file/show/{partnerId}/{filename}")
	public void showFile(@PathVariable(value="partnerId",required=true)Integer partnerId,
			@PathVariable(value="filename",required=true)String filename,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		try {
			//数据检查
			if(partnerId == null || partnerId < 1 ) {
				return;
			}
			if("undefined".equals(filename)) {
				return;
			}
			String url = this.mfyxServerUrl + this.imageShowFileurl; 
			url = url.replace("{partnerId}",partnerId + "");
			url = url.replace("{filename}",filename);	
			File file = HttpUtils.downloadFile(this.tmpFileDir,url);
			InputStream is = new FileInputStream(file);
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


