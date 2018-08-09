<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container " id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <a class="col-xs-2" href="/user/index/basic" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >我的分享</h3>
  </div>
  <div class="row" style="width:100%;margin:5px 0px 1px 0px;padding:0px 8px;background-color:white">
	  <div class="row" style="padding-top:5px;font-size:18px">
	    <label class="col-xs-6 control-label" style="padding-right:1px;text-align:center">已推荐用户数：</label>
	    <label class="col-xs-6 control-label" style="padding-left:1px">${count!0}</label>
	  </div>  	  

      <div class="row" style="margin:0">
        <div class="thumbnail">
	      <img id="qrcodeshow" alt="推广二维码" src="${showurl!''}" style="width:300px;height:300px">
	      <div class="caption" style="text-align:center">
	        <span>长按可分享二维码给朋友</span>
		    <p style="text-align:center">我的微信分享二维码(有效期28天后自动重新生成新的)</p>
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
   <h4>关于推荐的说明</h4>
   <div>
   	<h5>1、参与人员:</h5>
   	&nbsp;&nbsp;&nbsp;&nbsp;任何人均可参与<br>
   	<h5>2、参与方法：</h5>
   	&nbsp;&nbsp;&nbsp;&nbsp;仅需将摩放优选介绍个其他人，然后让TA扫一扫您的推广二维码后关注摩放优选公众号即可；一定要是您的二维码，每个人的分享二维码不同！！！<br>
    <h5>3、奖励措施：</h5>
    &nbsp;&nbsp;&nbsp;&nbsp;1）您每分享一个用户即可获得${(sysparam_spread_per_user_score)!'20'}个积分，现金消费1元获得${(sysparam_trade_score_one_yuan)!'1'}个积分，当积分数量达到${(sysparam_vip_activate_need_score)!'500'}时，系统自动激活会员账户；<br>
    &nbsp;&nbsp;&nbsp;&nbsp;2）您所分享的用户一旦有了现金消费交易，您将会获得特定比例的奖励资金，奖励资金进入会员可用余额；<br>
    &nbsp;&nbsp;&nbsp;&nbsp;3）会员可用余额可用于消费也可提现，积分可用于指定商品兑换；<br>
   </div>
  </div>
</div><!-- end of container -->
<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>
