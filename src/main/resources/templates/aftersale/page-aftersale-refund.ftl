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
<body class="light-gray-bg" style="overflow:scroll">
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
  <!-- 商家 -->
  <div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/shop/mcht/${(order.partnerId)?string('#')}">
        <img alt="头像" src="/shop/pcert/logo/${(order.partnerId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
        ${order.partnerBusiName}
      </a>
  </div>
  
  <!-- 售后信息 -->
  <#if (aftersale.applyTime)??>
   <#include "/common/tpl-aftersale-apply-4fm.ftl" encoding="utf8"> 
  </#if>
  <#if (aftersale.dealResult)??>
   <#include "/common/tpl-aftersale-deal-4fm.ftl" encoding="utf8"> 
  </#if>
   
  <!-- 退款申请信息 -->
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;<br>
  	填写说明：退货则需要填写退货的物流信息，官方配送则名称为“摩放优选”，单号为订单号；商家自取则名称为“商家名称”，单号为订单号；
  	快递配送则名称为“快递公司名称”，单号为物流公司的单号；买家送达则名称为“买家昵称”，单号为订单号；</span>
  </div>
  <#if !(order.status)?starts_with('6')>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">退款类型<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <select class="form-control" v-model="param.type" required >
           <option value="" disabled> 请选择... </option>
           <option value="1"> 未收到货 </option>
           <option value="3"> 签收退货 </option>
         </select>
       </div>
  </div>
  </#if>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">退款原因<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <textarea class="form-control" v-model="param.reason" required placeholder="退款原因最少3个字符">
           
         </textarea>
       </div>
  </div>
  <#if order.status == '61'>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">配送类型<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <select class="form-control" v-model="param.dispatchMode" required @change="changeDispatch">
           <option value="" disabled> 请选择...</option>
           <option value="2"> 商家自取 </option>
           <option value="3"> 快递配送 </option>
           <option value="4"> 买家送达 </option>
         </select>
       </div>
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
   </#if>
  <#if ((vipBasic.status)!'') == '1'>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">会员密码<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <input type="password" class="form-control" v-model="param.passwd" required maxlength="20">
       </div>
   </div>  
   </#if>   
   <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交退款申请</button>
   </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		aftersaleReason: JSON.parse('${(aftersale.applyReason)!"[]"}'),
		aftersaleResult: JSON.parse('${(aftersale.dealResult)!"[]"}'),
		order:{
			status:'${order.status}',
			goodsSpec:JSON.parse('${(order.goodsSpec)!"[]"}'),
		},
		
		param:{
			orderId:'${order.orderId}',
			passwd:'',
			<#if order.status != '61'>
			type:'',
			</#if>
			reason:'',
			<#if order.status == '61'>
			dispatchMode:'',
			logisticsComp:'',
			logisticsNo:''
			</#if>
		}
	},
	methods:{
		<#if order.status == '61'>
		changeDispatch: function(){
			if('3' != this.param.dispatchMode){
				this.param.logisticsNo = '${order.orderId}';
				if('1' == this.param.dispatchMode){
					this.param.logisticsComp = '摩放优选';
				}else if('2' == this.param.dispatchMode){
					this.param.logisticsComp = '${(order.partnerBusiName)!""}';
				}else if('4' == this.param.dispatchMode){
					this.param.logisticsComp = '${(order.nickname)!''}';
				}
			}else{
				containerVue.param.logisticsNo = '';
				containerVue.param.logisticsComp = '';
			}
		},
		</#if>
		submit:function(){
			<#if order.status != '61'>
			if(!this.param.type ){
				alertMsg('错误提示','退款类型不可为空！');
				return;
			}
			if(!this.param.reason || this.param.reason.length<3){
				alertMsg('错误提示','退款原因不可少于3个字符！');
				return;
			}
			</#if>
			<#if order.status == '61'>
			if(!this.param.dispatchMode ){
				alertMsg('错误提示','配送方式不可为空！');
				return;
			}
			if(!this.param.logisticsComp || this.param.logisticsComp.length<2){
				alertMsg('错误提示','配送方名称不可小于2个字符！');
				return;
			}
			if(!this.param.logisticsNo || this.param.logisticsNo.length<3){
				alertMsg('错误提示','配送单号不可小于2个字符！');
				return;
			}
			</#if>
			$.ajax({
				url: '/aftersale/refund/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/aftersale/manage/refunding";
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

