<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container" id="container" style="padding:0;oveflow:scroll">
   <div class="row">
     <h3 class="row" style="margin:5px 0;text-align:center" >商品评价({{appr.apprCnt}})</h3>
   </div>
   <div class="row" style="margin:1px 0px;">
     <div class="row" v-for="order in appr.apprList" style="margin:1px 0;background-color:white;">
       <div class="row" style="margin:1px 2px;padding:3px 10px">
         <span class="pull-left"><img alt="头像" :src="order.headimgurl" width="20px" height="20px" style="border-radius:50%">{{order.nickname}}</span>
         <span class="pull-right">{{order.updateTime}}</span>
       </div>
       <#include "/common/tpl-order-buy-content-4vue.ftl" encoding="utf8">
       <div class="row" style="margin:1px 0">
         <div class="col-xs-12" v-for="sub in order.appraiseInfo">
         {{sub.time}} &nbsp;&nbsp;&nbsp;&nbsp;{{sub.content}}
         </div>
       </div>
     </div>
   </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		appr:{
			apprList:[],
			apprCnt:0,
		},
		param:{
			objNm: '${objNm}',
			objId: '${objId?string("#")}',
			begin:0,
			pageSize:100,
		}
	},
	methods:{
		getAllAppr: function(isRefresh,isFirst){//是否刷新，是否从前面插入
			 $("#loadingData").show();
			 if(isRefresh){ //清空数据
			 	containerVue.appr.apprCnt = 0;
			 	containerVue.appr.apprList = [];
			 }
			 if(containerVue.appr.apprList.lenght>=300){
				 if(isFirst){//清除后一百条
					 containerVue.appr.apprList.splice(200,100);
				 }else{
					 containerVue.appr.apprList.splice(0,100); 
				 }
			 }
			 var url = '';
			 if(this.param.objNm == 'goods'){
				 url = '/appraise/getall/goods/' + this.param.objId;
			 }else if(this.param.objNm == 'partner'){
				 url = '/appraise/getall/partner/' + this.param.objId;
			 }else{
				 return;
			 }
			 $.ajax({
					url: url,
					method:'post',
					data: this.param,
					success: function(jsonRet,status,xhr){
						 $("#loadingData").hide();
						if(jsonRet && jsonRet.errcode == 0){//
							var i=0;
						    var j=jsonRet.datas.length;
							for(;i<jsonRet.datas.length;){
								var appr = null;
								if(isFirst){
									appr = jsonRet.datas[j];
								}else{
									appr = jsonRet.datas[i];
								}
								if(appr.appraiseInfo){//有评价内容
									appr.appraiseInfo = JSON.parse(appr.appraiseInfo);
								}else{
									appr.appraiseInfo = [{'time':appr.appraiseTime,'content':"买家太懒，啥也没留下！！！"}];
								}
								appr.goodsSpec = JSON.parse(appr.goodsSpec);
								appr.headimgurl = startWith(appr.headimgurl,'http')?appr.headimgurl:('/user/headimg/show/'+appr.userId);
								if(isFirst){	
									containerVue.appr.apprList.unshift(appr);
								}else{
									containerVue.appr.apprList.push(appr);
								}
								i++;j--;
							}
							containerVue.param.begin = jsonRet.pageCond.begin;
							containerVue.appr.apprCnt = jsonRet.pageCond.count;
						}else{
							if(jsonRet && jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								//alertMsg('错误提示',jsonRet.errmsg);
							}
						}
					},
					failure:function(){
						$("#loadingData").hide();
					},
					dataType: 'json'
				});			 
		 }
	}
});
containerVue.getAllAppr(true,false);
var winHeight = $(window).height(); //页面可视区域高度   
var scrollHandler = function () {  
    var pageHieght = $(document.body).height();  
    var scrollHeight = $(window).scrollTop(); //滚动条top   
    var r = (pageHieght - winHeight - scrollHeight) / winHeight;
    if (r < 0.5) {//上拉翻页 
   	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
   	 	containerVue.getAllAppr(false,false);
    }
    if(scrollHeight<0){//下拉翻页
    		var cnt = containerVue.appr.apprList.length%containerVue.param.pageSize;
    		cnt = containerVue.appr.apprList.length - cnt;
   	 	containerVue.param.begin = containerVue.param.begin - cnt;
   	 	if(containerVue.param.begin <= 0){
   	 		containerVue.param.begin = 0;
   	 		containerVue.param.getAllAppr(true,true);
   	 	}else{
   	 		containerVue.param.getAllAppr(false,true);
   	 	}
    }
}  
//定义鼠标滚动事件  
$("#container").scroll(scrollHandler); 
</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
