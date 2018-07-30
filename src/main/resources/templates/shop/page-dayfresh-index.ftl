<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<div style="height:3px;background-color:#E0E0E0 ;margin-bottom:5px;"></div>
<header>
  <div class="weui-navbar">
    <div class="weui-navbar__item <#if mode = 'today'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');">
	今日热荐
	</div>
	<div class="weui-navbar__item <#if mode = 'tomorrow'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');">
    明日主推
	</div>
	<div class="weui-navbar__item <#if mode = 'history'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');">
    历史推出
	</div>
  </div>

</header>
<div class="container">
  <div class="row">

  </div>
</div>
<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<div class="modal fade " id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorTitle" aria-hidden="false" data-backdrop="static">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="errorTitle" style="color:red">错误提示</h4>
     		</div>
     		<div class="modal-body">
       			<p> ${errmsg} </p><p/>
     		</div>
     		<div class="modal-footer">
     			<div style="margin-left:50px">
        			</div>
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
$("#errorModal").modal('show');
</script>
</#if>

</body>
</html>