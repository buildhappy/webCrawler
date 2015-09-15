package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Www5577_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 我机网 http://www.5577.com
 * www5577 #97
 * (1)the app detail page有http://www.5577.com/s/[0-9]* 
 * 						  http://www.5577.com/youxi/[0-9]* 
 * 						  http://www.5577.com/azpj/[0-9]*
 * 	三种 ，其中后两种的页面布局相同
 * @author DMT
 */
public class Www5577 implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.5577.com/search.asp?keyword=%B2%B6%D3%E3%B4%EF%C8%CB&searchType=soft
		//System.out.println(page.getHtml().toString());
		if(page.getUrl().regex("http://www\\.5577\\.com/search\\.asp\\?.*").match()){
			//app的具体介绍页面											
			List<String> urls = page.getHtml().links("//dl[@class='list_box']").regex("http://www\\.5577\\.com/s/[0-9]*\\.html").all();
			
			List<String> urlyouxi = page.getHtml().links("//dl[@class='list_box']").regex("http://www\\.5577\\.com/youxi/[0-9]*\\.html").all();
			
			List<String> urlazpj = page.getHtml().links("//dl[@class='list_box']").regex("http://www\\.5577\\.com/azpj/[0-9]*\\.html").all();
			
			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='tspage']").regex("http://www\\.5577\\.com/search\\.asp\\?.*").all();
			
			urlyouxi.addAll(urls);
			urlyouxi.addAll(urlazpj);
			urlyouxi.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(urlyouxi);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.5577\\.com/s/[0-9]*\\.html").match()
				|| page.getUrl().regex("http://www\\.5577\\.com/youxi/[0-9]*\\.html").match() 
				|| page.getUrl().regex("http://www\\.5577\\.com/azpj/[0-9]*\\.html").match()){
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
//			
//			String nameString=page.getHtml().xpath("//div[@class='info_body']/h1/text()").toString();
//	//		System.out.println("nameString="+nameString);
//			if(nameString != null && nameString.contains("V")){
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//				
//			}
//			else if(nameString != null && nameString.contains("v")){
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//
//			}
//			else if(nameString != null && nameString.contains(".")){
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//			}
//			else{
//				appName = nameString;
//				appVersion = null;
//			}
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='app_down']/a[1]/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//ul[@class='info']/li[5]/b/text()").toString();
//					
//			String sizeString = page.getHtml().xpath("//ul[@class='info']/li[4]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='info']/li[2]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = null;
//				appDownloadedTime = DownloadedTimeString;		
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
//			*/
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Www5577_Detail.getApkDetail(page);
		}
		
//		if(page.getUrl().regex("http://www\\.5577\\.com/youxi/[0-9]*\\.html").match() || page.getUrl().regex("http://www\\.5577\\.com/azpj/[0-9]*\\.html").match()){
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
//			
//			String nameString=page.getHtml().xpath("//dl[@class='g_msg']/dt[1]/h1/text()").toString();
//	//		System.out.println("nameString="+nameString);
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//				
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//
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
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//dl[@class='g_msg']/dd[4]/a/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//dl[@class='g_msg']/dd[3]/span/text()").toString();
//					
//			String sizeString = page.getHtml().xpath("//dl[@class='g_msg']/dd[2]/strong/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//dl[@class='g_msg']/dd[3]/text()").toString();
//			if(updatedateString.contains("更新时间"))
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("更新时间")+5,updatedateString.length());
//			else appUpdateDate = null;
//			
//			String typeString = "apk";
//				appType =typeString;
//			String venderString = page.getHtml().xpath("//dl[@class='g_msg']/dd[2]/text()").toString();
//			if(venderString.contains("厂商"))
//				appvender = venderString.substring(venderString.indexOf("厂商")+3,venderString.length());
//			else appvender = null;
//			
//			String DownloadedTimeString = null;
//				appDownloadedTime = DownloadedTimeString;		
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
//			
//			return apk;
//		}
		
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
