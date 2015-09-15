package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Oyksoft_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Oyksoft www.oyksoft.com
 * Oyksoft #128
 * osplatform 字段和实际下载到的平台有时不匹配
 * @author DMT
 */
public class Oyksoft implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Oyksoft.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Oyksoft.process()" + page.getUrl());
		//index page		http://www.oyksoft.com/search.asp?action=s&sType=ResName&keyword=%E6%89%8B%E6%9C%BA%E7%AE%A1%E5%AE%B6		
		if(page.getUrl().regex("http://www\\.oyksoft\\.com/search\\.asp\\?.*").match()){
			//app的具体介绍页面											http://www.oyksoft.com/soft/16727.html
			List<String> url1 = page.getHtml().links("//div[@class='softlist']").regex("http://www\\.oyksoft\\.com/soft/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='showpage']").regex("http://www\\.oyksoft\\.com/search\\.asp\\?.*").all();
			
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
		if(page.getUrl().regex("http://www\\.oyksoft\\.com/soft/.*").match()){
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
//			
//			
//			String platFormString =page.getHtml().xpath("//div[@id='softinfo']/ul/li[11]/text()").toString();
//			osPlatform = platFormString;
//			
//			String nameString=page.getHtml().xpath("//div[@id='softtitle']/h1/text()").toString();			
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("."))
//			{
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//			}
//			else 
//			{
//				appName = nameString;
//				appVersion = null;
//			}
//			
//			
//			if(!osPlatform.contains("Android") && !osPlatform.contains("android"))
//			{
//				logger.info("return from Bkill.process()");
//				return null;
//				
//			}
//			else if(!nameString.contains("Android") && !nameString.contains("android"))
//			{
//				logger.info("return from Bkill.process()");
//				return null;
//				
//			}
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='down_oyksoft']/a[2]/@href").toString();
//			
//			
//			
//			String sizeString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[1]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[8]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			String DownloadedTimeString = page.getHtml().xpath("//div[@id='softinfo']/ul/li[2]/text()").toString();
//				appDownloadedTime = DownloadedTimeString;
//			
//			String descriptionString = page.getHtml().xpath("//div[@id='softcontent']").toString();
//				
//				while(descriptionString.contains("<"))
//					if(descriptionString.indexOf("<") == 0) descriptionString = descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
//					else if(descriptionString.contains("<!--")) descriptionString = descriptionString.substring(0,descriptionString.indexOf("<!--")) + descriptionString.substring(descriptionString.indexOf("-->")+3,descriptionString.length());
//					else descriptionString = descriptionString.substring(0,descriptionString.indexOf("<")) + descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
//			appDescription = descriptionString.replace("\n", "").replace(" ", "");		
//			
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
			logger.info("return from Oyksoft.process()");
			return Oyksoft_Detail.getApkDetail(page);
		}
		logger.info("return from Oyksoft.process()");
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
