package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 七匣子[中国] app搜索抓取
 * url:http://www.7xz.com/ng/search_MT_0_0_rate_1.html
 *
 * @version 1.0.0
 */
public class PagePro7xz_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro7xz_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='col-md-9 clearfix']/h1/text()").toString();
        String appVersion = html.xpath("//table[@class='table']/tbody/tr[3]/td[6]/text()").get();
        String appDownloadUrl = html.xpath("//p[@class='sprit down_android']/a/@href").toString();
        String osPlatform = html.xpath("//table[@class='table']/tbody/tr[2]/td[2]/text()").get();
        String appSize = html.xpath("//table[@class='table']/tbody/tr[3]/td[4]/text()").get();
        String appUpdateDate = null;
        String appType = null;

        String appDescription = html.xpath("//p[@class='p_detail p_l_r10']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='carousel-inner']/div/img/@src").all();
        String appTag = StringUtils.join(html.xpath("//p[@class='tag1']/a/text()").all(), ",");
        String appCategory = html.xpath("//div[@class='row positon']/div/a[2]/text()").get();
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
