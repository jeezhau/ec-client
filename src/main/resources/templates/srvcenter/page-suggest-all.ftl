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
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container " style="oveflow:scroll">
   <div class="row">
     <a class="col-xs-2" href="/srvcenter/index/suggest" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >我的建议</h3>
   </div>
   <div class="row" >
	   <div class="row" style="width:100%;margin:1px 0px;padding:0 20px;background-color:white;">
	    <div class="row">
	      <span class="pull-left">建议标题：1234567890</span>
	      <span class="pull-right">2018-4-15</span>
	    </div>
	    <div class="row">
	      <p>建议内容：非常好，味道不错，水分充足，以后一定经常关注。</p>
	    </div>
	    <div class="row" style="color:gray">
	      <span>处理时间：2018-04-15</span><br>
	      <p>处理结果：3斤大果</p>
	      <p>奖励积分：0 </p>
	    </div>
	  </div>
  </div>
  <div class="row" >
	   <div class="row" style="width:100%;margin:1px 0px;padding:0 20px;background-color:white;">
	    <div class="row">
	      <span class="pull-left">建议标题：1234567890</span>
	      <span class="pull-right">2018-4-15</span>
	    </div>
	    <div class="row">
	      <p>建议内容：非常好，味道不错，水分充足，以后一定经常关注。</p>
	    </div>
	    <div class="row" style="color:gray">
	      <span>处理时间：2018-04-15</span><br>
	      <p>处理结果：3斤大果</p>
	      <p>奖励积分：0 </p>
	    </div>
	  </div>
  </div>  
</div><!-- end of container -->

</body>
</html>