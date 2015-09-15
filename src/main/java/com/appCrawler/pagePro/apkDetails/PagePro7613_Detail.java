package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 软件下吧[中国] app搜索抓取
 * url:http://www.7613.com/soft/14/
 *
 * @version 1.0.0
 */
public class PagePro7613_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro7613_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='rg']/div[@class='t8']/text()").toString();
        String moreInfo = html.xpath("//div[@class='p90 p901']").get();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@class='downloadurll']/a/@href").toString();
        String osPlatform = StringUtils.substringBetween(moreInfo, "运行环境：", "<br />");
        String appSize = StringUtils.substringBetween(moreInfo, "软件大小：", "<br />");
        String appUpdateDate = StringUtils.substringBetween(moreInfo, "更新时间：", "<br />");
        String appType = null;

        String appDescription = html.xpath("//div[@class='p90 p70']/text()").get();
        List<String> appScreenshot = null;
        String appTag = null;
        String appCategory = html.xpath("//div[@id='weizhi']/a[3]/text()").get();
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
