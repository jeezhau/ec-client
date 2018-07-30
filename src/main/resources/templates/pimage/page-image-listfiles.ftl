<script type="text/javascript">
/**
 * 获取指定文件夹下的文件数据
 *
 * @param folderImgId	待获取数据的文件夹ID
 * @param folderName		待获取数据的文件夹名称
 * @param filesArr		保存获取数据的数据
 * @param currFolder		当前文件夹
 * @param currUpPathArr	从顶级到当前的文件夹路径数据
 */
function getFolderFiles(folderImgId,folderName,filesArr,currFolder,currUpPathArr){
	//查询路径组更新层级目录
	var fullFolderName = '';
	var index = 0;
	for(;index<currUpPathArr.length;index++){
		fullFolderName += "/" + currUpPathArr[index].folderName;
		if(currUpPathArr[index].folderId == folderImgId){
			break;
		}
	}
	if(index == currUpPathArr.length-1){
		return;
	}else if(index < currUpPathArr.length-1){
		currUpPathArr.splice(index+1,currUpPathArr.length-(index+1));
	}else{
		currUpPathArr.push({'folderId':folderImgId,'folderName':folderName});
		fullFolderName += "/" + folderName;
	}
	fullFolderName = fullFolderName.substring(1);
	currFolder.folderId = currUpPathArr[index].folderId;
	currFolder.fullFolderName = fullFolderName;
	currFolder.folderName = currUpPathArr[index].fileName;
	$.ajax({
		url: '/pimage/folder/list',
		method:'post',
		data: {'folderImgId':folderImgId},
		success: function(jsonRet,status,xhr){
			filesArr.splice(0,filesArr.length)
			if(jsonRet && jsonRet.files){
				for(var i=0;i<jsonRet.files.length;i++){
					filesArr.push(jsonRet.files[i]);
				}
			}else{
				if(jsonRet && jsonRet.errcode !==0 ){
					alertMsg('错误提示','获取数据失败！')
				}
			}
		},
		dataType: 'json'
	});
}
</script>