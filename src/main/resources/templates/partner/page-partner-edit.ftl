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
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/fileinput_locale_zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
    <script src="https://cache.amap.com/lbs/static/es5.min.js"></script>
    
	<link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.6&key=2b12c05334ea645bd934b55c8e46f6ea"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
    <script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main.css"/>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container" id="partnerContainer" style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li class="active" style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editPartner').show();$('#reviewInfo').hide();">
	    <a href="javascript:;">基本信息编辑</a>
	  </li>
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editPartner').hide();$('#reviewInfo').show();">
	    <a href="javascript:;">审批进度</a>
	  </li>
	</ul>
  </div>
  <div class="row" id="editPartner" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
    <h5 style="text-align:center">合作伙伴基本信息编辑</h5>
    <h6>
      &nbsp;&nbsp;&nbsp;&nbsp;所有的照片一旦上传，只可使用新照片再次上传替换删除，不可直接删除；页面删除操作只在本地进行，不会删除服务器上的照片数据！<br>
      &nbsp;&nbsp;&nbsp;&nbsp;系统需要获取经营地的经纬度信息，因此请您务必<span style="color:red">在地图中获取位置</span>并提交操作，并同意我们获取您的当前位置，否则可能无法提交或审核不通过！
    </h6>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
	  <div class="form-group">
	    <div class="col-xs-12" style="padding:0 30px;text-align:right">
	      <a href="javascript:;" @click="showMap"><img alt="" src="/icons/收货地址.png" width=30px height=30px></a>
	    </div>
	  </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-省份<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px" >
          <select class="form-control" v-model="param.province" v-on:change="changeProvince" disabled>
            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-城市<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control"  v-model="param.city" v-on:change="changeCity" disabled>
            <option v-for="item in metadata.cities" v-bind:value="item.cityName">{{item.cityName}}</option>
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-区县<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control"  v-model="param.area" disabled>
            <option v-for="item in metadata.areas" v-bind:value="item.areaName">{{item.areaName}}</option>
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">详细地经营地址<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="text" class="form-control" v-model="param.addr" maxLength=200 v-bind:disabled="!param.canUpdAdd" placeholder="请输入详细经营地址，不含省市县" >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">合作伙伴类型<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
         <#if !(myPartner.partnerId)??>
          <select class="form-control"  v-model="param.pbTp">
         <#else>
          <select class="form-control"  v-model="param.pbTp" disabled>
         </#if>
            <option value="" disabled>请选择合作伙伴类型...</option>
            <option value="1">特约商户</option>
            <!-- <option value="2">推广招商</option> -->
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营名称<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.busiName"  maxLength=30  placeholder="经营名称，一般为公司名称的简写" >
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">法人姓名<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.legalPername"  maxLength=100   placeholder="请输入法人姓名" >
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">法人身份证号码<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.legalPeridno"  maxLength=20  required placeholder="请输入18位法人身份证号码">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业类型<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.compType" required>
            <option value="1">小微商户</option>
            <option value="2">公司</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业名称<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.compName"  maxLength=100  required placeholder="请输入个体户或公司名称">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">营业执照号码<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.licenceNo"  maxLength=50 placeholder="请输入营业执照号码">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">业务联系电话<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.phone"  maxLength=20  required placeholder="请输入11位业务联系电话">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">企业经营简介<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <textarea class="form-control" v-model="param.introduce"  maxLength=600  rows=10 required placeholder="请输入10-600字符的企业经营简介"></textarea>
        </div>
      </div>        
      <div class="form-group">
        <div style="text-align:center">
          <button type="button" class="btn btn-info" style="margin:20px;display:none" @click="getLocation"><span style="color:red">*</span>获取当前经营地址</button>
        </div>
      </div> 
      <div class="form-group">
	    <label class="col-xs-12 control-label" style="padding-right:1px">企业LOGO<span style="color:red">*</span></label>
	    <div class="col-xs-12">
	        <input class="form-control" id="logoImg"  type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	    </div>
	  </div>
	  <div class="form-group">
	    <label class="col-xs-12 control-label">法人身份证照片<span style="color:red" >*</span></label>
	      <div class="col-xs-12">
	        <h6 >头像正面</h6>
	        <input class="form-control" id="certFile_1"  height="100px" width="100%" type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	      </div>
	      <div class="col-xs-12">
	        <h6 >国徽反面</h6>
	        <input class="form-control" id="certFile_2"  type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	      </div>
	  </div>
	  <div class="form-group">
	    <label class="col-xs-12 control-label" style="padding-right:1px">公司营业执照或小微商户法人手持身份证正面照</label>
	    <div class="col-xs-12">
	        <input class="form-control" id="licenceImg"  type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	    </div>
	  </div>
      <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info" id="save" style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning" id="reset" style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>
	</form>
  </div>

  <div class="row" id="reviewInfo" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<h5 style="text-align:center">最新审批结果信息</h5>
  	<p>合作伙伴ID：{{review.partnerId}}</p>
  	<p>审批时间：{{review.reviewTime}}</p>
  	<p>审批结果：{{getStatus()}} </p>
  	<p>审批意见：{{review.reviewLog}}</p>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var partnerContainerVue = new Vue({
	el:'#partnerContainer',
	data:{
		initData:{
			pbTp:'${(myPartner.pbTp)!"1"}',
			country:'中国',
			province:'${(myPartner.province)!''}',
			city:'${(myPartner.city)!''}',
			area:'${(myPartner.area)!''}',
			addr:'${(myPartner.addr)!''}',
			busiName:'${(myPartner.busiName)!''}',
			legalPername:'${(myPartner.legalPername)!''}',
			legalPeridno:'${(myPartner.legalPeridno)!''}',
			compType:'${(myPartner.compType)!''}',
			compName:'${(myPartner.compName)!''}',
			licenceNo:'${(myPartner.licenceNo)!''}',
			phone:'${(myPartner.phone)!''}',
			introduce:`${(myPartner.introduce)!''} `,
			locationX:'${(myPartner.locationX)!''}',
			locationY:'${(myPartner.locationY)!''}'
		},	//初始化的数据
		metadata:{
			provinces:[],
			cities:[],
			areas:[]
		},
		review:{
			partnerId:'${(myPartner.partnerId)!""}',
			status:'${(myPartner.status)!"0"}',
			reviewTime:'${(myPartner.reviewTime)!''}' ,
			reviewLog:"${(myPartner.reviewLog)!''}"
		},
		param:{
			pbTp:'${(myPartner.pbTp)!''}',
			country:'中国',
			province:'${(myPartner.province)!''}',
			city:'${(myPartner.city)!''}',
			area:'${(myPartner.area)!''}',
			addr:'${(myPartner.addr)!''}',
			busiName:'${(myPartner.busiName)!''}',
			legalPername:'${(myPartner.legalPername)!''}',
			legalPeridno:'${(myPartner.legalPeridno)!''}',
			compType:'${(myPartner.compType)!''}',
			compName:'${(myPartner.compName)!''}',
			licenceNo:'${(myPartner.licenceNo)!''}',
			phone:'${(myPartner.phone)!''}',
			introduce: `${(myPartner.introduce)!''}`,
			locationX:'${(myPartner.locationX)!''}',
			locationY:'${(myPartner.locationY)!''}',
			canUpdAdd:false
		}
	},
	methods:{
		getStatus: function(){
    			if("0" == this.review.status) {
    				return "待审核";
    			}else if("1"== this.review.status) {
    				return "严重违规关店";
    			}else if("R" == this.review.status) {
    				return "审核拒绝";
    			}else if("S" == this.review.status) {
    				return "审核通过";
    			}else if("C" == this.review.status) {
    				return "暂时关闭歇业";
    			}
        		return "其他";
		},
		getAllProvinces: function(){
			$.ajax({
				url: '/city/province/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						partnerContainerVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							partnerContainerVue.metadata.provinces.push(jsonRet[i]);
						}
						if(partnerContainerVue.param.city){//有城市参数
							getCities();
						}
					}else{
						alertMsg('错误提示','获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		changeProvince: function(){
			partnerContainerVue.param.city = '';
			partnerContainerVue.param.area = '';
			partnerContainerVue.metadata.cities = [];
			partnerContainerVue.metadata.areas = [];
			getCities();
		},
		changeCity: function(){
			partnerContainerVue.param.area = '';
			partnerContainerVue.metadata.areas = [];
			getAreas();
		},
		showMap: function(){
			$('#showAddrMap').show();
		},
		getLocation:function(province,city,area,addr,locX,locY){
			this.param.province = province;
			this.param.city = city;
			this.param.area = area;
			this.param.addr = addr.replace(province,'').replace(city,'').replace(area,'');
			this.param.locationX = locX;
			this.param.locationY = locY;
			getCities();
			this.param.canUpdAdd = true;
		},
		submit: function(){
			$("#dealingData").show();
			$.ajax({
				url: '/partner/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"合作伙伴基本信息保存成功！");
							$.extend(partnerContainerVue.initData,partnerContainerVue.param); 
							//window.location.reload();
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
		},
		reset: function(){
			$.extend(partnerContainerVue.param,partnerContainerVue.initData); 
			getCities();
		}
	}
});

partnerContainerVue.getAllProvinces();
function getCities(){
	var provCode = "";
	for(var i=0;i<partnerContainerVue.metadata.provinces.length;i++){
		if(partnerContainerVue.metadata.provinces[i].provName == partnerContainerVue.param.province){
			provCode = partnerContainerVue.metadata.provinces[i].provCode;
			break;
		}
	}
	$.ajax({
		url: '/city/city/getbyprov/' + provCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				partnerContainerVue.metadata.cities = [];
				for(var i=0;i<jsonRet.length;i++){
					partnerContainerVue.metadata.cities.push(jsonRet[i]);
				}
				if(partnerContainerVue.param.area){
					getAreas();
				}
			}else{
				alertMsg('错误提示','获取城市数据(地级市)失败！')
			}
		},
		dataType: 'json'
	});
}

function getAreas(){
	var cityCode = "";
	for(var i=0;i<partnerContainerVue.metadata.cities.length;i++){
		if(partnerContainerVue.metadata.cities[i].cityName == partnerContainerVue.param.city){
			cityCode = partnerContainerVue.metadata.cities[i].cityCode;
			break;
		}
	}
	$.ajax({
		url: '/city/area/getbycity/' + cityCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				partnerContainerVue.metadata.areas = [];
				for(var i=0;i<jsonRet.length;i++){
					partnerContainerVue.metadata.areas.push(jsonRet[i]);
				}
			}else{
				alertMsg('错误提示','获取城市数据(县)失败！')
			}
		},
		dataType: 'json'
	});
}

