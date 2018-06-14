<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
	<title>摩放优选</title>
	<!-- Bootstrap -->
	<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!--Vue -->
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <!-- -->
    <link href="/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/templatemo-style.css" rel="stylesheet">
    
    <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js"></script>
</head>
<body class="light-gray-bg">

<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8"> 

<div class="container " id="container" style="oveflow:scroll">
    <ul class="nav navbar-nav nav-tabs" style="font-weight:bold;"> 
        <li class="<#if status='4refund'> active </#if>" style="width:25%" @click="getOrders('4refund',$event)"> 
          <a href="javascript:;"> 可退款 </a> 
        </li>  
        <li class="<#if status='refunding'> active </#if>" style="width:25%" @click="getOrders('refunding',$event)"> 
          <a href="javascript:;"> 退款中 </a> 
        </li> 
        <li class="<#if status='4exchange'> active </#if>" style="width:25%" @click="getOrders('4exchange',$event)"> 
          <a href="javascript:;" > 可换货 </a> 
        </li>
        <li class="<#if status='exchanging'> active </#if>" style="width:25%" @click="getOrders('exchanging',$event)"> 
          <a href="javascript:;" > 换货中 </a> 
        </li>                       
     </ul>
  
  <div class="row"><!-- 所有订单之容器 -->
  
	  <div v-for="order in orders" class="col-xs-12 col-sm-12 col-md-6 col-lg-4" style="padding:3px ">
	    <#include "/common/tpl-order-partner-4vue.ftl" encoding="utf8">
	    <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8">
		<div class="row" style="margin:1px 0;padding:0px 18px 0px 18px;background-color:white;">
		    <a v-if="startWith(order.status,'1') || order.status == '20'" class="btn btn-default pull-right" :href="'/order/cancel/begin/'+order.orderId" style="padding:0 3px;margin:0 3px" >
		      <span >取消订单</span>
		    </a>
		    <a v-if="startWith(order.status,'3') ||  startWith(order.status,'4') || startWith(order.status,'5')" class="btn btn-default pull-right" :href="'/order/logistics/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    
		    <a v-if="param.status=='4refund' && (startWith(order.status,'3') ||  startWith(order.status,'4') || order.status==='55' || order.status==='56' || order.status==='57' || order.status==='58')" class="btn btn-primary pull-right" :href="'/aftersale/refund/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >申请退款</span></a>
		    <a v-if="param.status=='4refund' && (order.status==='61')" class="btn btn-primary pull-right" :href="'/aftersale/refund/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >提交退货物流</span></a>
		    <a v-if="param.status=='4exchange' && (order.status==='40' || order.status==='41')" class="btn btn-primary pull-right" :href="'/aftersale/exchange/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >申请换货</span></a>
		    <a v-if="param.status=='4exchange' && (order.status==='51')" class="btn btn-primary pull-right" :href="'/aftersale/exchange/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >提交退货物流</span></a>		    
		</div>
	  </div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			status:"${status!'refund'}",
			begin:0,
			pageSize:100
		},
		orders:[]
	},
	methods:{
		getOrders:function(stat,event){
			$("#loadingData").show();
			$("#nomoreData").hide();
			if(event){
				$(event.target).addClass('active');$(event.target.parentElement).addClass('active');
				$(event.target).siblings().removeClass('active');$(event.target.parentElement).siblings().removeClass('active');
			}
			this.param.status = stat;
			containerVue.orders = [];
			
			$.ajax({
				url: '/aftersale/getall/' + this.param.status,
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
						containerVue.begin = jsonRet.pageCond.begin;
						containerVue.pageSize = jsonRet.pageCond.pageSize;
					}else{
						if(jsonRet && jsonRet.errmsg){
							//alert(jsonRet.errmsg);
							$("#nomoreData").show();
						}
					}
					$("#loadingData").hide();
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getOrders("${status!'refund'}");
</script>


<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>