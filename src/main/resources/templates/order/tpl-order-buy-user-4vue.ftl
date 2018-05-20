
	    <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
		  <span class="pull-left" >
		    <img v-if="startWith(order.headimgurl,'http')" alt="头像" :src="'/user/headimg/show/' + order.userId" width="20px" height="20px" style="border-radius:50%"> 
		    <img v-if="!startWith(order.headimgurl,'http')" alt="头像" :src="order.headimgurl" width="20px" height="20px" style="border-radius:50%"> 
		    {{order.nickname}}
		  </span>
		  <span class="pull-right">
		    <a :href="'/order/detail/'+ order.orderId">{{getOrderStatus(order.status)}}</a>
		  </span>
		</div>