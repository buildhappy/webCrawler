package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * APP导航 http://www.appdh.com/
 * Appdh #153
 * @author DMT
 */
public class Appdh implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Appdh.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Appdh.process()" + page.getUrl());
		//http://www.appdh.com/keyword/?s=aa
		if(page.getUrl().regex("http://www\\.appdh\\.com/keyword.*").match()){
			//app的具体介绍页面								http://www.appdh.com/app/sportstap/
			List<String> url1 = page.getHtml().links("//div[@class='clearfix']").regex("http://www\\.appdh\\.com/app/.*").all();
			
			List<String> url2 = page.getHtml().links("//div[@class='pages']").regex("http://www\\.appdh\\.com/keyword.*").all();
						
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.appdh\\.com/app/.*").match()){
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			String appDescription =null;        //app的详细介绍
			osPlatform = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[5]/text()").toString();
			if(osPlatform == null || !osPlatform.contains("Android")){
				logger.info("？？？？return from Appdh.process()");
				return null;
			}
			
			appName=page.getHtml().xpath("//h1[@class='f20 bold']/strong/text()").toString();		
			
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//div[@class='left pr_20 pt_2 pl_20']/a/@href").toString();
			
			
		
			appSize = page.getHtml().xpath("//div[@class='left pr_20 pt_2 pl_20']/a/text()").toString();
			if(appSize!= null && appSize.contains("("))
				appSize = appSize.substring(appSize.indexOf("(")+1,appSize.length()-1);
			
			appUpdateDate = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[3]/text()").toString();
			appVersion = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[2]/text()").toString();
			appvender = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[6]/text()").toString();
			appDownloadedTime = page.getHtml().xpath("//div[@class='grid_7 alpha omega']/p[8]/text()").toString();

			appType = "apk";
			
			String descriptionString = page.getHtml().xpath("//div[@class='grid_12']/div[5]/div[3]").toString();
			
			String allinfoString = descriptionString;
			while(allinfoString!= null && allinfoString.contains("<"))
				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			if(allinfoString != null && allinfoString.contains("\n"))
				appDescription = allinfoString.replace("\n", "");
			else appDescription = allinfoString;
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
			}
			logger.info("return from Appdh.process()");
			return apk;
		}
		logger.info("return from Appdh.process()");
		return null;

	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}

