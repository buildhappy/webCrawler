package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Srui_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 新锐下载 http://www.srui.cn/
 * Srui #80
 * (1)页面的标签里面没有可用的属性可供提取字段，如果使用绝对路径的话有些页面的标签不完整，
 *    提取信息会出现错误，因此使用获取页面的html然后剔除标签的方法提取字段信息
 * (2)名字和版本号在一个字段里，不同的页面标签有细微的差异，需要判断。需要分割，而且分割需要详细分类
 * 
 * @author DMT
 */
public class Srui implements PageProcessor{
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://so.srui.cn/cse/search?s=5754943238134558878&q=%BB%AA%CB%B6
		if(page.getUrl().regex("http://so\\.srui\\.cn/cse/search\\?.*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@id='center']").regex("http://www\\.srui\\.cn/soft/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pager clearfix']").regex("http://so\\.srui\\.cn/cse/search\\?.*").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		if(page.getUrl().regex("http://www\\.srui\\.cn/soft/.*").match()){
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
//
//			String allinfoString = page.getHtml().toString();
//			while(allinfoString.contains("<"))
//				if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//				else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
//				else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
//			osPlatform =allinfoString.substring(allinfoString.indexOf("运行环境")+4,allinfoString.indexOf("软件等级"));
//
//			if(osPlatform.contains("Android") == false && osPlatform.contains("android") == false)
//				return null;
//			osPlatform = osPlatform.replace("\n", "");
//			osPlatform = osPlatform.replace(" ", "");
//			//名字和版本号
//			String nameString=page.getHtml().xpath("//td[@colspan='3']/h1/text()").toString();		
//			if(nameString == null) 
//				nameString=page.getHtml().xpath("//td[@colspan='3']/strong/text()").toString();	
//			if(nameString != null && nameString.contains("|"))
//			{
//				appName=nameString.substring(0,nameString.indexOf("|")-1);
//				appVersion = null;
//			}
//			else if(nameString != null && nameString.contains(")"))
//			{
//				appName=nameString.substring(0,nameString.indexOf(")")+1);
//				appVersion = nameString.substring(nameString.indexOf(")")+1,nameString.length());
//			}
//			else if(nameString != null && nameString.contains("V"))
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
//			else 
//			{
//				appName = nameString;
//				appVersion = null;
//			}
//
//			//allinfoString = allinfoString.replace("\n", "");
//			//allinfoString = allinfoString.replace(" ", "");
//			appDetailUrl = page.getUrl().toString();
//			
//			String downloadurlString = page.getHtml().toString().substring(0,page.getHtml().toString().lastIndexOf("相关软件"));
//				downloadurlString = downloadurlString.substring(downloadurlString.lastIndexOf("免费下载"));	
//				if(downloadurlString.contains("apk")) downloadurlString=downloadurlString.substring(0,downloadurlString.lastIndexOf("apk")+3);
//			appDownloadUrl = downloadurlString.substring(downloadurlString.lastIndexOf("http"));
//			if(appDownloadUrl.contains(">")) 
//				appDownloadUrl = appDownloadUrl.substring(0, appDownloadUrl.indexOf(">")-1);
//					
//			String sizeString = allinfoString.substring(allinfoString.indexOf("软件大小")+4,allinfoString.indexOf("授权方式"));
//			sizeString = sizeString.replace("\n", "");			
//			appSize = sizeString.replace(" ", "");
//			
//			String updatedateString = null;
//			if(allinfoString.indexOf("解压密码") < allinfoString.indexOf("推荐"))
//				 updatedateString = allinfoString.substring(allinfoString.indexOf("更新时间")+4,allinfoString.indexOf("解压密码"));
//			else updatedateString = allinfoString.substring(allinfoString.indexOf("更新时间")+4,allinfoString.indexOf("推荐"));
//			updatedateString = updatedateString.replace("\n", "");
//			while(updatedateString.indexOf(" ") == 0) updatedateString= updatedateString.substring(1);
//			while(updatedateString.lastIndexOf(" ") == updatedateString.length()-1) updatedateString= updatedateString.substring(0,updatedateString.length()-1);
//			
//			appUpdateDate = updatedateString;
//			
//			String typeString = "apk";
//				appType = typeString;
//			
//			appvender=null;
//				
//			String DownloadedTimeString = null;
//				appDownloadedTime = DownloadedTimeString;
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
			
			return Srui_Detail.getApkDetail(page);
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
