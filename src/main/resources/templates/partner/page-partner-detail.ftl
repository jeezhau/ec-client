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
<div class="container " style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <a class="col-xs-2" href="/user/index/basic" style="vertical-algin:center;text-align:center"><img width="15px" height="15px" alt="" src="/icons/返回.png"></a>
     <h3 class="col-xs-9" style="margin:5px 0;text-align:center" >合作伙伴基本信息</h3>
  </div>
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white">
	<form class="form-horizontal" id="editForm" action="" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
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
        <label class="col-xs-4 control-label" style="padding-right:1px">个体户或公司名称<span style="color:red">*</span></label>
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
        <div style="text-align:center">
          <input type="hidden" v-model="param.locationX">
          <input type="hidden" v-model="param.locationY">
          <button type="button" class="btn btn-info" id="save" style="margin:20px" @click="submit"><span style="color:red">*</span>获取当前地址，请在经营详细地址操作</button>
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
		initData:{
			
		},	//初始化的数据
		metadata:{
			provinces:[{'provCode':'100001','provName':'北京市'},],
			cities:[],
			areas:[]
		},
		param:{
			country:'中国',
			province:'',
			city:'',
			area:'',
			addr:'',
			busiName:'',
			legalPername:'',
			legalPeridno:'',
			compName:'',
			licenceNo:'',
			phone:'',
			locationX:'',
			locationY:''
		}
	},
	methods:{
		getAllProvinces: function(){
			$.ajax({
				url: '/city/province/getall',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						editFormVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							editFormVue.metadata.provinces.push(jsonRet[i]);
						}
					}else{
						alert('获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		changeProvince: function(){
			editFormVue.param.city = '';
			editFormVue.param.area = '';
			editFormVue.metadata.cities = [];
			editFormVue.metadata.areas = [];
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
						alert('获取城市数据(地级市)失败！')
					}
				},
				dataType: 'json'
			});
		},
		changeCity: function(){
			editFormVue.param.area = '';
			editFormVue.metadata.areas = [];
			var cityCode = "";
			for(var i=0;i<editFormVue.metadata.cities.length;i++){
				if(editFormVue.metadata.cities[i].cityName == editFormVue.param.city){
					cityCode = editFormVue.metadata.cities[i].cityCode;
					break;
				}
			}
			$.ajax({
				url: '/city/area/getbycity/' + cityCode,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						editFormVue.metadata.areas = [];
						for(var i=0;i<jsonRet.length;i++){
							editFormVue.metadata.areas.push(jsonRet[i]);
						}
					}else{
						alert('获取城市数据(县)失败！')
					}
				},
				dataType: 'json'
			});
		},
		submit: function(){
			$.ajax({
				url: '/partner/save',
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
			
		}
	}
});
editFormVue.getAllProvinces();
function getBasic(){
	$.ajax({
		url: '/partner/get',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet){
				if(0 == jsonRet.errcode){
					if(jsonRet.datas){
						editFormVue.param.nickname = jsonRet.datas.nickname;
						editFormVue.param.phone = jsonRet.datas.phone;
						editFormVue.param.birthday = jsonRet.datas.birthday;
						editFormVue.param.sex = jsonRet.datas.sex;
						editFormVue.param.province = jsonRet.datas.province;
						editFormVue.param.city = jsonRet.datas.city;
						editFormVue.param.profession = jsonRet.datas.profession;
						editFormVue.param.favourite = jsonRet.datas.favourite;
						editFormVue.param.introduce = jsonRet.datas.introduce;
						
						editFormVue.initData.nickname = jsonRet.datas.nickname;
						editFormVue.initData.phone = jsonRet.datas.phone;
						editFormVue.initData.birthday = jsonRet.datas.birthday;
						editFormVue.initData.sex = jsonRet.datas.sex;
						editFormVue.initData.province = jsonRet.datas.province;
						editFormVue.initData.city = jsonRet.datas.city;
						editFormVue.initData.profession = jsonRet.datas.profession;
						editFormVue.initData.favourite = jsonRet.datas.favourite;
						editFormVue.initData.introduce = jsonRet.datas.introduce;
					}
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
//getBasic();
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