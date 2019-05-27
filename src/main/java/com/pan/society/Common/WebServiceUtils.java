package com.pan.society.Common;

import com.yonyou.ocm.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 调用SAP WebService 接口的工具类。
 *
 * @author 马亮
 * @version [1.0, 2017年7月13日]
 */
public class WebServiceUtils {
	private static final Logger logger = LoggerFactory.getLogger(WebServiceUtils.class);

	private static final String URL = "http://qypid.quanyou.com.cn:50000/dir/wsdl?p=ic/c7c0f284e0893af2ba02e224bea4de56";

	private static final String METHOD_TYPE = "GET";

	private static final String USER_NAME = "ZPI_XT";

	private static final String PASSWORD = "123456";

	/**
	 * 调用Web Service接口。
	 *
	 * @param interfaceId 接口标识。
	 * @param jsonData    请求的JSON字符串。
	 * @return 返回的JSON字符串。
	 */
	public static String callWebService(String interfaceId, String jsonData) {
		String jsonResult;
		try {
			final String messageId = UUID.randomUUID().toString().replace("-", "");
			byte[] bytes = getRequestBytes(messageId, interfaceId, jsonData);
			String xmlResult = callWebService(bytes);
			System.out.println(xmlResult);
			jsonResult = getJsonDataFromXmlResult(xmlResult);
		}
		catch (UnsupportedEncodingException e) {
			throw new BusinessException("字符编码解析错误", e);
		}
		return jsonResult;
	}

	private static String callWebService(byte[] data) {
		return callWebService(URL, METHOD_TYPE, USER_NAME, PASSWORD, data);
	}

	/**
	 * 调用WebService
	 *
	 * @param url        webservice地址
	 * @param methodType POST /get
	 * @param userName   登录用户
	 * @param password   登录密码
	 * @param inputData  输入字节数组
	 * @return 返回字符串。
	 */
	private static String callWebService(String url, String methodType, String userName, String password,
			byte[] inputData) {
		HttpURLConnection conn;
		OutputStream os = null;
		InputStream is = null;
		try {
			conn = getConn(url, methodType, userName, password);
			os = conn.getOutputStream();
			os.write(inputData);
			is = conn.getInputStream();
//			String msgid = conn.getHeaderField("content-id");
			return getResult(is);
		}
		catch (Exception e) {
			logger.error("调用SAP服务时出现错误", e);
		}
		finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			}
			catch (IOException e) {
				logger.error("调用SAP服务时出现错误", e);
			}
		}
		return null;
	}

	/**
	 * 从XML格式的返回内容中获取JSON数据。
	 *
	 * @param content XML格式的返回内容。
	 * @return JSON数据。
	 */
	private static String getJsonDataFromXmlResult(String content) {
		final String startStr = "<JSONDATA>";
		final String endStr = "</JSONDATA>";
		int start = content.indexOf(startStr) + startStr.length();
		int end = content.indexOf(endStr);
		if (start <= end) {
			return content.substring(start, end);
		}
		return "";
	}

	/**
	 * 获取WebService的HTTP连接。
	 *
	 * @param url        访问地址。
	 * @param methodType 请求类型。
	 * @param userName   用户名。
	 * @param password   密码。
	 * @return HTTP连接。
	 * @throws Exception
	 */
	private static HttpURLConnection getConn(String url, String methodType, String userName, String password)
			throws Exception {
		// 服务的地址
		URL wsUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();

		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(methodType);
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection","Keep_Alive");
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		conn.setRequestProperty("Authorization", "Basic " +
				new String(org.apache.commons.codec.binary.Base64.encodeBase64((userName + ":" + password).getBytes())));
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		// 根据ResponseCode判断连接是否成功  
        int responseCode = conn.getResponseCode();  
        if (responseCode != 200) {  
        	System.out.println("Not Success");
        } else {  
        	System.out.println("Success");
        }  
		return conn;
	}

	/**
	 * 获取请求字节数组。
	 *
	 * @param messageId   消息标识。
	 * @param interfaceId 接口标识。
	 * @param jsonInput   输入的JSON字符串。
	 * @return 请求字节数组。
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getRequestBytes(String messageId, String interfaceId, String jsonInput)
			throws UnsupportedEncodingException {
		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
				"xmlns:urn=\"urn:xt:common\">" + "<soapenv:Header/>" + "<soapenv:Body>" +
				"<urn:MT_XT_COMMON_DATA_REQ>" + "<MESSAGEID>" + messageId + "</MESSAGEID>" + "<INTERFACEID>" +
				interfaceId + "</INTERFACEID>" + "<JSONDATA>" + jsonInput + "</JSONDATA>" +
				"</urn:MT_XT_COMMON_DATA_REQ>" + "</soapenv:Body>" + "</soapenv:Envelope>";
		System.out.println(body);
		return body.getBytes("UTF-8");
	}

	/**
	 * 从输入流中获取字符串。
	 *
	 * @param is 输入流。
	 * @return 输入流中的字符串。
	 * @throws Exception
	 */
	private static String getResult(InputStream is) throws Exception {
		StringBuilder sb = new StringBuilder();
		String temp;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((temp = reader.readLine()) != null) {
			sb.append(new String(temp.getBytes("UTF-8"), "UTF-8") + "\n");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
}
