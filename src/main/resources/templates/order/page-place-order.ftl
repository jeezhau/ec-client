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
    
    <script src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <link href="/css/weui.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
    
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.6&key=2b12c05334ea645bd934b55c8e46f6ea"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
    <script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main.css"/>
</head>
<body class="light-gray-bg"  style="overflow-y:scroll;overflow-x:hidden">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#include "/common/tpl-loading-and-nomore-data.ftl" encoding="utf8">
<#include "/user/tpl-ajax-login-modal.ftl" encoding="utf8">

<#if goods??>
<div class="container goods-container" id="container" style="padding:0 0;overflow:scroll">
 <div class="row" style="margin:5px 1px 1px ;" >
     <div style="margin:1px 0;padding:0 5px;background-color:white;font-size:130%;text-align:center" >
      <a href="/shop/goods/${(goods.goodsId)?string('#')}">  ${(goods.goodsName)!''} </a>
     </div>
 </div>
 <div class="row " style="margin:0">
  <!-- 图片轮播 -->
  <div id="myCarousel" class="carousel slide">
    <!-- 轮播（Carousel）指标 -->
    <ol class="carousel-indicators">
        <li v-for="item,index in goods.courselImgPaths" data-target="#myCarousel" :data-slide-to="index" v-bind:class="{ active: index===0 }"></li>
    </ol>   
    <!-- 轮播（Carousel）项目 -->
    <div class="carousel-inner">
        <div  v-bind:class="[{active:(index===0)}, 'item']" v-for="imgpath,index in goods.courselImgPaths" >
            <img :src="'/shop/gimage/${(goods.partnerId)?string('#')}/' + imgpath" style="max-width:100%;max-height:600px;">
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="carousel-control left" href="#myCarousel" data-slide="prev"><span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-left"></span></a>
    <a class="carousel-control right" href="#myCarousel" data-slide="next"><span _ngcontent-c3="" aria-hidden="true" class="glyphicon glyphicon-chevron-right"></span></a>
  </div>
  
  <!-- 商家信息 -->
  <div class="row" style="margin:3px 15px;background-color:white;padding:3px 8px;">
    <a href="/shop/mcht/${(goods.partnerId)?string('#')}">
     <img class="pull-left" alt="" src="/shop/pcert/logo/${(goods.partnerId)?string('#')}" style="width:25px;height:25px;border-radius:30%">
    </a>
   <span class="pull-right">${(goods.partner.province)!''}-${(goods.partner.area)!''}-${(goods.partner.addr)!''}</span>
  </div> 
  
  <!-- 限购信息 -->
  <div v-if="goods.limitedNum > 0" class="row" style="margin:3px 15px;background-color:white;color:red;padding:3px 8px;">
   <span class="pull-left">每人限购数量(包括已购)：{{goods.limitedNum}} 件</span>
   <span class="pull-right">限购时间：{{goods.beginTime}} 至 {{goods.endTime}}</span>
  </div>  
  
  <form action="/order/create" method="post" style="margin-bottom:80px">
    <input type="hidden" name="goodsId" value="${(goods.goodsId)?string('#')}">
    <div class="row" style="margin:15px 1px;">
      <div class="row" style="margin:3px 15px 3px 15px;padding:3px 3px">
        <label class="col-xs-12 control-label">购买数量(具体库存以付款提交时为准)<span style="color:red">*</span>:</label>
        <div class="col-xs-12" style="padding:0px 3px">
	       <table class="table table-striped table-bordered table-condensed" style="font-size:15px">
	         <tr >
	           <th width="30%" style="padding:2px 2px">规格名称</th>
	           <th width="20%" style="padding:2px 2px">销售单位</th>
	           <th width="15%" style="padding:2px 2px">售价(¥)</th>
	           <th width="35%" style="padding:2px 2px;color:red;text-align:center">购买数量</th>
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
	           <td style="padding:2px 2px;text-align:center">
	             <button type="button" class="btn btn-danger btn-xs" style="margin:0" @click="addBuyNum(index,'sub')">-</button>
                 <input type="number" :id="'buyNum_'+index" style="width:50%;text-align:center" min=0 :max="item.stock" value=0 @change="changeBuyNum(index)">
                 <button type="button" class="btn btn-danger btn-xs" style="margin:0" @click="addBuyNum(index,'add')">+</button>
	           </td>
	         </tr>
	       </table>    
        </div>
      </div>
      <div class="row" style="margin:3px 15px 3px 15px;padding:5px 3px;" id="receiverAddr">
        <!-- <label class="col-xs-12 control-label" style="vertical-algin:center;">配送信息(选择收货地址)<span style="color:red">*</span>:</label> -->
        <div class="col-xs-12">
          <div class="row">
            <div class="col-xs-9" style="padding:0;font-weight:bolder">
              <div class="row" style="margin:0">
	              <div class="col-xs-6" style="padding:0">
	                <input type="hidden" name="recvId" v-model="param.recvId">
	                <input type="text" class="form-control" name="recvName" v-model="param.recvName" disabled style="width:100%;" placeholder="收货人姓名" required>
	              </div>
	              <div class="col-xs-6" style="padding:0">
	                <input type="text" class="form-control" name="recvPhone" v-model="param.recvPhone" disabled style="width:100%" placeholder="联系电话" required>
	              </div>
              </div>
              <div class="row" style="margin:0;font-weight:bolder">
              	<div class="col-xs-3" style="padding:0 ">
              	 <input type="hidden" name="recvCountry" v-model="param.recvCountry">
                  <input type="text" class="form-control" name="recvProvince" v-model="param.recvProvince" disabled placeholder="省份" required>
                 </div>
              	<div class="col-xs-4" style="padding:0">
                  <input type="text" class="form-control" name="recvCity" v-model="param.recvCity" disabled placeholder="市" required>
                 </div>
              	<div class="col-xs-5" style="padding:0">
                  <input type="text" class="form-control" name="recvArea" v-model="param.recvArea" disabled placeholder="区县" required>
                 </div>                                  
              </div>
            </div>
            <div class="col-xs-3" style="vertical-algin:center;font-weight:bolder;">
              <a class="btn btn-default" href="javascript:;" @click="selectReceiver">
               <img alt="" src="/icons/收货地址.png" width="70%" height="70%" style="max-width:30px;max-height:30px"><br>
               <span style="font-size:14px">收货地址</span>
              </a>
            </div>
          </div>
          <div class="row" style="font-weight:bolder">
            <input type="text" class="form-control" name="recvAddr" v-model="param.recvAddr" disabled placeholder="收货人地址" required>
          </div>
        </div>
      </div> 
      <div class="row" style="text-align:right;">
        <div class="col-xs-12" style="padding-right:40px">
         <button v-if="param.flag == 0" type="button" class="btn btn-sm btn-danger" @click="checkData">请点击确认购买内容与收货信息</button>
        </div>
      </div>
      <div class="row" style="margin:8px 15px 3px 15px;">
        <label class="col-xs-3 control-label" style="padding-right:0">配送方式<span style="color:red">*</span>:</label>
        <div class="col-sx-9" style="padding-left:0">
          <select required v-model="param.postageIdAndMode" @change="changeDispatch">
            <option value="" disabled>请选择(须先完成数据检查计算)...</option>
            <option v-for="item in dispatchMatchs" v-bind:value="item.postageId + '-' + item.mode">
              {{item.postageId + '#' + getDispatchMode(item.mode) + ' ¥' + item.carrage}} 
            </option>
          </select>
        </div>
       </div>
       <div class="row" style="margin:8px 15px 3px 15px;">
         <label class="col-xs-3 control-label" style="padding-right:0">买家留言:</label>
         <div class="col-sx-9" style="padding-left:0">
           <textarea class="form-control" style="width:66%" maxlength=600 name="remark"></textarea>
         </div>
       </div> 
     </div>
     
     <div class="row" style="margin:15px 25px;padding:3px 3px;">
       <div class="col-xs-7" style="font-size:16px;">
         <div class="col-xs-12" style="padding:0">
           <div class="col-xs-6" style="padding:0">
             共<span style="color:red"> {{param.countAll}} </span>件
           </div>
           <div class="col-xs-6" style="padding:0">
             <span style="color:red"> {{new Number(param.amount).toFixed(2)}} </span>元<br>
           </div>
         </div>
          <div class="col-xs-12" style="padding:0">
           <div class="col-xs-6" style="padding:0">
             <label>邮费</label>
           </div>
           <div class="col-xs-6" style="padding:0">
             <span style="color:red"> {{param.carrage}} </span>元<br>
           </div>
         </div> 
         <div class="col-xs-12" style="padding:0">
           <div class="col-xs-6" style="padding:0">
             <label>总金额</label>
           </div>
           <div class="col-xs-6" style="padding:0">
             <span style="color:red"> {{(new Number(param.amount) + new Number(param.carrage)).toFixed(2)}} </span>元<br>
           </div>
         </div>        
       </div>
       <div class="col-xs-5" style="text-align:center;padding-right:0px">
         <input type="hidden" name="goodsSpec" v-model="param.goodsSpec" required>
         <input type="hidden" name="dispatchMode" v-model="param.dispatchMode" required>
	     <input type="hidden" name="postageId" v-model="param.postageId" required>
	     <button v-if="param.flag == 1" type="button" class="btn btn-sm btn-danger" >请选择配送方式</button>
         <button v-if="param.flag == 2" type="submit" class="btn btn-sm btn-danger" style="font-size:18px;font-weight:bold" @click="submitCheck">立即下单</button>
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
			limitedNum:${(goods.limitedNum)?string('#')},
			beginTime:'${(goods.beginTime)!""}',
			endTime:'${(goods.endTime)!""}',
			courselImgPaths:'${(goods.carouselImgPaths)!""}'.split(','),
			specDetailArr:JSON.parse('${(goods.specDetail)!"[]"}'),
		},
		dispatchMatchs:[],
		param:{
			flag:0,//计算标志 0-待检查数据,1-待选择配送方式，2-提交支付
			goodsId:${(goods.goodsId)?string('#')},
			recvId:'',
			recvName:'',
			recvPhone:'',
			recvAddr:'',
			recvCountry:'',
			recvProvince:'',
			recvCity:'',
			recvArea:'',
			goodsSpec:'',
			countAll:0,
			carrage:0,
			amount:0,//购买金额
			dispatchMode:'',
			postageId:'',
			postageIdAndMode:''
		}
	},
	methods:{
		changeBuyNum:function(index){
			var spec = this.goods.specDetailArr[index];
			var value = $('#buyNum_' + index).val();
			var val = parseInt(value);
			if(isNaN(val) ){
				$('#buyNum_' + index).focus();
				return false;
			}
			$('#buyNum_' + index).val(val);
			spec.buyNum = val;
			
			this.param.countAll = 0;
			for(var i=0;i<this.goods.specDetailArr.length;i++){
				var sp = this.goods.specDetailArr[i];
				var num =  sp.buyNum ? sp.buyNum : 0;
				this.param.countAll = this.param.countAll + num;
			}
			this.param.flag = 0;//重新检查
			this.dispatchMatchs = [];
			this.param.carrage = 0;
			this.param.dispatchMode = '';
			this.param.postageId = ''
			this.param.postageIdAndMode = '';
		},
		addBuyNum:function(index,type){
			var spec = this.goods.specDetailArr[index];
			if(!spec.buyNum){
				spec.buyNum = 0;
			}
			if(type == 'add'){
				spec.buyNum = spec.buyNum + 1;
			}else{
				if(spec.buyNum == 0){
					return;
				}
				spec.buyNum = spec.buyNum - 1;
			}
			$('#buyNum_' + index).val(spec.buyNum);
			this.param.countAll = 0;
			for(var i=0;i<this.goods.specDetailArr.length;i++){
				var sp = this.goods.specDetailArr[i];
				var num =  sp.buyNum ? sp.buyNum : 0;
				this.param.countAll = this.param.countAll + num;
			}
			this.param.flag = 0;//重新检查
			this.dispatchMatchs = [];
			this.param.carrage = 0;
			this.param.dispatchMode = '';
			this.param.postageId = ''
			this.param.postageIdAndMode = '';
		},
		changeDispatch:function(){
			var arr = this.param.postageIdAndMode.split("-");
			this.param.postageId = arr[0];
			this.param.dispatchMode = arr[1];
			for(var i=0;i<this.dispatchMatchs.length;i++){
				var dm = this.dispatchMatchs[i];
				if(dm.postageId == arr[0] && dm.mode == arr[1]){
					this.param.carrage = dm.carrage;
				}
			}
			this.param.goodsSpec = JSON.stringify(this.goods.specDetailArr);
			this.param.flag = 2;//可提交
		},
		getDefaultReceiver:function(){
			$.ajax({
				url: '/receiver/getdefault',
				method:'get',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.receiver){
						containerVue.param.recvId = jsonRet.receiver.recvId;
						containerVue.param.recvPhone = jsonRet.receiver.phone;
						containerVue.param.recvName = jsonRet.receiver.receiver;
						containerVue.param.recvCountry = jsonRet.receiver.country;
						containerVue.param.recvProvince = jsonRet.receiver.province;
						containerVue.param.recvCity =  jsonRet.receiver.city;
						containerVue.param.recvArea = jsonRet.receiver.area;
						containerVue.param.recvAddr = jsonRet.receiver.addr;
					}else{
						if(jsonRet && jsonRet.errcode === -100000){
							$('#ajaxLoginModal').modal('show');
						}else{
							//alertMsg('错误提示','获取默认收货人信息失败！');
						}
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
				containerVue.param.recvCountry = receiver.country;
				containerVue.param.recvProvince = receiver.province;
				containerVue.param.recvCity =  receiver.city;
				containerVue.param.recvArea = receiver.area;
				containerVue.param.recvAddr = receiver.addr;
				$('#selectReceiverModal').modal('hide');
				containerVue.param.flag = 0;//重新检查
				containerVue.dispatchMatchs = [];
				containerVue.param.carrage = 0;
				containerVue.param.dispatchMode = '';
				containerVue.param.postageId = '';
				containerVue.param.postageIdAndMode = '';
			}
		},
		checkData: function(){
			this.param.countAll = 0;
			this.param.amount = 0;
			for(var i=0;i<this.goods.specDetailArr.length;i++){
				var sp = this.goods.specDetailArr[i];
				var num =  sp.buyNum ? sp.buyNum : 0;
				this.param.countAll = this.param.countAll + num;
				this.param.amount += sp.price * num;
			}
			if(this.param.countAll<=0 || !this.param.recvId){//购买数量为0或为选择收货信息
				alertMsg('错误提示',"请输入购买数量并选择收货人信息！");
				return;
			}
			$.ajax({
				url: '/order/checkData',
				method:'post',
				data: {'goodsId':this.param.goodsId,'recvId':this.param.recvId,'goodsSpec':JSON.stringify(this.goods.specDetailArr)},
				success: function(jsonRet,status,xhr){
					if(jsonRet){
						if(jsonRet.match){
							containerVue.dispatchMatchs = [];
							for(var i=0;i<jsonRet.match.length;i++){
								//{postageId:'',mode:'',carrage:''}
								containerVue.dispatchMatchs.push(jsonRet.match[i]);
								containerVue.param.flag = 1;
							}
						}else{//出现逻辑错误
							if(jsonRet.errcode === -100000){
								$('#ajaxLoginModal').modal('show');
							}else{
								alertMsg('错误提示',jsonRet.errmsg);
							}
						}
					}else{
						alertMsg('错误提示','系统数据访问失败！')
					}
				},
				dataType: 'json'
			});
		},
		submitCheck:function(){
			if(this.param.flag != 2){
				alertMsg('系统提示','请完成数据检查并选择配送方式！');
				return false;
			}
			$("#receiverAddr .form-control[disabled]").removeAttr("disabled"); 
			return true;
		}
	}
});
containerVue.getDefaultReceiver();

