
<!-- 消息提示模态框（Modal） -->
<div id="alertMsgModal" class="" style="position:fixed;left:0;top:0;right:0;bottom:0;margin:0;width:100%;display:none;z-index:100000000;background:rgba(0,0,0,0.2);display:none;">
  <div style="margin:20% 10%">
  		<div class="modal-content">
     		<div class="modal-header">
     		  <h4 class="modal-title" id="alertMsgTitle" style="display:inline-block;color:red" ></h4>
        	      <button type="button" class="close" onclick="$('#alertMsgModal').hide()">× </button>
     		</div>
     		<div class="modal-body">
       			<p id="alertMsgContent"> </p><p/>
     		</div>
     		<div class="modal-footer">
     			<div style="margin-left:50px">
        			</div>
     		</div>
  		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<script>
function alertMsg(title,content){
	if($("#dealingData")){
		$("#dealingData").hide();
	}
	$('#alertMsgTitle').html(title);
	$('#alertMsgContent').html(content);
	$('#alertMsgModal').show();
}
function hideAlertMsg(){
	if($("#dealingData")){
		$("#dealingData").hide();
	}
}
</script>
