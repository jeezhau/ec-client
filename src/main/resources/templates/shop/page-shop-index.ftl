<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="position:relative;overflow:scroll;min-height:100%;">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<div id="goTopId" style="display:none;position:fixed;right:1px;bottom:200px;height:38px;width:38px;z-index:10000">
  <img alt="顶部" src="/icons/顶部.png" style="width:100%;height:100%" onclick="$(window).scrollTop(1)">
</div>
<div class="container " id="container" style="padding:0 1px; min-height:100%;">
  <header >
    <#include "/menu/page-category-menu.ftl" encoding="utf8"> 
  </header>
  <div class="row" style="margin:0 0;">
    <div v-for="goods in goodsList" class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:3px 2px;">
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/shop/goods/' + goods.goodsId"  target="_blank">
	        <img alt="" :src="'/shop/gimage/' + goods.partnerId + '/' + goods.mainImgPath" style="height:160px;max-width:100%">
	      </a>
	    </div>
	    <div class="row" style="margin:1px 1px;" >
	      <div style="margin:1px 0;padding:0 5px;background-color:white" >
		        {{goods.goodsName}}
	      </div>
	      <div class="row" style="margin:1px 0px;padding:0 5px;background-color:white;color:red" >
	      	<span class="pull-left ">售价: <span>{{goods.priceLowest}}</span>元</span>
	      	<span class="pull-right ">销量: <span>{{goods.saledCnt}}</span>件</span>
	      </div>
	      <div class="row" style="margin:1px 0 2px 0;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 3px" :href="'/order/place/'+ goods.goodsId" target="_blank"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 3px" href="javascript:;" @click="addCollection('2',goods.goodsId)" target="_blank"><span style="color:white">加入收藏</span></a>
	      </div>
	    </div>
    </div> 
 
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
 el:'#container',
 data:{
	param:{
		categoryId:'', 
		keywords:'',
		pageSize:20,
		begin:0,
		count:0
	},
	goodsList:[] 
 },
 methods:{
	 getAll: function(isRefresh,isFirst){
		 $("#loadingData").show();
		 $("#nomoreData").hide();
		 if(isRefresh){ //清空数据
			 containerVue.param.count = 0;
			 containerVue.goodsList = [];
		 }else{
			 if(containerVue.goodsList.lenght >= 300){
				 if(isFirst){//清除后一百条
					 containerVue.goodsList.splice(200,100);
				 }else{//清除前一百条
					 containerVue.goodsList.splice(0,100); 
				 }
			 }
		 }
		 $.ajax({
				url: '/shop/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errcode == 0){//
						var i = 0;
						var j = jsonRet.datas.length-1;
						for(;i<jsonRet.datas.length;){
							if(isFirst){
								containerVue.goodsList.unshift(jsonRet.datas[j]);
							}else{
								containerVue.goodsList.push(jsonRet.datas[i]);
							}
							i++;j--;
						}
						containerVue.param.pageSize = jsonRet.pageCond.pageSize;
						containerVue.param.begin = jsonRet.pageCond.begin;
						containerVue.param.count = jsonRet.pageCond.count;
					}else{
						//alert(jsonRet.errmsg);
						//$("#nomoreData").show();
					}
					$("#loadingData").hide();
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});			 
	 },
	 addCollection: function(collType,targetId){
		$.ajax({
			url: '/collection/add/'+collType + '/' + targetId,
			method:'post',
			data: {},
			success: function(jsonRet,status,xhr){
				if(jsonRet && jsonRet.errmsg){
					if(jsonRet.errcode !==0){
						alertMsg('错误提示',jsonRet.errmsg);
					}else{
						alertMsg('系统提示','收藏成功！');
						setTimeout("hideAlertMsg()",1000);
					}
				}else{
					alertMsg('错误提示','系统失败！');
				}
			},
			dataType: 'json'
		});
	}
 }
});
 containerVue.getAll(true,false);
 //分类查询
 function getGoodsByCat(categoryId){
	 containerVue.goodsList = [];
	 containerVue.param.categoryId = categoryId;
	 containerVue.param.begin = 0;
	 containerVue.getAll(true,false);
 }
 function getGoodsByKey(keywords){
	 if(keywords){
		 containerVue.goodsList = [];
		 containerVue.param.keywords = keywords;
		 containerVue.param.begin = 0;
		 containerVue.getAll(true,false);
	 }
 } 
 
 var winHeight = $(window).height(); //页面可视区域高度   
 var scrollHandler = function () {  
	 if($(window).scrollTop()>10){
		  $('#goTopId').show();
    }else{
   	   $('#goTopId').hide();
    }
     var pageHieght = $(document.body).height();  
     var scrollHeight = $(window).scrollTop(); //滚动条top   
     var r = (pageHieght - winHeight - scrollHeight) / winHeight;
     if (r>=0 && r < 0.2 && containerVue.param.count > containerVue.goodsList.length) {//上拉翻页 
    	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
    	 	containerVue.getAll(false,false);
     }
     if(scrollHeight <= 0){ //下拉翻页
    	 	if(containerVue.param.begin <= 0){
    	 		return;
    	 	}
    	 	var currPageCnt = containerVue.goodsList.length%containerVue.param.pageSize;//当前页的数量
 		if(currPageCnt == 0){
 			currPageCnt = containerVue.param.pageSize;
 		}
	 	containerVue.param.begin = containerVue.param.begin - (containerVue.goodsList.length-currPageCnt);//总数据的开始
    	 	containerVue.param.begin = containerVue.param.begin - containerVue.param.pageSize;
     	if(containerVue.param.begin <= 0){
     		containerVue.param.begin = 0;
     		containerVue.getAll(true,true);
     	}else{
    	 		containerVue.getAll(false,true);
     	}
     }
 }  
 //定义鼠标滚动事件  
$(window).scroll(scrollHandler);

</script> 


<footer >
  <div class="row" style="position:absolute;bottom:60px;left:0;right:0;height:80px;text-align:center;background-color:#D0D0D0">
	<p>&nbsp;</p>
	<span style="display:inline-block; margin:0 10px;"></span>
	Copyright <font style="font-family:'微软雅黑';">©</font> 2017-2020 摩放优选 <a href="http://www.miitbeian.gov.cn/" target="_blank" rel="nofollow">滇ICP备18002601号-1</a> 
  </div>
  <div style="min-height:60px"></div>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

