package com.appCrawler.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * read apk infos from the taskId_channelId.txt file for full stack crawler
 * @author buildhappy
 *
 */
public class FullStackFileReader {
	private final String fileName;
	private long apkCounter;
	
	private boolean hasMore = true;

	/**
	 * the number of getting apks from file each time
	 */
	private final int readMod = 50;
	private FileReader reader;
	BufferedReader readerBuffer;
	
	public FullStackFileReader(String fileName){
		this.fileName = fileName;
		init();
	}
	
	private void init(){
		try {
			reader = new FileReader(fileName);
			readerBuffer = new BufferedReader(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * read apk infos from file
	 * @return
	 * @throws IOException
	 */
	public String getAppsInfoFromFile() throws IOException{
		int counter = 0;
		StringBuilder strBuilder = new StringBuilder();
		String line = null;
		strBuilder.append("[");
		while((line = readerBuffer.readLine()) != null && counter <= readMod){
			if(counter < readMod && counter > 0){
				strBuilder.append(line + ",");
			}
			counter++;
			apkCounter++;
		}

		strBuilder.append("]");
		String data = strBuilder.toString();
		data = data.replace(",]", "]");
		if(line == null){
			hasMore = false;
			releaseSource();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private void releaseSource(){
		try {
			readerBuffer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	
	public long getApkCounter() {
		return apkCounter;
	}

	public void setApkCounter(long apkCounter) {
		this.apkCounter = apkCounter;
	}
}
