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
<header >
	<ul class="nav nav-tabs" style="margin:3px 8px 1px 8px">
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('reviewgds')) >
	  <#if (mode!'')=='goods'>
	  <li style="width:50%;text-align:center" class="active" onclick="window.location.href='/review/manage/goods'" >
	  <#else>
	  <li style="width:50%;text-align:center" class="" onclick="window.location.href='/review/manage/goods'" >
	  </#if>
	    <a href="javascript:;"  style="padding:10px 8px">商品</a>
	  </li>
	</#if>
	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('reviewappr')) >
	  <#if (mode!'')=='appraise'>
	  <li style="width:50%;text-align:center" class="active" onclick="window.location.href='/review/manage/appraise'" >
	  <#else>
	  <li style="width:50%;text-align:center" class="" onclick="window.location.href='/review/manage/appraise'" >
	  </#if>
	    <a href="javascript:;"  style="padding:10px 8px">评论</a>
	  </li>
	</#if>
	</ul>
</header>
<#if (mode!'')=='goods'>
  <div class="row" id="goodsList" style="margin:0" >  <!-- 商品管理列表 -->
    <div class="row" style="margin:0;padding:5px 2px;background-color:white">
      <div class="col-xs-6" style="padding:5px 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">审核状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.reviewResult" required>
		            <option value="4review">待审核</option>
		            <option value="normal">通过</option>
		            <option value="refuse">拒绝</option>
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
           <th width="80%" style="padding:2px 2px">商品名称</th>
           <th width="20%" style="padding:2px 2px;text-align:center">审核状态</th>
         </tr>
	     <tr v-for="item in dataList" >
            <td style="padding:2px 2px">{{item.goodsName}}</td>
            <td style="padding:2px 2px;text-align:center">
              <a target="_blank" :href="'/goods/preview/' + item.goodsId + '?mode=review'">{{getGoodsRewResult(item.reviewResult)}}</a>
            </td>
	    </tr>
	  </table>
    </div>
  </div>
</#if>  
  
<#if (mode!'')=='appraise'>
  <div class="row" id="appraiseList" style="margin:0" ><!-- 评论管理列表 -->
    <div class="row" style="margin:0;padding:5px 2px;background-color:white">
      <div class="col-xs-6" style="padding:5px 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">审核状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.reviewResult" required>
		            <option value="4review">待审核</option>
		            <option value="normal">通过</option>
		            <option value="refuse">拒绝</option>
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
           <th width="60%" style="padding:2px 2px">订单ID</th>
           <th width="20%" style="padding:2px 2px;text-align:center">评价时间</th>
           <th width="20%" style="padding:2px 2px;text-align:center">状态</th>
         </tr>
	     <tr v-for="item in dataList" >
            <td style="padding:2px 2px">{{item.orderId}}</td>
            <td style="padding:2px 2px">{{item.appraiseTime}}</td>
            <td style="padding:2px 2px;text-align:center">
              <a target="_blank" :href="'/psaleorder/appraise/review/' + item.orderId">{{getAppraiseStatus(item.appraiseStatus)}}</a>
            </td>
	    </tr>
	  </table>
    </div>
  </div>  
</#if>  
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	 el:'#container',
	 data:{
		param:{
			reviewResult:'1',
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
			var url = '';
			<#if (mode!'') == 'goods'> url = '/review/list/goods';</#if>
			<#if (mode!'') == 'appraise'> url = '/review/list/appraise';</#if>
			if(!url){
				return;
			}
			var searchParam = {
				'reviewStatus':containerVue.param.reviewResult,
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
