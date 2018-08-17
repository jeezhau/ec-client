<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="oveflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 

<div class="container " id="container" style="padding:0;">
  <div class="row" style="margin:5px 0;text-align:center" >
    <ul class="nav navbar-nav nav-tabs" style="width:100%">
        <li class="<#if status='all'> active </#if>" style="width:20%" onclick="window.location.href='/psaleorder/show/all'"> 
          <a href="javascript:;" style="padding:2px 3px"> 全 部 </a> 
        </li>  
        <li class="<#if status='4pay'> active </#if>" style="width:20%" onclick="window.location.href='/psaleorder/show/4pay'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待付款 </a> 
        </li> 
        <li class="<#if status='4delivery'> active </#if>" style="width:20%" onclick="window.location.href='/psaleorder/show/4delivery'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待发货 </a> 
        </li> 
        <li class="<#if status='4sign'> active </#if>" style="width:20%" onclick="window.location.href='/psaleorder/show/4sign'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待收货 </a> 
        </li> 
        <li class="<#if status='4appraise'> active </#if>" style="width:20%" onclick="window.location.href='/psaleorder/show/4appraise'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待评价 </a> 
        </li>                                               
     </ul>
  </div>
  <div class="row" style="margin:0"><!-- 所有订单之容器 --> 
  
    <div v-for="order in orders" class="row" style="margin:8px 0;padding:0 0">
      <#include "/porder/tpl-porder-buy-user-4vue.ftl" encoding="utf8"> 
      <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8"> 

	  <div class="row" style="margin:0px 0;padding:0px 18px 0px 18px;background-color:white;">
		    <a v-if="order.status ==='10' || order.status ==='11' || order.status ==='12' || order.status ==='20'" class="btn btn-default pull-right" style="padding:0 3px;margin:0 3px" @click="cancelOrder(order)">
		      <span >协商取消</span>
		    </a>

			<a v-if="order.status ==='20'" class="btn btn-info pull-right" style="padding:0 3px;margin:0 3px" @click="readyGoods(order)"><span >接单备货</span></a>
			<a v-if="order.status ==='21'" class="btn btn-info pull-right" style="padding:0 3px;margin:0 3px" @click="readyGoods(order)"><span >取消备货</span></a>
		    <a v-if="order.status ==='20' || order.status ==='21'" class="btn btn-danger pull-right" :href="'/psaleorder/delivery/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >立即发货</span></a>
		    
		    <a v-if="order.status==='30' " class="btn btn-default pull-right" :href="'/psaleorder/logistics/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    
		    <a v-if="order.status==='41' || order.status==='57' " class="btn btn-primary pull-right" :href="'/psaleorder/appraise/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span > 评 价 </span></a>
		    
      </div>
	</div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">

var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			status:'',
			begin:0,
			pageSize:30,
			count:0
		},
		orders:[]
	},
	methods:{
		getOrders:function(stat){
			$("#loadingData").show();
			$("#nomoreData").hide();
			if(stat){
				this.param.status = stat;
			}
			containerVue.orders = [];
			$.ajax({
				url: '/psaleorder/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							var item = jsonRet.datas[i];
							item.goodsSpec = JSON.parse(item.goodsSpec);
							item.headimgurl = startWith(item.headimgurl,'http')? item.headimgurl: ('/user/headimg/show/'+item.userId);
							containerVue.orders.push(item);
						}
						containerVue.param.pageSize = jsonRet.pageCond.pageSize;
						containerVue.param.begin = jsonRet.pageCond.begin;
						containerVue.param.count = jsonRet.pageCond.count;
					}else{
						if(jsonRet && jsonRet.errmsg){
							//alert(jsonRet.errmsg);
							//$("#nomoreData").show();
						}
					}
					$("#loadingData").hide();
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});
		},
		cancelOrder : function(order){
			$('#cancelOrderModal').modal('show');
			cancelOrderVue.order = order;
		},
		readyGoods: function(order){
			$('#readyGoodsModal').modal('show');
			readyGoodsVue.order = order;
		}
	}
});
containerVue.getOrders('${status!''}');
//分页初始化
scrollPager(containerVue.param,containerVue.orders,containerVue.getOrders) 

</script>

<!-- 卖家协商取消订单（Modal） -->
<div class="modal fade " id="cancelOrderModal" tabindex="-1" role="dialog" aria-labelledby="cancelOrderTitle" aria-hidden="false" data-backdrop="static" style="top:20%">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="cancelOrderTitle" style="color:red">协商取消订单</h4>
     		</div>
     		<div class="modal-body">
       			<#include "/porder/tpl-porder-buy-user-4vue.ftl" encoding="utf8"> 
      			<#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8"> 
       			 <div class="row" style="margin:3px 0px;background-color:white; color:red">
       			   <p/>
       			   <p>订单状态: {{getOrderStatus(order.status)}}</p>
       			   <span>&nbsp;&nbsp;&nbsp;&nbsp;如果您不能完成发货，请即时拨打该电话与买家协商，请求对方取消订单，结束本次交易！</span>
       			   <span>下单联系人电话：{{order.userPhone}}</span>
       			 </div>
     		</div>
     		<div class="modal-footer">
     			
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
 var cancelOrderVue = new Vue({
	 el:'#cancelOrderModal',
	 data:{
		 order:{},
	 },
	 methods:{
		 
	 }
 });
</script>

<!-- 买家备货订单（Modal） -->
<div class="modal fade " id="readyGoodsModal" tabindex="-1" role="dialog" aria-labelledby="readyGoodsModalTitle" aria-hidden="false" data-backdrop="static" style="top:20%">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="readyGoodsModalTitle" style="color:red">接单备货</h4>
     		</div>
     		<div class="modal-body">
       			<#include "/porder/tpl-porder-buy-user-4vue.ftl" encoding="utf8"> 
      			<#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8"> 
	     		<div class="row" style="margin:3px 0px;padding:5px 10px;background-color:white">
				   <span>以下为买家备注：</span><br>
				   <p>{{order.remark}}</p>
				</div>     			
       			 <div class="row" style="margin:3px 0px;background-color:white; color:red">
       			   <p/>
       			   <p>订单状态: {{getOrderStatus(order.status)}}</p>
       			   <span>&nbsp;&nbsp;&nbsp;&nbsp;如果您能保证尽快发货，但是目前库存不足，可以电联告知买家，然后执行此操作！务必注意：如果长时间未发货，可能遭到买家投诉！</span>
       			   <span>下单联系人电话：{{order.userPhone}}</span>
       			 </div>
     		</div>
     		<div class="modal-footer" style="text-align:certer">
     			<button class="btn btn-info" @click="submit"> 
     			  <span v-if="order.status=='20'">开始备货 </span>
     			  <span v-if="order.status=='21'">取消备货 </span>
     			</button>
     			<button class="btn btn-default" data-dismiss="modal"> 关 闭 </button>
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
 var readyGoodsVue = new Vue({
	 el:'#readyGoodsModal',
	 data:{
		 order:{},
	 },
	 methods:{
		 submit: function(){
			 $("#dealingData").show();
			 $.ajax({
				url: '/psaleorder/ready/' + this.order.orderId,
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							window.location.href = "/psaleorder/show/all";
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！');
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		 }
	 }
 });
</script>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
