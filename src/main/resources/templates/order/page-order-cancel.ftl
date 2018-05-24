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

  <!-- 支付明细 -->
  <#if (payFlow.flowId)??>
  <#include "/order/tpl-order-pay-flow-4fm.ftl" encoding="utf8"> 
  </#if>
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;如果您已付款，取消后您的付款金额将返回至您的付款账户！</span>
  </div>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">取消缘由<span style="color:red">*</span></label>
       <div class="col-xs-9">
         <textarea maxLength=600 class="form-control" v-model="param.reason" required></textarea>
       </div>
  </div> 
  
  <#if (vipBasic.status) == '1'>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">会员密码<span style="color:red">*</span></label>
       <div class="col-xs-9">
         <input type="password" class="form-control" v-model="param.passwd" required>
       </div>
   </div>
   </#if>
   <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交取消</button>
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
			reason:''
		}
	},
	methods:{
		submit:function(){
			$("#dealingData").show();
			if(!this.param.reason || this.param.reason.length<3){
				alertMsg('错误提示','取消理由不可小于3个字符！');
				return;
			}
			<#if (vipBasic.status) == '1'>
			if(!this.param.passwd || this.param.passwd.length<6){
				alertMsg('错误提示','支付密码不可小于6个字符！');
				return;
			}
			</#if>
			$.ajax({
				url: '/order/user/cancel/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/order/user/show/all";
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
</script>
</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

