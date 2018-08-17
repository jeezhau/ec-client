<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<div class="container" id="container" style="padding:0;oveflow-y:scroll">
  <div class="row" style="margin:3px 3px;padding:5px 2px;background-color:white">
      <div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="search.status">
		            <option value=""> 请选择... </option>
		            <option value="0">待审核</option>
		            <option value="R">终审拒绝</option>
		            <option value="S">终审通过</option>
		            <option value="A">初审拒绝</option>
		            <option value="B">初审通过</option>
		            <option value="C">暂时关闭歇业</option>
		            <option value="1">严重违规关店</option>
	            </select>
	        </div>
	      </div>
      </div>
      <div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">合作类型</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="search.pbTp" required>
		            <option value=""> 请选择... </option>
		            <option value="1">商户</option>
		            <option value="2">招商推广</option>
	            </select>
	        </div>
	      </div>
      </div>
      <div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">企业类型</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="search.compType" required>
		            <option value=""> 请选择... </option>
		            <option value="1">小微商户</option>
		            <option value="2">公司</option>
	            </select>
	        </div>
	      </div>
      </div>
      <div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" style="padding:2px">
	    <div class="form-group" style="padding:0 20px;text-align:right">
          <button type="button" class="btn btn-primary" @click="getPartners(true,false)">&nbsp;&nbsp;查&nbsp;&nbsp;询&nbsp;&nbsp; </button>
        </div>
      </div>
  </div>
  
  <div class="row" style="margin:3px 0px;text-align:center;vertical-align:center">
    <div v-for="item in dataList" v-if="item.partnerId != '${((myPartner.partnerId)!0)?string("#")}'" class="col-xs-6 col-sm-6 col-md-4 col-lg-3" style="padding:0 3px">
      <div class="thumbnail">
        </a><img :src="'/partner/cert/show/logo/'+ item.partnerId"  style="width:90%;max-width:180px;height:100px;max-height:180px" alt="LOGO">
        <div class="caption">
          <h5><a :href="'/mypartners/detail/'+item.partnerId + '/show'" >{{item.busiName}}</a></h5>
          <p>
              <a v-if="item.status == '0' || item.status =='S' || item.status =='R' || item.status =='A' || item.status =='B'" :href="'/mypartners/detail/'+item.partnerId + '/review'" class="btn btn-success" role="button" style="padding:0px 3px"> 审核/抽查 </a>
              <a v-if="item.pbTp == '1'" :href="'/mypartners/detail/'+item.partnerId + '/changeUp'" class="btn btn-default" role="button" style="padding:0px 3px"> 变更上级 </a>
          </p>
        </div>
      </div>
    </div>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">

var containerVue = new Vue({
	el:'#container',
	data:{
		search:{
			
		},
		pageCond:{
			begin:0,
			pageSize:20,
			count:0
		},
		dataList:[]
	},
	methods:{
		getPartners: function(isRefresh,isForward){
			var url = '/mypartners/getall';
			var searchParam = {'jsonSearchParams':JSON.stringify(this.search),'begin':this.pageCond.begin,'pageSize':this.pageCond.pageSize};
			getAllData(isRefresh,isForward,url,searchParam,containerVue.dataList,containerVue.pageCond);
		}
	}
});
//页面保留一页的数据
containerVue.getPartners(true,false);  //初始获取数据

//分页初始化
scrollPager(containerVue.pageCond,containerVue.dataList,containerVue.getPartners);
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>
