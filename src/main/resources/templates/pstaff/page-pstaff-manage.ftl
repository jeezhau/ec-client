<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">

<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 
<#include "/common/tpl-msg-alert.ftl" encoding="utf8"> 

<div class="container " id="container" style="oveflow:scroll">
  <div class="row" style="margin:5px ;text-align:center" >
    <ul class="nav navbar-nav nav-tabs" style="padding:0 5px;font-weight:bold;">  
       <#if partnerUserTP=='bindVip'>
       <li class="pull-right"><a href="/pstaff/edit/0" style="padding:2px 3px">新增员工</a></li>
       </#if>
    </ul>
  </div>
  <div class="row">
    <ul class="list-group">
      <li v-for="staff in dataList" class="list-group-item">
       <div class="row" style="margin:0">
         <span class="pull-left">{{staff.userId}} {{staff.nickname}}</span>
         <a class="pull-right" :href="'/pstaff/edit/'+staff.userId">修改</a>
       </div>
      </li>
    </ul>
  </div>
  
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		dataList:[]
	},
	methods:{
		getAll: function(stat,event){
			$("#loadingData").show();
			$("#nomoreData").hide();
			containerVue.dataList = [];
			$.ajax({
				url: '/pstaff/getall',
				method:'post',
				data: {'begin':0,'pageSize':10},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							var item = jsonRet.datas[i];
							containerVue.dataList.push(item);
						}
						containerVue.begin = jsonRet.pageCond.begin;
						containerVue.pageSize = jsonRet.pageCond.pageSize;
					}else{
						if(jsonRet && jsonRet.errmsg){
							$("#nomoreData").show();
						}
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
containerVue.getAll();
</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8">
</footer>

</body>
</html>
