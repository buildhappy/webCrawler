package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 搜狐应用中心[中国] app搜索抓取
 * url:http://www.4355.com/e/search/result/?searchid=6665
 *
 * @version 1.0.0
 */
public class PagePro4355_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro4355_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='gameinfo']/h1/text()").toString();
        String appVersion = org.apache.commons.lang3.StringUtils.substringAfterLast(html.xpath("//div[@class='gameinfo']/ul/li[2]/text()").toString(), "：");
        String appDownloadUrl = html.xpath("//li[@class='az']/a/@href").toString();
        String osPlatform = org.apache.commons.lang3.StringUtils.substringAfterLast(html.xpath("//div[@class='gameinfo']/ul/li[5]/text()").toString(), "：");
        String appSize = org.apache.commons.lang3.StringUtils.substringAfterLast(html.xpath("//div[@class='gameinfo']/ul/li[1]/text()").toString(), "：");
        String appUpdateDate = org.apache.commons.lang3.StringUtils.substringAfterLast(html.xpath("//div[@class='gameinfo']/ul/li[6]/text()").toString(), "：");
        String appType = "APK";

        String appDescription = html.xpath("//div[@id='j_app_desc']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='s-box']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='location']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
       // String dowloadNum = org.apache.commons.lang3.StringUtils.substringAfterLast(html.xpath("//div[@class='gameinfo']/ul/li[3]/text()").toString(), "：");;
        String dowloadNum =html.xpath("//div[@class='gameinfo']/ul/li[3]/i/text()").toString();
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
