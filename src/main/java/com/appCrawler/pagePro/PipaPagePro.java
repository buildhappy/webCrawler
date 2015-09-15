package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.appCrawler.pagePro.apkDetails.PipaPagePro_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * 琵琶网：http://www.pipaw.com/
 * @author Administrator
 *
 */
public class PipaPagePro implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setCycleRetryTimes(3).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		Apk apk = null;
		//search page  http://wy.pipaw.com/gamelist-all-1.html?keyword=*#*#*#
		if(page.getUrl().regex("http://wy\\.pipaw\\.com/gamelist-all-1\\.html\\?keyword.*").match()
				|| page.getUrl().regex("http://www\\.pipaw\\.com/searchsite\\.html\\?keyword.*").match()){
			//app的具体介绍页面		
			String urlString = page.getUrl().toString();
			//获取关键字
			String keywordString= urlString.substring(urlString.indexOf("=")+1,urlString.length());
			//搜网游
			page.addTargetRequest("http://www.pipaw.com/searchsite.html?keyword="+keywordString+"&list_type=1");
			//搜单击
			page.addTargetRequest("http://www.pipaw.com/searchsite.html?keyword="+keywordString+"&list_type=2");
			//搜应用
			page.addTargetRequest("http://www.pipaw.com/searchsite.html?keyword="+keywordString+"&list_type=5");
			List<String> url1 = page.getHtml().links("//ul[@class='ul_icon_dj clearfix']").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//p[@class='page']").all();	
			List<String> url3 = page.getHtml().links("//div[@class='main_left_div']").all();	
			url1.addAll(url2);
			url1.addAll(url3);
					
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);

			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
			return null;
		}//http://wy.pipaw.com/game1439/
		else  if(page.getUrl().regex("http://wy\\.pipaw\\.com/game[0-9]+/").match()){
			page.addTargetRequest(page.getUrl().toString()+"down.html");
		}
		else return PipaPagePro_Detail.getApkDetail(page);
		return null;
