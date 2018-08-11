<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8"> 

<div class="container " id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row" style="margin:5px">
    <div class="form-group">
        <div class="col-xs-10" style="padding:0">
           <div class="col-xs-6" style="padding:0">
             <input class="form-control" type="date" v-model="param.beginCrtTime" placeholder="创建开始日期" >
           </div>
           <div class="col-xs-6" style="padding:0">
             <input class="form-control" type="date" v-model="param.endCrtTime" placeholder="创建结束日期" >
           </div>
        </div>
        <div class="col-xs-2" style="padding:0">
          <button class="btn btn-info" @click="searchFlows">🔍</button>
        </div>
      </div>
  </div>
  <div class="row" style="margin:0px;padding:5px 8px;">
	<div v-for="flow in flowList" class="row" style="margin-top:3px;background-color:white">
	 <div class="col-xs-12">
	   <label class="col-xs-3" style="padding:0">资金变动类型</label>
	   <span class="col-xs-9" style="padding:0">{{getChangeFlowType(flow.changeType)}}</span>
	 </div>
	 <div class="col-xs-12">
	   <label class="col-xs-3" style="padding:0">相关金额(分)</label>
	   <span class="col-xs-9" style="padding:0">{{flow.amount}}</span>
	 </div>
	 <div class="col-xs-12">
	   <label class="col-xs-3"  style="padding:0">创建时间</label>
	   <span class="col-xs-9"  style="padding:0">{{flow.createTime}}</span>
	 </div>	 
	 <div class="col-xs-12">
	   <label class="col-xs-3" style="padding:0">资金变动缘由</label>
	   <span class="col-xs-9" style="padding:0">{{flow.reason}}</span>
	 </div>
	 <div class="col-xs-12">
	   <label class="col-xs-3" style="padding:0">是否已经入帐</label>
	   <span class="col-xs-9" style="padding:0">{{flow.sumFlag == 'S'?'是':'否'}}</span>
	 </div>
	</div>
  </div>

</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		flowList:[],
		param:{
			beginCrtTime:null,
			endCrtTime:null
		},
		pageCond:{
			begin:0,
			pageSize:30
		}
	},
	methods:{
		searchFlows:function(){
			this.flowList = [];
			this.param.begin = 0;
			this.getAllFlow();
		},
		getAllFlow: function(isRefresh,isForward){
			var url = '/vip/flow/getall';
			var searchParam = {
				'jsonParams':JSON.stringify(this.param),
				'begin':this.pageCond.begin,
				'pageSize':this.pageCond.pageSize
			};
			getAllData(isRefresh,isForward,url,searchParam,containerVue.flowList,containerVue.pageCond);
		}
	}
});
containerVue.getAllFlow(true,false);
//分页初始化
scrollPager(containerVue.pageCond,containerVue.flowList,containerVue.getAllFlow);

</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
