<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#if (partner.partnerId)??>
<div class="container" id="container" style="oveflow:scroll">
  
  <!-- 商家信息 -->
  <div v-for="kf in dataList" class="col-xs-12 col-sm-6 col-md-6 col-lg-4" style="margin:5px 0px 3px 0px;background-color:white;padding:3px 8px;">
     <div class="thumbnail">
        <img :src="'/shop/kfqrcode/${(partner.partnerId)?string('#')}/show/' + kf.userId"  alt="客服二维码">
        <div class="caption">
           <h3>客服人员: {{kf.nickname}}</h3>
           <p>请扫描二维码加客服人员好友，并与其交流，务必保留交流详情截图，以便需要时作为投诉的证据！</p>
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
		getAllKF: function(){
		 	containerVue.dataList = [];
		 	$.ajax({
				url: '/shop/kfstaff/getall/${(partner.partnerId)?string("#")}?tagId=${tagId!''}',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){//
						for(var i=0;i<jsonRet.datas.length;i++){
							containerVue.dataList.push(jsonRet.datas[i]);
						}
					}
				},
				dataType: 'json'
			});			 
		 }
	}
});
containerVue.getAllKF();
</script>

<footer >
 <#if (tagId!'') == ''>
  <div class="row" style="margin:50px 0"></div>
  <div class="weui-tabbar" style="position:fixed;left:0px;bottom:0px">
    	<a href="/shop/index" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/首页.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">商城首页</p>
	</a>
    <a href="/shop/mcht/${(partner.partnerId)?string('#')}" class="weui-tabbar__item " >
	    <span style="display: inline-block;position: relative;">
	        <img src="/icons/商家.png" alt="" class="weui-tabbar__icon">
	    </span>
	    <p class="weui-tabbar__label">进入商家</p>
    </a>      	
  </div>
 <#else>
   <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
 </#if>
</footer>
</#if>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>