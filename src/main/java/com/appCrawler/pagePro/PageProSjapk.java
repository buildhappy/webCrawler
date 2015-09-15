package com.appCrawler.pagePro;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 卓乐 http://www.sjapk.com/
 * Sjapk #93
 * (1)the app detail page的url的有大小写不同的两个，即html和Html，需要去重
 * (2)翻页的url中的中文字符需要转码
 * @author DMT
 */
public class PageProSjapk implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page			http://www.sjapk.com/Search.asp?SmallID=1&Key=%CA%D6%BB%FAqq&typeid=1&page=1	
		if(page.getUrl().regex("http://www\\.sjapk\\.com/Search\\.asp\\?.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='main_down']").regex("http://www\\.sjapk\\.com/[0-9].*").all();

			//添加下一页url(翻页)
			List<String> url2 = new LinkedList<String>();
			if(url1.isEmpty() == false){
				url2 = page.getHtml().links("//div[@class='bottom_down']").regex("http://www\\.sjapk\\.com/Search\\.asp\\?.*").all();
			}
			List<String> url3 = new LinkedList<String>();
			for(String temp:url2){
				try {
					String str = null;
					//获取url中的中文字符并替换为相应的url编码
					str = temp.replaceAll("[^\\u4e00-\\u9fa5]", "");			//获取url中的中文字符	
					//System.out.println("str:" + str);
					temp=temp.replaceFirst(str, URLEncoder.encode(str,"gb2312"));	//替换
					//System.out.println("temp:" + temp);
					
					url3.add(temp);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			url1.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.sjapk\\.com/[0-9].*").match()){
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			
			String nameString=page.getHtml().xpath("//div[@class='main_r_f']/h1/text()").toString();			
				appName =nameString;
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//div[@class='main_r_xiazai5']/a/@href").toString();
			
			osPlatform = page.getHtml().xpath("//div[@class='main_r_f']/p[4]/font[1]/text()").toString();
			
			String versionString = page.getHtml().xpath("//div[@class='main_r_f']/p[4]/font[2]/text()").toString();
				appVersion = versionString;
			
			String sizeString = page.getHtml().xpath("//div[@class='main_r_xiazai3']/div[1]/p[1]/font[1]/text()").toString();
				appSize = sizeString;
			
			String updatedateString = page.getHtml().xpath("//div[@class='main_r_xiazai3']/div[1]/p[1]/font[2]/text()").toString();
				appUpdateDate = updatedateString;
			
			String typeString = "apk";
				appType =typeString;
			
			appvender=null;
				
			String DownloadedTimeString = null;
				appDownloadedTime = DownloadedTimeString;		
			/*
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appvender);
			System.out.println("appDownloadedTime="+appDownloadedTime);
			*/
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
			}
			
			return apk;
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
