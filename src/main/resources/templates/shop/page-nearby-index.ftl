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
    
    <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
</head>
<body class="light-gray-bg" >
<header >
  <#include "/menu/page-category-menu.ftl" encoding="utf8"> 
</header>
<div class="container goods-container" id="container" style="padding:0 1px;overflow:scroll">
  <div class="row" style="margin:0 0;overflow:scroll">
    <div v-for="goods in goodsList" class="col-xs-6" style="padding:0px 0px;">
        <div class="row" style="margin:1px 1px;padding:0 10px;background-color:white;text-align:center;vertical-align:center" >
          <a v-bind:href="'/partner/detail/' + goods.partnerId">
	        <img class="pull-left" alt="" src="/images/mfyx_logo.jpeg" style="width:25px;height:25px;border-radius:30%">
	      </a>
	      <span class="pull-right">昆明市-官渡区</span>
        </div>
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/goods/detail/' + goods.goodsId">
	        <img title="190X150最优" alt="" src="/images/mfyx_logo.jpeg" style="width:90%;height:150px">
	      </a>
	    </div>
	    <div style="margin:1px 1px;" >
	      <div style="margin:1px 0;padding:0 5px;background-color:white" >
		        是非得失分身乏术地方简单描述简单描述史蒂夫舒
	      </div>
	      <div style="margin:1px 0px;padding:0 5px;background-color:white;color:red" >
	      	<span class="pull-left ">惠¥: <span>0.00</span>元</span>
	      	<span class="pull-right ">库存: <span>100</span>件</span>
	      </div>
	      <div style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 12px" href="/order/order/begin/goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 12px"href="/order/order/begin/goodsId"><span style="color:white">加入收藏</span></a>
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
			mode:'cat', //cat、keywords
		},
		goodsList:[{goodsId:123,partnerId:999},{goodsId:123,partnerId:999},{goodsId:123,partnerId:999},{goodsId:123,partnerId:999},
			{goodsId:123,partnerId:999},{goodsId:123,partnerId:999},{goodsId:123,partnerId:999},{goodsId:123,partnerId:999}] 
	 },
	 methods:{
		 
	 }
 });
 //设置查询方式
 function setSearchMode(mode,param){
	 if(mode == "cat"){//分类
		 
	 }
 }
  
 var winHeight = $(window).height(); //页面可视区域高度   
 var scrollHandler = function () {  
     var pageHieght = $(document.body).height();  
     var scrollHeight = $(window).scrollTop(); //滚动条top   
     var r = (pageHieght - winHeight - scrollHeight) / winHeight;
     if (r < 0.5) {//0.5是个参数  
         if (0) {//每10页做一次停顿！  
             //getData(i);
             //$(window).unbind('scroll');  
             $("#btn_Page").show();  
         } else {  
             getData(i);  
             $("#btn_Page").hide();  
         }  
     }  
 }  
 //定义鼠标滚动事件  
 $(window).scroll(scrollHandler);  
 

</script>  
<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>