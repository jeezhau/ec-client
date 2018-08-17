<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="oveflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">

<div class="container " id="container" >
	<div class="row" style="background-color:white"><!-- 投诉内容 -->
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
	    <div v-if="complain.dealLog && complain.dealLog.length>0" class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
		  <label class="row control-label" style="margin:0">处理结果：</label>
          <div class="row" style="margin:0">
		    <div v-for="deal in complain.dealLog" v-if="deal && deal.time" class="row" style="margin:1px 0px;padding:0 20px;">
		     <div class="row">
		       <span class="pull-left">处理人ID：{{deal.oprid}}</span>
		       <span class="pull-right">处理时间：{{deal.time}}</span>
		     </div>
		     <div class="row">
		       <p>处理内容：{{deal.result}}</p>
		     </div>
		    </div>
		  </div>
	    </div>
	    <div v-if="complain.revisitLog && complain.revisitLog.length>0" class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
		  <label class="row control-label" style="margin:0">回访结果：</label>
          <div class="row" style="margin:0">
		    <div v-for="revisit in complain.revisitLog" v-if="revisit && revisit.time" class="row" style="margin:1px 0px;padding:0 20px;">
		     <div class="row">
		       <span class="pull-left">回访人ID：{{revisit.oprid}}</span>
		       <span class="pull-right">回访时间：{{revisit.time}}</span>
		     </div>
		     <div class="row">
		       <p>回访内容：{{revisit.result}}</p>
		     </div>
		    </div>
		  </div>
	    </div>
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
						containerVue.complain.dealLog = JSON.parse(containerVue.complain.dealLog?containerVue.complain.dealLog:"[]");
						containerVue.complain.revisitLog = JSON.parse(containerVue.complain.revisitLog?containerVue.complain.revisitLog:"[]");
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
