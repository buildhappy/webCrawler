package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 魅族溜[中国] app搜索抓取
 * url:http://m.mz6.net/search/?keyword=MT&dosumbit=1&m=wap&a=search&modelid=14
 *
 * @version 1.0.0
 */
public class PageProMz6_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMz6_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='header-content']/span/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//div[@class='content']/p[@class='game-info']/span[3]/text()").get(), "版本");
        String appDownloadUrl = html.xpath("//span[@class='btn btn-sec-p']/b/a/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='content']/p[@class='game-info']/span[2]/text()").get();
        String appUpdateDate = null;
        String appType = null;

        String appDescription = html.xpath("//div[@id='intro_s']/p[1]/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='app-detail-screen']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = null;
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
