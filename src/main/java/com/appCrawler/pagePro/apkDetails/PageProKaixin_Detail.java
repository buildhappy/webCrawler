package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 开心手游[中国] app搜索抓取
 * url:http://www.kaixinshouyou.com/sou_youxi.asp
 *
 * @version 1.0.0
 */
public class PageProKaixin_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProKaixin_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='gameInfoBg']/div[@class='leftType']/h1/text()").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@class='gameInfoBg']/p[@class='d']/a[1]/@href").toString();
        if(appDownloadUrl!=null&&!appDownloadUrl.endsWith(".apk")){
        	appDownloadUrl=null;
        }
        String osPlatform = null != appDownloadUrl && appDownloadUrl.endsWith(".apk") ? "Android" : null;
        String appSize = null;//html.xpath("//div[@class='leftCol gamesDetailsCol']/div[@class='text']/p[3]").get();
        appSize = StringUtils.isNotEmpty(appSize) ? StringUtils.substringBetween(appSize, "：</strong>", "<br />").replace(" ", "") : null;
        String appUpdateDate = null;
        String appType = null;

        //String appDescription = html.xpath("//div[@class='text']/p[1]/text()").get();
        String appDescription = html.xpath("//div[@class='gameInfoBg']/div/ul/li/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='mj_tu_1']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='breadcrumb']/p/a[2]/text()").get();
        String appCommentUrl = null;
        String appComment = html.xpath("//div[@class='commentlist']").get();
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
            apk.setAppComment(appComment);
            apk.setAppCategory(appCategory);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
