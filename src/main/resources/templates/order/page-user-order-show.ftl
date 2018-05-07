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
</head>
<body class="light-gray-bg">
<div class="container " id="container" style="oveflow:scroll">
  <div class="row" style="margin:5px 0;text-align:center" >
    <ul class="nav navbar-nav nav-tabs" style="padding:0 5px">
        <li class="<#if status='all'> active </#if>"  @click="getOrders('all',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 全 部 </a> 
        </li>  
        <li class="<#if status='4pay'> active </#if>"  @click="getOrders('4pay',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待付款 </a> 
        </li> 
        <li class="<#if status='4delivery'> active </#if>"  @click="getOrders('4delivery',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待发货 </a> 
        </li> 
        <li class="<#if status='4sign'> active </#if>"  @click="getOrders('4sign',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待收货 </a> 
        </li> 
        <li class="<#if status='4appraise'> active </#if>"  @click="getOrders('4appraise',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 待评价 </a> 
        </li> 
        <li class="<#if status='4refund'> active </#if>"  @click="getOrders('4refund',$event)"> 
          <a href="javascript:;" style="padding:2px 3px"> 退换货 </a> 
        </li>                                               
     </ul>
  </div>
  <div class="row"><!-- 所有订单之容器 -->
  
	  <div v-for="order in orders" class="row" style="margin:3px 0;padding:0 0">
	    <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
	      <a class="pull-left" :href="'/partner/mcht/' + order.partnerId">
	        <img alt="头像" :src="'/partner/cert/show/logo/' + order.partnerId" width="20px" height="20px" style="border-radius:50%"> 
	        {{order.partnerBusiName}}
	      </a>
		  <span class="pull-right"><a href="javascript:;">{{getStatus(order.status)}}</a></span>
	    </div>
	    <div class="row" style="margin:1px 0px;background-color:white;">
		    <div class="col-xs-4" style="padding-left:1px;padding-right:0px">
		      <a :href="'/goods/show/' + order.goodsId"><img alt="" :src="'/image/file/show/' + order.goodsMainImgPath" height=88px width=99%></a>
		    </div>
		    <div class="col-xs-8" style="overflow:scroll;padding:0 5px 1px 0">
		       <div>{{order.goodsName}}</div>
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
		  	<span class="pull-left">实付¥：{{order.amount}}</span> 
		  	<span class="pull-right">配送方式：{{getDispatchMode(order.dispatchMode)}}</span>
		  </div>
		  <div class="row" style="margin:3px 0;padding:3px 18px 3px 18px;background-color:white;">
		    <a v-if="order.status ==='10'" class="btn btn-default pull-right" :href="'/order/pay/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >取消订单</span></a>
		    <a v-if="order.status ==='10'" class="btn btn-danger pull-right" :href="'/order/pay/begin/' + order.orderId" style="padding:0 3px;margin:0 3px"><span >立即付款</span></a>
		    
		    <a v-if="order.status==='30' " class="btn btn-default pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    <a v-if="order.status==='30' " class="btn btn-default pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >延长收货</span></a>
		    <a v-if="order.status==='30' " class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >确认收货</span></a>
		    
		    <a v-if="order.status==='40' " class="btn btn-primary pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >立即评价</span></a>
		    
		    <a v-if="order.status==='31' " class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >申请换货</span></a>
		    <a v-if="order.status==='31' " class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >申请退货</span></a>
		    
		  </div>
		  
	  </div>
	
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			status:''
		},
		orders:[]
	},
	methods:{
		getStatus:function(code){
			if(code == '10'){
				return '待付款';
			}else if(code == '20'){
				return '待发货';
			}else if(code == '30'){
				return '待签收';
			}else if(code == '40'){
				return '待评价';
			}
			
		},
		getDispatchMode:function(code){
			if(code){
				if('1' === code){
					return '官方统一配送';
				}else if('2' == code){
					return '商家自行配送';
				}else if('3' == code){
					return '快递配送';
				}else if('4' == code){
					return '客户自取';
				}
			}
		},
		getOrders:function(stat,event){
			if(event){
				$(event.target).addClass('active');$(event.target.parentElement).addClass('active');
				$(event.target).siblings().removeClass('active');$(event.target.parentElement).siblings().removeClass('active');
			}
			this.param.status = stat;
			containerVue.orders = [];
			$.ajax({
				url: '/order/user/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							var item = jsonRet.datas[i];
							item.goodsSpec = JSON.parse(item.goodsSpec);
							containerVue.orders.push(item);
						}
					}else{
						if(jsonRet && jsonRet.errmsg){
							alert(jsonRet.errmsg);
						}
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getOrders('${status!''}');
</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
