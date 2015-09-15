package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 安软[中国] app搜索抓取
 * url:http://www.anruan.com/search.php?t=soft&keyword=MT
 *
 * @version 1.0.0
 */
public class PageProAnruan_Detial {


    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnruan_Detial.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='pn2 appun']/div[@class='pa']/div[@class='pc']/h1/text()").toString();
        String appVersion = StringUtils.trim(StringUtils.substringAfter(html.xpath("//ul[@class='c1']/li[@class='app_green']/span/text()").get(), "："));
        if(appVersion != null)
        	appVersion = appVersion.replaceAll("\\S\\s|　","");
        String appDownloadUrl = html.xpath("//li[@class='app_down']/a/@href").toString();
        String osPlatform = html.xpath("//ul[@class='c1']/li[4]/text()").get();
        String appSize = html.xpath("//ul[@class='c1']/li[6]/text()").get();
        String appUpdateDate = html.xpath("//ul[@class='c1']/li[5]/text()").get();
        String appType = null;

        String appDescription = html.xpath("//div[@class='intro']/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@class='m-screen-list JS-scroller']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='here']/a[3]/text()").get();
        String appCommentUrl = null;
       // String appComment = html.xpath("//div[@class='comment']").get();
        String dowloadNum = html.xpath("//ul[@class='c1']/li[7]/text()").get();;
if(appName==null){
	appName = html.xpath("//div[@class='txt fl']/a/text()").toString();
	appVersion = html.xpath("//ul[@class='zone_ul']/li[1]/b/text()").get();
	appDownloadUrl = html.xpath("//div[@class='zone_img']/a/@href").toString();
	appSize = html.xpath("//ul[@class='zone_ul']/li[3]/b/text()").get();
	appUpdateDate = html.xpath("//ul[@class='zone_ul']/li[4]/b/text()").get();
	appDescription = html.xpath("//div[@class='con zone_intro']/text()").get();
	appScreenshot = html.xpath("//div[@class='detail_img_list']/ul/li/img/@src").all();
	appCategory = html.xpath("//ul[@class='zone_ul']/li[2]/b/text()").get();
}
        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
         //   apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appDescription);

        return apk;
    }
}
