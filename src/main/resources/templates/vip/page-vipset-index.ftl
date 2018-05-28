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
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if ((vipBasic.status)!'') == "1">
<div class="container" id="container" style="padding:0px 0px;oveflow:scroll">
 
<ul class="list-group">
    <li class="list-group-item">
      <span >手机设置
        <#if (vipBasic.phone)?? && (vipBasic.phone)?length gt 10><span style="font-size:70%">(${vipBasic.phone})</span>
        <#else>
          <span style="font-size:70%">(未绑定)</span>
        </#if></span>
      <span class="pull-right" >
       <#if (vipBasic.phone)?? && (vipBasic.phone)?length gt 10>
       <a href="/vip/phone/mgr">修改</a>
       <#else>
       <a href="/vip/phone/mgr">绑定</a>
       </#if>
       </span>
    </li>
    <li class="list-group-item">
      <span>邮箱设置
        <#if (vipBasic.email)?? && (vipBasic.email)?length gt 3><span style="font-size:70%">(${vipBasic.email})</span>
        <#else>
          <span style="font-size:70%">(未绑定)</span>
        </#if></span>
      <span class="pull-right" >
       <#if (vipBasic.email)?? && (vipBasic.email)?length gt 3>
       <a href="/vip/email/mgr">修改</a>
       <#else>
       <a href="/vip/email/mgr">绑定</a>
       </#if>
       </span>
    </li>
    <li class="list-group-item">
      <span>会员密码</span>
      <span class="pull-right" >
       <a href="/vip/passwd/mgr">设置</a>
       </span>
    </li>    
    <li class="list-group-item">
      <span>提现设置</span>
      <span class="pull-right" >
       <a href="/vip/account/mgr">设置</a>
      </span>
    </li>
</ul>

</div><!-- end of container -->
</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
