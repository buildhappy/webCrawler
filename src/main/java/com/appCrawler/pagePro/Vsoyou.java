package com.appCrawler.pagePro;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Vsoyou_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 威搜游 http://vsoyou.com/
 * Vsoyou #111
 * 翻页是通过js完成的，这里只使用第一页的搜索结果
 * @author DMT
 */
public class Vsoyou implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		if(page.getUrl().regex("http://vsoyou\\.com/game/s0\\.htm\\?keyword.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@id='gm_list_all']").regex("http://vsoyou\\.com/game.*").all();
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}

		}
		
		//the app detail page
		else {
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
//			String nameString=page.getHtml().xpath("//li[@class='game_li_02']/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//li[@class='game_li_04']/a/@href").toString();
//			
//			String allinfoString = page.getHtml().xpath("//li[@class='game_li_03']").toString();
//			if(allinfoString != null)	
//			{
//			if(allinfoString.contains("作者") && allinfoString.contains("类别"))
//				appvender = allinfoString.substring(allinfoString.indexOf("作者")+3,allinfoString.indexOf("类别"));
//			if(allinfoString.contains("版本号"))
//				appVersion = allinfoString.substring(allinfoString.indexOf("版本号")+4,allinfoString.indexOf("<br"));
//			if(allinfoString.contains("大小") && allinfoString.contains("适用固件"))
//				appSize = allinfoString.substring(allinfoString.indexOf("大小")+3,allinfoString.indexOf("适用固件"));
//			if(allinfoString.contains("适用固件") && allinfoString.contains("上架时间"))
//				osPlatform = allinfoString.substring(allinfoString.indexOf("适用固件")+5,allinfoString.indexOf("上架时间")-8);
//			if(allinfoString.contains("上架时间"))
//				appUpdateDate = allinfoString.substring(allinfoString.indexOf("上架时间")+5,allinfoString.length()-5);	
//			}
//			
//			appDescription = page.getHtml().xpath("//li[@class='game_li_07']").toString();
//			allinfoString = appDescription;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			appDescription = allinfoString;
//			appType = "apk";
							
			/*
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appvender);
			System.out.println("appDownloadedTime="+appDownloadedTime);
			System.out.println("appDescription="+appDescription);
			*/
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Vsoyou_Detail.getApkDetail(page);
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
