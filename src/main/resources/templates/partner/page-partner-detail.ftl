<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (partner.partnerId)??>
<div class="container" id="partnerContainer" style="padding:0px 0px;overflow-x:hidden;oveflow-y:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li class="active" style="width:50%;text-align:center;" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#partnerDetail').show();$('#partnerDetail').siblings().hide();">
	    <a href="javascript:;">基本信息</a>
	  </li>
	  <#if (mode!'') == 'review'>
	  <li style="width:50%;text-align:center;" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#changeUpForm').show();$('#changeUpForm').siblings().hide();">
	    <a href="javascript:;">审批与抽查</a>
	  </li>
	  </#if>
	  <#if (mode!'') == 'show'>
	  <li style="width:50%;text-align:center;" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#reviewInfo').show();$('#reviewInfo').siblings().hide();">
	    <a href="javascript:;">审批进度</a>
	  </li>
	  </#if>
	  <#if (mode!'') == 'changeUp'>
	  <li style="width:50%;text-align:center;" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#changeUpForm').show();$('#changeUpForm').siblings().hide();">
	    <a href="javascript:;">变更推广上级</a>
	  </li>
	  </#if>	  
	</ul>
  </div>
  <div class="row" style="margin:0">
  <div class="row" id="partnerDetail" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
    <h5 style="text-align:center">合作伙伴基本信息</h5>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">合作伙伴ID  </label>
        <div class="col-xs-8" style="padding-left:1px" >
          <input type='text' class="form-control" value="${(partner.partnerId)?string('#')}" disabled>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-省份  </label>
        <div class="col-xs-8" style="padding-left:1px" >
          <input type='text' class="form-control" value="${(partner.province)!''}" disabled>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-城市  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type='text' class="form-control" value="${(partner.city)!''}" disabled>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-区县  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type='text' class="form-control" value="${(partner.area)!''}" disabled>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">详细地经营地址  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="text" class="form-control" value="${(partner.addr)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">合作伙伴类型  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" value="${(partner.pbTp)!''}" disabled>
            <option value="" disabled>请选择合作伙伴类型...</option>
            <option value="1">特约商户</option>
            <option value="2">推广招商</option>
          </select>
        </div>
      </div> 
      <#if ((partner.pbTp)!'') == '1'>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">推广上级ID  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control"  value="${((partner.upPartnerId)!0)?string('#')}" disabled >
        </div>
      </div>
      </#if>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营名称  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.busiName)!''}" disabled >
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">法人姓名  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.legalPername)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">法人身份证号码  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.legalPeridno)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业类型  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" value="${(partner.compType)!''}" disabled >
            <option value="1">小微商户</option>
            <option value="2">公司</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业名称  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.compName)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">营业执照号码  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.licenceNo)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">业务联系电话  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" value="${(partner.phone)!''}" disabled >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业经营简介  </label>
        <div class="col-xs-8" style="padding-left:1px">
          <textarea class="form-control" disabled >${(partner.introduce)!''}</textarea>
        </div>
      </div>
      
      <div class="form-group">
        <div class="col-xs-12" style="text-align:center">
          <div class="thumbnail">
            <div class="caption"><h3>企业LOGO</h3></div>
	        <img class="thumbnail" src="/partner/cert/show/logo/${partner.partnerId?string('#')}" alt="LOGO" style="min-width:100px;min-height:100px;max-width:90%">
	      </div>
	    </div>
	    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" style="text-align:center">
          <div class="thumbnail">
            <div class="caption"><h3>身份证正面</h3></div>
	        <img class="thumbnail" src="/partner/cert/show/idcard1/${partner.partnerId?string('#')}" alt="身份证正面" style="max-width:90%">
	      </div>
	    </div>
	    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" style="text-align:center">
          <div class="thumbnail">
            <div class="caption"><h3>身份证反面</h3></div>
	        <img class="thumbnail" src="/partner/cert/show/idcard2/${partner.partnerId?string('#')}" alt="身份证反面" style="max-width:90%">
	      </div>
	    </div>
	    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" style="text-align:center">
          <div class="thumbnail">
            <div class="caption"><h3>公司营业执照或小微商户法人手持身份证正面照</h3></div>
	        <img class="thumbnail" src="/partner/cert/show/licence/${partner.partnerId?string('#')}" alt="公司营业执照或小微商户法人手持身份证正面照" style="max-width:90%">
	      </div>
	    </div>
	    	<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6" style="text-align:center">
          <div class="thumbnail">
            <div class="caption"><h3>签约协议</h3></div>
	        <img class="thumbnail" src="/partner/cert/show/agreement/${partner.partnerId?string('#')}" alt="签约协议" style="max-width:90%">
	      </div>
	    </div>
	  </div>
	  
	  <!-- 分割线 -->
	  <#if ((partner.pbTp)!'')=='1' >
	  <div class="form-group" title="指是否在买家签收评价完成后，在交易资金结算时是否将买家付款时支付的手续费退给买家！">
        <label class="col-xs-4 control-label" style="padding-right:1px">是否退支付手续费<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <#if ((settle.isRetfee)!'0')=='0'><label><input type="radio" value="0" checked style="display:inline-block" disabled> 否 </label></#if>
          <#if ((settle.isRetfee)!'0')=='1'><label><input type="radio" value="1" checked style="display:inline-block"  disabled> 是 </label></#if>
        </div>
      </div>
      <div class="form-group">
        <label  class="col-xs-4 control-label" style="padding-right:3px">平台服务费费率(%)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:3px">
          <input type="number" class="form-control" value="${(settle.serviceFeeRate)!''}" style="width:100px;display:inline-block" disabled>
          <span>%</span>
        </div>
      </div>
      </#if>
      <#if ((partner.pbTp)!'')=='2' >
      <div class="form-group">
        <label  class="col-xs-4 control-label" style="padding-right:3px">推广奖励比例(%)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:3px">
          <input type="number" class="form-control" value="${(settle.shareProfitRate)!''}" style="width:100px;display:inline-block" disabled>
          <span>%</span>
        </div>
      </div>
      </#if>     
	</form>
  </div>
  
  <#if (mode!'') == 'show'>
  <div class="row" id="reviewInfo" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<h5 style="text-align:center">最新审批结果信息</h5>
  	<p>合作伙伴ID：${(partner.partnerId)?string('#')}</p>
  	<p>审批结果：{{getPartnerStatus('${(partner.status)!''}')}} </p>
  	<p>初审时间：<#if (partner.freviewTime)??>${(partner.freviewTime)?string('yyyy-MM-dd HH:mm:ss')}</#if></p>
  	<p>初 审 人：${(partner.freviewOpr)!''}</p>
  	<p>初审意见：${(partner.freviewLog)!''}</p>
  	<p>终审时间：<#if (partner.lreviewTime)??>${(partner.lreviewTime)?string('yyyy-MM-dd HH:mm:ss')}</#if></p>
  	<p>终 审 人：${(partner.lreviewOpr)!''}</p>
  	<p>终审意见：${(partner.lreviewLog)!''}</p>
  </div>
  </#if>
  
  <#if (mode!'') == 'review'>
  <div class="row" id="changeUpForm" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
  	<h5 style="text-align:center">合作伙伴审批与抽查</h5>
  	<div class="form-group">
        <label  class="col-xs-4 control-label" style="padding-right:3px">合作伙伴ID</label>
        <div class="col-xs-8" style="padding-left:3px">
          <input class="form-control" value="${(partner.partnerId)?string('#')}" disabled>
        </div>
    </div>
    <#if ((partner.pbTp)!'')=='1' >
    <div class="form-group">
        <label  class="col-xs-4 control-label" style="padding-right:3px">平台服务费费率(%)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:3px">
          <input type="number" class="form-control" v-model="param.serviceFeeRate" style="width:100px;display:inline-block" required min=1.2 max=10>
          <span>%(1.2-10)</span>
        </div>
    </div>
    </#if>
    <#if (myPartner.partnerId) == SYS_PARTNERID && ((partner.pbTp)!'')=='2' >
    <div class="form-group">
        <label  class="col-xs-4 control-label" style="padding-right:3px">推广奖励比例(%)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:3px">
          <input type="number" class="form-control" v-model="param.shareProfitRate" style="width:100px;display:inline-block" required  min=0 max=70>
          <span>%(0-70)</span>
        </div>
    </div>
    </#if>
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
  </#if>
  
  <#if (mode!'') == 'changeUp'>
  <div class="row" id="changeUpForm" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
  	<h5 style="text-align:center">变更推广上级</h5>
  	<div class="form-group">
        <label  class="col-xs-3 control-label">合作伙伴ID</label>
        <div class="col-xs-9">
          <input class="form-control" value="${(partner.partnerId)?string('#')}" disabled>
        </div>
    </div>
  	<div class="form-group">
        <label  class="col-xs-3 control-label">原上级ID</label>
        <div class="col-xs-9">
          <input class="form-control" value="${((partner.upPartnerId)!0)?string('#')}" disabled>
        </div>
    </div>
    <div class="form-group">
        <label  class="col-xs-3 control-label">新上级ID<span style="color:red">*</span></label>
        <div class="col-xs-9">
          <input class="form-control" v-model="param.newUpId" maxLength=11 required placeholder="新的上级合作伙伴ID">
        </div>
    </div>
    <div class="form-group" style="text-align:center">
         <button type="button" class="btn btn-info" @click="submit()" style="margin:20px">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
    </div>
    </form>
  </div>
  </#if>
  
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var partnerContainerVue = new Vue({
	el:'#partnerContainer',
	<#if (mode!'') == 'review' || (mode!'') == 'changeUp'>
	data:{
		param:{
			partnerId:'${(partner.partnerId)?string("#")}',
			<#if (mode!'') == 'changeUp'>
			newUpId:'',
			<#else>
			review:'',
			result:'',
			shareProfitRate: ${(settle.shareProfitRate)!3},
			serviceFeeRate: ${(settle.serviceFeeRate)!3}
			</#if>
		}
	},
	methods:{
		submit: function(result){
			$("#dealingData").show();
			<#if (mode!'') == 'review'>
			this.param.result = result;
			if(!this.param.review || this.param.review.length<2 || this.param.review.length>600){
				alertMsg('错误提示','审核意见：长度为2-600字符！');
				return;
			}
			<#else>
			if( !(/\d{3,11}/.exec(this.param.newUpId)) ){
				alertMsg('错误提示','新推广上级ID：格式不正确！');
				return;
			}
			</#if>
			$.ajax({
				url: '/mypartners/${mode!""}',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"信息提交成功！");
							window.location.href = '/mypartners/detail/'+partnerContainerVue.param.partnerId + "/show";
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
	</#if>
});

</script>

</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
