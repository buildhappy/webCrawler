package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 乐商店[中国] app搜索抓取
 * url:http://www.lenovomm.com/search/index.html?q=MT
 *
 * @version 1.0.0
 */
public class PageProLenovomm_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProLenovomm_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='ff-wryh detailAppname txtCut']/h1/text()").toString();
        String appVersion = html.xpath("//ul[@class='detailAppInfo fl']/li[2]/span/text()").toString();
        String appDownloadUrl = html.xpath("//ul[@class='detailTop2 fgrey5']/li[1]/a/@href").toString();
        String osPlatform = html.xpath("//ul[@class='detailAppInfo fl']/li[3]/span/text()").toString();
        String appSize = html.xpath("//ul[@class='detailAppInfo fl']/li[1]/span/text()").toString();
        String appUpdateDate = html.xpath("//ul[@class='detailAppInfo fl']/li[5]/span/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='introCon']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='pr oh']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='w1000 bcenter']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = html.xpath("//span[@class='fgrey5']/text()").toString();
        if(dowloadNum != null){
        	dowloadNum = dowloadNum.split("下载：")[1];
        	dowloadNum = dowloadNum.split("次安装")[0];
        	
        }

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
