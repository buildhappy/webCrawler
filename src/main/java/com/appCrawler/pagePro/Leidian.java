package com.appCrawler.pagePro;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Leidian_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 雷电   http://www.leidian.com/
 * Leidian.java    #77
 * 搜索结果中的翻页的url在爬虫中无法打开，但是在浏览器中可以  使用curl工具也可以打开
 * 解决方式：把结尾带有参数||的字段去除，这样在爬虫中就可以打开了
 * @author DMT
 */
public class Leidian implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Liqucn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Leidian.process()" + page.getUrl());
		//index page			http://www.leidian.com/s?q=qq&ie=utf-8&t=&src=shouji_www	
		if(page.getUrl().regex("http://www\\.leidian\\.com/s\\?.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@class='mod-soft-list']").regex("http://soft\\.leidian\\.com/detail.*").all();

			List<String> url2 = page.getHtml().links("//ul[@class='mod-soft-list']").regex("http://game\\.leidian\\.com/detail.*").all();

			//添加下一页url(翻页)
			List<String> url3 = page.getHtml().links("//div[@class='pageation']").regex("http://www\\.leidian\\.com/s\\?.*").all();

			List<String> url4 = new LinkedList<String>();
			for(String temp:url3)
			{
				try {
					String str = temp.substring(0,temp.length()-7);
					
					url4.add(str);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
			url1.addAll(url2);
			url1.addAll(url4);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
			

		}
		
		//the app detail page http://soft.leidian.com/detail/index/soft_id/85436
		if(page.getUrl().regex("http://soft\\.leidian\\.com/detail.*").match() || page.getUrl().regex("http://game\\.leidian\\.com/detail.*").match()){
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
//			 appName=page.getHtml().xpath("//h1/text()").toString();			
//				
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			String downloadUrlString = page.getHtml().toString();
//				appDownloadUrl = downloadUrlString.substring(downloadUrlString.indexOf("downurl")+10,downloadUrlString.indexOf("barcode")-16);		
//			
//			String osPlatformString = page.getHtml().xpath("//div[@class='mod-base-info']/ul/li[3]/text()").toString();
//				osPlatform = osPlatformString.substring(osPlatformString.indexOf("：")+1,osPlatformString.length());
//						
//			String versionString = page.getHtml().xpath("//div[@class='mod-base-info']/ul/li[2]/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("：")+1,versionString.length());
//			
//			String sizeString = page.getHtml().xpath("//div[@class='mod-base-info']/ul/li[1]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='soft-extra-info']/span[2]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//						
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='soft-extra-info']/span[1]/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());
//			
//			String venderString = page.getHtml().xpath("//ul[@class='clearfix']/li[6]/text()").toString();
//				appvender = venderString.substring(venderString.indexOf("：")+1,venderString.length());		
//				/*
//				System.out.println("appName="+appName);
//				System.out.println("appDetailUrl="+appDetailUrl);
//				System.out.println("appDownloadUrl="+appDownloadUrl);
//				System.out.println("osPlatform="+osPlatform);
//				System.out.println("appVersion="+appVersion);
//				System.out.println("appSize="+appSize);
//				System.out.println("appUpdateDate="+appUpdateDate);
//				System.out.println("appType="+appType);
//				System.out.println("appvender="+appvender);
//				System.out.println("appDownloadedTime="+appDownloadedTime);
//				*/
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Leidian.process()");
			return Leidian_Detail.getApkDetail(page);
		}
		logger.info("return from Leidian.process()");
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
