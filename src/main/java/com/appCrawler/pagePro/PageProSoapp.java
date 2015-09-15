package com.appCrawler.pagePro;

import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PageProSoapp_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 搜应用 app搜索抓取
 * url:http://www.souapp.com/
 *
 * @version 1.0.0
 */
public class PageProSoapp implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProSoapp.class);

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

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.souapp\\.com").match()) {
            // 获取dom对象
//            Html html = page.getHtml();
//
//            // 找出对应需要信息
//            String appDetailUrl = page.getUrl().toString();
//            String appName = "育儿专家指导";
//            String appVersion = null;
//            String appDownloadUrl = html.xpath("//a[@class='am-btn am-btn-success am-btn-sm']/@href").get();
//            String osPlatform = null;
//            String appSize = null;
//            String appUpdateDate = null;
//            String downloadNum = null;
//            String appDesc = html.xpath("//article[@class='am-paragraph am-paragraph-default am-no-layout']/text()").get();
//            String appType = null;
//
//            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);
//
//            if (null != appName && null != appDownloadUrl) {
//                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
//                apk.setAppDescription(appDesc);

                return PageProSoapp_Detail.getApkDetail(page);
            
        }

        return null;
    }

    /**
     * get the site settings
     *
     * @return site
     * @see us.codecraft.webmagic.Site
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
