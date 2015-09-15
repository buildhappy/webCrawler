package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Www7xz_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 7匣子 http://www.7xz.com
 * Www7xz #135
 * @author DMT
 */
public class Www7xz implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Www7xz.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Www7xz.process()" + page.getUrl());
		//index page				http://www.7xz.com/ng/search_qq_0_0_rate_1.html
		if(page.getUrl().regex("http://www\\.7xz\\.com/ng/search_.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@class='classes-list clearfix js-hover']").regex("http://www\\.7xz\\.com/.*/").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='Pagebox']").regex("http://www\\.7xz\\.com/ng/search_.*").all();
			
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
		else if(page.getUrl().regex("http://www\\.7xz\\.com/.*/").match()){
			//System.out.println(page.getHtml().toString());
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
//			String nameString=page.getHtml().xpath("//div[@class='col-md-9 clearfix']/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//p[@class='sprit down_android']/a/@href").toString();
//			
//			String allinfoString=page.getHtml().xpath("//table").toString();
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//					
//
//
//			String platFormString =allinfoString.substring(allinfoString.indexOf("游戏平台")+5,allinfoString.indexOf("发行商")-1);
//				osPlatform = platFormString.replace("\n", "").replace(" ", "");
//		
//			String versionString = allinfoString.substring(allinfoString.indexOf("游戏版本")+5,allinfoString.length());
//				appVersion = versionString.replace("\n", "").replace(" ", "");
//			
//			String sizeString = allinfoString.substring(allinfoString.indexOf("游戏大小")+5,allinfoString.indexOf("游戏版本")-1);
//				appSize = sizeString.replace("\n", "").replace(" ", "");
//					
//			appType = "apk";
//			
//			String venderString = allinfoString.substring(allinfoString.indexOf("发行商")+4,allinfoString.indexOf("玩家")-1);
//				appvender=venderString.replace("\n", "").replace(" ", "");
//			
//			String descriptionString = page.getHtml().xpath("//p[@class='p_detail p_l_r10']/text()").toString();
//				appDescription = descriptionString;	
//			
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
//			logger.info("return from Www7xz.process()");
			return Www7xz_Detail.getApkDetail(page);
		}
		logger.info("return from Www7xz.process()");
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
