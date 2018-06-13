package com.mofangyouxuan.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONObject;
import com.mofangyouxuan.common.ErrCodes;
import com.mofangyouxuan.dto.Category;
import com.mofangyouxuan.dto.Goods;
import com.mofangyouxuan.dto.PartnerBasic;
import com.mofangyouxuan.dto.PartnerStaff;
import com.mofangyouxuan.dto.VipBasic;
import com.mofangyouxuan.service.GoodsService;
import com.mofangyouxuan.service.PartnerMgrService;
import com.mofangyouxuan.service.PartnerStaffService;
import com.mofangyouxuan.utils.HttpUtils;
import com.mofangyouxuan.utils.PageCond;

/**
 * 商城管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/shop")
@SessionAttributes({"clientPF","isDayFresh","sys_func","categories"})
public class ShopAction {
	
	@Value("${sys.tmp-file-dir}")
	private String tmpFileDir;
	@Value("${mfyx.mfyx-server-url}")
	private String mfyxServerUrl;
	@Value("${mfyx.pimage-file-show-url}")
	private String imageShowFileurl;
	private String[] certTypeArr = {"logo","idcard1","idcard2","licence"}; 	//当前支持的证件类型
	
	/**
	 * 获取商城管理主页
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/index")
	public String getIndex(ModelMap map,HttpServletRequest request) {
		List<Category> categories = (List<Category>) map.get("categories");
		if(categories == null) {
			categories = GoodsService.getCategories();
			map.put("categories", categories);
		}
		map.put("isFirstWxPage", request.getAttribute("isFirstWxPage"));
		map.put("isDayFresh", "0");	//系统默认为访问商城管理
		map.put("sys_func", "shop");
		
		return "shop/page-shop-index";
	}
	
	@RequestMapping("/getall")
	@ResponseBody
	public String getShopAllGoods(Integer categoryId,String keywords,PageCond pageCond,
			String city,String area,String lat,String lng) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			params.put("isSelf", false);
			if(categoryId != null && categoryId>0) {
				params.put("categoryId", categoryId);	
			}
			if(keywords != null && keywords.trim().length()>1) {
				params.put("keywords", keywords.trim());	
			}
			JSONObject sortParams = new JSONObject();
			sortParams.put("sale", "1#1");
			sortParams.put("time", "2#1");
			//{errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
			jsonRet = GoodsService.searchGoods(true,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}

	/**
	 * 任何人获取指定商品的详细信息并展示，包含展示部分合作伙伴信息
	 * @param goodsId
	 * @param map
	 * @return
	 */
	@RequestMapping("/goods/{goodsId}")
	public String showGoods(@PathVariable("goodsId")Long goodsId,ModelMap map) {
		Goods goods = null;
		try {
			JSONObject obj = GoodsService.getGoods(true,goodsId,true);
			if(obj == null || !obj.containsKey("goods")) {
				map.put("errmsg", "获取商品详情失败！");
			}else {
				goods = JSONObject.toJavaObject(obj.getJSONObject("goods"),Goods.class);
				if(goods != null && "S".equals(goods.getPartner().getStatus()) && 
						"1".equals(goods.getStatus()) && "1".equals(goods.getReviewResult())) {
					map.put("goods", goods);
				}else {
					map.put("errmsg", "该商品当前不可访问！");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			map.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return "shop/page-goods-show";
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
	public String showMcht(@PathVariable("partnerId")Integer partnerId,ModelMap map) {

		PartnerBasic partner = PartnerMgrService.getPartnerById(partnerId);
		if(partner == null || !"S".equals(partner.getStatus())) {
			map.put("errmsg", "系统中没有该商户信息！");
		}else {
			map.put("mcht", partner);
		}
		return "shop/page-mcht-index";
	}
	
	@RequestMapping("/mcht/getall/{partnerId}")
	@ResponseBody
	public String getMchtAllGoods(@PathVariable("partnerId")Integer partnerId,PageCond pageCond) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject params = new JSONObject();
			params.put("isSelf", false);
			params.put("partnerId", partnerId);	
			JSONObject sortParams = new JSONObject();
			sortParams.put("sale", "1#1");
			sortParams.put("time", "2#1");
			jsonRet = GoodsService.searchGoods(true,params.toJSONString(), sortParams.toString(), JSONObject.toJSONString(pageCond));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "出现异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 显示商品图片
	 * @param filename
	 * @return
	 */
	@RequestMapping("/gimage/{partnerId}/{filename}")
	public void showGoodsFile(@PathVariable(value="partnerId",required=true)Integer partnerId,
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
	

	/**
	 * 显示证件图片
	 * @param certType
	 * @return
	 */
	@RequestMapping("/pcert/{certType}/{partnerId}")
	public void showPartnerCert(@PathVariable(value="certType",required=true)String certType,
			@PathVariable(value="partnerId",required=true)Integer partnerId,
			OutputStream out,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		try {
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
			File file = PartnerMgrService.showCert(partnerId, certType);
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
	 * 获取指定合作伙伴的客服信息
	 * @return {errcode:0,errmsg:"ok",pageCond:{},datas:[{}...]} 
	 */
	@RequestMapping("/kfstaff/getall/{partnerId}")
	@ResponseBody
	public Object getPartnerKf(@PathVariable("partnerId")Integer partnerId,
			HttpSession session) {
		JSONObject jsonRet = new JSONObject();
		try {
			JSONObject search = new JSONObject();
			search.put("isKf", "1");
			jsonRet = PartnerStaffService.getPartnersAll(partnerId, search, new PageCond(0,10));
		}catch(Exception e) {
			e.printStackTrace();
			jsonRet.put("errcode", ErrCodes.COMMON_EXCEPTION);
			jsonRet.put("errmsg", "系统异常，异常信息：" + e.getMessage());
		}
		return jsonRet.toString();
	}
	
	/**
	 * 获取合作伙伴的客服信息
	 * 【权限人】
	 * 任何人
	 * @param partnerId
	 * @param map
	 * @return
	 */
	@RequestMapping("/kfshow/{partnerId}")
	public String showPartnerKf(@PathVariable("partnerId")Integer partnerId,ModelMap map) {
		PartnerBasic partner = PartnerMgrService.getPartnerById(partnerId);
		if(partner == null || !"S".equals(partner.getStatus())) {
			map.put("errmsg", "系统中没有该商户信息！");
		}else {
			map.put("partner", partner);
		}
		return "shop/page-partnerkf-show";
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
	@RequestMapping("/kfqrcode/{partnerId}/show/{userId}")
	public void showKFQrcode(@PathVariable("partnerId")Integer partnerId,
			@PathVariable("userId")Integer userId,
			OutputStream out,HttpServletRequest request,HttpServletResponse response) throws IOException {
		String mode = "kfqrcode";
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
	
	
	
}
