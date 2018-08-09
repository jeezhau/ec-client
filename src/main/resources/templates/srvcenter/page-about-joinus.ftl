<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<header>
  <div class="weui-navbar" style="position:fixed;left:0px;top:0px">
	<div class="weui-navbar__item weui-bar__item_on " onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#aboutMgr').show();$('#aboutMgr').siblings().hide()">
    About
	</div>
	<div class="weui-navbar__item " onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#joinUsMgr').show();$('#joinUsMgr').siblings().hide()">
    Join US
	</div>
  </div>
  <div style="height:50px;background-color:#E0E0E0 ;margin-bottom:0px;"></div>
</header>
<div class="container" style="overflow:scroll;margin-bottom:50px">
  <div class="row" id="aboutMgr" style="">
    <div class="row" style="margin:0 3px;padding:0 15px">
      <br><h3 style="text-align:center">关于摩放优选</h3><br>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;健康与快乐是当下人们的生活追求！我们（昆明摩放优选科技服务有限责任公司，简称摩放优选）即为此而生，您的健康、快乐就是我们的极致追求！衷心的希望我们能一直陪伴着您的优质生活。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;目前，我们以美丽的云南为起点，邀请优质特色商家一起合作，以为您提供一切的美好。从哪里来，终还是要归于何处。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;深入基底，从产品的源地发展，一起前行，为产家与消费者直连，你需我有，没有中间套路，一切只为共赢。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;愿在您的支持与鼓励下，更好的为您服务。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;我们：摩顶放踵，利天下为之！<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;为您：优选品质，健康、快乐又美好！<br>
      </p>
    </div>
  </div>
  <div class="row" id="joinUsMgr" style="display:none">
    <div class="row" style="margin:0 3px;padding:0 15px">
      <br><h3 style="text-align:center">有您才会更美好</h3><br>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;世界无限美好，而我们却如此渺小！</p>
   	  <p>&nbsp;&nbsp;&nbsp;&nbsp;有您，有TA，有我，在一起；就是整个世界！！</p>
   	  <p>&nbsp;&nbsp;&nbsp;&nbsp;有您，有TA，有我，在一起；就是整个世界！！</p>
   	  <p>&nbsp;&nbsp;&nbsp;&nbsp;有您，有TA，有我，在一起；就是整个世界！！</p>
   	  
   	  <p>&nbsp;&nbsp;&nbsp;&nbsp;一切的美好在等待，我正在，你，在哪里？</p>
    </div>
    <div style="position:fixed;left:0;bottom:60px;width:100%">
      <p style="text-algin:left;padding:3px 3px;font-size:80%;color:gray">两种方式相见：<br>
      	&nbsp;&nbsp;&nbsp;&nbsp;1、个人中心/我-基本/推荐分享 <br>
      	&nbsp;&nbsp;&nbsp;&nbsp;2、个人中心/我-会员/合作伙伴</p>
      <!-- <p style="text-align: right;padding:1px 3px">加入我们：18601672645</p> -->
    </div>
  </div>    
</div>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>
