/**
 * 判断字符串是否以指定字串开头
 * @param source
 * @param target
 * @returns
 */
function startWith(source,target){
	if(!source || !target){
		return false;
	}
	if(source.indexOf(target)==0){
		return true;
	}else{
		return false;
	}
}

/**
 * 获取订单状态
 * @param code
 * @returns
 */
var orderStatObj = { '10':'待付款','11':'付款成功','12':'付款失败',
		 '20':'待发货','21':'备货中','22':'已发货',
		 '30':'待签收','31':'已签收',
		 '40':'待评价','41':'评价完成',
		 '50':'买家申请换货','51':'同意换货','52':'买家已发货、等待卖家收货','53':'已收到退货、核验中','54':'核验不通过、协商解决','55':'已重新发货、待收货','56':'待评价','57':'评价完成(换货结束)','58':'卖家不同意换货',
		 '60':'买家申请退款','61':'卖家同意退货','62':'买家已发货、等待卖家收货','63':'已收到退货、核验中','64':'核验不通过、协商解决','65':'同意退款,资金回退中','66':'退款完成','67':'退款失败','68':'卖家不同意退款',
		 'DS':'已取消完成','DR':'取消完成，退款成功','D0':'买家取消，退款中','DF':'买家取消，退款失败',
		 'CM':'交易完成','CC':'已关闭交易(超时6月)'};
function getOrderStatus(code){
	var val = orderStatObj[code];
	if(val){
		return val;
	}else{
		return code;
	}
}

/**
 * 系统对账状态
 */
var oBalStatObj = { '':'还未对账','SS':'成功','S':'初步完成，需再次确认','0':'数据不完整','1':'需要人工核查'};
function getOBalStatus(code){
	var val = oBalStatObj[code];
	if(val){
		return val;
	}else{
		return code;
	}
}

/**
 * 获取配送模式
 * @param code
 * @returns
 */
function getDispatchMode(code){
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
}
/**
 * 获取资金变动类型
 * @param code
 * 资金变动类型(1-可用余额来源，2','可用余额去向，3-冻结金额来源，4-解冻金额去向)
 * @returns
 */
var flowObj = {'10':'服务费','11':'客户退款完成','12':'交易奖励','13':'平台奖励','14':'资金解冻','15':'积分兑换','16':'现金红包','17':'其他','18':'提现失败','19':'卖家退支付手续费',
		 '20':'退款服务费','21':'提现申请','22':'消费','23':'资金冻结','24':'投诉罚款','25':'申请退款','26':'交易奖励','27':'卖家退支付手续费',
		 '31':'冻结交易买卖额','32':'买单投诉冻结','33':'提现冻结','34':'申请退款',
		 '41':'恢复可用余额','42':'提现完成','43':'交易完成','44':'买家退款完成','45':'退款失败'}
function getChangeFlowType(code){
	var val = flowObj[code];
	var prefix = '';
	if(startWith(code,'1')){
		prefix = '可用余额来源：';
	}else if(startWith(code,'2')){
		prefix = '可用余额去向：';
	}else if(startWith(code,'3')){
		prefix = '冻结金额来源：';
	}else if(startWith(code,'4')){
		prefix = '解冻金额去向：';
	}
	if(val){
		return prefix + val;
	}else{
		return code;
	}
}

/**
 * 获取支付方式
 * @returns
 */
function getPayType(tp){
	if('1' == tp){
		return '余额支付';
	}else if(startWith(tp,'2')){
		return '微信支付';
	}else if(startWith(tp,'3')){
		return '支付宝';
	}else{
		return '未知';
	}
}

/**
 * 获取支付状态
 * @param code
 * @returns
 */
var fayFlowObj = {'00':'创建预付单','01':'超时关闭',
		'10':'支付成功,未到账（未收到通知）','11':'支付成功,已到账',
		'20':'退款成功,未到账','21':'退款成功',
		'F1':'支付失败','F2':'退款失败'}
function getPayStatus(code){
	var val = fayFlowObj[code];
	if(val){
		return val;
	}else{
		return code;
	}
}

/**
 * 获取提现方式
 * @param code
 * @returns
 */
function getCashType(code){
	if('1' == code){
		return '手动';
	}else if('2' == code){
		return '自动每天';
	}else if('3' == code){
		return '自动每周';
	}else if('4' == code){
		return '自动每月';
	}
	return '未知';
}


/**
 * 获取通道类型
 * @param code
 * @returns
 */
function getChannelType(code){
	if('1' == code){
		return '银行';
	}else if('2' == code){
		return '微信';
	}else if('3' == code){
		return '支付宝';
	}
	return '未知';
}

/**
 * 获取账户类型
 * @param code
 * @returns
 */
function getAccountType(code){
	if('1' == code){
		return '对私';
	}else if('2' == code){
		return '对公';
	}
	return '未知';
}

