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
    
    <link href="/css/mfyx.css" rel="stylesheet">
</head>
<body class="light-gray-bg">
<div class="container " id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <a class="col-xs-2" href="/user/index/basic" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >我是推广员</h3>
  </div>
  <div class="row" style="width:100%;margin:5px 0px 1px 0px;padding:0px 8px;background-color:white">
	  <div class="row" style="padding-top:5px;font-size:18px">
	    <label class="col-xs-6 control-label" style="padding-right:1px;text-align:center">已推广用户数：</label>
	    <label class="col-xs-6 control-label" style="padding-left:1px">${count}</label>
	  </div>  	  

      <div class="row">
        <div class="thumbnail">
	      <img id="qrcodeshow" alt="推广二维码" src="${showurl}" width="80%" height="80%">
	      <div class="caption">
		    <p style="text-align:center">我的推广二维码(有效期28天后自动重新生成新的)</p>
	      </div>
	      <form action="/user/spread">
        		<input type="hidden" name="create" value="1">
        		<div style="text-align:center">
        			<button type="button" class="btn btn-primary">重新生成</button>
        		</div>
           </form>
        </div>
      </div>	
  </div>
  <div class="row" style="width:100%;margin:1px 0px 1px 0px;padding:5px 8px;background-color:white">
   <h4>关于推广员的说明</h4>
   <div>
   	<h5>1、参与人员:</h5>
   	&nbsp;&nbsp;&nbsp;&nbsp;任何人均可参与<br>
   	<h5>2、参与方法：</h5>
   	&nbsp;&nbsp;&nbsp;&nbsp;仅需将摩放优选介绍个其他人，然后让TA扫一扫您的推广二维码后关注摩放优选公众号即可；一定要是您的二维码，每个人的推广二维码不同！！！<br>
    <h5>3、奖励措施：</h5>
    &nbsp;&nbsp;&nbsp;&nbsp;1）您每推广一个用户即可获得10个积分，现金消费10元获得一个积分，当积分数量达到100时，系统自动激活会员账户；<br>
    &nbsp;&nbsp;&nbsp;&nbsp;2）您所推广的用户一旦有了现金消费交易，您将会获得特定比例的分润，目前为消费金额的千分之三，分润金额进入会员可用余额；<br>
    &nbsp;&nbsp;&nbsp;&nbsp;3）会员可用余额可用于消费也可提现到您的微信钱包，积分可用于指定商品兑换；<br>
   </div>
  </div>
</div><!-- end of container -->
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