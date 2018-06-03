
    <div class="col-xs-12 col-" style="margin:0px 0px;padding:5px 10px;background-color:white">
      <a class="pull-left" href="/shop/mcht/${(order.partnerId)?string('#')}">
        <img alt="头像" src="/shop/pcert/logo/${(order.partnerId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
        ${(order.partnerBusiName)!''}
      </a>
	  <span class="pull-right"><a href="/order/detail/${order.orderId}">{{getOrderStatus(order.status)}}</a></span>
    </div>