package com.appCrawler.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * generate test data to file for downloader model
 * just for test
 * @author buildhappy
 *
 */
public class ToFileSendData implements SendData{
	/**
	 * the path to store the data file
	 */
	private String filePath;
	private File file;
	private FileWriter writer;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ToFileSendData(){
		logger.info("ToFileSendData constructor");
		//filePath = "D://";
		filePath = "/home/crawler/testData/";
		file = new File(filePath + "testData.txt");
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ToFileSendData(String channelId){
		logger.info("ToFileSendData constructor");
		//filePath = "D://";
		filePath = "/home/crawler/testData/";
		file = new File(filePath +  channelId + ".txt");
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * write data to file
	 * @param data
	 * @throws  
	 */
	public void sendData(String address , String data){
		logger.info("ToFileSendData sendData(String address , String data)");
		try {
			writer.write(data + "\n");
			System.out.println(data);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * release the I/O source
	 */
	public void printDone(){
		logger.info("ToFileSendData printDone()");
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		ToFileSendData send = new ToFileSendData();
		send.sendData("fads" , "abcdefg");
		send.printDone();
	}

	@Override
	public void sendData(String address, String taskId, String data) {
		// TODO Auto-generated method stub
		
	}
}
