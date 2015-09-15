package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Paojiao_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 泡椒网手机软件下载 www.paojiao.cn/
 * Paojiao #133
 * (1)有网游和单击软件两种详细页面
 * @author DMT
 */
public class Paojiao implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Paojiao.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Paojiao.process()" + page.getUrl());
		//index page				http://www.paojiao.cn/search__%E5%88%80%E5%A1%94%E4%BC%A0%E5%A5%87_1.html
		if(page.getUrl().regex("http://www\\.paojiao\\.cn/search_.*").match()){
			//app的具体介绍页面											http://www.paojiao.cn/
			List<String> url1 = page.getHtml().links("//div[@class='top-list']/ul").regex("http://www\\.paojiao\\.cn/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page']").regex("http://www\\.paojiao\\.cn/search_.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.paojiao\\.cn/ruanjian.*").match()
				|| page.getUrl().regex("http://www\\.paojiao\\.cn/pojie.*").match()
	            || page.getUrl().regex("http://www\\.paojiao\\.cn/danji.*").match()){
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
//			String nameString=page.getHtml().xpath("//h2[@class='detail-title']/text()").toString();			
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//			
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				
//			}
//			else if(nameString != null && nameString.contains("."))
//			{
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				
//			}
//			else 
//			{
//				appName = nameString;
//			}
//				
//			appDetailUrl = page.getUrl().toString();
//			String appdownloadurlString =page.getHtml().xpath("//ul[@class='side_nav']/li[2]/p/a[1]/@href").toString();
//			appDownloadUrl = appdownloadurlString;
//			
//			String versionString = page.getHtml().xpath("//ul[@class='info']/li[3]/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//ul[@class='info']/li[1]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='info']/li[5]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			
//			appType ="apk";
//			
//			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='info']/li[6]/text()").toString();
//				appDownloadedTime = DownloadedTimeString;
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='info-content']/div[2]").toString();
//			String allinfoString = descriptionString;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			appDescription = allinfoString.replace("\n", "");
//			
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
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			logger.info("return from Paojiao.process()");
			return Paojiao_Detail.getApkDetail(page);
		}
		else if(page.getUrl().regex("http://www\\.paojiao\\.cn/wangyou.*").match()
	            || page.getUrl().regex("http://www\\.paojiao\\.cn/youxi.*").match()){
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
//			appName = page.getHtml().xpath("//div[@class='app-right']/h2/span/text()").toString();	
//			
//			appDetailUrl = page.getUrl().toString();
//			
//			String appdownloadurlString =page.getHtml().xpath("//p[@class='az']/a[1]/@ex_url").toString();
//			appDownloadUrl = appdownloadurlString;
//			
//			String versionString = page.getHtml().xpath("//div[@class='stat']/span[3]/i/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='stat']/span[2]/i/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='tc']").toString();
//				
//			String allinfoString = updatedateString;
//				while(allinfoString.contains("<"))
//					if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//					else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//					else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				appUpdateDate = allinfoString.substring(allinfoString.indexOf("更新日期")+4+13,allinfoString.indexOf("状态")-1).replace("\n", "");
//			if(allinfoString.contains("开发商"))	
//				appvender = allinfoString.substring(allinfoString.indexOf("开发商"),allinfoString.length());
//			
//			appType ="apk";
//			
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='stat']/span[1]/i/text()").toString();
//				appDownloadedTime = DownloadedTimeString;
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='app-right']").toString();
//			allinfoString = descriptionString;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			appDescription = allinfoString.replace("\n", "");
//			
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
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Paojiao.process()");
			return Paojiao_Detail.getApkDetail(page);
		}
		logger.info("return from Paojiao.process()");
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
