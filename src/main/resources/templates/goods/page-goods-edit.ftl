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
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    
</head>
<body class="light-gray-bg">
<div class="container" id="goodsContainer" style="padding:0px 0px;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li style="width:50%" class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editGoods').show();$('#reviewInfo').hide();">
	    <a href="javascript:;">基本信息编辑</a>
	  </li>
	  <li style="width:50%" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editGoods').hide();$('#reviewInfo').show();">
	    <a href="javascript:;">审批反馈</a>
	  </li>
	</ul>
  </div>
  <div class="row" id="editGoods" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
    <h5 style="text-align:center">商品基本信息编辑</h5>
    <h6 style="color:red">
      &nbsp;&nbsp;&nbsp;&nbsp;所有信息必须确保真实可靠，不可出现描述与真实品质不符的情况！<br>
    </h6>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">商品名称<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.goodsName" required maxLength="100" placeHolder="请输入商品名称2-100字符">
        </div>
      </div>         
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">商品分类<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control"  v-model="param.categoryId" required>
            <option v-for="item in metadata.categories" v-bind:value="item.id">{{item.name}}</option>
          </select>
        </div>
      </div> 
       <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">商品主图<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-9" style="padding-left:0"><input class="form-control"  id="mainImgPath" v-model="param.mainImgPath" required readonly placeholder="请选择图片"></div>
          <div class="col-xs-3" style="padding-left:0"><button class="btn btn-info" @click="selectImage(1,'mainImgPath')">选择</button></div>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">商品轮播图<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
           <div class="col-xs-9" style="padding-left:0"><textarea class="col-xs-9 form-control" id="carouselImgPaths" v-model="param.carouselImgPaths" maxLength=500 rows=10 readonly placeholder="请选择图片" ></textarea></div>
           <div class="col-xs-3" style="padding-left:0"><button class="btn btn-info" @click="selectImage(10,'carouselImgPaths')">选择</button></div>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">商品库存<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.stock"  min=0 max=999999  placeholder="请输入商品库存数量" >
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">限购数量<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="datetime" class="form-control" v-model="param.limitedNum" min=0 max=999999 placeholder="请输入每人限购数量，0表示不限购" >
        </div>
      </div>
      <div class="form-group" v-if="param.limitedNum>0">
        <label class="col-xs-4 control-label" style="padding-right:1px">限购开始时间<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="date" class="form-control" v-model="param.beginTime"  maxLength=20  required placeholder="请输入限购开始时间">
        </div>
      </div>
      <div class="form-group" v-if="param.limitedNum>0">
        <label class="col-xs-4 control-label" style="padding-right:1px">限购结束时间<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="date" class="form-control" v-model="param.endTime"  maxLength=20  required placeholder="请输入限购开始时间">
        </div>
      </div>      
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">配送模式<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.dispatchMode" required>
            <option value="1">官方统一配送</option>
            <option value="2">商家自行配送</option>
            <option value="3">快递配送</option>
            <option value="4">客户自取</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">销售范围<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.isCityWide" required>
            <option value="0">全国</option>
            <option value="1">同城</option>
          </select>
        </div>
      </div>
      <div class="form-group" v-if="param.isCityWide === '1' ">
        <label class="col-xs-4 control-label" style="padding-right:1px">同城配送距离<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.distLimit" required>
            <option value="0">全城</option>
            <option value="3">3km以内</option>
            <option value="5">5km以内</option>
            <option value="10">10km以内</option>
            <option value="20">20km以内</option>
          </select>
        </div>
      </div>
      <div class="form-group" v-if="param.isCityWide === '0' ">
        <label class="col-xs-4 control-label" style="padding-right:1px">全国配送省份<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.provLimit" required>
            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">运费模版<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.distrIds" required >
            <option v-for="item in metadata.postages" v-bind:value="item.postage_id">{{item.name}}</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <div class="col-xs-12" style="padding-left:1px">
          <label class="col-xs-6 control-label" >商品图文描述<span style="color:red">*</span></label>
        </div>
        <div class="col-xs-12" >
          <textarea class="form-control" v-model="param.goodsDesc"  maxLength=10000  rows=30 required 
          placeholder="    请输入10-10000字符的企业经营简介。
    编辑说明：最好请在文本编辑器中编辑好之后复制粘贴于此。
    相关格式说明：如果是一个段落请将段落内容放置于标签: <p>与</p>之间；换行则在句末添加标签：<br>；插入图片使用标签：<img href='  ' width = '100%' > ，href属性的内容来自图库中图片的链接(不是图片名称)。不清楚的话可以问问身边的做软件开发的朋友。"></textarea>
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

  <div class="row" id="reviewInfo" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<h5 style="text-align:center">最新审批结果信息</h5>
  	<p>审批时间：{{review.reviewTime}}</p>
  	<p>审批结果：{{getReviewResult()}} </p>
  	<p>审批意见：{{review.reviewLog}}</p>
  </div>
