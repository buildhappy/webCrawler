package com.appCrawler.pagePro;

import com.appCrawler.utils.PropertiesUtil;
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
import java.util.Set;

/**
 * 安卓中文网(tgbus)[中国] app搜索抓取
 * url:http://a.tgbus.com/game/, http://a.tgbus.com/soft/
 *网站搜索框不可用了
 * @version 1.0.0
 */
public class PageProTgbus implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProTgbus.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.baidu\\.com/baidu\\?word=.*&tn=bds&cl=3&si=a\\.tgbus\\.com&ct=2097152").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='content_left']/div/h3").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.baidu\\.com/link\\?url=.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            String appId = html.xpath("//div[@class='sontxt fl']/strong/a/@href").toString();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='son_lf fl']/h2/strong/text()").toString();
            String versionTemp = html.xpath("//div[@class='son_lf fl']/h2/em/text()").toString();
            String appVersion = null != versionTemp ? versionTemp.replace("【", "").replace("】", "") : null;
            String appDownloadUrl = String.format("http://a.tgbus.com/download/%s/1", StringUtils.substringBetween(appId, "item-", "/"));
            String osPlatform = null;
            String appSize = html.xpath("//div[@class='sontxt fl']/ol/li[5]/text()").toString();
            String appUpdateDate = html.xpath("//div[@class='sontxt fl']/ol/li[7]/text()").toString();
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
