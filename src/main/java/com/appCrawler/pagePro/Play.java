package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Play_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * playcn爱游戏 http://www.play.cn/
 * Play #96
 * (1)网站中有些页面打开缓慢
 * @author DMT
 */
public class Play implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page			http://www.play.cn/game/search?keyword=%E5%AE%89%E5%85%A8				
		if(page.getUrl().regex("http://www\\.play\\.cn/game/search.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@class='m_list_game fix']").regex("http://www\\.play\\.cn/game/gamedetail.*").all();
			
			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//ul[@class='pages']").regex("http://www\\.play\\.cn/game/search.*").all();
			
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
		if(page.getUrl().regex("http://www\\.play\\.cn/game/gamedetail.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='game_info']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='info_side']/div[3]/a/@href").toString();
//			
//			osPlatform = null;
//			
//			String versionString = null;
//				appVersion = versionString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='game_layer pl15 pr15 pb15 mb15']/div[1]/div[2]/p/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='game_layer pl15 pr15 pb15 mb15']/div[2]/div[1]/p/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=page.getHtml().xpath("//div[@class='game_layer pl15 pr15 pb15 mb15']/div[3]/span[2]/text()").toString();
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='game_layer pl15 pr15 pb15 mb15']/div[2]/div[2]/p/text()").toString();
//				appDownloadedTime = DownloadedTimeString;		
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Play_Detail.getApkDetail(page);
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
