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

<#if ((vipBasic.status)!'') == '1'>

<div class="container"  id="container" style="padding:0px 0px;oveflow:scroll">

  <div class="row" style="min-height:100px">
    <div v-for="item in dataList" class="row" style="margin:5px 10px;padding:3px 0;background-color:white">
      <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
        <span class="col-xs-12" style="padding:0"><label> 账 户：</label><span>{{item.accountName}} {{item.accountNo}}</span></span>
      </div>
      <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3">
        <span class="col-xs-6" style="padding:0"><label> 提现方式：</label><span>{{getCashType(item.cashType)}}</span></span>
        <span class="col-xs-6" style="padding:0"><label> 通道类型：</label><span>{{getChannelType(item.channelType)}}</span></span>
      </div>
      <div class="col-xs-12" style="text-align:center">
        <span class="col-xs-4"><button class="btn btn-info" style="width:90%" @click="showAccount(item)"> 详 情 </button></span>
        <span class="col-xs-4"><button class="btn btn-primary" style="width:90%" @click="updAccount(item)"> 变 更 </button></span>
        <span class="col-xs-4"><button class="btn btn-default" style="width:90%" @click="delAccount(item)"> 删 除 </button></span>
      </div>
    </div>
  </div>
  <div class="row" style="margin:10px ;text-align:center">
    <button type="button" class="btn btn-danger" style="width:90%" @click="addAccount"> 新增账户 </button>
  </div>
  
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		dataList:[]
	},
	methods:{
		addAccount: function(){
			$('#saveAccountModal').modal('show');
			saveAccountVue.saveAccountTitle = '新增账户';
			saveAccountVue.oprFlag = 1;
			saveAccountVue.param.settleId = 0;
		},
		updAccount: function(item){
			$('#saveAccountModal').modal('show');
			saveAccountVue.saveAccountTitle = '变更账户';
			saveAccountVue.oprFlag = 2;
			$.extend(saveAccountVue.param,item); 
			saveAccountVue.initData = item;
		},
		delAccount: function(item){
			$('#saveAccountModal').modal('show');
			saveAccountVue.saveAccountTitle = '删除账户';
			saveAccountVue.oprFlag = 3;
			$.extend(saveAccountVue.param,item);
		},
		showAccount: function(item){
			$('#saveAccountModal').modal('show');
			saveAccountVue.saveAccountTitle = '账户详情';
			saveAccountVue.oprFlag = 0;
			$.extend(saveAccountVue.param,item);
		},
		getAll: function(){
			$("#loadingData").show();
			$.ajax({
				url: '/vip/settle/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					containerVue.dataList = [];
					$("#loadingData").hide();
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							containerVue.dataList.push(jsonRet.datas[i]);
						}
					}else{
						if(jsonRet && jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							//alertMsg('错误提示',jsonRet.errmsg);
						}
					}
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getAll();
</script>

<div class="modal fade " id="saveAccountModal" tabindex="-1" role="dialog" aria-labelledby="saveAccountTitle" aria-hidden="false" data-backdrop="static" style="top:10px">
<div class="modal-dialog">
<div class="modal-content">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
    <h4 class="modal-title" id="saveAccountTitle" style="color:red" >{{saveAccountTitle}}</h4>
  </div>
  <div class="modal-body">
	  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white;">
	    <p v-if="oprFlag == 1 || oprFlag == 2" style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;请正确输入账户信息，如果信息有误，导致的打款失败或错误，将由会员您自己承担！！！</p>
		<form class="form-horizontal"  action="" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
		  <div class="form-group">
		    <label class="col-xs-3 control-label" style="padding-right:0">提现方式<span style="color:red">*</span></label>
		    <div class="col-xs-8">
		      <select v-model="param.cashType" :disabled="oprFlag == 0 || oprFlag ==3">
		        <option value="" disabled> 请选择提现方式... </option>
		        <option value="1">手动</option>
		        <!-- <option value="2">自动(每周一)</option>
		        <option value="3">自动(每月一号)</option> -->
		      </select>
		    </div>
		  </div>	
		  <div class="form-group">
		    <label class="col-xs-3 control-label" style="padding-right:0">账户类型<span style="color:red">*</span></label>
		    <div class="col-xs-8">
			   <label><input type="radio" value="1" v-model="param.accountType" style="display:inline-block;width:18px;height:18px" :disabled="oprFlag == 0 || oprFlag ==3">对私</label>
			   <label><input type="radio" value="2" v-model="param.accountType" style="display:inline-block;width:18px;height:18px" :disabled="oprFlag == 0 || oprFlag ==3">对公</label>
		    </div>
		  </div>	
		  <div class="form-group">
		    <label class="col-xs-3 control-label" style="padding-right:0">身份证号<span style="color:red">*</span></label>
		    <div class="col-xs-8">
		      <input type="text" class="form-control" v-model="param.idNo" pattern="\w{18}"  maxLength=100 required placeholder="持账户人身份证号" :disabled="oprFlag == 0 || oprFlag ==3">
		    </div>
		  </div>
		  <div class="form-group">
		    <label class="col-xs-3 control-label" style="padding-right:0">通道类型<span style="color:red">*</span></label>
		    <div class="col-xs-8">
		       <select v-model="param.channelType" :disabled="oprFlag == 0 || oprFlag ==3" @change="changeChl">
		        <option value="" disabled> 请选择通道类型... </option>
		        <option value="1">银行</option>
		        <!-- <option value="2">微信</option> -->
		        <option value="3">支付宝</option>
		      </select>
		    </div>
		  </div>		  
		  <div class="form-group">
		    <label class="col-xs-3 control-label" style="padding-right:0">账户名称<span style="color:red">*</span></label>
		    <div class="col-xs-8">
		      <input type="text" class="form-control" v-model="param.accountName" pattern="\w{2,100}" maxLength=100 required placeholder="账户名称（2-100个字符）" :disabled="oprFlag == 0 || oprFlag ==3">
		    </div>
		  </div>
		  <div class="form-group">
	        <label class="col-xs-3 control-label" style="padding-right:0">账户号<span style="color:red">*</span></label>
	        <div class="col-xs-8">
	          <input type="text" class="form-control" v-model="param.accountNo" pattern="\w{3,30}" maxLength=30 required placeholder="账户号（3-30个字符）" :disabled="oprFlag == 0 || oprFlag ==3">
	        </div>
	      </div>
	      <div class="form-group">
	        <label class="col-xs-3 control-label" style="padding-right:0">开户行<span style="color:red">*</span></label>
	        <div class="col-xs-8">
	          <input type="text" class="form-control" v-model="param.accountBank" pattern="\w{2,100}" maxLength=100 required placeholder="开户行名称（2-100个字符）" :disabled="oprFlag == 0 || oprFlag ==3">
	        </div>
	      </div>
	      <div class="form-group" v-if="oprFlag != 0">
	        <label class="col-xs-3 control-label"  style="padding-right:0">会员密码<span style="color:red">*</span></label>
	        <div class="col-xs-8">
	          <input type="password" class="form-control" v-model="param.passwd" pattern="\w{6,20}" maxLength=20 required placeholder="会员操作密码（6-20个字符）" >
	        </div>
	      </div>
	      <div class="form-group">
	         <div style="text-align:center">
	           <button v-if="oprFlag==1 || oprFlag==2" type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;保 存&nbsp;&nbsp;</button>
	           <button v-if="oprFlag==1 || oprFlag==2" type="button" class="btn btn-danger"  style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
	           <button v-if="oprFlag==3 " type="button" class="btn btn-danger"  style="margin:20px" @click="deleteAct">&nbsp;&nbsp;删 除&nbsp;&nbsp; </button>
	           <button v-if="oprFlag==0 || oprFlag==3" type="button" class="btn btn-default"  style="margin:20px" @click="close">&nbsp;&nbsp;关 闭&nbsp;&nbsp; </button>
	         </div>
	      </div>
		</form>	
	  </div>
  </div>
</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
var saveAccountVue = new Vue({
	el:'#saveAccountModal',
	data:{
		oprFlag:1,
		saveAccountTitle:'',
		initData:{},
		param:{
			settleId:'',
			cashType:'',
			idNo:'',
			channelType:'',
			accountType:'',
			accountName:'',
			accountNo:'',
			accountBank:'',
			passwd:''
		}
	},
	methods:{
		changeChl:function(){
			if(this.param.channelType=='2'){
				this.param.accountBank='微信';
			}else if(this.param.channelType=='3'){
				this.param.accountBank='支付宝';
			}
		},
		submit: function(){
			delete this.param.updateTime;
			delete this.param.vipId;
			$("#dealingData").show();
			$.ajax({
				url: '/vip/settle/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode !== 0){
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}else{
							containerVue.getAll();
						}
					}else{
						alertMsg('错误提示',jsonRet.errmsg)
					}
					$('#saveAccountModal').modal('hide');
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		},
		reset: function(){
			$.extend(containerVue.param,containerVue.initData); 
		},
		close: function(){
			$('#saveAccountModal').modal('hide');
		},
		deleteAct: function(){
			$("#dealingData").show();
			$.ajax({
				url: '/vip/settle/delete',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode !== 0){
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}else{
							containerVue.getAll();
						}
						containerVue.getAll();
					}else{
						alertMsg('错误提示',jsonRet.errmsg)
					}
					$('#saveAccountModal').modal('hide');
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

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
