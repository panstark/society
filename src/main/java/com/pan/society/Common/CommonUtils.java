package com.pan.society.Common;

import com.yonyou.iuap.context.InvocationInfoProxy;
import com.yonyou.ocm.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用工具类
 *
 * @author wangruiv
 * @date 2017-06-27 13:02:42
 */
public class CommonUtils {
	
	private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	/**
	 * 获取当前的登录用户名。
	 *
	 * @return 当前的登录用户名。
	 */
	public static String getCurrentUserName() {
		// 从线程对象中获取当前用户
		final String userLoginName = InvocationInfoProxy.getParameter("_A_P_userLoginName");
		return userLoginName;
	}
	
	/**
	 * 获取当前的用户名。
	 *
	 * @return 当前的用户名。
	 * @throws UnsupportedEncodingException 
	 */
	public static String getUserName() throws UnsupportedEncodingException {
		String userName = InvocationInfoProxy.getParameter("_A_P_userName");
		//汉字需要转两回
        try {
            userName = URLDecoder.decode(userName, "UTF-8");
            userName = URLDecoder.decode(userName, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userName;
	}
	
	/**
	 * 获取当前的用户ID。
	 *
	 * @return 当前的用户ID。
	 */
	public static String getCurrentUserId() {
		// 从线程对象中获取当前用户
		final String userId = InvocationInfoProxy.getParameter("_A_P_userId");
		InvocationInfoProxy.setUserid(userId);
		return userId;
	}
	
	/**
	 * 获取当前实体的表名。
	 *
	 * @return 当前的用户ID。
	 */
	public static String getEntityTable(Class<?> cls) {
		Table annotation = (Table)cls.getAnnotation(Table.class);
		if(annotation!= null){
			return annotation.name();
		}
		return null;
	}
	
	/**
	 * 将当前实体转换成Map（如果是参照属性，则将参照属性的id值赋予这个属性）
	 * @param <T>
	 *
	 * @return 实体。
	 */
	public static Map<String, Object> getEntityMap(Object entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fs = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			Object val = "";
			
			try {
				val = f.get(entity);
			} catch (IllegalArgumentException e) {
				logger.error("实体转map时的实体参数不合法", e);
				throw new BusinessException("实体转map时的实体参数不合法");
			} catch (IllegalAccessException e) {
				logger.error("实体转map反射时调用了private方法所导致的，安全权限异常", e);
			}
			
			if(val==""||val==null){
				continue;
			}
			String type = f.getType().toString();// 得到此属性的类型
			if (type.endsWith("String")) {
				map.put(f.getName(), val); // 给属性设值
			} else if (type.endsWith("int") || type.endsWith("Integer")) {
				map.put(f.getName(), val); // 给属性设值
			} else if (type.endsWith("Boolean") || type.endsWith("boolean")) {
				continue;
			} else if (type.endsWith("Map") || type.endsWith("List")|| type.endsWith("Set")) {
				continue;
			} else {
				
				Field[] fields = f.getType().getSuperclass().getDeclaredFields();
				Object obj = "";
				for(Field field: fields){
					boolean fieldHasAnno = field.isAnnotationPresent(Id.class);
					if(fieldHasAnno){
						char[] cs = field.getName().toCharArray();
				        cs[0]-=32;
				        String name = String.valueOf(cs);
						Method m = null;
						try {
							m = f.getType().getSuperclass().getDeclaredMethod("get"+name);
						} catch (NoSuchMethodException e) {
							logger.error("实体转map时，找不到参照字段的主键字段get方法", e);
						} catch (SecurityException e) {
							logger.error("实体转map找参照字段的主键字段get方法时，安全异常", e);
						}
						try {
							obj = m.invoke(val);
						} catch (IllegalAccessException e) {
							logger.error("实体转map反射时调用了private方法所导致的，安全权限异常", e);
						} catch (IllegalArgumentException e) {
							logger.error("实体转map时，调用get方法参数不合法", e);
						} catch (InvocationTargetException e) {
							logger.error("InvocationTargetException", e);
						}
						break;
					}	
				}
				if(obj==null||obj==""){
					continue;
				}
				map.put(f.getName(), obj);
				
			}

		}
		return map;
	}
	
	/**
	 * 合并数组
	 * @param T[] 
	 *
	 * @return T。
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concatAll(T[] first, T[]... rest) {  
		  int totalLength = first.length;  
		  for (T[] array : rest) {  
		    totalLength += array.length;  
		  }  
		  T[] result = Arrays.copyOf(first, totalLength);  
		  int offset = first.length;  
		  for (T[] array : rest) {  
		    System.arraycopy(array, 0, result, offset, array.length);  
		    offset += array.length;  
		  }  
		  return result;  
	}

	/**
	 * 获取SimpleDateFormat
	 *
	 * @param parttern
	 *            日期格式
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException
	 *             异常：非法日期格式
	 */
	public static SimpleDateFormat getDateFormat(String parttern)
			throws RuntimeException {
		return new SimpleDateFormat(parttern);
	}


	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1){
				return ip.substring(0,index);
			}else{
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
		String remote = request.getRemoteAddr();
		logger.info("===remote===>{}", remote);
		return remote;
	}


	/**
	 * 反射调用 set 方法
	 * @param o
	 * @param fieldName
	 * @param value
	 */
	private static final String INVOKE_SET = "set";
	public static void invokeSet(Object o, String fieldName, Object value) {
		Method method = getMethod(INVOKE_SET, o.getClass(), fieldName);
		try {
			method.invoke(o, new Object[] { value });
		} catch (Exception e) {
			logger.error("反射 set 方法错误", e);
			throw new BusinessException("反射 set 方法错误", e);
		}
	}

	/**
	 * 反射调用 get 方法
	 * @param o
	 * @param fieldName
	 * @return
	 */
	private static final String INVOKE_GET = "get";
	public static Object invokeGet(Object o, String fieldName) {
		Method method = getMethod(INVOKE_GET, o.getClass(), fieldName);
		try {
			return method.invoke(o, new Object[0]);
		} catch (Exception e) {
			logger.error("反射  get 方法错误", e);
			throw new BusinessException("反射 get 方法错误", e);
		}
	}
	public static Method getMethod(String type, Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append(type);
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			logger.error("反射调用方法错误", e);
			throw new BusinessException("反射调用方法错误", e);
		}
	}
}
