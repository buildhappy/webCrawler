package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.Android173Sy_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * #169
 * 手游世界http://android.173sy.com/
 * 可以通过伪造下载链接来构造下载链接
 * http://android.173sy.com/download/downloadapk.php?id=13425&s=1
 * 将id后的参数修改成相应的apk的id就好
 * @author Administrator
 *
 */
public class Android173Sy implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		
		if(page.getUrl().regex("http://android\\.173sy\\.com/games/search\\.php\\?key=*").match()){
			//app的具体介绍页面											
			List<String> detailUrl = page.getHtml().links("//div[@class='searchlist']").all();
			
			List<String> pageUrl = page.getHtml().links("//div[@class='pagel tac r mt_25']").all();
			
			//添加下一页url(翻页)
			List<String> url4 = page.getHtml().links("//div[@class='pager clearfix']").all();
			
			detailUrl.addAll(pageUrl);
			System.out.println("detailUrl size="+detailUrl.size());
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(detailUrl);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		String appName = null;				//app名字
		appName = page.getHtml().xpath("//div[@class='gi_t_title']/text()").toString();
		appName = page.getHtml().xpath("//p[@class='dl_ititle']/text()").toString();
		//the app detail page
		if(appName != null){
//			Apk apk = null;
//			String rawId = page.getHtml().xpath("//p[@class='dbtn_download']/@onclick").get();
//			String id = null;
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			String rawVersion = page.getHtml().xpath("//p[@class='cf51e7e lh20 mt_5']/text()").toString();
//			if((rawId != null) && (rawVersion != null)){
//				id = rawId.split("\\(")[1].split(",")[0];
//				appDownloadUrl = "http://android.173sy.com/download/downloadapk.php?id="+ id + "&s=1";
//				appVersion = rawVersion.split("： ")[1].trim();
//			}
//			
//			List<String> infos = page.getHtml().xpath("//div[@class='fc mt_10']/p/text()").all();
//			appSize = infos.get(0).split("：")[1];
//			appUpdateDate = infos.get(1).split("：")[1];
//			appDownloadedTime = page.getHtml().xpath("//span[@id='downdownnum1_\\*']/text()").toString();
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			}
			
			return Android173Sy_Detail.getApkDetail(page);
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
