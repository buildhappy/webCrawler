package com.appCrawler.pagePro;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Hzhuti_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 好主题 http://www.hzhuti.com/
 * Hzhuti #92
 * (1)此网站的app下载url需要在app detail page 中打开新的url获取到
 * (2)需要的app detail page有三类，soft|android|anzhuo
 * (3)2015/5/5 获取apk下载链接时需要打开url，需要设置代理，不然无法打开
 * @author DMT
 */
public class Hzhuti implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Hzhuti.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page)  {
		logger.info("call in Hzhuti.process()" + page.getUrl());
		//index page				http://www.hzhuti.com/plus/search_games.php?g=%B2%B6%D3%E3%B4%EF%C8%CB
		if(page.getUrl().regex("http://www\\.hzhuti\\.com/plus/search_games\\.php\\?.*").match() ||page.getUrl().regex("http://www\\.hzhuti\\.com/search/.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='listbox']").regex("http://www\\.hzhuti\\.com/soft.*").all();
			
			List<String> url2 = page.getHtml().links("//div[@class='listbox']").regex("http://www\\.hzhuti\\.com/android.*").all();

			List<String> url3 = page.getHtml().links("//div[@class='listbox']").regex("http://www\\.hzhuti\\.com/anzhuo.*").all();

			//添加下一页url(翻页)
			List<String> url4 = page.getHtml().links("//ul[@class='pagelist']").regex("http://www\\.hzhuti\\.com/search/.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			url1.addAll(url4);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.hzhuti\\.com/.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/h3/text()").toString();			
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
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
//			appDetailUrl = page.getUrl().toString();
//			
//			String downloadUrlString = page.getHtml().xpath("//div[@class='article_txt']/p[1]/a/@href").toString();
//			if(downloadUrlString == null )
//			{
//				logger.info("return from Hzhuti.process()");
//				return null;
//			}
//			String sourcefile=null;
//			 URL url;
//			 String l = null;
//			 StringBuffer bs=null;
//			try {
//				url = new URL(downloadUrlString);
//				 HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
//				 urlcon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//		           urlcon.connect();         //获取连接
//		           InputStream is = urlcon.getInputStream();
//		           BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
//		            bs = new StringBuffer();		           
//		           while((l=buffer.readLine())!=null){
//		               bs.append(l).append("/n");
//		           }
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	          
//			sourcefile = bs.toString();
//			if(sourcefile == null )
//			{
//				logger.info("can't not find the appDownloadUrl,return from Hzhuti.process()");
//				return null;
//			}
//			
//			appDownloadUrl="www.hzhuti.com/"+sourcefile.substring(sourcefile.indexOf("plus/download.php"),sourcefile.indexOf("downbtn")-10);
//			
//			String platformString =page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[7]/text()").toString();
//				osPlatform = platformString.substring(platformString.indexOf("：")+1,platformString.length());
//			
//			String sizeString = page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[1]/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[4]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = null;
//				appDownloadedTime = DownloadedTimeString;	
//			/*
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
//			 */
//			if(appName != null && appDownloadUrl != null){
//				logger.info("return from Hzhuti.process()");
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Hzhuti_Detail.getApkDetail(page);
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
