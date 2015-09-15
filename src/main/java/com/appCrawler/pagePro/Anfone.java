package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Anfone_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 安丰市场 www.anfone.com
 * Anfone #87
 * (1)搜索结果的翻页url中p=0是第1页
 * @author DMT
 */
public class Anfone implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Anfone.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Anfone.process()" + page.getUrl());
		//index page				
		if(page.getUrl().regex("http://zhannei\\.baidu\\.com/cse/search\\?q=.*" ).match() ){
			//app的具体介绍页面								http://www.anfone.com/soft/54475.html			
			List<String> url1 = page.getHtml().links("//div[@id='results']").regex("http://www\\.anfone\\.com/soft/.*").all();

			if( page.getUrl().toString().contains("click"))
			{
			//添加下一页url(翻页)  
				List<String> url2 = page.getHtml().links("//div[@class='pager clearfix']").regex("http://zhannei\\.baidu\\.com/cse/search\\?q=.*").all();
			
				url1.addAll(url2);
			}
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.anfone\\.com/soft/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='downloadbox']/table/tbody/tr[1]/td[2]/text()").toString();			
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V"));
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v"));
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("."))
//			{
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//			}
//			else 
//			{
//				appName = nameString;
//				appVersion = null;
//			}
//
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='downloadbox']/table/tbody/tr[3]/td[2]/a/@href").toString();
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='downloadbox']/table/tbody/tr[2]/td[2]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//				
//			String DownloadedTimeString = null;
//				appDownloadedTime = DownloadedTimeString;
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Anfone.process()");
			return Anfone_Detail.getApkDetail(page);
		}
		logger.info("return from Anfone.process()");
		
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
