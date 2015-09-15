package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安卓中文网(tgbus)[中国] app搜索抓取
 * url:http://a.tgbus.com/game/, http://a.tgbus.com/soft/
 *
 * @version 1.0.0
 */
public class PageProTgbus_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProTgbus_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        String appId = html.xpath("//div[@class='sontxt fl']/strong/a/@href").toString();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='son_lf fl']/h2/strong/text()").toString();
        String versionTemp = html.xpath("//div[@class='son_lf fl']/h2/em/text()").toString();
        String appVersion = null != versionTemp ? versionTemp.replace("【", "").replace("】", "") : null;
        String appDownloadUrl = String.format("http://a.tgbus.com/download/%s/1", StringUtils.substringBetween(appId, "item-", "/"));
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='sontxt fl']/ol/li[5]/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='sontxt fl']/ol/li[7]/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='son_fx'][3]/text()").get();
        List<String> appScreenshot = html.xpath("//tr[@class='portal-item-screenshots']/td/a/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//p[@class='dqwz']/b/a[3]/text()").get();
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
