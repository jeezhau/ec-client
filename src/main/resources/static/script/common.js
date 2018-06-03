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
		 'DS':'已取消完成','D0':'买家取消','D1':'买家取消',
		 'C':'已关闭交易(超时6月)'};
function getOrderStatus(code){
	var val = orderStatObj[code];
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
 * 资金变动类型(1-可用余额来源，2','可用余额去向，3-冻结金额来源，4-解冻金额去向)':
 * 	11-客户退款完成、12-系统分润、13-平台奖励、14-资金解冻、15-积分兑换、16-现金红包、17-其他
 * 	21-提现申请、22-消费、23-资金冻结、24-投诉罚款、25-其他
 * 	31-冻结交易买卖额、32-买单投诉冻结、33-提现冻结、34-其他
 * 	41-恢复可用余额、42-提现完成、43-交易完成、44-买家退款完成
 * @returns
 */
var flowObj = {'11':'客户退款完成','12':'系统分润','13':'平台奖励','14':'资金解冻','15':'积分兑换','16':'现金红包','17':'其他',
		 '21':'提现申请','22':'消费','23':'资金冻结','24':'投诉罚款','25':'其他',
		 '31':'冻结交易买卖额','32':'买单投诉冻结','33':'提现冻结','34':'其他',
		 '41':'恢复可用余额','42':'提现完成','43':'交易完成','44':'买家退款完成'}
function getChangeFlowType(code){
	var val = flowObj[code];
	if(val){
		return val;
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

