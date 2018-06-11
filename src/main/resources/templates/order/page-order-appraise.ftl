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
    <link href="/css/bootstrap-slider.min.css" rel="stylesheet">
    <script src="/script/bootstrap-slider.min.js"></script>
    
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="padding:0;overflow:scroll">
  <#include "/order/tpl-order-buy-user-4fm.ftl" encoding="utf8"> 
  <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8"> 

  <!-- 商家 -->
  <div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/shop/mcht/${order.partnerId}">
        <img alt="头像" src="/shop/pcert/logo/${order.partnerId}" width="20px" height="20px" style="border-radius:50%"> 
        ${order.partnerBusiName}
      </a>
  </div>
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;填写说明：如果您已经收到购买的商品，请即时提交您的签收评价！</span>
  </div>
  <div class="row" style="margin:3px 0; background-color:white">
    <#if !(order.appraiseTime)??>
    <div class="row" style="margin:5px 0;">
    	   <label class="col-xs-5 control-label" style="padding-right:0">物流服务得分<span style="color:red">*</span></label>
       <div class="col-xs-7" style="padding-left:0">
         <input id="scoreLogistics" type="text"  data-slider-ticks="[1,10]" data-slider-ticks-labels='["1", "10"]'
              data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="9" data-slider-tooltip="show">
       </div>
    </div>
    <div class="row" style="margin:5px 0">
    	   <label class="col-xs-5 control-label" style="padding-right:0">商品品质描述得分<span style="color:red">*</span></label>
       <div class="col-xs-7" style="padding-left:0">
         <input id="scoreGoods" type="text"  data-slider-ticks="[1,10]" data-slider-ticks-labels='["1", "10"]'
              data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="9" data-slider-tooltip="show">
       </div>
     </div>
     <div class="row" style="margin:5px 0">
    	   <label class="col-xs-5 control-label" style="padding-right:0">商家服务得分<span style="color:red">*</span></label>
       <div class="col-xs-7" style="padding-left:0">
         <input id="scoreMerchant" type="text"  data-slider-ticks="[1,10]" data-slider-ticks-labels='["1", "10"]'
              data-slider-min="1" data-slider-max="10" data-slider-step="1" data-slider-value="9" data-slider-tooltip="show">
       </div>
     </div>
     </#if>
     <div class="row" style="margin:3px 0">
    	   <label class="col-xs-3 control-label" style="padding-right:0">评价内容</label>
       <div class="col-xs-9" style="padding-left:0">
         <textarea class="form-control" v-model="param.content" maxlength="600"></textarea>
       </div>
     </div>
     <div class="row" style="margin:15px 0;text-align:center">
       <button type="submit" class="btn btn-danger" @click="submit">提交评价</button>
     </div>
  </div>
  <div class="row" style="margin:3px 0;">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">历史评价</span>
    </div>
    <div v-for="item in order.appraiseInfo" class="row" style="margin:1px 1px ;padding:3px 0;background-color:white" >
      <div class="col-xs-12">
       <span class="pull-left">{{item.time}}</span>
      </div>
      <div class="col-xs-12">
       {{item.content}}
      </div>
     </div>
   </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		order:{
			status:'${order.status}',
			goodsSpec:JSON.parse('${(order.goodsSpec)!"[]"}'),
			appraiseInfo: JSON.parse('${(order.appraiseInfo)!"[]"}'),
		},
		param:{
			orderId:'${order.orderId}',
			<#if !(order.appraiseTime)??>
			scoreLogistics: 10, 
			scoreMerchant: 10, 
			scoreGoods: 10, 
			</#if>
			content:''
		}
	},
	methods:{
		submit:function(){
			$("#dealingData").show();
			if(this.param.content && this.param.content.length > 600){
				alertMsg('错误提示','评价内容不可多于600个字符！');
				return;
			}
			<#if !(order.appraiseTime)??>
			this.param.scoreLogistics = $("#scoreLogistics").slider().val();
			this.param.scoreMerchant = $("#scoreMerchant").slider().val();
			this.param.scoreGoods = $("#scoreGoods").slider().val();
			</#if>
			$.ajax({
				url: '/order/appraise/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/order/show/all";
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
$("#scoreLogistics").slider({ min: 0, max: 10, value: 10, focus: true });

$("#scoreGoods").slider({ min: 0, max: 10, value: 10, focus: true });

$("#scoreMerchant").slider({ min: 0, max: 10, value: 10, focus: true });

</script>
</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<#include "/menu/page-bottom-menu.ftl" encoding="utf8">

</body>
</html>

