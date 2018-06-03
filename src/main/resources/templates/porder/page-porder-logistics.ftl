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
    <script src="/script/common.js"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container " id="container" style="margin:0 0;padding:0;overflow:scroll">
 <#if (order.orderId)?? >
  <div class="row" style="margin:5px 1px ;padding:3px 5px;background-color:white" >
    <span>订单ID：${order.orderId}</span><br>
    <span>创建时间：${(order.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
  </div>
  
  <!-- 物流信息 -->
  <#if (order.sendTime)??>
  <div class="row" style="margin:5px 1px 1px 1px;" >
    <div class="row" style="margin:1px 0;background-color:white;">
	   <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">配送物流信息</span>
	</div>
    <div class="row" style="padding:3px 3px;background-color:white"><!-- 配送方信息 -->
     <div class="col-xs-12">
       <span>配送方式：{{getDispatchMode('${order.dispatchMode}')}}</span>
     </div>
     <div class="col-xs-12">
       <span>配送方名称：${order.logisticsComp}</span>
     </div>
     <div class="col-xs-12">
       <span>配送单号：${(order.logisticsNo)!''}</span>
     </div>
    </div>
  </div>
  <div class="row" style="margin:2px 0px 5px 0px;background-color:white;">
     <#if (order.signTime)??>
  	 <div class="col-xs-12" style="padding:0 3px">
	    <span class="pull-left" style="padding:0 10px;">${(order.signTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
	    <span class="pull-right" style="padding:0 10px;">签收完成</span>
	 </div>
	 </#if>
	 <div v-for=" item in logistics" class="col-xs-12">
	   <span class="pull-left" style="padding:0 10px;">{{item.time}}</span>
	   <span class="pull-right" style="padding:0 10px;">{{item.content}}</span>
	 </div>
	 <div class="col-xs-12" style="padding:0 3px">
	    <span class="pull-left" style="padding:0 10px;">${(order.sendTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
	    <span class="pull-right" style="padding:0 10px;">发货</span>
	 </div>
  </div>
  </#if>
  
  <#include "/common/tpl-order-buy-receiver-4fm.ftl" encoding="utf-8">

  <#include "/common/tpl-order-buy-content-bigimg-4fm.ftl" encoding="utf-8">
  

</#if>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr: JSON.parse('${(order.goodsSpec)!"[]"}'),
		logistics: JSON.parse('${(order.logistics)!"[]"}'),
	},
	methods:{
		
	}
});

</script>

<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

