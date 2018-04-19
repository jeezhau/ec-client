package com.mofangyouxuan.wx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mofangyouxuan.wx.utils.PageCond;

/**
 * 商品新获取 
 * @author jeekhan
 *
 */
@Controller
@RequestMapping("/goods")
@SessionAttributes({"isDayFresh","sys_func"})
public class GoodsAction {
	
	
	/**
	 * 获取商品详细信息
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/detail/{goodsId}")
	public String getDetail(@PathVariable("goodsId")String goodsId,ModelMap map) {
		
		return "page-goods-detail";
	}
	
	/**
	 * 根据关键字查询商品信息
	 * 返回满足匹配的前100条信息
	 * @param keywords
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public String search(String keywords) {
		
		return "";
	}
	
	/**
	 * 获取指定分类的商品数据
	 * @param category 类别名称	
	 * @param pageCond 分页信息 
	 * @return
	 */
	@RequestMapping("/getall/{category}")
	@ResponseBody
	public String getAllByCat(@PathVariable("category")String category,PageCond pageCond) {
		
		
		return "";
	}
	
	/**
	 * 获取所有商品数据
	 * @param pageCond 分页信息	
	 * @return
	 */
	@RequestMapping("/getall")
	@ResponseBody
	public String getAll(PageCond pageCond) {
		
		
		return "";
	}
	
	
}


