package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 淘宝手机助手 app搜索抓取
 * url:http://android.25pp.com/?spm=0.0.0.0.JmuMUt&taobao
   id:181
 * @version 1.0.0
 */
public class PagePro25pp_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro25pp_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='txt']/h1/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//div[@class='txt']/ul/li[1]/text()").get(), "版本：");
        String appDownloadUrl = html.xpath("//div[@class='aoubtL']/a/@href").toString();
        String osPlatform = StringUtils.substringAfter(html.xpath("//div[@class='txt']/ul/li[5]/text()").get(), "系统要求：");
        String appSize = StringUtils.substringAfter(html.xpath("//div[@class='txt']/ul/li[3]/text()").get(), "大小： ");
        String appUpdateDate = null;
        String appType = null;

        String appDescription = html.xpath("//div[@class='conTxt']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='pic']/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='txt']/ul/li[2]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = html.xpath("//div[@class='downMunber']/ul/li[1]/span/text()").get();

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
