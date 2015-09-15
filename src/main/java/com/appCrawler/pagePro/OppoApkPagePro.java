package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.OppoApkPagePro_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * oppo手机软件：http://store.nearme.com.cn/
 * 伪造下载链接:
 * http://store.nearme.com.cn/product/download.html?id=588252&from=1135_-1
 * 将id和from后的参数进行处理
 * @author buildhappy
 *
 */
public class OppoApkPagePro implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(1).setSleepTime(2);
	@Override
	public Apk process(Page page) {
		//search page
		if(page.getUrl().regex("http://store\\.oppomobile\\.com/search/*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='list_content']").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//ul[@class='yiiPager']").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		String appName = page.getHtml().xpath("//div[@class='soft_info_middle']/h3/text()").toString();
		
		//the app detail page
		if(page.getUrl().regex("http://store\\.oppomobile\\.com/.*").match() && appName != null){
//			Apk apk = null;
//			String rawId = null;
//			String id = null;
//			String rawFromId = null;
//			String fromId = null;
//			String appDetailUrl = page.getUrl().toString();
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = "apk";				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			String appDescription = null;
//			
//			List<String> info = null;
//			rawId = page.getHtml().xpath("//a[@class='detail_down']/@onclick").toString();
//			if(rawId != null){
//				id = rawId.split("\\(|\\)")[1];
//				if((rawFromId=page.getUrl().toString()).contains("=")){
//					fromId = rawFromId.split("=")[1];
//				}
//				appDownloadUrl	= "http://store.nearme.com.cn/product/download.html?id="+ id + "&from="+fromId;
//			}
//			info = page.getHtml().xpath("//ul[@class='soft_info_more']/li/text()").all();	
//			
//			appName = page.getHtml().xpath("//div[@class='soft_info_middle']/h3/text()").toString();
//			if(info != null && appName != null){
//				int size = info.size();
//				//String[] cur = null;
//				appUpdateDate = size>=0?info.get(0).split("：")[1]:null;
//				appSize = size>0?info.get(1).split("：")[1]:null;
//				appVersion = size>1?info.get(2).split("：")[1]:null;
//				osPlatform = size>3?info.get(4).split("：")[1]:null;
//			}
//			appDescription = page.getHtml().xpath("//input[@id='soft_description']/@value").toString();
////			System.out.println("appName="+appName);
////			System.out.println("appDetailUrl="+appDetailUrl);
////			System.out.println("appDownloadUrl="+appDownloadUrl);
////			System.out.println("osPlatform="+osPlatform);
////			System.out.println("appVersion="+appVersion);
////			System.out.println("appSize="+appSize);
////			System.out.println("appUpdateDate="+appUpdateDate);
////			System.out.println("appType="+appType);
////			System.out.println("appvender="+appvender);
//			System.out.println("appDescription="+appDescription);
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			return OppoApkPagePro_Detail.getApkDetail(page);
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
