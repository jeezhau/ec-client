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
<body class="light-gray-bg" >
<header >
	<ul class="nav nav-tabs" style="margin:3px 8px 3px 8px">
	  <li onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#manageNotice').show();$('#goodsListShow').hide()">
	    <a href="javascript:;"  style="padding:10px 8px">管理须知</a>
	  </li>
	  <li class="active"  onclick="$(this).addClass('active');$(this).siblings().removeClass('active');$('#manageNotice').hide();$('#goodsListShow').show()">
	    <a href="javascript:;"  style="padding:10px 8px">我的商品清单</a>
	  </li>
	</ul>
</header>
<div class="container goods-container" id="container" style="overflow:scroll">

  <div class="row" id="manageNotice" style="margin:5px 0 ;display:none;"><!-- 管理须知 -->
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
	    </p>
    </div>
    <div class="col-xs-12" style="margin-top:10px;text-align:center">
     <a class="btn btn-primary" href="/goods/edit/0">新建商品</a>
     <p>一旦您新建商品将默认同意以上须知规则！</p>
    </div>  
  </div>
    
  <div class="row" id="goodsListShow" style="margin-top:5px" ><!-- 商品管理列表 -->
    <div class="row" style="margin:1px 3px;background-color:white">
      <div class="col-xs-6" style="padding:0 2px">
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
      <div class="col-xs-6" style="padding:0 2px">
	      <div class="form-group">
	        <label class="col-xs-4 control-label" style="padding-right:1px">审核状态</label>
	        <div class="col-xs-8" style="padding-left:1px">
		        <select class="form-control" v-model="param.reviewResult" required>
		            <option value="0">待审核</option>
		            <option value="1">通过</option>
		            <option value="2">拒绝</option>
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
    <div class="row" style="margin:1px 0">
 	  <ul class="list-group" style="padding:0 0px;background-color:white;margin:1px 0px;">
	    <li v-for="item in goodsList" class="list-group-item" style="padding-left:20px;padding-right:20px">
	      <input type="checkbox" v-model="param.selectedArr" v-bind:value="item.goodsId" style="display:inline-block;padding:0 5px;width:15px;height:15px">
	      <a v-bind:href="'/goods/edit/' + item.goodsId" style="max-width:80%"><span>{{item.goodsName}}</span></a>
	      <span style="margin-left:8px;background-color:white;">
	        <input type="number" v-bind:value="item.stock" style="width:50px;text-align:right" v-on:change="changeStock(item.goodsId,event)">件
	      </span>
	    </li>
	  </ul>
    </div>
  </div>
</div><!-- end of container -->
<footer>
  <#include "/menu/page-partner-func-menu.ftl" encoding="utf8"> 
</footer>
<script type="text/javascript">
 var containerVue = new Vue({
	 el:'#container',
	 data:{
		param:{
			status:'1', 
			reviewResult:'0',
			selectedArr:[]
		},
		goodsList:[] 
	 },
	 methods:{
		 getAll: function(){
			 containerVue.goodsList = [];
			 $.ajax({
					url: '/goods/getall',
					method:'post',
					data: this.param,
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.datas){
								for(var i=0;i<jsonRet.datas.length;i++){
									containerVue.goodsList.push(jsonRet.datas[i]);
								}
							}else{
								alert(jsonRet.errmsg);
							}
						}else{
							alert('获取数据失败！')
						}
					},
					dataType: 'json'
				});			 
		 },
		 changeStatus: function(newStat){
			 if(this.param.selectedArr.length<1){
				 alert("请选择要批量" + (newStat==='1'?'上架':'下架')+ "的商品！");
				 return
			 }
			 $.ajax({
					url: '/goods/changeStatus',
					method:'post',
					data: {'goodsIds':this.param.selectedArr.join(','),'newStatus':newStat},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.errcode ==0){
								containerVue.getAll();
								containerVue.param.selectedArr = [];
							}else if(jsonRet.errmsg && jsonRet.errmsg != 'ok'){
								alert(jsonRet.errmsg);
								
							}
						}else{
							alert('获取数据失败！')
						}
					},
					dataType: 'json'
				});
		 },
		 changeStock:function(goodsId,event){
			 var newCnt = $(event.target).val();
			 $.ajax({
					url: '/goods/changeStock',
					method:'post',
					data: {'goodsId':goodsId,'newCnt':newCnt},
					success: function(jsonRet,status,xhr){
						if(jsonRet ){
							if(jsonRet.errmsg && jsonRet.errmsg != 'ok'){
								alert(jsonRet.errmsg);
							}
						}else{
							alert('获取数据失败！')
						}
					},
					dataType: 'json'
				});
		 }
	 }
 });
 containerVue.getAll();
 
 var winHeight = $(window).height(); //页面可视区域高度   
 var scrollHandler = function () {  
     var pageHieght = $(document.body).height();  
     var scrollHeight = $(window).scrollTop(); //滚动条top   
     var r = (pageHieght - winHeight - scrollHeight) / winHeight;
     if (r < 0.5) {//0.5是个参数  
         if (i % 10 === 0) {//每10页做一次停顿！  
             getData(i);
             //$(window).unbind('scroll');  
             $("#btn_Page").show();  
         } else {  
             getData(i);  
             $("#btn_Page").hide();  
         }  
     }  
 }  
 //定义鼠标滚动事件  
 $(window).scroll(scrollHandler);  
 

</script>  
</body>
</html>
