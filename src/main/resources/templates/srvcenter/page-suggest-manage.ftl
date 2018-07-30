<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<header>
  <div class="weui-navbar" style="position:fixed;left:0px;top:0px">
    <div class="weui-navbar__item <#if mode = 'complain'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#complainMgr').show();$('#complainMgr').siblings().hide()">
	投诉
	</div>
	<div class="weui-navbar__item <#if mode = 'suggest'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#suggestMgr').show();$('#suggestMgr').siblings().hide()">
    建议
	</div>
	<div class="weui-navbar__item <#if mode = 'about'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#aboutMgr').show();$('#aboutMgr').siblings().hide()">
    About
	</div>
	<div class="weui-navbar__item <#if mode = 'joinus'> weui-bar__item_on </#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#joinUsMgr').show();$('#joinUsMgr').siblings().hide()">
    Join US
	</div>
  </div>
  <div style="height:50px;background-color:#E0E0E0 ;margin-bottom:0px;"></div>
</header>
<div class="container" style="overflow:scroll;margin-bottom:50px">

  <div class="row" id="complainMgr" style="display:<#if mode = 'complain'>block<#else>none</#if>">
	<div class="row"><!-- 投诉内容 -->
	 <form class="form-horizontal" role="form" id="complainForm">
	   <h3 style="text-align:center;padding:3px 0 0 0;">请正确输入以下信息提交开始投诉</h3>
	   <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">订单ID<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%"  maxlength="50" required placeholder="请输入您要投诉的商品订单ID">
         </div>
       </div> 
       <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">回访电话<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" maxlength="20" required placeholder="请输入您当前使用的电话号码">
         </div>
       </div> 
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">投诉内容<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="600" rows=8 required placeholder="请输入您的投诉内容，最长600个字符。您的投诉反馈我们将尽快处理，请耐心等待。如果您需要获得即时处理，请填写了该表单提交后拨打投诉热线，热线号码见页面底部。"></textarea>
         </div>
        </div>
        <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;text-align:center">
         <button class="btn btn-primary" style="margin:0 25px">提 交</button>
         <button class="btn btn-warning" style="margin:0 25px">重 置</button>
        </div>
	   </form>
	 </div>
     <div class="row" style="margin:5px 0px 3px 0px;">
        <div class="row" style="margin:1px 0px;background-color:white;">
          <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">我的投诉(10)</span>
          <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray"><a href="/srvcenter/complain/getall">查看全部&gt;</a></span>
        </div>
	    <div class="row" style="margin:1px 0px;padding:0 20px;background-color:white;"><!-- 最近一条投诉记录 -->
	     <div class="row">
	       <span class="pull-left">订单ID：1234567890</span>
	       <span class="pull-right">2018-4-15</span>
	     </div>
	     <div class="row">
	       <p>投诉内容：非常好，味道不错，水分充足，以后一定经常关注。</p>
	     </div>
	     <div class="row" style="color:gray">
	       <span>处理时间：2018-04-15</span><br>
	       <p>处理结果：3斤大果</p>
	     </div>
	   </div>
	</div>
	<div class="row" style="margin:5px 3px 3px 3px;background-color:white">
	  <h5>投诉热线</h5>
	  <span style="color: red">特别提示：<br>&nbsp;&nbsp;&nbsp;&nbsp;请先填写提交了上面的投诉表单之后再拨打该热线，否则将不会被受理！！！</span><br>
	  <span>热线号码：18183813085</span>
	</div>
  </div>
  <div class="row" id="suggestMgr" style="display:<#if mode = 'suggest'>block<#else>none</#if>">  <!-- 建议 -->
	<div class="row">
	 <form class="form-horizontal" role="form" id="suggestForm">
	   <h3 style="text-align:center;padding:3px 0 0 0;">请正确输入以下信息提交建议</h3>
	   <div class="row" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">建议标题<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <input type="text" class="form-control" style="width:66%" maxlength="20" required placeholder="请输入建议标题，最长20个字符">
         </div>
       </div> 
       <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;">
         <label class="col-xs-3 control-label">建议内容<span style="color:red">*</span>:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" maxlength="600" rows=8 required placeholder="请输入您的建议内容，最长600个字符；一旦您的宝贵建议被采纳将会获得不等的可消费积分！"></textarea>
         </div>
        </div>
        <div class="row form-group" style="margin:3px 5px 3px 15px;padding:3px 3px;text-align:center">
         <button class="btn btn-primary" style="margin:0 25px">提 交</button>
         <button class="btn btn-warning" style="margin:0 25px">重 置</button>
        </div>
	   </form>
	 </div>
     <div class="row" style="margin:5px 0px 3px 0px;">
        <div class="row" style="margin:1px 0px;background-color:white;">
          <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">我的建议(10)</span>
          <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray"><a href="/srvcenter/suggest/getall">查看全部&gt;</a></span>
        </div>
	    <div class="row" style="margin:1px 0px;padding:0 20px;background-color:white;"><!-- 最近一条投诉记录 -->
	     <div class="row">
	       <span class="pull-left">建议标题：1234567890</span>
	       <span class="pull-right">2018-4-15</span>
	     </div>
	     <div class="row">
	       <p>建议内容：非常好，味道不错，水分充足，以后一定经常关注。<p>
	     </div>
	     <div class="row" style="color:gray">
	       <span>处理时间：2018-04-15</span><br>
	       <p>处理结果：3斤大果</p>
	       <p>奖励积分：0 </p>
	     </div>
	   </div>
	</div>  
  </div> 
  <div class="row" id="aboutMgr" style="display:<#if mode = 'about'>block<#else>none</#if>">
    <div class="row" style="margin:0 3px;padding:0 15px">
      <br><h3 style="text-align:center">关于摩放优选</h3><br>
      <p>&nbsp;&nbsp;&nbsp;&nbsp;健康与快乐是当下人们的生活追求！我们（摩放优选）即为此而生，您的健康、快乐就是我们的极致追求！衷心的希望我们能一直陪伴着您的优质生活。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;目前，我们以美丽的云南为起点，为您到各类批发市场与原产地挑选各类优质的产品，同时也邀请优质商家一起合作，初步确保材质的安全与优秀。
   		优选回来的产品再次经过我们的精心挑选，去除残次品并做初加工。这就是我们现今的工作。<br>
   		&nbsp;&nbsp;&nbsp;&nbsp;同时，为了完美的优质，我们也有一个终极目标，那就是在有一天能够拥有我们自己的农庄（生产基地）与加工基地。
   		愿在您的支持与鼓励下我们的目标能早日实现，以能更好的为您服务。
      </p>
    </div>
  </div>
  <div class="row" id="joinUsMgr" style="display:<#if mode = 'joinus'>block<#else>none</#if>">
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
      	&nbsp;&nbsp;&nbsp;&nbsp;1、个人中心/我-基本/我要推广 <br>
      	&nbsp;&nbsp;&nbsp;&nbsp;2、个人中心/我-会员/合作伙伴</p>
      <p style="text-align: right;padding:1px 3px">加入我们：18601672645</p>
    </div>
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