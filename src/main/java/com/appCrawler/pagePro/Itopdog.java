package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Itopdog_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 软件盒子 http://www.itopdog.cn/
 * Itopdog #81
 * (1)该网站的关键字搜索结果的翻页有错误，通过页面给定的结果无法进入到正确的搜索结果中
 * 但是可以通过手动构造翻页url来获取，尝试多个关键字的搜索，搜索结果最多只有两页，因此，手动构造第二页的url
 * (2)此网站的下载次数是及时更新的
 * (3)该网站有些应用已经无法下载
 * @author DMT
 */
public class Itopdog implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.itopdog.cn/home.php?type=az&ct=home&ac=search&q=%E6%B5%8F%E8%A7%88%E5%99%A8
		if(page.getUrl().regex("http://www\\.itopdog\\.cn/home\\.php\\?*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='panel']").regex("http://www\\.itopdog\\.cn/az.*").all();

			//添加下一页url(翻页)  第2页
			List<String> url2 = page.getHtml().links("//div[@class='clearfix pagewrap']").regex("http://www\\.itopdog\\.cn/.*").all();
			if(url2.isEmpty() == false)
			{
				String url = page.getUrl()+"&per_page=20";			
				url1.add(url);
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
		if(page.getUrl().regex("http://www\\.itopdog\\.cn/az.*").match()){
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
//			//有的名字里面包含版本号，有的不包含
//			String nameString=page.getHtml().xpath("//font[@class='h2_css']/text()").toString();
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
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
//			appDownloadUrl = page.getHtml().xpath("//div[@class='down-btn']/a/@href").toString();
//			
//			osPlatform = page.getHtml().xpath("//dl[@class='clearfix appinfo']/dd[4]/text()").toString();
//			
//			String sizeString = page.getHtml().xpath("//dl[@class='clearfix appinfo']/dd[1]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='six code2d']/strong/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//
//			//下载次数是动态获取的，使用downloadTimeUrl构造出获取下载次数的链接
//			String id=appDetailUrl.substring(appDetailUrl.indexOf("-")+1,appDetailUrl.lastIndexOf(".")-1);
//			String downloadTimeUrl="http://www.itopdog.cn/home.php?ct=home&ac=get_updown_api&id="+id;			
//			String line=null;
//			try {
//				//打开一个网址，获取源文件，这个网址里面是
////				{
////					state: true,
////					up: "0",
////					down: "0",
////					up_per: "0%",
////					down_per: "0%",
////					down_all: "3"
////					}
//				URL url=new URL(downloadTimeUrl);
//				BufferedReader reader;
//				reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				for(int i=0;i<7;i++)
//					line=reader.readLine();
//				//line=document.write('30168');
//			} catch (Exception e) {
//			}
//			if(line != null)
//				appDownloadedTime =line.substring(line.indexOf("\"")+1,line.lastIndexOf("\"")-1);
//						
////			String DownloadedTimeString = page.getHtml().xpath("//ul[@class='mdccs']/li[9]/text()").toString();
////				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
//			
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
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			
			return Itopdog_Detail.getApkDetail(page);
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
