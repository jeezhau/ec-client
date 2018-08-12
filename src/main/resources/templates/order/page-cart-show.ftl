<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">

<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8"> 
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container " id="container" style="oveflow:scroll">
  <div class="row">
    <div class="col-xs-2"><a href="/shop/index"><img alt="" src="/icons/返回.png" style="width:18px;height:18px"></a></div>
    <div class="col-xs-10"><h3 style="text-align:center;margin:3px">我的购物车</h3></div>
  </div>
  <div class="row">
	  <div v-for="order,index in orders" class="row" style="margin:8px 0;padding:0 0">
	    <#include "/common/tpl-order-partner-4vue.ftl" encoding="utf8">
	    <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8">
		<div class="row" style="margin:1px 0;padding:0px 18px 0px 18px;background-color:white;">
		    <a v-if="order.status == '10'" class="pull-left" style="padding:0 3px;margin:0 3px" >
		      <input type="checkbox" style="display:inline-block;width:20px;height:20px" :value="order.orderId" v-model="param.selOrrders" @change="sumSel(order.orderId,order.amount)">
		    </a>
		    <a v-if=" order.status == '10' && order.incart == '1'" class="btn btn-danger pull-right" @click="delOrder(order.orderId,index)" style="padding:0 3px;margin:0 3px" >
		      <span>删除</span>
		    </a>
		</div>
	  </div>
	  <p v-if="orders.length == 0" style="text-align:center;color:gray;margin-top:100px">WO还是空的，赶紧去把WO装满吧！！！</p>
  </div>
  <!-- 支付 -->
  <footer >
    <form action="/order/pay/btchoose" method="post">
      <input type="hidden" name="orderIds" v-model="param.orderIds">
	  <div class="row" style="margin:50px 0"></div>
	  <p v-if="orders.length>0 && param.selOrrders.length==0" style="text-align:center;position:fixed;left:3px;bottom:1px">选择订单可完成批量支付</p>
	  <div v-if="param.selOrrders.length>0" class="weui-tabbar" style="position:fixed;left:0px;bottom:1px">
	    	<span class="weui-tabbar__item " style="width:70%">
	      <span class="weui-tabbar__label" >
	      	 <span>共 <span style="color:red;font-size:18px">{{param.countAll}}</span> 件</span>
	      	 <span> <span style="color:red;font-size:18px">{{new Number(param.amountAll).toFixed(2)}}</span> 元</span>
	      </span>
		</span>
	    <button type="submit" class="weui-tabbar__item " style='width:30%;background-color:#EA0000;text-align:center;vertical-align:center;'>
		   <span class="weui-tabbar__label" style="margin:35px 0;font-size:25px;color:white">立即支付</span>
	    </button>
	  </div>
	</form>
  </footer>
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			orderIds:'',
			selOrrders:[],
			countAll:0,
			amountAll:0.00
		},
		pageCond:{
			begin:0,
			pageSize:100,
			count:0
		},
		orders:[]
	},
	methods:{
		delOrder: function(orderId,index){
			if(!confirm("您确定要删除该订单吗？")){
				return;
			}
			 $("#dealingData").show();
			 $.ajax({
				url: '/order/cart/remove/' + orderId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							alertMsg('系统提示',"删除购物车订单成功！！！");
							containerVue.orders.splice(index,1);
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
		 },
		 sumSel:function(orderId,amount){
			 this.param.countAll = this.param.selOrrders.length;
			 var isSel = false;
			 for(var i=0;i<this.param.selOrrders.length;i++){
				 if(this.param.selOrrders[i] == orderId){
					 isSel = true;
				 }
			 }
			 if(isSel){
				 this.param.amountAll += amount;
			 }else{
				 this.param.amountAll -= amount;
			 }
			 this.param.orderIds = this.param.selOrrders.join('_');
		 },
		 getOrders:function(stat){
			$("#loadingData").show();
			$("#nomoreData").hide();
			containerVue.orders = [];
			$.ajax({
				url: '/order/getall/1',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							var item = jsonRet.datas[i];
							item.goodsSpec = JSON.parse(item.goodsSpec);
							item.headimgurl = startWith(item.headimgurl,'http')? item.headimgurl: ('/user/headimg/show/'+item.userId);
							containerVue.orders.push(item);
						}
						containerVue.begin = jsonRet.pageCond.begin;
						containerVue.pageSize = jsonRet.pageCond.pageSize;
					}else{
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								//alertMsg('错误提示',jsonRet.errmsg);
							}
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
		cancelOrder:function(order){
			$('#cancelOrderModal').modal('show');
			cancelOrderVue.order = order;
		}
	}
});
containerVue.getOrders();
//分页初始化
scrollPager(containerVue.pageCond,containerVue.orders,containerVue.getOrders) ;

</script>



</body>
</html>
