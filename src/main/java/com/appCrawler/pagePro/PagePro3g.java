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
 * 3G门户[中国] app搜索抓取
 * url:http://www.goforandroid.com/zh/index.html
 *
 * @version 1.0.0
 */
public class PagePro3g implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro3g.class);

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
        if (page.getUrl().regex("http://www\\.goforandroid\\.com/zh/index\\.html").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().xpath("//div[@class='main_image']/ul/li/div/div[@class='banner_txt1']/a/@onclick").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                String url = StringUtils.substringBetween(iter.next(), "'", "'");
                page.addTargetRequest("http://www.goforandroid.com/zh/" + url);
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.goforandroid\\.com/zh/.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='softshow']/ul/li[1]/h1/text()").toString();
            String appVersion = null;
            String appDownloadUrl = html.xpath("//div[@class='softshow']/ul/li[2]/a/@href").toString();
            String osPlatform = null;
            String appSize = null;
            String appUpdateDate = null;
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate);

            if (null != appName && null != appDownloadUrl) {
                return new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            }
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
