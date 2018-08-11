<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/head/page-common-head.ftl" encoding="utf8">
</head>
<body class="light-gray-bg" style="overflow:scroll">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8"> 

<div class="container" id="container" style="padding:0;">
<header >
	<ul class="nav nav-tabs" style="margin:3px 8px 3px 8px">
	  <li style="width:50%;text-align:center" onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#manageNotice').show();$('#goodsListShow').hide()">
	    <a href="javascript:;"  style="padding:10px 8px">管理须知</a>
	  </li>
	  <li style="width:50%;text-align:center" class="active"  onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#manageNotice').hide();$('#goodsListShow').show()">
	    <a href="javascript:;"  style="padding:10px 8px">我的商品清单</a>
	  </li>
	</ul>
</header>
  <div class="row" id="manageNotice" style="margin:0 ;display:none;"><!-- 管理须知 -->
    <div class="col-xs-12" style="">
	  	<p>商品管理功能：主要用于合作伙伴对商品的整个生命周期的管理；一个商品的一生：新添加--待审核--审核通过--未上架／已上架--已下架--删除 。</p>
	    <p>须知：<br>
	      1、一个商品仅在<span>已上架</span>状态时才可进行销售，新添加或修改名称与内容描述的商品需要先审核通过才可上架；<br>
	      2、如果商品审核拒绝，则会说明拒绝理由，可重新修改后再提交审核；<br>
	      3、合作伙伴所填写的一切商品信息须真实可靠，如发现弄虚作假将被警告，情节严重者将被关店处理；<br>
	      4、合作伙伴所售卖的商品产生的品质与描述纠纷问题由合作伙伴自己负责；摩放优选不负任何责任且并督促处理；<br>
	      5、编辑商品时所使用到的图片须来自于‘图库’；<br>
	      6、每个商品须有至少一个邮费模板，如果邮费模版的支持配送范围和商品的销售范围不一致时将导致客户下单失败；<br>
	      7、在商品拥有过个运费模版时，客户下单时将选择满足配送条件且邮费最低的那个模版；<br>
	      8、合作伙伴须保证提供以下服务：正品保证、同城急速、退货保障、极速发货；
	    </p>
    </div>
    <div class="row" style="margin:0;margin-top:10px;text-align:center">
     <a class="btn btn-primary" href="/goods/edit/0">新建商品</a>
     <p>一旦您新建商品将默认同意以上须知规则！</p>
    </div>  
  </div>
    
  <div class="row" id="goodsListShow" style="margin:0" ><!-- 商品管理列表 -->
    <div class="row" style="margin:0;background-color:white">
      <div class="col-xs-6" style="padding:5px 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">上架状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.status" required>
		            <option value="0">待上架</option>
		            <option value="1">已上架</option>
		            <option value="2">已下架</option>
	            </select>
	        </div>
	      </div>
      </div>
      <div class="col-xs-6" style="padding:5px 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">审核状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.reviewResult" required>
		            <option value="0">待审核</option>
		            <option value="S">通过</option>
		            <option value="R">拒绝</option>
	            </select>
	        </div>
	      </div>
      </div>
      <div class="form-group" style="padding:0 20px">
        <button type="button" class="btn btn-primary" @click="getAll()">&nbsp;&nbsp;查&nbsp;&nbsp;询&nbsp;&nbsp; </button>
        <button type="button" class="btn btn-info" @click="changeStatus('1')" >&nbsp;批量上架&nbsp;</button>
        <button type="button" class="btn btn-info" @click="changeStatus('2')" >&nbsp;批量下架&nbsp;</button>   
      </div>
    </div>
    <div class="row"  style="margin:0">
       <table class="table table-striped table-bordered table-condensed">
         <tr>
           <th width="8%" style="padding:2px 2px;text-align:center">选择</th>
           <th width="60%" style="padding:2px 2px">商品名称</th>
           <th width="20%" style="padding:2px 2px;text-align:center">库存</th>
           <th width="15%" style="padding:2px 2px;text-align:center">编辑</th>
         </tr>
	     <tr v-for="item in goodsList" >
            <td style="padding:2px 2px;text-align:center">
              <input type="checkbox" v-model="selectedArr" v-bind:value="item.goodsId" style="display:inline-block;padding:0 5px;width:15px;height:15px">
            </td>
            <td style="padding:2px 2px">
              <a :href="'/goods/preview/' + item.goodsId">{{item.goodsName}}</a>
            </td>
            <td style="padding:2px 2px;text-align:center">
              <button class="btn btn-primary" style="padding:2px 3px" @click="changeSpec(item.goodsId,item.goodsName,item.specDetail)">&nbsp;&nbsp;修 改&nbsp;&nbsp;</button>
            </td>
            <td style="padding:2px 2px;text-align:center">
              <a class="btn btn-success" style="padding:2px 3px" :href="'/goods/edit/' + item.goodsId" >&nbsp;&nbsp;编 辑&nbsp;&nbsp;</a>
            </td>
	    </tr>
	  </table>
    </div>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
 var containerVue = new Vue({
	 el:'#container',
	 data:{
		param:{
			status:'1', 
			reviewResult:'S',
			pageSize:30,
			begin:'0'
		},
		selectedArr:[],
		goodsList:[] 
	 },
	 methods:{
		 getAll: function(isRefresh,isForward){
			var url = '/goods/getall';
			var searchParam = this.param;
			getAllData(isRefresh,isForward,url,searchParam,containerVue.goodsList,containerVue.param);
		 },
		 changeStatus: function(newStat){
			 if(this.selectedArr.length<1){
				 alertMsg('错误提示',"请选择要批量" + (newStat==='1'?'上架':'下架')+ "的商品！");
				 return
			 }
			 $.ajax({
					url: '/goods/changeStatus',
					method:'post',
					data: {'goodsIds':this.selectedArr.join(','),'newStatus':newStat},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.errcode ==0){
								containerVue.getAll();
								containerVue.selectedArr = [];
							}else if(jsonRet.errmsg && jsonRet.errmsg != 'ok'){
								alertMsg('错误提示',jsonRet.errmsg);
								
							}
						}else{
							alertMsg('错误提示','获取数据失败！')
						}
					},
					dataType: 'json'
				});
		 },
		 changeSpec : function(goodsId,goodsName,specDetail){
			 $('#editSpecDetailModal').modal('show');
			 editSpecDetailVue.goodsId = goodsId;
			 editSpecDetailVue.goodsName = goodsName;
			 editSpecDetailVue.specDetailArr = specDetail;
		 }
	 }
 });
 containerVue.getAll(true); //初始化
 
 
