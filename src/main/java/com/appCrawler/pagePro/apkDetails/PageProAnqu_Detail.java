package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安趣市场[中国] app搜索抓取
 * url:http://api.anqu.com/search/index/?keyword=MT
 *
 * @version 1.0.0
 */
public class PageProAnqu_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnqu_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='contlf3']/text()").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//a[@class='btn']/@href").toString();
        String osPlatform = null;
        String appSize = null;
        String appUpdateDate = null;
        String appType = null;

        String appDescription = html.xpath("//div[@class='yxjs']/p/text()").get();
        List<String> appScreenshot = null;
        String appTag = null;
        String appCategory = StringUtils.substringAfterLast(html.xpath("//div[@class='gamjs']/ul/li[2]/text()").toString(), "：");
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
