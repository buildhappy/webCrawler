package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Mi_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 小米商店 http://app.mi.com/
 * Mi  #85
 * 1.搜索结果显示需点击显示全部方能全部显示,这样做的话给改变url，因此直接使用显示全部的搜索结果的url
 * 2.app detail page页面内没有下载次数，但是有评分次数，把它当做下载次数
 * @author DMT
 */
public class Mi implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page		http://app.mi.com/searchAll?keywords=%E5%A4%A9%E5%A4%A9&typeall=phone			
		if(page.getUrl().regex("http://app\\.mi\\.com/searchAll\\?keywords.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='applist-wrap']").regex("http://app\\.mi\\.com/detail.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pages']").regex("http://app\\.mi\\.com/searchAll\\?keywords.*").all();
			
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
		if(page.getUrl().regex("http://app\\.mi\\.com/detail.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='intro-titles']/h3/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='app-info-down']/a/@href").toString();
//			
//			osPlatform = null;
//			
//			String versionString = page.getHtml().xpath("//div[@class='details preventDefault']/ul/li[4]/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='details preventDefault']/ul/li[2]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("/div[@class='details preventDefault']/ul/li[6]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=page.getHtml().xpath("//div[@class='intro-titles']/p/text()").toString();			;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//span[@class='app-intro-comment']/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("(")+2,DownloadedTimeString.indexOf(")")-4);		
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
			
			return Mi_Detail.getApkDetail(page);
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
