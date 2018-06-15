<!-- 用户登录模态框（Modal） -->
<div class="modal fade " id="ajaxLoginModal" tabindex="-1" role="dialog"  aria-hidden="false" data-backdrop="static" style="top:10%">
<div class="modal-dialog">
<div class="modal-content">
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
</div>
<div class="modal-body">
  <div class="templatemo-content-widget templatemo-login-widget white-bg" >
	<header class="text-center">
      <div class="square"></div>
      <h1>欢迎</h1>
    </header>
    <form class="templatemo-login-form" method="post">
    	<div class="form-group">
    		<div class="input-group">
        		<div class="input-group-addon"><i class="fa fa-user fa-fw"></i></div>	        		
            <input class="form-control" type="text" v-model="param.username" required placeholder="邮箱/手机号">           
        </div>	
    	</div>
    	<div class="form-group">
    		<div class="input-group">
        		<div class="input-group-addon"><i class="fa fa-key fa-fw"></i></div>	        		
            <input class="form-control" type="password" v-model="param.password"  required placeholder="******">           
        </div>	
    	</div>
		<div class="form-group">
			<button type="button" class="templatemo-blue-button width-100" @click="login"> 登 录 </button>
		</div>
    </form>
  </div>
  <div class="templatemo-content-widget templatemo-login-widget templatemo-register-widget white-bg">
    <div class="row" style="padding:0 20px">
      <a href="/login" class="pull-right" >还未有账号? </a>
    </div>
  </div>
</div>
</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
var ajaxLoginVue = new Vue({
	el:'#ajaxLoginModal', 
	data:{
		param:{
			username:'',
			password:''
		}
	},
	methods:{
	  	login: function(){
		  if(!this.param.username){
			  alertMsg('错误提示','用户名：不可为空！')
			  return;
		  }
		  if(!this.param.password){
			  alertMsg('错误提示','登录密码：不可为空！')
			  return;
		  }
		  $.ajax({
				url: '/login-ajax',
				method:'post',
				data: this.param,
				success: function(jsonRet,status,xhr){
					$("#dealingData").hide();
					if(jsonRet && jsonRet.errmsg){
						if(jsonRet.errcode < 0){
							alertMsg('错误提示',jsonRet.errmsg)
						}else{
							if(jsonRet.errcode === 0){
								$('#ajaxLoginModal').modal('hide');
							}
						}
					}else{
						alertMsg('错误提示','登录失败！')
					}
				},
				failure:function(){
					$("#dealingData").hide();
				},
				dataType: 'json'
			});
	  	}
	}
});
</script>
