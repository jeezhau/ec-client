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
		    <div class="row" style="max-height:500px;overflow:scroll">
		      <ul class="list-group" style="padding:0 5px;background-color:white;margin:0 20px 0 20px;">
		        <li v-for="item in files" class="list-group-item">
		          <a v-if="isFileOrDir(item)===false" href="javascript:;" @click="listFiles(folder.join('/') + '/' + item)">
		            <img alt="文件夹" width=20px height=20px src="/icons/文件夹.png">&nbsp;&nbsp;&nbsp;{{item}}
		          </a>
		          <span v-if="isFileOrDir(item)===true">
		            <img alt="" width=20px height=20px  v-bind:src="'/image/file/show/' + item">
		            <span>&nbsp;&nbsp;&nbsp; {{item}}</span>
		            <input type="checkbox" style="display:inline-block;margin-left:10px;width:20px;height:20px" v-bind:value="item" v-model="selectedImages">
		          </span>
		        </li>
		      </ul>
		    </div>
		  </div>
		  <div class="row" style="margin-top:15px;text-align:center">
		  	<button class="btn btn-primary" @click="confirm">确认</button>
		  </div>
		</div>
     </div>
   </div>
</div><!-- end of modal -->
<script>
var imageGalleryShowVue = new Vue({
	el:'#imageGalleryShowModal',
	data:{
		selectCntLimit: 1, 	//选择限制数量
		targetElId: '',		//显示选择图片的元素ID
		selectedImages:[], 	//已选择的图片
		
		folder:['Home'],
		showMode:"list", 	//显示方式 list 、image
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
			if(imageGalleryShowVue.cacheFiles[folderPath] && imageGalleryShowVue.cacheFiles[folderPath].length>0){ //缓存查找
				imageGalleryShowVue.files = [];
				var cache = imageGalleryShowVue.cacheFiles[folderPath];
				for(var i=0;i<cache.length;i++){
					imageGalleryShowVue.files.push(cache[i]);
				}
				//更新层级目录
				var arr = folderPath.split("/");
				imageGalleryShowVue.folder = [];
				for(var i=0;i<arr.length;i++){
					imageGalleryShowVue.folder.push(arr[i]);
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
							imageGalleryShowVue.files = [];
							imageGalleryShowVue.cacheFiles[folderPath] = [];
							for(var i=0;i<jsonRet.files.length;i++){
								imageGalleryShowVue.files.push(jsonRet.files[i]);
								imageGalleryShowVue.cacheFiles[folderPath].push(jsonRet.files[i]);
							}
							//更新层级目录
							var arr = folderPath.split("/");
							imageGalleryShowVue.folder = [];
							for(var i=0;i<arr.length;i++){
								imageGalleryShowVue.folder.push(arr[i]);
							}
						}
					}else{
						alert('获取数据失败！')
					}
				},
				dataType: 'json'
			});
		},		
		isFileOrDir: function(filename){
			if(filename.lastIndexOf(".")>0){//普通文件
				return true;
			}else{	//目录
				return false;
			}
		},
		confirm : function(){
			if(this.selectCntLimit == 1 && this.selectedImages.length != 1){
				alert("您必须且仅可选择一张图片！");
				return ;
			}
			if(this.selectCntLimit > 1 && (this.selectedImages.length < 1 || this.selectedImages.length > this.selectCntLimit)){
				alert("您必须选择[1-" + this.selectCntLimit + "]张图片！");
				return ;
			}
			if(this.targetElId){
				$('#'+ this.targetElId).val(this.selectedImages.join(','));
				$('#imageGalleryShowModal').modal('hide');
			}
		}
	}
});
imageGalleryShowVue.listFiles('Home');
</script>

