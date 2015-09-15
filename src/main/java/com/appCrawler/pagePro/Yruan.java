package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Yruan_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 亿软网 http://www.yruan.com/
 * Yruan #125
 * @author DMT
 * (1)搜索结果中获得的url是一个由不同平台的版本的软件下载，需要再跳转一次
 */
public class Yruan implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.yruan.com/search.php?keyword=%E5%AE%89%E5%85%A8%E7%AE%A1%E5%AE%B6
		if(page.getUrl().regex("http://www\\.yruan\\.com/search\\.php\\?keyword=.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@id='list_softs']").regex("http://www\\.yruan\\.com/softdetail.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//form[@name='page_jump']").regex("http://www\\.yruan\\.com/search\\.php\\?keyword=.*").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
	//软件的detail页面里面有不同类型的应用下载 sybian Android iPhone	
		else if(page.getUrl().regex("http://www\\.yruan\\.com/softdetail.*").match()){
			//http://www.yruan.com/softdown.php?id=8110&phoneid=
			List<String> url1 = page.getHtml().links("//div[@class='column12']").regex("http://www\\.yruan\\.com/softdown.*").all();
						
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}
		}
	//the app detail page
		if(page.getUrl().regex("http://www\\.yruan\\.com/softdown.*").match()){
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
//		//如果不是安卓平台的直接返回	
//			String platFormString =page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[2]/span/text()").toString();
//			if(platFormString.contains("Android") || platFormString.contains("android"))
//				osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
//			else return null;
//			
//			String nameString=page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/a/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[3]/div[2]/a/@href").toString();
//			
//			
//			String versionString = null;
//				appVersion = versionString;
//				
//			String sizeString = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/text()").toString();
//				appSize = sizeString.substring(sizeString.lastIndexOf("：")+1,sizeString.length());
//			
//			String updatedateString = null;
//				appUpdateDate = updatedateString;
//			
//			String typeString = page.getHtml().xpath("//div[@class='right']/div[1]/ul/li[1]/text()").toString();
//				appType =typeString.substring(typeString.indexOf("：")+1,typeString.indexOf("大小")-1);
//			
//			appvender=null;
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
			
			return Yruan_Detail.getApkDetail(page);
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
