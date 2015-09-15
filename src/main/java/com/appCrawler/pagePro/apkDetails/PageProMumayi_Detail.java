package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 木蚂蚁[中国] app搜索抓取
 * url:http://s.mumayi.com/index.php?q=MT&typeid=0
 *
 * @version 1.0.0
 */
public class PageProMumayi_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMumayi_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='iapp_hd mart10']/h1/text()").toString();
        String appVersion = StringUtils.substringAfterLast(appName, "V");
        String appDownloadUrl = html.xpath("//div[@class='ibtn fl']/a[3]/@href").toString();
        String osPlatform = html.xpath("//ul[@class='istyle fl']/li[5]/ul/li/div[1]/text()").get();
        String appSize = html.xpath("//ul[@class='istyle fl']/li[4]/text()").get();
        String appUpdateDate = html.xpath("//ul[@class='istyle fl']/li[3]/text()").get();
        String appType = null;

        String appDescription = html.xpath("//div[@class='ibox']/p[2]/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='ibox']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//ul[@class='istyle fl']/li[2]/text()").get();
        String appCommentUrl = null;
        String appComment = html.xpath("//div[@id='h_d']").get();
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
