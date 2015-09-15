package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.LinkedList;
import java.util.List;

/**
 * 宝软[中国] app搜索抓取
 * url:http://www.baoruan.com/
 *
 * @version 1.0.0
 */
public class PageProBaoRuan_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProBaoRuan_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = "3D宝软桌面";
        String appVersion = null;
        String appDownloadUrl = "http://d3.17xgame.com/download/2014/11/14/j/F_A0_xm_004.apk";
        String osPlatform = "Android";
        String appSize = null;
        String appUpdateDate = null;
        String appType = "APK";

        String appDescription = null;
        //List<String> appScreenshot = html.xpath("//div[@class='box_fixed b1_fixed']/div[@class=''b1_02]/img/@src").all();
        List<String> appScreenshot = new LinkedList<String>();
        appScreenshot.add("http://static.17xgame.com/v61//images/beautify/zhuomian/images/b1_02.png");
        String appTag = null;
        String appCategory = null;
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

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
