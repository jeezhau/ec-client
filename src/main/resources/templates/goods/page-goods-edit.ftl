<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
    <#include "/head/page-ckeditor-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">

<div class="container" id="goodsContainer" style="padding:0;oveflow:scroll">
  <div class="row">
     <ul class="nav nav-tabs" style="margin:0 15%">
	  <li style="width:50%;text-align:center" class="active" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editGoods').show();$('#reviewInfo').hide();">
	    <a href="javascript:;">商品编辑</a>
	  </li>
	  <li style="width:50%;text-align:center" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#editGoods').hide();$('#reviewInfo').show();">
	    <a href="javascript:;">审批进度</a>
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
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">商品名称<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <input class="form-control" v-model="param.goodsName" required maxLength="100" placeHolder="请输入商品名称2-100字符">
        </div>
      </div>         
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">商品分类<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <select class="form-control"  v-model="param.categoryId" required>
            <option v-for="item in metadata.categories" v-bind:value="item.categoryId">{{item.categoryName}}</option>
          </select>
        </div>
      </div> 
       <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">商品主图<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <div class="col-xs-9" style="padding-left:0"><input class="form-control"  v-model="param.mainImgPath" required readonly placeholder="请选择图片"></div>
          <div class="col-xs-3" style="padding-left:0"><button type="button" class="btn btn-info" @click="selectImage(1,'main')">选择</button></div>
        </div>
      </div> 
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">商品轮播图<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
           <div class="col-xs-9" style="padding-left:0"><textarea class="col-xs-9 form-control" v-model="param.carouselImgPaths" maxLength=500 rows=10 readonly placeholder="请选择图片" ></textarea></div>
           <div class="col-xs-3" style="padding-left:0"><button type="button" class="btn btn-info" @click="selectImage(10,'carousel')">选择</button></div>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">商品产地<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
           <input type="text" class="form-control" v-model="param.place"  max=100 >
        </div>       
      </div>
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">生产企业<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
           <input type="text" class="form-control" v-model="param.vender"  max=100 >
        </div>       
      </div>
      <div class="form-group">
        <div class="col-xs-12" style="padding-left:1px">
          <label class="col-xs-12 control-label" style="text-align:left">规格明细与库存(名称唯一，所有字段必填)<span style="color:red">*</span></label>
        </div>
        <div class="col-xs-12"  style="max-height:300px;overflow:scroll">
           <table class="table table-striped table-bordered table-condensed">
             <tr>
               <th width="30%" style="padding:2px 2px">规格名称</th>
               <th width="13%" style="padding:2px 2px">数量值</th>
               <th width="10%" style="padding:2px 2px">单位</th>
               <th width="15%" style="padding:2px 2px">售价(¥)</th>
               <th width="15%" style="padding:2px 2px">带包装重量(kg)</th>
               <th width="15%" style="padding:2px 2px">库存件数</th>
               <th width="8%" style="padding:2px 2px;vertical-align:center">
                 <button type="button" class="btn btn-primary" style="padding:2px 2px" @click="addSpec">添加</button>
               </th>
             </tr>
             <tr v-for="(item,index) in specDetailArr" >
               <td style="padding:2px 0px">
                 <input type="text"  style="width:100%" maxlength="20" :value="item.name" @change="setSpecItem('name',index,$event)">
               </td>
               <td style="padding:2px 0px">
                 <input type="number" style="width:100%" min=0 max=999999 :value="item.val" @change="setSpecItem('val',index,$event)">
               </td>
               <td style="padding:2px 0px">
                 <input type="text" style="width:100%" :value="item.unit" maxlength=5 @change="setSpecItem('unit',index,$event)">
               </td>
               <td style="padding:2px 0px">
                 <input type="number" style="width:100%" :value="item.price" min=0 max=99999999 @change="setSpecItem('price',index,$event)">
               </td> 
               <td style="padding:2px 0px">
                 <input type="number" style="width:100%" :value="item.grossWeight" min=0 max=99999999 @change="setSpecItem('gross',index,$event)">
               </td>               
               <td style="padding:2px 0px">
                 <input type="number" style="width:100%" min=0 max=999999 :value="item.stock" @change="setSpecItem('stock',index,$event)">
               </td>
               <th width="8%" style="padding:2px 2px;vertical-align:center">
                 <button type="button" class="btn btn-danger" style="padding:2px 2px" @click="delSpec(index)">删除</button>
               </th>
             </tr>
           </table>
        </div>       
      </div>
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">限购数量(件)<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.limitedNum" min=0 max=999999 placeholder="请输入每人限购数量，0表示不限购" >
        </div>
      </div>
      <div class="form-group" v-if="param.limitedNum>0">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">限购开始时间<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <input type="date" class="form-control" v-model="param.beginTime"  maxLength=20  required placeholder="请输入限购开始时间">
        </div>
      </div>
      <div class="form-group" v-if="param.limitedNum>0">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">限购结束时间<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <input type="date" class="form-control" v-model="param.endTime"  maxLength=20  required placeholder="请输入限购开始时间">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px" title="签收后支持多少天可退换货，0表示不支持，质量问题除外">退换货期限(天)<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <input type="number" class="form-control" v-model="param.refundLimit" title="签收后支持多少天可退换货，0表示不支持，质量问题除外" min=0 max=999999 placeholder="签收后支持多少天可退换货，0表示不支持" >
        </div>
      </div>   
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">运费模版<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <select class="form-control" v-model="postageIdArr" required multiple>
            <option v-for="item in metadata.postages" v-bind:value="item.postageId">{{item.postageName}}</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-4 col-sm-3 col-md-3 col-lg-2 control-label" style="padding-right:1px">是否立即上架<span style="color:red">*</span></label>
        <div class="col-xs-8 col-sm-9 col-md-9 col-lg-10" style="padding-left:1px">
          <label style="padding:0 5px"><input type="radio" v-model="param.status" value="0" style="display:inline-block;width:15px;height:15px">否</label>
          <label style="padding:0 5px"><input type="radio" v-model="param.status" value="1" style="display:inline-block;width:15px;height:15px">是</label>
        </div>
      </div>     
      <div class="form-group">
        <div class="col-xs-12" style="padding-left:1px">
          <label class="col-xs-12 control-label" style="text-align:left">商品图文描述<span style="color:red">*</span></label>
        </div>
        <div class="col-xs-12" >
          <div id="hiddenGoodsDesc" style="display:none">${(goods.goodsDesc)!''}</div>
          <textarea class="form-control ckeditor" id="content" maxLength=10000  rows=30 required ></textarea>
        </div>
      </div>
      <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info" style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning" style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>
	</form>
  </div>

  <div class="row" id="reviewInfo" style="width:100%;margin:1px 0px 0px 0px;padding:5px 8px;background-color:white;display:none">
  	<h5 style="text-align:center">最新审批结果信息</h5>
  	<p>最新审批时间：{{review.reviewTime}}</p>
  	<p>最新审批结果：{{getGoodsRewResult(review.reviewResult)}} </p>
  	<p>审批意见：<br>
	 <div v-for=" item in review.reviewLog" class="col-xs-12" style="margin-top:1px;">
	   <div class="col-xs-1" style="margin-top:-2px;padding:0;vertical-align:center;">
	    <img alt="" src="/icons/树形线.png" style="width:80%;height:110%">
	   </div>
	   <div class="col-xs-11" style="padding:0">
	     <span class="pull-left" style="padding:0 3px;">{{item.time}}({{getGoodsRewResult(item.result)}})</span>
	     <span class="pull-right" style="padding:0 3px;">{{item.content}}</span>
	   </div>
	 </div>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var goodsContainerVue = new Vue({
	el:'#goodsContainer',
	data:{
		metadata:{
			categories:[],
			postages:[]
		},
		review:{
			reviewResult:'${(goods.reviewResult)!"0"}',
			reviewTime:'${(goods.reviewTime)!''}' ,
			<#if ((goods.reviewLog)!'') != ''>
			reviewLog:JSON.parse('${(goods.reviewLog)!"[]"}')
			<#else>
			reviewLog:JSON.parse('[]')
			</#if>
		},
		param:{
			goodsId:'${((goods.goodsId)!"-1")?string("#")}',
			goodsName:'${(goods.goodsName)!''}',
			categoryId:'${((goods.categoryId)!-1)?string("#")}',
			goodsDesc:'',
			mainImgPath:'${(goods.mainImgPath)!''}',
			carouselImgPaths:'${(goods.carouselImgPaths)!''}',
			place:'${(goods.place)!''}',
			vender:'${(goods.vender)!''}',
			//priceLowest:'${(goods.priceLowest)!''}',
			//stockSum:'${(goods.stockSum)!''}',
			limitedNum:'${((goods.limitedNum)!0)?string("#")}',
			beginTime:'${(goods.beginTime)!''}',
			endTime:'${(goods.endTime)!''}',
			refundLimit:'${((goods.refundLimit)!7)?string("#")}',
			postageIds:'${(goods.postageIds)!''}',
			status:'${(goods.status)!''}',
			jsonSpecArr:''
		},
		postageIdArr:'${(goods.postageIds)!''}'.split(','),
		specDetailArr:JSON.parse('${(goods.specDetail)!"[]"}')
	},
	watch:{
		postageIdArr :function(){
			this.param.postageIds = this.postageIdArr.join(',');
		}
	},
	methods:{
		addSpec:function(){
			if(this.specDetailArr == null){
				this.specDetailArr = [];
			}
			this.specDetailArr.push(new Object({name:'',val:'',unit:'',price:'',grossWeight:'',stock:''}));
		},
		delSpec:function(index){
			this.specDetailArr.splice(index,1);
		},
		setSpecItem:function(field,index,event){
			var spec = this.specDetailArr[index];
			var value = $(event.target).val();
			if(field === 'name'){
				if(!value){
					value = '';
				}
				value = value.trim();
				if(value.length>20){
					alertMsg('错误提示','规格明细中第 ' + (index+1) + " 条数据的'规格名称'不合规，长度须为1-20字符！");
					$(event.target).focus();
					return false;
				}
				$(event.target).val(value);
				spec.name = value;
			}
			if(field == 'val' && value){
				var val = parseInt(value);
				if(isNaN(val) || val < 1 || val > 999999){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的数量值不合规，须为1-999999的整数值！");
					$(event.target).focus();
					return false;
				}
				$(event.target).val(val);
				spec.val = val;
			}
			if(field == 'unit' && value){
				value = value.trim();
				if(value.length < 1 || value.length > 5){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的单位不合规，长度须为1-5字符！");
					$(event.target).focus();
					return false;
				}
				$(event.target).val(value);
				spec.unit = value;
			}
			if(field == 'price' && value){
				var val = parseFloat(value);
				if(isNaN(val) || val < 0 || val > 99999999.99){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的单价不合规，须为0-99999999.99的数值！");
					$(event.target).focus();
					return false;
				}
				val = val.toFixed(2);
				$(event.target).val(val);
				spec.price = val;
				/* if(this.param.priceLowest){
					if(val<this.param.priceLowest){
						this.param.priceLowest = val;
					}
				}else{
					this.param.priceLowest = val;
				} */
			}
			if(field == 'gross' && value){
				var val = parseInt(value);
				if(isNaN(val) || val < 1 || val > 99999999){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的带包装重量不合规，须为1-99999999的整数值！");
					$(event.target).focus();
					return false;
				}
				$(event.target).val(val);
				spec.grossWeight = val;
			}
			if(field == 'stock' && value){
				var val = parseInt(value);
				if(isNaN(val) || val < 0 || val > 999999){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的库存不合规，须为0-999999的整数值！");
					$(event.target).focus();
					return false;
				}
				$(event.target).val(val);
				spec.stock = val;
				
				/* this.param.stockSum = 0;
				for(var i=0;i<this.specDetailArr.length;i++){
					var sp = this.specDetailArr[i];
					if(sp.name && sp.stock){
						this.param.stockSum = this.param.stockSum + parseInt(sp.stock);
					}
				} */
			}
		},
		getCategories: function(){
			$.ajax({
				url: '/goods/queryCat',
				method:'get',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet.categories){
						goodsContainerVue.metadata.categories = [];
						for(var i=0;i<jsonRet.categories.length;i++){
							goodsContainerVue.metadata.categories.push(jsonRet.categories[i]);
						}
					}else{
						alertMsg('错误提示','获取商品分类数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		getPostages: function(){
			//获取系统所有的运费模版
			$.ajax({
				url: '/postage/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet ){
						if(jsonRet.postages){
							goodsContainerVue.metadata.postages = [];
							for(var i=0;i<jsonRet.postages.length;i++){
								goodsContainerVue.metadata.postages.push(jsonRet.postages[i]);
							}
						}
					}else{
						alertMsg('错误提示','获取数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		selectImage: function(selectCntLimit,imgType){
			//选择商品的图片
			$('#imageGalleryShowModal').modal('show');
			imageGalleryShowVue.selectCntLimit = selectCntLimit;
			imageGalleryShowVue.selectedImages = [];
			imageGalleryShowVue.callbackFun = function(images){
				if(imgType === 'main'){
					goodsContainerVue.param.mainImgPath = images;
				}else{
					goodsContainerVue.param.carouselImgPaths = images;
				}
			}
		},
		submit: function(){
			$('#dealingData').show();
			this.param.goodsDesc = CKEDITOR.instances.content.getData();
			//组织规格数据
			var okSpecArr = [];
			//var stockSum = 0;
			//var priceLowest = 1000000000;
			for(var i=0;i<this.specDetailArr.length;i++){
				var spec = this.specDetailArr[i];
				if(!spec.name){
					continue;//过滤该条数据
				}else{
					spec.name = spec.name.trim();
				}
				if(spec.name.length > 20){
					alertMsg('错误提示',"规格明细中的第 " + (i+1) + "条的规格名称不合规，长度范围：2-20字符！");
					return;
				}
				if(!spec.unit || spec.unit.trim().length > 5){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的单位不合规，长度须为1-5字符！");
					return false;
				}else{
					spec.unit = spec.unit.trim();
				}
				var val = parseInt(spec.val);
				if(isNaN(val) || val<1 || val>999999){
					alertMsg('错误提示',"规格明细中的第 " + (i+1) + "条的数量值不合规，须为1-999999的整数值！");
					return false;
				}else{
					spec.val = val;
				}
				var val = parseFloat(spec.price);
				if(isNaN(val) || val < 0 || val > 99999999){
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的单价不合规，须为0-99999999.99的数值！");
					return false;
				}else{
					val = val.toFixed(2);
					spec.price = val;
					/* if(priceLowest > val){
						priceLowest = val;
					} */
				}
				var val = parseInt(spec.grossWeight);
				if(isNaN(val) || val<1 || val>99999999){
					alertMsg('错误提示',"规格明细中的第 " + (i+1) + "条的带包装重量不合规，须为1-99999999的整数值！");
					return false;
				}else{
					spec.grossWeight = val;
				}
				var val = parseInt(spec.stock);
				if(isNaN(val) || val< 0 || val > 999999){
					alertMsg('错误提示',"规格明细中的第 " + (i+1) + "条的库存不合规，须为0-999999的整数值！");
					return false;
				}else{
					spec.stock = val;
					//stockSum += val;
				}
				okSpecArr.push(spec);
			}
			if(okSpecArr.length<1){
				alertMsg('错误提示',"规格明细数据不可为空，至少要有一条数据！");
				return ;
			}
			for(var i=0;i<okSpecArr.length;i++){
				for(var j=i+1;j<okSpecArr.length;j++){
					if(okSpecArr[i].name == okSpecArr[j].name){
						alertMsg('错误提示',"规格明细不可出现同规格名称的记录！");
						return false;
					}
				}
			}
			this.param.jsonSpecArr = JSON.stringify(okSpecArr);
			$.ajax({
				url: '/goods/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet){
						if(0 == jsonRet.errcode){
							window.location.href='/goods/manage';
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		},
		reset: function(){
			window.location.reload();
		}
	}
});

goodsContainerVue.getCategories();
goodsContainerVue.getPostages();
if(goodsContainerVue.specDetailArr.length == 0){
	goodsContainerVue.addSpec();	
}
//goodsContainerVue.param.goodsDesc = $('#hiddenGoodsDesc').html();

//页面初始化
var editor = CKEDITOR.replace( 'content' );	//修改 #content 的显示方式为 CKEDITOR
var contentObj = CKEDITOR.instances['content'];
contentObj.setData($('#hiddenGoodsDesc').html()); 
function addUploadButton(editor){
    CKEDITOR.on('dialogDefinition', function( ev ){
        var dialogName = ev.data.name;
        var dialogDefinition = ev.data.definition;
        if ( dialogName == 'image' ){
            var infoTab = dialogDefinition.getContents( 'info' );
            infoTab.add({
                type : 'button',
                id : 'upload_image',
                align : 'center',
                label : '选择',
                onClick : function( evt ){
                    var thisDialog = this.getDialog();
                    var txtUrlObj = thisDialog.getContentElement('info', 'txtUrl');
                    var txtUrlId = txtUrlObj.getInputElement().$.id;
                    addUploadImage(txtUrlId);
                }
            }, 'browse'); //place front of the browser button
            var widthTipTab = dialogDefinition.getContents( 'info' );
            widthTipTab.add({
                type : 'button',
                id : 'width_tip',
                align : 'center',
                label : '宽度可用百分数，表占距页面的宽度比，如 100%，也可用像素值！',
            }, 'width'); //place front of the browser button
            var heightTipTab = dialogDefinition.getContents( 'info' );
            heightTipTab.add({
                type : 'button',
                id : 'height_tip',
                align : 'center',
                label : '高度则一般使用像数值，如500px！！！',
            }, 'width'); //place front of the browser button
        }
    });
}
 
function addUploadImage(theURLElementId){
    var imgUrl = '/shop/gimage/${(myPartner.partnerId)?string("#")}/'; 
  	//选择商品的图片
	$('#imageGalleryShowModal').modal('show');
	$('#imageGalleryShowModal').css('z-index',100000);
	imageGalleryShowVue.selectCntLimit = 1;
	imageGalleryShowVue.selectedImages = [];
	imageGalleryShowVue.callbackFun = function(images){
		var urlObj = $('#'+theURLElementId);
		 urlObj.trigger('blur'); //触发url文本框的事件，以便预览图片
	    urlObj.val(imgUrl + images);
	    urlObj.trigger('blur'); //触发url文本框的事件，以便预览图片
	    urlObj.blur();
	    urlObj.trigger('keyup'); //触发url文本框的事件，以便预览图片
	};
}
addUploadButton(editor);

</script>

<#include "/pimage/page-image-show-tpl.ftl" encoding="utf8">
<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

<#if errmsg??>
<!-- 错误提示模态框（Modal） -->
<#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>