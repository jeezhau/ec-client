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
    
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if (payFlow.flowId)?? >
<div class="container " id="container" style="padding:0;overflow:scroll">
  <!-- 商品信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:center;">${order.goodsName}</div>
    <div class="col-xs-12" style="text-align:center;">
      <a href="/shop/goods/${(order.goodsId)?string('#')}">
       <img alt="" src="/shop/gimage/${(order.partnerId)?string('#')}/${(order.goodsMainImgPath)!''}" style="max-width:99%;max-height:160px;">
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
  </div> 
  <!-- 支付明细 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
       <label class="col-xs-3" style="padding:0">订单号：</label>
       <span class="col-xs-8" style="padding:0">${order.orderId!''}</span>
    </div>
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
		goodsSpecArr:JSON.parse('${(order.goodsSpec)!"[]"}'),
		param:{
			orderId:'${order.orderId}',
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
				url: '/order/pay/submit/bal/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//创建支付成功
							window.location.href = "/order/pay/finish/" + containerVue.param.orderId;
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

