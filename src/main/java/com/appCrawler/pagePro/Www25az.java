package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Www25az_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**			
 * 爱吾安卓 http://www.25az.com/
 * Www25az
 * 有些应用放在百度网盘中，无法下载		
 * @author DMT
 */
public class Www25az implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page		http://www.25az.com/game/?key=qq		
		if(page.getUrl().regex("http://www\\.25az\\.com/game/\\?.*").match()){
			//app的具体介绍页面						http://www.25az.com/game/View/7603/					
			List<String> url1 = page.getHtml().links("//ul[@class='app_list']").regex("http://www\\.25az\\.com/game.*").all();

//			//添加下一页url(翻页)
//			List<String> url2 = page.getHtml().links("//div[@class='mdpage']").regex("http://www\\.ruan8\\.com/search\\.php\\?.*").all();
//			
//			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.25az\\.com/game.*").match()){
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
//		 appName=page.getHtml().xpath("//div[@class='app-msg']/h1/text()").toString();			
//				
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='app_down']/a[4]/@href").toString();
//			
//			 osPlatform =page.getHtml().xpath("//div[@class='app-msg']/dl/dd[5]/text()").toString();
//		
//			 appVersion = page.getHtml().xpath("//div[@class='app-msg']/dl/dd[1]/text()").toString();
//				
//			
//			 appSize = page.getHtml().xpath("//div[@class='app-msg']/dl/dd[2]/text()").toString();
//				
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='l']/li[8]/text()").toString();
//			if(updatedateString != null &&updatedateString.contains("："))
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			
//				appType = "apk";
//			
//			appvender=null;
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			
			return Www25az_Detail.getApkDetail(page);
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
