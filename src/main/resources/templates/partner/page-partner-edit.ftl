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
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script>
  wx.config({
      debug: false,
      appId: '${APP_ID}',
      timestamp: ${timestamp},
      nonceStr: '${nonceStr}',
      signature: '${signature}',
      jsApiList: [
        'checkJsApi',
        'chooseImage',
        'previewImage',
        'uploadImage',
        'downloadImage',
        'getNetworkType',
        'openLocation',
        'getLocation'
      ]
  });
</script>
</head>
<body class="light-gray-bg">
<div class="container" id="partnerContainer" style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editPartner').show();$('#reviewInfo').hide();">
	    <a href="javascript:;">基本信息编辑</a>
	  </li>
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editPartner').hide();$('#reviewInfo').show();">
	    <a href="javascript:;">审批反馈</a>
	  </li>
	</ul>
  </div>
  <div class="row" id="editPartner" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
    <h5 style="text-align:center">合作伙伴基本信息编辑</h5>
    <h6>
      &nbsp;&nbsp;&nbsp;&nbsp;所有的照片一旦上传，只可使用新照片再次上传替换删除，不可直接删除；页面删除操作只在本地进行，不会删除服务器上的照片数据！<br>
      &nbsp;&nbsp;&nbsp;&nbsp;系统需要获取经营地的经纬度信息，因此请您务必在经营地执行编辑提交操作，并同意我们获取您的当前位置，否则可能无法提交或审核不通过！
    </h6>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-省份<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.province" v-on:change="changeProvince">
            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
          </select>
        </div>
      </div>         
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-城市<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control"  v-model="param.city" v-on:change="changeCity">
            <option v-for="item in metadata.cities" v-bind:value="item.cityName">{{item.cityName}}</option>
          </select>
        </div>
      </div> 
       <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">经营地-县<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control"  v-model="param.area" >
            <option v-for="item in metadata.areas" v-bind:value="item.areaName">{{item.areaName}}</option>
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">详细地经营地址<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="text" class="form-control" v-model="param.addr" maxLength=200 placeholder="请输入详细经营地址，不含省市县" >
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
          <button type="button" class="btn btn-info" style="margin:20px" @click="getLocation"><span style="color:red">*</span>请在经营详细地址位置操作并同意获取地址</button>
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
	        <input class="form-control" id="certFile_1"  height="100px" width="100%" type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	        <h4 style="text-align:center">头像正面</h4>
	      </div>
	      <div class="col-xs-12">
	        <input class="form-control" id="certFile_2"  type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	         <h4 style="text-align:center">国徽反面</h4>
	      </div>
	  </div>
	  <div class="form-group">
	    <label class="col-xs-12 control-label" style="padding-right:1px">营业执照照片</label>
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
			country:'中国',
			province:'${(partner.province)!''}',
			city:'${(partner.city)!''}',
			area:'${(partner.area)!''}',
			addr:'${(partner.addr)!''}',
			busiName:'${(partner.busiName)!''}',
			legalPername:'${(partner.legalPername)!''}',
			legalPeridno:'${(partner.legalPeridno)!''}',
			compType:'${(partner.compType)!''}',
			compName:'${(partner.compName)!''}',
			licenceNo:'${(partner.licenceNo)!''}',
			phone:'${(partner.phone)!''}',
			introduce:'${(partner.introduce)!''}',
			locationX:'${(partner.locationX)!''}',
			locationY:'${(partner.locationY)!''}'
		},	//初始化的数据
		metadata:{
			provinces:[],
			cities:[],
			areas:[]
		},
		review:{
			status:'${(partner.status)!"0"}',
			reviewTime:'${(partner.reviewTime)?string("yyyy-MM-dd HH:mm:ss")}' ,
			reviewLog:"${(partner.reviewLog)!''}"
		},
		param:{
			country:'中国',
			province:'${(partner.province)!''}',
			city:'${(partner.city)!''}',
			area:'${(partner.area)!''}',
			addr:'${(partner.addr)!''}',
			busiName:'${(partner.busiName)!''}',
			legalPername:'${(partner.legalPername)!''}',
			legalPeridno:'${(partner.legalPeridno)!''}',
			compType:'${(partner.compType)!''}',
			compName:'${(partner.compName)!''}',
			licenceNo:'${(partner.licenceNo)!''}',
			phone:'${(partner.phone)!''}',
			introduce:'${(partner.introduce)!''}',
			locationX:'${(partner.locationX)!''}',
			locationY:'${(partner.locationY)!''}'
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
						if(partnerContainerVue.param.city){
							var provCode = "";
							for(var i=0;i<partnerContainerVue.metadata.provinces.length;i++){
								if(partnerContainerVue.metadata.provinces[i].provName == partnerContainerVue.param.province){
									provCode = partnerContainerVue.metadata.provinces[i].provCode;
									break;
								}
							}
							getCities(provCode,partnerContainerVue.param.area);
						}
					}else{
						alert('获取城市数据(省份)失败！')
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
			var provCode = "";
			for(var i=0;i<partnerContainerVue.metadata.provinces.length;i++){
				if(partnerContainerVue.metadata.provinces[i].provName == partnerContainerVue.param.province){
					provCode = partnerContainerVue.metadata.provinces[i].provCode;
					break;
				}
			}
			getCities(provCode);
		},
		changeCity: function(){
			partnerContainerVue.param.area = '';
			partnerContainerVue.metadata.areas = [];
			var cityCode = "";
			for(var i=0;i<partnerContainerVue.metadata.cities.length;i++){
				if(partnerContainerVue.metadata.cities[i].cityName == partnerContainerVue.param.city){
					cityCode = partnerContainerVue.metadata.cities[i].cityCode;
					break;
				}
			}
			getAreas(cityCode);
		},
		getLocation:function(){
			this.param.locationX = 12.2345;
			this.param.locationY = 25.8765;
		},
		submit: function(){
			$.ajax({
				url: '/partner/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alert("合作伙伴基本信息修改成功！");
							$.extend(partnerContainerVue.initData,partnerContainerVue.param); 
							window.location.reload();
						}else{//出现逻辑错误
							alert(jsonRet.errmsg);
						}
					}else{
						alert('系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		},
		reset: function(){
			$.extend(partnerContainerVue.param,partnerContainerVue.initData); 
			var provCode = "";
			for(var i=0;i<partnerContainerVue.metadata.provinces.length;i++){
				if(partnerContainerVue.metadata.provinces[i].provName == partnerContainerVue.param.province){
					provCode = partnerContainerVue.metadata.provinces[i].provCode;
					break;
				}
			}
			getCities(provCode,partnerContainerVue.param.area);
		}
	}
});

