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
	  
	</form>
  </div>
  <#if (mode!'') == 'show'>
  <div class="row" id="reviewInfo" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<h5 style="text-align:center">最新审批结果信息</h5>
  	<p>合作伙伴ID：${(partner.partnerId)?string('#')}</p>
  	<p>审批时间：${(partner.reviewTime)!''}</p>
  	<p>审批人：${(partner.reviewOpr)!''}</p>
  	<p>审批结果：{{getPartnerStatus('${(partner.status)!''}')}} </p>
  	<p>审批意见：${(partner.reviewLog)!''}</p>
  </div>
  </#if>
  <#if (mode!'') == 'review'>
  <div class="row" id="changeUpForm" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
  	<h5 style="text-align:center">合作伙伴审批与抽查</h5>
  	<div class="form-group">
        <label  class="col-xs-3 control-label">合作伙伴ID</label>
        <div class="col-xs-9">
          <input class="form-control" value="${(partner.partnerId)?string('#')}" disabled>
        </div>
    </div>
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
			result:''
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

<div id="showAddrMap" style="position:fixed;left:0;top:0;right:0;bottom:0;margin:0;width:100%;display:none;z-index:1000;background:rgba(0,0,0,0.2);display:none;">
<div id="mapContainer" style="top:10px;width:100%;height:500px;"></div>
<div id="myPageTop" style="left:10px">
    <table style="width:100%;text-align:right">
        <tr style="width:100%">
            <td class="column1"><label><a onclick="$('#showAddrMap').hide();">关闭</a></label></td>
        </tr>
        <tr>
            <td class="column1"><input type="text" style="width:90%" readonly id="lnglat" placeholder="点击地图选择地点"></td>
        </tr>	        
        <tr>
            <td class="column1"><input type="text" id="keyword" name="keyword" value="请输入关键字：(选定后搜索)" style="width:90%" onfocus='this.value=""'/></td> 
        </tr>
        
    </table>
</div>
<script type="text/javascript">
var windowsArr = [];
   var marker = [];
    var map = new AMap.Map("mapContainer", {
        resizeEnable: true,
        zoom: 13,
    });
    AMap.plugin('AMap.Geocoder',function(){
        var geocoder = new AMap.Geocoder({
            city: "010"//城市，默认：“全国”
        });
        var marker = new AMap.Marker({
            map:map,
            bubble:true
        })
        map.on('click',function(e){
            marker.setPosition(e.lnglat);
            geocoder.getAddress(e.lnglat,function(status,result){
              if(status=='complete'){
                var addr = result.regeocode;
                partnerContainerVue.getLocation(addr.addressComponent.province,addr.addressComponent.city,
                		addr.addressComponent.district,addr.formattedAddress,e.lnglat.getLng(),e.lnglat.getLat());
                document.getElementById("lnglat").value = addr.formattedAddress;
              }else{
                 alertMsg('系统提示','无法获取地址');
              }
            });
        })
    });
     AMap.plugin(['AMap.Autocomplete','AMap.PlaceSearch'],function(){
        var autoOptions = {
          city: "昆明", //城市，默认全国
          input: "keyword",//使用联想输入的input的id
          
        };
        autocomplete= new AMap.Autocomplete(autoOptions);
        var placeSearch = new AMap.PlaceSearch({
              city:'昆明',
              map:map
        });
        AMap.event.addListener(autocomplete, "select", function(e){
           //TODO 针对选中的poi实现自己的功能
           placeSearch.setCity(e.poi.adcode);
           placeSearch.search(e.poi.name)
        });
      }); 
   
</script>
</div>

</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
