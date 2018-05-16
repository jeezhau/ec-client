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
    
    <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <link href="/css/weui.css" rel="stylesheet">
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<div class="container " style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li style="width:50%" class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editAct').show();$('#editPayPwd').hide();">
	    <a href="javascript:;">提现银行账户</a>
	  </li>
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editAct').hide();$('#editPayPwd').show();">
	    <a href="javascript:;">会员管理密码</a>
	  </li>
	</ul>
  </div>
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white" id="editAct">
    <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;请正确输入银行账户信息，如果信息有误，导致的打款失败或错误，将由会员您自己承担！！！手机号为银行开户时预留的手机号，请确保该手机号是您在使用，因如果您忘记密码时将使用该手机号码进行找回！！！</p>
	<form class="form-horizontal" id="editForm" action="" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
	  <div class="form-group">
	    <label class="col-xs-3 control-label">身份证号<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="text" class="form-control" v-model="param.actId" pattern="\w{18}"  maxLength=100 required placeholder="请输入个人/法人身份证号">
	    </div>
	  </div> 
	  <div class="form-group">
	    <label class="col-xs-3 control-label">手机号<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="tel" class="form-control" v-model="param.phone" pattern="\w{11}"  maxLength=100 required placeholder="请输入开户预留手机号">
	    </div>
	  </div>	
	  <div class="form-group">
	    <label class="col-xs-3 control-label">账户名称<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="text" class="form-control" v-model="param.actNm" pattern="\w{2,100}" maxLength=100 required placeholder="请输入账户名称（2-100个字符）">
	    </div>
	  </div>  	  
	  <div class="form-group">
        <label class="col-xs-3 control-label">账户号<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="text" class="form-control" v-model="param.actNo" pattern="\w{3,30}" maxLength=30 required placeholder="请输入账户号（3-30个字符）">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">开户行名称<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="text" class="form-control" v-model="param.actBlk" pattern="\w{2,100}" maxLength=100 required placeholder="请输入开户行名称（2-100个字符）">
        </div>
      </div> 
      <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning"  style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>
	</form>	
  </div>
  
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white;display:none" id="editPayPwd">
    <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;在本系统中的会员将有一个会员管理密码，修改银行账户信息、余额支付、取消、退款、提现等都需要使用该密码，请谨慎设置和保管，不要向任何人透露，我们的工作人员绝不会以任何理由要求您提供您的密码！！！初始密码为提现账户信息中的手机号的后6位！</p>
    	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >  
      <div class="form-group">
        <label class="col-xs-3 control-label">原密码<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="password" class="form-control" v-model="param.oldPwd" pattern="\w{6,20}" title="6-20个字符组成" maxLength=30 required placeholder="请输入原密码（6-20个字符）">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">新密码<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="password" class="form-control" v-model="param.newPwd" pattern="\w{6,20}" title="6-20个字符组成" maxLength=30 required placeholder="请输入新密码（6-20个字符）">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">确认密码<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="password" class="form-control" v-model="param.reNewPwd" pattern="\w{6,20}" title="6-20个字符组成" maxLength=30 required placeholder="请输入确认密码（6-20个字符）">
        </div>
      </div>       
      <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning" style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>
	</form>
	<a href="javascript:;">忘记密码？</a>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var editActVue = new Vue({
	el:'#editAct',
	data:{
		initData:{
			actNm:'${(vipBasic.accountName)!""}',
			actNo:'${(vipBasic.accountNo)!""}',
			actBlk:'${(vipBasic.accountBlank)!""}',
		},	//初始化的数据
		param:{
			actNm:'${(vipBasic.accountName)!""}',
			actNo:'${(vipBasic.accountNo)!""}',
			actBlk:'${(vipBasic.accountBlank)!""}',
		}
	},
	methods:{
		submit: function(){
			$.ajax({
				url: '/user/basic/update',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"提现银行账户信息修改成功！")
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		},
		reset: function(){
			$.extend(editActVue.param,editActVue.initData); 
		}
	}
});

var editPayPwdVue = new Vue({
	el:'#editPayPwd',
	data:{
		param:{
			oldPwd:'',
			newPwd:'',
			reNewPwd:''
		}
	},
	methods:{
		submit: function(){
			$.ajax({
				url: '/user/basic/update',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"会员管理密码修改成功！")
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		},
		reset: function(){
			editPayPwd.param = {oldPwd:'',newPwd:'',reNewPwd:''}; 
		}
	}
});
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
