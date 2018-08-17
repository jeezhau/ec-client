<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<div class="container " id="container" style="oveflow:scroll">
   <div class="row">
     <a class="col-xs-2" href="/complain/manage" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >我的投诉</h3>
   </div>
   <div class="row">
	 <div v-for="item in dataList" class="row" style="margin:1px 0px;padding:0 3px;background-color:white;">
	    <div class="row" style="margin:0;padding:0 3px">
	      <span class="pull-left">{{item.orderId}}</span>
	      <span class="pull-right">{{getCpStatus(item.status)}}</span>
	    </div>
	    <div class="row" style="margin:0;padding:0 3px">
	      <p>投诉时间：{{item.createTime}}</p>
	    </div>
	    <div class="row" style="margin:0;padding:0 3px">
	      <p>投诉内容：{{item.content}}</p>
	    </div>
	    <div v-if="item.dealLog && item.dealLog.length>0" class="row" style="margin:0;padding:0 3px;color:gray">
		    <div class="row" style="margin:1px 0px;background-color:white;">
		      <span class="pull-left" style="padding:0 10px;font-weight:bolder;color:gray">处理结果</span>
		    </div>
		    <div v-for="deal in JSON.parse(item.dealLog)" v-if="deal && deal.time" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
		     <div class="row">
		       <span class="pull-left">{{deal.time}}</span>
		     </div>
		     <div class="row">
		       <p>{{deal.result}}</p>
		     </div>
		    </div>
	    </div>
	    <div class="col-xs-12" style="padding:0 10px;text-align:right">
	       <a v-if="item.status=='0'" class="btn btn-primary" :href="'/complain/manage?oprFlag=S&cplanId=' + item.cplanId" style="padding:0px 8px"> 修 改 </a>
	       <a class="btn btn-danger" :href="'/complain/manage?oprFlag=D&cplanId=' + item.cplanId" style="padding:0px 8px"> 删 除 </a>
	    </div>
	  </div>
  </div>  
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		dataList:[],
		pageCond:{
			begin:0,
			pageSize:30,
			count:0
		}
	},
	methods:{
		getCpStatus:function(code){
			if(code =='0'){
				return '待处理';
			}else if(code =='1'){
				return '待回访';
			}else if(code =='2'){
				return '完成';
			}
			return '其他';
		},
		getComplains :function(isRefresh,isForward){
			var url = '/complain/getall';
			var searchParam = {
				'begin':containerVue.pageCond.begin,
				'pageSize':containerVue.pageCond.pageSize
			};
			getAllData(isRefresh,isForward,url,searchParam,containerVue.dataList,containerVue.pageCond);
		}
	}
});
containerVue.getComplains(true,false);

//分页初始化
scrollPager(containerVue.pageCond,containerVue.dataList,containerVue.getComplains);

</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
