package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Bkill_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/** 
 * 必杀客 http://www.bkill.com
 * Bkill #136
 * @author DMT
 */
public class Bkill implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Bkill.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Bkill.process()" + page.getUrl());
		//index page				http://www.bkill.com/d/search.php?searchType=&mod=do&n=1&keyword=qq
		if(page.getUrl().regex("http://www\\.bkill\\.com/d/search\\.php\\?.*").match()){
			//app的具体介绍页面											http://www.bkill.com/download/11432.html
			List<String> url1 = page.getHtml().links("//div[@class='clsList']").regex("http://www\\.bkill\\.com/download.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='clsList']/div").regex("http://www\\.bkill\\.com/d/search\\.php\\?.*").all();
			
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
		if(page.getUrl().regex("http://www\\.bkill\\.com/download.*").match()){
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
//			String platFormString =page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[11]/span/text()").toString();
//			osPlatform = platFormString;
//			if(!osPlatform.contains("Android") && !osPlatform.contains("android"))
//			{
//				logger.info("return from Bkill.process()");
//				return null;
//				
//			}
//			String nameString=page.getHtml().xpath("//h1[@class='title_h1']/text()").toString();			
//			System.out.println("nameString="+nameString);
//			if(nameString != null && nameString.contains("V"))
//					{
//						appName=nameString.substring(0,nameString.indexOf("V")-1);
//						appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//					}
//					else if(nameString != null && nameString.contains("v"))
//					{
//						appName=nameString.substring(0,nameString.indexOf("v")-1);
//						appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//					}
//					else if(nameString != null && nameString.contains("."))
//					{
//						appName=nameString.substring(0,nameString.indexOf(".")-1);
//						appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//					}
//					else 
//					{
//						appName = nameString;
//						appVersion = null;
//					}
//
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='down_link_main']/ul/li[5]/a/@href").toString();
//			
//			
//			
//			String sizeString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[2]/span/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[8]/span/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			String venderString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[7]/span/text()").toString();
//				appvender=venderString;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='soft_Abstract h305 w412 l top7 bd']/ul/li[10]/span/script/@src").toString();
//			String line=null;
//			try {
//				//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
//				URL url=new URL(DownloadedTimeString);
//				BufferedReader reader;
//				reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				line=reader.readLine();
//				//line=document.write('30168');
//			} catch (Exception e) {
//			}
//			if(line != null)
//				appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='jieshao']/p/text()").toString();
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
			logger.info("return from Bkill.process()");
			return Bkill_Detail.getApkDetail(page);
		}
		logger.info("return from Bkill.process()");
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
