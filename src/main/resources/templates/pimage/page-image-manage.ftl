<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common4-head.ftl" encoding="utf8">
    <#include "/head/page-fileinput-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="oveflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container" id="container" style="padding:0;">
  <div class="row" style="margin:0px;width:100%">
    <div class="row" style="margin:0;width:100%;text-align:center">
      <ol class="breadcrumb " style="width:100%;margin-bottom:1px;background-color:white">
        <span>当前文件夹：</span>
        <li v-for="item in currUpPathArr" @click="listFiles(item.folderId,item.folderName)">/<a href="javascript:;">{{item.folderName}}</a></li>
      </ol>
    </div>
    <div class="row" style="width:100%;margin:0;padding:0 5px;background-color:white;margin:0 ;text-align:center;justify-content:center;">
	     <a class="btn" href="javascript:;" @click="createNewFolder"><img src="/icons/新建文件夹.png" width="25px" height="25px"><br>新建文件夹</a>
	     <a class="btn" href="javascript:;" @click="uploadImg"><img src="/icons/上传图片.png" width="25px" height="25px"><br>上传文件</a>
	     <a class="btn" href="javascript:;" @click="showMode='list'"><img src="/icons/列表-1.png" width="25px" height="25px"><br>显示</a>
	     <a class="btn" href="javascript:;" @click="showMode='image'"><img src="/icons/列表.png" width="25px" height="25px"><br>显示</a>
    </div>
    <div v-if="showMode=='list'" class="row" style="width:100%;margin:3px 0">
      <ul class="list-group" style="width:100%;padding:0 5px;background-color:white;">
        <li v-for="item in files" class="list-group-item" style="width:100%;">
          <a v-if="item.isDir==='1'" href="javascript:;" @click="listFiles(item.imgId,item.fileName)"><img alt="文件夹" width=20px height=20px src="/icons/文件夹.png">&nbsp;&nbsp;&nbsp;{{item.fileName}}</a>
          <span v-if="item.isDir==='0'"><img alt="" width=20px height=20px  v-bind:src="'/pimage/file/show/' + item.imgId"><span>&nbsp;&nbsp;&nbsp; {{item.fileName}}.{{item.imgType}}</span> ({{item.imgId}})</span>
        </li>
      </ul>
    </div>
    <div v-if="showMode=='image'" class="row" style="width:100%;margin:3px 0">
      <div v-for="item,index in files" class="col-6 col-sm-4 col-md-3 col-lg-3 col-xl-2" style="padding:3px 3px">
        <div style="position:relative;text-align:center;background-color:white;padding:2px">
          <a v-if="item.isDir==='1'" href="javascript:;" @click="listFiles(item.imgId,item.fileName)"><img alt="文件夹" style="height:160px;max-width:100%" src="/icons/文件夹.png"></a>
          <img v-if="item.isDir==='0'" alt="" style="height:160px;max-width:100%"  v-bind:src="'/pimage/file/show/' + item.imgId">
          <img v-if="item.usingCnt>0" alt="引用" style="position:absolute;right:3px;top:3px;height:15px;width:15px" src="/icons/引用.png">
        </div>
        <div style="width:100%;text-align:center;background-color:white;padding:2px">
         <span>{{item.fileName}}<span v-if="item.isDir==='0'">.{{item.imgType}}</span> ({{item.imgId}})</span>
        </div>
        <div style="width:100%;text-align:center;justify-content:center;align-items:center;background-color:white;padding:2px">
         <button type="button" class="btn btn-info" style="padding:0;margin:1px 0px"  @click="renameFile(index,item)"> 重命名</button>
         <button v-if="item.usingCnt<=0" type="button" class="btn btn-danger" style="padding:0;margin:1px 0px"  @click="deleteImg(index,item)">&nbsp;删除&nbsp;</button>
         <button v-if="item.isDir==='0'" type="button" class="btn btn-primary" style="padding:0;margin:1px 0px" @click="replaceImg(item.imgId,item.fileName)">&nbsp;替换&nbsp;</button>
         <button type="button" class="btn btn-secondary" style="padding:0;margin:1px 0px" @click="moveFile(index,item)">&nbsp;移动&nbsp;</button>
        </div>
      </div>
    </div>
  </div>
 
