<!DOCTYPE html>
<html lang="zh-CN">
<head>
   <#include "/head/page-common-head.ftl" encoding="utf8">
   <#include "/head/page-fileinput-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<#if (staff.partnerId)??>
<div class="container" id="container" style="oveflow:scroll">
<div class="row">
  <ul class="nav nav-tabs" style="margin:0 15%">
	 <li class="active" style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#staffBasic').show();$('#editPwd').hide();">
	   <a href="javascript:;">基本信息编辑</a>
	 </li>
	 <#if (partnerUserTP!'')=='staff'>
	 <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#staffBasic').hide();$('#editPwd').show();">
	   <a href="javascript:;">修改密码</a>
	 </li>
	 </#if>
	 <#if partnerUserTP=='bindVip' && ((staff.userId)!0) gt 0 >
	 <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#staffBasic').hide();$('#resetPwd').show();">
	    <a href="javascript:;">重置密码</a>
	  </li>
	  </#if>	  
  </ul>
</div>
<div id="staffBasic">
  <form class="form-horizontal" method ="post"  role="form" style="margin:20px 0">
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">合作伙伴ID<span style="color:red" >*</span></label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.partnerId" disabled  >
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">用户ID<span style="color:red" >*</span></label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.userId" maxLength=10 required placeholder="请输入员工的系统用户ID" >
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">内部员工ID</label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.staffId" maxLength=10  placeholder="请输入内部员工ID" >
	  </div>
	</div>	
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">员工昵称<span style="color:red" >*</span></label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.nickname" maxLength=20  required placeholder="请输入员工昵称" >
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">员工邮箱</label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.email" maxLength=100  placeholder="请输入员工邮箱" >
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">员工电话</label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.phone" maxLength=20  placeholder="请输入员工电话" >
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">员工职责介绍</label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <textarea class="form-control" v-model="params.introduce" maxLength=600  placeholder="请输入员工职责介绍" ></textarea>
	  </div>
	</div>
	
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">授予职能标签</label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <#if partnerUserTP=='bindVip'>
	    <select class="form-control" v-model="tagListArr" multiple placeholder="请选择授予员工的职能管理标签" >
	    <#else>
	    <select class="form-control" v-model="tagListArr" multiple disabled placeholder="请选择授予员工的职能管理标签" >
	    </#if> 
	     <optgroup label="------通用------">
	      <option value="basic">基本信息管理</option>
	     </optgroup>
	    <#if myPartner.pbTp == '1'>
	   	 <optgroup label="------商家合作伙伴专用------">
	      <option value="pimage">图库管理</option>
	      <option value="postage">运费模版管理</option>
	      <option value="goods">商品管理</option>
	      <option value="saleorder">销售订单管理</option>
	      <option value="aftersale">售后服务处理</option>
	      <option value="complain4p">投诉上级</option>
	      <option value="preceiver">退换货地址管理</option>
	     </optgroup>
	     </#if>
	     <#if myPartner.pbTp == '2'>
	     <optgroup label="------推广合作伙伴专用------">
	      <option value="reviewgds">商品审核</option>
	      <option value="mypartners">推广下级管理</option>
	      <option value="reviewappr">评价审核</option>
	      </optgroup>
	     </#if>
	     <#if myPartner.partnerId == SYS_PARTNERID>
	     <optgroup label="------系统专用------">
	      <option value="ComplainDeal">系统处理-投诉处理</option>
	      <option value="ComplainRevisit">系统处理-投诉回访</option>
	      <option value="CashapplyDeal">系统处理-提现处理</option>
	      <option value="kf4common">系统客服-用户(通用)</option>
	      <option value="kf4vip">系统客服-VIP</option>
	      <option value="kf4partner">系统客服-合作伙伴</option>
	      </optgroup>
	     </#if>
	    </select>
	  </div>
	</div>
	<#if partnerUserTP=='bindVip' &&  (staff.recId) == 0 >
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">初始密码<span style="color:red" >*</span></label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <input type="text" class="form-control" v-model="params.passwd" maxLength=10 required placeholder="请输入员工的初始密码" >
	  </div>
	</div>
	</#if>	
	<div class="form-group">
	  <label class="col-xs-4 control-label" style="padding-right:1px">是否客服<span style="color:red" >*</span></label>
	  <div class="col-xs-8" style="padding-left:1px">
	    <#if partnerUserTP=='staff'>
		  <label><input type="radio" value="0" v-model="params.isKf" style="display:inline-block;width:18px;height:18px" disabled >否</label>
		  <label><input type="radio" value="1" v-model="params.isKf" style="display:inline-block;width:18px;height:18px" disabled >是</label>
		<#else>
		  <label><input type="radio" value="0" v-model="params.isKf" style="display:inline-block;width:18px;height:18px" >否</label>
		  <label><input type="radio" value="1" v-model="params.isKf" style="display:inline-block;width:18px;height:18px" >是</label>		
		</#if>
	  </div>
	</div>
	<#if partnerUserTP=='staff'>
	<div v-show="${(staff.isKf)!''} == '1' " class="form-group">
	    <label class="col-xs-12 control-label" style="padding-right:1px">对外客服二维码<span style="color:red">*</span></label>
	    <div class="col-xs-12">
	        <input class="form-control" id="kfqrcode"  type="file" name="image" type="file" accept="image/*" class="file-loading">
	    </div>
	</div>
	<div v-show="${(staff.isKf)!''} == '1' " class="form-group">
	    <label class="col-xs-12 control-label" style="padding-right:1px">头像<span style="color:red">*</span></label>
	    <div class="col-xs-12">
	        <input class="form-control" id="headimg"  type="file" name="image" type="file" accept="image/*" class="file-loading">
	    </div>
	</div>	
	</#if>
    <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info" style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning" style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>	
  </form>

