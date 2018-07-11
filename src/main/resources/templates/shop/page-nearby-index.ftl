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
    
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.6&key=2b12c05334ea645bd934b55c8e46f6ea"></script>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main.css"/>
</head>
<body class="light-gray-bg" >

<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container goods-container" id="container" style="padding:0 1px;overflow:scroll">
  <header >
    <#include "/menu/page-category-menu.ftl" encoding="utf8"> 
  </header>
  <div class="col-xs-12">
   <span>您的当前收货位置：{{param.province}}{{param.city}}{{param.area}}{{param.town}}</span>
   <a href="javascript:;" @click="showMap"><img alt="" src="/icons/收货地址.png" width=20px height=20px></a>
  </div>
  <div class="row" style="margin:0 0;">
    <div v-for="goods in goodsList" class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:3px 2px;">
        <div class="row" style="margin:1px 1px;padding:2px 10px;background-color:white;text-align:center;vertical-align:center" >
          <div class="row" style="margin:0">
            <a v-bind:href="'/shop/mcht/' + goods.partnerId">
	          <img class="pull-left" alt="" :src="'/shop/pcert/logo/' + goods.partnerId" style="width:25px;height:25px;border-radius:30%">
	        </a>
	        <span class="pull-right">约{{goods.partner.distance}}km</span>
	      </div>
	      <div class="row" style="margin:0">
	        <span class="pull-right">{{goods.partner.area}}{{getShortAddr(goods.partner.addr)}}</span>
	      </div>
        </div>
	    <div style="margin:2px 1px;background-color:white;text-align:center;vertical-align:center" >
	      <a v-bind:href="'/shop/goods/' + goods.goodsId">
	        <img alt="" :src="'/shop/gimage/' + goods.partnerId + '/' + goods.mainImgPath" style="max-height:160px;max-width:100%">
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
	      <div class="row" style="margin:1px 0px 2px 0;padding:0 5px 3px 5px;background-color:white;text-align:center" >
	        <a class="btn btn-danger " style="padding:3px 5px" :href="'/order/place/'+ goods.goodsId"><span style="color:white">立即下单</span></a>
	        <a class="btn btn-primary" style="padding:3px 5px" href="javascript:;" @click="addCollection('2',goods.goodsId)"><span style="color:white">加入收藏</span></a>
	      </div>
	    </div>
    </div>
  </div>
  <div class="row" style="margin:10px 20px 80px 20px;">
      <p style="color:gray">没有更多数据了....</p>
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
		 if(isRefresh){ //清空数据
			 containerVue.goodsCnt = 0;
			 containerVue.goodsList = [];
		 }else{
			 if(containerVue.goodsList.lenght>=300){
				 if(isFirst){//清除后一百条
					 containerVue.goodsList.splice(200,100);
				 }else{
					 containerVue.goodsList.splice(0,100); 
				 }
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
					}
					$("#loadingData").hide();
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});			 
	 },
	 showMap: function(){
			$('#showAddrMap').show();
	},
	 getLocation: function(province,city,area,town,lng,lat){
			this.param.province = province;
			this.param.city = city;
			this.param.area = area;
			this.param.town = town;
			this.param.lng = lng;
			this.param.lat = lat;
			this.param.canUpdAdd = true;
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
     if (r>0 && r < 0.2) {//上拉后翻页 
    	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
    	 	containerVue.getAll(false,false);
     }
     if(scrollHeight<0){ //下拉前翻页
    	 var currPageCnt = containerVue.goodsList.length%containerVue.param.pageSize;//当前页的数量
 		if(currPageCnt == 0){
 			currPageCnt = containerVue.param.pageSize;
 		}
	 	containerVue.param.begin = containerVue.param.begin - (containerVue.goodsList.length-currPageCnt);//总数据的开始
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


<#if !(receiverPosition.province)??>
<script>
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
    			containerVue.getLocation(result.addressComponent.province,result.addressComponent.city,result.addressComponent.district,
    			result.addressComponent.township,result.position.lng,result.position.lat);
    			//系统业务调用
    			containerVue.getAll(true,false);
		}
    });
});
</script>
<div id="showAddrMap" style="position:fixed;left:0;top:0;right:0;bottom:0;margin:0;width:100%;display:none;z-index:1000;background:rgba(0,0,0,0.2);display:none;">
<div id="mapContainer" style="top:10px;width:100%;height:500px;"></div>
<div id="myPageTop" style="left:10px">
    <table style="width:100%;text-align:right">
        <tr style="width:100%">
            <td class="column1"><label><a onclick="$('#showAddrMap').hide();">关闭</a></label></td>
        </tr>
        <tr>
            <td class="column1"><input type="text" style="width:90%" readonly id="lnglat" placeholder="点击地图选择地点"></td>
        </tr>		        
        <tr>
            <td class="column1"><input type="text" id="keyword" name="keyword" value="请输入关键字：(选定后搜索)" style="width:90%" onfocus='this.value=""'/></td> 
        </tr>
        
    </table>
</div>
<script type="text/javascript">
var windowsArr = [];
   var marker = [];
    var map = new AMap.Map("mapContainer", {
        resizeEnable: true,
        zoom: 13,
    });
    AMap.plugin('AMap.Geocoder',function(){
        var geocoder = new AMap.Geocoder({
            city: "010"//城市，默认：“全国”
        });
        var marker = new AMap.Marker({
            map:map,
            bubble:true
        })
        map.on('click',function(e){
            marker.setPosition(e.lnglat);
            geocoder.getAddress(e.lnglat,function(status,result){
              if(status=='complete'){
                var addr = result.regeocode;
                containerVue.getLocation(addr.addressComponent.province,addr.addressComponent.city,
                		addr.addressComponent.district,addr.addressComponent.township,e.lnglat.getLng(),e.lnglat.getLat());
                document.getElementById("lnglat").value = addr.formattedAddress;
              }else{
                 alertMsg('系统提示','无法获取地址');
              }
            });
        })
    });
     AMap.plugin(['AMap.Autocomplete','AMap.PlaceSearch'],function(){
        var autoOptions = {
          city: "昆明", //城市，默认全国
          input: "keyword",//使用联想输入的input的id
          
        };
        autocomplete= new AMap.Autocomplete(autoOptions);
        var placeSearch = new AMap.PlaceSearch({
              city:'昆明',
              map:map
        });
        AMap.event.addListener(autocomplete, "select", function(e){
           //TODO 针对选中的poi实现自己的功能
           placeSearch.setCity(e.poi.adcode);
           placeSearch.search(e.poi.name)
        });
      }); 
   
</script>
</div>
<script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
<script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
<#else>
<script>
	containerVue.getAll(true,false);
</script>
</#if>

<footer >
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
