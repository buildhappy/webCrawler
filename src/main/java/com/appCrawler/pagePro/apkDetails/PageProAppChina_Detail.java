package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 移动MM[中国] app搜索抓取
 * url:http://mm.10086.cn/searchapp?st=0&q=MT&dt=android
 *
 * @version 1.0.0
 */
public class PageProAppChina_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAppChina_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='app-detail']/div[@class='msg']/h1/text()").toString();
        String moreInfo = html.xpath("//div[@class='intro']/p[@class='art-content']/text()").toString();
        String appVersion = StringUtils.substringBetween(moreInfo, "版本：", " ");
        String appDownloadUrl = html.xpath("//div[@class='app-detail']/div[@class='download-button direct_download']/a/@href").toString();
        String osPlatform = StringUtils.substringAfter(moreInfo, " 要求：");
        String appSize = StringUtils.substringBetween(html.xpath("//div[@class='app-detail']/div[@class='msg']/span[@class='app-statistic']/text()").toString(), "：", " ");
        String appUpdateDate = StringUtils.substringBetween(moreInfo, "更新：", " ");
        String appType = null;
        //String appDescription = html.xpath("//p[@class='art-content'][1]/text()").get();
        String appDescription = html.xpath("//p[@class='art-content']/text()").get();
        List<String> appScreenshot = html.xpath("//ul[@class='app-screenshot-list']/li/img/@src").all();
        String appTag = StringUtils.join(html.xpath("//div[@class='detail-app-tag']/ul/li/a/text()").all(), ",");
        String appCategory = html.xpath("//div[@class='breadcrumb centre-content']/a[3]/text()").get();
        String appCommentUrl = null;
        //String appComment = html.xpath("//ul[@class='remark-list']").get();
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
