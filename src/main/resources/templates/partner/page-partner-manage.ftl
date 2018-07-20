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
    
</head>
<body class="light-gray-bg">
<div class="container" style="oveflow:scroll">
  <div class="row">
     <h3 style="margin:10px 0;text-align:center" >
     <#if (myPartner.partnerId)??>
     <span>${myPartner.busiName}(ID:${(myPartner.partnerId)?string('#')})</span>
     <#else>
     <span>我是合作伙伴</span>
     </#if>
     </h3>
  </div>
  <div class="row" style="margin:30px 0px;text-align:center;vertical-align:center">
    <#if (partnerUserTP=='bindVip') || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('basic') )>
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
      <div style="background-color:white">
	    <a href="/partner/edit" >
		    <img src="/icons/合作伙伴-申请开通.png" alt="" width="90px" height="90px">
		    <p >我的信息</p>
		</a>
      </div>
    </div>
    </#if>
    
  <#if ((myPartner.pbTp)!'')=='1'>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('pimage')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pimage/manage" >
		    <img src="/icons/图库管理.png" alt="" width="90px" height="90px">
		    <p>图库管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('postage')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/postage/manage" >
		    <img src="/icons/运费模板管理.png" alt="" width="90px" height="90px">
		    <p>运费模版管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('goods')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/goods/manage" >
		    <img src="/icons/商品管理.png" alt=""  width="90px" height="90px">
		    <p >商品管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('saleorder')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/psaleorder/show/all" >
		    <img src="/icons/销售订单.png" alt="" width="90px" height="90px">
		    <p >销售订单</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('aftersale')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/paftersale/manage/refunding" >
		    <img src="/icons/售后服务.png" alt="" width="90px" height="90px">
		    <p >售后服务</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('complain4p')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pcomplain/partner/manage" >
		    <img src="/icons/投诉.png" alt="" width="90px" height="90px">
		    <p >向上投诉</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('preceiver')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/preceiver/manage" >
		    <img src="/icons/地址管理.png" alt="" width="90px" height="90px">
		    <p >收货信息</p>
		</a>
       </div>
    </div>
    </#if>
  </#if>
    
  <#if ((myPartner.pbTp)!'')=='2' >
    <#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('mypartners'))) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/mypartners/manage" >
		    <img src="/icons/下级管理.png" alt="" width="90px" height="90px">
		    <p >下级管理</p>
		</a>
       </div>
    </div>
    </#if>     
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('review')) >
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/review/manage/goods" >
		    <img src="/icons/信息审核.png" alt="" width="90px" height="90px">
		    <p >信息审核</p>
		</a>
       </div>
    </div>
    </#if> 
    <#if myPartner.partnerId == SYS_PARTNERID>
     <#if  partnerUserTP=='bindVip' || partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('Complain')>
     <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pcomplain/sys/manage" >
		    <img src="/icons/投诉.png" alt="" width="90px" height="90px">
		    <p >投诉处理</p>
		</a>
       </div>
     </div>   
     </#if> 
     <#if  partnerUserTP=='bindVip' || partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('CashapplyDeal')>
     <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pcash/manage" >
		    <img src="/icons/提现.png" alt="" width="90px" height="90px">
		    <p >提现处理</p>
		</a>
       </div>
     </div>   
     </#if>     
    </#if> 
  </#if>
    
  <#if (myPartner.pbTp)??>     
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pstaff/manage" >
		    <img src="/icons/员工管理.png" alt="" width="90px" height="90px">
		    <p >员工管理</p>
		</a>
       </div>
    </div>    
  </#if>
    <div class="col-xs-4 col-sm-4 col-md-3 col-lg-3" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pstaff/kfshow/${SYS_PARTNERID?string('#')}?tagId=kf4partner" >
		    <img src="/icons/客服.png" alt="" width="90px" height="90px">
		    <p >官方客服</p>
		</a>
       </div>
    </div>  
  </div>
  <div class="row" style="margin-top:3px;vertical-align:center">
    <div class="col-xs-12">
      <div style=";text-align:center;padding:8px 2px;background-color:white">
        <p>诚信用心有爱！</p>
        <p>优选品质，健康你我ta，福乐一家！</p>
      </div> 
    </div>
  </div>
  <div class="row" style="margin-top:3px;vertical-align:center">
    <div class="col-xs-12">
      <div style=";text-align:center;padding:8px 2px;background-color:white">
        <p>欢迎您加入我们！</p>
        <p>这里将是你我的未来！</p>
      </div> 
    </div>
  </div>  
  
  <div class="row" style="margin:30px 0">
    <div class="col-xs-12" style="text-align:center">
      <a class="btn btn-danger" style="padding:8px 20px;width:80%" href="/partner/logout">退出登录</a>
    </div>
  </div>
</div><!-- end of container -->

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>