<script>
var staffBasicVue = new Vue({
	el:'#staffBasic',
	data:{
		params:{
			recId:'${(staff.recId)?string("#")}',
			partnerId:'${(staff.partnerId)?string("#")}',
			<#if (staff.userId)??>
			userId:'${(staff.userId)?string("#")}',
			<#else>
			userId:'',
			</#if>
			staffId:'${(staff.staffId)!""}',
			nickname:'${(staff.nickname)!""}',
			phone:'${(staff.phone)!""}',
			email:'${(staff.email)!""}',
			introduce:`${(staff.introduce)!""}`,
			isKf:'${(staff.isKf)!""}',
			tagList:'${(staff.tagList)!""}',
			<#if (staff.recId) == 0 >
			passwd:''
			</#if>
		},
		tagListArr:'${(staff.tagList)!''}'.split(','),
	},
	watch:{
		tagListArr :function(){
			this.params.tagList = this.tagListArr.join(',');
		}
	},
	methods:{
		submit: function(){
			$("#dealingData").show();
			$.ajax({
				url: '/pstaff/saveStaff',
				method:'post',
				data: this.params,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alertMsg('系统提示',"合作伙伴员工信息保存成功！");
							window.location.href='/pstaff/manage';
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
			window.location.reload();
		}
	}
});
<#if partnerUserTP=='staff'>
$(document).on('ready', function() {
    $("#kfqrcode").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/pstaff/upload/kfqrcode', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{},
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
        <#if (staff.isKf=='1' && ((staff.userId)!0) gt 0) >
        initialPreview: [ //预览图片的设置
            '<img src="/pstaff/${(staff.partnerId)?string('#')}/show/${(staff.userId)?string("#")}/kfqrcode" alt="客服二维码照片" class="file-preview-image" style="width:96px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#kfqrcode').on('fileerror', function(event, data, msg) {
		alertMsg('系统提示',"客服二维码照片文件上传失败！");
		$('#kfqrcode').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#kfqrcode").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				//alertMsg('系统提示',"客服二维码文件上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#kfqrcode').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#kfqrcode').fileinput('clear');
		}
    });
    
    $("#headimg").fileinput({
    	language: 'zh', //设置语言
        uploadUrl: '/pstaff/upload/headimg', //上传的地址
        deleteUrl:'',
        uploadAsync:true,
        showUpload: true, //是否显示上传按钮
        uploadExtraData:{},
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
        <#if (staff.isKf=='1' && ((staff.userId)!0) gt 0) >
        initialPreview: [ //预览图片的设置
            '<img src="/pstaff/${(staff.partnerId)?string('#')}/show/${(staff.userId)?string("#")}/headimg" alt="员工头像" class="file-preview-image" style="width:96px">'
        ]
        </#if>
    });
    //异步上传错误结果处理
    $('#headimg').on('fileerror', function(event, data, msg) {
		alertMsg('系统提示',"员工头像照片文件上传失败！");
		$('#headimg').fileinput('clear');
    });
    //异步上传成功结果处理
    $("#headimg").on("fileuploaded", function (event, data, previewId, index) {
    		var jsonRet = data.response;
    		if(jsonRet){
			if(0 == jsonRet.errcode){
				//alertMsg('系统提示',"员工头像文件上传成功！！");
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$('#headimg').fileinput('clear');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$('#headimg').fileinput('clear');
		}
    });    
});
</#if>   
</script>

</div>

<#if partnerUserTP=='staff'>
<!-- ========修改密码======== -->
<div id="editPwd" style="display:none;margin:20px 0">
<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >  
     <div class="form-group">
       <label class="col-xs-4 control-label" style="padding-right:1px">原密码<span style="color:red">*</span></label>
       <div class="col-xs-8" style="padding-left:1px">
         <input type="password" class="form-control" v-model="param.oldPasswd" pattern="\w{6,20}" maxLength=20 required placeholder="原密码（6-20个字符）">
       </div>
     </div>
     <div class="form-group">
       <label class="col-xs-4 control-label" style="padding-right:1px">新密码<span style="color:red">*</span></label>
       <div class="col-xs-8" style="padding-left:1px">
         <input type="password" class="form-control" v-model="param.newPasswd" pattern="\w{6,20}" maxLength=20 required placeholder="新密码（6-20个字符）">
       </div>
     </div>
     <div class="form-group">
       <label class="col-xs-4 control-label" style="padding-right:1px">确认密码<span style="color:red">*</span></label>
       <div class="col-xs-8" style="padding-left:1px">
         <input type="password" class="form-control" v-model="param.reNewPasswd" pattern="\w{6,20}" maxLength=20 required placeholder="确认密码（6-20个字符）">
       </div>
     </div>       
     <div class="form-group">
        <div style="text-align:center">
          <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
        </div>
     </div>
</form>
	
<script>
  var editpwdVue = new Vue({
	  el:'#editPwd',
	  data:{
		  param:{
			  oldPasswd:'',
			  newPasswd:'',
			  reNewPasswd:''
		  }
	  },
	  methods:{
		  submit: function(){
				$("#dealingData").show();
				//密码强度正则，6-20位，包括至少1个大写字母，1个小写字母，1个数字
				var pPattern = /^.*(?=.{6,20})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/;
				if(!pPattern.exec(this.param.newPasswd)){
					alertMsg('错误提示','新密码格式不正确(6-20位，包括至少1个大写字母，1个小写字母，1个数字)！');
					return;
				}
				if(this.param.newPasswd != this.param.reNewPasswd){
					alertMsg('错误提示','新密码与确认密码格式不一致！');
					return;
				}
				if(!this.param.oldPasswd){
					alertMsg('错误提示','原密码不可为空！');
					return;
				}
				$.ajax({
					url: '/pstaff/updpwd',
					method:'post',
					data: this.param,
					success: function(jsonRet,status,xhr){
						$("#dealingData").hide();
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode !== 0){
								alertMsg('错误提示',jsonRet.errmsg);
							}else{
								window.location.href = "/pstaff/manage";
							}
						}else{
							alertMsg('错误提示','修改员工密码失败！')
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
</div>
</#if>

<#if partnerUserTP=='bindVip' && ((staff.userId)!0) gt 0 >
<!-- 重置密码 -->
<div id="resetPwd" style="display:none;margin:20px 0">
<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" > 
   <div class="form-group">
     <label class="col-xs-3 control-label">用户ID<span style="color:red">*</span></label>
     <div class="col-xs-8">
       <input type="text" class="form-control" v-model="param.userId" maxLength=20 required readonly>
     </div>
   </div> 
   <div class="form-group">
     <label class="col-xs-3 control-label">新密码<span style="color:red">*</span></label>
     <div class="col-xs-8">
       <input type="text" class="form-control" v-model="param.newPasswd" pattern="\w{6,20}" maxLength=20 required placeholder="请输入新密码（6-20个字符）">
     </div>
   </div>      
   <div class="form-group">
      <div style="text-align:center">
        <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
      </div>
   </div>
</form>
<script>
  var resetPwdVue = new Vue({
	  el:'#resetPwd',
	  data:{
		  param:{
			  userId:'${(staff.userId)?string("#")}',
			  newPasswd:''
		  }
	  },
	  methods:{
		  submit: function(){
				$("#dealingData").show();
				if(!this.param.newPasswd || this.param.newPasswd.length<6 || this.param.newPasswd.length>20){
					alertMsg('错误提示','新密码长为6-20字符！');
					return;
				}
				$.ajax({
					url: '/pstaff/resetpwd',
					method:'post',
					data: this.param,
					success: function(jsonRet,status,xhr){
						$("#dealingData").hide();
						if(jsonRet && jsonRet.errmsg){
							if(jsonRet.errcode !== 0){
								alertMsg('错误提示',jsonRet.errmsg);
							}else{
								window.location.href = "/pstaff/manage";
							}
						}else{
							alertMsg('错误提示','修改员工密码失败！')
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
</div>


</#if>  
  
</div><!-- end of container -->

</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
