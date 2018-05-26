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
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#if (goods.goodsId)??>
<div class="container goods-container" id="container" style="oveflow:scroll">

  <!-- 商品名称 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;padding:3px 8px;font-size:150%;font-weight:bold;">
    ${(goods.goodsName)!''}
  </div> 
  <!-- 图片轮播 -->
  <div id="myCarousel" class="carousel slide">
    <!-- 轮播（Carousel）指标 -->
    <ol class="carousel-indicators">
        <li v-for="item,index in courselImgPaths" data-target="#myCarousel" :data-slide-to="index" v-bind:class="{ active: index===0 }"></li>
    </ol>   
    <!-- 轮播（Carousel）项目 -->
    <div class="carousel-inner">
        <div  v-bind:class="[{active:(index===0)}, 'item']" v-for="imgpath,index in courselImgPaths" >
            <img :src="'/image/file/show/${((goods.partner.vipId)!'')?string('#')}/' + imgpath" >
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#myCarousel" 
       data-slide="prev"> <span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
    <a class="carousel-control right" href="#myCarousel" 
       data-slide="next">&rsaquo;</a>
  </div>
  
  <!-- 售卖信息 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;color:red">
    <div class="col-xs-4" style="padding-right:3px;">最低价(¥)：<span>${(goods.priceLowest)!''}</span></div>
    <div class="col-xs-4" style="padding:0 3px;">总库存：<span>${(goods.stockSum)!''}</span></div>
    <div class="col-xs-4" style="padding-left:3px;">已售：<span style="color:red">${(goods.saledCnt)!''}</span></div>
  </div>
  
  <!-- 商家信息 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;padding:3px 8px;">
    <a href="/partner/mcht/${(goods.partnerId)?string('#')}">
     <img class="pull-left" alt="" src="/partner/cert/show/logo/${(goods.partnerId)?string('#')}" style="width:25px;height:25px;border-radius:30%">
    </a>
   <span class="pull-right">${(goods.partner.province)!''}-${(goods.partner.area)!''}-${(goods.partner.addr)!''}</span>
  </div>

  <!-- 服务特点 -->
  <div class="row" style="margin:5px 0px 3px 0px;font-weight:lighter;font-size:80%">
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">正品保证</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">同城急速</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">退货保障</span></div>
    <div class="col-xs-3" style="padding:0 3px"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">极速发货</span></div>
  </div>
  
  <!--  ====== 前三条买家评价 ======= -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家评价({{apprCnt}})</span>
      <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">
        <a v-if="apprCnt>0" href="/appraise/show/goods/${(goods.goodsId)?string('#')}">查看全部&gt;</a>
        <a v-if="apprCnt<=0" href="javascript:;">查看全部&gt;</a>
      </span>
    </div>
    <div v-for="appr in apprList" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left">
         <img alt="头像" :src="appr.headimgurl" width="20px" height="20px" style="border-radius:50%">{{appr.nickname}}
       </span>
       <span class="pull-right">{{appr.appraiseInfo[0].time}}</span>
     </div>
     <div class="row">
       {{appr.appraiseInfo[0].content}}
     </div>
    </div>    
  </div>
  
  <!--  ====== 商品参数详细信息 =====   -->
  <div class="row" style="margin:5px 0px 3px 0px;">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">商品参数</span>
    </div>
    <div class="row" style="margin:1px 0px;padding:0 8px;background-color:white;">
      <div class="row">
        <div class="col-xs-12"> 产地：<span>${(goods.place)!''}</span></div>
        <div class="col-xs-12"> 生产者：<span>${(goods.vender)!''}</span></div>
      </div>
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="40%" style="padding:2px 2px">名称</th>
           <th width="20%" style="padding:2px 2px">量值</th>
           <th width="20%" style="padding:2px 2px">售价(¥)</th>
           <th width="20%" style="padding:2px 2px">库存件数</th>
         </tr>
         <tr v-for="item in specDetailArr" >
           <td style="padding:2px 2px">
             <span style="width:100%" >{{item.name}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.val}} {{item.unit}}</span>
           </td>
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.price}}</span>
           </td>               
           <td style="padding:2px 2px">
              <span style="width:100%" >{{item.stock}}</span>
           </td>
         </tr>
       </table>
    </div>
  </div>
  
  <!--  ====== 商品详情 =====  -->
  <div class="row" style="margin:5px 0px 3px 0px;">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">宝贝详情</span>
    </div>
    <div class="row" style="width:100%;margin:1px 0px;padding:0 8px;background-color:white;">
      ${(goods.goodsDesc)!''}
    </div>
  </div> 
  
  <!-- 同类推荐 -->
  <div class="row">
    
  </div> 

</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		courselImgPaths:'${(goods.carouselImgPaths)!""}'.split(','),
		specDetailArr:JSON.parse('${(goods.specDetail)!"[]"}'),
		apprList:[],
		apprCnt:0
	},
	methods:{
		getAllAppr: function(){
			 containerVue.apprCnt = 0;
			 containerVue.goodsList = [];
			 $.ajax({
					url: '/appraise/getall/goods/${(goods.goodsId)?string("#")}',
					method:'post',
					data: {'begin':0,'pageSize':3},
					success: function(jsonRet,status,xhr){
						if(jsonRet && jsonRet.errcode == 0){//
							for(var i=0;i<jsonRet.datas.length;i++){
								var appr = jsonRet.datas[i];
								if(appr.appraiseInfo){//有评价内容
									appr.appraiseInfo = JSON.parse(appr.appraiseInfo);
								}else{
									appr.appraiseInfo = {'time':appr.appraiseTime,'content':"卖家太懒，啥也没留下！！！"}
								}
								appr.headimgurl = startWith(appr.headimgurl,'http')?appr.headimgurl:('/user/headimg/show/'+appr.userId)
								containerVue.apprList.push(appr);
							}
							containerVue.apprCnt = jsonRet.pageCond.count;
						}
					},
					dataType: 'json'
				});			 
		 },
		 addCollection: function(collType,targetId){
			$.ajax({
				url: '/collection/add/'+collType + '/' + targetId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode !==0){
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统失败！');
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getAllAppr();
</script>

<footer >
  <div class="row" style="margin:50px 0"></div>
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px">
    	<a href="/shop/index" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/首页.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">商城首页</p>
	</a>
    <a href="/partner/mcht/${(goods.partnerId)?string('#')}" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/商家.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">逛商家</p>
    </a> 	
    <a href="javascript:;" onclick="containerVue.addCollection('2','${(goods.goodsId)?string('#')}')" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/收藏.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">加入收藏</p>
     </a>
   
     <a href="/order/place/${(goods.goodsId)?string('#')}" class="weui-tabbar__item " style="background-color:red;text-align:center;">
        <p><p class="weui-tabbar__label" style="font-size:20px;color:white;vertical-align:center;">立即下单</p>
     </a>     	
  </div>
</footer>
</#if>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>