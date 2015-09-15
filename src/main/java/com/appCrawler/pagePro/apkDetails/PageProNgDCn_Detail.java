package com.appCrawler.pagePro.apkDetails;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.appCrawler.utils.MyNicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.List;

/**
 * 当乐安致 http://www.d.cn/
 * 有三种不同的页面分类，分别是网游、应用和游戏
 * @author DMT
 */
public class PageProNgDCn_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProNgDCn_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        if(page.getUrl().regex("http://android\\.d\\.cn/game.*html").match()
                || page.getUrl().regex("http://android\\.d\\.cn/software.*").match()){
            Apk apk = null;
            String appName = null;              //app名字
            String appDetailUrl = null;         //具体页面url
            String appDownloadUrl = null;       //app下载地址
            String osPlatform = null ;          //运行平台
            String appVersion = null;           //app版本
            String appSize = null;              //app大小
            String appUpdateDate = null;        //更新日期
            String appType = null;              //下载的文件类型 apk？zip？rar？ipa?
            String appvender = null;            //app开发者  APK这个类中尚未添加
            String appDownloadedTime=null;      //app的下载次数
            String appDescription =null;        //app的详细介绍
            String appCategory=null;
            
            appName =page.getHtml().xpath("//div[@class='de-app-des']/h1/text()").toString();
            if(appName == null)
                appName =page.getHtml().xpath("//div[@class='de-head-l']/h1/text()").toString();
            appDetailUrl = page.getUrl().toString();
            String downd=page.getHtml().xpath("//div[@class='de-adapt']/a/@onclick").toString();
            String flag=StringUtils.substringBetween(downd, "rt:'", "'");
            String id=StringUtils.substringBetween(downd, "ri:'", "'");
            // 获取下下载链接，模拟浏览器请求
            appDownloadUrl = getDownloadUrl(page.getUrl().get(),flag,id);

            appVersion = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[2]/span[2]/text()").toString();

            appSize = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[4]/text()").toString();

            appUpdateDate = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[3]/text()").toString();

            String typeString = page.getHtml().xpath("//ul[@class='de-app-tip clearfix']/li[4]/text()").toString();
            appType =typeString;
            String tempString = page.getHtml().xpath("//ul[@class='de-game-info clearfix']").toString();
            if(tempString.contains("热度")){
                osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();
                appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/a/text()").toString();
                if(null == appvender)
                    appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[10]/text()").toString();
            }
            else{
                osPlatform = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[8]/text()").toString();
                appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/a/text()").toString();
                if(null == appvender)
                    appvender = page.getHtml().xpath("//ul[@class='de-game-info clearfix']/li[9]/text()").toString();

            }

            String descriptionString = page.getHtml().xpath("//div[@class='de-intro-inner']/text()").toString();
            appDescription = descriptionString;
            List<String> appScreenshot = html.xpath("//ul[@class='shot-list pr clearfix fl']/li/img/@src").all();
            appCategory=page.getHtml().xpath("//li[@class='de-game-firm']/a/text()").toString();
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
            System.out.println("appDescription="+appDescription);

            if(appName != null && appDownloadUrl != null){
                apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
                apk.setAppScreenshot(appScreenshot);
                apk.setAppDescription(appDescription);
                apk.setAppCategory(appCategory);
            }
            LOGGER.info("return from D.process()");
            return apk;
        }
        else if(page.getUrl().regex("http://ng\\.d\\.cn/.*").match()){
       
            Apk apk = null;
            String appName = null;              //app名字
            String appDetailUrl = null;         //具体页面url
            String appDownloadUrl = null;       //app下载地址
            String osPlatform = null ;          //运行平台
            String appVersion = null;           //app版本
            String appSize = null;              //app大小
            String appUpdateDate = null;        //更新日期
            String appType = null;              //下载的文件类型 apk？zip？rar？ipa?
            String appvender = null;            //app开发者  APK这个类中尚未添加
            String appDownloadedTime=null;      //app的下载次数
            String appDescription =null;  //app的详细介绍
            String appCategory=null;
            List<String> appScreenshot = null;
            appDetailUrl = page.getUrl().toString();
            appName =page.getHtml().xpath("//h1[@class='gameNameTitle']/a/text()").toString();
            if(appName!=null)
            
            {
            	appDownloadUrl = page.getHtml().xpath("//div[@class='downAnd mb10']/a/@href").toString();
            String allinfoString = page.getHtml().xpath("//div[@class='rigame fl']").toString();
            System.out.println("allinfoString"+allinfoString);
            if(allinfoString!= null)
            {

          /*  while(allinfoString.contains("<"))
                if(allinfoString.indexOf("<") == 0) allinfoString = allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());
                else if(allinfoString.contains("<!--")) allinfoString = allinfoString.substring(0,allinfoString.indexOf("<!--")) + allinfoString.substring(allinfoString.indexOf("-->")+3,allinfoString.length());
                else allinfoString = allinfoString.substring(0,allinfoString.indexOf("<")) + allinfoString.substring(allinfoString.indexOf(">")+1,allinfoString.length());*/

            

            appVersion = StringUtils.substringBetween(allinfoString, "版本号：</span>","<br />").replaceAll("\\s*|\t|\r|\n", "");
            appUpdateDate = StringUtils.substringBetween(allinfoString, "更新时间：</span>","<br />");
            osPlatform =StringUtils.substringBetween(allinfoString, "平台：</span>","<br />");
            appCategory=StringUtils.trim(StringUtils.substringBetween(allinfoString, "类型：</span>","<br />")).replaceAll("\\s*|\t|\r|\n", "");
            }
            appDescription = page.getHtml().xpath("//div[@class='zgamejs']/p/text()").toString();

            appScreenshot = html.xpath("//ul[@class='shot-list pr clearfix fl']/li/img/@src").all();
            }
            else {
            	appName =page.getHtml().xpath("//p[@class='namep']/span/text()").toString();
            	appDownloadUrl = page.getHtml().xpath("//div[@class='downAnd']/a/@href").toString();
            	String detail=page.getHtml().xpath("//p[@class='andline']/text()").toString();
            	System.out.println("detail"+detail);
            	if(detail!=null){
            		appUpdateDate = StringUtils.substringBetween(detail, "更新时间：","游戏大小：");
            		appSize =StringUtils.substringAfterLast(detail, "游戏大小：");
            	}
            	 osPlatform =StringUtils.substringAfter(page.getHtml().xpath("//ul[@class='nzkfgamey']/li[2]/text()").toString(), "平台：");
                 
            	 appCategory=StringUtils.trim(StringUtils.substringAfter(page.getHtml().xpath("//ul[@class='nzkfgamey']/li[1]/text()").toString(), "类型： "));
            	 if(appCategory!=null) appCategory=appCategory.replaceAll("\\s*|\t|\r|\n", "");
            	 appDescription = page.getHtml().xpath("//div[@class='kfGamejs']/p/text()").toString();

                 appScreenshot = html.xpath("//ul[@class='shot-list pr clearfix fl']/li/img/@src").all();
            }
            
            
            
            
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
            System.out.println("appDescription="+appDescription);

            if(appName != null && appDownloadUrl != null){
                apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
                apk.setAppDescription(appDescription);
                apk.setAppScreenshot(appScreenshot);
                apk.setAppCategory(appCategory);
            }
            LOGGER.info("return from D.process()");
            return apk;
        }

        return null;
    }

    /**
     * 模拟浏览器请求一次，获取下载链接
     * @param url
     * @return
     */
    private static String getDownloadUrl(String url,String flag,String id) {
        MyNicelyResynchronizingAjaxController ajaxController = new MyNicelyResynchronizingAjaxController();
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
        //HtmlUnitDriver
        //设置webClient的相关参数
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.setAjaxController(ajaxController);
        webClient.getOptions().setTimeout(35000);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        try {
		//	 if (url.matches("http://android\\.d\\.cn/software/.*")) {
			     /*   HtmlPage rootPage = webClient.getPage(url);
			        System.out.println("Page"+rootPage);
					List<?> list = rootPage.getByXPath("//ul[@class='de-down']/li[1]/a/@onclick");
					if (null != list && list.size() > 0) {
						return StringUtils.substringBetween(((DomAttr) list.get(0)).getValue(), "'", "'");
					}*/
        	
				//模拟浏览器打开一个目标网址
					UnexpectedPage page = webClient.getPage("http://android.d.cn/rm/red/"+flag+"/"+id);
				//	System.out.println("UnexpectedPage"+"http://android.d.cn/rm/red/"+flag+"/"+id);
					JSONObject json = JSON.parseObject(page.getWebResponse().getContentAsString());
					JSONArray objects = json.getJSONArray("pkgs");

					if (objects.size() > 0) {
						return objects.getJSONObject(0).getString("pkgUrl");
					}			 
					 
			 }
			/* else {
			    //模拟浏览器打开一个目标网址
				UnexpectedPage page = webClient.getPage("http://android.d.cn/rm/red/1/" + StringUtils.substringBefore(StringUtils.substringAfterLast(url, "/"), "."));
				JSONObject json = JSON.parseObject(page.getWebResponse().getContentAsString());
				JSONArray objects = json.getJSONArray("pkgs");

				if (objects.size() > 0) {
					return objects.getJSONObject(0).getString("pkgUrl");
				}
			 }
        }*/
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
        
}