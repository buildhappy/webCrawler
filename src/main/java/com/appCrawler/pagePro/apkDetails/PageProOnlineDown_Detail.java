package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.Set;

/**
 * 华军软件园[中国] app搜索抓取
 * url:http://search.newhua.com/search_list.php?searchname=MT&searchsid=6&app=search&controller=index&action=search&type=news
 *
 * @version 1.0.0
 */
public class PageProOnlineDown_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProOnlineDown_Detail.class);

    public static Apk getApkDetail(Page page, Set<Apk> resSet){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//div[@class='appinfo']/div[@class='app_name']/h2/span[1]/text()").toString();
        String appVersion = html.xpath("//div[@class='appinfo']/div[@class='app_other']/ul/li[2]/text()").toString();
        String appDownloadUrl = html.xpath("//div[@class='appinfo']/div[@class='download']/a[1]/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='appinfo']/div[@class='app_other']/ul[1]/li[4]/div/text()").toString();
        String appUpdateDate = html.xpath("//div[@class='appinfo']/div[@class='app_other']/ul/li[8]/div/text()").toString();
        String appType = null;
        String appDescription = html.xpath("//div[@class='infobox']/html()").get();
        if(appDescription!=null)  appDescription=appDescription.replaceAll("(<[^>]+>)|\\s*|\t|\r|\n", "");
        List<String> appScreenshot = html.xpath("//div[@id='slide08']/ul/li/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//div[@class='app_other']/ul[1]/li[3]/text()").toString();
        String appCommentUrl=null;// = "http://newhua.duoshuo.com/api/threads/listPosts.json?container_url=" + page.getUrl().get();
        String appComment = null;
        String dowloadNum = null;

        Apk apk = null;
        if (page.getUrl().regex("http://www.onlinedown.net/soft/*").match()) {
            if (null != appName && null != appDownloadUrl) {
                apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
                apk.setAppDescription(appDescription);
                apk.setAppScreenshot(appScreenshot);
                apk.setAppCommentUrl(appCommentUrl);
                apk.setAppComment(appComment);
                apk.setAppDownloadTimes(dowloadNum);
                apk.setAppCategory(appCategory);
                apk.setAppTag(appTag);

                // 加入返回集合
                resSet.add(apk);

                // 再次抓取下载地址
                page.addTargetRequest(appDownloadUrl);
            }
        }

        // 更新下载地址
        if(page.getUrl().regex("http://www\\.onlinedown\\.net/softdown/*").match()) {
            for (Apk apk_new : resSet) {
                if (apk_new.getAppDownloadUrl().equals(page.getUrl().get())) {
                    // 获取真实的url
                    String url = page.getHtml().xpath("//div[@class='down-menu']/a[1]/@href").get();
                    apk_new.setAppDownloadUrl(url);

                    LOGGER.debug(apk_new.getAppName() + " real download url : {}", url);

                    return apk_new;
                }
            }
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return null;
    }
}
