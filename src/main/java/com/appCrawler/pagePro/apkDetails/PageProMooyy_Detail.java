package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 摸鱼网[中国] app搜索抓取
 * url:http://www.mooyy.com/plus/search.php?kwtype=0&searchtype=titlekeyword&typeid=2&keyword=qq
 *
 * @version 1.0.0
 */
public class PageProMooyy_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMooyy_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='lay_main']/div[@class='m_box']/div[@class='m_hd_01']/h3/text()").toString();
        String appVersion = null;
        String appDownloadUrl = html.xpath("//div[@class='txt_list'][1]/h5/a/@href").toString();
        String osPlatform = html.xpath("//div[@class='txt_list'][1]/p[1]/text()").toString();
        String appSize = StringUtils.substringAfterLast(html.xpath("//div[@class='txt_list'][1]/h5/a/text()").toString(), "  ");
        String appUpdateDate = StringUtils.substringAfter(html.xpath("//dl[@class='game_intro']/dd[6]/text()").get(), "：");
        String appType = null;

        String appDescription = html.xpath("//div[@class='mj_yyjs font-f-yh']/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='mj_tu_1']/img/@src").all();
        String appTag = null;
        String appCategory = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[6]/text()").toString(), "：");;
        String appCommentUrl = null;
        String appComment = html.xpath("//div[@id='h_d']").get();
        String dowloadNum = null;

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
