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
<div class="container" id="postageContainer" style="padding:0px 0px;oveflow:scroll">
  <div class="row" id="editpostage" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;">
    <h5 style="text-align:center;margin:15px 0">运费模版信息编辑</h5>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">模版名称<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.postageName" required maxLength="20" placeHolder="请输入模版名称2-20字符">
        </div>
      </div>         
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">配送范围<span style="color:red">*</span></label>
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
        <label class="col-xs-4 control-label" style="padding-right:1px">是否免邮<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.isFree" required>
            <option value="0">不免邮</option>
            <option value="1">无条件免邮</option>
            <option value="2">重量限制</option>
            <option value="3">金额限制</option>
            <option value="4">距离限制</option>
            <option value="23">重量与金额限制</option>
            <option value="24">重量与距离限制</option>
            <option value="34">金额与距离限制</option>
            <option value="234">重量金额距离限制</option>
          </select>
        </div>
      </div>           
      <div class="form-group" v-if="param.isFree.indexOf('2')>=0">
        <label class="col-xs-4 control-label" style="padding-right:1px">免邮重量<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.freeWeight" min=1  max=999999  placeholder="低于该重量免邮" >
        </div>
      </div>
      <div class="form-group" v-if="param.isFree.indexOf('3')>=0">
        <label class="col-xs-4 control-label" style="padding-right:1px">免邮金额<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="text" class="form-control" v-model="param.freeAmount" maxLength=10  placeholder="超过该金额免邮" >
        </div>
      </div>
      <div class="form-group" v-if="param.isFree.indexOf('4')>=0">
        <label class="col-xs-4 control-label" style="padding-right:1px">免邮距离<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.freeDist" min=1  max=999999  placeholder="近于该距离免邮" >
        </div>
      </div>
      <div class="form-group" v-if="param.isFree !== '1' && param.isCityWide === '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">起步距离(km)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.firstDist"  min=1  max=999999 placeholder="起步距离"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.firstDPrice"  maxLength=5  required placeholder="起步距离价格"></div>
        </div>
      </div> 
      <div class="form-group" v-if="param.isFree !== '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">起步重量(kg)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.firstWeight"  min=1  max=999999 placeholder="起步重量"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.firstWPrice"  maxLength=5  required placeholder="起步重量价格"></div>
        </div>
      </div>
      <div class="form-group" v-if="param.isFree !== '1' && param.isCityWide === '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">续距(kg)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.additionDist"  min=1  max=999999 placeholder="续距"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.additionDPrice"  maxLength=5  required placeholder="续距价格"></div>
        </div>
      </div>       
      <div class="form-group" v-if="param.isFree !== '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">续重(kg)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.fadditionWeight"  min=1  max=999999 placeholder="续重"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.additionWPrice"  maxLength=5  required placeholder="续重价格"></div>
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
var postageContainerVue = new Vue({
	el:'#postageContainer',
	data:{
		initData:{
			postageId:'${(postage.postageId)!-1}',
			postageName:'${(postage.postageName)!''}',
			isCityWide:'${(postage.isCityWide)!''}',
			distLimit:'${(postage.distLimit)!''}',
			provLimit:'${(postage.provLimit)!''}',
			isFree:'${(postage.isFree)!''}',
			freeWeight:'${(postage.freeWeight)!''}',
			freeAmount:'${(postage.freeAmount)!''}',
			freeDist:'${(postage.freeDist)!''}',
			firstDist:'${(postage.firstDist)!''}',
			firstWeight:'${(postage.firstWeight)!''}',
			firstDPrice:'${(postage.firstDPrice)!''}',
			firstWPrice:'${(postage.firstWPrice)!''}',
			additionDist:'${(postage.additionDist)!''}',
			additionWeight:'${(postage.additionWeight)!''}',
			additionDPrice:'${(postage.additionDPrice)!''}',
			additionWPrice:'${(postage.additionWPrice)!''}'
		},	//初始化的数据
		metadata:{
			provinces:[]
		},
		param:{
			postageId:'${(postage.postageId)!-1}',
			postageName:'${(postage.postageName)!''}',
			isCityWide:'${(postage.isCityWide)!''}',
			distLimit:'${(postage.distLimit)!''}',
			provLimit:'${(postage.provLimit)!''}',
			isFree:'${(postage.isFree)!''}',
			freeWeight:'${(postage.freeWeight)!''}',
			freeAmount:'${(postage.freeAmount)!''}',
			freeDist:'${(postage.freeDist)!''}',
			firstDist:'${(postage.firstDist)!''}',
			firstWeight:'${(postage.firstWeight)!''}',
			firstDPrice:'${(postage.firstDPrice)!''}',
			firstWPrice:'${(postage.firstWPrice)!''}',
			additionDist:'${(postage.additionDist)!''}',
			additionWeight:'${(postage.additionWeight)!''}',
			additionDPrice:'${(postage.additionDPrice)!''}',
			additionWPrice:'${(postage.additionWPrice)!''}'
		}
	},
	methods:{
		getAllProvinces: function(){
			$.ajax({
				url: '/city/province/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
						postageContainerVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							postageContainerVue.metadata.provinces.push(jsonRet[i]);
						}
					}else{
						alert('获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		submit: function(){
			$.ajax({
				url: '/postage/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							alert("合作伙伴基本信息修改成功！");
							$.extend(postageContainerVue.initData,postageContainerVue.param); 
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
			$.extend(postageContainerVue.param,postageContainerVue.initData); 
		}
	}
});

postageContainerVue.getAllProvinces();

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