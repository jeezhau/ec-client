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
<div class="container " style="oveflow:scroll">
  <div class="row" style="margin:5px 0;text-align:center" >
    <a class="col-xs-2 <#if status='all'> active </#if>" href="#" style="padding:0px 1px;">全部</a>
    <a class="col-xs-2 <#if status='forPay'> active </#if>" href="#" style="padding:0px 1px;" >待付款</a>
    <a class="col-xs-2 <#if status='forDelivery'> active </#if>" href="#" style="padding:0px 1px;" >待发货</a>
    <a class="col-xs-2 <#if status='forTake'> active </#if>" href="#" style="padding:0px 1px;" >待收货</a>
    <a class="col-xs-2 <#if status='forAppraise'> active </#if>" href="#" style="padding:0px 1px;" >待评价</a>
    <a class="col-xs-2 <#if status='forRefund'> active </#if>" href="#" style="padding:0px 1px;" >退换货</a>
  </div>
  <div class="row"><!-- 所有订单之容器 -->
	  <div class="row goods-item" style="padding:0 0">
	    <div class="row" style="margin:0px 0px;padding:5px 10px;background-color:white">
	      <span class="pull-left"><img alt="头像" src="/images/mfyx_logo.jpeg" width="20px" height="20px" style="border-radius:50%">商家经营名称</span>
		  <span class="pull-right">状态-待付款</span>
	    </div>
	    <div class="row " style="margin:1px 0px;">
	      <div class="row" style="background-color:white;">
		    <div class="col-xs-4" style="padding-left:16px;padding-right:0px">
		      <a href="/goods/detail/goodsId"><img alt="" src="/images/mfyx_logo.jpeg" height=68px width=80%></a>
		    </div>
		    <div class="col-xs-8" style="overflow:hidden;padding:0 5px 1px 0">
		    		&nbsp;&nbsp;&nbsp;&nbsp;是非得失分身乏术地方简单描述简单描述史蒂夫舒服舒服的方sdfsfsf首发首发对
		    </div>
		  </div>
		  <div class="row" style="margin:1px 0;padding:3px 18px 1px 18px;background-color:white;">
		    <span>规格：<span>红色</span></span>
		  </div>
		  <div class="row" style="padding:3px 18px 3px 18px;background-color:white;">
		    <a class="btn btn-danger pull-right" href="/order/pay/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >立即付款</span></a>
		    
		    <a class="btn btn-default pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >查看物流</span></a>
		    <a class="btn btn-default pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >延长收货</span></a>
		    <a class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >确认收货</span></a>
		    
		    <a class="btn btn-primary pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >立即评价</span></a>
		    
		    <a class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >申请换货</span></a>
		    <a class="btn btn-danger pull-right" href="/order/order/begin/goodsId" style="padding:0 3px;margin:0 3px"><span >申请退货</span></a>
		    
		  </div>
	    </div>
	  </div>
	<div class="row goods-item" >
	  <div class="row" >
	    <div class="row">
	        <span class="pull-left"><img alt="头像" src="/images/mfyx_logo.jpeg" width="20px" height="20px" style="border-radius:50%">商家经营名称</span>
	        <span class="pull-right">状态-待付款</span>
	    </div>
	  </div>
	  <div class="row" >  
	    <div class="col-xs-7 goods-item_main-img">
	      <a href="/goods/detail/goodsId"><img title="190X153最优" alt="" src="/images/mfyx_logo.jpeg" class="goods-item_main-img-size"></a>
	    </div>
	    <div class="col-xs-5 goods-item_info">
	      <div class="goods-item_info-desc">
		        是非得失分身乏术地方简单描述
		        简单描述史蒂夫舒服舒服的方sdfsfsf首发首发对
	      </div>
	      <div>
	      	<p class="goods-item_info-price">优惠价¥: <span>0.00</span></p>
	      </div>
	      <div>
	        <a class="btn btn-danger goods-item_info-order" href="/order/order/begin/goodsId">
	          <span style="color:white">立即下单</span>
	        </a>
	      </div>
	    </div>
	  </div>
	</div><!-- end if item -->	
  </div>
  
</div><!-- end of container -->

</body>
</html>