//		String appName = page.getHtml().xpath("//div[@class='soft_info_middle']/h3/text()").toString();
//
//		List<String> infos = null;
//		
//		String appDetailUrl = page.getUrl().toString();
//		String appDownloadUrl = null;		//app下载地址
//		String osPlatform = null ;			//运行平台
//		String appVersion = null;			//app版本
//		String appSize = null;				//app大小
//		String appUpdateDate = null;		//更新日期
//		String appType = "apk";				//下载的文件类型 apk？zip？rar？ipa?
//		String appvender = null;			//app开发者  APK这个类中尚未添加
//		String appDownloadedTime=null;		//app的下载次数
//		String appDescription = null;
//		int length = 0;
//		
//		if(page.getUrl().regex("http://wy\\.pipaw\\.com/[a-zA-Z0-9]+").match()){///down.html
//			if((appDownloadUrl = page.getHtml().xpath("//div[@class='dow_button']/a/@href").toString()) != null){
//				//these sites: http://wy.pipaw.com/game1536/down.html
//				infos = page.getHtml().xpath("//ul[@class='text']/li/text()").all();
//				length = infos.size();
//				infos = page.getHtml().xpath("//div[@class='position]/a/@title").all();
//				appName = infos.size() > 1 ? infos.get(1) : null;
//				//System.out.println("appName:" + appName);
//				length = infos.size();
//				infos = page.getHtml().xpath("//ul[@class='text']/li/text()").all();
//				appVersion = length > 1 ? infos.get(0):null;
//				appSize = length > 2 ? infos.get(1):null;
//				appUpdateDate = length > 3 ? infos.get(2):null;
//				osPlatform = length > 4 ? infos.get(3):null;
//				System.out.println("in one");
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//				return apk;
//				
//			}
//			if((appDownloadUrl = page.getHtml().xpath("//div[@class='fram_an1']/a/@href").toString()) != null){
//				//http://wy.pipaw.com/game568/down.html
//				infos = page.getHtml().xpath("//dl[@class='main-dl']/dt/h2/text()").all();
//				//printList(infos);
//				length = infos.size();
//				String cur[] = null;
//				cur = length > 0 ? infos.get(0).split("："):null;
//				appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 1 ? infos.get(1).split("："):null;
//				appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 2 ? infos.get(2).split("："):null;
//				osPlatform =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 4 ? infos.get(5).split("："):null;
//				appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				System.out.println(appUpdateDate);
//				appName = page.getHtml().xpath("//span[@class='Data_about_l']/text()").toString().trim();
//				appName = appName.replace("：  >  > ", "");
//				appName = appName.replace("下载", "");
//				//System.out.println("appName:" + appName);
//				//appDownloadUrl = page.getHtml().xpath("//div[@class='fram_an1']/a/@href").toString();
//				System.out.println("in two");
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//				return apk;
//			}
//			if((appDownloadUrl = page.getHtml().xpath("//dl[@class='gamesdownBtn']/dd/a/@href").toString())!= null){
//				//http://wy.pipaw.com/game964/down.html
//				infos = page.getHtml().xpath("//dl[@class='main-dl']/dt/h2/text()").all();
//				//printList(infos);
//				
//				length = infos.size();
//				String cur[] = null;
//				
//				cur = length > 1 ? infos.get(0).split("："):null;
//				appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 1 ? infos.get(1).split("："):null;
//				appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 2 ? infos.get(2).split("："):null;
//				appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				
//				cur = length > 3 ? infos.get(3).split("："):null;
//				osPlatform=  (cur != null) && (cur.length >= 2)?cur[1]:null;
//				//System.out.println("os:" + os);
//				infos = page.getHtml().xpath("//div[@class='news_list']/a/text()").all();
//				appName = infos.size() > 1 ? infos.get(1).trim() : null;
//				System.out.println("in three");
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//				return apk;
//			}
// 		}
//		if((appDownloadUrl = page.getHtml().xpath("//div[@class='idkz1']/ul/li/a/@href").toString())!=null){
// 			//http://www.pipaw.com/wjzjmy/
// 			infos = page.getHtml().xpath("//div[@class='gema_logo_lao']/span/text()").all();
// 			//printList(infos);
// 			//System.out.println("appDownloadUrl:" + appDownloadUrl);
//			length = infos.size();
//			String cur[] = null;
//			
//			cur = length > 1 ? infos.get(1).split(":"):null;
//			appDownloadedTime =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//			
//			cur = length > 3 ? infos.get(3).split(":"):null;
//			appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//			
//			cur = length > 5 ? infos.get(5).split(":"):null;
//			osPlatform=  (cur != null) && (cur.length >= 2)?cur[1]:null;
//			
//			cur = length > 6 ? infos.get(6).split(":"):null;
//			appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//			
//			cur = length > 7 ? infos.get(7).split(":"):null;
//			appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//			//System.out.println("appUpdateDate:" + appUpdateDate);
//			appName = page.getHtml().xpath("div[@class='idke']/text()").toString();
//			appName = appName.replace("当前位置： >  >", "");
//			appName = appName.trim();
//			System.out.println("in four");
//			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			return apk;
////			System.out.println("date:" + date);
////			System.out.println("appName:" + appName);
//		}
//		//eg:http://wy.pipaw.com/game210/
//		if((appDownloadUrl = page.getHtml().xpath("//div[@class='app_mig']/ul/li/a/@href").toString())!=null){
//			List<String> info = page.getHtml().xpath("//div[@class='Navigation1']/ul/li/a/text()").all();
//			appName = page.getHtml().xpath("//div[@class='Navigation1']/ul/li[2]/a/text()").toString();
//			appName = appName.replace("官网" , "");
//			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			return apk;
//		}
//		//eg:http://wy.pipaw.com/game823/
//		
//		if((appDownloadUrl = page.getHtml().xpath("//div[@class='download_top']/a/@href").toString())!=null){
//			System.out.println("appDownloadUrl" + appDownloadUrl);
//			appName = page.getHtml().xpath("//div[@class='gema_logo_zi']/a/text()").toString();
//			appDescription =page.getHtml().xpath("//div[@class='screenshots_in']/text()").toString(); 
//			infos = page.getHtml().xpath("//div[@class='gema_logo_lao']/span/text()").all();
//			if(infos.size() > 2){
//				osPlatform = infos.get(0).split("：")[1];
//				osPlatform = osPlatform.trim();
//				appDownloadedTime = infos.get(3).split("：")[1];
//			}
//			apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//			apk.setAppDescription(appDescription);
//			apk.setAppDownloadTimes(appDownloadedTime);
//			return apk;
//		}
		
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	public void printList(List list){
		for(int i = 0 ; i < list.size(); i++){
			System.out.println(list.get(i));
		}
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args){
		String s = "QQ御剑天涯官网";
		System.out.println(s.contains("官网"));
		s = s.replace("官网", " ");
		
		
		System.out.println(s);
	}
}
