<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
	<title>摩放优选</title>
	<!-- Bootstrap -->
	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!--Vue -->
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <!-- -->
    <link href="/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/templatemo-style.css" rel="stylesheet">
    
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
</head>
<body class="light-gray-bg" >

<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<div class="container goods-container" id="container" style="padding:0 1px;overflow:scroll">
  <header >
    <#include "/menu/page-category-menu.ftl" encoding="utf8"> 
  </header>
  <div class="row" style="margin:0 0;overflow:scroll">
    <div v-for="goods in goodsList" class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:3px 2px;">
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/goods/show/' + goods.goodsId">
	        <img alt="" :src="'/image/file/show/' + goods.partner.vipId + '/' + goods.mainImgPath" style="width:90%;max-width:200px;height:150px">
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
	      <div class="row" style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 5px" :href="'/order/place/'+ goods.goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 5px" href="javascript:;" @click="addCollection('2',goods.goodsId)"><span style="color:white">加入收藏</span></a>
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
		begin:0
	},
	goodsList:[] ,
	goodsCnt:0,
 },
 methods:{
	 getAll: function(isRefresh,isFirst){
		 $("#loadingData").show();
		 $("#nomoreData").hide();
		 if(containerVue.goodsList.length>100){
		 	containerVue.goodsList = [];
		 }
		 if(isRefresh){ //清空数据
			 containerVue.goodsCnt = 0;
			 containerVue.goodsList = [];
		 }
		 if(containerVue.goodsList.lenght>=300){
			 if(isFirst){//清除后一百条
				 containerVue.goodsList.splice(200,100);
			 }else{
				 containerVue.goodsList.splice(0,100); 
			 }
		 }
		 $.ajax({
				url: '/shop/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errcode == 0){//
						var i=0;
						var j = jsonRet.datas.length;
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
					}else{
						//alert(jsonRet.errmsg);
						$("#nomoreData").show();
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
     var pageHieght = $(document.body).height();  
     var scrollHeight = $(window).scrollTop(); //滚动条top   
     var r = (pageHieght - winHeight - scrollHeight) / winHeight;
     if (r < 0.5) {//上拉翻页 
    	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
    	 	containerVue.getAll(false,false);
     }
     if(scrollHeight<0){ //下拉翻页
    	 	var cnt = containerVue.goodsList.length%containerVue.param.pageSize;
 		cnt = containerVue.goodsList - cnt;
	 	containerVue.param.begin = containerVue.param.begin - cnt;
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
 $("#container").scroll(scrollHandler); 
 

</script> 
 
<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

