package com.appCrawler.pagePro.apkDetails;

import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public class PageProAnfun_Detail {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnfun_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='content-categoryCtn-title clearfix']/h1/text()").toString();
        String appVersion = html.xpath("//ul[@class='sideBar-appDetail']/li[2]/div/text()").toString();
        String appDownloadUrl = html.xpath("//div[@class='content-detailCtn-icon']/a/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//ul[@class='sideBar-appDetail']/li[3]/div/text()").toString();
        String appUpdateDate = html.xpath("//ul[@class='sideBar-appDetail']/li[4]/div/text()").toString();
        String appType = null;

//        String appDescription = html.xpath("//div[@class='toggle-detail-content']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='slide-line']/div/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//ul[@class='sideBar-appDetail']/li[1]/div/a/text()").get();
        String appCommentUrl = null;
        String appComment = null;

        
//    	System.out.println("appName="+appName);
//		System.out.println("appDetailUrl="+appDetailUrl);
//		System.out.println("appDownloadUrl="+appDownloadUrl);
//		System.out.println("osPlatform="+osPlatform);
//		System.out.println("appVersion="+appVersion);
//		System.out.println("appSize="+appSize);
//		System.out.println("appUpdateDate="+appUpdateDate);
//		System.out.println("appType="+appType);
//		
//		System.out.println("appDescription="+appDescription);
//		System.out.println("appTag="+appTag);
//		System.out.println("appCategory="+appCategory);
        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
//            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate,appTag, appCategory, appScreenshot, appCommentUrl, appComment, null);

        return apk;
    }
}

