package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 搜狗市场[中国] app搜索抓取
 * url:http://app.sogou.com/search?title=MT
 *
 * @version 1.0.0
 */
public class PageProSoGou_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProSoGou_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='content']/div[@class='d-title cf']/em/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//ul[@class='dd cf']/li[4]/text()").get(), "版本：");
        String appDownloadUrl = html.xpath("//div[@class='down_pc cf']/a/@href").toString();
        String osPlatform = StringUtils.substringAfter(html.xpath("//ul[@class='dd cf']/li[5]/text()").get(), "系统要求：");
        String appSize = StringUtils.substringAfter(html.xpath("//ul[@class='dd cf']/li[3]/text()").get(), "大小：");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//ul[@class='dd cf']/li[2]/text()").get(), "更新时间：");
        String appType = null;

        String appDescription = html.xpath("//div[@class='detail_content']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='makeMeScrollable']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='sub_nav cf']/a[3]/text()").get();
        String appCommentUrl = "http://app.sogou.com/port/detail/getCommentList/0/1/1/" + StringUtils.substringAfterLast(page.getUrl().get(), "/") + "/";
        String appComment = null;
        String dowloadNum = StringUtils.substringAfter(html.xpath("//ul[@class='dd cf']/li[1]/text()").get(), "下载量：");;

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
