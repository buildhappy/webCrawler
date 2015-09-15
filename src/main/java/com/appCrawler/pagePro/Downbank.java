package com.appCrawler.pagePro;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Downbank_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 下载银行 http://www.downbank.cn
 * Downbank #132
 * (1)下载链接有防盗链设置
 * @author DMT
 */
public class Downbank implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Downbank.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in Downbank.process()" + page.getUrl());
		//index page	http://www.downbank.cn/search.asp?word=%CA%D6%BB%FA%B9%D9%BC%D2&m=2&searchbtn2=%BF%AA%CA%BC%CB%D1%CB%F7			
		if(page.getUrl().regex("http://www\\.downbank\\.cn/search\\.asp\\?.*").match()){
			//app的具体介绍页面											[0-9]+\\.html
		//	List<String> url1 = page.getHtml().links("//div[@id='searchmain']").regex("http://www\\.downbank\\.cn/[0-9]+\\..*").all();
			List<String> url1 = page.getHtml().links("//div[@id='searchmain']").regex("http://www\\.downbank\\.cn/.*").all();
			List<String> url4 = new LinkedList<String>();
			for(String temp:url1){
				try {
					if(!temp.contains("_"))
						url4.add(temp);		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//p[@class='list_page']").regex("http://www\\.downbank\\.cn/search\\.asp\\?.*").all();
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
			
			url4.addAll(url3);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url4);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		else if(page.getUrl().regex("http://www\\.downbank\\.cn/.*").match()){
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
//			osPlatform = page.getHtml().xpath("//div[@id='soft_name']/ul/li[4]/span/text()").toString();
//			if(!osPlatform.contains("android")){
//				logger.info("return from Downbank.process()");
//				return null;
//			}
//			String nameString=page.getHtml().xpath("//div[@id='soft_name']/h1/label/b/text()").toString();		
//			if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("."))
//			{
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//			}
//			else{
//				appName = nameString;
//				appVersion = null;
//			}
//				
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@id='soft_down']/ul/a[2]/@href").toString();
//		
//			appSize = page.getHtml().xpath("//div[@id='soft_name']/ul/li[1]/span/text()").toString();
//			
//			appUpdateDate = page.getHtml().xpath("//div[@id='soft_name']/ul/li[8]/span/text()").toString();
//			
//			appType = "rar";
//			
//			String descriptionString = page.getHtml().xpath("//div[@id='soft_intro']").toString();
//			String allinfoString = descriptionString;
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			
//			appDescription = allinfoString.replace("\n", "");
//			
//			/*
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
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			logger.info("return from Downbank.process()");
			return Downbank_Detail.getApkDetail(page);
		}
		logger.info("return from Downbank.process()");
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

