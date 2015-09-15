package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Duote_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 多特 http://www.duote.com/   ---->>>王牌手机助手 http://zhushou.2345.com/
 * Duote.java #84  
 * @author DMT
 */
public class Duote implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://zhushou.2345.com/index.php?
		if(page.getUrl().regex("http://zhushou\\.2345\\.com/index\\.php\\?*").match()){
			//app的具体介绍页面											
			//软件
			List<String> url1 = page.getHtml().links("//div[@class='section section-2 search_list']").regex("http://zhushou\\.2345\\.com/soft.*").all();
			//游戏
			List<String> url2 = page.getHtml().links("//div[@class='section section-2 search_list']").regex("http://zhushou\\.2345\\.com/game.*").all();

			//添加下一页url(翻页)
			List<String> url3 = page.getHtml().links("//div[@class='Mpage']").regex("http://zhushou\\.2345\\.com/search\\.php\\?.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page					 | http://zhushou\\.2345\\.com/game.*
		if(page.getUrl().regex("http://zhushou\\.2345\\.com/soft.*").match() || page.getUrl().regex("http://zhushou\\.2345\\.com/game.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='areaSoftinfo']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//a[@class='btn_down_to_pc']/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//ul[@class='prop_area']/li[4]/text()").toString();
//			
//			String versionString = page.getHtml().xpath("//ul[@class='prop_area']/li[1]/text()").toString();
//				appVersion = versionString;
//				
//			String sizeString = page.getHtml().xpath("//div[@class='prop']/span[2]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='prop_area']/li[2]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			String venderString = page.getHtml().xpath("//ul[@class='prop_area']/li[5]/text()").toString();
//				appvender = venderString;
//			
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='prop']/span[1]/text()").toString();
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
			
			return Duote_Detail.getApkDetail(page);
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
