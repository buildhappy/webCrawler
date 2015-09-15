package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.LinkedList;
import java.util.List;

/**
 * 安果网[中国] app搜索抓取
 * url:http://www.91anguo.com/e/action/ListInfo.php?&classid=16&softfj=%E5%AE%89%E5%8D%93
 *
 * @version 1.0.0
 */
public class PageProAnGuo_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnGuo_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();

        String appName = html.xpath("//div[@class='box01 appxx']/div[@class='gameface']/text()").toString();
        String appVersion = StringUtils.substringAfter(html.xpath("//div[@class='box01 appxx']/ul[@class='gameinfo']/li[1]/text()").get(), "版本：");
        String appDownloadUrl = html.xpath("//div[@class='box01 dlurl']/a[1]/@href").toString();
        String osPlatform = html.xpath("//div[@class='box01 appxx']/ul[@class='gameinfo']/li[10]/span/text()").get();
        String appSize = StringUtils.substringAfter(html.xpath("//div[@class='box01 appxx']/ul[@class='gameinfo']/li[5]/text()").get(), "安卓：");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//div[@class='box01 appxx']/ul[@class='gameinfo']/li[7]/text()").get(), "更新：");
        String appType = null;

        String appDescription = html.xpath("//div[@id='izone']/p/text()").get();
        List<String> appScreenshot1 = html.xpath("//ul[@class='gameimg']/li/a/img/@src").all();
        List<String> appScreenshot = new LinkedList<String>();
        for(String url : appScreenshot1){
        	if(!url.startsWith("http://www.91anguo.com")){
        		url = "http://www.91anguo.com" + url;
        	}
        	appScreenshot.add(url);
        }
        
        String appTag = null;
        String appCategory = html.xpath("//h1[@class='path f_l']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;

        
        
        Apk apk = null;
        if (null != appName && null != appDownloadUrl && appDownloadUrl.length() > 10 && !appDownloadUrl.equals("appDetailUrl")
        		&&(osPlatform.contains("安卓")|| osPlatform.toLowerCase().contains("android"))) {
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
