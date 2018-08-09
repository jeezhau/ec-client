<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    <link href="/css/bootstrap-slider.min.css" rel="stylesheet">
    <script src="/script/bootstrap-slider.min.js"></script>
</head>
<body class="light-gray-bg" style="overflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (order.orderId)?? >
<div class="container " id="container" style="padding:0;">
  <#include "/porder/tpl-porder-buy-user-4fm.ftl" encoding="utf8"> 
  <#include "/common/tpl-order-buy-content-4fm.ftl" encoding="utf8"> 

  <!-- 商家 -->
  <div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/partner/mcht/${(order.partnerId)?string('#')}">
        <img alt="头像" src="/partner/cert/show/logo/${(order.partnerId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
        ${order.partnerBusiName}
      </a>
  </div>
  <div class="row" style="margin:3px 0px;background-color:white; color:red">
    <p/>
  	<span>&nbsp;&nbsp;&nbsp;&nbsp;填写说明：如果客户已经签收评价，请您也完成对客户的评价！</span>
  </div>
  <div class="row" style="margin:3px 0; background-color:white">
    <#if !(appraise)??>
    <div class="row" style="margin:5px 0;">
    	   <label class="col-xs-4 control-label" style="padding-right:0">客户得分<span style="color:red">*</span></label>
       <div class="col-xs-7" style="padding-left:0">
         <input id="score" type="text"  data-slider-ticks="[1,10]" data-slider-ticks-labels='["1","10"]'
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
    <div v-for="item in apprUser" class="row" style="margin:1px 1px ;padding:3px 0;background-color:white" >
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
		},
		apprUser: JSON.parse('${(appraise.content)!"[]"}'),
		param:{
			orderId:'${order.orderId}',
			<#if !(appraise)??>
			score: 10, 
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
			<#if !(appraise)??>
			this.param.score = $("#score").slider().val();
			</#if>
			$.ajax({
				url: '/psaleorder/appraise/submit/' + this.param.orderId ,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/psaleorder/show/all";
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
$("#score").slider({ min: 0, max: 10, value: 10, focus: true });

</script>
</#if> 

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

