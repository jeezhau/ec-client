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
<div class="container goods-container" style="oveflow:scroll">
  <div id="goodsImgCarousel" class="carousel slide goods-item">
    <!-- 轮播（Carousel）指标 -->
    <ol class="carousel-indicators">
        <li data-target="#goodsImgCarousel" data-slide-to="1" class="active"></li>
        <li data-target="#goodsImgCarousel" data-slide-to="2"></li>
        <li data-target="#goodsImgCarousel" data-slide-to="3"></li>
        <li data-target="#goodsImgCarousel" data-slide-to="4"></li>
        <li data-target="#goodsImgCarousel" data-slide-to="5"></li>
    </ol>   
    <!-- 轮播（Carousel）项目 -->
    <div class="carousel-inner" style="height:100%">
        <div class="item active">
            <img src="/images/slide1.png" alt="First slide">
        </div>
        <div class="item">
            <img src="/images/slide2.jpeg" alt="Second slide">
        </div>
        <div class="item">
            <img src="/images/slide3.JPG" alt="Third slide">
        </div>
        <div class="item">
            <img src="/images/slide4.png" alt="Four slide">
        </div>
        <div class="item">
            <img src="/images/slide5.jpeg" alt="Five slide">
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#goodsImgCarousel" 
       data-slide="prev"> <span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
    <a class="carousel-control right" href="#goodsImgCarousel" 
       data-slide="next">&rsaquo;</a>
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white">
    <!-- 售卖参数 -->
    <div class="col-xs-6"><span class="pull-left" style="color:red">优惠价¥: <span>0.00</span> </span>
    </div>
    <div class="col-xs-6 pull-right">
    <span class="pull-right">已卖: <span>123</span> 件</span>
    </div>
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;padding:3px 3px;background-color:white;font-weight:bold;">
    是否哈里哈；哈佛；是否哈；水立方加快立法；还是立刻发货司法考试发挥水库
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;font-weight:lighter;font-size:80%">
    <!-- 服务特点 -->
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">正品保证</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">同城急速</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">7天退货</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">30小时发货</span></div>
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;" onclick="">
    <!-- 前三条买家评价 -->
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家评价(10)</span>
      <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">查看全部&gt;</span>
    </div>
    <div class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left"><img alt="头像" src="/images/mfyx_logo.jpeg" width="20px" height="20px" style="border-radius:50%">用户昵称</span>
       <span class="pull-right">2018-4-15</span>
     </div>
     <div class="row">
       非常好，味道不错，水分充足，以后一定经常关注。
     </div>
     <div class="row" style="font-weight:lighter;font-size:80%;color:gray">
       重量：3斤大果
     </div>
    </div>
    
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;">
    <!-- 商品参数详细信息 -->
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">宝贝规格</span>
    </div>
    <div class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
      <span>产地：昆明</span><br>
      <span>重量：3斤</span>
      <span>库存：100件</span>
    </div>
  </div>
  <div class="row" style="margin:5px 0px 3px 0px;">
    <!-- 商品详情 -->
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">宝贝详情</span>
    </div>
    <div class="row" style="width:100%;margin:1px 0px;padding:0 8px;background-color:white;">
           比较运算符  
      
    表达式中支持的比较运算符有如下几个:  
     =或者==:判断两个值是否相等.  
    !=:判断两个值是否不等.  
    gt:判断左边值是否大于右边值  
    gte:判断左边值是否大于等于右边值  
    lt:判断左边值是否小于右边值  
    lte:判断左边值是否小于等于右边值  
      
    注意:=和!=可以用于字符串,数值和日期来比较是否相等,但=和!=两边必须是相同类型的值,否则会产生错误,
    而且FreeMarker是精确比 较,”x”,”x “,”X”是不等的.其它的运行符可以作用于数字和日期,但不能作用于字符串,大部分的时候,使用gt等字母运算符代替>会有更好的效果,
    因为 FreeMarker会把>解释成FTL标签的结束字符,当然,也可以使用括号来避免这种情况,如: 
               比较运算符  
      
    表达式中支持的比较运算符有如下几个:  
     =或者==:判断两个值是否相等.  
    !=:判断两个值是否不等.  
    gt:判断左边值是否大于右边值  
    gte:判断左边值是否大于等于右边值  
    lt:判断左边值是否小于右边值  
    lte:判断左边值是否小于等于右边值  
      
    注意:=和!=可以用于字符串,数值和日期来比较是否相等,但=和!=两边必须是相同类型的值,否则会产生错误,
    而且FreeMarker是精确比 较,”x”,”x “,”X”是不等的.其它的运行符可以作用于数字和日期,但不能作用于字符串,大部分的时候,使用gt等字母运算符代替>会有更好的效果,
    因为 FreeMarker会把>解释成FTL标签的结束字符,当然,也可以使用括号来避免这种情况,如:  
    </div>
  </div> 
  <div class="row">
    <!-- 同类推荐 -->
  </div> 
  <div class="row" style="margin:30px 0">
  </div>
</div><!-- end of container -->
<footer style="left:0px;bottom:0px;padding:0 0">
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px">
    	<a href="/shop/index" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/首页.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">商城首页</p>
	</a>
    <a href="#" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/收藏.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">收藏</p>
     </a>
    <a href="/order/order/begin/goodsId" class="weui-tabbar__item " style='background-color:red;'>
	    <span class="weui-tabbar__label" style="text-align:center;vertical-align:middle;font-size:20px;color:white">立即下单</span>
     </a>     	
  </div>
</footer>
</body>
</html>