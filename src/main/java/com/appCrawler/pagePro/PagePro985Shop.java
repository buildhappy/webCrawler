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
 * 百信众联[中国] app搜索抓取
 * url:http://www.958shop.com/apk/search.aspx?wd=MT
 *
 * @version 1.0.0
 */
public class PagePro985Shop implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro985Shop.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.958shop\\.com/apk/search.aspx\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//ul[@class='list-text clearfix']/li/dl/dt").all();
            urlList.addAll(page.getHtml().links("//div[@class='page']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.958shop\\.com/android.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='soft-summary clearfix']/h1/strong/text()").toString();
            String appVersion = null;
            String appDownloadUrl = StringUtils.substringBetween(html.get(), "window.location.href=\"", "\";");
            String osPlatform = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[3]/text()").toString();
            String appSize = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[1]/text()").toString();
            String appUpdateDate = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[7]/text()").toString();
            String downloadNum = html.xpath("//div[@class='soft-summary clearfix']/ul[@class='soft-infor']/li[6]/text()").toString();
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlad:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, downloadNum);

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
