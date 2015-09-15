package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Mobyware_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * mobyWare[德国] http://www.mobyware.net/
 * Mobyware名 #78
 * @author DMT
 * (1)网址 301 Moved Permanently http://www.mobuware.net/
 */
public class Mobyware implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.mobuware.net/programList.php?view=global&platform_id=18&search=mobile
		if(page.getUrl().regex("http://www\\.mobuware\\.net/programList\\.php\\?view=global.*").match()){
			//app的具体介绍页面					http://www.mobuware.net/android-os/alyac-android-download-free-147007.html						
			List<String> url1 = page.getHtml().links("//table[@class='program_list']/tbody").regex("http://www\\.mobuware\\.net/android-os/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page_ruler']").regex("http://www\\.mobuware\\.net/programList\\.php\\?view=global.*").all();
			
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
		if(page.getUrl().regex("http://www\\.mobuware\\.net/android-os/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@id='program_info_title']/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			//下载地址手动构造
//			String urlidString=page.getHtml().xpath("//div[@class='ratingblock']/div/@id").toString();
//				urlidString=urlidString.substring(urlidString.indexOf("long")+4);
//			appDownloadUrl="http://www.mobyware.net/get-software-"+urlidString+".html";
//			
//			
//			String platFormString =page.getHtml().xpath("//td[@id='program_info']/div[1]/span[1]/span[2]/span/text()").toString();
//				osPlatform = platFormString;
//		
//			String versionString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[1]/span[1]/text()").toString();
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[6]/span[1]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[3]/span[1]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[4]/span[1]/text()").toString();
//				
//			String DownloadedTimeString = page.getHtml().xpath("//td[@id='program_info']/div[1]/span[5]/span[1]/text()").toString();
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
			
			return Mobyware_Detail.getApkDetail(page);
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
