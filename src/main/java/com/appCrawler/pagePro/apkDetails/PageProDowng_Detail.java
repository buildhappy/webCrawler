package com.appCrawler.pagePro.apkDetails;
/**
 * #150 绿色软件站 app搜索抓取
 * url:http://search.downg.com/search.asp?action=s&sType=ResName&catalog=&keyword=%CA%D6%BB%FAQQ&Submit=%CB%D1%CB%F7
 *
 * @author DMT
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class PageProDowng_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProDowng_Detail.class);
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
		
		 osPlatform = html.xpath("//li[@class='span2']/text()").toString();
         if(!osPlatform.contains("Android")) return null;
         else osPlatform = osPlatform.substring(5,osPlatform.length());
     
         appDetailUrl = page.getUrl().toString();
         String nameString = html.xpath("//div[@class='cp software-info']/div[@class='cp-top']/h3/text()").toString();        
         if(nameString != null && nameString.contains("V"))
			{
				appName=nameString.substring(0,nameString.indexOf("V")-1);
				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
			}
			else if(nameString != null && nameString.contains("v"))
			{
				appName=nameString.substring(0,nameString.indexOf("v")-1);
				appVersion = nameString.substring(nameString.indexOf("v")+1,nameString.length());
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
         
         appDownloadUrl = html.xpath("//ul[@class='download-list']/li[3]/a/@href").get();
         
        
         
         appSize = StringUtils.substringAfterLast(html.xpath("//ul[@class='clearfix software-infolist']/li[4]/text()").get(), "：");
         appUpdateDate = StringUtils.substringAfterLast(html.xpath("//ul[@class='clearfix software-infolist']/li[2]/text()").get(), "：");
         appDownloadedTime = null;
         appDescription = html.xpath("//div[@class='cp software-desc']/div[@class='cp-main']/p[2]/font/text()").get();
         appType = null != appDownloadUrl ? StringUtils.substringAfterLast(appDownloadUrl, ".") : null;
         appScrenshot = html.xpath("//div[@class='cp software-desc']//img/@src").all();
         appTag = html.xpath("//div[@class='cp software-desc']//a/text()").all().toString();
          
//		System.out.println("appName="+appName);
//		System.out.println("appDetailUrl="+appDetailUrl);
//		System.out.println("appDownloadUrl="+appDownloadUrl);
//		System.out.println("osPlatform="+osPlatform);
//		System.out.println("appVersion="+appVersion);
//		System.out.println("appSize="+appSize);
//		System.out.println("appUpdateDate="+appUpdateDate);
//		System.out.println("appType="+appType);
//		System.out.println("appVenderName="+appVenderName);
//		System.out.println("appDownloadedTime="+appDownloadedTime);
//		System.out.println("appDescription="+appDescription);
//		System.out.println("appTag="+appTag);
//		System.out.println("appScrenshot="+appScrenshot);
//		System.out.println("appCategory="+appCategory);

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
	
}
