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
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<div class="container" id="container" style="oveflow:scroll">
  <div class="row">
     <h3 style="margin:10px 0;text-align:center" >我的图库</h3>
  </div>
  <div class="row" style="margin-top:10px;">
    <div class="row">
	    <div class="col-xs-12" style="padding:0 15px">
	      <ol class="breadcrumb " style="margin-bottom:1px;background-color:white">
	        <span>当前文件夹：</span>
	        <li v-for="(item,index) in folder" @click="listFiles(getFolderPath(index))">
	          <a href="javascript:;">{{item}}</a>
	        </li>
	      </ol>
	    </div>
    </div>
    <div class="row">
	    <div class="col-xs-12" style="padding:0 5px;background-color:white;margin:0 20px 0 3px;text-align:center">
	     <a class="btn" href="javascript:;" @click="createNewFolder"><img src="/icons/新建文件夹.png" width="20px" height="20px"></a>
	     <a class="btn" href="javascript:;" @click="uploadImg"><img src="/icons/上传图片.png" width="20px" height="20px"></a>
	    </div>
    </div>
    <div class="row">
      <ul class="list-group" style="padding:0 5px;background-color:white;margin:0 20px 0 20px;">
        <li v-for="item in files" class="list-group-item">
          <a v-if="isFileOrDir(item)===false" href="javascript:;" @click="listFiles(folder.join('/') + '/' + item)"><img alt="文件夹" width=20px height=20px src="/icons/文件夹.png">&nbsp;&nbsp;&nbsp;{{item}}</a>
          <span v-if="isFileOrDir(item)===true"><img alt="" width=20px height=20px  v-bind:src="'/image/file/show/${(vipBasic.vipId)?string('#')}/' + item"><span>&nbsp;&nbsp;&nbsp; {{item}}</span></span>
        </li>
      </ul>
    </div>
  </div>
 
