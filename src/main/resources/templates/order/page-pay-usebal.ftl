<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if (payFlow.flowId)?? >
<div class="container " id="container" style="padding:0;overflow:scroll">
  <!-- 商品信息 -->
    <!-- 商品订单信息 -->
  <div class="row" style="margin:0px 1px ;padding:5px 0;" >
    <#list list as order>
     <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8">
    </#list>
  </div> 
  <!-- 支付明细 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
       <label class="col-xs-3" style="padding:0">支付方式：</label>
       <span class="col-xs-8"  style="padding:0">{{getPayType('${(payFlow.payType)!''}')}}</span>
    </div>
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">创建时间：</label>
        <span class="col-xs-8"  style="padding:0">${(payFlow.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
    </div>   
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3" style="padding:0">订单额¥：</label>
        <span class="col-xs-8"  style="padding:0">${(payFlow.payAmount/100)}</span>     
    </div>
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
        <label class="col-xs-3"  style="padding:0">手续费¥：</label>
        <span class="col-xs-8"  style="padding:0">${payFlow.feeAmount/100}</span>
    </div>   
  </div>  
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="row">
      <label class="col-xs-3 control-label" style="padding-right:0">支付密码<span style="color:red">*</span></label>
      <div class="col-xs-8">
        <input type="password" class="form-control" v-model="param.passwd" required>
      </div>
    </div>
    <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交支付</button>
    </div>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			orderIds:'${orderIds}',
			passwd:''
		}
	},
	methods:{
		submit:function(){
			$("#dealingData").show();
			if(!this.param.passwd || this.param.passwd.length<6){
				alertMsg('错误提示','支付密码不可为空！');
				return;
			}
			$.ajax({
				url: '/order/pay/submit/bal/' + this.param.orderIds ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//创建支付成功
							window.location.href = "/order/pay/finish/" + containerVue.param.orderIds;
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

