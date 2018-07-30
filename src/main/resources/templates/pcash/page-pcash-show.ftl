<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="oveflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#if (vip.vipId)??>
<div class="container " id="container" style="padding:0">
	<div class="row" style="margin:0;margin-top:8px;"><!-- 申请内容 -->
	  <h3 style="text-align:center;background-color:white">申请信息</h3>
	  <div class="row" style="margin:0;margin-top:1px;background-color:white">
		<div class="col-xs-6" style="padding:0 5px">申请时间：${(apply.applyTime)?string("yyyy-MM-dd hh:mm:ss")}</div>
		<div class="col-xs-6" style="padding:0 5px">当前状态：{{getCashApplyStatus('${apply.status}')}}</div>
	  </div>
	  <div class="row"  style="margin:0;margin-top:1px;background-color:white">
		<div class="col-xs-6" style="padding:0 5px">申请会员ID：${(apply.vipId)?string('#')}</div>
		<div class="col-xs-6" style="padding:0 5px">可用余额(元)：${(vip.balance)/100}</div>
	  </div>
	  <div class="row"  style="margin:0;margin-top:1px;background-color:white">
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">提现方式：{{getCashType('${apply.cashType}')}}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">账户类型：{{getAccountType('${apply.accountType}')}}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">通道类型：{{getChannelType('${apply.channelType}')}}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">账户机构：${(apply.accountBank)!''}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">身份证号：${(apply.idNo)!''}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">账 户 号 ：${(apply.accountNo)!''}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">提现额(元)：${(apply.cashAmount)/100}</div>
	    <div class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 5px">手续费(元)：${(apply.cashFee)/100}</div>
	  </div>
	</div>
	<div class="row"  style="margin:0;margin-top:3px;background-color:white"><!-- 受理或完成 -->
	 <form class="form-horizontal" role="form">
	 <#if apply.status=='1' || apply.status=='S' || apply.status=='F' >
	  <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
	   <label class="col-xs-3 col-sm-2 col-md-1 col-lg-1 control-label">处理备注</label>
       <div class="col-xs-9 col-xs-10 col-xs-11 col-xs-11">
         <p class="form-control">${(apply.memo)!''}</p>
       </div>
      </div>
	 </#if>
	 <#if ((mode!'')=='accept' || (mode!'')=='finish') && (apply.status=='0' || apply.status=='1')>
       <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">处理结果<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <#if (mode!'')=='accept'>
           <label><input type="radio" value="1" v-model="param.result" style="display:inline-block;width:18px;height:18px" >受理</label>
           </#if>
           <#if (mode!'')=='finish'>
           <label><input type="radio" value="S" v-model="param.result" style="display:inline-block;width:18px;height:18px" >提现成功</label>
		   <label><input type="radio" value="F" v-model="param.result" style="display:inline-block;width:18px;height:18px" >提现失败</label>	
		    </#if>	
         </div>
       </div>
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">备注内容<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="1000" rows=8 v-model="param.memo" ></textarea>
         </div>
        </div>
      </#if>
        <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;text-align:center">
        <#if ((mode!'')=='accept' || (mode!'')=='finish') && (apply.status=='0' || apply.status=='1')>
           <button type="button" class="btn btn-primary" style="margin:0 25px" @click="submit">提交</button>
        </#if>
           <button type="button" class="btn btn-default" style="margin:0 25px" onclick="window.close()"> 关 闭 </button>
        </div>
	   </form>
	 </div>
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			applyId:'${apply.applyId}',
			vipId:'${(apply.vipId)?string("#")}',
			result:'',
			memo:''
		}
	},
	methods:{
		submit :function(){
			if(!this.param.result){
				alertMsg('错误提示','处理结果：不可为空！');
				return;
			}
			if(!this.param.memo || this.param.memo.length<2 || this.param.memo.length>1000){
				alertMsg('错误提示','处理内容：长度2-1000字符！');
				return;
			}
			$.ajax({
				url: '/pcash/deal',
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
</script>
</#if>
<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
