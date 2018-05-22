<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>欢迎登陆 摩放优选</title>
	<!-- Bootstrap -->
	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!--Vue -->
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <!-- -->
    <link href="/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/templatemo-style.css" rel="stylesheet">
    
</head>
<body class="light-gray-bg">
<div class="templatemo-content-widget templatemo-login-widget white-bg" >
	<header class="text-center">
      <div class="square"></div>
      <h1>欢迎</h1>
    </header>
    <form action="/login" class="templatemo-login-form" method="post">
    	<div class="form-group">
    		<div class="input-group">
        		<div class="input-group-addon"><i class="fa fa-user fa-fw"></i></div>	        		
            <input class="form-control" type="text" name="username" value= "${username!""}" required placeholder="邮箱/手机号">           
        </div>	
    	</div>
    	<div class="form-group">
    		<div class="input-group">
        		<div class="input-group-addon"><i class="fa fa-key fa-fw"></i></div>	        		
            <input class="form-control" type="password" name="password"  required placeholder="******">           
        </div>	
    	</div>
		<div class="form-group">
			<button type="submit" class="templatemo-blue-button width-100"> 登 录 </button>
		</div>
    </form>
</div>
<div id="registerContainer">
<div class="templatemo-content-widget templatemo-login-widget templatemo-register-widget white-bg">
  <div class="row" style="padding:0 20px">
    <a href="/shop/index" class="pull-left"> 先逛一逛 </a>
    <a href="javascript:;" class="pull-right"  @click="needRegister = !needRegister">还未有账号? </a>
  </div>
</div>
<div v-if="needRegister == true" class="templatemo-content-widget templatemo-login-widget templatemo-register-widget white-bg" >
    <div class="row">
	  <p style="padding:3px 5px;font-weight:bold"> 
	    <a href="#"> 官方注册</a>
	  </p>
	</div>	
	<div class="row">
	  <p style="padding:3px 5px;font-weight:bold"> 
	    <a href="javascript:;" @click="scanQrCode"> 微信注册(扫描二维码关注)</a>
	  </p>
	</div>	
	<div class="row">
	  <p style="padding:3px 5px;font-weight:bold"> 
	    <span href="javascript:;"> 微信注册(搜索"摩放优选"并关注)</span>
	  </p>
	</div>
</div>
</div>
<script>
var containerVue = new Vue({
	el:'#registerContainer',
	data:{
		needRegister:false
	},
	methods:{
		scanQrCode: function(){
			$('#qrCodeModal').modal('show');
		}
	}
});
</script>

<div class="modal fade " id="qrCodeModal" tabindex="-1" role="dialog" aria-labelledby="qrCodeTitle" aria-hidden="false" data-backdrop="static" style="top:20%">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="qrCodeTitle" style="color:red" >扫描二维码关注</h4>
     		</div>
     		<div class="modal-body">
     		  <div class="row" style="text-align:center">
       			<img src="/img/mfyx_qrcode_zfc.jpg" width="200px" height="200px"></img>
       		  </div>
     		</div>
     		<div class="modal-footer">
     		  <p style="text-align:left">通过微信关注注册的用户有一定的系统注册延迟！！！</p>
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>
