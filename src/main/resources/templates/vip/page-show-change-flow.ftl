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
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js"></script>
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
	   <span class="col-xs-9" style="padding:0">{{flow.sumFlag == '1'?'是':'否'}}</span>
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
		getAllFlow: function(){
			 $("#loadingData").show();
			 $("#nomoreData").hide();
			 if(containerVue.flowList.length>100){
			 	containerVue.flowList = [];
			 }
			$.ajax({
				url: '/vip/flow/getall',
				method:'post',
				data: {'jsonParams':JSON.stringify(this.param),'begin':this.pageCond.begin,'pageSize':this.pageCond.pageSize},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errcode == 0){//
						for(var i=0;i<jsonRet.datas.length;i++){
							containerVue.flowList.push(jsonRet.datas[i]);
						}
						containerVue.param.pageSize = jsonRet.pageCond.pageSize;
						containerVue.param.begin = jsonRet.pageCond.begin;
					}else{
						if(jsonRet && jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							//alertMsg('错误提示',jsonRet.errmsg);
						}
						$("#nomoreData").show();
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
containerVue.getAllFlow();

var winHeight = $(window).height(); //页面可视区域高度   
var scrollHandler = function () {  
    var pageHieght = $(document.body).height();  
    var scrollHeight = $(window).scrollTop(); //滚动条top   
    var r = (pageHieght - winHeight - scrollHeight) / winHeight;
    if (r < 0.5) {//上拉翻页 
   	 	containerVue.begin = containerVue.pageCond.begin + containerVue.pageCond.pageSize;
   	 	containerVue.getAll();
    }
    if(scrollHeight < 0){//下拉刷新
    		containerVue.pageCond.begin = containerVue.pageCond.begin - containerVue.pageCond.pageSize;
     	if(containerVue.pageCond.begin < 0){
     		containerVue.pageCond.begin = 0;
     	}
   	 	containerVue.getAll();
    }
}  
//定义鼠标滚动事件
$("#container").scroll(scrollHandler); 

</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
