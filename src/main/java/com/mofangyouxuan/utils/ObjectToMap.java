package com.mofangyouxuan.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 将java 对象转换为Map
 * @author jeekhan
 *
 */
public class ObjectToMap {
	
	/**
	 * 将 POJO对象 属性提取到Map中
	 * @param obj	pojo对象
	 * @param excludeFields	需要排除的字段
	 * @param needNull 是否需要将空值的字段提取
	 * @return
	 */
	public static Map<String,Object> object2Map(Object obj,String[] excludeFields,boolean needNull) {
		Map<String,Object> map = new HashMap<String,Object>();
		Field[] fields = obj.getClass().getDeclaredFields();
		outer:
		for(Field field:fields) {
			try {
				String methodName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				methodName = "get"+ methodName;
				Method method = obj.getClass().getDeclaredMethod(methodName);
				for(String exclu:excludeFields) {
					if(exclu.equals(field.getName())) {
						continue outer;
					}
				}
				Object result = method.invoke(obj);
				if(result != null) {
					map.put(field.getName(), result);
				}else {
					if(needNull) {
						map.put(field.getName(), result);
					}
				}
			}catch(Exception e) {
				
			}
		}
		return map;
	}

}
