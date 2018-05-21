	<div class="row" style="margin:1px 0px;">
      <div class="row" style="margin:1px 0px;background-color:white;">
	    <div class="col-xs-4" style="padding-left:1px;padding-right:0px">
	      <a href="/goods/show/${(order.goodsId)?string('#')}"><img alt="" src="/image/file/show/${(order.mchtUId)?string('#')}/${(order.goodsMainImgPath)!''}" height=88px width=99%></a>
	    </div>
	    <div class="col-xs-8" style="overflow:scroll;padding:0 5px 1px 0">
	       <div style="text-align:center">${order.goodsName}</div>
		   <table class="table table-striped table-bordered table-condensed">
	         <tr>
	           <th width="30%" style="padding:2px 2px;text-align:center">规格名称</th>
	           <th width="15%" style="padding:2px 2px;text-align:center">量值</th>
	           <th width="20%" style="padding:2px 2px;text-align:center">售价(¥)</th>
	           <th width="20%" style="padding:2px 2px;text-align:center">购买数量</th>
	         </tr>
	         <tr v-for="item,index in order.goodsSpec" >
	           <td style="padding:2px 2px;">
	             <span style="width:100%" >{{item.name}}</span>
	           </td>
	           <td style="padding:2px 2px;text-align:right">
	              <span style="width:100%" >{{item.val}} {{item.unit}}</span>
	           </td>
	           <td style="padding:2px 2px;text-align:right">
	              <span style="width:100%" >{{item.price}}</span>
	           </td> 
	           <td style="padding:2px 2px;text-align:right">
                 <span  style="width:80%" > {{item.buyNum}}</span>
	           </td>
	         </tr>
	       </table>
	     </div>
	    </div>
	   <div class="row" style="margin:1px 0px;padding:1px 3px;background-color:white;">
	  	  <span class="pull-left"> 金额¥：${order.amount}</span> 
	  	  <span class="pull-right">配送方式：{{getDispatchMode('${order.dispatchMode}')}}</span>
	  </div>
	</div>
	