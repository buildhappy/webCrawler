package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 下载之家[中国] app搜索抓取
 * url:http://www.downza.cn/search?k=MT
 *
 * @version 1.0.0
 */
public class PageProDownza_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProDownza_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//h1[@itemprop='name']/text()").toString();
        String appVersion = html.xpath("//span[@itemprop='version']/text()").get();
        String appDownloadUrl = html.xpath("//ul[@class='ul_Address']/li[1]/a/@href").toString();
        String osPlatform = StringUtils.substringAfter(html.xpath("//li[@itemprop='operatingSystem']/text()").get(), "：");
        String appSize = html.xpath("//span[@itemprop='fileSize']/text()").get();
        String appUpdateDate = html.xpath("//span[@itemprop='dateModified']/text()").get();
        String appType = null;

        String appDescription = html.xpath("//div[@id='soft-intro']/p[1]/text()").get();
        List<String> appScreenshot = null;
        String appTag = null;
        String appCategory = html.xpath("//p[@id='fast-nav']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl && !appVersion.toLowerCase().contains("iphone")
        		&& !osPlatform.toLowerCase().contains("win") && !osPlatform.toLowerCase().contains("iphone")) {
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
