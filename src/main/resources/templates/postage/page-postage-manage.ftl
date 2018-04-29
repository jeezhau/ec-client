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
    
    <script src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">

</head>
<body class="light-gray-bg">
<header >
	<ul class="nav nav-tabs " style="margin:3px 8px 3px 8px">
	  <li class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#postageNotice').show();$('#myAllPostages').hide()">
	    <a href="javascript:;"  style="padding:10px 2px">运费模版须知</a>
	  </li>
	  <li onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#postageNotice').hide();$('#myAllPostages').show()">
	    <a href="javascript:;"  style="padding:10px 2px">我的所有模板</a>
	  </li>
	</ul>
</header>
<div class="container" id="container" style="oveflow:scroll">
  <div class="row" style="margin:5px 0 " id="postageNotice"><!-- 管理须知 -->
    <div class="col-xs-12" style="">
	  	<p>商品管理功能：主要用于合作伙伴对商品的整个生命周期的管理；一个商品的一生：新添加--待审核--审核通过--未上架／已上架--已下架--删除 。</p>
	    <p>须知：<br>
	      1、一个商品仅在<span>已上架</span>状态时才可进行销售，新添加或修改名称与内容描述的商品需要先审核通过才可上架；<br>
	      2、如果商品审核拒绝，则会说明拒绝理由，可重新修改后再提交审核；<br>
	      3、合作伙伴所填写的一切商品信息须真实可靠，如发现弄虚作假将被警告，情节严重者将被关店处理；<br>
	      4、合作伙伴所售卖的商品产生的品质与描述纠纷问题由合作伙伴自己负责；摩放优选不负任何责任且并督促处理；<br>
	    </p>
    </div>
    <div class="col-xs-12" style="margin-top:10px;text-align:center">
     <a class="btn btn-primary" href="/goods/edit/0">新建模版</a>
     <p>一旦您新建商品将默认同意以上须知规则！</p>
    </div>  
  </div>
  
  <div class="row" id="myAllPostages" style="display:none">
	<div class="row" style="margin:0 0;padding-left:20px;">
      <a class="btn btn-info" href="/postage/edit/0">新建模版</a>
	</div>
	<div class="row">
	  <ul class="list-group" style="padding:0 0px;background-color:white;margin:5px 20px 0 20px;">
	    <li v-for="item in postages" class="list-group-item">
	      <a  v-bind:href="'/postage/edit/' + item.postageId"><span>{{item.postageName}}</span></a>
	      <a  href="javascript:;" class="badge" style="background-color:white" @click="deletePostage(item.postageId)"><img alt="" width=20px height=20px src="/icons/删除1.png"></a>
	    </li>
	  </ul>
    </div>
  </div>
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		postages:[]
	},
	methods:{
		getAll: function(){
			$.ajax({
				url: '/postage/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet ){
						if(jsonRet.postages){
							containerVue.postages = [];
							for(var i=0;i<jsonRet.postages.length;i++){
								containerVue.postages.push(jsonRet.postages[i]);
							}
						}
					}else{
						alert('获取数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		deletePostage: function(postageId){
			if(confirm("您确定要删除该模版码吗？")){
				$.ajax({
					url: '/postage/getusingcnt/' + postageId,
					method:'post',
					data: {},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.cnt > 0){
								alert('该模版正在被 ' + jsonRet.cnt + " 个商品使用中，您不可删除！");
							}else{
								$.ajax({
									url: '/postage/delete/' + postageId,
									method:'post',
									data: {},
									success: function(jsonRet,status,xhr){
										if(jsonRet ){
											if(jsonRet.errcode == 0){
												alert('模版删除成功！');
												window.location.reload();
											}
										}else{
											alert('获取数据失败！')
										}
									},
									dataType: 'json'
								});
							}
						}else{
							alert('获取数据失败！')
						}
					},
					dataType: 'json'
				});
			}else{
				return ;
			}
		}
	}
});
containerVue.getAll();
</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>