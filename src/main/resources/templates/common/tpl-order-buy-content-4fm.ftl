	<div class="row" style="margin:2px 0px;">
      <div class="row" style="margin:1px 0px;background-color:white;">
	    <div class="col-xs-4" style="text-align:center;padding-left:1px;padding-right:0px">
	      <a href="/shop/goods/${(order.goodsId)?string('#')}"><img alt="" src="/shop/gimage/${(order.partnerId)?string('#')}/${(order.goodsMainImgPath)!''}" style="height:88px;max-width:99%"></a>
	    </div>
	    <div class="col-xs-8" style="overflow:scroll;padding:0 5px 1px 0">
	       <div style="text-align:center">${order.goodsName}</div>
		   <table class="table table-striped table-bordered table-condensed">
	         <tr>
	           <th width="30%" style="padding:2px 2px;text-align:center">规格名称</th>
	           <th width="15%" style="padding:2px 2px;text-align:center">规格</th>
	           <th width="20%" style="padding:2px 2px;text-align:center">售价(¥)</th>
	           <th width="20%" style="padding:2px 2px;text-align:center">购买数量</th>
	         </tr>
	         <#list order.specList as item>
	         <tr>
	           <td style="padding:2px 2px;">
	             <span style="width:100%" >${item.name}</span>
	           </td>
	           <td style="padding:2px 2px;text-align:right">
	              <span style="width:100%" >${item.val}${item.unit}</span>
	           </td>
	           <td style="padding:2px 2px;text-align:right">
	              <span style="width:100%" >${item.price}</span>
	           </td> 
	           <td style="padding:2px 2px;text-align:right">
                 <span  style="width:80%" >${item.buyNum}</span>
	           </td>
	         </tr>
	         </#list>
	       </table>
	     </div>
	    </div>
	   <#if (order.amount)??>
	   <div class="row" style="margin:1px 0px;padding:1px 3px;background-color:white;">
	  	  <span class="pull-left"> 金额¥：${order.amount}</span> 
	  </div>
	  </#if>
	</div>
	