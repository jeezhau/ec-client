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
<#if (mcht.partnerId)??>  
<div class="container" id="container" style="padding:0;oveflow:scroll">
  <!-- 商家基础信息 -->
  <div class="row" style="margin:0 0px;padding:0;font-weight:bold;background-color:white;">
   <div class="col-xs-12" style="padding:3px 0;">
     <div class="col-xs-4" style="text-align:center;background-color:gray">
      <img alt="" src="/partner/cert/show/logo/${(mcht.partnerId)?string('#')}" width="50px" height=50px style="border-radius:30%">
      <br>
      <span>${mcht.busiName}</span>
     </div>
     <div class="col-xs-4" style="text-align:center;">
       <div style="">
        <span>品质：10</span><br>
        <span>物流：10</span><br>
        <span>服务：10</span><br>
       </div>
     </div>
     <div class="col-xs-4" style="text-align:center;background-color:green">
      <img alt="" src="/partner/cert/show/logo/${(mcht.partnerId)?string('#')}" width="50px" height=50px style="border-radius:30%">
      <br>
      <span>${mcht.busiName}</span>
     </div>    
   </div>
   <div class="col-xs-12">
    <div class="row" style="padding:3px">
      <span>商家名称：${mcht.compName}</span> <br>
      <span>商家地址：${mcht.province} ${mcht.city} ${mcht.area} ${mcht.addr}</span><br>
      <p>经营介绍：<br>&nbsp;&nbsp;&nbsp;&nbsp;${mcht.introduce}</p>
    </div>
   </div>
  </div> 
  
  <!--  ====== 前三条买家评价 ======= -->
  <div class="row" style="margin:8px 0px 3px 0px;" onclick="">
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家评价({{apprCnt}})</span>
      <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">
        <a v-if="apprCnt>0" href="/appraise/show/partner/${(mcht.partnerId)?string('#')}">查看全部&gt;</a>
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
 
  <!-- 售卖货架 -->
  <div class="row" style="margin:5px 0px 3px 0px;">
    <div class="col-xs-12">
     <h3 style="text-align:center;padding:5px">所有热卖商品</h3>
    </div>
    <div class="row" style="margin:3px 0">
      <div v-for="goods in goodsList" class="col-xs-6" style="padding:3px 2px ;">
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/goods/show/' + goods.goodsId">
	        <img alt="" :src="'/image/file/show/' + goods.partner.vipId + '/' + goods.mainImgPath" style="width:90%;height:150px">
	      </a>
	    </div>
	    <div class="row" style="margin:1px 1px;" >
	      <div style="margin:1px 0;padding:0 5px;background-color:white" >
		        {{goods.goodsName}}
	      </div>
	      <div class="row" style="margin:1px 0px;padding:0 5px;background-color:white;color:red" >
	      	<span class="pull-left ">惠¥: <span>{{goods.priceLowest}}</span>元</span>
	      	<span class="pull-right ">库存: <span>{{goods.stockSum}}</span>件</span>
	      </div>
	      <div class="row" style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 5px" :href="'/order/place/'+ goods.goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 5px" :href="'/order/order/begin/' + goods.goodsId"><span style="color:white">加入收藏</span></a>
	      </div>
	    </div>
	  </div>
    </div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		apprCnt:0,
		apprList:[],
		goodsList:[],
	},
	methods:{
		getAllGoods: function(){
			 containerVue.goodsList = [];
			 $.ajax({
					url: '/shop/mcht/getall/${mcht.partnerId}',
					method:'post',
					data: {},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.errcode == 0){//
								for(var i=0;i<jsonRet.datas.length;i++){
									containerVue.goodsList.push(jsonRet.datas[i]);
								}
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}else{
							alertMsg('错误提示','获取数据失败！')
						}
					},
					dataType: 'json'
				});			 
		 },
		 getAllAppr: function(){
			 containerVue.goodsList = [];
			 $.ajax({
					url: '/appraise/getall/partner/${mcht.partnerId}',
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
		 }
	}
});
containerVue.getAllGoods();
containerVue.getAllAppr();
</script>
</#if>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
