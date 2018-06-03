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
    
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js"></script>
</head>
<body class="light-gray-bg">

<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8"> 

<div class="container " id="container" style="oveflow:scroll">
  <div class="row" style="margin:5px 0;text-align:center" >
    <ul class="nav navbar-nav nav-tabs" style="width:100%;padding:0 5px">
        <li class="<#if status='all'> active </#if>" style="width:20%"  @click="getOrders('all',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 全 部 </a> 
        </li>  
        <li class="<#if status='4pay'> active </#if>" style="width:20%" @click="getOrders('4pay',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待付款 </a> 
        </li> 
        <li class="<#if status='4delivery'> active </#if>" style="width:20%" @click="getOrders('4delivery',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待发货 </a> 
        </li> 
        <li class="<#if status='4sign'> active </#if>" style="width:20%" @click="getOrders('4sign',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待签收 </a> 
        </li> 
        <li class="<#if status='4appraise'> active </#if>" style="width:20%" @click="getOrders('4appraise',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待评价 </a> 
        </li>                                               
     </ul>
  </div>
  <div class="row"><!-- 所有订单之容器 -->
  
	  <div v-for="order in orders" class="row" style="margin:8px 0;padding:0 0">
	    <#include "/common/tpl-order-partner-4vue.ftl" encoding="utf8">
	    <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8">
		<div class="row" style="margin:1px 0;padding:0px 18px 0px 18px;background-color:white;">
		    <a v-if="startWith(order.status,'1') || order.status == '20'" class="btn btn-default pull-right" :href="'/order/cancel/begin/'+order.orderId" style="padding:0 3px;margin:0 3px" >
		      <span >取消订单</span>
		    </a>
		    <a v-if="order.status ==='10' || order.status ==='12'" class="btn btn-danger pull-right" :href="'/order/pay/choose/' + order.orderId" style="padding:0 3px;margin:0 3px">
		      <span >立即付款</span>
		    </a>
		    
		    <a v-if="order.status==='30' " class="btn btn-default pull-right" :href="'/order/logistics/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    
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
				url: '/order/getall',
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
		},
		cancelOrder:function(order){
			$('#cancelOrderModal').modal('show');
			cancelOrderVue.order = order;
		}
	}
});
containerVue.getOrders('${status!''}');
var winHeight = $(window).height(); //页面可视区域高度   
var scrollHandler = function () {  
    var pageHieght = $(document.body).height();  
    var scrollHeight = $(window).scrollTop(); //滚动条top   
    var r = (pageHieght - winHeight - scrollHeight) / winHeight;
    if (r < 0.5) {//上拉翻页 
   	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
   	    containerVue.getOrders(containerVue.param.status);
    }
    if(scrollHeight<0){//下拉翻页
   	 	containerVue.param.begin = containerVue.param.begin - containerVue.param.pageSize;
   	 	if(containerVue.param.begin <= 0){
   	 		containerVue.param.begin = 0;
   	 	}
   	    containerVue.getOrders(containerVue.param.status);
    }
}  
//定义鼠标滚动事件  
$("#container").scroll(scrollHandler);
</script>


<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
