package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 易用汇[中国] app搜索抓取
 * url:http://www.anzhuoapk.com/search/QQ-1/
 *
 * @version 1.0.0
 */
public class PageProAnZhuoApk_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnZhuoApk_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='content1_top']/div[@class='content1_top_txt z']/h1/text()").toString();
        String appVersion = StringUtils.substringBetween(html.xpath("//div[@class='content1_top']/div[@class='content1_top_txt z']/h1/text()").toString(), "(", ")");
        if(null != appVersion)
        	appVersion = appVersion.trim();
        String appDownloadUrl = html.xpath("//div[@class='content1_bottom']/a[1]/@href").toString();
        String osPlatform = null;
        String appSize = StringUtils.substringAfter(html.xpath("//div[@class='content1_top']/div[@class='content1_top_txt z']/div[@class='ctxt']/span[4]/text()").toString(), "文件大小：");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//div[@class='content1_top']/div[@class='content1_top_txt z']/div[@class='ctxt']/span[5]/text()").toString(), "上架时间：");
        String appType = null;

        String appDescription = html.xpath("//div[@class='content3 content_title']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='botton-scroll']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='position']/a[3]/text()").get();
        String appCommentUrl = "http://www.anzhuoapk.com" + html.xpath("//div[@id='comment_1']/@url").get();
        String appComment = null;
        String dowloadNum = StringUtils.substringAfter(html.xpath("//div[@class='content1_top']/div[@class='content1_top_txt z']/div[@class='ctxt']/span[3]/text()").toString(), "：");

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