//获取界面上轮播图容器
var $carousels = $('#myCarousel');
var startX,endX;
// 在滑动的一定范围内，才切换图片
var offset = 50;
// 注册滑动事件
$carousels.on('touchstart',function (e) {
    // 手指触摸开始时记录一下手指所在的坐标x
    startX = e.originalEvent.touches[0].clientX;

});
$carousels.on('touchmove',function (e) {
    // 目的是：记录手指离开屏幕一瞬间的位置 ，用move事件重复赋值
    endX = e.originalEvent.touches[0].clientX;
});
$carousels.on('touchend',function (e) {
    //console.log(endX);
    //结束触摸一瞬间记录手指最后所在坐标x的位置 endX
    //比较endX与startX的大小，并获取每次运动的距离，当距离大于一定值时认为是有方向的变化
    var distance = Math.abs(startX - endX);
    if (distance > offset){
        //说明有方向的变化
        //根据获得的方向 判断是上一张还是下一张出现
        $(this).carousel(startX > endX ? 'next':'prev');
    }
});
</script>

<!-- 收货人显示Model -->
<div class="modal fade " style="height:450px;overflow:scroll" id="selectReceiverModal" tabindex="-1" role="dialog" aria-labelledby="selectReceiverModalLabel" aria-hidden="true" data-backdrop="static">
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
</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>

