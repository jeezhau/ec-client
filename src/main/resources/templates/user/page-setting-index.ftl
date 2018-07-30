<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if ((userBasic.status)!'') == "1">
<div class="container" id="container" style="padding:0px 0px;oveflow:scroll">
 
<ul class="list-group">
    <li class="list-group-item">
      <span >手机设置
        <#if (userBasic.phone)?? && (userBasic.phone)?length gt 10><span style="font-size:70%">(${userBasic.phone})</span>
        <#else>
          <span style="font-size:70%">(未绑定)</span>
        </#if></span>
      <span class="pull-right" >
       <#if (userBasic.phone)?? && (userBasic.phone)?length gt 10>
       <a href="/user/phone/manage">修改</a>
       <#else>
       <a href="/user/phone/manage">绑定</a>
       </#if>
       </span>
    </li>
    <li class="list-group-item">
      <span>邮箱设置
        <#if (userBasic.email)?? && (userBasic.email)?length gt 3><span style="font-size:70%">(${userBasic.email})</span>
        <#else>
          <span style="font-size:70%">(未绑定)</span>
        </#if></span>
      <span class="pull-right" >
       <#if (userBasic.email)?? && (userBasic.email)?length gt 3>
       <a href="/user/email/manage">修改</a>
       <#else>
       <a href="/user/email/manage">绑定</a>
       </#if>
       </span>
    </li>
    <li class="list-group-item">
      <span>登录密码</span>
      <span class="pull-right" >
       <a href="/user/passwd/manage">设置</a>
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
