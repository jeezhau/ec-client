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

<#if ((vipBasic.status)!'') == "1">
<div class="container" id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="col-xs-12" style="margin-top:5px;background-color:white;">
     <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;手机号请确保是您在使用，因如果您忘记密码时将使用该手机号码进行找回，同时在账户提现时可能需要用到收取验证码！！！</p>
	 <#if (vipBasic.phone)?? && (vipBasic.phone)?length gt 10>
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">原手机号<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <input type="tel" class="form-control" v-model="param.oldPhone" disabled placeholder="请输入手机号">
	    </div>
	  </div>	
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">原手机验证码<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
		    <div class="col-xs-7" style="padding:0">
		      <input class="form-control" v-model="param.oldVeriCode" pattern="\d{6}"  maxLength=6 required placeholder="原手机号验证码">
		    </div>
		    <div class="col-xs-2">
		      <button v-if="time.oldTime==120 || time.oldTime <= 0 " type="button" class="btn btn-primary" @click="getVeriCode('old')">获取验证码</button>
		      <button v-if="time.oldTime<120 && time.oldTime > 0 " type="button" class="btn btn-default" > {{time.oldTime}} s </button>
		    </div>
	    </div>	    
	  </div> 
	 </#if>	
	 <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">新手机号<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <input type="tel" class="form-control" v-model="param.newPhone" pattern="\d{11}" maxLength=11 required placeholder="新手机号">
	    </div>
	  </div>	
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">新手机验证码<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
		    <div class="col-xs-7" style="padding:0">
		      <input class="form-control" v-model="param.newVeriCode" pattern="\d{6}"  maxLength=6 required placeholder="新手机号验证码">
		    </div>
		    <div class="col-xs-2">
		      <button v-if="time.newTime==120 || time.newTime <= 0 " type="button" class="btn btn-primary" @click="getVeriCode('new')">获取验证码</button>
		      <button v-if="time.newTime<120 && time.newTime > 0 " type="button" class="btn btn-default" > {{time.newTime}} s </button>
		    </div>
	    </div>	    
	  </div>
	  <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submitPhone">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
         </div>
      </div>	
  </div>
</div>
  <script type="text/javascript">
  var containerVue = new Vue({
	  el:'#container',
	  data:{
		  param:{
			  oldPhone:'${(vipBasic.phone)!''}',
			  oldVeriCode:'',
			  newPhone:'',
			  newVeriCode:''
		  },
  		  time:{
  			oldTimer: null,
  			oldTime: 120,
  			newTimer: null,
  			newTime: 120
  		  }
	  },
	  methods:{
		  submitPhone: function(){
			  $("#dealingData").show();
			  var pattern1 = /^1[3-9]\d{9}$/;
			  var pattern2 = /^\d{6}$/;
			  if(!pattern1.exec(this.param.newPhone)){
				  alertMsg('错误提示','新手机号格式不正确！')
				  return;
			  }
			  if(!pattern2.exec(this.param.newVeriCode)){
				  alertMsg('错误提示','新手机号验证码格式不正确！')
				  return;
			  }
			  <#if (vipBasic.phone)?? && (vipBasic.phone)?length gt 10>
			  if(!pattern2.exec(this.param.oldVeriCode)){
				  alertMsg('错误提示','原手机号验证码格式不正确！')
				  return;
			  }
			  </#if>
			  $.ajax({
					url: '/vip/vipset/updphone',
					method:'post',
					data: this.param,
					success: function(jsonRet,status,xhr){
						$("#dealingData").hide();
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode !== 0){
								alertMsg('错误提示',jsonRet.errmsg)
							}else{
								window.location.href = "/vip/vipset";
							}
						}else{
							alertMsg('错误提示','绑定手机号失败！')
						}
					},
					failure:function(){
						$("#dealingData").hide();
					},
					dataType: 'json'
				});
		  },
		  getVeriCode: function(flag){
			  var phone = "";
			  if(flag ==='new'){
				 phone = this.param.newPhone; 
				 this.time.newTime = 120;
			  }else{
				  phone = this.param.oldPhone; 
				  this.time.oldTime = 120; 
			  }
			  var pattern = /^1[3-9]\d{9}$/;
			  if(!pattern.exec(phone)){
				  alertMsg('错误提示','电话号码格式不正确！')
				  return;
			  }
			  veriCodeTime(flag);
			  $.ajax({
					url: '/vip/vericode/phone/apply',
					method:'post',
					data: {'phone':phone},
					success: function(jsonRet,status,xhr){
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode !== 0){
								alertMsg('错误提示',jsonRet.errmsg)
								if(flag ==='new' && containerVue.time.newTimer){
									clearTimeout(containerVue.time.newTimer);
									containerVue.time.newTime = 120;
								}else if(containerVue.time.oldTimer){
									clearTimeout(containerVue.time.oldTimer);
									containerVue.time.oldTime = 120;
								}
							}
						}else{
							alertMsg('错误提示','获取验证码失败！')
							if(flag ==='new' && containerVue.time.newTimer){
								clearTimeout(containerVue.time.newTimer);
								containerVue.time.newTime = 120;
							}else if(containerVue.time.oldTimer){
								clearTimeout(containerVue.time.oldTimer);
								containerVue.time.oldTime = 120;
							}
						}
					},
					dataType: 'json'
				});
		  }
	  }
  });
  function veriCodeTime(flag){
	if(flag == 'new'){
		  if(containerVue.time.newTime == 0 && containerVue.time.newTimer){
			  clearTimeout(containerVue.time.newTimer);
		  }else{
			  containerVue.time.newTimer = setTimeout("veriCodeTime('new')",1000);
		  }
		  containerVue.time.newTime = containerVue.time.newTime - 1;
	}else{
		if(containerVue.time.oldTime == 0 && containerVue.time.oldTimer){
			  clearTimeout(containerVue.time.oldTimer);
		  }else{
			  containerVue.time.oldTimer = setTimeout("veriCodeTime('old')",1000);
		  }
		  containerVue.time.oldTime = containerVue.time.oldTime - 1;
	}
  }
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
