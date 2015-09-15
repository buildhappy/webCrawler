package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Eoemarket_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 优亿市场[中国] http://partner.eoemarket.com/qq/bibei/index/
 * Eoemarket #666
 * 该网站下载apk时电脑必须安装类似于应用包之类的手机管理软件，点击下载apk后电脑自动启动手机管理软件进行下载
 * 不过可以构造下载链接，但是太过复杂
 * ·通过抓包获取的下载：http://d2.eoemarket.com/app0/17/17896/apk/721873.apk?channel_id=401
 * ·页面中存在的链接1：http://c11.eoemarket.com/app0/17/17896/icon/721873.png
 * ·页面中存在的链接2：http://download.eoemarket.com/app?id=17896%26client_id=146%26channel_id=401%26track=pc_qq_show_index_app17896_2
 *  通过后两个链接拼接成下载链接
 * @author buildhappy&DMT 
 */
public class Eoemarket implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Eoemarket.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Eoemarket.process()" + page.getUrl());
		//index page				http://partner.eoemarket.com/qq/search/index/?kw=qq
		if(page.getUrl().regex("http://partner\\.eoemarket\\.com/qq/search/index/\\?kw=.*").match()){
			//app的具体介绍页面			http://partner.eoemarket.com/qq/show/index/appId/31924								
			List<String> url1 = page.getHtml().links("//ul[@class='mainlist ranklist chcon']").regex("http://partner\\.eoemarket\\.com/qq/show/index/appId/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='sabrosus']").regex("http://partner\\.eoemarket\\.com/qq/search/index/\\?kw=.*").all();
			
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
		if(page.getUrl().regex("http://partner\\.eoemarket\\.com/qq/show/index/appId/.*").match()){
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
//			appName =page.getHtml().xpath("//div[@class='para']/div[1]/h1/text()").toString();			
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			String url1 = page.getHtml().xpath("//div[@class='showpara']/img/@src").toString();
//			String url2 = page.getHtml().xpath("//div[@class='code']/img/@src").toString();
//			if(page.getUrl().toString().contains("appId") && url2 != null && url2.split("\\|").length > 0){
//				//System.out.println("url1:" + url1);
//				url1 = url1.split("http://c11.eoemarket.com/|.png").length > 0 ? url1.split("http://c11.eoemarket.com/|.png")[1]:null;
//				if(url1 != null){			
//					url1 = url1.replace("icon", "apk");
//					url1 = url1.split("_")[0];
//				}
//				//System.out.println("url2:" + url2);
//				url2 = url2.split("chld\\=S\\|1\\&chl=")[1];
//				url2 =url2.split("channel_id=|%").length > 3 ?  url2.split("channel_id=|%")[3]:null;
//				appDownloadUrl = "http://d2.eoemarket.com/" + url1 + ".apk?channel_id=" + url2;
//			}
//			
//			appVersion = page.getHtml().xpath("//div[@class='para']/div[1]/span/text()").toString();
//			
//
//			appSize = page.getHtml().xpath("//div[@class='txt']/span[2]/text()").toString();
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='para']/div[4]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			appType ="apk";
//			
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='para']/div[2]/span/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length());		
//			
//			String descriptionString = page.getHtml().xpath("//div[@class='apptxt']").toString();
//			String allinfoString = descriptionString;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			appDescription = allinfoString;	
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
			logger.info("return from Eoemarket.process()");
			return Eoemarket_Detail.getApkDetail(page);
		}
		logger.info("return from Eoemarket.process()");
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
