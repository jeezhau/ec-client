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