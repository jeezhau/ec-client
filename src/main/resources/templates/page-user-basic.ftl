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
<div class="container " style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <a class="col-xs-2" href="/user/index/basic" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >基本信息修改</h3>
  </div>
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white">
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
          <input class="form-control" v-model="param.province" maxLength=100 placeholder="请输入省份" >
        </div>
      </div>         
      <div class="form-group">
        <label for="city" class="col-xs-3 control-label">所在城市</label>
        <div class="col-xs-8">
          <input class="form-control" v-model="param.city"  maxLength=100 placeholder="请输入城市" >
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

</div><!-- end of container -->
<script type="text/javascript">
var editFormVue = new Vue({
	el:'#editForm',
	data:{
		initData:{},	//初始化的数据
		medadata:{
			province:[],
			city:[]
		},
		param:{
			openId:'',
			nickname:'',
			birthday:'',
			phone:'',
			sex:'',
			province:'',
			city:'',
			favourite:'',
			profession:'',
			introduce:''
		}
	},
	methods:{
		submit: function(){
			$.ajax({
				url: '/user/basic/update',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alert("基本信息修改成功！")
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
			this.param.openId = this.initData.openId;
			this.param.nickname = this.initData.nickname;
			this.param.phone = this.initData.phone;
			this.param.birthday = this.initData.birthday;
			this.param.sex = this.initData.sex;
			this.param.province = this.initData.province;
			this.param.city = this.initData.city;
			this.param.profession = this.initData.profession;
			this.param.favourite = this.initData.favourite;
			this.param.introduce = this.initData.introduce;
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
					editFormVue.param.openId = jsonRet.datas.openId;
					editFormVue.param.nickname = jsonRet.datas.nickname;
					editFormVue.param.phone = jsonRet.datas.phone;
					editFormVue.param.birthday = jsonRet.datas.birthday;
					editFormVue.param.sex = jsonRet.datas.sex;
					editFormVue.param.province = jsonRet.datas.province;
					editFormVue.param.city = jsonRet.datas.city;
					editFormVue.param.profession = jsonRet.datas.profession;
					editFormVue.param.favourite = jsonRet.datas.favourite;
					editFormVue.param.introduce = jsonRet.datas.introduce;
					
					editFormVue.initData.openId = jsonRet.datas.openId;
					editFormVue.initData.nickname = jsonRet.datas.nickname;
					editFormVue.initData.phone = jsonRet.datas.phone;
					editFormVue.initData.birthday = jsonRet.datas.birthday;
					editFormVue.initData.sex = jsonRet.datas.sex;
					editFormVue.initData.province = jsonRet.datas.province;
					editFormVue.initData.city = jsonRet.datas.city;
					editFormVue.initData.profession = jsonRet.datas.profession;
					editFormVue.initData.favourite = jsonRet.datas.favourite;
					editFormVue.initData.introduce = jsonRet.datas.introduce;
				}else{//出现逻辑错误
					alert(jsonRet.errmsg);
				}
			}else{
				alert('系统数据访问失败！')
			}
		},
		dataType: 'json'
	});
}
getBasic();
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