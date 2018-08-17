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
  <div class="row" style="margin:5px 0;text-align:center" >
    <ul class="nav navbar-nav nav-tabs" style="width:100%;padding:0 5px">
        <li class="<#if status='all'> active </#if>" style="width:20%"  onclick="window.location.href='/order/show/all' "> 
          <a href="javascript:;" style="padding:2px 3px"> 全 部 </a> 
        </li>  
        <li class="<#if status='4pay'> active </#if>" style="width:20%" onclick="window.location.href='/order/show/4pay'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待付款 </a> 
        </li> 
        <li class="<#if status='4delivery'> active </#if>" style="width:20%" onclick="window.location.href='/order/show/4delivery'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待发货 </a> 
        </li> 
        <li class="<#if status='4sign'> active </#if>" style="width:20%" onclick="window.location.href='/order/show/4sign'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待签收 </a> 
        </li> 
        <li class="<#if status='4appraise'> active </#if>" style="width:20%" onclick="window.location.href='/order/show/4appraise'"> 
          <a href="javascript:;" style="padding:2px 3px"> 待评价 </a> 
        </li>                                               
     </ul>
  </div>
  <div class="row"><!-- 所有订单之容器 -->
  
	  <div v-for="order in orders" class="row" style="margin:8px 0;padding:0 0">
	    <#include "/common/tpl-order-partner-4vue.ftl" encoding="utf8">
	    <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8">
		<div class="row" style="margin:1px 0;padding:0px 18px 0px 18px;background-color:white;">
		    <a v-if="startWith(order.status,'1') || order.status == '20' || order.status == 'DF'" class="btn btn-default pull-right" :href="'/order/cancel/begin/'+order.orderId" style="padding:0 3px;margin:0 3px" >
		      <span >取消订单</span>
		    </a>
		    <a v-if="order.status ==='10' || order.status ==='12'" class="btn btn-danger pull-right" :href="'/order/pay/choose/' + order.orderId" style="padding:0 3px;margin:0 3px">
		      <span >立即付款</span>
		    </a>
		    
		    <a v-if="order.status==='30' " class="btn btn-default pull-right" :href="'/order/logistics/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    <a v-if="order.status==='22' || order.status==='30' || order.status==='55'" class="btn btn-default pull-right" @click="prolong(order.orderId)" style="padding:0 3px;margin:0 3px"><span >延长收货</span></a>
		    
		    <a v-if="order.status==='30' || order.status==='54'" class="btn btn-primary pull-right" :href="'/order/appraise/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >签收评价</span></a>
		    <a v-if="order.status==='31' || order.status==='40' || order.status==='55'" class="btn btn-primary pull-right" :href="'/order/appraise/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >立即评价</span></a>
		    <a v-if="order.status==='41' || order.status==='56'" class="btn btn-primary pull-right" :href="'/order/appraise/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >追加评价</span></a>
		    
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
		prolong: function(orderId){
			 $("#dealingData").show();
			 $.ajax({
				url: '/order/prolong/' + orderId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode === 0){//成功
							alertMsg('系统提示',"延长收货申请成功！！！");
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
		getOrders:function(stat){
			$("#loadingData").show();
			$("#nomoreData").hide();
			if(stat){
				this.param.status = stat;
			}
			containerVue.orders = [];
			$.ajax({
				url: '/order/getall/0',
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
						containerVue.param.begin = jsonRet.pageCond.begin;
						containerVue.param.pageSize = jsonRet.pageCond.pageSize;
						containerVue.param.count = jsonRet.pageCond.count;
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
containerVue.getOrders('${status!''}');
//分页初始化
scrollPager(containerVue.param,containerVue.orders,containerVue.getOrders) ;

</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
