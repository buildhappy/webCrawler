package com.appCrawler.pagePro;


import com.appCrawler.pagePro.apkDetails.PageProApkye_Detail;
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
 *安卓在线[中国] app搜索抓取
 * url:http://www.apkye.com/?s=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA&x=7&y=5
 *
 * @version 1.0.0
 */
public class PageProApkye implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProApkye.class);

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
        if (page.getUrl().regex("http://www\\.apkye\\.com/\\?s=.*").match()) {
        	
            LOGGER.debug("match success, url:{}", page.getUrl());
            
            //匹配具体app页面
            List<String> url1 = page.getHtml().links("//div[@class='archive_title']/h2").all();
            //匹配下一页
            List<String> url2 = page.getHtml().links("//div[@id='pagenavi']").all();
            url1.addAll(url2);

            //remove the duplicate urls in list
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
    if (page.getUrl().regex("http://www\\.apkye\\.com/.*").match()) {
    	return PageProApkye_Detail.getApkDetail(page);
  
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