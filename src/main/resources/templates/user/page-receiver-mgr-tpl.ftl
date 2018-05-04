<!-- 收货人管理子模块 -->
<div class="container " id="receiverManage" style="padding:0px 0px;oveflow:scroll">
  <div class="row" style="margin:5px 0;text-align:center">
     <h3>我的收货信息</h3>
  </div>
  <div class="row" style="margin:5px 0;padding:3px 0;">
    <div class="row" v-for="item in receiverList" style="margin:9px 3px;padding:8px;background-color:white;">
      <div class="col-xs-12">
        <span class="pull-left">{{item.receiver}}</span>
        <span class="pull-left" style="padding: 0 10px">{{item.phone}}</span>
        <img v-if="item.isDefault === '1'" class="pull-right" src="/icons/默认.png" width=50px height=50px>
      </div>
      <div class="col-xs-12">
        <span>{{item.province}}</span> 
        <span>{{item.city}}</span>
        <span>{{item.area}}</span>
        <span>{{item.addr}}</span>
      </div>  
      <div class="col-xs-12" style="padding-top:3px">
        <button v-if="item.isDefault === '0' " class="btn btn-primary" style="padding:3px 3px" @click="setDefault(item.recvId)"> &nbsp;设为默认 &nbsp;</button>
        <button class="btn btn-warning" style="padding:3px 3px" @click="editReceiver(item)"> &nbsp; &nbsp; 编 辑 &nbsp; &nbsp; </button>
        <button v-if="item.isDefault === '0' " class="btn btn-danger" style="padding:3px 3px" @click="deleteReceiver(item.recvId)"> &nbsp; &nbsp;删 除 &nbsp; &nbsp; </button>
        <button v-if="useFlag === 1 " class="btn btn-success" style="padding:3px 3px" @click="useReceiver(item)"> &nbsp; &nbsp;使 用 &nbsp; &nbsp; </button>
      </div>   
    </div>
    
  </div>
  
  <div class="row" style="margin:15px 0;text-align:center">
    <button class="btn btn-danger" style="width:90%" @click="editReceiver()">新增地址</button>
  </div>

