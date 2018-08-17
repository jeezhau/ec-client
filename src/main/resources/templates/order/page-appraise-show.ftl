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
     <h3 class="row" style="margin:5px 0;text-align:center" >商品评价({{param.count}})</h3>
   </div>
   <div class="row" style="margin:1px 0px;">
     <div class="row" v-for="order in apprList" style="margin:1px 0;background-color:white;">
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
		apprList:[],
		param:{
			objNm: '${objNm}',
			objId: '${objId?string("#")}',
			begin:0,
			pageSize:100,
			count:0
		}
	},
	methods:{
		getAllAppr: function(isRefresh,isFirst){//是否刷新，是否从前面插入
			 $("#loadingData").show();
			 if(isRefresh){ //清空数据
			 	this.param.count = 0;
			 	this.apprList = [];
			 }
			 if(this.dataList.lenght >= 10*this.param.pageSize){
				if(isFirst){//清除最有一页
					this.dataList.splice(9*this.param.pageSize,this.param.pageSize);
				}else{//清除最前一页
					this.dataList.splice(0,this.param.pageSize); 
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
									containerVue.apprList.unshift(appr);
								}else{
									containerVue.apprList.push(appr);
								}
								i++;j--;
							}
							containerVue.param.pageSize = jsonRet.pageCond.pageSize;
							containerVue.param.begin = jsonRet.pageCond.begin;
							containerVue.param.count = jsonRet.pageCond.count;
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

//分页初始化
scrollPager(containerVue.param,containerVue.apprList,containerVue.getAllAppr) 

</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
