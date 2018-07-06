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
    
    <script src="/script/common.js" type="text/javascript"></script>
    
</head>
<body class="light-gray-bg" style="overflow-y:scroll;overflow-x:hidden">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 

<div class="container" id="container" style="padding:0;">
  <div class="row" style="margin:0" >  <!-- 管理列表 -->
    <div class="row" style="margin:0;padding:5px 2px;background-color:white">
      <div class="col-xs-6" style="padding:5px 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.status" required>
		            <option value="0">待受理</option>
		            <option value="1">已受理</option>
		            <option value="S">提现成功</option>
		            <option value="F">提现失败</option>
	            </select>
	        </div>
	      </div>
	    </div>
	    <div class="col-xs-6" style="padding:5px 2px">
      	  <div class="form-group" style="padding:0 20px">
        		<button type="button" class="btn btn-primary" @click="getAll(true,false)">&nbsp;&nbsp;查&nbsp;&nbsp;询&nbsp;&nbsp; </button>
      	  </div>
      	</div>
    </div>
    <div class="row" style="margin:0">
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="10%" style="padding:2px 2px">账户类型</th>
           <th width="10%" style="padding:2px 2px">通道类型</th>
           <th width="25%" style="padding:2px 2px">账户名</th>
           <th width="25%" style="padding:2px 2px">申请时间</th>
           <th width="20%" style="padding:2px 2px">状态</th>
           <th width="10%" style="padding:2px 2px;text-align:center">操作</th>
         </tr>
	     <tr v-for="item in dataList" >
            <td style="padding:2px 2px">{{getAccountType(item.accountType)}}</td>
            <td style="padding:2px 2px">{{getChannelType(item.channelType)}}</td>
            <td style="padding:2px 2px">{{item.accountName}}</td>
            <td style="padding:2px 2px">{{item.applyTime}}</td>
            <td style="padding:2px 2px">{{getCashApplyStatus(item.status)}}</td>
            <td style="padding:2px 2px;text-align:center">
            	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('ComplainDeal')) >
              <a v-if="item.status=='0'" target="_blank" :href="'/pcash/show/accept/' + item.applyId">受理</a>
            </#if>
            <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('ComplainRevisit')) >
              <a v-if="item.status=='1'" target="_blank" :href="'/pcash/show/finish/' + item.applyId">完成</a>
            </#if>
            <a v-if="item.status=='S' || item.status=='F'" target="_blank" :href="'/pcash/show/detail/' + item.applyId">详情</a>
            </td>
	    </tr>
	  </table>
    </div>
  </div>

</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	 el:'#container',
	 data:{
		param:{
			status:'0',
		},
		pageCond:{
			pageSize:20,
			begin:0,
			count:0
		},
		dataList:[] 
	},
	methods:{
		getAll: function(isRefresh,isForward){
			var url = '/pcash/getall';
			var searchParam = {
				'jsonSearchParams':JSON.stringify(containerVue.param),
				'begin':containerVue.pageCond.begin,
				'pageSize':containerVue.pageCond.pageSize
			};
			getAllData(isRefresh,isForward,url,searchParam,containerVue.dataList,containerVue.pageCond);
		}
	}
});
//分页初始化
scrollPager('container',containerVue.pageCond,containerVue.dataList,containerVue.getAll) 

</script> 


<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
