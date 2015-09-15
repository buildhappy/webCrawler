package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 百信众联[中国] app搜索抓取
 * url:http://www.958shop.com/apk/search.aspx?wd=MT
 *
 * @version 1.0.0
 */
public class PagePro958shop_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro958shop_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='soft-summary clearfix']/h1/strong/text()").toString();
        String appVersion = null;
        String appDownloadUrl = StringUtils.substringBetween(html.get(), "window.location.href=\"", "\";");
        //String appDownloadUrl = html.xpath("//div[@class='down-button-box h-down-box']/a/@href").toString();
        String osPlatform = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[3]/text()").toString();
        String appSize = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[1]/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[7]/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='summary-text']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='screenshot-out']/table/tbody/tr/td/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='nav-path']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[6]/text()").toString();;

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
