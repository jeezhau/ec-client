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

<div class="container goods-container" id="container" style="margin:0 0;overflow:scroll">

 <div class="row" style="margin:5px 1px 1px ;" >
     <div style="margin:1px 0;padding:0 5px;background-color:white;font-size:130%;text-align:center" >
      <a href="/goods/show/${(goods.goodsId)?string('#')}">  ${(goods.goodsName)!''} </a>
     </div>
 </div>
 <div class="row " >
  <!-- 图片轮播 -->
  <div id="myCarousel" class="carousel slide">
    <!-- 轮播（Carousel）指标 -->
    <ol class="carousel-indicators">
        <li v-for="item,index in goods.courselImgPaths" data-target="#myCarousel" :data-slide-to="index" v-bind:class="{ active: index===0 }"></li>
    </ol>   
    <!-- 轮播（Carousel）项目 -->
    <div class="carousel-inner">
        <div  v-bind:class="[{active:(index===0)}, 'item']" v-for="imgpath,index in goods.courselImgPaths" >
            <img :src="'/image/file/show/' + imgpath" >
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#myCarousel" 
       data-slide="prev"> <span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
    <a class="carousel-control right" href="#myCarousel" 
       data-slide="next">&rsaquo;</a>
  </div>
   <!-- 商家信息 -->
  <div class="row" style="margin:5px 15px;background-color:white;padding:3px 8px;">
    <a href="'/partner/mcht/${(goods.partnerId)?string('#')}">
     <img class="pull-left" alt="" src="/partner/cert/show/logo/${(goods.partnerId)?string('#')}" style="width:25px;height:25px;border-radius:30%">
    </a>
   <span class="pull-right">${(goods.partner.province)!''}-${(goods.partner.area)!''}-${(goods.partner.addr)!''}</span>
  </div> 
   
  <form action="/order/pay/begin">
    <input type="hidden" name="goodsId" value="${(goods.goodsId)?string('#')}">
    <div class="row" style="margin:15px 1px;">
      <div class="row" style="margin:3px 15px 3px 15px;padding:3px 3px">
        <label class="col-xs-3 control-label">购买数量<span style="color:red">*</span>:</label>
        <div class="col-xs-12" style="padding:0px 3px">
	       <table class="table table-striped table-bordered table-condensed">
	         <tr>
	           <th width="30%" style="padding:2px 2px">规格名称</th>
	           <th width="15%" style="padding:2px 2px">量值</th>
	           <th width="15%" style="padding:2px 2px">售价(¥)</th>
	           <th width="20%" style="padding:2px 2px">库存件数</th>
	           <th width="20%" style="padding:2px 2px">购买数量</th>
	         </tr>
	         <tr v-for="item,index in goods.specDetailArr" >
	           <td style="padding:2px 2px">
	             <span style="width:100%" >{{item.name}}</span>
	           </td>
	           <td style="padding:2px 2px">
	              <span style="width:100%" >{{item.val}} {{item.unit}}</span>
	           </td>
	           <td style="padding:2px 2px">
	              <span style="width:100%" >{{item.price}}</span>
	           </td> 
	           <td style="padding:2px 2px">
	              <span style="width:100%" >{{item.stock}}</span>
	           </td> 	                         
	           <td style="padding:2px 2px;text-align:center">
                 <input type="number" style="width:80%" min=0 :max="item.stock" value=0 @change="changeBuyNum(index,$event)">
	           </td>
	         </tr>
	       </table>    
        </div>
      </div>
      <div class="row" style="margin:3px 15px 3px 15px;padding:5px 3px;">
        <label class="col-xs-12 control-label" style="vertical-algin:center;">配送信息<span style="color:red">*</span>:</label>
        <div class="col-xs-12">
          <div class="row">
            <div class="col-xs-9" style="padding:0">
              <div class="row" style="margin:0">
	              <div class="col-xs-6" style="padding:0">
	                <input type="text" class="form-control" name="recvName" v-model="param.recvName" readonly style="width:100%;" placeholder="收货人姓名" required>
	              </div>
	              <div class="col-xs-6" style="padding:0">
	                <input type="text" class="form-control" name="revPhone" v-model="param.recvPhone" readonly style="width:100%" placeholder="联系电话" required>
	              </div>
              </div>
              <div class="row" style="margin:0">
              	<div class="col-xs-3" style="padding:0 ">
                  <input type="text" class="form-control" name="recvProvince" v-model="param.recvProvince" readonly placeholder="省份" required>
                 </div>
              	<div class="col-xs-4" style="padding:0">
                  <input type="text" class="form-control" name="recvCity" v-model="param.recvCity" readonly placeholder="市" required>
                 </div>
              	<div class="col-xs-5" style="padding:0">
                  <input type="text" class="form-control" name="recvArea" v-model="param.recvArea" readonly placeholder="区县" required>
                 </div>                                  
              </div>
            </div>
            <div class="col-xs-3" style="vertical-algin:center;">
              <a class="btn btn-default" href="javascript:;" @click="selectReceiver"><img alt="" src="/icons/地址管理.png" width="100%" height="100%"></a>
            </div>
          </div>
          <div class="row">
            <input type="text" class="form-control" name="addr" v-model="param.recvAddr" readonly placeholder="收货人地址" required>
          </div>
        </div>

      </div> 
      <div class="row" style="margin:8px 15px 3px 15px;">
        <label class="col-xs-3 control-label">配送方式<span style="color:red">*</span>:</label>
        <div class="col-sx-9">
          <select required v-model="param.postageIdAndMode" @change="changeDispatch">
            <option v-for="item in dispatchMatchs" value="item.postageId + '-' + item.mode">
              {{item.postageId + '-' + getDispatchMode(item.mode)}}
            </option>
          </select>
        </div>
       </div>
       <div class="row" style="margin:8px 15px 3px 15px;">
         <label class="col-xs-3 control-label">买家留言:</label>
         <div class="col-sx-9">
           <textarea class="form-control" style="width:66%" rows="5" name="remark"></textarea>
         </div>
       </div> 
     </div>
     
     <div class="row" style="margin:15px 25px;padding:3px 3px;width:100%">
       <div class="col-xs-7">
         共<span style="color:red">{{param.countAll}}</span>件，
         邮费¥<span style="color:red">{{param.carrage}}</span>，
         总金额¥<span style="color:red">{{param.amount}}</span>
       </div>
       <div class="col-xs-5">
         <input type="hidden" name="goodsSpec" v-model="param.goodsSpec" required>
         <input type="hidden" name="dispatchMode" v-model="param.dispatchMode" required>
	     <input type="hidden" name="postageId" v-model="param.postageId" required>
         <input type="hidden" name="carrage" v-model="param.carrage" required>
         <input type="hidden" name="total" required>
         <button v-if="param.flag == 0" type="button" class="btn btn-sm btn-danger" @click="computeFee">数据检查/金额计算</button>
         <button v-if="param.flag == 1" type="submit" class="btn btn-sm btn-danger">立即支付</button>
       </div>
    </div>
    </form>
  </div>
