<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container" id="container" style="padding:0px;oveflow:scroll">
  <h3 style="margin-top:20px;text-align:center">欢迎注册</h3>
  <div class="row" style="margin:10px 0">
  	  <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">注册媒介<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <select v-model="param.media">
	        <option value=""> 请选择...</option>
	        <option value="1"> 手机</option>
	        <option value="2"> 邮箱</option>
	      </select>
	    </div>
	  </div>
	  <div v-if="param.media ==='2'" class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">邮箱<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <input type="email" class="form-control" v-model="param.email"  maxLength=100 required placeholder="邮箱">
	    </div>
	  </div>	
	  <div v-if="param.media ==='2'" class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">邮箱验证码<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
		    <div class="col-xs-7" style="padding:0">
		      <input class="form-control" v-model="param.veriCode" pattern="\d{6}"  maxLength=6 required placeholder="邮箱验证码">
		    </div>
		    <div class="col-xs-2">
		      <button v-if="time.newTime==120 || time.newTime <= 0 " type="button" class="btn btn-primary" @click="getVeriCode('email')">获取验证码</button>
		      <button v-if="time.newTime<120 && time.newTime > 0 " type="button" class="btn btn-default" > {{time.newTime}} s </button>
		    </div>
	    </div>	    
	  </div>
	  <div v-if="param.media !=='2'" class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">手机号<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <input type="tel" class="form-control" v-model="param.phone" pattern="\d{11}" maxLength=11 required placeholder="手机号">
	    </div>
	  </div>	
	  <div v-if="param.media !=='2'" class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">手机验证码<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
		    <div class="col-xs-7" style="padding:0">
		      <input class="form-control" v-model="param.veriCode" pattern="\d{6}"  maxLength=6 required placeholder="手机号验证码">
		    </div>
		    <div class="col-xs-2">
		      <button v-if="time.newTime==120 || time.newTime <= 0 " type="button" class="btn btn-primary" @click="getVeriCode('phone')">获取验证码</button>
		      <button v-if="time.newTime<120 && time.newTime > 0 " type="button" class="btn btn-default" > {{time.newTime}} s </button>
		    </div>
	    </div>
	  </div>
      <div class="row" style="margin:3px 0">
	    <label class="col-xs-4 control-label" style="padding-right:0">昵称<span style="color:red">*</span></label>
	    <div class="col-xs-8" style="padding-left:0">
	      <input type="text" class="form-control" v-model="param.nickname" pattern="\w{3,20}" title="3-20个字符组成" maxLength=20 required placeholder="请输入昵称（3-20个字符）">
	    </div>
	  </div> 
      <div class="row" style="margin:3px 0">
        <label class="col-xs-4 control-label" style="padding-right:0">新密码<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:0">
          <input type="password" class="form-control" v-model="param.passwd" pattern="\w{6,20}" maxLength=20 required placeholder="请输入新密码（6-20个字符）">
        </div>
      </div>
      <div class="row" style="margin:3px 0">
        <label class="col-xs-4 control-label" style="padding-right:0">确认密码<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:0">
          <input type="password" class="form-control" v-model="param.reNewPwd" pattern="\w{6,20}" maxLength=20 required placeholder="请输入确认密码（6-20个字符）">
        </div>
      </div>
      <div class="row" style="margin:3px 0">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
         </div>
      </div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			media:'1',
			nickname:'',
			phone:'',
			email:'',
			veriCode:'',
			passwd:'',
			reNewPwd:'',
			sex:'0'
		},
	  	time:{
  			newTimer: null,
  			newTime: 120
  		}
	},
	methods:{
		submit: function(){
			$("#dealingData").show();
			//密码强度正则，6-20位，包括至少1个大写字母，1个小写字母，1个数字
			var pPattern = /^.*(?=.{6,20})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/;
			if(!pPattern.exec(this.param.passwd)){
				alertMsg('错误提示','新密码格式不正确(6-20位，包括至少1个大写字母，1个小写字母，1个数字)！');
				return;
			}
			if(this.param.passwd != this.param.reNewPwd){
				alertMsg('错误提示','新密码与确认密码格式不一致！');
				return;
			}
			if(this.param.media === '1'){
				this.param.email = '';
			}else{
				this.param.phone = '';
			}
			$.ajax({
				url: '/register/quick',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							if('ok' == jsonRet.errmsg){
								window.location.href="/login";
							}else{
								alertMsg('系统提示',jsonRet.errmsg);
							}
							//alertMsg('系统提示',"注册成功！您可到基本信息编辑中上传您钟爱的头像！！！")
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
		getVeriCode: function(media){
			  var phoneOrEmail = "";
			  var param = {};
			  if(media ==='phone'){
				  phoneOrEmail = this.param.phone; 
				  var pattern = /^1[3-9]\d{9}$/;
				  if(!pattern.exec(phoneOrEmail)){
					  alertMsg('错误提示','电话号码格式不正确！')
					  return;
				  }
				  param = {'phone':phoneOrEmail};
			  }else{
				  phoneOrEmail = this.param.email; 
				  var pattern = /^[A-Za-z0-9_\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
				  if(!pattern.exec(phoneOrEmail)){
					  alertMsg('错误提示','邮箱格式不正确！')
					  return;
				  }
				  param = {'email':phoneOrEmail};
			  }
			  this.time.newTime = 120;
			  veriCodeTime(media);
			  $.ajax({
					url: '/user/vericode/'+media+'/apply',
					method:'post',
					data: param,
					success: function(jsonRet,status,xhr){
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode !== 0){
								alertMsg('错误提示',jsonRet.errmsg)
								if(containerVue.time.newTimer){
									clearTimeout(containerVue.time.newTimer);
									containerVue.time.newTime = 120;
								}
							}
						}else{
							alertMsg('错误提示','获取验证码失败！')
							if(containerVue.time.newTimer){
								clearTimeout(containerVue.time.newTimer);
								containerVue.time.newTime = 120;
							}
						}
					},
					dataType: 'json'
				});
		  }
	}
});
function veriCodeTime(flag){
	  if(containerVue.time.newTime == 0 && containerVue.time.newTimer){
		  clearTimeout(containerVue.time.newTimer);
	  }else{
		  containerVue.time.newTimer = setTimeout("veriCodeTime('new')",1000);
	  }
	  containerVue.time.newTime = containerVue.time.newTime - 1;
  }
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <div style="position:absolute;left:0px;right:0px;bottom:60px;height:100px;text-align:center;background-color:#D0D0D0">
	<p>&nbsp;</p>
	<span style="display:inline-block; margin:0 10px;"></span>
	Copyright <font style="font-family:'微软雅黑';">©</font> 2017-2020 摩放优选 <a href="http://www.miitbeian.gov.cn/" target="_blank" rel="nofollow">滇ICP备18002601号-1</a> 
  </div>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>


