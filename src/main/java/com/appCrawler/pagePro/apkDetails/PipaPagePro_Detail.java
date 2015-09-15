package com.appCrawler.pagePro.apkDetails;
/**
 *  琵琶网：http://www.pipaw.com/
 * @author Administrator
 * @author DMT
 *
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;


public class PipaPagePro_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PipaPagePro_Detail.class);
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
		 //应用   http://www.pipaw.com/apps/1.html  
		if(page.getUrl().regex("http://www\\.pipaw\\.com/apps/.*html").match()){
			System.out.println("in this");
			appName = page.getHtml().xpath("//div[@class='ppw_local']/em/text()").toString();
			if(appName == null) return null;
			appDetailUrl = page.getUrl().toString();
			appCategory = page.getHtml().xpath("//tbody/tr[1]/td[1]/a/text()").toString();
			osPlatform = page.getHtml().xpath("//tbody/tr[1]/td[4]/span/text()").toString();
			appVersion = page.getHtml().xpath("//tbody/tr[2]/td[1]/span/text()").toString();
			appSize = page.getHtml().xpath("//tbody/tr[2]/td[2]/span/text()").toString();
			appDownloadedTime = page.getHtml().xpath("//tbody/tr[2]/td[3]/span/text()").toString();
			appUpdateDate = page.getHtml().xpath("//tbody/tr[2]/td[4]/span/text()").toString();
			appVenderName = page.getHtml().xpath("//tbody/tr[3]/td[1]/span/text()").toString();
			appTag = page.getHtml().xpath("//tbody/tr[3]/td[2]/span/a/text()").all().toString();
			appDescription = usefulInfo(page.getHtml().xpath("//div[@class='show_mes']").toString());
			appScrenshot = page.getHtml().xpath("//div[@class='big_pic bd']//img/@src").all();
			appDownloadUrl = page.getHtml().xpath("//dd//p[@class='p1']/a/@href").toString();
		}
		//网游
		else if(page.getUrl().regex("http://wy\\.pipaw\\.com/game[0-9]+/down.html").match()){
			appName = page.getHtml().xpath("//div[@class='position']/a[2]/text()").toString();
			if(appName == null) return null;
			appDetailUrl = page.getUrl().toString();
			appVersion = page.getHtml().xpath("//ul[@class='text']/li[1]/text()").toString();
			appSize = page.getHtml().xpath("//ul[@class='text']/li[2]/text()").toString();
			appUpdateDate = page.getHtml().xpath("//ul[@class='text']/li[3]/text()").toString();
			osPlatform = page.getHtml().xpath("//ul[@class='text']/li[4]/text()").toString();
			appDownloadUrl =page.getHtml().xpath("//div[@class='dow_button']/a[1]/@href").toString();
			appCategory = page.getHtml().xpath("//ul[@class='ul_2']/li[3]/text()").toString().split("：")[1];
			appDescription = page.getHtml().xpath("//p[@class='p_1']/text()").toString();
			appScrenshot = page.getHtml().xpath("//div[@class='small_pic']//img/@src").all();
		}//单击
		else if(page.getUrl().regex("http://www\\.pipaw\\.com/[a-zA-Z0-9]+/").match())
		{
			//单击种类1
			if(page.getHtml().xpath("//span[@class='ziad']/a/text()").toString() !=null)
			{
			appName = page.getHtml().xpath("//span[@class='ziad']/a/text()").toString();
			appDetailUrl = page.getUrl().toString();
			appCategory = page.getHtml().xpath("//div[@class='game_img_r']/span[2]/text()").toString().split("：")[1];
			if(page.getHtml().xpath("//div[@class='game_img_r']/span[4]/text()").toString()!= null && !page.getHtml().xpath("//div[@class='game_img_r']/span[4]/text()").toString().endsWith("："))	
				appVersion = page.getHtml().xpath("//div[@class='game_img_r']/span[4]/text()").toString().split("：")[1];
			appDownloadUrl = page.getHtml().xpath("//dl[@class='dl1']/dt/a/@href").toString();
			appDescription = page.getHtml().xpath("//div[@class='Gift_foomg1']/a/text()").toString();
			appScrenshot = page.getHtml().xpath("//ul[@id='example2']//img/@src").all();
			}
			//单击种类2
			else{
//				String tempString =page.getHtml().toString();
//				System.out.println(page.getHtml().toString());
			appName = page.getHtml().xpath("//div[@class='gema_logo_zi']/a/text()").toString();
			if(appName == null) return null;
			appDetailUrl = page.getUrl().toString();
			appCategory = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[1]/a/text()").toString();
			appDownloadedTime = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[2]/text()").toString();
			if(appDownloadedTime!=null && !appDownloadedTime.endsWith("："))
				appDownloadedTime = appDownloadedTime.substring(3,appDownloadedTime.length());
			appVersion = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[4]/text()").toString();
			if(appVersion!=null && !appVersion.endsWith("："))
				appVersion = appVersion.substring(3,appVersion.length());
			osPlatform = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[6]/text()").toString();
			if(osPlatform!=null && !osPlatform.endsWith("："))
				osPlatform = osPlatform.substring(3,osPlatform.length());
			appSize = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[7]/text()").toString();
			if(appSize!=null && !appSize.endsWith("："))
				appSize = appSize.substring(3,appSize.length());
			appUpdateDate = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[8]/text()").toString();
			if(appUpdateDate!=null && !appUpdateDate.endsWith("："))
				appUpdateDate = appUpdateDate.substring(5,appUpdateDate.length());
			appVenderName = page.getHtml().xpath("//div[@class='gema_logo_lao']/span[9]/a/text()").toString();
			appDownloadUrl = page.getHtml().xpath("//dl/dd[2]/a/@href").toString();
			appDescription = page.getHtml().xpath("//div[@class='screenshots_in']/p/text()").toString();
			appScrenshot = page.getHtml().xpath("//div[@id='products']//a/@href").all();
			}		
		}else return null;
		
	
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

//
//
//
//List<String> infos = null;
//
//int length = 0;
//
//if(page.getUrl().regex("http://wy\\.pipaw\\.com/[a-zA-Z0-9]+/").match()){///down.html
//	if((appDownloadUrl = page.getHtml().xpath("//div[@class='dow_button']/a/@href").toString()) != null){
//		//these sites: http://wy.pipaw.com/game1536/down.html
//		infos = page.getHtml().xpath("//ul[@class='text']/li/text()").all();
//		length = infos.size();
//		infos = page.getHtml().xpath("//div[@class='position]/a/@title").all();
//		appName = infos.size() > 1 ? infos.get(1) : null;
//		//System.out.println("appName:" + appName);
//		length = infos.size();
//		infos = page.getHtml().xpath("//ul[@class='text']/li/text()").all();
//		appVersion = length > 1 ? infos.get(0):null;
//		appSize = length > 2 ? infos.get(1):null;
//		appUpdateDate = length > 3 ? infos.get(2):null;
//		osPlatform = length > 4 ? infos.get(3):null;
//		System.out.println("in one");				
//	}
//	
//	if((appDownloadUrl = page.getHtml().xpath("//div[@class='fram_an1']/a/@href").toString()) != null){
//		//http://wy.pipaw.com/game568/down.html
//		infos = page.getHtml().xpath("//dl[@class='main-dl']/dt/h2/text()").all();
//		//printList(infos);
//		length = infos.size();
//		String cur[] = null;
//		cur = length > 0 ? infos.get(0).split("："):null;
//		appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 1 ? infos.get(1).split("："):null;
//		appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 2 ? infos.get(2).split("："):null;
//		osPlatform =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 4 ? infos.get(5).split("："):null;
//		appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		System.out.println(appUpdateDate);
//		appName = page.getHtml().xpath("//span[@class='Data_about_l']/text()").toString().trim();
//		appName = appName.replace("：  >  > ", "");
//		appName = appName.replace("下载", "");
//		//System.out.println("appName:" + appName);
//		//appDownloadUrl = page.getHtml().xpath("//div[@class='fram_an1']/a/@href").toString();
//		System.out.println("in two");				
//	}
//	
//	if((appDownloadUrl = page.getHtml().xpath("//dl[@class='gamesdownBtn']/dd/a/@href").toString())!= null){
//		//http://wy.pipaw.com/game964/down.html
//		infos = page.getHtml().xpath("//dl[@class='main-dl']/dt/h2/text()").all();
//		//printList(infos);
//		
//		length = infos.size();
//		String cur[] = null;
//		
//		cur = length > 1 ? infos.get(0).split("："):null;
//		appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 1 ? infos.get(1).split("："):null;
//		appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 2 ? infos.get(2).split("："):null;
//		appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		
//		cur = length > 3 ? infos.get(3).split("："):null;
//		osPlatform=  (cur != null) && (cur.length >= 2)?cur[1]:null;
//		//System.out.println("os:" + os);
//		infos = page.getHtml().xpath("//div[@class='news_list']/a/text()").all();
//		appName = infos.size() > 1 ? infos.get(1).trim() : null;
//		System.out.println("in three");				
//	}
//	}
//if((appDownloadUrl = page.getHtml().xpath("//div[@class='idkz1']/ul/li/a/@href").toString())!=null){
//		//http://www.pipaw.com/wjzjmy/
//		infos = page.getHtml().xpath("//div[@class='gema_logo_lao']/span/text()").all();
//		//printList(infos);
//		//System.out.println("appDownloadUrl:" + appDownloadUrl);
//	length = infos.size();
//	String cur[] = null;
//	
//	cur = length > 1 ? infos.get(1).split(":"):null;
//	appDownloadedTime =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//	
//	cur = length > 3 ? infos.get(3).split(":"):null;
//	appVersion =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//	
//	cur = length > 5 ? infos.get(5).split(":"):null;
//	osPlatform=  (cur != null) && (cur.length >= 2)?cur[1]:null;
//	
//	cur = length > 6 ? infos.get(6).split(":"):null;
//	appSize =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//	
//	cur = length > 7 ? infos.get(7).split(":"):null;
//	appUpdateDate =  (cur != null) && (cur.length >= 2)?cur[1]:null;
//	//System.out.println("appUpdateDate:" + appUpdateDate);
//	appName = page.getHtml().xpath("div[@class='idke']/text()").toString();
//	appName = appName.replace("当前位置： >  >", "");
//	appName = appName.trim();
//	
//	
////	System.out.println("date:" + date);
////	System.out.println("appName:" + appName);
//}
////eg:http://wy.pipaw.com/game210/
//if((appDownloadUrl = page.getHtml().xpath("//div[@class='app_mig']/ul/li/a/@href").toString())!=null){
//	List<String> info = page.getHtml().xpath("//div[@class='Navigation1']/ul/li/a/text()").all();
//	appName = page.getHtml().xpath("//div[@class='Navigation1']/ul/li[2]/a/text()").toString();
//	appName = appName.replace("官网" , "");
//	
//}
////eg:http://wy.pipaw.com/game823/
//
//if((appDownloadUrl = page.getHtml().xpath("//div[@class='download_top']/a/@href").toString())!=null){
//	
//	appName = page.getHtml().xpath("//div[@class='gema_logo_zi']/a/text()").toString();
//	appDescription =page.getHtml().xpath("//div[@class='screenshots_in']/text()").toString(); 
//	infos = page.getHtml().xpath("//div[@class='gema_logo_lao']/span/text()").all();
//	if(infos.size() > 2){
//		osPlatform = infos.get(0).split("：")[1];
//		osPlatform = osPlatform.trim();
//		appDownloadedTime = infos.get(3).split("：")[1];
//	}
//}
