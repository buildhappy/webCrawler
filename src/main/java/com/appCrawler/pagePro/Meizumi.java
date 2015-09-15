package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Meizumi_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 魅族迷 http://www.meizumi.com/
 * Meizumi #88
 * @author DMT
 */
public class Meizumi implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.meizumi.com/search?q=%E5%AE%89%E5%85%A8%E7%AE%A1%E5%AE%B6		
		if(page.getUrl().regex("http://www\\.meizumi\\.com/search\\?*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='box_content']").regex("http://www\\.meizumi\\.com/apk/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pagebox']").regex("http://www\\.meizumi\\.com/search\\?*").all();
			
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
		if(page.getUrl().regex("http://www\\.meizumi\\.com/apk/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@id='app_info']/table/tbody/tr[1]/td[2]/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='appDownload']/div[1]/p[1]/span/a/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//div[@class='app_info']/p[4]/text()").toString();
//			
//			String versionString = page.getHtml().xpath("//div[@class='app_info']/p[3]/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='app_info']/p[7]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='app_info']/p[8]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			String venderString=page.getHtml().xpath("//div[@class='info_row3']/span[2]/text()").toString();
//				appvender=venderString.substring(venderString.indexOf("：")+1,venderString.length());
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='info_row3']/span[1]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("约")+1,DownloadedTimeString.indexOf("次"));		
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
			return Meizumi_Detail.getApkDetail(page);
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
