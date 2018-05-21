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
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">


<div class="container " id="container" style="margin:0 0;padding:0;overflow:scroll">
 <#if payRetCode?? >
 <!-- 支付结果信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;height:80px;background-color:#6699FF;" >
    <div class="col-xs-12" style="text-align:center;vertical-align:center;font-size:20px;font-weight:bold;padding-top:20px">
     <#if (payRetCode >= 0) > <img alt="" src="/icons/支付成功.png" width=30px height=30px > </#if>
     <span style="padding:5px">${payRetMsg}</span>
    </div>
  </div>
  <!-- 支付明细 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
       <label class="col-xs-3" style="padding:0">订单号：</label>
       <span class="col-xs-8" style="padding:0">${order.orderId!''}</span>
    </div>
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3" style="padding:0">支付方式：</label>
        <span class="col-xs-8"  style="padding:0">{{getPayType('${payType!''}')}}</span>    
    </div>
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">支付时间：</label>
        <span class="col-xs-8"  style="padding:0">${payTime!''}</span>
    </div>   
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3" style="padding:0">订单额¥：</label>
        <span class="col-xs-8"  style="padding:0">${amount!''}</span>
    </div>
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">手续费¥：</label>
        <span class="col-xs-8"  style="padding:0">${fee!''}</span>
    </div>    
  </div> 
  <!-- 商家 -->
  <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/partner/mcht/${order.partnerId}">
        <img alt="头像" src="/partner/cert/show/logo/${order.partnerId}" width="20px" height="20px" style="border-radius:50%"> 
        ${order.partnerBusiName}
      </a>
  </div>
  <!-- 商品信息 -->
  <div class="row" style="margin:3px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:center;">${order.goodsName}</div>
    <div class="col-xs-12" style="text-align:center;">
      <a href="/goods/show/${(order.goodsId)?string('#')}">
       <img alt="" src="/image/file/show/${(order.mchtUId)?string('#')}/${(order.goodsMainImgPath)!''}" style="width:99%;height:150px;">
      </a>
    </div>
    <div class="col-xs-12" style="padding:0px 3px">
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="30%" style="padding:2px 2px">规格名称</th>
           <th width="15%" style="padding:2px 2px">量值</th>
           <th width="15%" style="padding:2px 2px">售价(¥)</th>
           <th width="20%" style="padding:2px 2px">购买数量</th>
         </tr>
         <tr v-for="item,index in goodsSpecArr" >
           <td style="padding:2px 2px">
             <span style="width:100%" >{{item.name}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.val}} {{item.unit}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.price}}</span>
           </td> 	                         
           <td style="padding:2px 2px;text-align:center">
              <span style="width:100%" >{{item.buyNum}}</span>
           </td>
         </tr>
       </table>    
     </div>
  </div> 
</#if>    
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr:JSON.parse('${(order.goodsSpec)!"[]"}')
	},
	methods:{
		getPayType:function(tp){
			if('1' == tp){
				return '余额支付';
			}else if('2' == tp){
				return '微信支付';
			}
		}
	}
});
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