/**
 * 获取提现状态
 * @param code
 * @returns
 */
function getCashApplyStatus(code){
	if('0' == code){
		return '待受理';
	}else if('1' == code){
		return '已受理';
	}else if('S' == code){
		return '提现成功到账';
	}else if('F' == code){
		return '提现失败';
	}
	return '未知';	
}

/**
 * 获取合作伙伴状态
 * @returns
 */
function getPartnerStatus(code){
	if("0" == code) {
		return "待审核";
	}else if("1"== code) {
		return "严重违规关店";
	}else if("R" == code) {
		return "终审拒绝";
	}else if("S" == code) {
		return "终审通过";
	}else if("C" == code) {
		return "暂时关闭歇业";
	}else if("A" == code) {
		return "初审拒绝";
	}else if("B" == code) {
		return "初审通过";
	}
	return code;
}

/**
 * 获取所有数据
 * @param isRefresh	是否刷新
 * @param isForward	是否向前插入
 * @param url	获取数据的后台URL
 * @param param	查询参数
 * @param dataList	数据保存数组
 * @param pageCond	分页信息
 * @returns
 */
function getAllData(isRefresh,isForward,url,searchParam,dataList,pageCond){
	$("#loadingData").show();
	$("#nomoreData").hide();
	if(isRefresh){ //清空数据
		pageCond.count = 0;
		dataList.splice(0,dataList.length); 
	}else{
		if(dataList.lenght >= 10*pageCond.pageSize){
			if(isForward){//清除最有一页
				dataList.splice(9*pageCond.pageSize,pageCond.pageSize);
			}else{//清除最前一页
				dataList.splice(0,pageCond.pageSize); 
			}
		}
	}
	$.ajax({
		url: url,
		method:'post',
		data: searchParam,
		success: function(jsonRet,status,xhr){
			if(jsonRet && jsonRet.datas){//
				var i = 0;
				var j = jsonRet.datas.length-1;
				for(;i<jsonRet.datas.length;){
					if(isForward){
						dataList.unshift(jsonRet.datas[j]);
					}else{
						dataList.push(jsonRet.datas[i]);
					}
					i++;j--;
				}
				pageCond.begin = jsonRet.pageCond.begin;
				pageCond.pageSize = jsonRet.pageCond.pageSize;
				pageCond.count = jsonRet.pageCond.count;
			}else{
				//alert(jsonRet.errmsg);
				//$("#nomoreData").show();
				if(jsonRet.errcode === -100000){
					$('#ajaxLoginModal').modal('show');
				}else{
					alertMsg('错误提示',jsonRet.errmsg);
				}
			}
			$("#loadingData").hide();
		},
		failure:function(){
			$("#loadingData").hide();
		},
		dataType: 'json'
	});	 
}

/**
 * 滚动监测分页
 * @param pageCond	分页信息{begin,pageSize,count}
 * @param dataList	查询结果存储数组
 * @param searchFun	执行的查询函数(isRefresh,isForward)，isRefresh：是否刷新已有数据，isForward：是否向前插入数据
 * @returns
 */
function scrollPager(pageCond,dataList,searchFun){
	var winHeight = $(window).height(); //页面可视区域高度   
	var scrollHandler = function () {  
		var pageHieght = $(document.body).height();  
		var scrollHeight = $(window).scrollTop(); //滚动条top   
		var r = (pageHieght - winHeight - scrollHeight) / winHeight;
		if (r>=0 && r < 0.1 && pageCond.count > dataList.length) {//上拉翻页 
			pageCond.begin = pageCond.begin + pageCond.pageSize;
			searchFun(false,false);
		}
		if(scrollHeight <= 0){ //下拉翻页
			if(pageCond.begin <= 0){
    	 			return;
    	 		}
			var currPageCnt = dataList.length % pageCond.pageSize;//当前页的数量
			if(currPageCnt == 0){
				currPageCnt = pageCond.pageSize;
			}
			pageCond.begin = pageCond.begin - (dataList.length-currPageCnt);//总数据的开始
			pageCond.begin = pageCond.begin - pageCond.pageSize;
			if(pageCond.begin <= 0){
				pageCond.begin = 0;
				searchFun(true,true);
			}else{
				searchFun(false,true);
			}
		}
	}
	 //定义鼠标滚动事件  
	$(window).scroll(scrollHandler);
}

/**
 * 获取商品的审核状态
 * @param code
 * @returns
 */
function getGoodsRewResult(code){
	if("0" == code) {
		return "待审核";
	}else if("R" == code) {
		return "审核拒绝";
	}else if("S" == code) {
		return "审核通过";
	}
	return code;
}

/**
 * 获取订单评价状态
 * @param code
 * @returns
 */
function getAppraiseStatus(code){
	if("0" == code) {
		return "待审核";
	}else if("R" == code) {
		return "审核拒绝";
	}else if("S" == code) {
		return "正常";
	}
	return code;
}