</div><!-- end of container -->
<script type="text/javascript">
var goodsContainerVue = new Vue({
	el:'#goodsContainer',
	data:{
		initData:{
			goodsId:'${(goods.goodsId)!-1}',
			goodsName:'${(goods.goodsName)!''}',
			categoryId:'${(goods.categoryId)!''}',
			goodsDesc:'${(goods.goodsDesc)!''}',
			mainImgPath:'${(goods.mainImgPath)!''}',
			carouselImgPaths:'${(goods.carouselImgPaths)!''}',
			stock:'${(goods.stock)!''}',
			limitedNum:'${(goods.limitedNum)!0}',
			beginTime:'${(goods.beginTime)!''}',
			endTime:'${(goods.endTime)!''}',
			dispatchMode:'${(goods.dispatchMode)!''}',
			isCityWide:'${(goods.isCityWide)!''}',
			distLimit:'${(goods.distLimit)!''}',
			provLimit:'${(goods.provLimit)!''}',
			distrIds:'${(goods.distrIds)!''}',
			status:'${(goods.status)!''}'
		},	//初始化的数据
		metadata:{
			categories:[],
			provinces:[],
			postages:[]
		},
		review:{
			reviewResult:'${(goods.reviewResult)!"0"}',
			reviewTime:'' ,
			reviewLog:"${(goods.reviewLog)!''}"
		},
		param:{
			id:'${(goods.id)!-1}',
			goodsName:'${(goods.goodsName)!''}',
			categoryId:'${(goods.categoryId)!''}',
			goodsDesc:'${(goods.goodsDesc)!''}',
			mainImgPath:'${(goods.mainImgPath)!''}',
			carouselImgPaths:'${(goods.carouselImgPaths)!''}',
			stock:'${(goods.stock)!''}',
			limitedNum:'${(goods.limitedNum)!''}',
			beginTime:'${(goods.beginTime)!''}',
			endTime:'${(goods.endTime)!''}',
			dispatchMode:'${(goods.dispatchMode)!''}',
			isCityWide:'${(goods.isCityWide)!''}',
			distLimit:'${(goods.distLimit)!''}',
			provLimit:'${(goods.provLimit)!''}',
			distrIds:'${(goods.distrIds)!''}',
			status:'${(goods.status)!''}'
		}
	},
	methods:{
		getReviewResult: function(){
    			if("0" == this.review.reviewResult) {
    				return "待审核";
    			}else if("2" == this.review.reviewResult) {
    				return "审核拒绝";
    			}else if("1" == this.review.reviewResult) {
    				return "审核通过";
    			}
        		return "其他";
		},
		getCategories: function(){
			$.ajax({
				url: '/goods/categories',
				method:'get',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet.categories){
						goodsContainerVue.metadata.categories = [];
						for(var i=0;i<jsonRet.categories.length;i++){
							goodsContainerVue.metadata.categories.push(jsonRet.categories[i]);
						}
					}else{
						alert('获取商品分类数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		getAllProvinces: function(){
			$.ajax({
				url: '/city/province/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						goodsContainerVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							goodsContainerVue.metadata.provinces.push(jsonRet[i]);
						}
					}else{
						alert('获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		getPostages: function(){
			//获取系统所有的运费模版
			
		},
		selectImage: function(selectCntLimit,targetElId){
			//选择商品的图片
			$('#imageGalleryShowModal').modal('show');
			imageGalleryShowVue.selectCntLimit = selectCntLimit;
			imageGalleryShowVue.targetElId = targetElId;
			imageGalleryShowVue.selectedImages = [];
		},
		submit: function(){
			$.ajax({
				url: '/goods/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alert("合作伙伴基本信息修改成功！");
							$.extend(goodsContainerVue.initData,goodsContainerVue.param); 
							window.location.reload();
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
			$.extend(goodsContainerVue.param,goodsContainerVue.initData); 
		}
	}
});

goodsContainerVue.getCategories();
goodsContainerVue.getAllProvinces();

function getCities(provCode,areaName){
	$.ajax({
		url: '/city/city/getbyprov/' + provCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				goodsContainerVue.metadata.cities = [];
				for(var i=0;i<jsonRet.length;i++){
					goodsContainerVue.metadata.cities.push(jsonRet[i]);
				}
				if(areaName){
					var cityCode = "";
					for(var i=0;i<goodsContainerVue.metadata.cities.length;i++){
						if(goodsContainerVue.metadata.cities[i].cityName == goodsContainerVue.param.city){
							cityCode = goodsContainerVue.metadata.cities[i].cityCode;
							break;
						}
					}
					getAreas(cityCode);
				}
			}else{
				alert('获取城市数据(地级市)失败！')
			}
		},
		dataType: 'json'
	});
}

function getAreas(cityCode){
	$.ajax({
		url: '/city/area/getbycity/' + cityCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				goodsContainerVue.metadata.areas = [];
				for(var i=0;i<jsonRet.length;i++){
					goodsContainerVue.metadata.areas.push(jsonRet[i]);
				}
			}else{
				alert('获取城市数据(县)失败！')
			}
		},
		dataType: 'json'
	});
}
</script>

<#include "/image/page-image-show-tpl.ftl" encoding="utf8"> 

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