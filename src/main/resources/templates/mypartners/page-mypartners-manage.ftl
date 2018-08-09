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
		param:{
			begin:0,
			pageSize:20
		},
		dataList:[]
	},
	methods:{
		getPartners: function(isRefresh,isFirst){
			$("#loadingData").show();
			$("#nomoreData").hide();
			 
			 if(isRefresh){ //清空数据
				 containerVue.param.count = 0;
				 containerVue.dataList = [];
			 }else{
				 if(containerVue.dataList.lenght>=10*containerVue.param.pageSize){
					 if(isFirst){//清除最后一页
						 containerVue.dataList.splice(9*containerVue.param.pageSize,containerVue.param.pageSize);
					 }else{//清除最前一页
						 containerVue.dataList.splice(0,containerVue.param.pageSize); 
					 }
				 }
			 }
			 $.ajax({
					url: '/mypartners/getall',
					method:'post',
					data: {'jsonSearchParams':JSON.stringify(this.search),'begin':this.param.begin,'pageSize':this.param.pageSize},
					success: function(jsonRet,status,xhr){
						if(jsonRet && jsonRet.datas){//
							var i=0;
							var j = jsonRet.datas.length;
							for(;i<jsonRet.datas.length;){
								if(isFirst){
									containerVue.dataList.unshift(jsonRet.datas[j]);
								}else{
									containerVue.dataList.push(jsonRet.datas[i]);
								}
								i++;j--;
							}
							containerVue.param.pageSize = jsonRet.pageCond.pageSize;
							containerVue.param.begin = jsonRet.pageCond.begin;
						}
						$("#loadingData").hide();
					},
					failure:function(){
						$("#loadingData").hide();
					},
					dataType: 'json'
				});	
		}
	}
});
//页面保留一页的数据
containerVue.getPartners(true,false);  //初始获取数据
var winHeight = $(window).height(); //页面可视区域高度   
var scrollHandler = function () {  
    var pageHieght = $(document.body).height();  
    var scrollHeight = $(window).scrollTop(); //滚动条top   
    var r = (pageHieght - winHeight - scrollHeight) / winHeight;
    if (r >= 0 && r < 0.2) {//上拉后翻页
   	 	containerVue.param.begin = containerVue.param.begin + containerVue.param.pageSize;
   	 	containerVue.getPartners(false,false);
    }
    if(scrollHeight<0){//下拉前翻页
    		var currPageCnt = containerVue.dataList.length%containerVue.param.pageSize;//当前页的数量
    		if(currPageCnt == 0){
    			currPageCnt = containerVue.param.pageSize;
    		}
	 	containerVue.param.begin = containerVue.param.begin - (containerVue.dataList.length-currPageCnt);//总数据的开始
    	 	containerVue.param.begin = containerVue.param.begin - containerVue.param.pageSize;
     	if(containerVue.param.begin <= 0){
     		containerVue.param.begin = 0;
     		containerVue.getPartners(true,true);
     	}else{
     		containerVue.getPartners(false,true);
     	}
    }
}  
//定义鼠标滚动事件  
$("#container").scroll(scrollHandler);
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>
