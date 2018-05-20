<!-- 收货人信息 -->
  <div class="row" style="margin:0">
	  <div class="row" style="margin:1px 0px;background-color:white;">
	      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">收货信息</span>
	  </div>
	  <div class="row" style="margin:1px 1px ;padding:3px 0;background-color:white" >
	    <div class="col-xs-12">
	      <span>
	        <#if (order.headimgurl)?starts_with('http')>
	        <img alt="头像" src="${(order.headimgurl)!''}" width="20px" height="20px" style="border-radius:50%"> 
	        </#if>
	        <#if !(order.headimgurl)?starts_with('http')>
	        <img alt="头像" src="/user/headimg/show/${(order.userId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
		    </#if>
		    <span>${order.nickname}</span>
	     </span><br>
	     <span>${order.recvName} , ${(order.recvPhone)!''}</span>
	    </div>
	    <div class="col-xs-12">
	        <span>${order.recvProvince}</span>
	        <span>${order.recvCity}</span>
	        <span>${order.recvArea}</span>
	        <span>${order.recvAddr}</span>
	     </div>
	     <div class="col-xs-12">
	       {{getDispatchMode(${order.dispatchMode})}}
	     </div>
	  </div>
  </div>
  