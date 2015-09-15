package us.codecraft.webmagic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.downloader.CurlPostPageDownloader;
import us.codecraft.webmagic.downloader.MyHttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 爬虫的启动接口，controller模块接受任务后，调用SpiderTrigger启动爬虫
 * @author buildhappy
 *
 */
public class Test implements Runnable {
	private String channelId;
	private String taskId;
	private String keyword;
	private String remoteIp;
	private Logger logger = LoggerFactory.getLogger(getClass());
	static{
        System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("http.proxyPort", "8001");
        System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("https.proxyPort", "8001");
		//threadPool = new CountableThreadPool(threadNum);
	}
	/**
	 * 
	 * @param taskId
	 * @param channelId
	 * @param keyword
	 */
	public Test(String taskId , String channelId , String keyword ,String remoteAddress){
		logger.info("SpiderTrigger Constructor parameter: taskId,channelId,keyword");
		this.taskId = taskId;
		this.channelId = channelId;
		this.keyword = keyword;
		this.remoteIp = remoteAddress;
	}
	
	public Test(String taskId , UserRequest requestData , String remoteAddress){
		logger.info("SpiderTrigger Constructor parameter: taskId,channelId,keyword");
		this.taskId = taskId;
		this.channelId = requestData.getChannelId();
		this.keyword = requestData.getKeyword();
		this.remoteIp = remoteAddress;
	}
	
	public void run() {
		logger.info("SpiderTrigger run");
		ChannelsDom channelDom = new ChannelsDom();
		channelDom = channelDom.createChannelsDom(channelId);
		
		if(channelDom != null){
			String referer;
			referer = channelDom.getHomeUrl();
			String pageEncoding;
			pageEncoding = channelDom.getPageEncoding();
			//searcher request using the keyword 
			if(keyword != null && keyword.length() != 0 && keyword != ""){
				logger.info("searcher request using the keyword");
				String searchUrl;
				PageProcessor pagePro;
				String postParam;
				pagePro = channelDom.getPagePro();
				
				String s = channelDom.getSearchUrl();
				searchUrl = s.replace("*#*#*#", keyword);
				
				String urlEncoding = null;
				urlEncoding = channelDom.getUrlEncoding();
				
				String encodedKeyword=keyword;
				if(urlEncoding != null){
					try {
						encodedKeyword = URLEncoder.encode(keyword, urlEncoding);
					} catch (UnsupportedEncodingException e) {
						logger.error(e.toString());
						e.printStackTrace();
					}
				}

				searchUrl = s.replace("*#*#*#", encodedKeyword);
				logger.info(searchUrl);
				//searchUrl = searchUrl + keyword;
				
				postParam = channelDom.getPostParam();
				Spider spider = new Spider(pagePro ,taskId ,channelId,remoteIp);
				
				if (postParam != null) {
					Map<String,Object> extras = new HashMap<String , Object>();
					extras.put(postParam, keyword);
					Request request = new Request();
					request.setExtras(extras);
					request.setMethod("post");
					request.setUrl(searchUrl);
					spider.addRequest(request).setDownloader(new CurlPostPageDownloader(postParam, keyword , referer ,pageEncoding,channelId))
							.thread(1).run();
//					spider.setDownloader(new MyHttpClientDownloader())
//					.addRequest(request).run();
				} else {
					spider.setDownloader(new MyHttpClientDownloader())
							.addUrl(searchUrl).thread(1).run();
				}				
			}//the full stack crawler
			else{
				logger.info("full stack crawler");
				String homeUrl = channelDom.getHomeUrl();
				PageProcessor fullPagePro = channelDom.getFullPagePro();
				
				FullStackSpider fullStackSpider = new FullStackSpider(fullPagePro , taskId , channelId , remoteIp);
				fullStackSpider.addUrl(homeUrl).setDownloader(new MyHttpClientDownloader()).addPipeline(new FilePipeline(taskId , channelId)).thread(1).run();
			}
		}else{
			//if can not find the channel by the channelId
			logger.error("SpiderTrigger no channel");
		}
	}
	
	public static void main(String[] args) throws IOException{
		if(args.length < 1){
			return;
		}
		String channelId = args[0];
		//15->91助手  21->百度 66->ApkGfan
		//67->fpwap   69->Ruan8
		Test spiderTrigger = new Test("task1" , channelId , "" , "");//工商银行  捕鱼达人 邮政银行
		Thread thread = new Thread(spiderTrigger);
		thread.run();
		
		//提取sitesInfo.xml中的数据
//		File file = new File("data.txt");
//		FileWriter writer = new FileWriter(file);
//		for(int i = 1; i < 180; i++){
//			ChannelsDom channelDom = new ChannelsDom();
//			channelDom = channelDom.createChannelsDom(i + "");
//			if(channelDom != null){
//				String name;
//				name = channelDom.getName();
//				String referer;
//				referer = channelDom.getHomeUrl();
//				System.out.println(name + ":" + referer);
//				writer.write(referer + "\n");
//				writer.flush();
//			}
//		}
//		writer.close();
	}
}


