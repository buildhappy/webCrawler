package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 搜狐应用中心[中国] app搜索抓取
 * url:http://app.sohu.com/search/?words=MT&page=2
 *
 * @version 1.0.0
 */
public class PageProSoHu_Detail {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageProSoHu_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='gjyyTitM']/div[@class='gy_01']/div/strong/text()").toString();
        String moreInfo = html.xpath("//div[@class='gy_02']/ul/li[2]/text()").toString();
        String appVersion = StringUtils.substringBetween(moreInfo, "版本： ", "  ");
        String appDownloadUrl = html.xpath("//div[@class='gjyyTitM']/div[@class='gy_03 clear']/div[2]/a/@href").toString();
        String osPlatform = StringUtils.substringBetween(html.xpath("//div[@class='gy_02']/ul/li[1]/text()").toString(), "固件： ", "  ");
        String appSize = StringUtils.substringBetween(moreInfo, "大小： ", "分类").replaceAll(" ", "");
        String appUpdateDate = StringUtils.substringBetween(moreInfo, "时间： ", "   ");
        String appType = null;
        String appDescription = html.xpath("//div[@class='gy_cont'][1]/div/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='gj_middle_inner']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//p[@class='daohang']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = StringUtils.substringBetween(html.xpath("//div[@class='gy_02']/ul/li[1]/text()").toString(), "下载： ", "  ");;

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
