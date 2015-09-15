package com.appCrawler.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * send the data to the specified url
 * @author buildhappy
 *
 */
public class JavaSocketSendData implements SendData{
	private static Logger logger = LoggerFactory.getLogger(JavaSocketSendData.class);
	public void sendData(String urlStr ,String taskId, String data){
		HttpURLConnection connection = null;
		OutputStream out = null;
		logger.error("call SendJsonData.sendJsonData()");
		try{
			//create the connection 
			URL url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			
			//set the connection properties
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			
			//set the http header properties
	        connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept","application/json");
            connection.connect();
            
            //send the data
            out = connection.getOutputStream();
            out.write(data.getBytes());
            
		}catch(Exception e){
			logger.error("send json data failed." , e);
		}finally{
			try{
				if(out != null){
					out.close();
				}
				if(connection != null){
					connection.disconnect();
				}
			}catch(Exception e){
				logger.error("SendJsonData release resources failed. " , e);
			}
		}
		
	}
}
