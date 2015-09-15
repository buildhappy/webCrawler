package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;






import com.appCrawler.pagePro.apkDetails.Ruan8_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 软吧http://www.ruan8.com/
 * Ruan8  #69
 * 2015年7月21日10:41:16 搜索接口不可用 
 * @author DMT
 */
public class Ruan8 implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				
		if(page.getUrl().regex("http://www\\.ruan8\\.com/search\\.php\\?*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@id='myTab2_con0']").regex("http://www\\.ruan8\\.com/soft.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='mdpage']").regex("http://www\\.ruan8\\.com/search\\.php\\?.*").all();
			
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
		if(page.getUrl().regex("http://www\\.ruan8\\.com/soft.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='mdlmenu w750']/h1/text()").toString();			
//				appName =nameString.substring(0,nameString.indexOf("v")-1);
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='mdddblist']/p/a/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//p[@id='os']/text()").toString();
//			
//			String versionString = page.getHtml().xpath("//div[@class='mdlmenu w750']/h1/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("v")+1,versionString.lastIndexOf(".")+2);
//			
//			String sizeString = page.getHtml().xpath("//ul[@class='mdccs']/li[7]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='mdccs']/li[8]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = page.getHtml().xpath("//ul[@class='mdccs']/li[3]/text()").toString();
//				appType =typeString.substring(typeString.indexOf("：")+1,typeString.length());
//			
//			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
//			
////			System.out.println("appName="+appName);
////			System.out.println("appDetailUrl="+appDetailUrl);
////			System.out.println("appDownloadUrl="+appDownloadUrl);
////			System.out.println("osPlatform="+osPlatform);
////			System.out.println("appVersion="+appVersion);
////			System.out.println("appSize="+appSize);
////			System.out.println("appUpdateDate="+appUpdateDate);
////			System.out.println("appType="+appType);
////			System.out.println("appvender="+appvender);
////			System.out.println("appDownloadedTime="+appDownloadedTime);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			
			return Ruan8_Detail.getApkDetail(page);
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
