
    <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
	  <span class="pull-left" >
	    <#if !((order.headimgurl)!'')?starts_with('http') >
	    <img alt="头像" src="/user/headimg/show/${(order.userId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
	    <#else>
	    <img alt="头像" src="${order.headimgurl}" width="20px" height="20px" style="border-radius:50%"> 
	    </#if>
	    ${(order.nickname)!''}
	  </span>
	  <span class="pull-right">
	    <a href="/psaleorder/detail/${order.orderId}">{{getOrderStatus(order.status)}}</a>
	  </span>
	</div>