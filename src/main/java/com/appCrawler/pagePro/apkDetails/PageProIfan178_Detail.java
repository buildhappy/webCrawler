package com.appCrawler.pagePro.apkDetails;
/**
 * 苹果资讯 app搜索抓取
 * #146
 *  @author DMT
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class PageProIfan178_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProIfan178_Detail.class);
	public static Apk getApkDetail(Page page){
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
		 Html html = page.getHtml();

         // 找出对应需要信息
          appDetailUrl = page.getUrl().toString();
          appName = html.xpath("//div[@class='page-page']/div[@class='t1']/h1/text()").toString();         
          if (StringUtils.isEmpty(appName)) {
             appName = html.xpath("//div[@class='box-dw-l-t']/h1/strong/text()").get();
         }
          if(appName == null)  return null;
          appVersion = null;
          appDownloadUrl = html.xpath("//div[@class='clearfix t2']/a[2]/@href").get();
         if (StringUtils.isEmpty(appDownloadUrl)) {
             appDownloadUrl = html.xpath("//div[@class='dw-btn']/a[@class='dw-btn2']/@href").get();
         }
          osPlatform = null;
          appSize = StringUtils.substringAfterLast(html.xpath("//div[@class='txt']/div[@class='clearfix inf']/p[1]/text()").get(), "：");
          appUpdateDate = html.xpath("//div[@class='page-page']/div/p[1]/text()").toString();
          
          appVenderName = html.xpath("//div[@class='page-page']/div/p[2]/text()").toString();
          if(appVenderName!= null && appVenderName.contains("：") )
        	  appVenderName = appVenderName.substring(appVenderName.indexOf("：")+1,appVenderName.length());
          
          appDownloadedTime = null;
          appDescription = html.xpath("//div[@class='tx']/div/p/text()").toString();
          appType = null;
          appScrenshot = html.xpath("//div[@class='scroll-img']//img/@src").all();
          
          appCategory = html.xpath("//div[@class='clearfix inf']/p[2]/text()").toString();
          if(appCategory == null || !appCategory.contains("安卓"))  return null;
          if(appCategory!= null && appCategory.contains("类型") && appCategory.contains("系统"))
        	  appCategory = appCategory.substring(appCategory.indexOf("类型")+3,appCategory.indexOf("系统")-1);
          
          
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
		else return null;
		
		return apk;
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
