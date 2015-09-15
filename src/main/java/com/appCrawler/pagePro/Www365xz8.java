package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 365下载吧http://www.365xz8.cn
 * Www365xz8 #129
 * @author DMT
 */
public class Www365xz8 implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Www365xz8.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Www365xz8.process()" + page.getUrl());
		//index page			http://www.365xz8.cn/soft/search.asp?act=topic&keyword=qq	
		if(page.getUrl().regex("http://www\\.365xz8\\.cn/soft/search\\.asp\\?.*").match()){
			//app的具体介绍页面								http://www.365xz8.cn/soft/sort07/89402.html			
			List<String> url1 = page.getHtml().links("//div[@id='searchmain']").regex("http://www\\.365xz8\\.cn/soft/sort07/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='mainNextPage']").regex("http://www\\.365xz8\\.cn/soft/search\\.asp\\?.*").all();
			
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
		if(page.getUrl().regex("http://www\\.365xz8\\.cn/soft/sort07/.*").match()){
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			String appDescription =null;        //app的详细介绍
			
			String allinfoString = page.getHtml().xpath("//dd[@class='downInfoRowL']").toString();

			while(allinfoString.contains("<"))
				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			
			String runningEnvironmentString = allinfoString.substring(allinfoString.indexOf("运行环境")+5,allinfoString.indexOf("下载统计")-1);
				System.out.println("runningEnvironmentString="+runningEnvironmentString);
			if(allinfoString.contains("iPhone") && !allinfoString.contains("Android"))
				{
					logger.info("return from Www365xz8.process()");
					return null;
					
				}
			
			String nameString=page.getHtml().xpath("//dt[@id='downInfoTitle']/text()").toString();			
			if(nameString != null && nameString.contains("V"))
			{
				appName=nameString.substring(0,nameString.indexOf("V")-1);
				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
			}
			else if(nameString != null && nameString.contains("v"))
			{
				appName=nameString.substring(0,nameString.indexOf("v")-1);
				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
			}
			else if(nameString != null && nameString.contains("."))
			{
				appName=nameString.substring(0,nameString.indexOf(".")-1);
				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
			}
			else 
			{
				appName = nameString;
				appVersion = null;
			}

				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//*[@id='mainBody']/div[6]/div[4]/div/table/tbody/tr/td[1]/a[4]/@href").toString();
			
			osPlatform = runningEnvironmentString;
		
			String sizeString = allinfoString.substring(allinfoString.indexOf("软件大小")+5,allinfoString.indexOf("推荐星级")-1);
				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
			
			String updatedateString = allinfoString.substring(allinfoString.indexOf("更新时间")+5,allinfoString.indexOf("插件情况")-1);
				appUpdateDate = updatedateString;
			
			appType ="apk";
			
					
			String descriptionString = page.getHtml().xpath("//div[@id='mainSoftIntro']").toString();
			while(descriptionString.contains("<"))
				if(descriptionString.indexOf("<") == 0) descriptionString = descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
				else if(descriptionString.contains("<!--")) descriptionString = descriptionString.substring(0,descriptionString.indexOf("<!--")) + descriptionString.substring(descriptionString.indexOf("-->")+3,descriptionString.length());
				else descriptionString = descriptionString.substring(0,descriptionString.indexOf("<")) + descriptionString.substring(descriptionString.indexOf(">")+1,descriptionString.length());
			appDescription = descriptionString;
			
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appvender);
			System.out.println("appDownloadedTime="+appDownloadedTime);
			System.out.println("appDescription="+appDescription);
			
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
			}
			logger.info("return from Www365xz8.process()");
			return apk;
		}
		logger.info("return from Www365xz8.process()");
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
