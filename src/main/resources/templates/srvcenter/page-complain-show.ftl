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
</head>
<body class="light-gray-bg">
<div class="container " id="container" style="oveflow:scroll">
   <div class="row">
     <a class="col-xs-2" href="/complain/manage/order" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >我的投诉</h3>
   </div>
   <div class="row">
	   <div v-for="item in dataList" class="row" style="margin:1px 0px;background-color:white;">
	    <div class="col-xs-12" style="padding:0 3px">
	      <span class="pull-left">订单ID：{{item.orderId}}</span>
	      <span class="pull-right">投诉时间：{{item.createTime}}</span>
	    </div>
	    <div class="col-xs-12" style="padding:0 3px">
	      <p>投诉内容：{{item.content}}</p>
	    </div>
	    <div class="col-xs-12" style="padding:0 3px;color:gray">
		    <div class="row" style="margin:1px 0px;background-color:white;">
		      <span class="pull-left" style="padding:0 10px;font-weight:bolder;color:gray">处理结果</span>
		    </div>
		    <div v-for="deal in item.dealLog" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
		     <div class="row">
		       <span class="pull-left">{{deal.time}}</span>
		     </div>
		     <div class="row">
		       <p>{{deal.result}}</p>
		     </div>
		    </div>
	    </div>
	    <div class="col-xs-12" style="padding:0 10px;text-align:right">
	       <a v-if="item.status=='0'" class="btn btn-primary" :href="'/complain/manage/order?oprFlag=S&cplanId=' + item.cplanId" style="padding:0px 8px"> 修 改 </a>
	       <a class="btn btn-danger" :href="'/complain/manage/order?oprFlag=D&cplanId=' + item.cplanId" style="padding:0px 8px"> 删 除 </a>
	    </div>
	  </div>
  </div>  
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		dataList:[],
		search:{
			begin:0,
			pageSize:1,
			count:0
		}
	},
	methods:{
		getComplain :function(){
			$.ajax({
				url: '/complain/order/getall',
				method:'post',
				data: this.search,
				success: function(jsonRet,status,xhr){
					containerVue.dataList = [];
					if(jsonRet && jsonRet.pageCond){
						if(jsonRet.datas){
							for(var i=0;i<jsonRet.datas.length;i++){
								var complain =jsonRet.datas[i];
								complain.dealLog = JSON.parse(complain.dealLog?complain.dealLog:'{}');
								containerVue.dataList.push(complain);
							}
						}
						containerVue.search.count = jsonRet.pageCond.count;
					}else{
						alertMsg('错误提示',jsonRet.errmsg);
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getComplain();
</script>

</body>
</html>
