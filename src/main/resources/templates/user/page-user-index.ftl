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
</head>

<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
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
          <#if ((userBasic.headimgurl)!'')?starts_with('http')>
             <img alt="" src="${userBasic.headimgurl}" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" > 
            <#else>
             <img alt="" src="/user/headimg/show/${(userBasic.userId)?string('#')}"width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" >
          </#if>
        </div>
        <div class="row" style="width:60%;margin:10px 20%;color:gray">
          <span class="pull-left"><img alt="" src="<#if userBasic.sex='1'>/icons/性别-男.png<#elseif userBasic.sex='2'>/icons/性别-女.png<#else>/icons/性别-未知.png</#if>" width="20px" height="20px"> ${userBasic.nickname} </span >
          <span class="pull-right"><img alt="" src="/icons/电话.png" width="20px" height="20px"> ${(userBasic.phone)!''} </span>
        </div>
      </div>
      <div class="row" style="margin:30px 1px 15px 1px;">
       <ul class="nav nav-pills nav-stacked" style="">
         <li style="background-color:white" >
           <a href="/order/user/show/all" >
             <span class=" pull-right" style="border:none">查询所有  &gt;</span>
             <img alt="" src="/icons/订单.png" width="20px" height="20px"> 我的订单
           </a>
           <span class="row">
             <span class="col-xs-3"  style="padding:0 3px">
                <a href="/order/user/show/4pay" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/等待付款.png" alt="" class="weui-tabbar__icon">
                        <span v-if="counts['10']>0" class="weui-badge" style="position: absolute;top: -2px;right: -13px;">{{counts['10']}}</span>
                    </span>
                    <p class="weui-tabbar__label">待付款</p>
                </a>
              </span>
              <span class="col-xs-3"  style="padding:0 3px">
                <a href="/order/user/show/4delivery" class="weui-tabbar__item " >
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待发货.png" alt="" class="weui-tabbar__icon">
                        <span v-if="counts['20']>0" class="weui-badge" style="position: absolute;top: -2px;right: -13px;">{{counts['20']}}</span>
                    </span>
                    <p class="weui-tabbar__label">待发货</p>
                </a>
              </span>
              <span class="col-xs-3" style="padding:0 3px">
                <a href="/order/user/show/4sign" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待收货.png" alt="" class="weui-tabbar__icon">
                        <span v-if="counts['30']>0" class="weui-badge" style="position: absolute;top: -2px;right: -13px;">{{counts['30']}}</span>
                    </span>
                    <p class="weui-tabbar__label">待收货</p>
                </a>
                </a>
              </span>
              <span class="col-xs-3" style="padding:0 3px">
                <a href="/order/user/show/4appraise" class="weui-tabbar__item ">
                    <span style="display: inline-block;position: relative;">
                        <img src="/icons/待评价.png" alt="" class="weui-tabbar__icon">
                        <span v-if="counts['40']>0" class="weui-badge" style="position: absolute;top: -2px;right: -13px;">{{counts['40']}}</span>
                    </span>
                    <p class="weui-tabbar__label">待评价</p>
                </a>
              </span>

            </span>
         </li>
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="/user/spread">
             <span class=" pull-right" style=""> 就加入  &gt;</span>
             <img alt="" src="/icons/服务号-营销推广.png" width="20px" height="20px"> 我要推广
           </a>
         </li>          
         <li style="background-color:white" >
           <a href="/collection/manage">
             <span class="pull-right" >查看 &gt; </span>
             <img alt="" src="/icons/收藏.png" width="20px" height="20px"> 我的收藏
             <span class="badge " style="color:red">${collCnt!''}</span>
           </a>
         </li>
         <li style="background-color:white" >
           <a href="/user/basic/edit">
             <span class="pull-right" style="">查看修改 &gt;</span>
             <img alt="" src="/icons/个人信息.png" width="20px" height="20px"> 个人资料
           </a>
         </li>
         <li style="background-color:white" >
           <a href="/receiver/manage">
             <span class=" pull-right" >编辑  &gt;</span>
             <img alt="" src="/icons/地址管理.png" width="20px" height="20px"> 收货地址
           </a>
         </li>
         <li style="background-color:white" >
           <a href="#">
             <span class=" pull-right" > 联系我们  &gt;</span>
             <img alt="" src="/icons/客服.png" width="20px" height="20px"> 官方客服
           </a>
         </li> 
         <li style="background-color:white" >
           <a href="/aftersales/user/mgr/refund">
             <span class="pull-right" > 退款换货 &gt; </span>
             <img alt="" src="/icons/无忧售后.png" width="20px" height="20px"> 无忧售后
           </a>
         </li>
         <li style="background-color:white" >
           <a href="/complain/manage/order">
             <span class="pull-right" > 订单投诉 &gt; </span>
             <img alt="" src="/icons/投诉.png" width="20px" height="20px"> 我要投诉
           </a>
         </li>  
         <li style="background-color:white" >
           <a href="/srvc/about">
             <span class=" pull-right" > 关于摩放优选  &gt;</span>
             <img alt="" src="/icons/关于我们.png" width="20px" height="20px"> About&Join
           </a>
         </li>       
         <li style="background-color:white" onclick="$(this).addClass('active');$(this).siblings().removeClass('active')">
           <a href="/logout">
             <img alt="" src="/icons/退出登录.png" width="20px" height="20px"> 退出登录
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
            <#if ((userBasic.headimgurl)!'')?starts_with('http')>
             <img alt="" src="${userBasic.headimgurl}" width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" > 
            <#else>
             <img alt="" src="/user/headimg/show/${(userBasic.userId)?string('#')}"width="99px" height="99px" style="padding:1px 1px;border-radius:50%;" >
            </#if>
          </#if>
        </div>
        <div class="row" style="margin:10px 5px;color:gray">
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/积分.png" width="20px" height="20px">${vipBasic.scores!!0}</span ></div>
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/余额.png" width="20px" height="20px"> ${((vipBasic.balance!!0)/100)?string('#0.00')} </span></div>
          <div class="col-xs-4" style="padding:0 3px;text-align:center"><span><img alt="" src="/icons/冻结.png" width="20px" height="20px"> ${((vipBasic.freeze!!0)/100)?string('#0.00')} </span></div>
        </div>
      </div>
      <div class="row" style="margin:30px 1px 15px 1px;text-align:center;vertical-align:center">
      	<div class="col-xs-4" style="padding:3px;">
	       <div style="background-color:white">
		   <a href="<#if vipBasic.status = '1'>/vip/vipset<#else>javascript:;</#if>">
			    <img alt="" src="/icons/账户设置.png" width="90px" height="90px"> 
			    <p >账户设置 </p>
			</a>
	       </div>
	    </div>
	    <div class="col-xs-4" style="padding:3px;">
	      <div style="background-color:white;">
		    <a href="<#if vipBasic.status = '1'><#else>javascript:;</#if>" >
			    <img alt="" src="/icons/提现.png" width="90px" height="90px">
			    <p > 可用余额提现</p>
			</a>
	      </div>
	    </div>
	    <div class="col-xs-4" style="padding:3px;">
	       <div style="background-color:white">
		    <a href="<#if vipBasic.status = '1'>/vip/flow/show<#else>javascript:;</#if>">
			    <img alt="" src="/icons/查看明细.png" width="90px" height="90px"> 
			    <p >资金变动明细</p>
			</a>
		  </div>
	    </div>
	    <div class="col-xs-4" style="padding:3px;">
	       <div style="background-color:white">
		    <a href="<#if vipBasic.status = '1'><#else>javascript:;</#if>">
			    <img alt="" src="/icons/客服.png" width="90px" height="90px"> 
			    <p >官方客服</p>
			</a>
	       </div>
	    </div>
	    <div class="col-xs-4" style="padding:3px;">
	       <div style="background-color:white">
		   <a href="<#if vipBasic.status = '1'>/partner/index<#else>javascript:;</#if>">
			    <img alt="" src="/icons/合作伙伴.png" width="90px" height="90px"> 
			    <p >合作伙伴 </p>
			</a>
	       </div>
	    </div>
      </div>
      	<#if vipBasic.status != '1'>
  			<p style="margin:5px 5px;font-size:18px;color:red">您尚未激活会员账户，所有会员功能将不可用！激活方式见：个人心中／我-基本／我要推广！</p>
  	</#if>
    </div>    

</div> <!-- end of container -->
<script>
var containerVue = new Vue({
	el:'#container',
	data:{
		counts:{'10':0,'20':0,'30':0,'40':0,'SA':0}
	},
	methods:{
		getOrderCounts :function(){
			$.ajax({
				url: '/order/user/count',
				method:'post',
				data: {},
				success: function(jsonRet,status,xhr){
					if(jsonRet && jsonRet.datas){
						for(var i=0;i<jsonRet.datas.length;i++){
							var item = jsonRet.datas[i];
							if('50,51,52,53,54,55,60,61,62,63,64'.indexOf(item.status)>=0){
								containerVue.counts['SA'] = item.cnt;
							}else{
								containerVue.counts[item.status] = item.cnt;
							}
						}
					}else{
						alertMsg('错误提示','系统失败！');
					}
				},
				dataType: 'json'
			});
		}
	}
});
containerVue.getOrderCounts();
</script>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

</body>
</html>