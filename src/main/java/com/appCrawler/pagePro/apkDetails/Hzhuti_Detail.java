package com.appCrawler.pagePro.apkDetails;
/**
 * 好主题 http://www.hzhuti.com/
 * Hzhuti #92
 * (1)2015/5/5 获取apk下载链接时需要打开url，需要设置代理，不然无法打开
 * @author DMT
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.slf4j.LoggerFactory;



import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;


public class Hzhuti_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Hzhuti_Detail.class);
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

		String nameString=page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/h3/text()").toString();			
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
			
		appDetailUrl = page.getUrl().toString();
		
		String downloadUrlString = page.getHtml().xpath("//div[@class='article_txt']/p[1]/a/@href").toString();
		if(downloadUrlString == null )
		{
			
			return null;
		}
		String sourcefile=null;
		 URL url;
		 String l = null;
		 StringBuffer bs=null;
		try {
			url = new URL(downloadUrlString);
			 HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
			 urlcon.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	           urlcon.connect();         //获取连接
	           InputStream is = urlcon.getInputStream();
	           BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
	            bs = new StringBuffer();		           
	           while((l=buffer.readLine())!=null){
	               bs.append(l).append("/n");
	           }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
		sourcefile = bs.toString();
		if(sourcefile == null )
		{
			System.out.println("can't not find the appDownloadUrl,return from Hzhuti.process()");
			return null;
		}
		
		appDownloadUrl="www.hzhuti.com/"+sourcefile.substring(sourcefile.indexOf("plus/download.php"),sourcefile.indexOf("downbtn")-10);
		
		String platformString =page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[7]/text()").toString();
			osPlatform = platformString.substring(platformString.indexOf("：")+1,platformString.length());
		
		String sizeString = page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[1]/text()").toString();
			appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
		
		String updatedateString = page.getHtml().xpath("//div[@class='article game_article']/div[2]/div[1]/p[4]/text()").toString();
			appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
		
		String typeString = "apk";
			appType =typeString;
		
			appVenderName=null;
			
		String DownloadedTimeString = null;
			appDownloadedTime = DownloadedTimeString;	
			
		appDescription = page.getHtml().xpath("//div[@class='article_banner']/p[1]/text()").toString();
		appScrenshot = page.getHtml().xpath("//div[@class='drama-poster']//img/@src").all();
		appTag = page.getHtml().xpath("//p[@class='l_p3']/a/text()").toString();
		appCategory = page.getHtml().xpath("//div[@class='daohan']/span/a[3]/text()").toString();
		
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
}
