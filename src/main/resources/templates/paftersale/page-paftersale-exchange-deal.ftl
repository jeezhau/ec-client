<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    <#include "/head/page-fileinput-head.ftl" encoding="utf8">
    <#include "/head/page-map-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="margin-bottom:80px;padding:0;overflow:scroll">
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
  
  <!-- 换货处理信息 -->
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;填写说明：重新发货则需要填写发货的物流信息，官方配送则名称为“摩放优选”，单号为订单号；商家自取则名称为“商家名称”，单号为订单号；
  	快递配送则名称为“快递公司名称”，单号为物流公司的单号；买家送达则名称为“买家昵称”，单号为订单号；</span>
  </div>
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">处理结果<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <select class="form-control" v-model="param.result" required @change="">
           <option value="" disabled> 请选择... </option>
           <#if order.status=='50'>
           <option value="51"> 同意换货 </option>
           <option value="58"> 不同意换货 </option>
           </#if>
           <#if order.status=='52'>
           <option value="53"> 已收到退货、核验中 </option>
           <option value="55"> 重新发货 </option>
           <option value="54"> 核验不通过、协商解决 </option>
           </#if>
           <#if order.status=='53'>
           <option value="55"> 重新发货 </option>
           <option value="54"> 核验不通过、协商解决 </option>
           </#if>       
         </select>
       </div>
  </div>
  <#if order.status == '50'>
   <div v-if="param.result=='51'" class="row" style="margin:3px 0;padding:5px 3px;" id="receiverAddr">
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
  <div v-if="param.result == '55'">
  <div class="row" style="margin:3px 0">
    	  <label class="col-xs-3 control-label" style="padding-right:0">配送类型<span style="color:red">*</span></label>
       <div class="col-xs-9" style="padding-left:0">
         <select class="form-control" v-model="param.dispatchMode" required @change="changeDispatch">
           <option value="" disabled> 请选择...</option>
           <option value="2"> 商家自配 </option>
           <option value="3"> 快递配送 </option>
           <option value="4"> 买家自取 </option>
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
   </div>  
   <div class="row" style="margin:5px 0;text-align:center">
      <button type="submit" class="btn btn-danger" @click="submit">提交换货申请</button>
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
			reason:'',
			dispatchMode:'',
			logisticsComp:'',
			logisticsNo:'',
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
			if(!this.param.result){
				alertMsg('错误提示','处理结果不可为空！');
				return;
			}
			if(!this.param.reason || this.param.reason.length<3){
				alertMsg('错误提示','处理明细不可少于3个字符！');
				return;
			}
			if(this.param.result == '51'){
				if(!this.param.recvId ){
					alertMsg('错误提示','退货地址：不可为空！');
					return;
				}else{
					this.param.recvAddr = this.param.recvProvince + this.param.recvCity + this.param.recvArea + this.param.recvAddr;
				}
			}
			if(this.param.result == '55'){
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
			}
			$.ajax({
				url: '/paftersale/exchange/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/paftersale/manage/exchanging";
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
<#if order.status == '50'>
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

