package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Newasp_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 新云网络  http://www.newasp.net/
 * Newasp #116
 * @author DMT
 */
public class Newasp implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.newasp.net/search.php?word=%E5%AE%89%E5%85%A8%E7%AE%A1%E5%AE%B6&submit=yes
		if(page.getUrl().regex("http://www\\.newasp\\.net/search\\.php\\?word=.*").match()){
			//app的具体介绍页面											http://www.newasp.net/android/74707.html
			List<String> url1 = page.getHtml().links("//div[@class='shlist']/ul").regex("http://www\\.newasp\\.net/android/[0-9]+\\.html").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='shpage']").regex("http://www\\.newasp\\.net/search\\.php\\?word=.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.newasp\\.net/android/[0-9]+\\.html").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='infobox']/div[2]/h1/text()").toString();			
//				appName =nameString.substring(0,nameString.indexOf(" "));
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			String downloadurlString = page.getHtml().xpath("//dl[@class='downlist']/script").toString();
//				appDownloadUrl = downloadurlString.substring(downloadurlString.indexOf("http"),downloadurlString.indexOf("本地高速下载")-2);
//			
//			String platFormString =page.getHtml().xpath("//ul[@class='infolist']/li[9]/text()").toString();
//				osPlatform = platFormString;
//		
//			String versionString = page.getHtml().xpath("//div[@class='infobox']/div[2]/h1/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf(" ")+1,versionString.lastIndexOf(" "));
//			
//			String sizeString = page.getHtml().xpath("//ul[@class='infolist']/li[4]/span/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='infolist']/li[7]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender = page.getHtml().xpath("//ul[@class='infolist']/li[6]/a/text()").toString();
//			if(appvender == null) appvender = page.getHtml().xpath("//ul[@class='infolist']/li[6]/text()").toString();
//				
//			String DownloadedTimeString = null;
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
			return Newasp_Detail.getApkDetail(page);
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