</div><!-- end of container -->
<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		folder:['Home'],
		showMode:"list", //显示方式 list 、image
		files:[],
		cacheFiles:{
			
		}
	},
	methods:{
		getFolderPath: function(index){
			var folderPath = "";
			for(var i = 0;i<=index;i++){
				folderPath += this.folder[i] + "/";
			}
			folderPath = folderPath.substring(0,folderPath.length-1);
			return folderPath;
		},
		listFiles: function(folderPath){
			//查询缓存数据
			if(containerVue.cacheFiles[folderPath] && containerVue.cacheFiles[folderPath].length>0){ //缓存查找
				containerVue.files = [];
				var cache = containerVue.cacheFiles[folderPath];
				for(var i=0;i<cache.length;i++){
					containerVue.files.push(cache[i]);
				}
				//更新层级目录
				var arr = folderPath.split("/");
				containerVue.folder = [];
				for(var i=0;i<arr.length;i++){
					containerVue.folder.push(arr[i]);
				}
				return;
			}
			$.ajax({
				url: '/image/folder/list',
				method:'post',
				data: {'folderPath':folderPath},
				success: function(jsonRet,status,xhr){
					if(jsonRet ){
						if(jsonRet.files){
							containerVue.files = [];
							containerVue.cacheFiles[folderPath] = [];
							for(var i=0;i<jsonRet.files.length;i++){
								containerVue.files.push(jsonRet.files[i]);
								containerVue.cacheFiles[folderPath].push(jsonRet.files[i]);
							}
							//更新层级目录
							var arr = folderPath.split("/");
							containerVue.folder = [];
							for(var i=0;i<arr.length;i++){
								containerVue.folder.push(arr[i]);
							}
						}
					}else{
						alertMsg('错误提示','获取数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		createNewFolder: function(){
			$("#createDirModal").modal('show');
			createDirVue.params.upFolderPath = this.folder.join("/");
			createDirVue.params.folderName = '';
		},
		uploadImg: function(){
			$("#uploadImgModal").modal('show');
			$('#uploadFolderPath').val(this.folder.join("/"));
			initFileUpload(this.folder.join("/"));
		},		
		isFileOrDir: function(filename){
			if(filename.lastIndexOf(".")>0){//普通文件
				return true;
			}else{	//目录
				return false;
			}
		}
	}
});
containerVue.listFiles('Home');
</script>
<!-- 新建文件夹（Modal） -->
<div class="modal fade " id="createDirModal" tabindex="-1" role="dialog" aria-labelledby="createDirModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="createDirModalLabel">新建文件夹</h4>
         </div>
         <div class="modal-body">
            <form class="form-horizontal" method ="post" action="add" role="form" >
               <div class="form-group">
               </div>
			   <div class="form-group">
			      <label class="col-xs-3 control-label" style="padding-right:1px">上级目录<span style="color:red" >*</span></label>
			      <div class="col-xs-9" style="padding-left:1px">
			         <input type="text" class="form-control" v-model="params.upFolderPath" readonly disabled >
			      </div>
			   </div>
			   <div class="form-group">
			      <label class="col-xs-3 control-label" style="padding-right:1px">新建文件夹名<span style="color:red" >*</span></label>
			      <div class="col-xs-9" style="padding-left:1px">
			         <input type="text" class="form-control" v-model="params.folderName" maxLength=10 required placeholder="请输入新建文件夹名(2-10字母数字汉字)..." >
			      </div>
			   </div>
			</form>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="submit">提交</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
var createDirVue = new Vue({
	el:'#createDirModal',
	data:{
		params:{
			upFolderPath:'',
			folderName:''
		}
	},
	methods:{
		submit:function(){
			var patt = new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5]{2,10}$');
			if(!patt.test(this.folderName)){
				alertMsg('错误提示',"新建文件夹名为(2-10字母数字汉字)！");
				return false;
			}
			$.ajax({
				url: '/image/folder/create',
				data: this.params,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							containerVue.files.push(createDirVue.params.folderName);
							if(!containerVue.cacheFiles[createDirVue.params.upFolderPath]){//还没有缓存
								containerVue.cacheFiles[createDirVue.params.upFolderPath] = [];
							}
							var cache = containerVue.cacheFiles[createDirVue.params.upFolderPath];
							cache.push(createDirVue.params.folderName);
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！');
					}
					$("#createDirModal").modal('hide');
				},
				dataType: 'json'
			});
		}
	}
});
</script>
<!-- 上传图片（Modal） -->
<div class="modal fade " id="uploadImgModal" tabindex="-1" role="dialog" aria-labelledby="uploadImgModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="uploadImgModalLabel">上传图片</h4>
         </div>
         <div class="modal-body">
            <form class="form-horizontal" method ="post" action="add" role="form" >
               <div class="form-group">
               </div>
			   <div class="form-group">
			      <label class="col-xs-3 control-label" style="padding-right:1px">图片归属目录<span style="color:red" >*</span></label>
			      <div class="col-xs-9" style="padding-left:1px">
			         <input type="text" class="form-control" id="uploadFolderPath" readonly disabled >
			      </div>
			   </div>
               <div class="form-group">
                 <label class="col-xs-3 control-label" style="padding-right:1px">上传图片<span style="color:red" >*</span></label>
                 <div class="col-xs-9" style="padding-left:1px">
                   <input id="upFile" type="file" name="image" type="file" class="file-loading">
                 </div>
                </div>
			</form>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
function initFileUpload(folderPath){
	$("#upFile").fileinput({
		language: 'zh', //设置语言
	    uploadUrl: '/image/file/upload', //上传的地址
	    deleteUrl:'',
	    uploadAsync:true,
	    showUpload: true, //是否显示上传按钮
	    uploadExtraData:{'folderPath':folderPath},
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
	    }
	});
	//异步上传错误结果处理
	$('#upFile').on('fileerror', function(event, data, msg) {
		alertMsg('错误提示',"照片文件上传失败！");
		$('#upFile').fileinput('clear');
		$("#uploadImgModal").modal('hide');
	});
	//异步上传成功结果处理
	$("#upFile").on("fileuploaded", function (event, data, previewId, index) {
		var jsonRet = data.response;
		if(jsonRet){
			if(0 == jsonRet.errcode){
				var folderPath = $('#uploadFolderPath').val();
				var filename = jsonRet.filename;//返回的文件名
				if(containerVue.files[containerVue.files.length-1] != filename){
					containerVue.files.push(filename);
				}
				if(!containerVue.cacheFiles[folderPath]){//还没有缓存
					containerVue.cacheFiles[folderPath] = [];
				}
				var cache = containerVue.cacheFiles[folderPath];
				if(cache[cache.length-1] != filename){
					cache.push(filename);
				}
				$("#uploadImgModal").modal('hide');
			}else{//出现逻辑错误
				alertMsg('错误提示',jsonRet.errmsg);
				$("#uploadImgModal").modal('hide');
			}
		}else{
			alertMsg('错误提示','系统数据访问失败！')
			$("#uploadImgModal").modal('hide');
		}
	});
}
</script>

</body>
</html>