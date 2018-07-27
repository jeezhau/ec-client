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
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#if (goods.goodsId)??>
<div class="container goods-container" id="container" style="padding:0;">

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
            <img :src="'/shop/gimage/${((goods.partnerId)!'')?string('#')}/' + imgpath"  style="max-width:100%;max-height:600px">
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#myCarousel" data-slide="prev"><span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
    <a class="carousel-control right" href="#myCarousel" data-slide="next"><span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
  </div>
  
  <!-- 售卖信息 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;color:red">
    <div class="col-xs-4" style="padding-right:3px;">最低价(¥)：<span>${(goods.priceLowest)!''}</span></div>
    <div class="col-xs-4" style="padding:0 3px;">总库存：<span>${(goods.stockSum)!''}</span></div>
    <div class="col-xs-4" style="padding-left:3px;">已售：<span style="color:red">${(goods.saledCnt)!''}</span></div>
  </div>
  
  <!-- 商家信息 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;padding:3px 8px;">
    <a href="javascript:;">
     <img class="pull-left" alt="" src="/partner/cert/show/logo/${(goods.partnerId)?string('#')}" style="width:25px;height:25px;border-radius:30%">
    </a>
   <span class="pull-right">${(goods.partner.province)!''}-${(goods.partner.area)!''}-${(goods.partner.addr)!''}</span>
  </div>

  <!-- 服务特点 -->
  <div class="row" style="margin:5px 0px 3px 0px;font-weight:lighter;font-size:80%">
    <div class="col-xs-3" style="padding:0 3px;text-align:center"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">正品保证</span></div>
    <div class="col-xs-3" style="padding:0 3px;text-align:center"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">同城急速</span></div>
    <#if goods.refundLimit==0>
    <div class="col-xs-3" style="padding:0 3px;text-align:center"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">不支持无理由退换货</span></div>
    </#if>
    <#if (goods.refundLimit gt 0) >
    <div class="col-xs-3" style="padding:0 3px;text-align:center"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">${goods.refundLimit}天无品质问题退换货</span></div>
    </#if>
    <div class="col-xs-3" style="padding:0 3px;text-align:center"><span ><img alt="" src="/icons/正确.png" width="15px" height="15px">极速发货</span></div>
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
  
  <#if (mode!'') == 'review'>
  <div class="row" id="reviewForm" style="margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
  	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
  	<h5 style="text-align:center">商品信息审核与抽查</h5>
  	<div class="form-group">
        <label  class="col-xs-3 control-label">审核意见<span style="color:red">*</span></label>
        <div class="col-xs-9">
          <textarea class="form-control" v-model="param.review" placeholder="请输入审核意见说明" rows="5" maxLength=600></textarea>
        </div>
    </div>
    <div class="form-group" style="text-align:center">
          <button type="button" class="btn btn-info" @click="review('S')" style="margin:20px">&nbsp;&nbsp;通 过&nbsp;&nbsp;</button>
          <button type="button" class="btn btn-warning" @click="review('R')" style="margin:20px">&nbsp;&nbsp;拒 绝&nbsp;&nbsp; </button>
    </div>
    </form>
  </div>
  </#if> 

</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		courselImgPaths:'${(goods.carouselImgPaths)!""}'.split(','),
		specDetailArr:JSON.parse('${(goods.specDetail)!"[]"}'),
		apprList:[],
		apprCnt:0,
		<#if (mode!'') == 'review'>
		param:{
			goodsId:'${(goods.goodsId)?string("#")}',
			review:'',
			result:''
		}
		</#if>
	},
	methods:{
		<#if (mode!'') == 'review'>
		review: function(result){
			$("#dealingData").show();
			this.param.result = result;
			if(!this.param.review || this.param.review.length<2 || this.param.review.length>600){
				alertMsg('错误提示','审核意见：长度为2-600字符！');
				return;
			}
			$.ajax({
				url: '/review/submit/goods',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"信息提交成功！");
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		},
		</#if>
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
									appr.appraiseInfo = [{'time':appr.appraiseTime,'content':"买家太懒，啥也没留下！！！"}];
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

//获取界面上轮播图容器
var $carousels = $('#myCarousel');
var startX,endX;
// 在滑动的一定范围内，才切换图片
var offset = 50;
// 注册滑动事件
$carousels.on('touchstart',function (e) {
    // 手指触摸开始时记录一下手指所在的坐标x
    startX = e.originalEvent.touches[0].clientX;

});
$carousels.on('touchmove',function (e) {
    // 目的是：记录手指离开屏幕一瞬间的位置 ，用move事件重复赋值
    endX = e.originalEvent.touches[0].clientX;
});
$carousels.on('touchend',function (e) {
    //console.log(endX);
    //结束触摸一瞬间记录手指最后所在坐标x的位置 endX
    //比较endX与startX的大小，并获取每次运动的距离，当距离大于一定值时认为是有方向的变化
    var distance = Math.abs(startX - endX);
    if (distance > offset){
        //说明有方向的变化
        //根据获得的方向 判断是上一张还是下一张出现
        $(this).carousel(startX > endX ? 'next':'prev');
    }
});

</script>

<#include "/menu/page-partner-func-menu.ftl" encoding="utf8">
</#if>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>