 <!-- 商城管理底部主菜单--> 
  <div class="weui-tabbar">
    <#if isDayFresh=='1'>
    	 <a href="/dayfresh/index/today" class="weui-tabbar__item <#if sys_func=='dayfresh'>weui-bar__item_on </#if>" >
	    <span style="display: inline-block;position: relative;">
	        <img src="/images/mfyx_logo.jpeg" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">每日鲜推</p>
      </a>
	<#else>
    	<a href="/shop/index" class="weui-tabbar__item <#if sys_func=='shop'>weui-bar__item_on </#if>" >
	    <span style="display: inline-block;position: relative;">
	        <img src="/images/mfyx_logo.jpeg" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">首页</p>
	</a>
	<a href="/nearby/index" class="weui-tabbar__item <#if sys_func=='nearby'>weui-bar__item_on </#if>" >
	    <img src="/images/mfyx_logo.jpeg" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">周边</p>
	</a>
    </#if>

	<a href="/srvcenter/index" class="weui-tabbar__item <#if sys_func=='srvcenter'>weui-bar__item_on </#if>" >
	    <span style="display: inline-block;position: relative;">
	        <img src="/images/mfyx_logo.jpeg" alt="" class="weui-tabbar__icon">
	        <span class="weui-badge weui-badge_dot" style="position: absolute;top: 0;right: -6px;"></span>
	    </span>
	    <p class="weui-tabbar__label">服务中心</p>
	</a>
	<a href="/user/index" class="weui-tabbar__item <#if sys_func=='user'>weui-bar__item_on </#if>" >
	    <img src="/images/mfyx_logo.jpeg" alt="" class="weui-tabbar__icon">
	    <p class="weui-tabbar__label">个人中心</p>
	</a>
  </div>