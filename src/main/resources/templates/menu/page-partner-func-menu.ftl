 <!-- 合作伙伴相关功能底部主菜单--> 
  <div class="row" style="height:120px"></div>
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px;height:60px">
    	<a href="/partner/manage" class="weui-tabbar__item <#if (sys_func!'')=='partner-index'>weui-bar__item_on </#if>" >
	    <img src="/icons/合作伙伴-申请开通.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">首页</p>
	</a>
  <#if ((myPartner.pbTp)!'')=='1'>

	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('postage')) )>
	<a href="/postage/manage" class="weui-tabbar__item <#if (sys_func!'')=='partner-postage'>weui-bar__item_on </#if>" >
	    <img src="/icons/运费模板管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">运费</p>
	</a>
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('pimage')) )>
	<a href="/pimage/manage" class="weui-tabbar__item <#if (sys_func!'')=='partner-image'>weui-bar__item_on </#if>" >
	    <img src="/icons/图库管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">图库</p>
	</a>
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('goods')) )>
	<a href="/goods/manage" class="weui-tabbar__item <#if (sys_func!'')=='partner-goods'>weui-bar__item_on </#if>" >
	    <img src="/icons/商品管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">商品</p>
	</a>
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('saleorder')) )>
	<a href="/psaleorder/show/all" class="weui-tabbar__item <#if (sys_func!'')=='partner-saleorder'>weui-bar__item_on </#if>" >
	    <img src="/icons/销售订单.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">已售</p>
	</a>	
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('aftersale')) )>
	<a href="/paftersale/manage/refunding" class="weui-tabbar__item <#if (sys_func!'')=='partner-aftersale'>weui-bar__item_on </#if>" >
	    <img src="/icons/售后服务.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">售后</p>
	</a>	
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('aftersale')) )>
	<a href="/pcomplain/partner/manage" class="weui-tabbar__item <#if (sys_func!'')=='complain4p'>weui-bar__item_on </#if>" >
	    <img src="/icons/投诉.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">投诉</p>
	</a>	
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('preceiver')) )>
	<a href="/preceiver/manage" class="weui-tabbar__item <#if (sys_func!'')=='preceiver'>weui-bar__item_on </#if>" >
	    <img src="/icons/地址管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">地址</p>
	</a>	
	</#if>
   </#if>

  <#if ((myPartner.pbTp)!'')=='2'>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('mypartners')) )>
	<a href="/mypartners/manage" class="weui-tabbar__item <#if (sys_func!'')=='mypartners'>weui-bar__item_on </#if>" >
	    <img src="/icons/下级管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">推·商</p>
	</a>
	</#if>
	<#if (partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('review')) )>
	<a href="/review/manage/goods" class="weui-tabbar__item <#if (sys_func!'')=='review'>weui-bar__item_on </#if>" >
	    <img src="/icons/信息审核.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">审核</p>
	</a>
	</#if>
    <#if myPartner.partnerId == SYS_PARTNERID>
     <#if  partnerUserTP=='bindVip' || partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('Complain')>
     <a href="/pcomplain/sys/manage" class="weui-tabbar__item <#if (sys_func!'')=='complain'>weui-bar__item_on </#if>" >
	    <img src="/icons/投诉.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">处·诉</p>
	 </a>
	 </#if>
	 <#if  partnerUserTP=='bindVip' || partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('CashapplyDeal')>
     <a href="/pcash/manage" class="weui-tabbar__item <#if (sys_func!'')=='CashapplyDeal'>weui-bar__item_on </#if>" >
	    <img src="/icons/提现.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">提现</p>
	 </a>
	 </#if>	
	</#if>	
  </#if>  
	
  <#if (myPartner.pbTp)??>
	<a href="/pstaff/manage" class="weui-tabbar__item <#if (sys_func!'')=='partner-pstaff'>weui-bar__item_on </#if>" >
	    <img src="/icons/员工管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">员工</p>
	</a>	
  </#if>
	<a href="/pstaff/kfshow/${SYS_PARTNERID?string('#')}?tagId=kf4partner" class="weui-tabbar__item <#if (sys_func!'')=='partner-syskf'>weui-bar__item_on </#if>" >
	    <img src="/icons/客服.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">客服</p>
	</a>		
  </div>
  