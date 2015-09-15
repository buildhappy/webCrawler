package com.appCrawler.utils;

/**
 * Usage:send data to the appointed address(eg.local file system or remote server)
 * @author buildhappy
 *
 */
public interface SendData {
	/**
	 * send data to the appointed address(eg.local file system or remote server)
	 * @param address
	 * @param data
	 */
	public void sendData(String address , String taskId , String data);
}
