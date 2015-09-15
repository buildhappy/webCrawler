package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 天天手游[中国] app搜索抓取
 * url:http://www.apkyx.com/search/?key=MT&searchType=game
 *
 * @version 1.0.0
 */
public class PageProApkyx_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProApkyx_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='pright w258 rt bd1']/div[@class='gdl']/a[1]/@title").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@class='pright w258 rt bd1']/div[@class='gdl']/a[2]/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='pright w258 rt bd1']/div[@class='gdl']/div/p[1]/span[@id='showbtn']/text()").toString() + html.xpath("//div[@class='pright w258 rt bd1']/div[@class='gdl']/div/p[1]/text()").toString();
        if (null != appSize) {appSize = appSize.replaceAll(" ", "");}
        String appUpdateDate = null;
        String appType = null;

        String appDescription = null;
        List<String> appScreenshot = html.xpath("//ul[@id='x_img_viewer']/li/img/@src").all();
        String appTag = null;
        String appCategory = null;
        String appCommentUrl = null;
        String appComment = html.xpath("//div[@id='h_d']").get();
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl && appDownloadUrl.length() > 0 && appDownloadUrl.endsWith(".apk")) {
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
