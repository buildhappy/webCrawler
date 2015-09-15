package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Zol_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * zol手机应用 http://sj.zol.com.cn/mobilesoft/
 * Zol #99
 * @author DMT
 */
public class Zol implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://xiazai.zol.com.cn/search?wd=%CA%D6%BB%FA%B9%DC%BC%D2&type=2
		if(page.getUrl().regex("http://xiazai\\.zol\\.com\\.cn/search\\?wd=.*").match()){
			//app的具体介绍页面											http://sj.zol.com.cn/	http://sj.zol.com.cn/detail/36/35404.shtml
			List<String> url1 = page.getHtml().links("//ul[@class='result-list']").regex("http://sj\\.zol\\.com\\.cn/.*").all();
//			List<String> url2 = page.getHtml().links("//ul[@class='result-list']").regex("http://sj\\.zol\\.com\\.cn/detail/.*").all();

			List<String> url2 = new LinkedList<String>();
			for(String temp:url1)
			{
				
					if(temp.endsWith("/"))	
					
					url2.add(temp);				
			
				
			}
			//添加下一页url(翻页)
			List<String> url3 = page.getHtml().links("//div[@class='page']").regex("http://xiazai\\.zol\\.com\\.cn/search\\?wd=.*").all();
			
//			url1.addAll(url2);
			url2.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url2);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://sj\\.zol\\.com\\.cn/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='soft-summary']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			String downloadString = page.getHtml().xpath("//div[@class='soft-detail']/div/a/@href").toString();
//				appDownloadUrl = downloadString;
//			
//			String platFormString =page.getHtml().xpath("//div[@class='soft-detail']/ul/li[2]/text()").toString();
//				osPlatform = platFormString;
//		
//			String versionString = page.getHtml().xpath("//div[@class='soft-detail']/h3/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf(" ")+1,versionString.length());
//			
//			String sizeString = page.getHtml().xpath("//div[@class='soft-detail']/ul/li[1]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='soft-text']/li[2]/em/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='summary-text clearfix']/li[3]/span[2]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(0,DownloadedTimeString.length()-1);		
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
			return Zol_Detail.getApkDetail(page);
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