</div><!-- end of container -->
<#include "/pimage/page-image-listfiles.ftl" encoding="utf8">
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		currUpPathArr:[], //[{'folderId':'','folderName':'Home'}]
		currFolder:{}, //{'folderId':'','folderName':'Home/','fileName':''}
		showMode:"list", //显示方式 list 、image
		files:[]
	},
	methods:{
		listFiles: function(folderImgId,folderName){
			getFolderFiles(folderImgId,folderName,this.files,this.currFolder,this.currUpPathArr);
		},
		createNewFolder: function(){
			$("#createDirModal").modal('show');
			createDirVue.params.upFolderImgId = this.currFolder.folderId;
			createDirVue.params.upFolderName = this.currFolder.fullFolderName;
			createDirVue.params.fileName = '';
			createDirVue.params.title = '新建文件夹';
		},
		renameFile: function(index,file){
			$("#createDirModal").modal('show');
			createDirVue.params.imgId = file.imgId;
			createDirVue.params.index = index;
			createDirVue.params.upFolderName = file.fileName;
			createDirVue.params.fileName = '';
			createDirVue.params.title = '文件重命名';
		},
		moveFile: function(index,file){
			$("#moveFileModal").modal('show');
			moveFileVue.params.imgId = file.imgId;
			moveFileVue.params.tagertParentId = '';
			moveFileVue.srcFindex = index;
			moveFileVue.srcFile = file;
			moveFileVue.srcFullFolderName = this.currFolder.fullFolderName;
			if(moveFileVue.currUpPathArr.length<1){
				moveFileVue.files = [{imgId:'Home',isDir:'1',fileName:'Home'}];
			}
		},
		replaceImg: function(imgId,fileName){
			$("#uploadImgModal").modal('show');
			$('#uploadFolderName').val(this.currFolder.fullFolderName);
			initFileUpload('replace',1,imgId);
		},
		uploadImg: function(){
			$("#uploadImgModal").modal('show');
			$('#uploadFolderName').val(this.currFolder.fullFolderName);
			initFileUpload('upload',10,this.currFolder.folderId);
		},
		deleteImg: function(index,file){
			if(file.usingCnt >0){
				alertMsg('错误提示','该文件正在被引用中，不可删除！');
				return;
			}
			$.ajax({
				url: '/pimage/delete',
				method:'post',
				data: {'imgId':file.imgId},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.errcode ==0){
						containerVue.files.splice(index,1);
						alertMsg('系统提示','文件删除成功！');
					}else{
						alertMsg('错误提示',jsonRet.errmsg);
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.listFiles('Home','Home');
</script>

<!-- 新建文件夹以及文件重命名（Modal） -->
<div class="modal fade " id="createDirModal" tabindex="-1" role="dialog" aria-labelledby="createDirModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title" id="createDirModalLabel">{{params.title}}</h4>
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
         </div>
         <div class="modal-body">
            <form class="form-horizontal" method ="post" action="add" role="form" >
               <div class="form-group">
               </div>
			   <div class="form-group">
			      <label v-if="params.title=='文件重命名'" class="col-xs-4 control-label" style="padding-right:1px">原文件名<span style="color:red" >*</span></label>
			      <label v-if="params.title=='新建文件夹'" class="col-xs-4 control-label" style="padding-right:1px">上级目录<span style="color:red" >*</span></label>
			      <div class="col-xs-8" style="padding-left:1px">
			         <input type="text" class="form-control" v-model="params.upFolderName" readonly disabled >
			      </div>
			   </div>
			   <div class="form-group">
			      <label v-if="params.title=='新建文件夹'" class="col-xs-4 control-label" style="padding-right:1px">文件夹名<span style="color:red" >*</span></label>
			      <label v-if="params.title=='文件重命名'" class="col-xs-4 control-label" style="padding-right:1px">新文件名<span style="color:red" >*</span></label>
			      <div class="col-xs-8" style="padding-left:1px">
			         <input type="text" class="form-control" v-model="params.fileName" maxLength=10 required placeholder="请输入文件名(2-10字母数字汉字)..." >
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
			index:'',
			title:'',
			imgId:'',
			upFolderImgId:'',
			upFolderName:'',
			fileName:''
		}
	},
	methods:{
		submit:function(){
			$("#dealingData").show();
			var patt = new RegExp('^[a-zA-Z0-9_\u4e00-\u9fa5]{2,10}$');
			if(!patt.test(this.params.fileName)){
				alertMsg('错误提示',"文件名为(2-10字母数字汉字)！");
				return false;
			}
			var param = {};
			var url = '';
			if(this.params.title =='新建文件夹'){
				param = {'upFolderImgId':this.params.upFolderImgId,'fileName':this.params.fileName};
				url = '/pimage/folder/create';
			}else{
				param = {'imgId':this.params.imgId,'fileName':this.params.fileName};
				url = '/pimage/rename';
			}
			$.ajax({
				url: url,
				data: param,
				method:'post',
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(createDirVue.params.title == '新建文件夹' && jsonRet.image){
						containerVue.files.unshift(jsonRet.image);
					}else if(createDirVue.params.title == '文件重命名' && jsonRet.errcode ==0){
						var old = containerVue.files[createDirVue.params.index];
						old.fileName = createDirVue.params.fileName;
						containerVue.files.splice(createDirVue.params.index,1,old);
					}else{
						alertMsg('错误提示',jsonRet.errmsg);
					}
					$("#createDirModal").modal('hide');
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

<!-- 文件移动（Modal） -->
<div class="modal fade " id="moveFileModal" tabindex="-1" role="dialog" aria-labelledby="moveFileLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title" id="moveFileLabel">文件移动</h4>
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
         </div>
         <div class="modal-body">
          <div class="row">
		   <div class="col-6" style="padding-right:3px">
		     <div class="card " style="margin-bottom:0px;border-radius:0;">
	           <div class="card-header">
	             <h4 style="text-align:center">源文件</h4><br>
	             <span>{{srcFullFolderName}}</span>
	           </div>
		       <div class="card-body">
			     <div style="position:relative;text-align:center;padding:2px">
	               <img v-if="srcFile.isDir==='1'" alt="文件夹" style="height:160px;max-width:100%" src="/icons/文件夹.png">
	               <img v-if="srcFile.isDir==='0'" alt="" style="height:160px;max-width:100%"  v-bind:src="'/pimage/file/show/' + srcFile.imgId">
	               <img v-if="srcFile.usingCnt>0" alt="引用" style="position:absolute;right:3px;top:3px;height:15px;width:15px" src="/icons/引用.png">
	             </div>
	             <div style="text-align:center;padding:2px">
	               <span>{{srcFile.fileName}}<span v-if="srcFile.isDir==='0'">.{{srcFile.imgType}}</span> ({{srcFile.imgId}})</span>
	             </div>	
	           </div>	       
	         </div>
		   </div>
		   <div class="col-6" style="padding-left:3px">
		     <div class="card" style="margin-bottom:0px;border-radius:0;">
	           <div class="card-header"><h4 style="text-align:center">选择目标文件夹</h4></div>
		       <div class="card-body" style="padding:0 1px">
		         <div class="row" style="margin:0;width:100%">
				    <button class="btn btn-primary" @click="selTopFolder" > 顶 级 </button> &nbsp;&nbsp;
				     <ol class="breadcrumb " style="margin-bottom:1px;background-color:white">
				        <li v-for="item in currUpPathArr" @click="listFiles(item.folderId,item.folderName)">
				          /<a href="javascript:;">{{item.folderName}}</a>
				        </li>
				     </ol>
			    </div>
			    <div class="row" style="margin:0;width:100%">
			      <ul class="list-group" style="padding:0;background-color:white;width:100%">
			        <li v-for="item in files" v-if="item.isDir==='1'"  class="list-group-item" style="width:100%">
			          <label><input type="radio" v-model="params.targetParentId" :value="item.imgId" style="display:inline-block"></label>&nbsp;&nbsp;&nbsp;
			          <a href="javascript:;" @click="listFiles(item.imgId,item.fileName)"><img alt="文件夹" width=20px height=20px src="/icons/文件夹.png">&nbsp;&nbsp;&nbsp;{{item.fileName}}</a>
			        </li>
			      </ul>
			    </div>
	           </div>
	         </div>	   
		   </div>
		   
		  </div>
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-success" v-on:click="submit">提交</button>
            <button type="button" class="btn btn-danger" data-dismiss="modal">关闭</button>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script type="text/javascript">
var moveFileVue = new Vue({
	el:'#moveFileModal',
	data:{
		srcFindex:'',
		srcFile:{},
		srcFullFolderName:'',
		currUpPathArr:[], //[{'folderId':'','folderName':'Home'}]
		currFolder:{}, //{'folderId':'','folderName':'Home/','fileName':''}
		files:[],
		params:{
			imgId:'', 	//待移动文件
			targetParentId:'', //目标文件夹
		},
	},
	methods:{
		listFiles: function(folderImgId,folderName){
			this.params.targetParentId = '';
			getFolderFiles(folderImgId,folderName,this.files,this.currFolder,this.currUpPathArr);
		},
		selTopFolder:function(){
			this.currUpPathArr = [],
			this.currFolder = [],
			this.files = [{imgId:'Home',isDir:'1',fileName:'Home'}],
			this.params.targetParentId = '';
		},
		submit:function(){
			$("#dealingData").show();
			if(!this.params.targetParentId){
				alertMsg('错误提示',"目标文件夹:不可为空！");
				return false;
			}
			if(this.srcFile.parentId == this.params.targetParentId){
				alertMsg('错误提示',"目标文件夹:不可与原文件的父文件夹相同！");
				return false;
			}
			if(this.params.imgId == this.params.targetParentId){
				alertMsg('错误提示',"目标文件夹:不可与原文件相同！");
				return false;
			}
			$.ajax({
				url: '/pimage/move',
				data: this.params,
				method:'post',
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet.errcode ==0){
						containerVue.files.splice(moveFileVue.srcFindex,1);
					}else{
						alertMsg('错误提示',jsonRet.errmsg);
					}
					$("#moveFileModal").modal('hide');
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

<!-- 上传图片（Modal） -->
<div class="modal fade " id="uploadImgModal" tabindex="-1" role="dialog" aria-labelledby="uploadImgModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title" id="uploadImgModalLabel">上传图片</h4>
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
         </div>
         <div class="modal-body">
            <form class="form-horizontal" method ="post" action="add" role="form" >
               <div class="form-group">
               </div>
			   <div class="form-group">
			      <label class="col-xs-4 control-label" style="padding-right:1px">图片归属目录<span style="color:red" >*</span></label>
			      <div class="col-xs-8" style="padding-left:1px">
			         <input type="text" class="form-control" id="uploadFolderName" readonly disabled >
			      </div>
			   </div>
               <div class="form-group">
                 <label class="col-xs-4 control-label" style="padding-right:1px">上传图片<span style="color:red" >*</span></label>
                 <div class="col-xs-8" style="padding-left:1px">
                   <input id="upFile" type="file" name="image" type="file" multiple class="file-loading">
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
function initFileUpload(mode,imgCntLimit,imgId){
	
	$("#upFile").fileinput({
		language: 'zh', //设置语言
	    uploadUrl: '/pimage/file/' + mode, //上传的地址
	    deleteUrl:'',
	    uploadAsync:true,
	    showUpload: true, //是否显示上传按钮
	    uploadExtraData:function(){ 
	    		return {'imgId':imgId}
	    	},
	    deleteExtraData:{},
	    allowedFileExtensions : ['jpg', 'png','jpeg'],//接收的文件后缀
	    dropZoneEnabled:false,
	    maxFileCount: imgCntLimit, //表示允许同时上传的最大文件个数
	    //autoReplace:true,
	    enctype: 'multipart/form-data',
	    msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
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
	    maxFileSize: 1024,//单位为kb，如果为0表示不限制文件大小
	    previewSettings: {
	        image: {width: "100px", height: "100px"},
	    }
	});
	$('#upFile').fileinput('refresh',{uploadExtraData:{'imgId':imgId},uploadUrl:'/pimage/file/' + mode, maxFileCount:imgCntLimit});
	//异步上传错误结果处理
	$('#upFile').on('fileerror', function(event, data, msg) {
		alertMsg('错误提示',"照片文件上传失败！");
		$('#upFile').fileinput('clear');
		$("#uploadImgModal").modal('hide');
	});
	//异步上传成功结果处理
	$("#upFile").on("fileuploaded", function (event, data, previewId, index) {
		var jsonRet = data.response;
		if(jsonRet && jsonRet.image){
			if(mode == 'upload'){
				for(var i=0;i<containerVue.files.length;i++){
					if(containerVue.files[i].imgId == jsonRet.image.imgId){
						containerVue.files.splice(i,1,jsonRet.image);
						break;
					}
				}
				if(i==containerVue.files.length){
					containerVue.files.push(jsonRet.image);
				}
			}else{
				alertMsg('系统提示',"执行页面刷新即可显示新图片！");
				/* for(var i=0;i<containerVue.files.length;i++){
					if(containerVue.files[i].imgId == jsonRet.image.imgId){
						containerVue.files.splice(i,1,jsonRet.image);
						break;
					}
				} */
			}
		}else{
			if(jsonRet && jsonRet.errmsg){
				alertMsg('错误提示',jsonRet.errmsg);
			}else{
				alertMsg('错误提示','系统数据访问失败！');
			}
		}
		$("#uploadImgModal").modal('hide');
	});
}
</script>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
