package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Anfensi_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 安粉丝 http://www.anfensi.com/
 * Anfensi #91
 * (1)搜索结果中有的app detail page的地址不是源地址，会重定向，因此需要匹配多种类型的地址
 * (2)下载次数是动态获取的
 * @author DMT
 */
public class Anfensi implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {	
		//index page				http://zhannei.baidu.com/cse/search?s=10366954480845088817&q=360&imageField.x=67&imageField.y=8
		if(page.getUrl().regex("http://zhannei\\.baidu\\.com/cse/search.*").match()){
			
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@id='results']").regex("http://www\\.anfensi\\.com/[0-9]+/").all();
			
			List<String> url2 = page.getHtml().links("//div[@id='results']").regex("http://www\\.anfensi\\.com/down/.+\\.html").all();
			
			List<String> url3 = page.getHtml().links("//div[@id='results']").regex("http://www\\.anfensi\\.com/Androidgame/\\w+.html").all();
			
			//添加下一页url(翻页)
			List<String> url4 = page.getHtml().links("//div[@class='pager clearfix']").regex("http://zhannei\\.baidu\\.com/cse/search.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			url1.addAll(url4);
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.anfensi\\.com/[0-9]+/").match() 
				|| page.getUrl().regex("http://www\\.anfensi\\.com/down/.+\\.html").match()
				|| page.getUrl().regex("http://www\\.anfensi\\.com/Androidgame/\\w+.html").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='yxxq-left']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='yxxq-left']/p/b/a/@href").toString();
//			
//			for(int i=1;i<12;i++)
//			{
//				String catString = page.getHtml().xpath("//p[@class='info']/b["+i+"]/i/text()").toString();
//				if(catString == null) break;
//				if(catString.contains("版　本"))
//					appVersion = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
//				else if(catString.contains("大　小"))
//					appSize = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
//				else if(catString.contains("平　台"))
//					osPlatform = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
//				else if(catString.contains("开发商"))
//					appvender = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
//				else if(catString.contains("更新时间"))
//					appUpdateDate = page.getHtml().xpath("//p[@class='info']/b["+i+"]/text()").toString();
//			}
//						
//			String typeString = "apk";
//				appType = typeString;
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
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Anfensi_Detail.getApkDetail(page);
		}
		
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
