package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 手游网[中国] app搜索抓取
 * url:http://search.shouyou.com/online?keyword=ME&type=1
 *
 * @version 1.0.0
 */
public class PageProShouYou_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProShouYou_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='game-info-c2']/h1[@class='tit']/text()").toString();

        String htmlStr = html.xpath("//div[@class='game-info-c2']/ul[@class='info-list']").get();
        String appVersion = StringUtils.substringBetween(StringUtils.substringBetween(htmlStr, "最新版本：", "</li>"), ">", "<");

        String appDownloadUrl = html.xpath("//div[@class='bt-wrap']/a[@id='download1']/@href").toString();
        if (StringUtils.isEmpty(appDownloadUrl)) {
            appDownloadUrl = html.xpath("//div[@class='gb-tab-c']/span[@class='item-3']/a/@href").toString();
        }
        String osPlatform = StringUtils.substringBetween(StringUtils.substringBetween(htmlStr, "系 统：", "</li>"), ">", "<");
        String appSize = StringUtils.substringBetween(StringUtils.substringBetween(htmlStr, "大 小：", "</li>"), ">", "<");
        String appUpdateDate = StringUtils.substringBetween(StringUtils.substringBetween(htmlStr, "更新时间：", "</li>"), ">", "<");
        String appType = null;

        String appDescription = html.xpath("//div[@class='intro-box']/div[@class='hd']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='gb-sshot-con-in']/div/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='breadcrumbs']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;

        Apk apk = null;
        if(appName==null){
        	appName = html.xpath("//h1[@class='sub']/text()").toString();	
        	appDownloadUrl = html.xpath("//div[@class='discript']/div[@class='clearfix']/a/@href").toString();
        	appVersion =html.xpath("//div[@class='discript']/div[@class='clearfix']/a/@data-version").toString();
        	appSize = html.xpath("//div[@class='discript']/div[@class='clearfix']/a/span/text()").toString();
        	appUpdateDate = html.xpath("//div[@class='m-param clearfix']/div[1]/p[3]/text()").toString();
        	appDescription = html.xpath("//div[@class='xs']/html()").get().replaceAll("(<[^>]+>)|\\s*|\t|\r|\n", "");
        	appScreenshot = html.xpath("//ul[@class='piclist clearfix']/li/div/img/@src").all();
        	appCategory = html.xpath("//div[@class='m-param clearfix']/div[2]/p[2]/span/text()").get();      	
        }
 
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
