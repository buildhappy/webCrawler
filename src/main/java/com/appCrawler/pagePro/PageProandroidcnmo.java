package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProandroidcnmo_Detail;
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 安卓中国[中国] app搜索抓取
 * url:http://app.cnmo.com/searchAll.php?s=qq&ts=1423376176047
 *
 * @version 1.0.0
 */
public class PageProandroidcnmo implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProandroidcnmo.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
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
        if (page.getUrl().regex("http://app\\.cnmo\\.com/search.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());
            //匹配具体app页面
            List<String> url1 = page.getHtml().links("//ul[@class='ResList']/li").regex("http://app\\.cnmo\\.com/android/.*").all();
            //匹配下一页
            List<String> url2 = page.getHtml().links("//div[@class='page']").regex("http://app.cnmo.com/search/.*").all();
            url1.addAll(url2);


            //add the urls to page
            Set<String> sets = Sets.newHashSet(url1);
            for (String url : sets) {
                if (PageProUrlFilter.isUrlReasonable(url)) {
                    page.addTargetRequest(url);
                }
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://app.cnmo.com/android/.*").match()) {
        	return PageProandroidcnmo_Detail.getApkDetail(page);
  
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
