<!-- 图库显示Model -->
<div class="modal fade " id="imageGalleryShowModal" tabindex="-1" role="dialog" aria-labelledby="imageGalleryShowModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="imageGalleryShowModalLabel">图片选择</h4>
         </div>
         <div class="modal-body">
		  <div class="row" style="margin-top:10px;">
		    <div class="row" style="margin:0">
			    <div class="col-xs-12" style="padding:0 5px;background-color:white;margin:0 ;text-align:center">
			     <a class="btn" href="javascript:;" @click="showMode='list'"><img src="/icons/列表-1.png" width="25px" height="25px"><br>显示</a>
			     <a class="btn" href="javascript:;" @click="showMode='image'"><img src="/icons/列表.png" width="25px" height="25px"><br>显示</a>
			    </div>
		    </div>
		    <div class="row">
			    <div class="col-xs-12" style="padding:0 15px">
			      <ol class="breadcrumb " style="margin-bottom:1px;background-color:white">
			        <span>当前文件夹：</span>
			        <li v-for="item in currUpPathArr" @click="listFiles(item.folderId,item.folderName)">
	                  <a href="javascript:;">{{item.folderName}}</a>
	                </li>
			      </ol>
			    </div>
		    </div>		    
		    <div v-if="showMode=='list'" class="row" style="margin:3px 0">
		      <ul class="list-group" style="padding:0 5px;background-color:white;">
		        <li v-for="item in files" class="list-group-item">
		          <input v-if="item.isDir=='0'" type="checkbox" style="display:inline-block;margin-left:10px;width:20px;height:20px" v-bind:value="item.imgId" v-model="selectedImages">
		          <a v-if="item.isDir==='1'" href="javascript:;" @click="listFiles(item.imgId,item.fileName)"><img alt="文件夹" width=20px height=20px src="/icons/文件夹.png">&nbsp;&nbsp;&nbsp;{{item.fileName}}</a>
		          <span v-if="item.isDir==='0'"><img alt="" width=20px height=20px  v-bind:src="'/pimage/file/show/' + item.imgId"><span>&nbsp;&nbsp;&nbsp; {{item.fileName}}.{{item.imgType}}</span> ({{item.imgId}})</span>
		        </li>
		      </ul>
		    </div>
		    <div v-if="showMode=='image'" class="row" style="margin:3px 0">
		      <div v-for="item,index in files" class="col-xs-6 col-sm-4 col-md-3 col-lg-3" style="padding:3px 3px">
		        <div style="position:relative;text-align:center;background-color:white;padding:2px">
		          <a v-if="item.isDir==='1'" href="javascript:;" @click="listFiles(item.imgId,item.fileName)"><img alt="文件夹" style="height:160px;max-width:100%" src="/icons/文件夹.png"></a>
		          <img v-if="item.isDir==='0'" alt="" style="height:160px;max-width:100%"  v-bind:src="'/pimage/file/show/' + item.imgId">
		          <img v-if="item.usingCnt>0" alt="引用" style="position:absolute;right:3px;top:3px;height:15px;width:15px" src="/icons/引用.png">
		        </div>
		        <div style="text-align:center;background-color:white;padding:2px">
		         <input v-if="item.isDir=='0'" type="checkbox" style="display:inline-block;margin-left:10px;width:20px;height:20px" v-bind:value="item.imgId" v-model="selectedImages">
		         <span>{{item.fileName}}<span v-if="item.isDir==='0'">.{{item.imgType}}</span> ({{item.imgId}})</span>
		        </div>
		      </div>
		    </div>
		  </div>
		  <div class="row" style="margin-top:15px;text-align:center">
		  	<button class="btn btn-primary" @click="confirm">确认</button>
		  </div>
		</div>
     </div>
   </div>
</div><!-- end of modal -->
<#include "/pimage/page-image-listfiles.ftl" encoding="utf8">
<script>
var imageGalleryShowVue = new Vue({
	el:'#imageGalleryShowModal',
	data:{
		selectCntLimit: 1, 	//选择限制数量
		targetElId: '',		//显示选择图片的元素ID
		selectedImages:[], 	//已选择的图片
		currUpPathArr:[], //[{'folderId':'','folderName':'Home'}]
		currFolder:{}, //{'folderId':'','folderName':'Home/','fileName':''}
		showMode:"list", 	//显示方式 list 、image
		files:[],
	},
	methods:{
		callbackFun:null,	//确认回调函数
		listFiles: function(folderImgId,folderName){
			getFolderFiles(folderImgId,folderName,this.files,this.currFolder,this.currUpPathArr);
		},
		confirm : function(){
			if(this.selectCntLimit == 1 && this.selectedImages.length != 1){
				alertMsg('错误提示',"您必须且仅可选择一张图片！");
				return ;
			}
			if(this.selectCntLimit > 1 && (this.selectedImages.length < 1 || this.selectedImages.length > this.selectCntLimit)){
				alertMsg('错误提示',"您必须选择[1-" + this.selectCntLimit + "]张图片！");
				return ;
			}
			if(this.callbackFun){
				this.callbackFun(this.selectedImages.join(','));
				$('#imageGalleryShowModal').modal('hide');
			}
		}
	}
});
imageGalleryShowVue.listFiles('Home','Home');
</script>

