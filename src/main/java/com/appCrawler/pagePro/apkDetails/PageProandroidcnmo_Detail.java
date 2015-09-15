package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安卓中国[中国] app搜索抓取
 * url:http://app.cnmo.com/searchAll.php?s=qq&ts=1423376176047
 *
 * @version 1.0.0
 */
public class PageProandroidcnmo_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProandroidcnmo_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String pageurl=page.getHtml().xpath("//ul[@class='reviewHdNav clearfix']/li[1]/a/@href").toString();
        String id=StringUtils.substringBetween(pageurl, "android/", "/");

        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//p[@class='reviewHdConTit']/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//div[@class='basicInforL']/ul[2]/li[1]/text()").toString(),"版本：");
        String appDownloadUrl = "http://app.cnmo.com/down_"+id+".do";
        String osPlatform = StringUtils.substringAfter(html.xpath("//div[@class='basicInforL']/ul[2]/li[4]/text()").toString(),"运行环境：");
        String appSize =StringUtils.substringAfter(html.xpath("//div[@class='basicInforL']/ul[1]/li[2]/text()").toString(),"应用大小：");
        String appUpdateDate =StringUtils.substringAfter(html.xpath("//div[@class='basicInforL']/ul[2]/li[3]/text()").toString(),"时间：");
        String appType = "APK";

        String appDescription = html.xpath("//p[@id='conIntro']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='wSlidesCont']/div/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='crumbs']/a[4]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
            apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
