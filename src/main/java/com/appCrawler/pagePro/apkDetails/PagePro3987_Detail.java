package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 统一下载站[中国] app搜索抓取
 * url:http://www.3987.com/shouji/index.php?m=search&c=index&a=init&typeid=2&q=MT
 *
 * @version 1.0.0
 */
public class PagePro3987_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro3987_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        List<String> infos = html.xpath("//div[@class='detail_con clearfix']/p/text()").all();

        int infosSize = infos.size();
        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//h1[@class='info_title']/text()").toString();
        String appVersion = infosSize > 3 ? infos.get(4) : null;//html.xpath("//li[@class='versions']/text()").toString();
        String appDownloadUrl = html.xpath("//div[@class='ulw']/a/@href").toString();//html.xpath("//div[@class='down_li clearfix']/ul/li[1]/a/@href").toString();
        String osPlatform = infosSize > 4 ? infos.get(5) : null;//html.xpath("//ul[@class='detailed_msg clearfix']/li[6]/text()").toString();
        String appSize = infosSize > 2 ? infos.get(3) : null;//html.xpath("//ul[@class='detailed_msg clearfix']/li[3]/text()").toString();
        String appUpdateDate = infosSize > 0 ? infos.get(1) : null;//html.xpath("//ul[@class='detailed_msg clearfix']/li[4]/text()").toString();
        String appType = null;

        String appDescription = html.xpath("//div[@class='intro-box-txt']").get();
        List<String> appScreenshot = html.xpath("//ul[@class='s-content'][1]/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='position']/a[3]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = html.xpath("//span[@id='hits']/text()").toString();;

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
