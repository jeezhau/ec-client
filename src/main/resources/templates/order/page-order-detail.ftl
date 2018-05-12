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
    <script src="/script/common.js"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<div class="container " id="container" style="margin:0 0;padding:0;overflow:scroll">
 <#if (order.orderId)?? >
  <div class="row" style="margin:5px 1px ;padding:3px 5px;background-color:white" >
    <span>订单ID：${(order.orderId)?string('#')}</span>
    <span> 创建时间：${(order.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
  </div>
  <!-- 收货人信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12">
      <span>
        <#if (order.headimgurl)?starts_with('http')>
        <img alt="头像" src="${(order.headimgurl)!''}" width="20px" height="20px" style="border-radius:50%"> 
        </#if>
        <#if !(order.headimgurl)?starts_with('http')>
        <img alt="头像" src="/user/headimg/show/${(order.userId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
	    </#if>
	    <span>${order.nickname}</span>
     </span><br>
     <span>${order.recvName} , ${(order.recvPhone)!''}</span>
    </div>
    <div class="col-xs-12">
        <span>${order.recvProvince}</span>
        <span>${order.recvCity}</span>
        <span>${order.recvArea}</span>
        <span>${order.recvAddr}</span>
     </div>
     <div class="col-xs-12">
       {{getDispatchMode(${order.dispatchMode})}}
     </div>
  </div>

  <!-- 商品信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:center;">
      <a class="pull-left" href="/partner/mcht/${(order.partnerId)?string('#')}">
	    <img alt="头像" src="/partner/cert/show/logo/${(order.partnerId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
	    <span>${(order.partnerBusiName)!''}</span>
	  </a><br>
      <span>${order.goodsName}</span>
    </div>
    <div class="col-xs-12" style="text-align:center;">
      <a href="/goods/show/${(order.goodsId)?string('#')}">
       <img alt="" src="/image/file/show/${(order.goodsMainImgPath)!''}" style="width:99%;height:150px;">
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
     <div class="row" style="margin:1px 1px;padding:3px 5px;background-color:white;">
		<span class="pull-left"> 金额¥：${order.amount}</span> 
		<span class="pull-right">{{getOrderStatus(${order.status})}}</span>
	 </div>
  </div>  
  
  <!-- 物流信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 3px;background-color:white" >
    
  </div>
  
  <!-- 推荐商品 -->
  <div class="row" style="margin:5px 1px ;padding:3px 3px;background-color:white" >
    
  </div>
  

</#if>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr:JSON.parse('${(order.goodsSpec)!"[]"}'),
	},
	methods:{
		getDispatchMode:function(code){
			if(code){
				if('1' === code){
					return '官方统一配送';
				}else if('2' == code){
					return '商家自行配送';
				}else if('3' == code){
					return '快递配送';
				}else if('4' == code){
					return '客户自取';
				}
			}
		}
	}
});

</script>

<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">


</body>
</html>

