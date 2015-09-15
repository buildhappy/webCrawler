package com.appCrawler.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 向百度网盘的应用发送服务请求
 *
 */
public class BaiduWangPan {
	private static final String remoteAddress = "http://10.108.122.150:3051/"; 
	/**
	 * 发生资源链接，返回下载链接
	 * @param url 资源链接,如http://pan.baidu.com/s/1bnm5ErH
	 * @return 资源的下载链接
	 * @throws IOException 
	 * @throws Exception 
	 */
    public static String getDLinke(String url) throws IOException{
    	//url = "http://pan.baidu.com/s/1bnm5ErH";
    	String downloadUrl = null;
    	CloseableHttpClient httpClient = HttpClients.createDefault();;
    	HttpGet httpGet = new HttpGet(remoteAddress + "?url=" + url);
    	CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
	    	if(entity != null){
	    		//System.out.println(EntityUtils.toString(entity, "utf-8"));
	    		downloadUrl = EntityUtils.toString(entity, "utf-8").toString();
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(response != null){
				response.close();
			}
		}
    	return downloadUrl;
    }
    public static void main(String[] args) throws Exception{
    	System.out.println(getDLinke("http://pan.baidu.com/s/1bnm5ErH"));
    }
}
