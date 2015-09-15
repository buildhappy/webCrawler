package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安卓乐园[中国] app搜索抓取
 * url:http://search.520apk.com/cse/search?q=QQ&s=17910776473296434043&nsid=1
 *
 * @version 1.0.0
 */
public class PagePro520apk_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro520apk_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='app_top']/h1/text()").toString();
        String appVersion = html.xpath("//div[@class='app_top']/div[@class='app_right']/p[1]/span[2]/text()").toString();
        String appDownloadUrl = html.xpath("//ul[@class='dl-bd clearfix'][1]/li[@class='bd_ico']/a/@href").toString();
        String osPlatform = html.xpath("//div[@class='app_top']/div[@class='app_right']/p[2]/span[3]/text()").toString();
        String appSize = html.xpath("//div[@class='app_top']/div[@class='app_right']/p[2]/span[1]/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='app_top']/div[@class='app_right']/p[2]/span[2]/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@id='detailed']/html()").get().replaceAll("<[^>]+>|", "").replaceAll("\\s*|\t|\r|\n", "");
        List<String> appScreenshot = html.xpath("//div[@class='scrollable']/ul/li/a/img/@src").all();
        String appTag = StringUtils.join(html.xpath("//div[@class='app_right']/p[3]/a/text()").all(), ",");
        String appCategory = html.xpath("//div[@class='position pt10']/a[3]/text()").get();
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
