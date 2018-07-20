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
<body class="light-gray-bg" style="overflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container " id="container" style="padding:0;">
 <#if (order.orderId)?? >
  <div class="row" style="margin:5px 1px ;padding:3px 5px;background-color:white" >
    <span>订单ID：${order.orderId}</span><br>
    <span>创建时间：${(order.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
  </div>
  
  <#include "/common/tpl-order-buy-receiver-4fm.ftl" encoding="utf-8">

  <#include "/common/tpl-order-buy-content-bigimg-4fm.ftl" encoding="utf-8">  
  
  <#if (payFlow.flowId)??>
  <!-- 支付明细 -->
  <#include "/porder/tpl-porder-payflow-4fm.ftl" encoding="utf8"> 
  </#if>
  
  <!-- 售后信息 -->
  <#if (aftersale.applyTime)??>
   <#include "/common/tpl-aftersale-apply-4fm.ftl" encoding="utf8"> 
  </#if>
  <#if (aftersale.dealResult)??>
   <#include "/common/tpl-aftersale-deal-4fm.ftl" encoding="utf8"> 
  </#if>
  
  <#if (apprMcht)?? >
  <!-- 买家评价信息 -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家评价</span>
    </div>
    <div v-for="appr in apprMcht" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left">{{appr.time}}</span>
     </div>
     <div class="row">
       {{appr.content}}
     </div>
    </div>
  </div> 
  </#if>
  <#if (apprUser)??>
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">卖家评价</span>
    </div>
    <div v-for="appr in apprUser" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left">{{appr.time}}</span>
     </div>
     <div class="row">
       {{appr.content}}
     </div>
    </div>
  </div>
  </#if> 
  
  <!-- 查看物流 -->
  <#if (order.sendTime)??>
  <div class="row" style="margin:5px 1px ;padding:3px 3px;background-color:white" >
    <a href="/order/logistics/${order.orderId}">查看物流</a>
  </div>
  </#if>
  <!-- 推荐商品 -->
  <#include "/shop/tpl-goods-recommend.ftl" encoding="utf-8">

</#if>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr: JSON.parse('${(order.goodsSpec)!"[]"}'),
		aftersaleReason: JSON.parse('${(aftersale.applyReason)!"[]"}'),
		aftersaleResult: JSON.parse('${(aftersale.dealResult)!"[]"}'),
		apprMcht: JSON.parse('${(apprMcht.content)!"[]"}'),
		apprUser: JSON.parse('${(apprUser.content)!"[]"}'),
	},
	methods:{
		
	}
});

</script>

<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

