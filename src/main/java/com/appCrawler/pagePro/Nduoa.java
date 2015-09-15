package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Nduoa_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * N多网  http://www.nduoa.com/
 * Nduoa.java #86
 * 搜索结果中有许多完全无关的结果
 * 搜索接口要自己构造--buildhappy
 * @author DMT
 */
public class Nduoa implements PageProcessor{
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Nduoa.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		LOGGER.debug("crawler url: {}", page.getUrl());
		//index page			http://www.nduoa.com/search?q=360	
		if(page.getUrl().regex("http://www\\.nduoa\\.com/search*").match()){
			//构造搜索接口
			if(page.getUrl().regex("http://www\\.nduoa\\.com/search\\?q=*").match()){
				String sk = page.getHtml().xpath("//input[@type='hidden']/@value").toString();
				//http://www.nduoa.com/search?sk=942aff3ce8dad9c249508a2695b54f32&q=qq
				//http://www.nduoa.com/search?sk=b550e7f6169bfcc598fbc0c6503015bf&q=qq
				//http://www.nduoa.com/search?q=qq
				String url = page.getUrl().toString();
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(url.split("\\?")[0]);
				strBuilder.append("?sk=");
				strBuilder.append(sk + "&");
				if(url.split("\\?").length > 1){
					strBuilder.append(url.split("\\?")[1]);
					page.addTargetRequest(strBuilder.toString());
				}
			}
			
			//app的具体介绍页面-->应用											
			List<String> url1 = page.getHtml().links("//div[@class='one-column module']").regex("http://www\\.nduoa\\.com/apk/detail/.*").all();

			//app的具体介绍页面-->游戏											
			List<String> url2 = page.getHtml().links("//div[@class='one-column module']").regex("http://www\\.nduoa\\.com/package/detail/.*").all();

			//添加下一页url(翻页)							http://www.nduoa.com/apk/detail
			List<String> url3 = page.getHtml().links("//div[@class='clearfix paginationWrapper']").regex("http://www\\.nduoa\\.com/search\\?.*").all();
			
			url1.addAll(url2);
			url1.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
			LOGGER.debug("app info results urls: {}", page.getTargetRequests());
		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.nduoa\\.com/apk/detail/.*").match() || page.getUrl().regex("http://www\\.nduoa\\.com/package/detail/.*").match()){
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
//			String nameString=page.getHtml().xpath("//span[@class='title']/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='downloadWrap']/div[1]/a/@href").toString();
//			
//			String osPlatformString = page.getHtml().xpath("//div[@class='adapt row popup']/h4/text()").toString();
//				osPlatform = osPlatformString.substring(osPlatformString.indexOf("：")+1,osPlatformString.length());
//			
//			String versionString = page.getHtml().xpath("//div[@class='apkinfo']/div[1]/div[2]/span[2]/text()").toString();
//				appVersion = versionString.substring(versionString.indexOf("(")+1,versionString.indexOf(")"));
//			
//			String sizeString = page.getHtml().xpath("//div[@class='size row']/text()").toString();
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//em[@class='em_gray']/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='levelCount']/span[2]/text()").toString();
//				appDownloadedTime = DownloadedTimeString;		
//			
////			System.out.println("appName="+appName);
////			System.out.println("appDetailUrl="+appDetailUrl);
////			System.out.println("appDownloadUrl="+appDownloadUrl);
////			System.out.println("osPlatform="+osPlatform);
////			System.out.println("appVersion="+appVersion);
////			System.out.println("appSize="+appSize);
////			System.out.println("appUpdateDate="+appUpdateDate);
////			System.out.println("appType="+appType);
////			System.out.println("appvender="+appvender);
////			System.out.println("appDownloadedTime="+appDownloadedTime);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			
			return Nduoa_Detail.getApkDetail(page);
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
	public static void main(String[] args){
		String s = "http://www.nduoa.com/search?q=qq";
		System.out.println(s.split("\\?").length);
	}
}
