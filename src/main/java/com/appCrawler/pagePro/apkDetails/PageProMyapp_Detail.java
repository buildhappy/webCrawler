package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 应用宝[中国] app搜索抓取
 * url:http://android.myapp.com/myapp/searchAjax.htm?kw=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA&pns=&sid=
 *
 * @version 1.0.0
 */
public class PageProMyapp_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMyapp_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='det-name-int']/text()").toString();
        String appVersion = html.xpath("//div[@class='det-othinfo-data']/text()").get();
        String appDownloadUrl = html.xpath("//a[@class='det-down-btn']/@data-apkurl").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='det-size']/text()").toString();
        String appUpdateDate = html.xpath("//div[@id='J_ApkPublishTime']/@data-apkPublishTime").get();
        String appType = null;

        String appDescription = html.xpath("//div[@class='det-app-data-info']/text()").get();
        List<String> appScreenshot = html.xpath("//span[@id='J_PicTurnImgBox']/div/img/@data-src").all();
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
