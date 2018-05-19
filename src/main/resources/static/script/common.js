/**
 * 判断字符串是否以指定字串开头
 * @param source
 * @param target
 * @returns
 */
function startWith(source,target){
	if(!source || !target){
		return -1;
	}
	if(source.indexOf(target)>=0){
		return 1;
	}else{
		return -1;
	}
}

/**
 * 获取订单状态
 * @param code
 * @returns
 */
function getOrderStatus(code){
	if(code == '10'){
		return '待付款';
	}else if(code == '12'){
		return '付款失败';
	}else if(code == '20'){
		return '待发货';
	}else if(code == '21'){
		return '备货中';
	}else if(code == '30'){
		return '待签收';
	}else if(code == '40'){
		return '待评价';
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
 * 资金变动类型(1-可用余额来源，2-可用余额去向，3-冻结金额来源，4-解冻金额去向)':
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
	}else if('2' == tp){
		return '微信支付';
	}
}
