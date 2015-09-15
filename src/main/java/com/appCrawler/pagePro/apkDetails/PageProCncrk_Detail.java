package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.LinkedList;
import java.util.List;

/**
 * 起点下载[中国] app搜索抓取
 * url:http://zhannei.baidu.com/cse/search?q=QQ&entry=1&s=5746876527562368484&nsid=
 *
 * @version 1.0.0
 */
public class PageProCncrk_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProCncrk_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='rightcolumn']/div[@id='softtitle']/span/text()").toString();
        String appVersion = StringUtils.substringBetween(html.xpath("//div[@class='rightcolumn']/div[@id='softtitle']/span/text()").toString(), " ", " ");
        String appDownloadUrl = html.xpath("//div[@class='down_url_box']/dl/dt[1]/a[1]/@href").toString();
        String osPlatform = html.xpath("//div[@class='rightcolumn']/div[@id='softinfo']/ul/li[4]/text()").toString();
        String appSize = html.xpath("//div[@class='rightcolumn']/div[@id='softinfo']/ul/li[1]/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='rightcolumn']/div[@id='softinfo']/ul/li[7]/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@id='softintro]/p/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='Pic_show']/div/a/img/@src").all();
        List<String> appScreenshot_2 = new LinkedList<String>();
        for(String s : appScreenshot){
        	if(!s.endsWith("http://www.")){
        		appScreenshot_2.add("http://www.cncrk.com" + s);
        	}
        	
        }
        String appTag = StringUtils.join(html.xpath("//div[@class='rightcolumn']/div[@id='softinfo']/ul/li[8]/a/text()").all(), ",");
        String appCategory = html.xpath("//div[@class='currentnav']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;//html.xpath("//div[@id='comment']").get();
        String dowloadNum = null;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl && !appDownloadUrl.endsWith(".exe")&& !appDownloadUrl.endsWith(".zip")) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot_2);
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
