package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appCrawler.pagePro.apkDetails.Gamersky_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 游民星空 http://shouyou.gamersky.com/
 * Gamersky #74
 * @author DMT
 */
public class Gamersky implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page				http://soso.gamersky.com/cse/search?q= &s=3068275339727451251&stp=1&nsid=1
		if(page.getUrl().regex("http://soso\\.gamersky\\.com/cse/search\\?q=.*").match()){
			//app的具体介绍页面	http://shouyou.gamersky.com/ku/394435.shtml
			List<String> url1 = page.getHtml().links("//div[@id='results']").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='pager clearfix']").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			String regex1 = "(?=(http://shouyou\\.gamersky\\.com/ku/*)).*$";//"[\\S]+['html\\?ch=']+\\d$";
			String regex2 = "(?=(http://soso\\.gamersky\\.com/cse/search\\?q=*)).*$";
			Pattern p1 = Pattern.compile(regex1);
			Pattern p2 = Pattern.compile(regex2);
			//Matcher m;
			while(it.hasNext()){
				String url = it.next();
				if(p1.matcher(url).matches() || p2.matcher(url).matches()){//url.matches("http://soso\\.gamersky\\.com/cse/search\\?q=.*")
					System.out.println("match:" + url);
					page.addTargetRequest(url);
				}
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://shouyou\\.gamersky\\.com/ku/*").match()){
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
//			String description = null;
//			
//			appName = page.getHtml().xpath("//div[@class='PF12_1']/a/text()").toString();			
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//a[@class='AI but1 countHit']/@href").toString();
//			
//			appvender = page.getHtml().xpath("//div[@class='PF12_3 li2']/span[@class='kf']/text()").toString();
//			description = page.getHtml().xpath("//div[@class='intro']/p/text()").toString().trim();
//			
//			List<String> infos = page.getHtml().xpath("//div[@class='MidL2 L']/div[@class='MidL2_2']/ul/li/text()").all();
//			int size = infos.size();
//			
//			if(size > 0){
//				if(infos.get(1).split("：").length > 0){
//					appSize = infos.get(1).split("：")[1];
//				}
//			}
//			if(size > 1){
//				if(infos.get(2).split("：").length > 0){
//					appVersion = infos.get(2).split("：")[1];
//				}
//			}
//			if(size > 2){
//				if(infos.get(3).split("：").length > 0){
//					osPlatform = infos.get(3).split("：")[1];
//				}
//			}
//			/*
//			System.out.println("appName="+appName);
//			System.out.println("appDetailUrl="+appDetailUrl);
//			System.out.println("appDownloadUrl="+appDownloadUrl);
//			System.out.println("appvender="+appvender);
//			System.out.println("description="+description);
//			System.out.println("osPlatform="+osPlatform);
//			System.out.println("appSize="+appSize);
//			System.out.println("appVersion="+appVersion);
//			 */
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//				apk.setAppDescription(description);
//			}
			return Gamersky_Detail.getApkDetail(page);
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

	public static void main(String[] args){
		String s = "http://soso.gamersky.com/cse/search?q=qq&click=1&s=3068275339727451251&nsid=1";
		String regex= "(?=(http://soso\\.gamersky\\.com/cse/search\\?q=*)).*$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		
		System.out.println(m.matches());
	}
}
