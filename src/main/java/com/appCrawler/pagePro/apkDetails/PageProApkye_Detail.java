package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 *安卓在线[中国] app搜索抓取
 * url:http://www.apkye.com/?s=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA&x=7&y=5
 *
 * @version 1.0.0
 */
public class PageProApkye_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProApkye_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//h2[@class='entry_title']/text()").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@id='download']/a/@href").toString();
        String osPlatform = null;
        String appSize = null;
        String appUpdateDate = html.xpath("//span[@class='date']/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@id='entry']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='mj_tu_1']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='browse']/a[3]/text()").get();
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
