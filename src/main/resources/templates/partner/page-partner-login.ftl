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
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <link href="/css/weui.css" rel="stylesheet">
</head>
<body class="light-gray-bg">
<div class="templatemo-content-widget templatemo-login-widget white-bg" id="container">
  <header class="text-center">
      <div class="square"></div>
      <h1>欢迎</h1>
  </header>
  <form class="form-horizontal" action="/partner/login" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
    <div class="form-group">
      <label class="col-xs-4 control-label" style="padding-right:3px">用户类型</label>	 
    	  <div class="input-group" style="padding-left:3px">
        	<select class="form-control" name="userTp" v-model="params.userTp">
        	  <option value="" disabled>请选择用户类型</option>
        	  <option value="bindVip">绑定VIP</option>
        	  <option value="staff">员工</option>
        	</select>   		
      </div>	
    	</div>
    <div v-if="params.userTp == 'staff'" class="form-group">
      <label class="col-xs-4 control-label" style="padding-right:3px">合作伙伴ID</label>
    	  <div class="input-group" style="padding-left:3px">
        <input class="form-control" type="text" name="partnerId" value="${(partnerId!0)?string('#')}" v-bind:required="params.userTp == 'staff'" placeholder="合作伙伴ID">           
      </div>	
    	</div>
    	<div class="form-group">
    	  <label class="col-xs-4 control-label" style="padding-right:3px">用户ID</label>
    	  <div class="input-group" style="padding-left:3px">
           <input class="form-control" type="text" name="userId" value= "${(userId!0)?string('#')}" required placeholder="员工ID或绑定会员ID">           
      </div>	
    	</div>
    	<div class="form-group">
    	  <label class="col-xs-4 control-label" style="padding-right:3px">密码<i class="fa fa-key fa-fw"></i></label>
    	  <div class="input-group" style="padding-left:3px">
           <input class="form-control" type="password" name="passwd"  title="员工输入员工操作密码，绑定会员输入会员密码" required placeholder="******">           
      </div>	
    	</div>
	<div class="form-group">
	   <div class="col-xs-12" style="text-align:center">
		<button type="submit" class="btn btn-primary" style="width:80%;padding:8px 15px"> 登 录 </button>
	   </div>
	</div>
  </form>
</div>
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		params:{
			userTp:'{userTp!""}'
		}
	}
});
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>
<footer>
  <div style="position:absolute;left:0px;right:0px;bottom:3px;height:60px;text-align:center;background-color:#D0D0D0">
	<span style="display:inline-block; margin:0 10px;">|</span>
	Copyright <font style="font-family:'微软雅黑';">©</font> 2017-2020 昆明摩放优选科技服务有限责任公司 <a href="http://www.miitbeian.gov.cn/" target="_blank" rel="nofollow">滇ICP备18002601号-1</a> 
  </div>
</footer>
</body>
</html>
