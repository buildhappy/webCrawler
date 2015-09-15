package com.appCrawler.pagePro.apkDetails;

import com.google.common.collect.Lists;

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
public class PageProMM10086_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMM10086_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='mj_big_title font-f-yh']/span/text()").toString();
        String appVersion = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[3]/text()").toString(), "：");
        String appDownloadUrl = html.xpath("//div[@class='mj_cont_left_t']/a[2]/@href").toString();
        String osPlatform = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[8]/text()").toString(), "：");
        String appSize = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[4]/text()").toString(), "：");
        String appUpdateDate = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[7]/text()").toString(), "：");;
        String appType = null;

        String appDescription = html.xpath("//div[@class='mj_yyjs font-f-yh']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='mj_tu_1']/img/@src").all();
        String appTag = null;
        String appCategory = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[6]/text()").toString(), "：");;
        String appCommentUrl = null;
        String appComment = null;//html.xpath("//div[@id='h_d']").get();
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
