package com.appCrawler.pagePro.apkDetails;

import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 刷机网 http://app.imobile.com.cn/
 *
 * @author buildhappy
 *
 */
public class PageProImobile_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProImobile_Detail.class);

    public static Apk getApkDetail(Page page) {
        // 获取dom对象
        Html html = page.getHtml();

        String appName = null;                //app名字
        String appDetailUrl = null;            //具体页面url
        String osPlatform = null;            //运行平台
        String appUpdateDate = null;        //更新日期
        String appDownloadUrl = null;
        String appVersion = null;
        String appSize = null;
        String dowloadNum = null;
        String vender = null;//开发商
        String appType = null;

        appName = page.getHtml().xpath("//div[@class='box700_intro']/h1/text()").toString();
        appDetailUrl = page.getUrl().toString();

        List<String> infos = null;
        infos = page.getHtml().xpath("//ul[@id='app_params']/li/text()").all();
        for (String s : infos) {
            System.out.println(s);
        }
        int length = infos.size();
        appVersion = length > 0 ? splitString(infos.get(0)) : null;
        appUpdateDate = length > 2 ? splitString(infos.get(2)) : null;
        appSize = length > 3 ? splitString(infos.get(3)) : null;
        dowloadNum = length > 4 ? splitString(infos.get(4)) : null;
        osPlatform = length > 5 ? splitString(infos.get(5)) : null;
        vender = length > 6 ? splitString(infos.get(6)) : null;
        appDownloadUrl = page.getHtml().xpath("//div[@class='download_install']/a[@class='download']/@href").toString();


        String appDescription = page.getHtml().xpath("//div[@class='shrink_cont']/p/text()").all().toString();
        List<String> appScreenshot = html.xpath("//div[@class='smallpic_box']/ul/li/span/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='bread_crumb']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        //String dowloadNum = null;

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

    /**
     * split a string
     * eg."下载次数：19万" retrun 19万
     * @param s
     * @return
     */
    public static String splitString(String s){
        return s.split("：").length > 1 ? s.split("：")[1]:null;
    }

}
