package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 手游天下[中国] app搜索抓取
 * url:http://android.155.cn/search.php?kw=MT&index=soft
 *
 * @version 1.0.0
 */
public class PagePro155_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro155_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='left_icon']/h1/text()").toString();
        String appVersion = html.xpath("//div[@class='left_icon']/h2/text()").toString();
        String appDownloadUrl = html.xpath("//div[@class='bottom_down']/a/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='xinxi_center']/p[3]/span/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='xinxi_center']/p[7]/span/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='jianjie_cent']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='snapShotCont']/div/img/@src").all();
        String appTag = null;
        String appCategory = null;
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = html.xpath("//div[@class='xinxi_center']/p[4]/span/text()").toString();;

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
