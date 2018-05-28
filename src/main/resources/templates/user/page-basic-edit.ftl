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
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container " style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li style="width:50%" class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editBasic').show();$('#updHeadImg').hide();">
	    <a href="javascript:;">基本信息编辑</a>
	  </li>
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editBasic').hide();$('#updHeadImg').show();">
	    <a href="javascript:;">头像变更</a>
	  </li>
	</ul>
  </div>
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white" id="editBasic">
	<form class="form-horizontal" id="editForm" action="" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
	  <div class="form-group">
	    <label class="col-xs-3 control-label">昵称<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="text" class="form-control" v-model="param.nickname" pattern="\w{3,20}" title="3-20个字符组成" maxLength=20 required placeholder="请输入昵称（3-20个字符）">
	    </div>
	  </div>  	  
	  <div class="form-group">
        <label class="col-xs-3 control-label">生日<span style="color:red">*</span></label>
        <div class="col-xs-6">
          <input class="form-control" type="date" v-model="param.birthday" required placeholder="请输入生日（2010-09-09）" >
        </div>
      </div>
 	  <div class="form-group">
        <label class="col-xs-3 control-label">性别<span style="color:red">*</span></label>
        <div class="col-xs-6">
          <select class="form-control" v-model="param.sex" required>
           <option value="0">保密</option>
           <option value="1">男</option>
           <option value="2">女</option>
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-3 control-label">移动电话<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="tel" class="form-control" v-model="param.phone" maxLength=20 placeholder="请输入移动电话号码" >
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-3 control-label">所在省份</label>
        <div class="col-xs-8">
          <select class="form-control" v-model="param.province" v-on:change="changeProvince">
            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
          </select>
        </div>
      </div>        
      <div class="form-group">
        <label for="city" class="col-xs-3 control-label">所在城市</label>
        <div class="col-xs-8">
          <select class="form-control"  v-model="param.city" >
            <option v-for="item in metadata.cities" v-bind:value="item.cityName">{{item.cityName}}</option>
          </select>
        </div>
      </div> 
      <div class="form-group">
        <label for="favourite" class="col-xs-3 control-label">兴趣爱好</label>
        <div class="col-xs-8">
          <input class="form-control" v-model="param.favourite"  maxLength=200  placeholder="请输入兴趣爱好,最长200字符" >
        </div>
      </div> 
      <div class="form-group">
        <label for="profession" class="col-xs-3 control-label">职业</label>
        <div class="col-xs-8">
          <input class="form-control" v-model="param.profession"  maxLength=100   placeholder="请输入职业,最长100字符" >
        </div>
      </div>       
      <div class="form-group">
        <label for="introduce" class="col-xs-3 control-label">个人简介</label>
        <div class="col-xs-8">
          <textarea class="form-control" v-model="param.introduce"  maxLength=600 rows=5 required placeholder="请输入个人简介,最长600字符"></textarea>
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
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white;display:none" id="updHeadImg">
    	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >  
    	  <div class="form-group">
	    <div class="col-xs-12">
	        <input class="form-control" id=headimg  type="file" name="image" type="file" accept="image/jpg" class="file-loading">
	    </div>
	  </div>
	</form>
  </div>
</div><!-- end of container -->
<script type="text/javascript">
var editFormVue = new Vue({
	el:'#editForm',
	data:{
		initData:{},	//初始化的数据
		metadata:{
			provinces:[],
			cities:[]
		},
		param:{
			nickname:'',
			birthday:'',
			phone:'',
			sex:'',
			province:'',
			city:'',
			favourite:'',
			profession:'',
			introduce:'',
		}
	},
	methods:{
		getAllProvinces: function(){
			$.ajax({
				url: '/city/province/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						editFormVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							editFormVue.metadata.provinces.push(jsonRet[i]);
						}
						if(editFormVue.param.city){//有城市参数
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
			editFormVue.param.city = '';
			editFormVue.param.area = '';
			editFormVue.metadata.cities = [];
			getCities();
		},
		submit: function(){
			$("#dealingData").show();
			$.ajax({
				url: '/user/basic/update',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"基本信息修改成功！")
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
			$.extend(editFormVue.param,editFormVue.initData); 
			getCities();
		}
	}
});
function getBasic(){
	$.ajax({
		url: '/user/basic/get',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet){
				if(0 == jsonRet.errcode){
					editFormVue.param.nickname = jsonRet.datas.nickname;
					editFormVue.param.phone = jsonRet.datas.phone;
					editFormVue.param.birthday = jsonRet.datas.birthday;
					editFormVue.param.sex = jsonRet.datas.sex;
					editFormVue.param.province = jsonRet.datas.province;
					editFormVue.param.city = jsonRet.datas.city;
					editFormVue.param.profession = jsonRet.datas.profession;
					editFormVue.param.favourite = jsonRet.datas.favourite;
					editFormVue.param.introduce = jsonRet.datas.introduce;
					$.extend(editFormVue.initData,editFormVue.param); 
					
					editFormVue.getAllProvinces();
				}else{//出现逻辑错误
					alertMsg('错误提示',jsonRet.errmsg);
				}
			}else{
				alertMsg('错误提示','系统数据访问失败！')
			}
		},
		dataType: 'json'
	});
}
getBasic();

function getCities(){
	var provCode = "";
	for(var i=0;i<editFormVue.metadata.provinces.length;i++){
		if(editFormVue.metadata.provinces[i].provName == editFormVue.param.province){
			provCode = editFormVue.metadata.provinces[i].provCode;
			break;
		}
	}
	$.ajax({
		url: '/city/city/getbyprov/' + provCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				editFormVue.metadata.cities = [];
				for(var i=0;i<jsonRet.length;i++){
					editFormVue.metadata.cities.push(jsonRet[i]);
				}
			}else{
				alertMsg('错误提示','获取城市数据(地级市)失败！')
			}
		},
		dataType: 'json'
	});
}
//头像上传
$("#headimg").fileinput({
	language: 'zh', //设置语言
    uploadUrl: '/user/headimg/upload', //上传的地址
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
    		<#if ((userBasic.headimgurl)!'')?starts_with('http')>'<img alt="头像" src="${userBasic.headimgurl}" class="file-preview-image" style="width:96px"> '</#if>
    		<#if !((userBasic.headimgurl)!'')?starts_with('http')>'<img alt="头像" src="/user/headimg/show/${(userBasic.userId)?string('#')}" class="file-preview-image" style="width:96px"> '</#if>
    ]
});
//异步上传错误结果处理
$('#headimg').on('fileerror', function(event, data, msg) {
	alertMsg('错误提示',"头像上传失败！");
	$('#headimg').fileinput('clear');
});
//异步上传成功结果处理
$("#headimg").on("fileuploaded", function (event, data, previewId, index) {
		var jsonRet = data.response;
		if(jsonRet){
		if(0 == jsonRet.errcode){
			alertMsg('系统提示',"头像上传成功！！");
		}else{//出现逻辑错误
			alertMsg('错误提示',jsonRet.errmsg);
			$('#headimg').fileinput('clear');
		}
	}else{
		alertMsg('错误提示','系统数据访问失败！')
		$('#headimg').fileinput('clear');
	}
});

</script>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
