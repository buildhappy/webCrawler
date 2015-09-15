package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 7230手游网[中国] app搜索抓取
 * url:http://www.7230.com/search.asp?keyword=MT
 *
 * @version 1.0.0
 */
public class PagePro7230_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro7230_Detail.class);

    public static Apk getApkDetail(Page page, Set<String> downlaodSet){
        // 获取dom对象
        Html html = page.getHtml();

        // 获取所有的下载地址,并找出对应的下载地址
        String appDownloadUrl = html.xpath("//div[@class='down-btn']/a/@href").toString();
        if(appDownloadUrl==null){
        String pageId = StringUtils.substringBefore(StringUtils.substringAfterLast(page.getUrl().get(), "/"), ".");
        Iterator<String> iter = downlaodSet.iterator();
        while (iter.hasNext()) {
            String url = iter.next();
            if (url.contains(pageId)) {
                appDownloadUrl = url;
            }
        }
        }
        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//h1[@class='title']/text()").toString();
        String appVersion = html.xpath("//div[@class='info clearfix']/div[@class='infoTop clearfix']/div[@class='txt']/div/span/text()").toString();
        String osPlatform = StringUtils.substringAfter(html.xpath("//div[@class='moreInfo']/p[2]/text()").toString(), "适用：");
        String appSize = StringUtils.substringAfter(html.xpath("//div[@class='moreInfo']/p[1]/em/text()").toString(), "大小：");
        String appUpdateDate = html.xpath("//div[@class='info clearfix']/div[@class='infoTop clearfix']/div[@class='txt']/p[2]/em/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='qbnr']/p/text()").get();
        appDescription += html.xpath("//div[@class='qbnr']/p[1]/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='xxx']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//p[@class='page-nav']/a[2]/text()").get();
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
            if(appDownloadUrl.endsWith(".zip")){
            	apk.setAppType("zip");
            }
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
