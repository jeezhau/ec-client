<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
	<title>摩放优选 我的收藏</title>
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
    
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container " id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row" style="margin:0">
    <div v-for="item in dataList"  class="col-xs-6 col-sm-6 col-md-4 col-lg-4" style="padding:3px;">
	  <div v-if="item.collType == '2'" class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
	     <div class="col-xs-12" style="text-align:center;background-color:white;">
	       <a class="pull-left" :href="'/shop/mcht/'+item.goodsPartnerId">
		      <img alt="头像" :src="'/shop/pcert/logo/'+ item.goodsPartnerId" width="20px" height="20px" style="border-radius:50%"> 
		      <span>{{item.partnerBusiName}}</span>
		   </a>
	     </div>
	      <div class="col-xs-12" style="text-align:center;">
	        <div><span>{{item.goodsName}}</span></div>
	        <div>
	          <a :href="'/shop/goods/' + item.relId">
	           <img alt="" :src="'/shop/gimage/' + item.goodsPartnerId + '/' + item.goodsMainImg" style="max-width:200px;max-height:180px">
	          </a>
	        </div>
	      </div>
	      <div class="col-xs-12" style="text-align:center;padding:3px;background-color:white;">
	        <a class="btn btn-danger pull-right" style="padding:3px 3px" :href="'/order/place/'+ item.relId"> 立即下单 </a>
	        <button class="btn btn-primary pull-left" style="padding:3px 3px" @click="deleteColl(item)"> &nbsp;删 除 &nbsp;</button>
	      </div>
	   </div>
	   <div v-if="item.collType == '1'" class="row" style="margin:5px 1px ;padding:3px 0;background-color:white" >
	     <div class="col-xs-12" style="text-align:center;background-color:white;">
	       <a class="pull-left" :href="'/partner/mcht/'+item.goodsPartnerId">
		      <img alt="头像" :src="'/partner/cert/show/logo/'+ item.goodsPartnerId" width="50px" height="50px" style="border-radius:50%"> 
		      <span>{{item.partnerBusiName}}</span>
		   </a>
	     </div>
	      <div class="col-xs-12" >
	        <p>{{item.partnerIntroduce}}</p>
	      </div>
	      <div class="col-xs-12" style="text-align:center;padding:3px;background-color:white;">
	        <button class="btn btn-primary pull-left" style="padding:3px 3px" @click="deleteColl(item)"> &nbsp; &nbsp;删 除 &nbsp; &nbsp; </button>
	      </div>
	   </div>
	   
	 </div>
  </div> 
  

</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		dataList:[]
	},
	methods:{
		getAll : function(){
			$("#loadingData").show();
			this.dataList = [];
			$.ajax({
				url: '/collection/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					$("#loadingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 === jsonRet.errcode){
							containerVue.receiverList = [];
							for(var i=0;i<jsonRet.datas.length;i++){
								containerVue.dataList.push(jsonRet.datas[i]);
							}
						}else{//出现逻辑错误
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								//alertMsg('错误提示',jsonRet.errmsg);
							}
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				failure:function(){
					$("#loadingData").hide();
				},
				dataType: 'json'
			});
		},
		deleteColl: function(coll){
			if(!confirm("您确定要删除该条收藏信息？")){
				return;
			}
			$.ajax({
				url: '/collection/delete/' + coll.collType + "/" + coll.relId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errmsg){
						if(0 === jsonRet.errcode){
							containerVue.getAll();
						}else{//出现逻辑错误
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getAll();
</script>



<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
