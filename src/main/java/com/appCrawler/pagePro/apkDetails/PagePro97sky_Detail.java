package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * 97sky[中国] app搜索抓取
 * url:http://www.97sky.cn/search?type=0&keyword=qq
 *
 * @version 1.0.0
 */
public class PagePro97sky_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro97sky_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='ppdpTitleIn']/h1/text()").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//ul[@class='ppCodeList']/li/a/@href").toString();
        String osPlatform = html.xpath("//ul[@class='ppdpList']/li[2]/span/text()").toString();
        String appSize = html.xpath("//ul[@class='ppdpList']/li[1]/span/text()").toString();
        String appUpdateDate =html.xpath("//ul[@class='ppdpList']/li[4]/span/text()").toString();
        String appType = null;
        List<String> appScreenshot=new ArrayList<String>();
        String appDescription = html.xpath("//div[@class='ddDetaCont']/text()").get();
        List<String> appScreenshot0 = html.xpath("//div[@id='focus']/ul/li/a/img/@src").all();
        for(String temp:appScreenshot0){
			if(!temp.startsWith("http://www.97sky.cn")){
				temp="http://www.97sky.cn"+temp;
			}
        	appScreenshot.add(temp);
        }
       
        String appTag = null;
        String appCategory = html.xpath("//div[@class='ppSite']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl && null != osPlatform && osPlatform.contains("Android")) {
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
