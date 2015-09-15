package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Muzhiwan_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 拇指玩 http://www.muzhiwan.com/
 * Muzhiwan #106
 * @author DMT
 */
public class Muzhiwan implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.muzhiwan.com/common/search?q=*#*#*#  http://www.muzhiwan.com/index.php?page=3&action=common&opt=search&q=qq
		if(page.getUrl().regex("http://www\\.muzhiwan\\.com/common/search.*").match() || page.getUrl().regex("http://www\\.muzhiwan\\.com/index.*").match()){
	//		System.out.println(page.getHtml().toString());
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='search_list left']/ul").regex("http://www\\.muzhiwan\\.com/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//p[@class='paging']").regex("http://www\\.muzhiwan\\.com/index.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.muzhiwan\\.com/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='game_name']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='detail_dbtn detail_way_t']/a[1]/@href").toString();
//			
//			String platformString =page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[8]/text()").toString();
//			osPlatform = platformString.substring(platformString.indexOf("：")+1,platformString.length());
//			
//			String versionString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[1]/span/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[2]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[5]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[7]/text()").toString();
//				appType =typeString.substring(typeString.indexOf("：")+1,typeString.length());
//			
//			appvender=page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[10]/a/text()").toString();
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='detail_info']/div[2]/ul/li[4]/span/text()").toString();
//				appDownloadedTime = DownloadedTimeString;
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
			return Muzhiwan_Detail.getApkDetail(page);
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
