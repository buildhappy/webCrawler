package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Www2265_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 2265安卓游戏 http://www.2265.com/
 * Www2265 #109
 * (1)没有发现有翻页的搜索结果
 * (2)搜索结果中有重复的url，而且有重定向	http://www.2265.com/sea_%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA.html 里面这个搜索结果  /game_4/62/android_239.html
 * @author DMT
 */
public class Www2265 implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page			http://www.2265.com/sea_*#*#*#.html	
		if(page.getUrl().regex("http://www\\.2265\\.com/sea_.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='listbox10']/div[2]/ul").regex("http://www\\.2265\\.com/.*").all();
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.2265\\.com/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='download']/center/a/@href").toString();
//			
//			String platFormString =page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/div[1]/div[2]/p[3]/text()").toString();
//				osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
//			
//			String versionString = page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/h1/span/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/div[1]/div[1]/p[2]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/div[1]/div[1]/p[3]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = "apk";
//				appType = typeString;
//				
//			String venderString = null;
//				appvender = venderString;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='ViewBox']/div[2]/div[1]/div[1]/div[1]/p[1]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
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
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			
			return Www2265_Detail.getApkDetail(page);
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
