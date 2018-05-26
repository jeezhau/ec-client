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
    
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.6&key=2b12c05334ea645bd934b55c8e46f6ea"></script> 
</head>
<body class="light-gray-bg" >

<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container goods-container" id="container" style="padding:0 1px;overflow:scroll">
  <header >
    <#include "/menu/page-category-menu.ftl" encoding="utf8"> 
  </header>
  <div class="col-xs-12">
   <span>您的当前位置：{{param.province}}{{param.city}}{{param.area}}{{param.town}}</span>
  </div>
  <div class="row" style="margin:0 0;">
    <div v-for="goods in goodsList" class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:3px 2px;">
        <div class="row" style="margin:1px 1px;padding:2px 10px;background-color:white;text-align:center;vertical-align:center" >
          <div class="row" style="margin:0">
            <a v-bind:href="'/partner/mcht/' + goods.partnerId">
	          <img class="pull-left" alt="" :src="'/partner/cert/show/logo/' + goods.partnerId" style="width:25px;height:25px;border-radius:30%">
	        </a>
	        <span class="pull-right">约{{goods.partner.distance}}km</span>
	      </div>
	      <div class="row" style="margin:0">
	        <span class="pull-right">{{goods.partner.area}}{{getShortAddr(goods.partner.addr)}}</span>
	      </div>
        </div>
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/goods/detail/' + goods.goodsId">
	        <img alt="" :src="'/image/file/show/' + goods.partner.vipId + '/' + goods.mainImgPath" style="width:90%;max-width:200px;height:150px">
	      </a>
	    </div>
	    <div style="margin:1px 1px;" >
	      <div style="margin:1px 0;padding:0 5px;background-color:white" >
		        {{goods.goodsName}}
	      </div>
	      <div style="margin:1px 0px;padding:0 5px;background-color:white;color:red" >
	      	<span class="pull-left ">售价: <span>{{goods.priceLowest}}</span>元</span>
	      	<span class="pull-right ">销量: <span>{{goods.saledCnt}}</span>件</span>
	      </div>
	      <div style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 12px" :href="'/order/place/'+ goods.goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 12px" href="javascript:;" @click="addCollection('2',goods.goodsId)"><span style="color:white">加入收藏</span></a>
	      </div>
	    </div>
    </div>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
 el:'#container',
 data:{
	param:{
		province:'${(receiverPosition.province)!''}',
		city:'${(receiverPosition.city)!''}',
		area:'${(receiverPosition.area)!''}',
		town:'${(receiverPosition.town)!''}',
		lat:'${(receiverPosition.lat)!''}',
		lng:'${(receiverPosition.lng)!''}',
		categoryId:'', 
		keywords:'',
		pageSize:20,
		begin:0
	},
	goodsList:[] ,
	goodsCnt:0,
 },
 methods:{
	 getShortAddr(addr){
		 if(addr.length>11){
			 return addr.substring(0,8) + '...';
		 }else{
			 return addr;
		 }
	 },
	 getAll: function(isRefresh,isFirst){
		 if(!this.param.city || !this.param.lat ||!this.param.lng){
			 alertMsg('系统提示',"因系统无法获取您的当前位置信息，该服务暂无法提供！");
			 return;
		 }
		 $("#loadingData").show();
		 $("#nomoreData").hide();
		 if(containerVue.goodsList.length>100){
		 	containerVue.goodsList = [];
		 }
		 if(isRefresh){ //清空数据
			 containerVue.goodsCnt = 0;
			 containerVue.goodsList = [];
		 }
		 if(containerVue.goodsList.lenght>=300){
			 if(isFirst){//清除后一百条
				 containerVue.goodsList.splice(200,100);
			 }else{
				 containerVue.goodsList.splice(0,100); 
			 }
		 }
		 $.ajax({
				url: '/nearby/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errcode == 0){//
						var i=0;
						var j = jsonRet.datas.length;
						for(;i<jsonRet.datas.length;){
							if(isFirst){
								containerVue.goodsList.unshift(jsonRet.datas[j]);
							}else{
								containerVue.goodsList.push(jsonRet.datas[i]);
							}
							i++;j--;
						}
						containerVue.param.pageSize = jsonRet.pageCond.pageSize;
						containerVue.param.begin = jsonRet.pageCond.begin;
					}else{
						//alert(jsonRet.errmsg);
						$("#nomoreData").show();
					}
					$("#loadingData").hide();
				},
				failure:function(){
					$("#loadingData").hide();
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

 //分类查询
 function getGoodsByCat(categoryId){
	 containerVue.goodsList = [];
	 containerVue.param.categoryId = categoryId;
	 containerVue.param.begin = 0;
	 containerVue.getAll(true,false);
 }
 function getGoodsByKey(keywords){
	 if(keywords){
		 containerVue.goodsList = [];
		 containerVue.param.keywords = keywords;
		 containerVue.param.begin = 0;
		 containerVue.getAll(true,false);
	 }
 } 
 
 var winHeight = $(window).height(); //页面可视区域高度   
 var scrollHandler = function () {  
     var pageHieght = $(document.body).height();  
     var scrollHeight = $(window).scrollTop(); //滚动条top   
     var r = (pageHieght - winHeight - scrollHeight) / winHeight;
     if (r < 0.5) {//上拉翻页 
    	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
    	 	containerVue.getAll(false,false);
     }
     if(scrollHeight<0){ //下拉翻页
    	 	var cnt = containerVue.goodsList.length%containerVue.param.pageSize;
 		cnt = containerVue.goodsList - cnt;
	 	containerVue.param.begin = containerVue.param.begin - cnt;
    	 	containerVue.param.begin = containerVue.param.begin - containerVue.param.pageSize;
     	if(containerVue.param.begin <= 0){
     		containerVue.param.begin = 0;
     		containerVue.getAll(true,true);
     	}else{
    	 		containerVue.getAll(false,true);
     	}
     }
 }  
 //定义鼠标滚动事件  
 $("#container").scroll(scrollHandler); 
 
</script> 

<script>
<#if !(receiverPosition.province)??>
var mapObj = new AMap.Map('iCenter');
mapObj.plugin('AMap.Geolocation', function () {
    geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,//是否使用高精度定位，默认:true
        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
        maximumAge: 0,           //定位结果缓存0毫秒，默认：0
        convert: true,           //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
        showButton: true,        //显示定位按钮，默认：true
        buttonPosition: 'LB',    //定位按钮停靠位置，默认：'LB'，左下角
        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        showMarker: true,        //定位成功后在定位到的位置显示点标记，默认：true
        showCircle: true,        //定位成功后用圆圈表示定位精度范围，默认：true
        panToLocation: true,     //定位成功后将定位到的位置作为地图中心点，默认：true
        zoomToAccuracy:true      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
    });
    mapObj.addControl(geolocation);
    geolocation.getCurrentPosition(function(status,result){
    		if(status == 'complete'){
    			containerVue.param.province = result.addressComponent.province;
    			containerVue.param.city = result.addressComponent.city;
    			containerVue.param.area = result.addressComponent.district;
    			containerVue.param.town = result.addressComponent.township;
    			containerVue.param.lat = result.position.lat;
    			containerVue.param.lng = result.position.lng;
    			//系统业务调用
    			containerVue.getAll(true,false);
		}
    });
    //AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
   //AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
});
<#else>
	containerVue.getAll(true,false);
</#if>
</script>


<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>
