package com.nis.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * 
 * <p>Title:BeanHelper</p>
 * <p>Description:去除Bean中String类型字段的前后空格</p>
 * 
 * @author:RKG
 * @Date:2016-4-29
 */
public class BeanHelper {

	/**
	 * 去掉bean中所有属性为字符串的前后空格
	 * 
	 * @param bean
	 * @throws Exception
	 */
	public static void beanAttributeValueTrim(Object bean) throws Exception {
		if (bean != null) {
			// 获取所有的字段包括public,private,protected,private
			Field[] fields = bean.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				if (f.getType().getName().equals("java.lang.String")) {
					String key = f.getName();// 获取字段名
					Object value = getFieldValue(bean, key);

					if (value == null)
						continue;
					setFieldValue(bean, key, value.toString().trim());
				}
			}
		}
	}

	/**
	 * 利用反射通过get方法获取bean中字段fieldName的值
	 * 
	 * @param bean
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	private static Object getFieldValue(Object bean, String fieldName)
			throws Exception {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("get")
				.append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();

		Object rObject = null;
		Method method = null;

		@SuppressWarnings("rawtypes")
		Class[] classArr = new Class[0];
		method = bean.getClass().getMethod(methodName, classArr);
		rObject = method.invoke(bean, new Object[0]);

		return rObject;
	}

	/**
	 * 利用发射调用bean.set方法将value设置到字段
	 * 
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	private static void setFieldValue(Object bean, String fieldName,
			Object value) throws Exception {
		StringBuffer result = new StringBuffer();
		String methodName = result.append("set")
				.append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1)).toString();

		/**
		 * 利用发射调用bean.set方法将value设置到字段
		 */
		Class[] classArr = new Class[1];
		classArr[0] = "java.lang.String".getClass();
		Method method = bean.getClass().getMethod(methodName, classArr);
		method.invoke(bean, value);
	}
	/**
	 * 
	 * transportBean2Map(将Java bean转换为Map)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param clazz
	 * @param obj
	 * @return
	 * @throws IntrospectionException 
	 *Map<String,String>
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @exception 
	 * @since  1.0.0
	 */
	public static Map<String,String> transportBean2Map(Class clazz,Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Map<String,String> bean=new LinkedHashMap<>();
		BeanInfo beanInfo=Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
			String key =propertyDescriptor.getName();
			if(!key.equals("class")){
				String _value=null;
				Method getter=propertyDescriptor.getReadMethod();
				Object value =getter.invoke(obj);
				if(value==null) continue;
				else if(value instanceof java.util.Date)//时间类型转换成long
				   _value=String.valueOf(((java.util.Date)value).getTime());
				else if(value instanceof Boolean||value instanceof Integer||value instanceof Long){
					_value=String.valueOf(value);
				}else if(value instanceof String){
					_value=(String)value;
				}else if(value instanceof Collection){
					throw new RuntimeException("不支持对象中嵌套的对象集合");
				}else{
					
				}
				if(_value!=null)
					bean.put(key, _value);
			}
			
		}
		return bean;
	}
	public static void main(String[] args) {
//		SrcIp ip=new SrcIp();
//		ip.setAreaId(1l);
//		 try {
//			Map<String,String> transportBean2Map=transportBean2Map(SrcIp.class,ip);
//			System.out.println(transportBean2Map.size());
//			for(Entry<String, String> e:transportBean2Map.entrySet()){
//				System.out.println(e.getKey()+" "+e.getValue());
//			}
//		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
