package com.appCrawler.pagePro.apkDetails;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
/**
 * 格子啦 http://www.gezila.com/
 * Gezila #76
 * (1)在搜索结果中有的会有一个专区跳转页面，只有通过这个专区介绍的才能跳转到软件的具体信息页面，因此，这个里面添加了一个
 * 专区介绍页面的分析，并从中获取搜索结果  比如搜索“节奏大师”这个关键字
 * http://www.gezila.com/search?t=android&q=%E8%8A%82%E5%A5%8F%E5%A4%A7%E5%B8%88
 * (2)在app detail page中，还有同一apk的其他版本的下载，这样会在一个页面有同一个apk的多个不同版本的下载，这些多个版本的apk除了版本不同外其他都相同
 * @author DMT
 */

public class Gezila_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Gezila_Detail.class);
	public static List<Apk> getApkDetail(Page page){
		List<Apk> apks = new LinkedList<Apk>();
		Apk apk = null;
		String appName = null;				//app名字
		String appDetailUrl = null;			//具体页面url
		String appDownloadUrl = null;		//app下载地址
		String osPlatform = null ;			//运行平台
		String appVersion = null;			//app版本
		String appSize = null;				//app大小
		String appUpdateDate = null;		//更新日期
		String appType = null;				//下载的文件类型 apk？zip？rar？
		String appVenderName = null;			//app开发者  APK这个类中尚未添加
		String appDownloadedTime=null;		//app的下载次数
		String appDescription = null;		//app的详细描述
		List<String> appScrenshot = null;			//app的屏幕截图
		String appTag = null;				//app的应用标签
		String appCategory = null;			//app的应用类别 
		
		String nameString=page.getHtml().xpath("//div[@class='head1']/h1/text()").toString();
		if(nameString != null && nameString.contains("版本"))
		{
			appName=nameString.substring(0,nameString.indexOf("版本")-1);						
			appVersion = nameString.substring(nameString.indexOf("版本")+2,nameString.length());
		}
		else if(nameString != null && nameString.contains("V"))
		{
			appName=nameString.substring(0,nameString.indexOf("V")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("v"))
		{
			appName=nameString.substring(0,nameString.indexOf("v")-1);
			appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
		}
		else if(nameString != null && nameString.contains("."))
		{
			appName=nameString.substring(0,nameString.indexOf(".")-1);
			appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
		}
		else 
		{
			appName = nameString;
			appVersion = null;
		}
		if(appName == null)  return null;
			
		appDetailUrl = page.getUrl().toString();
		
		appDownloadUrl = page.getHtml().xpath("//div[@class='c_down_info c_down_android']/div[3]/div[2]/ul/li[1]/a/@href").toString();
		
		osPlatform = page.getHtml().xpath("//span[@id='view_yqiu']//i/text()").toString();
		
		
		String sizeString = page.getHtml().xpath("//span[@id='view_size']/i/text()").toString();
			appSize = sizeString;
		
		String updatedateString = page.getHtml().xpath("//span[@id='view_time']/i/text()").toString();
			appUpdateDate = updatedateString;
		
		String typeString = "apk";
			appType =typeString;
		
		appVenderName=null;
			
		String DownloadedTimeString = null;
			appDownloadedTime = DownloadedTimeString;	
		
		appDescription = usefulInfo(page.getHtml().xpath("//div[@id='detailed']").toString()).trim();
		appScrenshot = page.getHtml().xpath("//a[@class='image']/@devsrc").all();
		appCategory = page.getHtml().xpath("//div[@class='location']/a[5]/text()").toString();
		appTag = page.getHtml().xpath("//p[@class='biaoqian']//a/text()").all().toString();
		
		System.out.println("appName="+appName);
		System.out.println("appDetailUrl="+appDetailUrl);
		System.out.println("appDownloadUrl="+appDownloadUrl);
		System.out.println("osPlatform="+osPlatform);
		System.out.println("appVersion="+appVersion);
		System.out.println("appSize="+appSize);
		System.out.println("appUpdateDate="+appUpdateDate);
		System.out.println("appType="+appType);
		System.out.println("appVenderName="+appVenderName);
		System.out.println("appDownloadedTime="+appDownloadedTime);
		System.out.println("appDescription="+appDescription);
		System.out.println("appTag="+appTag);
		System.out.println("appScrenshot="+appScrenshot);
		System.out.println("appCategory="+appCategory);

		if(appName != null && appDownloadUrl != null){
			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			Apk(String appName,String appMetaUrl,String appDownloadUrl,String osPlatform ,
//					String appVersion,String appSize,String appTsChannel, String appType,String cookie){	
			apk.setAppDownloadTimes(appDownloadedTime);
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);
						
		}
		addOtherVersion(page, apks);
		
		return apks;
	
	
	
	}
	
	private static void  addOtherVersion(Page page,List<Apk> apks) {
		String apknumString = page.getHtml().xpath("//div[@id='old_url']").toString();
		if(apknumString != null)
		{
		int apknum=0;
		for(int i=apknumString.indexOf("<li>");i<=apknumString.lastIndexOf("<li>") && i!= -1;apknum++,i=apknumString.indexOf("<li>", i+4));
//		System.out.println("apknum="+apknum);  //apknum保存其他版本的apk的个数	
		for(int j=1;j<apknum+1;j++)
		{
			//<li><a onclick="detail_down(1887,'android');_hmt.push(['_trackEvent','android','download']);"href="http://xl1.dl.gezila.com:1010/android/887/pp_android1.81.apk" target="_blank" class="xiazai">
//			</a><a onclick="detail_down(1887,'android');
//			_hmt.push(['_trackEvent','android','download']);" "
//					+ "href="http://xl1.dl.gezila.com:1010/android/887/pp_android1.81.apk" 
//						target="_blank">PP助手 V1.81</a><i>1.88MB</i><span>2014-05-14</span> </li>
			Apk apk1=null;
			String pathString="//div[@id='old_url']/ul"+"/li["+j+"]";
			String nametempString= page.getHtml().xpath(pathString+"/a[2]/text()").toString();
			String appName1 = null;				//app名字
			String appVersion1 = null;			//app版本
			if(nametempString != null && nametempString.contains("V"))
			{
				appName1=nametempString.substring(0,nametempString.indexOf("V")-1);
				appVersion1 = nametempString.substring(nametempString.indexOf("V")+1,nametempString.length());
			}
			else if(nametempString != null && nametempString.contains("v"))
			{
				appName1=nametempString.substring(0,nametempString.indexOf("v")-1);
				appVersion1 = nametempString.substring(nametempString.indexOf("V")+1,nametempString.length());
			}
			
			String appDetailUrl1 = page.getUrl().toString();			//具体页面url
			String appDownloadUrl1 = page.getHtml().xpath(pathString+"/a[2]/@href").toString();;		//app下载地址
			String osPlatform1 = null ;			//运行平台
			
			String appSize1 = page.getHtml().xpath(pathString+"/i/text()").toString();				//app大小
			String appUpdateDate1 = page.getHtml().xpath(pathString+"/span/text()").toString();;		//更新日期
			String appType1 = "apk";				//下载的文件类型 apk？zip？rar？ipa?
			
			apk1 = new Apk(appName1,appDetailUrl1,appDownloadUrl1,osPlatform1 ,appVersion1,appSize1,appUpdateDate1,appType1,null);
			apks.add(apk1);
			
		}
		
		}
		
		
	}
	
	private static String usefulInfo(String allinfoString)
	{
		String info = null;
		while(allinfoString.contains("<"))
			if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
			else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
			else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
		info = allinfoString.replace("\n", "").replace(" ", "");
		return info;
	}
	
}
