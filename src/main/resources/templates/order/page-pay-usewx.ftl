<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    <#include "/head/page-wechat-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if (payFlow.flowId)?? >
<div class="container " id="container" style="padding:0;overflow:scroll">
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
     <div class="thumbnail">
        <img src="/order/pay/show/wxqrcode/${orderIds!''}"  alt="支付二维码" style="max-legnth:300px;max-height:300px">
        <div class="caption">
           <h3>微信支付二维码</h3>
        </div>
     </div>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goodsSpecArr:JSON.parse('${(order.goodsSpec)!"[]"}'),
		param:{
			orderIds:'${orderIds!""}',
			passwd:''
		}
	},
	methods:{
		getPayStatus: function(){
			$.ajax({
				url: '/order/pay/status/' + this.param.orderIds ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//创建支付成功
							window.location.href = "/order/pay/finish/" + containerVue.param.orderIds;
						}
					}else{
						if(jsonRet && jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}
					setTimeout("containerVue.getPayStatus()",15000);
				},
				dataType: 'json'
			});
		}
	}
});
setTimeout("containerVue.getPayStatus()",15000);
</script>
</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

