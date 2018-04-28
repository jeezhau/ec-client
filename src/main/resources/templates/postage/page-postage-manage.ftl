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

</head>
<body class="light-gray-bg">
<div class="container" id="container" style="oveflow:scroll">
  <div class="row">
     <h3 style="margin:10px 0;text-align:center" >我的运费模版</h3>
  </div>
  <div class="row" style="margin-top:10px;">
    <div class="row" style="margin:0 15px">
	   <a class="btn btn-info" href="/postage/edit/0">新建</a>
    </div>
    <div class="row">
      <ul class="list-group" style="padding:0 0px;background-color:white;margin:5px 20px 0 20px;">
        <li v-for="item in postages" class="list-group-item">
          <a  v-bind:href="'/postage/edit/' + item.postageId"><span>{{item.postageName}}</span></a>
          <a  href="javascript:;" class="badge" style="background-color:white" @click="deletePostageTpl(item.postageId)"><img alt="" width=20px height=20px src="/icons/删除1.png"></a>
        </li>
      </ul>
    </div>
  </div>
 
</div><!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		postages:[{postageId:1,postageName:'tp1'}]
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
						alert('获取数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		deletePostageTpl: function(postageId){
			if(confirm("您确定要删除该模版码吗？")){
				
			}else{
				return ;
			}
		}
	}
});
//containerVue.listFiles('Home');
</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
</body>
</html>