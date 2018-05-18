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
<#if ((vipBasic.status)!'') == "1">
<div class="container" id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row" style="width:100%;margin:0px ;" >
	 <#if !(vipBasic.phone)??>
	 <div class="col-xs-12" style="margin-top:5px;background-color:white;">
	  <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;手机号请确保是您在使用，因如果您忘记密码时将使用该手机号码进行找回，同时在账户提现时可能需要用到收取验证码！！！</p>
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-3 control-label">手机号<span style="color:red">*</span></label>
	    <div class="col-xs-9">
	      <input type="tel" class="form-control" v-model="param.phone" pattern="\d{11}" maxLength=11 required placeholder="请输入手机号">
	    </div>
	  </div>	
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-3 control-label">验证码<span style="color:red">*</span></label>
	    <div class="col-xs-9">
		    <div class="col-xs-6" style="padding:0">
		      <input type="tel" class="form-control" v-model="param.vericode" pattern="\d{6}"  maxLength=6 required placeholder="请输入验证码">
		    </div>
		    <div class="col-xs-2">
		      <button v-if="time.time==120 || time.time <= 0 " type="button" class="btn btn-primary" @click="getVeriCode">获取验证码</button>
		      <button v-if="time.time<120 && time.time > 0 " type="button" class="btn btn-default" > {{time.time}} s </button>
		    </div>
	    </div>	    
	  </div> 
	  <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submitPhone">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
         </div>
      </div>	
	 </div>
	 </#if>
	 
	 <#if (vipBasic.phone)??>
	 <div  class="col-xs-12" style="margin-top:5px;background-color:white;">
	  <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;手机号请确保是您在使用，因如果您忘记密码时将使用该手机号码进行找回，同时在账户提现时可能需要用到收取验证码！！！</p>
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-3 control-label">手机号<span style="color:red">*</span></label>
	    <div class="col-xs-9">
	      <input type="tel" class="form-control" v-model="param.phone" pattern="\d{11}" maxLength=11 required placeholder="请输入手机号">
	    </div>
	  </div>	
	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-3 control-label">验证码<span style="color:red">*</span></label>
	    <div class="col-xs-9">
		    <div class="col-xs-6" style="padding:0">
		      <input type="tel" class="form-control" v-model="param.vericode" pattern="\d{6}"  maxLength=6 required placeholder="请输入验证码">
		    </div>
		    <div class="col-xs-2">
		      <button type="button" class="btn btn-primary" >获取验证码</button>
		    </div>
	    </div>	    
	  </div> 
	  <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submitPhone">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
         </div>
      </div>	
	 </div>
	 </#if>	  
  </div>
  <script type="text/javascript">
  var containerVue = new Vue({
	  el:'#container',
	  data:{
		  param:{
			  phone:'',
			  vericode:''
		  },
  		  time:{
  			timer: null,
  			time: 120
  		  }
	  },
	  methods:{
		  submitPhone: function(){
			  
		  },
		  getVeriCode: function(){
			  this.time.time = 120;
			  veriCodeTime();
			  $.ajax({
					url: '/vip/phone/vericode/get',
					method:'post',
					data: {},
					success: function(jsonRet,status,xhr){
						if(jsonRet && jsonRet.datas){
							for(var i=0;i<jsonRet.datas.length;i++){
								var item = jsonRet.datas[i];
								if('50,51,52,53,54,55,60,61,62,63,64'.indexOf(item.status)>=0){
									containerVue.counts['SA'] = item.cnt;
								}else{
									containerVue.counts[item.status] = item.cnt;
								}
							}
						}else{
							alertMsg('错误提示','获取城市数据(地级市)失败！')
						}
					},
					dataType: 'json'
				});
		  }
	  }
  });
  function veriCodeTime(){
	  if(containerVue.time.time == 0 && containerVue.time.timer){
		  clearTimeout(containerVue.time.timer);
	  }else{
		  containerVue.time.timer = setTimeout("veriCodeTime()",1000);
	  }
	  containerVue.time.time = containerVue.time.time - 1;
  }
  </script>
</div>
</#if>  

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
