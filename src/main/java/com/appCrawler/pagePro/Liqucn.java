package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Liqucn_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 历趣(谷歌电子市场) http://www.liqucn.com/
 * Liqucn #130
 * @author DMT
 */
public class Liqucn implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Liqucn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Liqucn.process()" + page.getUrl());
		//index page				http://www.liqucn.com/search/download/%E5%88%80%E5%A1%94%E4%BC%A0%E5%A5%87
		if(page.getUrl().regex("http://www\\.liqucn\\.com/search/download/.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@class='sear_list']").regex("http://www\\.liqucn\\.com/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page']").regex("http://www\\.liqucn\\.com/search/download/.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.liqucn\\.com/.*").match()){
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
//			appName =page.getHtml().xpath("//div[@class='app_leftinfo']/h1/text()").toString();	
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//ul[@class='app_btn']/li[2]/a/@href").toString();
//			
//			String allinfoString = page.getHtml().xpath("//div[@class='app_leftinfo']").toString();
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//
//			
//			appVersion = allinfoString.substring(allinfoString.indexOf("版本")+3,allinfoString.indexOf("分类")).replace("\n", "").replace(" ", "");
//			
//			appSize = allinfoString.substring(allinfoString.indexOf("大小")+3,allinfoString.indexOf("更新"));
//			
//			appUpdateDate =  allinfoString.substring(allinfoString.indexOf("更新")+3,allinfoString.indexOf("版本"));
//						
//			appType = "apk";
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='p_info']/text()").toString();
//				appDescription = descriptionString;	
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
			logger.info("return from Liqucn.process()");
			return Liqucn_Detail.getApkDetail(page);
		}
		logger.info("return from Liqucn.process()");
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
