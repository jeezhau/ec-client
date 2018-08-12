<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">

<div class="container " id="container" style="padding:0;overflow:scroll">
 <#if payRetCode?? >
 <!-- 支付结果信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;min-height:80px;background-color:#6699FF;" >
    <div class="col-xs-12" style="text-align:center;vertical-align:center;font-size:20px;font-weight:bold;padding-top:20px">
     <#if (payRetCode >= 0) > <img alt="" src="/icons/支付成功.png" width=30px height=30px > </#if>
     <span style="padding:5px">${payRetMsg}</span>
    </div>
  </div>
  <!-- 支付明细 -->
  <#if (payFlow.flowId)??>
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3" style="padding:0">支付方式：</label>
        <span class="col-xs-8"  style="padding:0">{{getPayType('${(payFlow.payType)!''}')}}</span>    
    </div>
    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">支付时间：</label>
        <span class="col-xs-8"  style="padding:0">${(payFlow.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
    </div>   
    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3" style="padding:0">订单额¥：</label>
        <span class="col-xs-8"  style="padding:0">${(payFlow.payAmount/100)}</span>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">手续费¥：</label>
        <span class="col-xs-8"  style="padding:0">${payFlow.feeAmount/100}</span>
    </div>    
  </div> 
  </#if>
  <!-- 商品信息 -->
  <#if list??>
  <div class="row" style="margin:0px 1px ;padding:5px 0;" >
    <#list list as order>
     <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8">
    </#list>
  </div> 
  </#if>
</#if>    
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr:JSON.parse('${(order.goodsSpec)!"[]"}')
	}
});
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

