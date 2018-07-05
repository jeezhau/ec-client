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
</head>
<body class="light-gray-bg" style="oveflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">

<div class="container " id="container" >
	<div class="row"><!-- 投诉内容 -->
	 <form class="form-horizontal" role="form" >
	   <h3 style="text-align:center;padding:3px 0 0 0;">投诉信息</h3>
	   <div v-if="complain.cpType=='1'" class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">订单ID</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" v-model="complain.orderId" maxlength="30" disabled>
         </div>
       </div>	   
	   <div v-if="complain.cpType=='2'" class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">上级合作伙伴ID</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" v-model="complain.partnerId" maxlength="30" disabled>
         </div>
       </div>
       <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">回访电话</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" maxlength="20" v-model="complain.phone" disabled>
         </div>
       </div>
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">投诉内容</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="600" rows=8 v-model="complain.content" disabled></textarea>
         </div>
        </div>
	   </form>
	 </div>
	<div class="row"><!-- 处理或回访 -->
	 <form class="form-horizontal" role="form">
	  <#if (mode!'')=='revisit'>
       <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">回访结果<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <label><input type="radio" value="0" v-model="param.result" style="display:inline-block;width:18px;height:18px" >须再处理</label>
		   <label><input type="radio" value="2" v-model="param.result" style="display:inline-block;width:18px;height:18px" >完成</label>		
         </div>
       </div>
       </#if>
       <#if (mode!'')=='deal' || (mode!'')=='revisit'>
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">内容<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="1000" rows=8 v-model="param.content" ></textarea>
         </div>
        </div>
        </#if>
        <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;text-align:center">
           <#if (mode!'')=='deal'><button v-if="complain.status=='0'" type="button" class="btn btn-primary" style="margin:0 25px" @click="submit">提交处理</button></#if>
           <#if (mode!'')=='revisit'><button v-if="complain.status=='1'" type="button" class="btn btn-primary" style="margin:0 25px" @click="submit">提交回访</button></#if>
           
           <button type="button" class="btn btn-default" style="margin:0 25px" onclick="window.close()"> 关 闭 </button>
        </div>
	   </form>
	 </div>
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		complain:{},
		param:{
			logId:'${logId!""}',
			result:'',
			content:''
		},
		search:{
			begin:0,
			pageSize:1,
			cplanId:'${logId!""}'
		}
	},
	methods:{
		getComplain :function(){
			$.ajax({
				url: '/pcomplain/sys/getall',
				method:'post',
				data: {'searchParams':JSON.stringify(this.search)},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						containerVue.complain =jsonRet.datas[0];
					}else{
						alertMsg('错误提示',jsonRet.errmsg);
					}
				},
				dataType: 'json'
			});
		},
		submit :function(){
			<#if (mode!'')=='revisit'>
			if(this.param.resutl == ''){
				alertMsg('错误提示','回访结果：不可为空！');
				return;
			}
			</#if>
			if(!this.param.content || this.param.content.length>1000){
				alertMsg('错误提示','处理内容：最长1000字符！');
				return;
			}
			$.ajax({
				<#if (mode!'')=='deal'>url: '/pcomplain/deal',</#if>
				<#if (mode!'')=='revisit'>url: '/pcomplain/revisit',</#if>
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode == 0){
							alertMsg('系统提示','信息提交成功！！！');
							window.close();
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

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
