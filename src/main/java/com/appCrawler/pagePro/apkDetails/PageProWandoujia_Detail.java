package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 /**
 * * 豌豆荚官方[中国] app搜索抓取
 * url:http://www.wandoujia.com/search?key=MT&source=apps
 * 评论网址：
 * http://apps.wandoujia.com/api/v1/comments/primary?packageName=com.sesame.dwgame.xiyou.ky
 * 只需修改后面的包名即可。
 * @version 1.0.0
 */
public class PageProWandoujia_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMM10086_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='app-info']/p[@class='app-name']/span/text()").toString();
        String appVersion = html.xpath("//dl[@class='infos-list']/dd[4]/text()").get();
        String appDownloadUrl = html.xpath("//div[@class='download-wp']/a[1]/@href").toString();
        String osPlatform = html.xpath("//dl[@class='infos-list']/dd[5]/text()").get();
        String appSize = html.xpath("//dl[@class='infos-list']/dd[1]/text()").get();
        String appUpdateDate = html.xpath("//dl[@class='infos-list']/dd[3]/time/@datetime").get();
        String appType = null;

        String appDescription = html.xpath("//div[@class='editorComment']/div/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='overview']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='crumb']/div[@class='second']/a/span/text()").get();
        String appCommentUrl = "http://apps.wandoujia.com/api/v1/comments/primary?packageName=" + StringUtils.substringBetween(appDownloadUrl, "apps/", "/download");;
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
