package com.appCrawler.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.utils.FullStackFileReader;
import com.appCrawler.utils.HttpSendData;
import com.appCrawler.utils.JsonUtils;
import com.appCrawler.utils.PropertiesUtil;

public class SendDataTest {
	protected static Logger logger = LoggerFactory.getLogger(SendDataTest.class);
	
	public static void main(String[] args) throws IOException {
		sendData();
	}

    public static void sendData() throws IOException{
    	logger.info("FullStackSpider sendData()");
    	String header = "";
    	String taskId = "task1";
    	String channelId = "168";
    	String path = PropertiesUtil.getCrawlerDataPath();;
    	FullStackFileReader fileReader = new FullStackFileReader(path + taskId + "_" + channelId + ".txt");
    	HttpSendData httpSendData = new HttpSendData();
    	while(fileReader.isHasMore()){
        	String appsInfo = fileReader.getAppsInfoFromFile();
        	String agentId = null;
        	String toSendData = JsonUtils.getSenderData(agentId , channelId , header , "4" , appsInfo);
        	
        	logger.info("Spider sendData():" + toSendData);
        	httpSendData.sendData("10.108.113.239:9090" , taskId , toSendData);
    	}
    	httpSendData.sendData("10.108.113.239:9090" , taskId , "sendData end");
    	logger.info(fileReader.getApkCounter() + "");

    }
}
