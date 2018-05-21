  <!-- 商品信息 -->
  <div class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
    <div class="col-xs-12" style="text-align:center;">
      <a class="pull-left" href="/partner/mcht/${(order.partnerId)?string('#')}">
	    <img alt="头像" src="/partner/cert/show/logo/${(order.partnerId)?string('#')}" width="20px" height="20px" style="border-radius:50%"> 
	    <span>${(order.partnerBusiName)!''}</span>
	  </a><br>
      <span>${order.goodsName}</span>
    </div>
    <div class="col-xs-12" style="text-align:center;">
      <a href="/goods/show/${(order.goodsId)?string('#')}">
       <img alt="" src="/image/file/show/${(order.mchtUId)?string('#')}/${(order.goodsMainImgPath)!''}" style="width:99%;height:150px;">
      </a>
    </div>
    <div class="col-xs-12" style="padding:0px 3px">
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="30%" style="padding:2px 2px">规格名称</th>
           <th width="15%" style="padding:2px 2px">量值</th>
           <th width="15%" style="padding:2px 2px">售价(¥)</th>
           <th width="20%" style="padding:2px 2px">购买数量</th>
         </tr>
         <tr v-for="item,index in goodsSpecArr" >
           <td style="padding:2px 2px">
             <span style="width:100%" >{{item.name}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.val}} {{item.unit}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.price}}</span>
           </td> 	                         
           <td style="padding:2px 2px;text-align:center">
              <span style="width:100%" >{{item.buyNum}}</span>
           </td>
         </tr>
       </table>    
     </div>
     <div class="row" style="margin:1px 1px;padding:3px 5px;background-color:white;">
		<span class="pull-left"> 金额¥：${order.amount}</span> 
		<span class="pull-right">{{getOrderStatus('${order.status}')}}</span>
	 </div>
  </div> 