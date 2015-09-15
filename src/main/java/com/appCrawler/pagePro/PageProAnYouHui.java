package com.appCrawler.pagePro;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 安友汇[中国] app搜索抓取
 * url:http://www.anyouhui.com/search/MT/
 *
 * @version 1.0.0
 */
public class PageProAnYouHui implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnYouHui.class);

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
        if (page.getUrl().regex("http://www.anyouhui.com/search/*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//ul[@class='list_pro']/li").all();
            urlList.addAll(page.getHtml().links("//div[@class='page']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www.anyouhui.com/detail/*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='fr w465 mt5']/h1/text()").toString().split(" ")[0];
            String appVersion = html.xpath("//div[@class='fr w465 mt5']/h1/text()").toString().split(" ")[1];
            String appDownloadUrl = html.xpath("//div[@class='mt20 img_01']/a/@href").toString();
            String osPlatform = StringUtils.substringAfterLast(html.xpath("//div[@class='phone']/div/div[5]/text()").toString(), "：");
            String appSize = null;
            String appUpdateDate = StringUtils.substringAfterLast(html.xpath("//div[@class='phone']/div/div[1]/text()").toString(), "：");
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
