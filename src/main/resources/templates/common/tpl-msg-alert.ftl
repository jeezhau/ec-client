
<!-- 消息提示模态框（Modal） -->
<div class="modal fade " id="alertMsgModal" tabindex="-1" role="dialog" aria-labelledby="alertMsgTitle" aria-hidden="false" data-backdrop="static" style="top:20%">
	<div class="modal-dialog">
  		<div class="modal-content">
     		<div class="modal-header">
        			<button type="button" class="close" data-dismiss="modal"  aria-hidden="true">× </button>
        			<h4 class="modal-title" id="alertMsgTitle" style="color:red" ></h4>
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
	$('#alertMsgModal').modal('show');
}
</script>
