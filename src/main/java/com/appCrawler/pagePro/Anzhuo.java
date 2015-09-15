package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Anzhuo_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 安卓网_anzhuo http://www.anzhuo.com/
 * #72 Anzhuo
 * (1)此网站在搜索apk的结果中，翻页中有关键字，如果是中文关键字需要进行转码方能进行正确的翻页
 * (2)获取此网站的apk的下载次数时，是一个动态获取的过程，直接在网站里面查看到的代码是经过解析的，需要到源文件里面查看，下载次数是在这个标签<span id=span_hit>里动态获取的
 * (3)此网站的apk的名字命名和版本命名都不规范，有以下四种 1)name版本：xx.xx 2)nameVx.x.x 3)namevx.x.x 4)namex.x.x (5)name
 * @author DMT
 */
public class Anzhuo implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(3).setSleepTime(0);
	@Override
	public Apk process(Page page) {
		//index page				
		if(page.getUrl().regex("http://s\\.anzhuo\\.com/search.php\\?*").match()){
			//app的具体介绍页面	
			List<String> url1 = page.getHtml().links("//div[@class='mbox']").regex("http://www\\.anzhuo\\.com/app.*").all();
			
			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pagelist']").regex("http://s\\.anzhuo\\.com/search.php\\?.*").all();
			
			List<String> url3 = new LinkedList<String>();

			for(String temp:url2)
			{
				try {
					String str = null;
					//获取url中的中文字符并替换为相应的url编码
					str = temp.replaceAll("[^\\u4e00-\\u9fa5]", "");					
					temp=temp.replaceFirst(str, URLEncoder.encode(str,"gb2312"));
					
					url3.add(temp);				
				} catch (UnsupportedEncodingException e) {
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
		if(page.getUrl().regex("http://www\\.anzhuo\\.com/app.*").match()){
//			List<Apk> apps = new LinkedList<Apk>();
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
//			//有的名字里面包含版本号，有的不包含
//			String nameString=page.getHtml().xpath("//div[@class='gameIntro']/h1/text()").toString();
//			if(nameString != null && nameString.contains("版本"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("版本")-1);						
//				appVersion = nameString.substring(nameString.indexOf("版本")+2,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("V"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("V")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("v"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("v")-1);
//				appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("."))
//			{
//				appName=nameString.substring(0,nameString.indexOf(".")-1);
//				appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//			}
//			else 
//				{
//				appName = nameString;
//				appVersion = null;
//				}
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//div[@class='gameIntro']/span/a/@href").toString();
//			
//			String osPlatformString = page.getHtml().xpath("//ul[@class='gameInfo']/li[5]/text()").toString();			
//				osPlatform = osPlatformString.substring(osPlatformString.indexOf("：")+1,osPlatformString.length());
//			
//			String sizeString = page.getHtml().xpath("//ul[@class='gameInfo']/li[4]/text()").toString();	
//				appSize = sizeString.substring(sizeString.indexOf("：")+1,sizeString.length());
//			
//			String updatedateString = page.getHtml().xpath("//ul[@class='gameInfo']/li[3]/text()").toString();
//				appUpdateDate = updatedateString.substring(updatedateString.indexOf("：")+1,updatedateString.length());
//			
//			String typeString ="apk";
//				appType =typeString;
//			
//			String venderString = null;
//				appvender = venderString;
//				
//			String downloadedTimeString = page.getHtml().xpath("//span[@id='span_hit']/script/@src").toString();
//			String line=null;
//			try {
//				//打开一个网址，获取源文件，这个网址里面是一个document.write("****")
//				URL url=new URL(downloadedTimeString);
//				BufferedReader reader;
//				reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				line=reader.readLine();
//				//line=document.write('30168');
//			} catch (Exception e) {
//			}
//			if(line != null)
//				appDownloadedTime =line.substring(line.indexOf("(")+2,line.indexOf(")")-1);
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
//				apps.add(apk);
//			}			
			return Anzhuo_Detail.getApkDetail(page);
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
