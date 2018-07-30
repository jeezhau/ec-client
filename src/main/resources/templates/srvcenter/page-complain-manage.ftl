<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="overflow:scroll;">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">


<#if !errmsg??>
<div class="container" id="container" style="padding:0;margin-bottom:50px">
	<div class="row"><!-- 投诉内容 -->
	 <form class="form-horizontal" role="form" id="complainForm">
	   <h3 style="text-align:center;padding:3px 0 0 0;">投诉信息</h3>
	   <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">订单ID<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" v-model="params.orderId" maxlength="30" required placeholder="请输入您要投诉的商品订单ID">
         </div>
       </div> 
       <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">回访电话<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" maxlength="20" v-model="params.phone" required placeholder="请输入您当前使用的电话号码">
         </div>
       </div> 
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">投诉内容<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="600" rows=8 v-model="params.content" required placeholder="请输入您的投诉内容，最长600个字符。您的投诉反馈我们将尽快处理，请耐心等待。如果您需要获得即时处理，请填写了该表单提交后拨打投诉热线，热线号码见页面底部。"></textarea>
         </div>
        </div>
        <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;text-align:center">
           <button v-if="oprFlag != 'D'" type="button" class="btn btn-primary" style="margin:0 25px" @click="submit">提 交</button>
           <button v-if="oprFlag != 'D'" type="button" class="btn btn-warning" style="margin:0 25px" @click="reset">重 置</button>
           <button v-if="oprFlag == 'D'" type="button" class="btn btn-primary" style="margin:0 25px" @click="delComplain">删 除</button>
        </div>
	   </form>
	 </div>
     <div class="row" style="margin:5px 0px 3px 0px;">
        <div class="row" style="margin:1px 0px;background-color:white;">
          <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">我的投诉({{search.count}})</span>
          <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">
            <a v-if="search.count>0" href="/complain/show">查看全部&gt;</a>
          </span>
        </div>
	</div>
	<div class="row" style="margin:5px 3px 3px 3px;background-color:white">
	  <h5>快速通道</h5>
	  <span style="color: red">特别提示：<br>&nbsp;&nbsp;&nbsp;&nbsp;请先填写提交了上面的投诉表单之后再联系客服，可快速获得受理！！！</span><br>
	</div>     
</div>
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		newestlog:{cplanId:0},
		oprFlag:'${oprFlag!"S"}',
		search:{
			<#if cplanId??>cplanId:'',</#if>
			begin:0,
			pageSize:1,
			count:0
		},
		params:{
			cplanId:0,
			orderId:'',
			content:'',
			phone:''
		}
	},
	methods:{
		submit: function(){
			$.ajax({
				url: '/complain/save',
				method:'post',
				data: this.params,
				success: function(jsonRet,status,xhr){
					if(jsonRet.errcode == 0){
						alertMsg('系统提示','投诉信息已成功提交！');
						containerVue.search.count = containerVue.search.count + 1;
					}else{
						if(jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}
				},
				dataType: 'json'
			});
		},
		reset: function(){
			containerVue.params.cplanId = containerVue.newestlog.cplanId;
			containerVue.params.orderId = containerVue.newestlog.orderId;
			containerVue.params.content = containerVue.newestlog.content;
			containerVue.params.phone = containerVue.newestlog.phone;
		},
		delComplain: function(){
			$.ajax({
				url: '/complain/delete/' + this.params.cplanId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet.errcode == 0){
						alertMsg('系统提示','投诉信息已成功删除！');
						containerVue.search.count = containerVue.search.count - 1;
						window.location.href = "/complain/manage";
					}else{
						if(jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}
				},
				dataType: 'json'
			});
		},
		getComplain :function(){
			$.ajax({
				url: '/complain/getall',
				method:'post',
				data: this.search,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.pageCond){
						<#if cplanId??>
						if(jsonRet.datas){
							containerVue.newestlog =jsonRet.datas[0];
							containerVue.params.cplanId = containerVue.newestlog.cplanId;
							containerVue.params.orderId = containerVue.newestlog.orderId;
							containerVue.params.content = containerVue.newestlog.content;
							containerVue.params.phone = containerVue.newestlog.phone;
						}
						</#if>
						containerVue.search.count = jsonRet.pageCond.count;
					}else{
						if(jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getComplain();
</script>
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>