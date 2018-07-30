<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="overflow-y:scroll;overflow-x:hidden">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (appraise)??>
<div class="container " id="container" style="padding:0;">
   <#include "/common/tpl-order-buy-content-bigimg-4fm.ftl" encoding="utf-8">
   <div class="row" style="margin:1px 2px;padding:3px 10px;background-color:white;">
       <span class="pull-left"><img alt="头像" :src="headimgurl" width="20px" height="20px" style="border-radius:50%">${(appraise.nickname)!''}</span>
       <span class="pull-right">${(appraise.updateTime)}</span>
    </div>
    <div v-for="appr in content" class="row" style="margin:1px 0px;padding:0 20px;background-color:white;">
     <div class="row">
       <span class="pull-left">{{appr.time}}</span>
     </div>
     <div class="row">
       {{appr.content}}
     </div>
    </div>
    <div class="row" id="reviewForm" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
  	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
  	<h5 style="text-align:center">订单评价审批与抽查</h5>
  	<div class="form-group">
        <label  class="col-xs-3 control-label">审核意见<span style="color:red">*</span></label>
        <div class="col-xs-9">
          <textarea class="form-control" v-model="param.review" placeholder="请输入审核意见说明" rows="5" maxLength=600></textarea>
        </div>
    </div>
    <div class="form-group" style="text-align:center">
          <button type="button" class="btn btn-info" @click="submit('S')" style="margin:20px">&nbsp;&nbsp;通 过&nbsp;&nbsp;</button>
          <button type="button" class="btn btn-warning" @click="submit('R')" style="margin:20px">&nbsp;&nbsp;拒 绝&nbsp;&nbsp; </button>
    </div>
    </form>
  </div>
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		param:{
			orderId:'${order.orderId}',
			result:'',
			review:''
		},
		headimgurl : startWith('${(appraise.headimgurl)!""}','http')?'${(appraise.headimgurl)!""}':('/user/headimg/show/${(appraise.userId)?string("#")}'),
		goodsSpecArr: JSON.parse('${(appraise.goodsSpec)!"[]"}'),
		content: JSON.parse('${(appraise.content)!"[]"}'),
	},
	methods:{
		submit: function(result){
			$("#dealingData").show();
			this.param.result = result;
			if(!this.param.review || this.param.review.length<2 || this.param.review.length>600){
				alertMsg('错误提示','审核意见：长度为2-600字符！');
				return;
			}
			$.ajax({
				url: '/review//submit/appraise',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"信息提交成功！");
							window.close();
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		}
	}
});
</script>
</#if>

<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">

<#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 

</body>
</html>

