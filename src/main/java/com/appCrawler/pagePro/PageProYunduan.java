package com.appCrawler.pagePro;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;

/**
 * 云端 app搜索抓取
 * url:http://www.17et.com/,http://read.yunduan.cn/
 *
 * @version 1.0.0
 */
public class PageProYunduan implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProYunduan.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 加入加入页面
        page.addTargetRequest("http://www.17et.com");
        page.addTargetRequest("http://read.yunduan.cn");

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.17et\\.com").match()) {
            System.out.println("ddddd");
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = "统统免费电话";
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
        }

        // 获取信息
        if (page.getUrl().regex("http://read\\.yunduan\\.cn").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = "云端阅读";
            String appVersion = null;
            String appDownloadUrl = html.xpath("//a[@class='btn_android']/@href").get();
            String osPlatform = null;
            String appSize = null;
            String appUpdateDate = null;
            String downloadNum = null;
            String appDesc = html.xpath("//div[@class='inner']/dl[@class='dl_style']/text()").get();
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);

            if (null != appName && null != appDownloadUrl) {
                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
                apk.setAppDescription(appDesc);

                return apk;
            }
        }

        return null;
    }

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    @Override
    public Site getSite() {
        return site;
    }

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
}
