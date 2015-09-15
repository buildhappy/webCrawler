package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Anzhi_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 安智网 http://www.anzhi.com/index.html
 * Anzhi #127
 * 下载apk的url需要手动构造
 * @author DMT
 */
public class Anzhi implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Anzhi.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Anzhi.process()" + page.getUrl());
		//index page		http://www.anzhi.com/search.php?keyword=qq		
		if(page.getUrl().regex("http://www\\.anzhi\\.com/search\\.php\\?keyword.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='app_list border_three']/ul").regex("http://www\\.anzhi\\.com/soft.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='app_list border_three']/div").regex("http://www\\.anzhi\\.com/search\\.php\\?keyword.*").all();
			
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
		if(page.getUrl().regex("http://www\\.anzhi\\.com/soft.*").match()){
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
//			appName=page.getHtml().xpath("//div[@class='detail_description']/div[1]/h3/text()").toString();			
//					
//			appDetailUrl = page.getUrl().toString();
//			
//			String pageidString=appDetailUrl.substring(appDetailUrl.indexOf("soft")+5,appDetailUrl.indexOf("html")-1);
//				appDownloadUrl = "www.anzhi.com/dl_app.php?s="+pageidString+"&n=5";
//			
//			String platFormString =page.getHtml().xpath("//ul[@id='detail_line_ul']/li[5]/text()").toString();
//				osPlatform = platFormString.substring(platFormString.indexOf("：")+1,platFormString.length());
//		
//			String versionString = page.getHtml().xpath("//span[@class='app_detail_version']/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("(")+1,versionString.lastIndexOf(")")-1);
//			
//			String sizeString = page.getHtml().xpath("//ul[@id='detail_line_ul']/li[4]/span/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//ul[@id='detail_line_ul']/li[3]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//		
//			appType = "apk";
//			
//			String venderString = page.getHtml().xpath("//ul[@id='detail_line_ul']/li[7]/text()").toString();
//				appvender=venderString.substring(venderString.indexOf("：")+1,venderString.length());
//				
//				
//			String DownloadedTimeString = page.getHtml().xpath("//ul[@id='detail_line_ul']/li[4]/span/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='app_detail_infor']/p/text()").toString();
//				appDescription = descriptionString.replace("\n", "");
//				appDescription = appDescription.replace(" ", "");
//			
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
//			System.out.println("appDescription="+appDescription);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Anzhi.process()");
			return Anzhi_Detail.getApkDetail(page);
		}
		logger.info("return from Anzhi.process()");
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