</div><!-- end of container -->

<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		goods:{
			courselImgPaths:'${(goods.carouselImgPaths)!""}'.split(','),
			specDetailArr:JSON.parse('${(goods.specDetail)!"[]"}'),
		},
		dispatchMatchs:[],
		param:{
			flag:0,//计算标志
			recvId:'',
			recvName:'',
			recvPhone:'',
			recvAddr:'',
			recvCountry:'',
			recvProvince:'',
			recvCity:'',
			recvArea:'',
			countAll:0,
			carrage:0,
			amount:0,
			dispatchMode:'',
			postageId:'',
			postageIdAndMode:''
		}
	},
	methods:{
		getDispatchMode:function(code){
			if(code){
				if('1' === code){
					return '官方统一配送';
				}else if('2' == code){
					return '商家自行配送';
				}else if('3' == code){
					return '快递配送';
				}else if('4' == code){
					return '客户自取';
				}
			}
		},
		changeBuyNum:function(index,event){
			var spec = this.goods.specDetailArr[index];
			var value = $(event.target).val();
			var val = parseInt(value);
			if(isNaN(val) || val < 0 || val > spec.stock){
				$(event.target).focus();
				return false;
			}
			$(event.target).val(val);
			spec.buyNum = val;
			
			this.param.countAll = 0;
			for(var i=0;i<this.goods.specDetailArr.length;i++){
				var sp = this.goods.specDetailArr[i];
				var num =  sp.buyNum ? sp.buyNum : 0;
				this.param.countAll = this.param.countAll + num;
			}
		},
		changeDispatch:function(){
			var arr = this.param.postageIdAndMode.split("-");
			this.param.postageId = arr[0];
			this.param.dispatchMode = arr[1];
		},
		getDefaultReceiver:function(){
			$.ajax({
				url: '/receiver/getdefault',
				method:'get',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet.receiver){
						containerVue.param.recvId = jsonRet.receiver.recvId;
						containerVue.param.recvPhone = jsonRet.receiver.phone;
						containerVue.param.recvName = jsonRet.receiver.receiver;
						containerVue.param.recvProvince = jsonRet.receiver.province;
						containerVue.param.recvCity =  jsonRet.receiver.city;
						containerVue.param.recvArea = jsonRet.receiver.area;
						containerVue.param.recvAddr = jsonRet.receiver.addr;
					}else{
						//alert('获取商品分类数据失败！')
					}
				},
				dataType: 'json'
			});
		},
		selectReceiver:function(){
			$('#selectReceiverModal').modal('show');
			receiverManageVue.useFlag = 1 ;
			receiverManageVue.callBackFun = function(receiver){
				containerVue.param.recvId = receiver.recvId;
				containerVue.param.recvPhone = receiver.phone;
				containerVue.param.recvName = receiver.receiver;
				containerVue.param.recvProvince = receiver.province;
				containerVue.param.recvCity =  receiver.city;
				containerVue.param.recvArea = receiver.area;
				containerVue.param.recvAddr = receiver.addr;
				$('#selectReceiverModal').modal('hide');
			}
		},
		computeFee:function(){
			this.param.countAll = 0;
			for(var i=0;i<this.goods.specDetailArr.length;i++){
				var sp = this.goods.specDetailArr[i];
				var num =  sp.buyNum ? sp.buyNum : 0;
				this.param.countAll = this.param.countAll + num;
			}
			if(this.param.countAll<=0){
				return;
			}
			
		}
	}
});
containerVue.getDefaultReceiver();
</script>

<!-- 图库显示Model -->
<div class="modal fade " id="selectReceiverModal" tabindex="-1" role="dialog" aria-labelledby="selectReceiverModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
            <h4 class="modal-title" id="selectReceiverModalLabel">收货人选择</h4>
         </div>
         <div class="modal-body">
 			<#include "/user/page-receiver-mgr-tpl.ftl" encoding="utf8"> 
		</div>
     </div>
   </div>
</div><!-- end of modal -->

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