</script>
<script type="text/javascript">
$(document).on('ready', function() {
    $("#logoImg").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/partner/cert/upload', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{'certType':'logo'},
        deleteExtraData:{},
        dropZoneEnabled:false,
        allowedFileExtensions : ['jpg', 'png','jpeg'],//接收的文件后缀
        previewFileType: "image",
        browseClass: "btn btn-success",
        browseLabel: "Pick Image",
        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
        removeClass: "btn btn-danger",
        removeLabel: "Delete",
        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
        uploadClass: "btn btn-info",
        uploadLabel: "Upload",
        uploadIcon: "<i class=\"glyphicon glyphicon-upload\"></i> ",
        maxFileSize: 2024,//单位为kb，如果为0表示不限制文件大小
        previewSettings: {
            image: {width: "100px", height: "100px"},
        },
        <#if (myPartner.busiName)??>
        initialPreview: [ //预览图片的设置
            '<img src="/partner/cert/show/logo/${(myPartner.partnerId)!''}" alt="LOGO照片" class="file-preview-image" style="width:96px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#logoImg').on('fileerror', function(event, data, msg) {
		alertMsg('系统提示',"企业LOGO照片文件上传失败！");
		$('#logoImg').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#logoImg").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alertMsg('系统提示',"企业LOGO文件上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#logoImg').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#logoImg').fileinput('clear');
		}
    });
     
    $("#certFile_1").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/partner/cert/upload', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{'certType':'idcard1'},
        deleteExtraData:{},
        dropZoneEnabled:false,
        allowedFileExtensions : ['jpg', 'png','jpeg'],//接收的文件后缀
        previewFileType: "image",
        browseClass: "btn btn-success",
        browseLabel: "Pick Image",
        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
        removeClass: "btn btn-danger",
        removeLabel: "Delete",
        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
        uploadClass: "btn btn-info",
        uploadLabel: "Upload",
        uploadIcon: "<i class=\"glyphicon glyphicon-upload\"></i> ",
        maxFileSize: 2024,//单位为kb，如果为0表示不限制文件大小
        previewSettings: {
            image: {width: "100px", height: "100px"},
        },
        <#if (myPartner.busiName)??>
        initialPreview: [ //预览图片的设置
            '<img src="/partner/cert/show/idcard1/${(myPartner.partnerId)!''}" alt="法人身份证正面" class="file-preview-image" style="width:96px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#certFile_1').on('fileerror', function(event, data, msg) {
		alertMsg('错误提示',"身份证照片文件正面上传失败！");
		$('#certFile_1').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#certFile_1").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alertMsg('系统提示',"身份证照片文件正面上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#certFile_1').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#certFile_1').fileinput('clear');
		}
    });
    
    $("#certFile_2").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/partner/cert/upload', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{'certType':'idcard2'},
        deleteExtraData:{},
        dropZoneEnabled:false,
        allowedFileExtensions : ['jpg', 'png','jpeg'],//接收的文件后缀
        previewFileType: "image",
        browseClass: "btn btn-success",
        browseLabel: "Pick Image",
        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
        removeClass: "btn btn-danger",
        removeLabel: "Delete",
        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
        uploadClass: "btn btn-info",
        uploadLabel: "Upload",
        uploadIcon: "<i class=\"glyphicon glyphicon-upload\"></i> ",
        maxFileSize: 2024,//单位为kb，如果为0表示不限制文件大小
        previewSettings: {
            image: {width: "100px", height: "100px"},
        },
        <#if (myPartner.busiName)??>
        initialPreview: [ //预览图片的设置
            '<img src="/partner/cert/show/idcard2/${(myPartner.partnerId)!''}" alt="法人身份证反面" class="file-preview-image" style="width:100px;height:100px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#certFile_2').on('fileerror', function(event, data, msg) {
		alertMsg('错误提示',"身份证照片文件反面上传失败！");
		$('#certFile_2').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#certFile_2").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alertMsg('系统提示',"身份证照片文件反面上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#certFile_2').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#certFile_2').fileinput('clear');
		}
    });
    
    $("#licenceImg").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/partner/cert/upload', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{'certType':'licence'},
        deleteExtraData:{},
        allowedFileExtensions : ['jpg', 'png','jpeg'],//接收的文件后缀
        dropZoneEnabled:false,
        previewFileType: "image",
        browseClass: "btn btn-success",
        browseLabel: "Pick Image",
        browseIcon: "<i class=\"glyphicon glyphicon-picture\"></i> ",
        removeClass: "btn btn-danger",
        removeLabel: "Delete",
        removeIcon: "<i class=\"glyphicon glyphicon-trash\"></i> ",
        uploadClass: "btn btn-info",
        uploadLabel: "Upload",
        uploadIcon: "<i class=\"glyphicon glyphicon-upload\"></i> ",
        maxFileSize: 2024,//单位为kb，如果为0表示不限制文件大小
        previewSettings: {
            image: {width: "100px", height: "100px"},
        },
        <#if (myPartner.busiName)??>
        initialPreview: [ //预览图片的设置
            '<img src="/partner/cert/show/licence/${(myPartner.partnerId)!''}" alt="营业执照照片" class="file-preview-image" style="width:96px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#licenceImg').on('fileerror', function(event, data, msg) {
		alertMsg('错误提示',"营业执照照片文件上传失败！");
		$('#licenceImg').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#licenceImg").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alertMsg('系统提示',"营业执照照片文件上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#licenceImg').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#licenceImg').fileinput('clear');
		}
    });
     
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
<!-- 
<script>
var mapObj = new AMap.Map('iCenter');
mapObj.plugin('AMap.Geolocation', function () {
    geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,//是否使用高精度定位，默认:true
        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
        maximumAge: 0,           //定位结果缓存0毫秒，默认：0
        convert: true,           //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
        showButton: true,        //显示定位按钮，默认：true
        buttonPosition: 'LB',    //定位按钮停靠位置，默认：'LB'，左下角
        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        showMarker: true,        //定位成功后在定位到的位置显示点标记，默认：true
        showCircle: true,        //定位成功后用圆圈表示定位精度范围，默认：true
        panToLocation: true,     //定位成功后将定位到的位置作为地图中心点，默认：true
        zoomToAccuracy:true      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
    });
    mapObj.addControl(geolocation);
    geolocation.getCurrentPosition(function(status,result){
    		if(status == 'complete'){
    			/* containerVue.param.city = result.addressComponent.city;
    			containerVue.param.area = result.addressComponent.district;
    			containerVue.param.lat = result.position.lat;
    			containerVue.param.lng = result.position.lng; */
    			//系统业务调用
    			partnerContainerVue.getLocation(result.addressComponent.city,result.addressComponent.district,result.position.lng,result.position.lat);
		}
    });
    //AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
   //AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
});
</script> -->

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
