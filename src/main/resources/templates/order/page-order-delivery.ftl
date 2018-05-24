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

<#if (order.orderId)?? >
<div class="container " id="container" style="margin:0 0;padding:0;overflow:scroll">
  <#include "/order/tpl-order-buy-user-4fm.ftl" encoding="utf8"> 
  <#include "/order/tpl-order-buy-content-4fm.ftl" encoding="utf8"> 

  <#include "/order/tpl-order-buy-receiver-4fm.ftl" encoding="utf-8"> 
  <!-- 支付明细 -->
  <#if (payFlow.flowId)??>
  <#include "/order/tpl-order-pay-flow-4fm.ftl" encoding="utf8"> 
  </#if>
  <!-- 商家 -->
  <div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/partner/mcht/${order.partnerId}">
        <img alt="头像" src="/partner/cert/show/logo/${order.partnerId}" width="20px" height="20px" style="border-radius:50%"> 
        ${order.partnerBusiName}
      </a>
  </div>
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;填写说明：官方配送则名称为“摩放优选”，单号为订单号；商家配送则名称为“商家名称”，单号为订单号；
  	快递配送则名称为“快递公司名称”，单号为物流公司的单号；客户自取则名称为“客户昵称”，单号为订单号；</span>
  </div>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">配送方名称<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <input type="text" class="form-control" v-model="param.logisticsComp" required maxlength="100">
       </div>
  </div> 
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">配送单号<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <input type="text" class="form-control" v-model="param.logisticsNo" required maxlength="100">
       </div>
   </div>
  <#if ((vipBasic.status)!'') == '1'>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">会员密码<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <input type="password" class="form-control" v-model="param.passwd" required maxlength="20">
       </div>
   </div>  
   </#if>
   <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交发货</button>
   </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		order:{
			status:'${order.status}',
			goodsSpec:JSON.parse('${(order.goodsSpec)!"[]"}'),
		},
		
		param:{
			orderId:'${order.orderId}',
			passwd:'',
			logisticsComp:'',
			logisticsNo:''
		}
	},
	methods:{
		submit:function(){
			$("#dealingData").hide();
			if(!this.param.logisticsComp || this.param.logisticsComp.length<2){
				alertMsg('错误提示','配送方名称不可小于2个字符！');
				return;
			}
			if(!this.param.logisticsNo || this.param.logisticsNo.length<3){
				alertMsg('错误提示','配送单号不可小于2个字符！');
				return;
			}
			$.ajax({
				url: '/order/partner/delivery/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/order/partner/show/all";
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！');
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		}
	}
});
if('3' != '${order.dispatchMode}'){
	containerVue.param.logisticsNo = '${order.orderId}';
	if('1' == '${order.dispatchMode}'){
		containerVue.param.logisticsComp = '摩放优选';
	}else if('2' == '${order.dispatchMode}'){
		containerVue.param.logisticsComp = '${(order.partnerBusiName)!""}';
	}else if('4' == '${order.dispatchMode}'){
		containerVue.param.logisticsComp = '${(order.nickname)!''}';
	}
}else{
	containerVue.param.logisticsNo = '';
	containerVue.param.logisticsComp = '';
}
</script>
</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