//分页初始化
 scrollPager(containerVue.param,containerVue.goodsList,containerVue.getAll); 

</script>  
<!-- 修改商品规格(不包含名称)Model -->
<div class="modal fade " id="editSpecDetailModal" tabindex="-1" role="dialog" aria-labelledby="editSpecDetailModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="editSpecDetailModalLabel">修改商品规格与库存</h4>
         </div>
         <div class="modal-body">
		  <div class="row" style="margin-top:10px;">
		      <div class="form-group">
		        <label class="col-xs-4 control-label" style="padding-right:1px">商品名称<span style="color:red">*</span></label>
		        <div class="col-xs-8" style="padding-left:1px">
		          <input class="form-control" v-model="goodsName" required maxLength="100" readonly>
		        </div>
		      </div>
		      <div class="form-group">
		        <div class="col-xs-12" style="padding-left:1px">
		          <label class="col-xs-12 control-label" >规格明细与库存(名称唯一,为空则过滤该条记录)<span style="color:red">*</span></label>
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
		             </tr>
		             <tr v-for="(item,index) in specDetailArr" >
		               <td style="padding:2px 0px">
		                 <input type="text"  style="width:100%" maxlength="20" :value="item.name" @change="setSpecItem('name',index,$event)" readonly>
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
		             </tr>
		           </table>
		        </div>       
		      </div>
		  </div>
		  <div class="row" style="margin-top:15px;text-align:center">
		  	<button class="btn btn-primary" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
		  	<button type="button" class="btn btn-default" data-dismiss="modal">&nbsp;&nbsp;关 闭&nbsp;&nbsp;</button>
		  </div>
		</div>
     </div>
   </div>
</div><!-- end of modal -->
<script>
var editSpecDetailVue = new Vue({
	el:'#editSpecDetailModal',
	data:{
		goodsId:'',
		goodsName: '', 
		specDetailArr: []
	},
	methods:{
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
			}
		},
		submit: function(){
			//组织规格数据
			var okSpecArr = [];
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
					alertMsg('错误提示',"规格明细中的第 " + (index+1) + "条的单价不合规，须为0-99999999的数值！");
					return false;
				}else{
					val = val.toFixed(2);
					spec.price = val;
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
			$.ajax({
				url: '/goods/changeSpec',
				method:'post',
				data: {goodsId:this.goodsId,specDetail:JSON.stringify(okSpecArr)},
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							$('#editSpecDetailModal').modal('hide');
							containerVue.getAll();
						}else{//出现逻辑错误
							alertMsg('错误提示',jsonRet.errmsg);
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		}
	}
});
</script>

<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
