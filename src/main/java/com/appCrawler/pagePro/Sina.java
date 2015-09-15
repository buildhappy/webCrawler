package com.appCrawler.pagePro;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Sina_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 新浪 http://app.sina.com.cn/app_index.php?f=p_dh
 * Sina #98
 * @author DMT
 */
public class Sina implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://app.sina.com.cn/search.php?q=qq&f=p_dh
		if(page.getUrl().regex("http://app\\.sina\\.com\\.cn/search\\.php\\?.*").match()){
//			System.out.println(page.getHtml().toString());
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='listArea']/ul").regex("http://app\\.sina\\.com\\.cn/appdetail\\.php.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='listArea']/div").regex("http://app\\.sina\\.com\\.cn/search\\.php\\?.*").all();
			
			List<String> url3 = new LinkedList<String>();
			for(String temp:url2)
			{
				try {
					String str = null;
					//获取url中的中文字符并替换为相应的url编码
					str = temp.replaceAll("[^\\u4e00-\\u9fa5]", "");			//获取url中的中文字符	\
					temp=temp.replaceFirst(str, URLEncoder.encode(str,"utf-8"));	//替换					
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
		if(page.getUrl().regex("http://app\\.sina\\.com\\.cn/appdetail\\.php.*").match()){
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
//			String nameString=page.getHtml().xpath("//div[@class='appView']/div[1]/h1/text()").toString();			
//				appName =nameString;
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='avAction']/a[1]/@href").toString();
//			
//			for(int i=1;i<5;i++)
//			{
//				String tempString = page.getHtml().xpath("//ul[@class='avInfoList']/li["+i+"]/text()").toString();
//				if(tempString == null) continue;
//				if(tempString.contains("大小"))
//					appSize = tempString.substring(tempString.indexOf("：")+1,tempString.length());
//				else if(tempString.contains("版本"))
//					appVersion = tempString.substring(tempString.indexOf("：")+1,tempString.length());
//				else if(tempString.contains("更新时间"))
//					appUpdateDate = tempString.substring(tempString.indexOf("：")+1,tempString.length());
//				else if(tempString.contains("系统要求"))
//					osPlatform = tempString.substring(tempString.indexOf("：")+1,tempString.length());
//			}
//			
//			String typeString = "apk";
//				appType =typeString;
//				
//			String venderString = page.getHtml().xpath("//div[@class='avOrigin']/text()").toString();
//				appvender= venderString.substring(venderString.indexOf("：")+1,venderString.length());
//				
//			String DownloadedTimeString = page.getHtml().xpath("//span[@class='downValue']/text()").toString();
//				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("(")+1,DownloadedTimeString.indexOf(")"));		
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
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Sina_Detail.getApkDetail(page);
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
