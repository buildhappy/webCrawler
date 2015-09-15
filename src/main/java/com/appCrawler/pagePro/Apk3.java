package com.appCrawler.pagePro;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Apk3_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Apk3 http://www.apk3.com/
 * Apk3 #107
 * @author DMT
 */
public class Apk3 implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://www.apk3.com/search.asp?m=0&s=0&word=%B2%B6%D3%E3%B4%EF%C8%CB&x=0&y=0
		if(page.getUrl().regex("http://zhannei.baidu.com/cse/search\\?.*").match()){
			//app的具体介绍页面											
			//List<String> url1 = page.getHtml().links("//div[@class='result f s0']").regex("http://www\\.apk3\\.com/soft\\.*").all();
			List<String> url1 = page.getHtml().xpath("//div[@class='result f s0']/h3/a/@href").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//p[@class='list_page']").regex("http://www\\.apk3\\.com/search\\.asp\\?.*").all();
			
			List<String> url3 = new LinkedList<String>();
			for(String temp:url2){
				try {
					String str = null;
					//获取url中的中文字符并替换为相应的url编码
					str = temp.replaceAll("[^\\u4e00-\\u9fa5]", "");			//获取url中的中文字符		
					temp=temp.replaceFirst(str, URLEncoder.encode(str,"gb2312"));	//替换
					url3.add(temp);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			url1.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				String url = it.next();
				page.addTargetRequest(url);
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.apk3\\.com/soft.*").match()
				|| page.getUrl().regex("http://www\\.apk3\\.com/game.*").match()){
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
//			appName=page.getHtml().xpath("//div[@class='a_app_attr_tit']/h1/text()").toString();
//			appDetailUrl = page.getUrl().toString();
//			//appDownloadUrl = page.getHtml().xpath("//ul[@class='downlistbox']/li[1]/a/@href").toString();
//			//System.out.println("appName=" + appName);
//			
//			String info = page.getHtml().xpath("//div[@class='a_app_attr r']/p/text()").toString();
//			if(info != null){
//				String[] ss = info.split("：");
//				int length = ss.length;
//				
//				if(length > 0){
//					appSize = ss[1].split(" 适用")[0];
//				}
//				if(length > 1){
//					osPlatform = ss[2].split(" 语言")[0];
//				}
//				if(length > 3){
//					appUpdateDate = ss[4].split(" 类型")[0];
//				}
//			}
//
//			
//			appDownloadedTime = page.getHtml().xpath("//span[@id='arc_view']/text()").toString();
//			//System.out.println("appDownloadedTime=" + appDownloadedTime);
//			
//			appType ="apk";
//			
//			appvender=null;
//			appDownloadUrl = page.getHtml().xpath("//div[@class='w320 l a_app_downlist']/ul/li/a/@href").toString();
//			
//			/*
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appVersion="+appVersion);
//			System.out.println("appSize="+appSize);
//			System.out.println("appUpdateDate="+appUpdateDate);
//			*/
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			return Apk3_Detail.getApkDetail(page);
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
