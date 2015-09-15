package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

public class ZZZ_PagePro91_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZZZ_PagePro91_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = page.getHtml().xpath("//div[@class='s_title clearfix']/h1/text()").toString();
        String appVersion = page.getHtml().xpath("//ul[@class='s_info']/li[1]/text()").toString();
        appVersion = null != appVersion ? appVersion.substring(appVersion.indexOf("：")+1,appVersion.length()) : null;
        String appDownloadUrl = page.getHtml().xpath("//div[@class='fr s_btn_box clearfix']/a/@href").toString();
        String osPlatform = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[8]/text()").toString(), "：");
        String appSize = page.getHtml().xpath("//ul[@class='s_info']/li[3]/text()").toString();
        appSize = null != appSize ? appSize.substring(appSize.indexOf("：")+1,appSize.length()) :null;
        String appUpdateDate = StringUtils.substringAfterLast(page.getHtml().xpath("//ul[@class='s_info']/li[5]/text()").toString(), "：");
        String appType = null;

        String appDescription = html.xpath("//div[@class='o-content']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='lstImges']/img/@src").all();
        String appTag = StringUtils.join(html.xpath("//ul[@class='s_info']/li[10]/a/text()").all(), ",");
        String appCategory = html.xpath("//div[@class='crumb clearfix']/a[2]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = StringUtils.substringAfterLast(page.getHtml().xpath("//ul[@class='s_info']/li[2]/text()").toString(), "：");

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
