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
    
    <script src="/script/common.js" type="text/javascript"></script>
    
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.6&key=2b12c05334ea645bd934b55c8e46f6ea"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
    <script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main.css"/>
</head>
<body class="light-gray-bg" style="overflow:scroll;">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="margin-bottom:80px;padding:0;overflow:scroll;">
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
  <#include "/common/tpl-order-buy-receiver-4fm.ftl" encoding="utf-8">
  
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
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;同意退款后买家的付款金额将返回至买家的付款账户！买家支付的手续费与国税将由您承担！！！<br>
  	填写说明：请根据买家提交的退款请求尽快完成处理！如有问题，可与买家友好协商解决！！！</span>
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
  <#if order.status == '60'>
   <div v-if="param.result=='61'" class="row" style="margin:3px 0;padding:5px 3px;" id="receiverAddr">
        <label class="col-xs-12 control-label" style="vertical-algin:center;">退货地址<span style="color:red">*</span>:</label>
        <div class="col-xs-12">
          <div class="row">
            <div class="col-xs-9" style="padding:0;font-weight:bolder">
              <div class="row" style="margin:0">
	              <div class="col-xs-6" style="padding:0">
	                <input type="hidden" name="recvId" v-model="param.recvId">
	                <input type="text" class="form-control" name="recvName" v-model="param.recvName" disabled style="width:100%;" placeholder="收货人姓名" required>
	              </div>
	              <div class="col-xs-6" style="padding:0">
	                <input type="text" class="form-control" name="recvPhone" v-model="param.recvPhone" disabled style="width:100%" placeholder="联系电话" required>
	              </div>
              </div>
              <div class="row" style="margin:0;font-weight:bolder">
              	<div class="col-xs-3" style="padding:0 ">
              	 <input type="hidden" name="recvCountry" v-model="param.recvCountry">
                  <input type="text" class="form-control" name="recvProvince" v-model="param.recvProvince" disabled placeholder="省份" required>
                 </div>
              	<div class="col-xs-4" style="padding:0">
                  <input type="text" class="form-control" name="recvCity" v-model="param.recvCity" disabled placeholder="市" required>
                 </div>
              	<div class="col-xs-5" style="padding:0">
                  <input type="text" class="form-control" name="recvArea" v-model="param.recvArea" disabled placeholder="区县" required>
                 </div>                                  
              </div>
            </div>
            <div class="col-xs-3" style="vertical-algin:center;font-weight:bolder;">
              <a class="btn btn-default" href="javascript:;" @click="selectReceiver">
               <img alt="" src="/icons/收货地址.png" width="70%" height="70%" style="max-width:30px;max-height:30px"><br>
               <span style="font-size:14px">收货地址</span>
              </a>
            </div>
          </div>
          <div class="row" style="font-weight:bolder">
            <input type="text" class="form-control" name="recvAddr" v-model="param.recvAddr" disabled placeholder="收货人地址" required>
          </div>
        </div>
     </div>
  </#if>
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
		},
		aftersaleReason: JSON.parse('${(aftersale.applyReason)!"[]"}'),
		aftersaleResult: JSON.parse('${(aftersale.dealResult)!"[]"}'),
		param:{
			orderId:'${order.orderId}',
			result:'',
			reason:'',
			recvId:'',
			recvName:'',
			recvPhone:'',
			recvAddr:'',
			recvCountry:'',
			recvProvince:'',
			recvCity:'',
			recvArea:''
		}
	},
	methods:{
		getDefaultReceiver:function(){
			$.ajax({
				url: '/preceiver/getdefault',
				method:'get',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.receiver){
						containerVue.param.recvId = jsonRet.receiver.recvId;
						containerVue.param.recvPhone = jsonRet.receiver.phone;
						containerVue.param.recvName = jsonRet.receiver.receiver;
						containerVue.param.recvCountry = jsonRet.receiver.country;
						containerVue.param.recvProvince = jsonRet.receiver.province;
						containerVue.param.recvCity =  jsonRet.receiver.city;
						containerVue.param.recvArea = jsonRet.receiver.area;
						containerVue.param.recvAddr = jsonRet.receiver.addr;
					}else{
						if(jsonRet && jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							//alertMsg('错误提示','获取默认收货人信息失败！');
						}
					}
				},
				dataType: 'json'
			});
		},
		selectReceiver:function(){
			$('#selectReceiverModal').modal('show');
			receiverManageVue.useFlag = 1 ;
			receiverManageVue.callBackFun = function(receiver){
				containerVue.param.recvId = receiver.recvId;
				containerVue.param.recvPhone = receiver.phone;
				containerVue.param.recvName = receiver.receiver;
				containerVue.param.recvCountry = receiver.country;
				containerVue.param.recvProvince = receiver.province;
				containerVue.param.recvCity =  receiver.city;
				containerVue.param.recvArea = receiver.area;
				containerVue.param.recvAddr = receiver.addr;
				$('#selectReceiverModal').modal('hide');
				containerVue.param.flag = 0;//重新检查
				containerVue.dispatchMatchs = [];
				containerVue.param.carrage = 0;
				containerVue.param.dispatchMode = '';
				containerVue.param.postageId = '';
				containerVue.param.postageIdAndMode = '';
			}
		},
		submit:function(){
			if(!this.param.result ){
				alertMsg('错误提示','处理结果不可为空！');
				return;
			}
			if(!this.param.reason || this.param.reason.length<3){
				alertMsg('错误提示','处理明细不可少于3个字符！');
				return;
			}
			if(this.param.result == '61'){
				if(!this.param.recvId ){
					alertMsg('错误提示','退货地址：不可为空！');
					return;
				}else{
					this.param.recvAddr = this.param.recvProvince + this.param.recvCity + this.param.recvArea + this.param.recvAddr;
				}
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
<#if order.status == '60'>
containerVue.getDefaultReceiver();
</#if>
</script>

<!-- 收货人显示Model -->
<div class="modal fade " style="height:450px;overflow:scroll" id="selectReceiverModal" tabindex="-1" role="dialog" aria-labelledby="selectReceiverModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="selectReceiverModalLabel">收货人选择</h4>
         </div>
         <div class="modal-body">
 			<#include "/preceiver/page-preceiver-tpl.ftl" encoding="utf8"> 
		</div>
     </div>
   </div>
</div><!-- end of modal -->

</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>
<footer>
 <#include "/menu/page-partner-func-menu.ftl" encoding="utf8">
</footer>

</body>
</html>

