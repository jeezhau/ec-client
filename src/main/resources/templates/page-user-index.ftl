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
</head>
<body class="light-gray-bg">

<header>
  <div class="weui-navbar">
    <div class="weui-navbar__item weui-bar__item_on" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#userBasic').show();$('#userVip').hide();">
	我-基本
	</div>
	<div class="weui-navbar__item" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#userBasic').hide();$('#userVip').show();">
    我-会员
	</div>
  </div>
</header>
<div class="container" style="padding:1px 30px">
    <div style="height:50px;background-color:#E0E0E0 ;margin-bottom:0px;"></div>
    <!-- 基本信息管理-->
    <div class="row" id="userBasic" > 
      <div class="row" style="padding:10px 10px;background-color:#880000">
        <div class="row" style="margin:10px 25%;vertical-algin:center;text-align:center">
          <img alt="" src="/images/mfyx_logo.jpeg" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" > 
        </div>
        <div class="row" style="width:60%;margin:10px 20%;color:gray">
          <span class="pull-left"><img alt="" src="/icons/性别-男.png" width="20px" height="20px"> jeekhan </span >
          <span class="pull-right"><img alt="" src="/icons/电话.png" width="20px" height="20px"> 15287953085 </span>
        </div>
      </div>
      <div class="row" style="margin:30px 1px 15px 1px;">
       <ul class="nav nav-pills nav-stacked" style="max-width: 350px;">
         <li class="active" style="background-color:white">
           <a href="#">
             <span class="badge pull-right" style="background-color:rgb(239,239,239);border:none">查询所有  &gt;</span>
             <img alt="" src="/icons/订单.png" width="20px" height="20px"> 我的订单
           </a>
         </li>
         <li style="background-color:white">
           <a href="#">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)">修改资料  &gt;</span>
             <img alt="" src="/icons/个人信息.png" width="20px" height="20px"> 个人资料
           </a>
         </li>
         <li style="background-color:white">
           <a href="#">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)">编辑  &gt;</span>
             <img alt="" src="/icons/地址.png" width="20px" height="20px"> 收货地址
           </a>
         </li>
         <li style="background-color:white">
           <a href="#">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)"> 联系我们  &gt;</span>
             <img alt="" src="/icons/客服.png" width="20px" height="20px"> 官方客服
           </a>
         </li> 
         <li style="background-color:white">
           <a href="#">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)"> 我的二维码  &gt;</span>
             <img alt="" src="/icons/服务号-营销推广.png" width="20px" height="20px"> 我要推广
           </a>
         </li>        
       </ul>
      </div>
      
    </div>
    <!-- 会员信息管理-->
    <div class="row" id="userVip" style="display:none"> 
      <div class="panel panel-info panel-body">
		会员信息
      </div>
    </div>    
</div>
<footer>
  <#include "/page-bottom-menu.ftl" encoding="utf8"> 
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