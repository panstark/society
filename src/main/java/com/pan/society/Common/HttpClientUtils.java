package com.pan.society.Common;

import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpClientUtils {

	
	
	public void execureHttpGetRequest(String url,String method,String data){
		
		HttpClientExecutor executor = new HttpClientExecutor(url, "POST");
        executor.maxConnectionSeconds(30);
        executor.putRequestHeader("Content-Type", "text/xml;charset=UTF-8");
        executor.putRequestParam("id", "test-id");
        executor.putRequestParam("type", "test-type");

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = executor.execute(responseHandler);

        System.out.println(result);
	}
	
	public static String execureHttpPostRequest(String url,String method,String data){
		
		HttpClientExecutor executor = new HttpClientExecutor(getUrl(url).toString(), "post");
        executor.maxConnectionSeconds(30);
        executor.putRequestHeader("Content-Type", "text/xml;charset=UTF-8");
        executor.putRequestParam("method", method);
        executor.putRequestParam("data", data);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String result = executor.execute(responseHandler);

        System.out.println(result);
        
        return result;
	}
	
	private static URI getUrl(String str){
		
		URL url;
		URI uri;
		try {
			url = new URL(str);
			uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			return uri;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (URISyntaxException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
}
