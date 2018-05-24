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
    
    <!-- 文件上传 -->
    <script src="/script/fileinput.min.js" type="text/javascript"></script>
    <script src="/script/zh.js" type="text/javascript"></script>
    <link href="/css/fileinput.min.css" rel="stylesheet">
    
    <link href="/css/mfyx.css" rel="stylesheet">
    <script src="/script/common.js" type="text/javascript"></script>
</head>
<body class="light-gray-bg">
<#include "/common/tpl-msg-alert.ftl" encoding="utf8">
<#if ((vipBasic.status)!'') == '1'>
<div class="container"  id="container" style="padding:0px 0px;oveflow:scroll">
  <div class="row" style="width:100%;margin:0px 0px 0px 0px;padding:5px 8px;background-color:white;">
    <p style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;请正确输入银行账户信息，如果信息有误，导致的打款失败或错误，将由会员您自己承担！！！</p>
	<form class="form-horizontal"  action="" method ="post" autocomplete="on" enctype="multipart/form-data" role="form" >
	  <div class="form-group">
	    <label class="col-xs-3 control-label" style="padding-right:0">提现方式<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <select v-model="param.cashTp">
	        <option value="1">手动</option>
	        <option value="2">自动(每周一)</option>
	        <option value="3">自动(每月一号)</option>
	      </select>
	    </div>
	  </div>	
	  <div class="form-group">
	    <label class="col-xs-3 control-label" style="padding-right:0">账户类型<span style="color:red">*</span></label>
	    <div class="col-xs-8">
		   <label><input type="radio" value="1" v-model="param.actTp" style="display:inline-block;width:18px;height:18px" >对私</label>
		   <label><input type="radio" value="2" v-model="param.actTp" style="display:inline-block;width:18px;height:18px" >对公</label>
	    </div>
	  </div>	  
	  <div class="form-group">
	    <label class="col-xs-3 control-label" style="padding-right:0">身份证号<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="text" class="form-control" v-model="param.idNo" pattern="\w{18}"  maxLength=100 required placeholder="请输入开户人身份证号">
	    </div>
	  </div>
	  <div class="form-group">
	    <label class="col-xs-3 control-label" style="padding-right:0">账户名称<span style="color:red">*</span></label>
	    <div class="col-xs-8">
	      <input type="text" class="form-control" v-model="param.actNm" pattern="\w{2,100}" maxLength=100 required placeholder="请输入账户名称（2-100个字符）">
	    </div>
	  </div>
	  <div class="form-group">
        <label class="col-xs-3 control-label" style="padding-right:0">账户号<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="text" class="form-control" v-model="param.actNo" pattern="\w{3,30}" maxLength=30 required placeholder="请输入账户号（3-30个字符）">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label" style="padding-right:0">开户行名称<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="text" class="form-control" v-model="param.actBlk" pattern="\w{2,100}" maxLength=100 required placeholder="请输入开户行名称（2-100个字符）">
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label"  style="padding-right:0">会员密码<span style="color:red">*</span></label>
        <div class="col-xs-8">
          <input type="password" class="form-control" v-model="param.pwd" pattern="\w{6,20}" maxLength=20 required placeholder="请输入会员密码（6-20个字符）">
        </div>
      </div>
      <div class="form-group">
         <div style="text-align:center">
           <button type="button" class="btn btn-info"  style="margin:20px" @click="submit">&nbsp;&nbsp;提 交&nbsp;&nbsp;</button>
           <button type="button" class="btn btn-warning"  style="margin:20px" @click="reset">&nbsp;&nbsp;重 置&nbsp;&nbsp; </button>
         </div>
      </div>
	</form>	
  </div>
</div><!-- end of container -->
<script type="text/javascript">
var containerVue = new Vue({
	el:'#container',
	data:{
		initData:{
			cashTp:'${(vipBasic.cashType)!""}',
			idNo:'${(vipBasic.idNo)!""}',
			actTp:'${(vipBasic.accountType)!""}',
			actNm:'${(vipBasic.accountName)!""}',
			actNo:'${(vipBasic.accountNo)!""}',
			actBlk:'${(vipBasic.accountBank)!""}',
		},	//初始化的数据
		param:{
			cashTp:'${(vipBasic.cashType)!""}',
			idNo:'${(vipBasic.idNo)!""}',
			actTp:'${(vipBasic.accountType)!""}',
			actNm:'${(vipBasic.accountName)!""}',
			actNo:'${(vipBasic.accountNo)!""}',
			actBlk:'${(vipBasic.accountBank)!""}',
			pwd:''
		}
	},
	methods:{
		submit: function(){
			$("#dealingData").show();
			$.ajax({
				url: '/vip/vipset/updact',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode !== 0){
							alertMsg('错误提示',jsonRet.errmsg)
						}else{
							window.location.href = "/vip/vipset";
						}
					}else{
						alertMsg('错误提示','绑定邮箱失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
		},
		reset: function(){
			$.extend(containerVue.param,containerVue.initData); 
		}
	}
});

</script>
</#if>

<#if errmsg??>
 <#include "/error/tpl-error-msg-modal.ftl" encoding="utf8">
</#if>

<footer>
  <#include "/menu/page-bottom-menu.ftl" encoding="utf8"> 
</footer>

</body>
</html>
