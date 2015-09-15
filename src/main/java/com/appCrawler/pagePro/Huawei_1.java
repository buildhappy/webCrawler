package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 华为 http://appstore.huawei.com/
 * Huawei #124
 * @author DMT
 * (1)翻页的url需要手动构造 
 */
public class Huawei_1 implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page			http://appstore.huawei.com/search/%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA	
		if(page.getUrl().regex("http://appstore\\.huawei\\.com/search/.*").match() || page.getUrl().regex("http://appstore\\.huawei\\.com:80/search/.*").match()){
			
			//app的具体介绍页面				http://appstore.huawei.com:80/app/C10166495							
			List<String> url1 = page.getHtml().links("//div[@class='unit-main']").regex("http://appstore\\.huawei\\.com:80/app/.*").all();

	//搜索的结果中目录页只需提取一次
			if(page.getUrl().toString().length()-page.getUrl().toString().lastIndexOf("/") >=3)
			{	
			//添加下一页url(翻页)		page-ctrl ctrl-app
			String indexsString = page.getHtml().xpath("//div[@class='page-ctrl ctrl-app']/script").toString();
//			<script type="text/javascript">
//            freshPage('searchListPage', '1','246','24', 'http://appstore.huawei.com:80/search/刀塔传奇/');
//        </script>
			System.out.println("indexsString="+indexsString);
			indexsString = indexsString.substring(indexsString.indexOf(indexsString.indexOf("searchListPage"))+27,indexsString.lastIndexOf(","));
			String totalString = indexsString.substring(1,indexsString.indexOf(",")-1);
			String amountString = indexsString.substring(indexsString.indexOf(",")+2,indexsString.length()-1);
// totalString=246 amountString=24
			int total=Integer.parseInt(totalString);
			int amount=Integer.parseInt(amountString);
			for(int i=1;i<total/amount;i++)
				url1.add(page.getUrl()+"/"+i);
			}
	
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}

		}
		
		//the app detail page
		if(page.getUrl().regex("http://appstore\\.huawei\\.com:80/app/.*").match()){
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			
			String nameString=page.getHtml().xpath("//div[@class='app-info flt']/ul[1]/li[2]/p[1]/span[1]/text()").toString();			
				appName =nameString;
				
			appDetailUrl = page.getUrl().toString();
			
			String downloadString= page.getHtml().xpath("//div[@class='app-function nofloat']/a[2]").toString();
				downloadString = downloadString.substring(downloadString.indexOf("http"),downloadString.length());			
			appDownloadUrl = downloadString.substring(0,downloadString.indexOf("'"));
			
			
			String platFormString =page.getHtml().xpath("//div[@id='firmware_items']").toString();
			while(platFormString.contains("<"))
				if(platFormString.indexOf("<") == 0) platFormString = platFormString.substring(platFormString.indexOf(">")+1,platFormString.length());
				else if(platFormString.contains("<!--")) platFormString = platFormString.substring(0,platFormString.indexOf("<!--")) + platFormString.substring(platFormString.indexOf("-->")+3,platFormString.length());
				else platFormString = platFormString.substring(0,platFormString.indexOf("<")) + platFormString.substring(platFormString.indexOf(">")+1,platFormString.length());
			osPlatform = platFormString.replace("\n", "");
		
			String versionString = page.getHtml().xpath("//div[@class='app-info flt']/ul[2]/li[4]/span/text()").toString();
				appVersion = versionString;
			
			String sizeString = page.getHtml().xpath("//div[@class='app-info flt']/ul[2]/li[1]/span/text()").toString();
				appSize = sizeString;
			
			String updatedateString = page.getHtml().xpath("//div[@class='app-info flt']/ul[2]/li[2]/span/text()").toString();
				appUpdateDate = updatedateString;
			
			String typeString = "apk";
				appType =typeString;
			
			appvender=page.getHtml().xpath("//div[@class='app-info flt']/ul[2]/li[3]/span/@title").toString();
				
			String DownloadedTimeString = page.getHtml().xpath("//div[@class='app-info flt']/ul[1]/li[2]/p[1]/span[2]/text()").toString();
				appDownloadedTime = DownloadedTimeString.substring(DownloadedTimeString.indexOf("：")+1,DownloadedTimeString.length()-1);		
			
			System.out.println("appName="+appName);
			System.out.println("appDetailUrl="+appDetailUrl);
			System.out.println("appDownloadUrl="+appDownloadUrl);
			System.out.println("osPlatform="+osPlatform);
			System.out.println("appVersion="+appVersion);
			System.out.println("appSize="+appSize);
			System.out.println("appUpdateDate="+appUpdateDate);
			System.out.println("appType="+appType);
			System.out.println("appvender="+appvender);
			System.out.println("appDownloadedTime="+appDownloadedTime);
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
			}
			
			return apk;
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
