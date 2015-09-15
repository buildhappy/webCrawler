package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 新浪手机软件[中国] app搜索抓取
 * url:http://down.tech.sina.com.cn/3gsoft/iframelist.php?classid=0&keyword=QQ&tag=&osid=&order=&page=2
 *
 * @version 1.0.0
 */
public class PageProSinaTech_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProSinaTech_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='b_cmon']/h1/text()").toString();
        String appVersion = StringUtils.substringAfter(appName, " ");
        String appDownloadUrl = html.xpath("//div[@class='share']/a/@href").toString();
        String osPlatform = StringUtils.substringAfter(html.xpath("//ul[@class='zcwords  clearfix']/li[6]/text()").get(), "：");
        String appSize = StringUtils.substringAfter(html.xpath("//ul[@class='zcwords  clearfix']/li[2]/p[2]/text()").get(), "：");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//ul[@class='zcwords  clearfix']/li[3]/p[1]/text()").get(), "：");
        String appType = null;

        String appDescription = html.xpath("//div[@class='zcblk']/p[2]/text()").get();
        List<String> appScreenshot = null;
        String appTag = null;
        String appCategory = html.xpath("//div[@class='blkBreadcrumb']/a[2]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = StringUtils.substringAfterLast(html.xpath("//ul[@class='zcwords  clearfix']/li[7]/text()").get(), "：");;

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
