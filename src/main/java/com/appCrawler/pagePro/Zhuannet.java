package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Zhuannet_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 赚网 http://apk.zhuannet.com/soft/
 * Zhuannet #73
 * 搜索结果又应用和游戏两种，通过url1和url2加入到队列中
 * @author DMT
 */
public class Zhuannet implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page			http://apk.zhuannet.com/search/?keyword=	
		if(page.getUrl().regex("http://apk\\.zhuannet\\.com/search/\\?.*").match()){
			String s = page.getHtml().xpath("//div[@class='no_app_tips']/text()").toString();
			if(s == null || !s.contains("未找到与")){
				//app的具体介绍页面											http://apk.zhuannet.com/soft/3.html
				List<String> url1 = page.getHtml().links("//div[@class='app_list_right left m_top0']").regex("http://apk\\.zhuannet\\.com/soft.*").all();
				
				List<String> url2 = page.getHtml().links("//div[@class='app_list_right left m_top0']").regex("http://apk\\.zhuannet\\.com/game.*").all();

				//添加下一页url(翻页)
				List<String> url3 = page.getHtml().links("//p[@class='list_pager']").regex("http://apk\\.zhuannet\\.com/search/\\?.*").all();
				
				url1.addAll(url2);
				url1.addAll(url3);
				
				//remove the duplicate urls in list
				HashSet<String> urlSet = new HashSet<String>(url1);
				
				//add the urls to page
				Iterator<String> it = urlSet.iterator();
				while(it.hasNext()){
					page.addTargetRequest(it.next());
				}
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://apk\\.zhuannet\\.com/soft.*").match() || page.getUrl().regex("http://apk\\.zhuannet\\.com/game.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='detail2_1_left']/h1/text()").toString();	
//			if(nameString.contains("v"))
//			{
//				appName =nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//			}
//			else {
//				appName = nameString;
//				appVersion= null;
//			}
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='detail2_3']/a/@href").toString();
//			
//			String osPlatformString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[3]/a/text()").toString();
//				osPlatform = osPlatformString;
//			
//			String sizeString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[5]/text()").toString();
//				appSize = sizeString;
//			
//			String updatedateString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[4]/text()").toString();
//				appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType =typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = page.getHtml().xpath("//div[@class='detail2_1_left']/dl/dd[6]/text()").toString();
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
			
			return Zhuannet_Detail.getApkDetail(page);
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
