<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8"> 
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container" id="container" style="margin:0;padding:0;overflow:scroll;margin-bottom:50px">
  <div class="row" >
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <h3 style="text-align:center;padding:5px 0 0 0;">提现申请</h3>
      <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
        <label class="col-xs-4 control-label" style="padding-right:1px">可提现余额(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(vipBasic.balance)/100}" disabled>
        </div>
      </div>
      <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
        <label class="col-xs-4 control-label" style="padding-right:1px">选择账户<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px" >
          <select class="form-control" v-model="param.accountNo" v-on:change="changeAccount" >
            <option value="" disabled> 请选择提现账户...</option>
            <option v-for="item in metadata.accounts" v-bind:value="item.accountNo" >{{item.accountName}} {{item.accountNo}} </option>
          </select>
        </div>
      </div>
      <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
        <label class="col-xs-4 control-label" style="padding-right:1px">提现金额(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.cashAmount" maxLength=10 @change="changeAmount" placeholder="提现金额" >
        </div>
      </div>
      <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
        <label class="col-xs-4 control-label" style="padding-right:1px">会员密码<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="password" class="form-control" v-model="param.passwd" maxLength=20 placeholder="会员密码" >
        </div>
      </div>      
      <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <div style="text-align:center">
           <button type="button" class="btn btn-info" id="save" style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
         </div>
      </div>
	</form>
  </div>

  <div class="row" style="margin:5px 0px 3px 0px;">
     <div class="row" style="margin:1px 0px;background-color:white;">
       <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">我的申请({{search.count}})</span>
       <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">
         <a v-if="search.count>0" href="/cash/show">查看全部&gt;</a>
       </span>
     </div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		metadata:{
			accounts:[]
		},
		param:{
			cashType:'',
			accountType:'',
			idNo:'',
			channelType:'',
			accountName:'',
			accountNo:'',
			accountBank:'',
			cashAmount:5,
			passwd:''
		},
		search:{
			begin:0,
			count:0
		}
	},
	methods:{
		changeAccount:function(){
			for(var i=0;i<this.metadata.accounts.length;i++){
				var item = this.metadata.accounts[i];
				if(item.accountNo == this.param.accountNo){
					this.param.cashType = item.cashType;
					this.param.accountType = item.accountType;
					this.param.idNo = item.idNo;
					this.param.channelType = item.channelType;
					this.param.accountName = item.accountName;
					this.param.accountBank = item.accountBank;
				}
			}
		},
		changeAmount: function(){
			var val = parseInt(this.param.cashAmount);
			if(isNaN(val) || val < 1 || val > 999999999){
				this.param.cashAmount = 5;
				return;
			}
			this.param.cashAmount = val;
		},
		submit: function(){
			$("#dealingData").show();
			if(!this.param.accountNo){
				alertMsg('错误提示','提现账户信息不可为空！');
				$("#dealingData").hide();
				return;
			}
			if(!this.param.cashAmount || this.param.cashAmount<5){
				alertMsg('错误提示','提现金额不可少于5元！');
				$("#dealingData").hide();
				return;
			}
			if(this.param.cashAmount>${vipBasic.balance/100}){
				alertMsg('错误提示','提现金额超过最大可用余额！');
				$("#dealingData").hide();
				return;
			}
			$.ajax({
				url: '/cash/apply/submit',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"提现申请提交成功！");
							container.param = {
								cashType:'',
								accountType:'',
								idNo:'',
								channelType:'',
								accountName:'',
								accountNo:'',
								accountBank:'',
								cashAmount:5,
								passwd:''
							}
							containerVue.search.count = containerVue.search.count+1;
						}else{//出现逻辑错误
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		},
		getAccounts: function(){
			$.ajax({
				url: '/vip/settle/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					containerVue.metadata.accounts = [];
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							containerVue.metadata.accounts.push(jsonRet.datas[i]);
						}
					}else{
						if(jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							//alertMsg('错误提示',jsonRet.errmsg);
						}
					}
				},
				dataType: 'json'
			});
		},
		getCnt: function(stat,event){
			containerVue.dataList = [];
			$.ajax({
				url: '/cash/getall',
				method:'post',
				data: {'begin':0,'pageSize':1},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						containerVue.search.begin = jsonRet.pageCond.begin;
						containerVue.search.count = jsonRet.pageCond.count;
					}
				},
				dataType: 'json'
			});
		}
	}
		
});
containerVue.getAccounts();
containerVue.getCnt();
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8">
</footer>

</body>
</html>
