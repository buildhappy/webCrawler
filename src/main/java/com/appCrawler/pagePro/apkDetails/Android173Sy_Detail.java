package com.appCrawler.pagePro.apkDetails;
/**
 * #169
 * 手游世界http://android.173sy.com/
 * 可以通过伪造下载链接来构造下载链接
 * http://android.173sy.com/download/downloadapk.php?id=13425&s=1
 * 将id后的参数修改成相应的apk的id就好
 * @author DMT
 *
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class Android173Sy_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Android173Sy_Detail.class);
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
		
		String rawVersion = page.getHtml().xpath("//p[@class='cf51e7e lh20 mt_5']/text()").toString();
		String rawId = page.getHtml().xpath("//p[@class='dbtn_download']/@onclick").get();
		String id = null;
		if((rawId != null) && (rawVersion != null)){
			id = rawId.split("\\(")[1].split(",")[0];
			appDownloadUrl = "http://android.173sy.com/download/downloadapk.php?id="+ id + "&s=1";
			appVersion = rawVersion.split("： ")[1].trim();
		}
		appName = page.getHtml().xpath("//div[@class='gi_t_title']/text()").toString();
		appName = page.getHtml().xpath("//p[@class='dl_ititle']/text()").toString();
		appDetailUrl = page.getUrl().toString();
		List<String> infos = page.getHtml().xpath("//div[@class='fc mt_10']/p/text()").all();
		appSize = infos.get(0).split("：")[1];
		appUpdateDate = infos.get(1).split("：")[1];
		appDownloadedTime = page.getHtml().xpath("//p[@class='pbox']/span/text()").toString();
		
		appVenderName = page.getHtml().xpath("//div[@class='gi_t_sing l tal']/p[2]/text()").toString();
		if(appVenderName != null && appVenderName.contains("："))
			appVenderName = appVenderName.substring(appVenderName.indexOf("：")+1,appVenderName.length());
	
		appCategory = page.getHtml().xpath("//div[@class='gi_t_sing l tal']/p[1]/text()").toString();
		if(appCategory != null && appCategory.contains("："))
			appCategory = appCategory.substring(appCategory.indexOf("："),appCategory.length());
		
		appDescription = usefulInfo(page.getHtml().xpath("//div[@class='content tal']").toString());
		appScrenshot = page.getHtml().xpath("//div[@class='c_imgbox']//img/@src").all();
		
		
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
