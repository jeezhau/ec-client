<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common4-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="position:relative">
<div class="container" style="oveflow:scroll">
  <div class="row" style="justify-content:center">
     <h3 style="margin:10px 0;text-align:center;justify-content:center" >
     <#if (myPartner.partnerId)??>
     <span>${myPartner.busiName}(ID:${(myPartner.partnerId)?string('#')})</span>
     <#else>
     <span>我是合作伙伴</span>
     </#if>
     </h3>
  </div>
  <div class="row" style="margin:30px 0px;text-align:center;justify-content:center;vertical-align:center">
    <#if (partnerUserTP=='bindVip') || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('basic') )>
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
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
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pimage/manage" >
		    <img src="/icons/图库管理.png" alt="" width="90px" height="90px">
		    <p>图库管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('postage')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/postage/manage" >
		    <img src="/icons/运费模板管理.png" alt="" width="90px" height="90px">
		    <p>运费模版管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('goods')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/goods/manage" >
		    <img src="/icons/商品管理.png" alt=""  width="90px" height="90px">
		    <p >商品管理</p>
		</a>
	  </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('saleorder')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/psaleorder/show/all" >
		    <img src="/icons/销售订单.png" alt="" width="90px" height="90px">
		    <p >销售订单</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('aftersale')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/paftersale/manage/refunding" >
		    <img src="/icons/售后服务.png" alt="" width="90px" height="90px">
		    <p >售后服务</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('complain4p')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pcomplain/partner/manage" >
		    <img src="/icons/投诉.png" alt="" width="90px" height="90px">
		    <p >向上投诉</p>
		</a>
       </div>
    </div>
    </#if>
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('preceiver')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
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
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/mypartners/manage" >
		    <img src="/icons/下级管理.png" alt="" width="90px" height="90px">
		    <p >我的推广</p>
		</a>
       </div>
    </div>
    </#if>     
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('review')) >
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
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
     <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pcomplain/sys/manage" >
		    <img src="/icons/投诉.png" alt="" width="90px" height="90px">
		    <p >投诉处理</p>
		</a>
       </div>
     </div>   
     </#if> 
     <#if  partnerUserTP=='bindVip' || partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('CashapplyDeal')>
     <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
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
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pstaff/manage" >
		    <img src="/icons/员工管理.png" alt="" width="90px" height="90px">
		    <p >员工管理</p>
		</a>
       </div>
    </div>    
  </#if>
    <div class="col-4 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px;">
       <div style="background-color:white">
	    <a href="/pstaff/kfshow/${SYS_PARTNERID?string('#')}?tagId=kf4partner" >
		    <img src="/icons/客服.png" alt="" width="90px" height="90px">
		    <p >官方客服</p>
		</a>
       </div>
    </div>  
  </div>
  <div class="row" style="margin:3px 0;width:100%;text-align:center;justify-content:center;">
      <div style="width:100%;text-align:center;justify-content:center;padding:8px 2px;background-color:white">
        <p>诚信用心有爱！</p>
        <p>优选品质，健康你我ta，福乐一家！</p>
      </div> 
  </div>
  <div class="row" style="margin:3px 0;width:100%;text-align:center;justify-content:center;">
      <div style="width:100%;text-align:center;justify-content:center;padding:8px 2px;background-color:white">
        <p>欢迎您加入我们！</p>
        <p>这里将是你我的未来！</p>
      </div> 
  </div>  
  
  <div class="row" style="margin:30px 0;width:100%;text-align:center;justify-content:center;">
     <a class="btn btn-danger" style="padding:8px 20px;width:80%" href="/partner/logout">退出登录</a>
  </div>
</div><!-- end of container -->

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <div  style="position:absolute;left:0px;right:0px;bottom:60px;height:100px;text-align:center;justify-content:center;background-color:#D0D0D0">
	<p><span style="display:inline-block; margin:0 10px;"></span></p>
	<p>Copyright <font style="font-family:'微软雅黑';">©</font> 2017-2020 昆明摩放优选科技服务有限责任公司 <a href="http://www.miitbeian.gov.cn/" target="_blank" rel="nofollow">滇ICP备18002601号-1</a></p> 
  </div>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>
