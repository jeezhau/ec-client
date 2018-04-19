package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 商品评论管理
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/appraise")
public class CommentAction {
	
	/**
	 * 获取商品评论显示首页
	 * @return
	 */
	@RequestMapping("/index/{goodsId}")
	public String getCommentShowIndex(@PathVariable("goodsId")String goodsId,ModelMap map) {
		//返回商品信息
		
		return "page-appraise-show";
	}
	
	
	/**
	 * 获取指定商品的所有评论
	 * @return
	 */
	@RequestMapping("/get/{goodsId}")
	@ResponseBody
	public String getAll(@PathVariable("goodsId")String goodsId,PageCond pageCond) {
		
		
		return "";
	}
	
	
	/**
	 * 添加评论
	 * @param jsonParam	评论参数
	 * @return
	 */
	@RequestMapping("/create")
	@ResponseBody
	public String addAppraise(String jsonParam) {
		
		return "";
	}
	
	
	/**
	 * 修改添加评论
	 * @param jsonParam	评论参数
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String updateAppraise(String jsonParam) {
		
		return "";
	}

}
