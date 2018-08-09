<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    <#include "/head/page-fileinput-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="padding:0;overflow:scroll">
  <#include "/common/tpl-order-partner-4fm.ftl" encoding="utf8"> 
  <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8"> 

  <!-- 支付明细 -->
  <#if (payFlow.flowId)??>
  <#include "/order/tpl-order-payflow-4fm.ftl" encoding="utf8"> 
  </#if>
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;如果您已付款，取消后您的付款订单金额(不含手续费)将返回至您的付款账户！手续费将不会退回！！！</span>
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
				url: '/order/cancel/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/order/show/all";
						}else{//出现逻辑错误
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
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

