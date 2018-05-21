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
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<div class="container" id="postageContainer" style="padding:0px 0px;oveflow-y:scroll">
  <div class="row" id="editpostage" style="background-color:white;margin:0 0;padding:0 5px">
    <h5 style="text-align:center;margin:15px 0">运费模版信息编辑</h5>
	<form class="form-horizontal" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">模版名称<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input class="form-control" v-model="param.postageName" required maxLength="20" placeHolder="请输入模版名称2-20字符">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">配送模式<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="dispModeArr" required multiple>
            <option value="1">官方统一配送</option>
            <option value="2">商家自行配送</option>
            <option value="3">快递配送</option>
            <option value="4">客户自取</option>
          </select>
        </div>
      </div>               
      <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">配送范围<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.isCityWide" required @change='changeCityWide'>
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
          <select class="form-control" v-model="provLimitArr" multiple required >
            <option value="全国">全国</option>
            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
          </select>
        </div>
      </div>
       <div class="form-group">
        <label class="col-xs-4 control-label" style="padding-right:1px">是否免邮<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <select class="form-control" v-model="param.isFree" required @change='changeIsFree'>
            <option value="0">不免邮</option>
            <option value="1">无条件免邮</option>
            <option value="2">重量限制</option>
            <option value="3">金额限制</option>
            <option v-if="param.isCityWide==='1'" value="4">距离限制</option>
            <option value="23">重量与金额限制</option>
            <option v-if="param.isCityWide==='1'" value="24">重量与距离限制</option>
            <option v-if="param.isCityWide==='1'" value="34">金额与距离限制</option>
            <option v-if="param.isCityWide==='1'" value="234">重量金额距离限制</option>
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
          <input type="text" class="form-control" v-model="param.freeAmount" min=1 maxLength=10  placeholder="超过该金额免邮" >
        </div>
      </div>
      <div class="form-group" v-if="param.isCityWide === '1' && param.isFree.indexOf('4')>=0">
        <label class="col-xs-4 control-label" style="padding-right:1px">免邮距离<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.freeDist" min=1  max=999999  placeholder="近于该距离免邮" >
        </div>
      </div>

      <div class="form-group" v-if="param.isFree !== '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">首重(kg)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.firstWeight"  min=1  max=999999 placeholder="起步重量(kg)"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.firstWPrice"  maxLength=5  required placeholder="起步重量价格(元)"></div>
        </div>
      </div>
      <div class="form-group" v-if="param.isFree !== '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">续重(kg)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.additionWeight"  min=1  max=999999 placeholder="续重(kg)"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.additionWPrice"  maxLength=5  required placeholder="续重价格(元)"></div>
        </div>
      </div>  
      <div class="form-group" v-if="param.isFree !== '1' && param.isCityWide === '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">首距(km)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.firstDist"  min=1  max=999999 placeholder="起步距离(km)"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.firstDPrice"  maxLength=5  required placeholder="起步距离价格(元)"></div>
        </div>
      </div>            
      <div class="form-group" v-if="param.isFree !== '1' && param.isCityWide === '1'">
        <label class="col-xs-4 control-label" style="padding-right:1px">续距(km)与价格(元)<span style="color:red">*</span></label>
        <div class="col-xs-8" style="padding-left:1px">
          <div class="col-xs-6" style="padding-right:1px"><input type="number" class="form-control" v-model="param.additionDist"  min=1  max=999999 placeholder="续距(km)"></div>
          <div class="col-xs-6" style="padding-left:1px"><input type="text" class="form-control" v-model="param.additionDPrice"  maxLength=5  required placeholder="续距价格(元)"></div>
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
<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
<script type="text/javascript">
var postageContainerVue = new Vue({
	el:'#postageContainer',
	data:{
		metadata:{
			provinces:[]
		},
		param:{
			postageId:'${(postage.postageId)!''}'.replace(',',''),
			postageName:'${(postage.postageName)!''}',
			dispatchMode:'${(postage.dispatchMode)!''}',
			isCityWide:'${(postage.isCityWide)!''}',
			distLimit:'${(postage.distLimit)!''}'.replace(',',''),
			provLimit:'${(postage.provLimit)!''}',
			isFree:'${(postage.isFree)!''}',
			freeWeight:'${(postage.freeWeight)!''}'.replace(',',''),
			freeAmount:'${(postage.freeAmount)!''}'.replace(',',''),
			freeDist:'${(postage.freeDist)!''}'.replace(',',''),
			firstDist:'${(postage.firstDist)!''}'.replace(',',''),
			firstWeight:'${(postage.firstWeight)!''}'.replace(',',''),
			firstDPrice:'${(postage.firstDPrice)!''}'.replace(',',''),
			firstWPrice:'${(postage.firstWPrice)!''}'.replace(',',''),
			additionDist:'${(postage.additionDist)!''}'.replace(',',''),
			additionWeight:'${(postage.additionWeight)!''}'.replace(',',''),
			additionDPrice:'${(postage.additionDPrice)!''}'.replace(',',''),
			additionWPrice:'${(postage.additionWPrice)!''}'.replace(',','')
		},
		provLimitArr:'${(postage.provLimit)!''}'.split(","),
		dispModeArr:'${(postage.dispatchMode)!''}'.split(""),
	},
	watch:{
		provLimitArr :function(){
			this.param.provLimit = this.provLimitArr.join(',');
		},
		dispModeArr:function(){
			this.param.dispatchMode = this.dispModeArr.join('');
			if(this.param.dispatchMode.indexOf('4')>=0){
				this.param.isFree = '1';
			}
		}
	},
	methods:{
		changeCityWide : function(){
			this.param.isFree = '';
			if(this.param.isCityWide === '0'){
				this.param.distLimit = '';
				this.param.freeDist = '';
				this.param.firstDist = '';
				this.param.firstDPrice = '';
				this.param.additionDist = '';
				this.param.additionDPrice = '';
			}else{
				this.provLimitArr = [];
			}
		},
		changeIsFree: function(){
			if(!this.param.isFree){
				return;
			}
			if(this.param.dispatchMode && this.param.dispatchMode.indexOf('4')>=0){
				this.param.isFree = '1';
			}
			if(this.param.isFree.indexOf('2')<0){
				this.param.freeWeight = '';
			}
			if(this.param.isFree.indexOf('3')<0){
				this.param.freeAmount = '';
			}
			if(this.param.isFree.indexOf('4')<0){
				this.param.freeDist = '';
				this.param.firstDist = '';
				this.param.firstDPrice = '';
				this.param.additionDist = '';
				this.param.additionDPrice = '';
			}
			if(this.param.isFree === '1'){
				this.param.freeAmount = '';
				this.param.freeWeight = '';
				this.param.firstWeight = '';
				this.param.firstWPrice = '';
				this.param.additionWeight = '';
				this.param.additionWPrice = '';
				this.param.freeDist = '';
				this.param.firstDist = '';
				this.param.firstDPrice = '';
				this.param.additionDist = '';
				this.param.additionDPrice = '';
			}
		},
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
						alertMsg('错误提示','获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		submit: function(){
			if(postageContainerVue.param.postageId>0){
				$.ajax({
					url: '/postage/getusingcnt/' + postageContainerVue.param.postageId,
					method:'post',
					data: {},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							var f = false;
							if(jsonRet.cnt >= 1){
								f = confirm('该模版正在被 ' + jsonRet.cnt + " 个商品使用中，运费信息的修改可能导致商品不支持配送而下单失败或邮费需要合作伙伴埋单！您仍确定要修改吗？");
							}else if(jsonRet.cnt == 0){
								f = true;
							}
							if(f == true){
								postageContainerVue.saveEdit();
							}
						}
					},
					dataType: 'json'
				});
			}else{
				postageContainerVue.saveEdit();
			}
		},
		saveEdit : function(){//保存信息编辑
			$.ajax({
				url: '/postage/save',
				method:'post',
				data: postageContainerVue.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							window.location.href='/postage/index';
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		},
		reset: function(){
			window.location.reload();
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