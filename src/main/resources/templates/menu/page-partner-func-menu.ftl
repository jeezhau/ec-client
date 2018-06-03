 <!-- 合作伙伴相关功能底部主菜单--> 
  <div class="row" style="height:80px"></div>
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px">
    <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('basic') )>
    	<a href="/partner/manage" class="weui-tabbar__item <#if sys_func=='partner-index'>weui-bar__item_on </#if>" >
	    <img src="/icons/合作伙伴-申请开通.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">首页</p>
	</a>
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('goods') )>
	<a href="/goods/manage" class="weui-tabbar__item <#if sys_func=='partner-goods'>weui-bar__item_on </#if>" >
	    <img src="/icons/商品管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">商品</p>
	</a>
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('postage') )>
	<a href="/postage/manage" class="weui-tabbar__item <#if sys_func=='partner-postage'>weui-bar__item_on </#if>" >
	    <img src="/icons/运费模板管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">运费</p>
	</a>
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('pimage') )>
	<a href="/pimage/manage" class="weui-tabbar__item <#if sys_func=='partner-image'>weui-bar__item_on </#if>" >
	    <img src="/icons/图库管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">图库</p>
	</a>
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('saleorder') )>
	<a href="/psaleorder/show/all" class="weui-tabbar__item <#if sys_func=='partner-saleorder'>weui-bar__item_on </#if>" >
	    <img src="/icons/销售订单.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">已售</p>
	</a>	
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('aftersale') )>
	<a href="/paftersale/manage/refunding" class="weui-tabbar__item <#if sys_func=='partner-aftersale'>weui-bar__item_on </#if>" >
	    <img src="/icons/售后服务.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">售后</p>
	</a>	
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('pstaff') )>
	<a href="/pstaff/manage" class="weui-tabbar__item <#if sys_func=='partner-pstaff'>weui-bar__item_on </#if>" >
	    <img src="/icons/员工管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">员工</p>
	</a>	
	</#if>	
  </div>
  