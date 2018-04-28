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
</head>

<body class="light-gray-bg">
<header>
  <div class="weui-navbar">
    <div class="weui-navbar__item <#if mode='basic'>weui-bar__item_on</#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#userBasic').show();$('#userVip').hide();">
	我-基本
	</div>
	<div class="weui-navbar__item <#if mode='vip'>weui-bar__item_on</#if>" onclick="$(this).addClass('weui-bar__item_on');$(this).siblings().removeClass('weui-bar__item_on');$('#userBasic').hide();$('#userVip').show();">
    我-会员
	</div>
  </div>
</header>
<div class="container" style="padding:1px 30px" id="container">
    <div style="height:50px;background-color:#E0E0E0 ;margin-bottom:0px;"></div>
    <!-- 基本信息管理-->
    <div class="row" id="userBasic" style="display:<#if mode='basic'>block<#else>none</#if>"> 
      <div class="row" style="padding:10px 10px;background-color:#880000">
        <div class="row" style="margin:10px 25%;vertical-algin:center;text-align:center">
          <img alt="" src="${(userBasic.headimgurl)!''}" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" > 
        </div>
        <div class="row" style="width:60%;margin:10px 20%;color:gray">
          <span class="pull-left"><img alt="" src="<#if userBasic.sex='1'>/icons/性别-男.png<#elseif userBasic.sex='2'>/icons/性别-女.png<#else>/icons/性别-未知.png</#if>" width="20px" height="20px"> ${userBasic.nickname} </span >
          <span class="pull-right"><img alt="" src="/icons/电话.png" width="20px" height="20px"> ${(userBasic.phone)!''} </span>
        </div>
      </div>
      <div class="row" style="margin:30px 1px 15px 1px;">
       <ul class="nav nav-pills nav-stacked" style="">
         <li style="background-color:white" >
           <a href="/order/show/all" >
             <span class=" pull-right" style="border:none">查询所有  &gt;</span>
             <img alt="" src="/icons/订单.png" width="20px" height="20px"> 我的订单
           </a>
           <span class="row">
             <span class="col-xs-2"  style="padding:0 3px">
                <a href="/order/show/forPay" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/等待付款.png" alt="" class="weui-tabbar__icon">
                        <span class="weui-badge" style="position: absolute;top: -2px;right: -13px;">8</span>
                    </span>
                    <p class="weui-tabbar__label">待付款</p>
                </a>
              </span>
              <span class="col-xs-2"  style="padding:0 3px">
                <a href="/order/show/forDelivery" class="weui-tabbar__item " >
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待发货.png" alt="" class="weui-tabbar__icon">
                        <span class="weui-badge" style="position: absolute;top: -2px;right: -13px;">8</span>
                    </span>
                    <p class="weui-tabbar__label">待发货</p>
                </a>
              </span>
              <span class="col-xs-2" style="padding:0 3px">
                <a href="/order/show/forTake" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待收货.png" alt="" class="weui-tabbar__icon">
                        <span class="weui-badge" style="position: absolute;top: -2px;right: -13px;">8</span>
                    </span>
                    <p class="weui-tabbar__label">待收货</p>
                </a>
                </a>
              </span>
              <span class="col-xs-2" style="padding:0 3px">
                <a href="/order/show/forAppraise" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待评价.png" alt="" class="weui-tabbar__icon">
                        <span class="weui-badge" style="position: absolute;top: -2px;right: -13px;">8</span>
                    </span>
                    <p class="weui-tabbar__label">待评价</p>
                </a>
              </span>
              <span class="col-xs-3" style="padding:0 3px">
                <a href="/order/show/forRefund" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/无忧售后.png" alt="" class="weui-tabbar__icon">
                        <span class="weui-badge" style="position: absolute;top: -2px;right: -13px;">8</span>
                    </span>
                    <p class="weui-tabbar__label">退换货</p>
                </a>
              </span>
            </span>
         </li>
         <li style="background-color:white" >
           <a href="#">
             <span class="pull-right" >查看 &gt; </span>
             <img alt="" src="/icons/收藏.png" width="20px" height="20px"> 我的收藏
             <span class="badge " style="color:red">8 </span>
           </a>
         </li>
         <li style="background-color:white" >
           <a href="/user/basic/edit">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)">查看修改 &gt;</span>
             <img alt="" src="/icons/个人信息.png" width="20px" height="20px"> 个人资料
           </a>
         </li>
         <li style="background-color:white" >
           <a href="#">
             <span class=" pull-right" >编辑  &gt;</span>
             <img alt="" src="/icons/地址.png" width="20px" height="20px"> 收货地址
           </a>
         </li>
         <li style="background-color:white" >
           <a href="#">
             <span class=" pull-right" > 联系我们  &gt;</span>
             <img alt="" src="/icons/客服.png" width="20px" height="20px"> 官方客服
           </a>
         </li> 
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="/user/spread">
             <span class=" pull-right" style=""> 就加入  &gt;</span>
             <img alt="" src="/icons/服务号-营销推广.png" width="20px" height="20px"> 我要推广
           </a>
         </li>       
       </ul>
      </div>
    </div>
    <!-- 会员信息管理-->
    <div class="row" id="userVip" style="display:<#if mode='vip'>block<#else>none</#if>"> 
      <div class="row" style="padding:10px 10px;background-color:#880000">
        <div class="row" style="margin:10px 25%;vertical-algin:center;text-align:center">
          <#if vipBasic.status != '1'>
           <img alt="" src="/icons/无效对象.png" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" >
          <#else>
          <img alt="" src="${userBasic.headimgurl!''}" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" > 
          </#if>
        </div>
        <div class="row" style="margin:10px 5px;color:gray">
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/积分.png" width="20px" height="20px">${vipBasic.scores!!0}</span ></div>
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/余额.png" width="20px" height="20px"> ${vipBasic.balance!!0} </span></div>
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/冻结.png" width="20px" height="20px"> ${vipBasic.freeze!!0} </span></div>
        </div>
      </div>
      <div class="row" style="margin:30px 1px 15px 1px;">
       <ul class="nav nav-pills nav-stacked" style="">
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="<#if vipBasic.status = '1'><#else>javascript:;</#if>" >
             <span class="badge pull-right" style="background-color:rgb(239,239,239);border:none">申请转出  &gt;</span>
             <img alt="" src="/icons/提现.png" width="20px" height="20px"> 提现
           </a>
         </li>
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="<#if vipBasic.status = '1'><#else>javascript:;</#if>">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)">查询所有资金流  &gt;</span>
             <img alt="" src="/icons/查看明细.png" width="20px" height="20px"> 资金变动明细
           </a>
         </li>
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="<#if vipBasic.status = '1'><#else>javascript:;</#if>">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)"> 联系我们  &gt;</span>
             <img alt="" src="/icons/客服.png" width="20px" height="20px"> 官方客服
           </a>
         </li>
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="<#if vipBasic.status = '1'>/partner/index<#else>javascript:;</#if>">
             <span class="badge pull-right" style="background-color:rgb(239,239,239)"> 申请开通商铺  &gt;</span>
             <img alt="" src="/icons/合作伙伴.png" width="20px" height="20px"> 合作伙伴
           </a>
         </li>
         <#if vipBasic.status != '1'>
         <p style="margin:5px 5px;font-size:18px;color:red">您尚未激活会员账户，所有会员功能将不可用！激活方式见：个人心中／我-基本／我要推广！</p>
         </#if>
       </ul>
      </div>
    </div>    
</div>
<script type="text/javascript">
var containerVue = new Vue({
	el:"#container",
	data:{
		userBasic:{
			headimgurl:'',
			nickname:'',
			phone:''
		},
		userVip:{}
	}
});
function getBasic(){
	$.ajax({
		url: '/user/basic/get',
		data: {},
		success: function(jsonRet,status,xhr){
			if(jsonRet){
				if(0 == jsonRet.errcode){
					containerVue.userBasic = jsonRet.datas;
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
<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>
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