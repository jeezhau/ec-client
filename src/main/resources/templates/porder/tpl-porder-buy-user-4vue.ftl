
	    <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
		  <span class="pull-left" >
		    <img alt="头像" :src="order.headimgurl" width="20px" height="20px" style="border-radius:50%"> 
		    {{order.nickname}}
		  </span>
		  <span class="pull-right">
		    <a :href="'/psaleorder/detail/'+ order.orderId">{{getOrderStatus(order.status)}}</a>
		  </span>
		</div>