</div><!-- end of container -->
<script type="text/javascript">
var receiverManageVue = new Vue({
	el:'#receiverManage',
	data:{
		useFlag:0,	//是否使用或编辑管理
		receiverList:[]
	},
	methods:{
		callBackFun:null,//使用的回调函数
		useReceiver:function(item){
			//点击使用是回调
			if(this.callBackFun){
				this.callBackFun(item);
			}
		},
		getAll : function(){
			$.ajax({
				url: '/receiver/getall',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							receiverManageVue.receiverList = [];
							for(var i=0;i<jsonRet.datas.length;i++){
								receiverManageVue.receiverList.push(jsonRet.datas[i]);
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
		},
		setDefault: function(recvId){
			$.ajax({
				url: '/receiver/setdefault/' + recvId,
				method:'post',
				data: {'recvId':recvId},
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							receiverManageVue.getAll();
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
		deleteReceiver: function(recvId){
			if(!confirm("您确定要删除该条收货人信息？")){
				return;
			}
			$.ajax({
				url: '/receiver/delete/' + recvId,
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							receiverManageVue.getAll();
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
		editReceiver : function(receiver){
			$('#editReceiverModal').modal('show');
			editReceiverVue.param.recvId = receiver ? receiver.recvId : 0;
			editReceiverVue.param.province = receiver ? receiver.province : '';
			editReceiverVue.param.city = receiver ? receiver.city : '';
			editReceiverVue.param.area = receiver ? receiver.area : '';
			editReceiverVue.param.addr = receiver ? receiver.addr : '';
			editReceiverVue.param.receiver = receiver ? receiver.receiver : '';
			editReceiverVue.param.phone = receiver ? receiver.phone : '';
			editReceiverVue.param.isDefault = receiver ? receiver.isDefault : '1';
			getCities();
		}
	}
});
receiverManageVue.getAll();
</script>

<!-- 编辑收货人信息Model -->
<div class="modal fade " id="editReceiverModal" tabindex="-1" role="dialog" aria-labelledby="editReceiverModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="editReceiverModalLabel">新增收货人信息</h4>
         </div>
         <div class="modal-body">
		  <div class="row" style="width:100%;margin:3px 1px 0px 1px;">
		      <div class="col-xs-12" style="padding:1px 18px ">
				  <div class="form-group" >
				    <label class="col-xs-4 control-label" style="padding:0 1px">收货人<span style="color:red">*</span></label>
				    <div class="col-xs-8" style="padding:0 1px">
				      <input type="text" class="form-control" v-model="param.receiver" pattern="\w{2,50}" title="2-50个字符组成" maxLength=50 required placeholder="收货人(2-50个字符)">
				    </div>
				  </div> 
			  </div> 
			  <div class="col-xs-12" style="padding:1px 18px">	  
				  <div class="form-group">
			        <label class="col-xs-4 control-label" style="padding:0 1px">联系电话<span style="color:red">*</span></label>
			        <div class="col-xs-8" style="padding:0 1px">
			          <input class="form-control" type="text" v-model="param.phone"  required placeholder="请输入联系电话" >
			        </div>
			      </div>
		      </div>
		      <div class="col-xs-12" style="padding:1px 18px ">	  
				  <div class="form-group">
			          <div class="col-xs-12" style="padding:1px 1px">
			            <div class="col-xs-3" style="padding:0 1px">
				          <select class="form-control" v-model="param.province" v-on:change="changeProvince">
				            <option v-for="item in metadata.provinces" v-bind:value="item.provName">{{item.provName}}</option>
				          </select>
			            </div>
			            <div class="col-xs-4" style="padding:0 1px">
			              <select class="form-control"  v-model="param.city" v-on:change="changeCity">
                            <option v-for="item in metadata.cities" v-bind:value="item.cityName">{{item.cityName}}</option>
                          </select>
			            </div>
			            <div class="col-xs-5" style="padding:0 1px">
			              <select class="form-control"  v-model="param.area" >
            				   <option v-for="item in metadata.areas" v-bind:value="item.areaName">{{item.areaName}}</option>
                          </select>
			            </div>
			          </div>
			          <div class="col-xs-12" style="padding:1px 1px">
			            <input class="form-control" type="text" v-model="param.addr"  required placeholder="街道详细地址" >
			          </div>          
			      </div>
		      </div>
		      <div class="col-xs-12" style="padding:0 18px 0 18px">
				  <div class="form-group">
			        <label class="col-xs-4 control-label" style="padding:0 1px">是否默认<span style="color:red">*</span></label>
			        <div class="col-xs-4" style="padding:0 1px">
		                <label><input type="radio" value="0" v-model="param.isDefault" style="display:inline-block;width:18px;height:18px" >否</label>
		                <label><input type="radio" value="1" v-model="param.isDefault" style="display:inline-block;width:18px;height:18px" >是</label>
			        </div>
			      </div>
		      </div> 
			  <div class="form-group">
		         <div style="text-align:center">
		           <button type="button" class="btn btn-info" style="margin:10px;width:90%" @click="submit">&nbsp;&nbsp;保   存&nbsp;&nbsp;</button>
		         </div>
		      </div>                
		  </div>
		</div>
     </div>
   </div>
</div><!-- end of modal -->
<script>
var editReceiverVue = new Vue({
	el:'#editReceiverModal',
	data:{
		metadata:{
			provinces:[],
			cities:[],
			areas:[]
		},
		param:{
			recvId:'',
			country:'中国',
			province:'',
			city:'',
			area:'',
			addr:'',
			receiver:'',
			phone:'',
			isDefault:''
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
						editReceiverVue.metadata.provinces = [];
						for(var i=0;i<jsonRet.length;i++){
							editReceiverVue.metadata.provinces.push(jsonRet[i]);
						}
						if(editReceiverVue.param.city){
							getCities();
						}
					}else{
						alert('获取城市数据(省份)失败！')
					}
				},
				dataType: 'json'
			});
		},
		changeProvince: function(){
			editReceiverVue.param.city = '';
			editReceiverVue.param.area = '';
			editReceiverVue.metadata.cities = [];
			editReceiverVue.metadata.areas = [];
			getCities();
		},
		changeCity: function(){
			editReceiverVue.param.area = '';
			editReceiverVue.metadata.areas = [];
			getAreas();
		},
		submit:function(){
			if(!this.param.area || !this.param.addr || !this.param.phone || !this.param.receiver){
				alert("数据有误，所有项都需正确填写！");
				return;
			}
			$.ajax({
				url: '/receiver/save',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(0 == jsonRet.errcode){
							containerVue.getAll();
							$('#editReceiverModal').modal('hide');
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
	}
});
editReceiverVue.getAllProvinces();
function getCities(){
	if(!editReceiverVue.param.province){
		return;
	}
	var provCode = "";
	for(var i=0;i<editReceiverVue.metadata.provinces.length;i++){
		if(editReceiverVue.metadata.provinces[i].provName == editReceiverVue.param.province){
			provCode = editReceiverVue.metadata.provinces[i].provCode;
			break;
		}
	}
	$.ajax({
		url: '/city/city/getbyprov/' + provCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				editReceiverVue.metadata.cities = [];
				for(var i=0;i<jsonRet.length;i++){
					editReceiverVue.metadata.cities.push(jsonRet[i]);
				}
				if(editReceiverVue.param.city){//
					getAreas();
				}
			}else{
				alert('获取城市数据(地级市)失败！')
			}
		},
		dataType: 'json'
	});
}

function getAreas(){
	var cityCode = "";
	for(var i=0;i<editReceiverVue.metadata.cities.length;i++){
		if(editReceiverVue.metadata.cities[i].cityName == editReceiverVue.param.city){
			cityCode = editReceiverVue.metadata.cities[i].cityCode;
			break;
		}
	}
	$.ajax({
		url: '/city/area/getbycity/' + cityCode,
		method:'post',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet && typeof jsonRet == 'object' && jsonRet instanceof Array){
				editReceiverVue.metadata.areas = [];
				for(var i=0;i<jsonRet.length;i++){
					editReceiverVue.metadata.areas.push(jsonRet[i]);
				}
			}else{
				alert('获取城市数据(县)失败！')
			}
		},
		dataType: 'json'
	});
}
</script>

