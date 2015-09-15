package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import com.appCrawler.utils.JSParserForShuiGuo;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 游戏基地[中国] app搜索抓取
 * url:http://s.shuiguo.com/qq_1_1.html
 *
 * @version 1.0.0
 */
public class PageProShuiGuo_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProShuiGuo_Detail.class);

    public static Apk getApkDetail(Page page) throws Exception{
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='f_cont']/h2/samp/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//div[@class='f_gameinfo1']/ul/li[2]/text()").get(), "：");
        String appDownloadUrl = JSParserForShuiGuo.getDownloadUrl(appDetailUrl); //= html.xpath("//a[@class='sd_xz sd_az1']/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='f_a_right']/h3/span/text()").get();
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//div[@class='f_gameinfo1']/ul/li[1]/text()").get(), "：");
        String appType = null;

        String appDescription = html.xpath("//div[@id='xqcon']/p[1]/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='imgScrollDiv']/ul/li/@data-src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='f_crumb']/a[2]/text()").get();
        String appCommentUrl = null;
    //    String appComment = html.xpath("//div[@id='xqcon']").get();
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
     //       apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appDescription);

        return apk;
    }
}
