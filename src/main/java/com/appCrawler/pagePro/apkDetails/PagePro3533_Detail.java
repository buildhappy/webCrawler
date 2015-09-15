package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 手机世界[中国] app搜索抓取
 * url:http://search.3533.com/game?keyword=DOTA
 *
 * @version 1.0.0
 */
public class PagePro3533_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro3533_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='rowleft']/div[@class='infos']/div[@class='block']/div[@class='title']/h1/text()").toString();
        String appVersion = StringUtils.substringAfterLast(html.xpath("//div[@class='packclear']/div[@class='packright']/div[@class='packinfo']/p/span[1]/text()").toString(), "：");
        String appDownloadUrl = html.xpath("//div[@class='packclear']/div[@class='packright']/div[@class='packinfo']/div[@class='packdown']/a/@href").toString();
        String osPlatform = StringUtils.substringAfterLast(html.xpath("//div[@class='packclear']/div[@class='packright']/div[@class='packinfo']/p/span[4]/span[1]/text()").toString(), "：");
        String appSize = StringUtils.substringAfterLast(html.xpath("//div[@class='packclear']/div[@class='packright']/div[@class='packinfo']/p/span[2]/text()").toString(), "：");
        String appUpdateDate = null;
        String appType = StringUtils.substringAfterLast(html.xpath("//div[@class='packclear']/div[@class='packright']/div[@class='packinfo']/p/span[3]/text()").toString(), "：");
        String appDescription = html.xpath("//p[@id='introelem']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='smallpic']/ul/li/span/img/@bigsrc").all();
        String appTag = StringUtils.join(html.xpath("//a[@id='blue']/text()").all(), ",");
        String appCategory = html.xpath("//a[@itemprop='applicationsubcategory']/text()").get();
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

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}," +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appTag:{}, appCategory:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appScreenshot, appCommentUrl, appComment, appTag, appCategory, appDescription);

        return apk;
    }
}
