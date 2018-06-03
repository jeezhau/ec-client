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
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="margin:0 0;padding:0;overflow:scroll;">
  <#include "/porder/tpl-porder-buy-user-4fm.ftl" encoding="utf8"> 
  <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8"> 

  <!-- 支付明细 -->
  <#if (payFlow.flowId)??>
  <#include "/porder/tpl-porder-payflow-4fm.ftl" encoding="utf8"> 
  </#if>
  <!-- 联系买家 -->
  <div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
      <span>买家联系电话：</span> <span>${(order.userPhone)!''}</span>
  </div>
  <#if (order.aftersalesReason)??>
  <!-- 买家售后申请信息 -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家售后申请</span>
    </div>
    <div v-for="reason in order.aftersalesReason" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-right">{{reason.type}}</span>
       <span class="pull-left">{{reason.time}}</span>
     </div>
     <div class="row">
       <p>{{reason.content.reason}}</p>
       <p v-if="reason.type.indexOf('退货')">
       {{getDispatchMode(reason.content.dispatchMode)}} {{reason.content.logisticsComp}} {{reason.content.logisticsNo}}
       </p>
     </div>
    </div>
  </div>
  </#if>
  <#if (order.aftersalesResult)??>
  <!-- 卖家售后回复信息 -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">卖家售后处理</span>
    </div>
    <div v-for="reason in order.aftersalesResult" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-right">{{reason.type}}</span>
       <span class="pull-left">{{reason.time}}</span>
     </div>
     <div class="row">
       <p>{{reason.content.reason}}</p>
       <p v-if="reason.content.dispatchMode">
       {{getDispatchMode(reason.content.dispatchMode)}} {{reason.content.dispatchMode.logisticsComp}} {{reason.content.dispatchMode.logisticsNo}}
       </p>
     </div>
    </div>
  </div> 
  </#if> 
  <!-- 退款申请信息 -->
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;填写说明：请根据买家提交的退款请求尽快完成处理！如有问题，可与买家友好协商解决！！！</span>
  </div>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">处理结果<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <select class="form-control" v-model="param.result" required >
           <option value="" disabled> 请选择... </option>
           <#if order.status=='60'>
           <option value="61"> 同意退货 </option>
           <option value="65"> 同意退款，资金回退中 </option>
           <option value="68"> 不同意退款 </option>
           </#if>
           <#if order.status=='62'>
           <option value="63"> 已收到退货、核验中 </option>
           <option value="64"> 核验不通过、协商解决 </option>
           <option value="65"> 同意退款，资金回退中 </option>
           </#if>
           <#if order.status=='63'>
           <option value="64"> 核验不通过、协商解决 </option>
           <option value="65"> 重新发货 </option>
           </#if> 
         </select>
       </div>
  </div>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">处理明细<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <textarea class="form-control" v-model="param.reason" required placeholder="处理明细最少3个字符">
           
         </textarea>
       </div>
  </div>  
   <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交处理结果</button>
   </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		order:{
			status:'${order.status}',
			goodsSpec:JSON.parse('${(order.goodsSpec)!"[]"}'),
			aftersalesReason: JSON.parse('${(order.aftersalesReason)!"[]"}'),
			aftersalesResult: JSON.parse('${(order.aftersalesResult)!"[]"}'),
		},
		
		param:{
			orderId:'${order.orderId}',
			result:'',
			reason:''
		}
	},
	methods:{
		submit:function(){
			if(!this.param.result ){
				alertMsg('错误提示','处理结果不可为空！');
				return;
			}
			if(!this.param.reason || this.param.reason.length<3){
				alertMsg('错误提示','处理明细不可少于3个字符！');
				return;
			}
			
			$.ajax({
				url: '/paftersale/refund/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/paftersale/manage/refunding";
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！');
					}
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

<#include "/menu/page-partner-func-menu.ftl" encoding="utf8">

</body>
</html>

