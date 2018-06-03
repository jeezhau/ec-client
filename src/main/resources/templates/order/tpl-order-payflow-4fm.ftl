  <div class="row" style="margin:5px 1px ;" >
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">支付与退款信息</span>
    </div>
    <div class="row" style="margin:2px 1px ;padding:3px 0;background-color:white">
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	       <label class="col-xs-3" style="padding:0">订单号：</label>
	       <span class="col-xs-8" style="padding:0">${payFlow.orderId!''}</span>
	    </div>
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	       <label class="col-xs-3" style="padding:0">支付方式：</label>
	       <span class="col-xs-8"  style="padding:0">{{getPayType('${(payFlow.payType)!''}')}}</span>
	    </div>
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	        <label class="col-xs-3"  style="padding:0">创建时间：</label>
	        <span class="col-xs-8"  style="padding:0">${(payFlow.createTime)?string('yyyy-MM-dd hh:mm:ss')}</span>
	    </div>
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	        <label class="col-xs-3" style="padding:0">订单额¥：</label>
	        <span class="col-xs-8"  style="padding:0">${(payFlow.payAmount/100)}</span>     
	    </div>
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	        <label class="col-xs-3"  style="padding:0">手续费¥：</label>
	        <span class="col-xs-8"  style="padding:0">${payFlow.feeAmount/100}</span>
	    </div> 
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	        <label class="col-xs-3"  style="padding:0">支付状态：</label>
	        <span class="col-xs-8"  style="padding:0">{{getPayStatus('${payFlow.status}')}}</span>
	    </div> 
	    <div class="col-xs-12" style="text-align:cneter;padding:0 3px">
	        <a class="btn btn-success" href="/order/pay/finish/${payFlow.orderId!''}">支付结果查询</a>
	    </div>    
    </div>
  </div>