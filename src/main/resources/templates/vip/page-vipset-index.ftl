<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if ((vipBasic.status)!'') == "1">
<div class="container" id="container" style="padding:0px 0px;oveflow:scroll">
 
<ul class="list-group">
    <li class="list-group-item">
      <span>会员密码</span>
      <span class="pull-right" >
       <a href="/vip/passwd/mgr">设置</a>
       </span>
    </li>    
    <li class="list-group-item">
      <span>账户管理</span>
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
