<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">

<div class="container " id="container" style="padding:0;overflow:scroll">
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
  <div class="row" style="margin:2px 0px 5px 0px;">
     <#if (order.signTime)??>
  	 <div class="col-xs-12" style="padding:0 3px;background-color:white;">
	    <span class="pull-left" style="padding:0 10px;">${(order.signTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
	    <span class="pull-right" style="padding:0 10px;">签收完成(${(order.signUser)!''})</span>
	 </div>
	 </#if>
	 <div v-if="logistics.status && logistics.status != '0'" class="col-xs-12" style="margin-top:3px;background-color:white;">
	   <span style="padding:0 10px;">{{logistics.msg}}</span>
	 </div>
	 <div v-if="logistics.status === '0' && logistics.result && logistics.result.list">
	 <div v-for=" item in logistics.result.list" class="col-xs-12" style="margin-top:1px;background-color:white;">
	   <div class="col-xs-1" style="margin-top:-2px;padding:0;vertical-align:center;">
	    <img alt="" src="/icons/树形线.png" style="width:80%;height:110%">
	   </div>
	   <div class="col-xs-11" style="padding:0">
	     <span class="pull-left" style="padding:0 3px;">{{item.time}}</span>
	     <span class="pull-right" style="padding:0 3px;">{{item.status}}</span>
	   </div>
	 </div>
	 </div>
	 <div class="col-xs-12" style="margin-top:1px;padding:0 3px;background-color:white;">
	    <span class="pull-left" style="padding:0 10px;">${(order.sendTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
	    <span class="pull-right" style="padding:0 10px;">发货</span>
	 </div>
  </div>
</#if>
  
  <#include "/common/tpl-order-buy-receiver-4fm.ftl" encoding="utf-8">

  <#include "/common/tpl-order-buy-content-bigimg-4fm.ftl" encoding="utf-8">
  
  <!-- 推荐商品 -->
  <#include "/shop/tpl-goods-recommend.ftl" encoding="utf-8">
  

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

