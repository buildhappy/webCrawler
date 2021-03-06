package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProCe1111_Detail;
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
 * Cell11[美国] app搜索抓取
 * url:http://www.cell11.com/android/?s=MT
 *
 * @version 1.0.0
 */
public class PageProCe1111 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProCe1111.class);

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
        if (page.getUrl().regex("http://www\\.cell11\\.com/android/\\?s=.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='content']/div[@class='loops-wrapper']/article/figure").all();

            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);
    		for (String temp : cacheSet) {
    			if(!temp.contains("http://www.cell11.com/android/wp-content/uploads/downloads"))				
    				page.addTargetRequest(temp);
    		}


            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

     // 获取信息
        if (page.getUrl().regex("http://www\\.cell11\\.com/android/.*").match()) {
        	return PageProCe1111_Detail.getApkDetail(page);
			

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
