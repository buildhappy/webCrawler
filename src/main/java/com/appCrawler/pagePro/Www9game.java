package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Www9game_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 九游  http://www.9game.cn/
 * Www9game #174
 * @author DMT 
 */
public class Www9game implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Www9game.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Www9game.process()" + page.getUrl());
		//index page				http://www.9game.cn/search/?keyword=qq&platformId=2
		if(page.getUrl().regex("http://www\\.9game\\.cn/search/\\?keyword=.*").match()){
			//app的具体介绍页面			http://www.9game.cn/chuiqiqiu/							
			List<String> url1 = page.getHtml().links("//div[@class='sr-other']").regex("http://www\\.9game\\.cn/.*/").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page-change']").regex("http://www\\.9game\\.cn/search/\\?keyword=.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.9game\\.cn/.*/").match()){
//			Apk apk = null;
//			String appName = null;				//app名字
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			String appDescription =null;        //app的详细介绍
//		
//			appName =page.getHtml().xpath("//h1[@class='h1-title']/a/text()").toString();			
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='btn-con']/a[1]/@href").toString();
//			
//			appType ="apk";
//							
//			appDescription = page.getHtml().xpath("//div[@class='right-text']/p/text()").toString();
//			/*
//			System.out.println("appName="+appName);
//			System.out.println("appDetailUrl="+appDetailUrl);
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appVersion="+appVersion);
//			System.out.println("appSize="+appSize);
//			System.out.println("appUpdateDate="+appUpdateDate);
//			System.out.println("appType="+appType);
//			System.out.println("appvender="+appvender);
//			System.out.println("appDownloadedTime="+appDownloadedTime);
//			System.out.println("appDescription="+appDescription);
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Www9game.process()");
			return Www9game_Detail.getApkDetail(page);
		}
		logger.info("return from Www9game.process()");
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
