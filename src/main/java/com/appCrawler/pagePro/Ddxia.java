
package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Ddxia_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 豆豆软件下载站 http://www.ddxia.com
 * Ddxia #138
 * 无站内搜索接口，搜索跳到百度搜索
 * @author DMT
 */
public class Ddxia implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Ddxia.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Ddxia.process()" + page.getUrl());
		//index page				http://www.baidu.com/baidu?word=%CE%A2%D0%C5&ct=2097152&si=www.ddxia.com
		if(page.getUrl().regex("http://www\\.baidu\\.com/baidu\\?word=.*si=www.ddxia.com").match()
				|| page.getUrl().regex("http://www\\.baidu\\.com/baidu\\?word=.*si=www.ddxia.com").match()){
			//app的具体介绍页面											http://www.baidu.com/link?url=
			List<String> url1 = page.getHtml().links("//div[@id='content_left']").regex("http://www\\.baidu\\.com/link.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@id='page']").regex("http://www\\.baidu\\.com/s\\?.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.baidu\\.com/link.*").match()){
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
//			osPlatform = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[6]/text()").toString();
//			if(osPlatform == null){
//				logger.info("return from Ddxia.process()");
//				return null;
//			}
//			
//			if(!osPlatform.contains("android") && !osPlatform.contains("Android") ){
//				logger.info("return from Ddxia.process()");
//				return null;
//			}
//			
//			String nameString=page.getHtml().xpath("//div[@class='rjxx_box']/h1/text()").toString();	
//			
//			if(nameString != null && nameString.contains("V")){
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
//			else{
//				appName = nameString;
//				appVersion = null;
//			}
//
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='xzdz_boxl']/ul[1]/li[1]/p/a/@href").toString();
//			
//			String sizeString = page.getHtml().xpath("//div[@class='ljxz']/a/div/text()").toString();
//			appSize = sizeString.substring(sizeString.indexOf("（")+1,sizeString.length()-1);
//			
//			appUpdateDate = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[9]/text()").toString();
//			
//			appType = "apk";
//			
//			String venderString = page.getHtml().xpath("//div[@class='rjxx_boxr']/ul/li[4]/a/@href").toString();
//			appvender = "官方网站"+venderString; 
//				
//			appDownloadedTime = page.getHtml().xpath("//div[@class='xzcs']/a/font/text()").toString();
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='rjjs_box']").toString();
//			String allinfoString = descriptionString;	
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());					
//			appDescription = allinfoString.replace("\n", "");	
//
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
//			
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Ddxia.process()");
			return Ddxia_Detail.getApkDetail(page);
		}
		logger.info("return from Ddxia.process()");
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
