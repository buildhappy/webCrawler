package com.appCrawler.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appCrawler.utils.BaiduWangPan;
import com.appCrawler.utils.JsonUtils;

import us.codecraft.webmagic.SpiderTrigger;
import us.codecraft.webmagic.Test;
import us.codecraft.webmagic.UserRequest;
import us.codecraft.webmagic.selector.thread.CountableThreadPool;

/**
 * 接受外部请求的Controller
 * 
 * @author buildhappy
 *
 */

@Controller
@RequestMapping(value = "/")
public class ControllerCenter{
	
	//private static CountableThreadPool threadPool;
	private static Executor threadPool = Executors.newCachedThreadPool();//.newCachedThreadPool();
	private static int threadNum = 3;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void setThreadNum(int threadNum) {
		ControllerCenter.threadNum = threadNum;
	}

	static{
        System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("http.proxyPort", "8001");
        System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("https.proxyPort", "8001");
		//threadPool = new CountableThreadPool(threadNum);
	}
	@RequestMapping(value="/" , method=GET)
	public String index(){
		System.out.println("in index  ");
		return "index";
	}
	
	@RequestMapping(value="/search/{dataId}" , method=RequestMethod.PUT)
	public String receiveData(@RequestBody String body){
		//BaiduWangPan.start();
		System.out.println(body);
		return "test";
	}
	
	@RequestMapping(value="/sendData",method=RequestMethod.GET)
	public void testSendData(){
		try {
			SendDataTest.sendData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * receive the crawling task
	 * @param taskId
	 * @param body
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/{taskId}" ,method=RequestMethod.POST)
	public String receiveTask(@PathVariable String taskId , @RequestBody String body , HttpServletRequest request){
		logger.info("ControllerCenter receiveTask()");
		System.out.println("taskId:" + taskId);
		System.out.println("body:" + body);
		UserRequest userRequest;
		userRequest = JsonUtils.jsonToUserRequest(body);
		String channelId = userRequest.getChannelId();
		String keyword = userRequest.getKeyword();
		String remoteIp = null;
		
		if(request != null){
			System.out.println("request.getRemoteAddr():" + request.getRemoteAddr());
			remoteIp = request.getRemoteHost() + ":" + "8080";//the port of remote user is 8080
			System.out.println("remoteAddress:" + remoteIp);
		}
		
		if(channelId == null || keyword == null || remoteIp == null){
			try {
				throw new RuntimeException("request pattern error " + taskId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "{\"code\":\"300\", \"msg\":\"failed\"}";
		}else{
			threadPool.execute(new SpiderTrigger(taskId , channelId , keyword , remoteIp));
			return "{\"code\":\"200\", \"msg\":\"received\"}";
		}
		//return "{\"code\":\"100\", \"msg\":\"waiting\"}";
	}
	
	/**
	 * convert the string array to list
	 * @param channels
	 * @return
	 */
	public List<String> getChannelsFromString(String channels){
		return Arrays.asList(channels.split(" "));
	}
	
	public static void main(String[] args){
		ControllerCenter c = new ControllerCenter();

		String request = "{channelId:\"101\" , keyword:\"qq\" , version:\"5.4.1\"}";
		request = "{\"channelId\":\"" + 10 + "\",\"keyword\":\"" + "qq" + "\"," + "\"version\":\"" + "5.4.1" + "\"}";
		UserRequest userRequest = JsonUtils.jsonToUserRequest(request);
		//String keyword = c.getKeywordFromRequest(request);
		System.out.println(userRequest.getChannelId());
		System.out.println(userRequest.getKeyword());
		System.out.println(userRequest.getVersion());
		c.receiveTask("fa" , request, null);
	}
}
