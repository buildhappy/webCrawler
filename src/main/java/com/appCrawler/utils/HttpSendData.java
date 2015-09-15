package com.appCrawler.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * send crawler data to the user(in put model)
 */

public class HttpSendData implements SendData{

	private static Logger logger = LoggerFactory.getLogger(HttpSendData.class);
	@Override
	public void sendData(String address , String taskId , String data) {
		logger.info("call SendInfo.sendInfo()");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String remoteAddress = "http://" + address + ":8080/subtask/search/" + taskId;
		//String remoteAddress = "http://10.108.113.195:8080/TestCrawlerData/1001/";
		
		/**/
	    HttpPut httpPut = new HttpPut(remoteAddress);
	    //httpPut.setHeader(header);
	    StringEntity myEntity = new StringEntity(data , ContentType.create("application/json" , "UTF-8"));
	    httpPut.setEntity(myEntity);
	    logger.info(data);
	    try {
	    	httpPut.setEntity(myEntity); 
			CloseableHttpResponse response = httpClient.execute(httpPut);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				logger.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
			}

		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		} 
	    finally {  
            //release the resources 
            try {  
                httpClient.close();  
            } catch (Exception e) {  
            	logger.error(e.toString());
                e.printStackTrace();  
            }  	
	    }
	    
		
		
	   logger.info("return from SendInfo.sendInfo()");
	}
	public static void main(String[] args)	{
		HttpSendData sendInfo=new HttpSendData();
		sendInfo.sendData("10.108.113.195:8080", "12122" , "test");
	}
}
