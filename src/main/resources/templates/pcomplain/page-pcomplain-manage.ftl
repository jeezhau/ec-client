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
		            <option value="0">待处理</option>
		            <option value="1">待回访</option>
		            <option value="2">完成</option>
	            </select>
	        </div>
	      </div>
	    </div>
	    <div class="col-xs-6" style="padding:5px 2px">
      	  <div class="form-group" style="padding:0 20px">
        		<button type="button" class="btn btn-primary" @click="getAll(true,false)">&nbsp;&nbsp;查&nbsp;&nbsp;询&nbsp;&nbsp; </button>
        		<#if (mode!'')=='partner'>
        		<a type="button" class="btn btn-info" href="/pcomplain/edit/0?oprFlag=E">&nbsp;&nbsp;新&nbsp;&nbsp;建&nbsp;&nbsp; </a>
        		</#if>
      	  </div>
      	</div>
    </div>
    <div class="row" style="margin:0">
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="40%" style="padding:2px 2px">投诉类型</th>
           <th width="20%" style="padding:2px 2px">状态</th>
           <th width="25%" style="padding:2px 2px">投诉时间</th>
           <th width="15%" style="padding:2px 2px;text-align:center">操作</th>
         </tr>
	     <tr v-for="item in dataList" >
            <td style="padding:2px 2px">{{getCpType(item.cpType)}}</td>
            <td style="padding:2px 2px">{{getCpStatus(item.status)}}</td>
            <td style="padding:2px 2px">{{item.createTime}}</td>
            <td style="padding:2px 2px;text-align:center">
            <#if (mode!'')=='sys'>
            	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('ComplainDeal')) >
              <a v-if="item.status=='0'" target="_blank" :href="'/pcomplain/show/deal/' + item.cplanId">提交处理</a>
            </#if>
            <#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('ComplainRevisit')) >
              <a v-if="item.status=='1'" target="_blank" :href="'/pcomplain/show/revisit/' + item.cplanId">提交回访</a>
            </#if>
            <a v-if="item.status=='2'" target="_blank" :href="'/pcomplain/show/detail/' + item.cplanId">详情</a>
            </#if>
            <#if (mode!'')=='partner'>
            	<#if partnerUserTP=='bindVip' || (partnerUserTP=='staff' && ((partnerStaff.tagList)!'')?contains('ComplainDeal')) >
              <a v-if="item.status=='0'" target="_blank" :href="'/pcomplain/edit/' + item.cplanId + '?oprFlag=E'">修改</a>
              <a v-if="item.status=='0'" target="_blank" :href="'/pcomplain/edit/' + item.cplanId + '?oprFlag=D'">删除</a>
            </#if>
            <a v-if="item.status=='1' || item.status=='2'" target="_blank" :href="'/pcomplain/show/detail/' + item.cplanId">详情</a>
            </#if>
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
		getCpType: function(code){
			if(code =='1'){
				return '商品订单';
			}else if(code =='2'){
				return '合作伙伴';
			}
			return '其他';
		},
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
		getAll: function(isRefresh,isForward){
			var url = '/pcomplain/${(mode!'')}/getall';
			var searchParam = {
				'searchParams':JSON.stringify(containerVue.param),
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
