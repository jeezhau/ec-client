 <!-- 合作伙伴相关功能底部主菜单--> 
  <div class="row" style="height:80px"></div>
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px">
    	<a href="/partner/index" class="weui-tabbar__item <#if sys_func=='partner-index'>weui-bar__item_on </#if>" >
	    <img src="/icons/合作伙伴-申请开通.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">管理首页</p>
	</a>
	<a href="/goods/manage" class="weui-tabbar__item <#if sys_func=='partner-goods'>weui-bar__item_on </#if>" >
	    <img src="/icons/商品管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">商品管理</p>
	</a>
	<a href="/postage/index" class="weui-tabbar__item <#if sys_func=='partner-postage'>weui-bar__item_on </#if>" >
	    <img src="/icons/运费模板管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">运费模版</p>
	</a>
	<a href="/image/index" class="weui-tabbar__item <#if sys_func=='partner-image'>weui-bar__item_on </#if>" >
	    <img src="/icons/图库管理.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">图库管理</p>
	</a>
	<a href="/order/partner/show/all" class="weui-tabbar__item <#if sys_func=='partner-order'>weui-bar__item_on </#if>" >
	    <img src="/icons/销售订单.png" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">销售订单</p>
	</a>	
  </div>