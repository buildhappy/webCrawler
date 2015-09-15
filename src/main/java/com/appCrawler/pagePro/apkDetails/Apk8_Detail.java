package com.appCrawler.pagePro.apkDetails;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
/**
 * 安卓网apk8 http://www.apk8.com/
 * Apk8  #71 
 * @author DMT
 */
public class Apk8_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Apk8_Detail.class);
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
		
		// 获取dom对象
        Html html = page.getHtml();

        if(page.getUrl().regex("http://www\\.apk8\\.com/game/.+").match()
    			|| page.getUrl().regex("http://www\\.apk8\\.com/soft/.+").match()
    			|| page.getUrl().regex("http://www\\.apk8\\.com/hdyx/.+").match()
    			|| page.getUrl().regex("http://www\\.apk8\\.com/baobao/.+").match()){
        // 找出对应需要信息
         appDetailUrl = page.getUrl().toString();
         appName = html.xpath("//div[@class='iconD tableborder']/div[@class='tit_b']/text()").toString();
         appVersion = html.xpath("//ol[@class='feileis'][1]/li[1]/text()").get();
         appDownloadUrl = html.xpath("//a[@class='bt_bd']/@href").get();
         osPlatform = html.xpath("//ol[@class='feileis'][2]/li[1]/text()").toString();
         appSize = html.xpath("//ol[@class='feileis'][3]/li[1]/text()").get();
         appUpdateDate = html.xpath("//ol[@class='feileis2']/li/text()").get();
         appVenderName = html.xpath("//ol[@class='feileis'][3]/li[2]/text()").toString();
         String urlid = appDetailUrl.substring(appDetailUrl.indexOf("_")+1,appDetailUrl.indexOf("html")-1);
         appDownloadedTime = getUrlContent(urlid);
         appDescription = usefulInfo(html.xpath("//div[@class='movie_list_1'][3]").toString());
         appType = "apk";
         appScrenshot = html.xpath("//tr[@class='portal-item-screenshots']//td//img/@src").all();
         appTag = html.xpath("//div[@class='detailsleft']/ol[5]/li[1]//a/text()").all().toString();
         appCategory = html.xpath("//div[@class='game_info_location']/a[3]/text()").toString();
        }
        else  if(page.getUrl().regex("http://www\\.apk8\\.com/paper/.+").match()
    			|| page.getUrl().regex("http://www\\.apk8\\.com/theme/.+").match()){
        	appDetailUrl = page.getUrl().toString();
        	appName = html.xpath("//div[@class='game_info_location']/a[3]/text()").toString();
        	appVersion = html.xpath("html/body/div[3]/div[3]/div[3]/div[3]/div[1]/table[2]/tbody/tr[1]/td[1]/span/text()").toString();
        	appUpdateDate = html.xpath("/html/body/div[3]/div[3]/div[3]/div[3]/div[1]/table[2]/tbody/tr[1]/td[2]/span/text()").toString();
        	appSize = html.xpath("/html/body/div[3]/div[3]/div[3]/div[3]/div[1]/table[2]/tbody/tr[3]/td[1]/span/text()").toString();
        	osPlatform = html.xpath("/html/body/div[3]/div[3]/div[3]/div[3]/div[1]/table[2]/tbody/tr[3]/td[2]/span/text()").toString();
        	appDownloadUrl = html.xpath("//a[@class='bt_bd']/@href").get();
        	appCategory = html.xpath("//div[@class='game_info_location']/a[3]/text()").toString();
        	appScrenshot = html.xpath("//tr[@class='portal-item-screenshots']//img/@src").all();
        	String urlid = appDetailUrl.substring(appDetailUrl.indexOf("_")+1,appDetailUrl.indexOf("html")-1);
        	appDownloadedTime = getUrlContent(urlid);
        	
        }
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
		
		return apk;
	}
	
	private static String getUrlContent(String urlid) {
		String lines = "";
		String sourcefile="";
		try {
			//打开一个网址，获取源文件			
			URL url=new URL("http://www.apk8.com/getpl.php?act=down&game_id="+urlid);		
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			 while ((lines = reader.readLine()) != null){
				 	sourcefile=sourcefile+lines;
				 	
				}
						
		} catch (Exception e) {
		}
		return sourcefile;
		
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
