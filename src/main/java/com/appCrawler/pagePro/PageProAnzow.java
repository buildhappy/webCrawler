package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProAnzow_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 安卓软件园(3gyu)[中国] app搜索抓取
 * url:http://www.anzow.com/Search.shtml?stype=anzow&q=DOTA
 *
 * @version 1.0.0
 */
public class PageProAnzow implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnzow.class);

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
        if (page.getUrl().regex("http://www.anzow.com/Search.shtml\\?stype=anzow&q=*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='box boxsbg']/dl[@class='down_list pd20']/dt").all();
            urlList.addAll(page.getHtml().links("//div[@id='pages']/ul").all());

            Set<String> sets = Sets.newHashSet(urlList);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url)&& !url.contains("http://www.anzow.com/download_app.shtml?files_id=")) {
					page.addTargetRequest(url);
				}
			}
            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www.anzow.com/download/.*").match()) {
        	return PageProAnzow_Detail.getApkDetail(page);

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
