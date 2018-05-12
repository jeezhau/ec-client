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
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<div class="container" id="container" style="padding:0;oveflow:scroll">
<#if (mcht.partnerId)??>  
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
      <span>法人代表：${mcht.legalPername} &nbsp;&nbsp;&nbsp;&nbsp; 业务联系：${mcht.phone}</span><br>
      <p>经营介绍：<br>&nbsp;&nbsp;&nbsp;&nbsp;${mcht.introduce}</p>
    </div>
   </div>
  </div> 
  
  <!--  ====== 买家评价 ======= -->
  <div class="row" style="margin:8px 0px 3px 0px;" >
    <div class="row" style="margin:1px 0px;background-color:white;">
      <span class="pull-left" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">买家评价({{apprCnt}})</span>
      <span class="pull-right" style="padding:0 10px;font-weight:bolder;font-size:120%;color:gray">
        <a v-if="apprCnt != 0 " href="/appraise/mcht/show/${mcht.partnerId}">查看全部&gt;</a>
        <a v-if="apprCnt == 0 " href="javascript:;">查看全部&gt;</a>
      </span>
    </div>
    <div class="row" v-for="appr in apprList" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left">
         <img alt="头像" :src="'/images/mfyx_logo.jpeg' + appr.headimgurl" width="20px" height="20px" style="border-radius:50%">{{appr.nickname}}
       </span>
       <span class="pull-right">{{appr.createTime}}</span>
     </div>
     <div class="row">
       {{appr.appraiseInfo}}
     </div>
    </div>
  </div>
 
  <!-- 售卖货架 -->
  <div class="row" style="margin:5px 0px 3px 0px;background-color:white;color:red">
    <div class="col-xs-12">
     <h3 style="text-align:center;padding:5px">所有热卖商品</h3>
    </div>
    <div v-for="goods in goodsList" class="col-xs-6" style="padding:0px 0px;">
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/goods/show/' + goods.goodsId">
	        <img alt="" :src="'/image/file/show/' + goods.mainImgPath" style="width:90%;height:150px">
	      </a>
	    </div>
	    <div style="margin:1px 1px;" >
	      <div style="margin:1px 0;padding:0 5px;background-color:white" >
		        {{goods.goodsName}}
	      </div>
	      <div style="margin:1px 0px;padding:0 5px;background-color:white;color:red" >
	      	<span class="pull-left ">惠¥: <span>{{goods.priceLowest}}</span>元</span>
	      	<span class="pull-right ">库存: <span>{{goods.stockSum}}</span>件</span>
	      </div>
	      <div style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 12px" :href="'/order/place/'+ goods.goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 12px" :href="'/order/order/begin/' + goods.goodsId"><span style="color:white">加入收藏</span></a>
	      </div>
	    </div>
    </div>
  </div>
  
</#if>
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
						if(jsonRet ){
							if(jsonRet.errcode == 0){//
								for(var i=0;i<jsonRet.datas.length;i++){
									var appr = jsonRet.datas[i];
									if(appr.appraiseInfo){
										appr.appraiseInfo = JSON.parse(appr.appraiseInfo);
									}
								}
								containerVue.apprCnt = jsonRet.pageCond.count;
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}else{
							alertMsg('错误提示','获取数据失败！')
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

<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
