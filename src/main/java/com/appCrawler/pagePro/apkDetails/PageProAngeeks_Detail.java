package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安极市场[中国] app搜索抓取
 * url:http://apk.angeeks.com/search?keywords=MT&x=0&y=0
 *
 * @version 1.0.0
 */
public class PageProAngeeks_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAngeeks_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//dl[@class='clear_div hr']/dd[1]/a/text()").toString();
        String appVersion = StringUtils.substringAfterLast(html.xpath("//dl[@class='clear_div hr']/dd[1]/span/text()").toString(), "：");
        String appDownloadUrl = html.xpath("//div[@class='rgmainsrimg'][1]/a/@href").toString();
        String osPlatform = null;
        String appSize = StringUtils.substringAfter(html.xpath("//div[@class='rgmainslx']/span[1]/text()").get(), "：");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//div[@class='rgmainslx']/span[2]/text()").get(), "：");
        String appType = null;

        String appDescription = html.xpath("//p[@id='summore']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='gdq']/div/dl/dt/a/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='wz2']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = StringUtils.substringAfter(html.xpath("//div[@class='rgmainslx']/span[3]/text()").get(), "：");;

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
