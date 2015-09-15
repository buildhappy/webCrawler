package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Www51vapp_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 安卓商店  http://www.51vapp.com/
 * Www51vapp #121
 * @author DMT
 */
public class Www51vapp implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page					http://www.51vapp.com/market/apps/search.vhtml?ct=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA
		if(page.getUrl().regex("http://www\\.51vapp\\.com/market/apps/search.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='l_list']/ul").regex("http://www\\.51vapp\\.com/market/apps/detail.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pager']").regex("http://www\\.51vapp\\.com/market/apps/search.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.51vapp\\.com/market/apps/detail.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='cont_fl']/div[1]/div[1]/h5/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='cont_fl']/div[1]/div[1]/div[1]/a/@href").toString();
//			
//			String platFormString =page.getHtml().xpath("//span[@class='fl']/p[2]/text()").toString();
//				osPlatform = platFormString.substring(platFormString.indexOf("固件")+3,platFormString.length());
//			//System.out.println(platFormString);
//				
//			String versionString = page.getHtml().xpath("//span[@class='fl']/p[2]/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("版本")+3,versionString.indexOf("大小")-1);
//			
//			String sizeString = page.getHtml().xpath("//span[@class='fl']/p[2]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("大小")+3,versionString.indexOf("语言")-1);
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender = null;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//span[@class='fl']/p[2]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("下载")+3,DownloadedTimeString.indexOf("次")-1);		
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
			return Www51vapp_Detail.getApkDetail(page);
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
