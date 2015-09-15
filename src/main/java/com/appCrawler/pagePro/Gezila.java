package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Gezila_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 格子啦 http://www.gezila.com/
 * Gezila #76
 * (1)在搜索结果中有的会有一个专区跳转页面，只有通过这个专区介绍的才能跳转到软件的具体信息页面，因此，这个里面添加了一个
 * 专区介绍页面的分析，并从中获取搜索结果  比如搜索“节奏大师”这个关键字
 * http://www.gezila.com/search?t=android&q=%E8%8A%82%E5%A5%8F%E5%A4%A7%E5%B8%88
 * (2)在app detail page中，还有同一apk的其他版本的下载，这样会在一个页面有同一个apk的多个不同版本的下载，这些多个版本的apk除了版本不同外其他都相同
 * @author DMT
 */
public class Gezila implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		
		return null;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		
		List<Apk> apks = new LinkedList<Apk>();
		//index page				
				if(page.getUrl().regex("http://www\\.gezila\\.com/search\\?.*").match()){
					//app的具体介绍页面											
					List<String> url1 = page.getHtml().links("//div[@class='centrecontent phone_search']").regex("http://www\\.gezila\\.com/android.*").all();

					//添加搜索结果中专区页面
					List<String> url2 = page.getHtml().links("//div[@class='centrecontent phone_search']").regex("http://www\\.gezila\\.com/zonedetail.*").all();
						
					//添加下一页url(翻页)
					List<String> url3 = page.getHtml().links("//div[@class='pages']").regex("http://www\\.gezila\\.com/search\\?.*").all();
					
					
					url1.addAll(url2);
					url1.addAll(url3);
					
					//remove the duplicate urls in list
					HashSet<String> urlSet = new HashSet<String>(url1);
					
					//add the urls to page
					Iterator<String> it = urlSet.iterator();
					while(it.hasNext()){
						page.addTargetRequest(it.next());
						
					}

				}
				
				//专区介绍页面，从里面获取the app detail page
				if(page.getUrl().regex("http://www\\.gezila\\.com/zonedetail.*").match()){
					
					page.addTargetRequest(page.getHtml().xpath("//dd[@class='downloadbtn']/a/@href").toString());
					
				}
				
				
				//the app detail page
				if(page.getUrl().regex("http://www\\.gezila\\.com/android.*").match()){
//					Apk apk = null;
//					String appName = null;				//app名字
//					String appDetailUrl = null;			//具体页面url
//					String appDownloadUrl = null;		//app下载地址
//					String osPlatform = null ;			//运行平台
//					String appVersion = null;			//app版本
//					String appSize = null;				//app大小
//					String appUpdateDate = null;		//更新日期
//					String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//					String appvender = null;			//app开发者  APK这个类中尚未添加
//					String appDownloadedTime=null;		//app的下载次数
//					
//					//有的名字里面包含版本号，有的不包含
//					String nameString=page.getHtml().xpath("//div[@class='head1']/h1/text()").toString();
//					if(nameString != null && nameString.contains("版本"))
//					{
//						appName=nameString.substring(0,nameString.indexOf("版本")-1);						
//						appVersion = nameString.substring(nameString.indexOf("版本")+2,nameString.length());
//					}
//					else if(nameString != null && nameString.contains("V"))
//					{
//						appName=nameString.substring(0,nameString.indexOf("V")-1);
//						appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//					}
//					else if(nameString != null && nameString.contains("v"))
//					{
//						appName=nameString.substring(0,nameString.indexOf("v")-1);
//						appVersion = nameString.substring(nameString.indexOf("V")+1,nameString.length());
//					}
//					else if(nameString != null && nameString.contains("."))
//					{
//						appName=nameString.substring(0,nameString.indexOf(".")-1);
//						appVersion = nameString.substring(nameString.indexOf(".")-1,nameString.length());
//					}
//					else 
//					{
//						appName = nameString;
//						appVersion = null;
//					}
//						
//					appDetailUrl = page.getUrl().toString();
//					
//					appDownloadUrl = page.getHtml().xpath("//div[@class='c_down_info c_down_android']/div[3]/div[2]/ul/li[1]/a/@href").toString();
//					
//					osPlatform = page.getHtml().xpath("//span[@id='view_yqiu']//i/text()").toString();
//					
//					
//					String sizeString = page.getHtml().xpath("//span[@id='view_size']/i/text()").toString();
//						appSize = sizeString;
//					
//					String updatedateString = page.getHtml().xpath("//span[@id='view_time']/i/text()").toString();
//						appUpdateDate = updatedateString;
//					
//					String typeString = "apk";
//						appType =typeString;
//					
//					appvender=null;
//						
//					String DownloadedTimeString = null;
//						appDownloadedTime = DownloadedTimeString;		
//					
//					System.out.println("appName="+appName);
//					System.out.println("appDetailUrl="+appDetailUrl);
//					System.out.println("appDownloadUrl="+appDownloadUrl);
//					System.out.println("osPlatform="+osPlatform);
//					System.out.println("appVersion="+appVersion);
//					System.out.println("appSize="+appSize);
//					System.out.println("appUpdateDate="+appUpdateDate);
//					System.out.println("appType="+appType);
//					System.out.println("appvender="+appvender);
//					System.out.println("appDownloadedTime="+appDownloadedTime);
//				
//					if(appName != null && appDownloadUrl != null){
//						apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//						apks.add(apk);
//					}					
//					String apknumString = page.getHtml().xpath("//div[@id='old_url']").toString();
//					if(apknumString != null)
//					{
//					int apknum=0;
//					for(int i=apknumString.indexOf("<li>");i<=apknumString.lastIndexOf("<li>") && i!= -1;apknum++,i=apknumString.indexOf("<li>", i+4));
////					System.out.println("apknum="+apknum);  //apknum保存其他版本的apk的个数	
//					for(int j=1;j<apknum+1;j++)
//					{
//						//<li><a onclick="detail_down(1887,'android');_hmt.push(['_trackEvent','android','download']);"href="http://xl1.dl.gezila.com:1010/android/887/pp_android1.81.apk" target="_blank" class="xiazai">
////						</a><a onclick="detail_down(1887,'android');
////						_hmt.push(['_trackEvent','android','download']);" "
////								+ "href="http://xl1.dl.gezila.com:1010/android/887/pp_android1.81.apk" 
////									target="_blank">PP助手 V1.81</a><i>1.88MB</i><span>2014-05-14</span> </li>
//						Apk apk1=null;
//						String pathString="//div[@id='old_url']/ul"+"/li["+j+"]";
//						String nametempString= page.getHtml().xpath(pathString+"/a[2]/text()").toString();
//						String appName1 = null;				//app名字
//						String appVersion1 = null;			//app版本
//						if(nametempString != null && nametempString.contains("V"))
//						{
//							appName1=nametempString.substring(0,nametempString.indexOf("V")-1);
//							appVersion1 = nametempString.substring(nametempString.indexOf("V")+1,nametempString.length());
//						}
//						else if(nametempString != null && nametempString.contains("v"))
//						{
//							appName1=nametempString.substring(0,nametempString.indexOf("v")-1);
//							appVersion1 = nametempString.substring(nametempString.indexOf("V")+1,nametempString.length());
//						}
//						
//						String appDetailUrl1 = page.getUrl().toString();			//具体页面url
//						String appDownloadUrl1 = page.getHtml().xpath(pathString+"/a[2]/@href").toString();;		//app下载地址
//						String osPlatform1 = null ;			//运行平台
//						
//						String appSize1 = page.getHtml().xpath(pathString+"/i/text()").toString();				//app大小
//						String appUpdateDate1 = page.getHtml().xpath(pathString+"/span/text()").toString();;		//更新日期
//						String appType1 = "apk";				//下载的文件类型 apk？zip？rar？ipa?
//						String appvender1 = null;			//app开发者  APK这个类中尚未添加
//						String appDownloadedTime1 =null;		//app的下载次数
//						apk1 = new Apk(appName1,appDetailUrl1,appDownloadUrl1,osPlatform1 ,appVersion1,appSize1,appUpdateDate1,appType1,null);
//						apks.add(apk);
//						
//					}
//					}
					
					return Gezila_Detail.getApkDetail(page);
				}				
		return null;
	}

}