partnerContainerVue.getAllProvinces();
function getCities(provCode,areaName){
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
				if(areaName){
					var cityCode = "";
					for(var i=0;i<partnerContainerVue.metadata.cities.length;i++){
						if(partnerContainerVue.metadata.cities[i].cityName == partnerContainerVue.param.city){
							cityCode = partnerContainerVue.metadata.cities[i].cityCode;
							break;
						}
					}
					getAreas(cityCode);
				}
			}else{
				alert('获取城市数据(地级市)失败！')
			}
		},
		dataType: 'json'
	});
}

function getAreas(cityCode){
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
				alert('获取城市数据(县)失败！')
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
        initialPreview: [ //预览图片的设置
            '<img src="${certShowBaseUrl}/logo" alt="LOGO照片" class="file-preview-image" style="width:96px">'
        ]
    });
    //异步上传错误结果处理
    $('#logoImg').on('fileerror', function(event, data, msg) {
		alert("营业执照照片文件上传失败！");
		$('#logoImg').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#logoImg").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alert("企业LOGO文件上传成功！！");
			}else{//出现逻辑错误
				alert(jsonRet.errmsg);
				$('#logoImg').fileinput('clear');
			}
		}else{
			alert('系统数据访问失败！')
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
        initialPreview: [ //预览图片的设置
            '<img src="${certShowBaseUrl}/idcard1" alt="法人身份证正面" class="file-preview-image" style="width:96px">'
        ]
    });
    //异步上传错误结果处理
    $('#certFile_1').on('fileerror', function(event, data, msg) {
		alert("身份证照片文件正面上传失败！");
		$('#certFile_1').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#certFile_1").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alert("身份证照片文件正面上传成功！！");
			}else{//出现逻辑错误
				alert(jsonRet.errmsg);
				$('#certFile_1').fileinput('clear');
			}
		}else{
			alert('系统数据访问失败！')
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
        initialPreview: [ //预览图片的设置
            '<img src="${certShowBaseUrl}/idcard2" alt="法人身份证反面" class="file-preview-image" style="width:100px;height:100px">'
        ]
    });
    //异步上传错误结果处理
    $('#certFile_2').on('fileerror', function(event, data, msg) {
		alert("身份证照片文件反面上传失败！");
		$('#certFile_2').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#certFile_2").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alert("身份证照片文件反面上传成功！！");
			}else{//出现逻辑错误
				alert(jsonRet.errmsg);
				$('#certFile_2').fileinput('clear');
			}
		}else{
			alert('系统数据访问失败！')
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
        initialPreview: [ //预览图片的设置
            '<img src="${certShowBaseUrl}/licence" alt="营业执照照片" class="file-preview-image" style="width:96px">'
        ]
    });
    //异步上传错误结果处理
    $('#licenceImg').on('fileerror', function(event, data, msg) {
		alert("营业执照照片文件上传失败！");
		$('#licenceImg').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#licenceImg").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				alert("营业执照照片文件上传成功！！");
			}else{//出现逻辑错误
				alert(jsonRet.errmsg);
				$('#licenceImg').fileinput('clear');
			}
		}else{
			alert('系统数据访问失败！')
			$('#licenceImg').fileinput('clear');
		}
    });
     
});
</script>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<div class="modal fade " id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorTitle" aria-hidden="false" data-backdrop="static">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="errorTitle" style="color:red">错误提示</h4>
     		</div>
     		<div class="modal-body">
       			<p> ${errmsg} </p><p/>
     		</div>
     		<div class="modal-footer">
     			<div style="margin-left:50px">
        			</div>
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
$("#errorModal").modal('show');
</script>
</#if>
</body>
</html>