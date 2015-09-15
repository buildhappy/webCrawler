package com.appCrawler.pagePro.apkDetails;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public class PagePro17et_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro17et_Detail.class);

    public static Apk getApkDetail(Page page){
    	// 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = "通通免费电话";
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@class='ni_indexdown_btn']/a[1]/@href").get();
        String osPlatform = null;
        String appSize = null;
        String appUpdateDate = null;
        String downloadNum = null;
        String appDesc = html.xpath("//div[@class='ni_index_text ni_ml20']/text()").get();
        String appType = null;

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);

        if (null != appName && null != appDownloadUrl) {
            Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDesc);

            return apk;
        }
        return null;
    }

}
