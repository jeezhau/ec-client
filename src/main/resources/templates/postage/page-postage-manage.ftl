<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">

</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<header >
	<ul class="nav nav-tabs" style="margin:3px 8px 3px 8px">
	  <li style="width:50%;text-align:center" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#postageNotice').show();$('#myAllPostages').hide()">
	    <a href="javascript:;"  style="padding:10px 8px">运费模版须知</a>
	  </li>
	  <li style="width:50%;text-align:center;" class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#postageNotice').hide();$('#myAllPostages').show()">
	    <a href="javascript:;"  style="padding:10px 8px">我的所有模板</a>
	  </li>
	</ul>
</header>
<div class="container" id="container" style="oveflow:scroll">
  <div class="row" style="margin:5px 0;display:none; " id="postageNotice"><!-- 管理须知 -->
    <div class="col-xs-12" style="">
	  	<p>运费模板：在创建商品是必须选择一个模版，用于指定该商品的邮费计算规则。</p>
	    <p>须知：<br>
	      0、每个模版有一个配送模式组合，即适用于使用哪种方式进行配送，'商家自取'必须免邮，'官方配送'费用要求以签约合同为准。<br>
	      1、每一个模版有一个适用的配送范围，运费模板的配送必须包含商品的销售范围才可适用于该商品。<br>
	      2、运费可以无条件免邮、不免邮、有条件免邮；<br>
	      3、配送范围为同城时，可有配送距离限制：即距离合作伙伴经营地的距离，距离限制适用于商家自己配送以及官方配送；<br>
	      4、某些省份地区的配送邮费较高，合作伙伴可单独设置该地区或选择不支持该地区配送；<br>
	      5、有条件免邮，包含：‘重量免邮’即低于该重量免邮，‘金额免邮’即高于该金额免邮，‘距离免邮’即近于该距离免邮仅适用同城；三者可组合设置，即同时满足组合设置条件时免邮；<br>
	      6、除‘无条件免邮’之外需要设置首重和首重的收费价格，以及续重和续重的收费价格；首重收费0表明首重免邮，续重收费0表面仅收首重邮费，超过首重的额外重量不另外收费；<br>
	      7、同城配送的非‘无条件免邮’还需要设置首距和首距的收费价格，以及续距和续距的收费价格；首距收费0表明首距免邮，续距收费0表面仅收首距邮费，超过首距的额外距离不另外收费；<br>
	      8、重量收费与距离收费将叠加计算，须特别注意；<br>
	      9、每个商品可选多个运费模板，系统将选择符合配送条件的邮费最低的模板；<br>
	      10、正在被使用中的模版修改时必须要特别注意，修改了配送范围将可能导致部分商品不支持配送而客户下单失败；费用计算信息的修改将可能导致合作伙伴支付的运费与客户支付的运费之间出现差额，差额将由合作伙伴承担；<br>
	    </p>
	    <p style="color:red">如果根据模版计算出邮费与合作伙伴支付给官方和第三方快递的邮费不符，差额将由合作伙伴承担！</p>
    </div>
    <div class="col-xs-12" style="margin-top:10px;text-align:center">
     <a class="btn btn-primary" href="/postage/edit/0">新建模版</a>
     <p>一旦您新建模版将默认同意以上须知规则！</p>
    </div>  
  </div>
  
  <div class="row" id="myAllPostages" >
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
						alertMsg('错误提示','获取数据失败！')
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
								alertMsg('错误提示','该模版正在被 ' + jsonRet.cnt + " 个商品使用中，您不可删除！");
							}else{
								$.ajax({
									url: '/postage/delete/' + postageId,
									method:'post',
									data: {},
									success: function(jsonRet,status,xhr){
										if(jsonRet ){
											if(jsonRet.errcode == 0){
												alertMsg('系统提示','模版删除成功！');
												window.location.reload();
											}
										}else{
											alertMsg('错误提示','获取数据失败！')
										}
									},
									dataType: 'json'
								});
							}
						}else{
							alertMsg('错误提示','获取数据失败！')
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
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>