package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PagePro7230_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

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
 * 7230手游网[中国] app搜索抓取
 * url:http://www.7230.com/search.asp?keyword=MT
 *id:32
 * @version 1.0.0
 */
public class PagePro7230 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro7230.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * 下载地址集合
     */
    private Set<String> downlaodSet = Sets.newHashSet();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.7230\\.com/.*").match() && !page.getUrl().get().equals(".html")) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://www\\.7230\\.com/.*").all();

            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);
    		for (String temp : cacheSet) {
    			if(!temp.contains("http://www.7230.com/down.asp?id=") && PageProUrlFilter.isUrlReasonable(temp))				
    				page.addTargetRequest(temp);
    		}

            // 加入下载url
            List<String> downloadList = page.getHtml().links().regex("http://www\\.7230\\.com/down\\.asp\\?id=.*").all();
            downlaodSet.addAll(downloadList);

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.7230\\.com/asoft/.*").match()
                        || page.getUrl().regex("http://www\\.7230\\.com/awy/.*").match()
                        || page.getUrl().regex("http://www\\.7230\\.com/ayouxi/.*").match()
                        || page.getUrl().regex("http://www\\.7230\\.com/d.*").match()) {

            Html html = page.getHtml();
			Apk apk = PagePro7230_Detail.getApkDetail(page, downlaodSet);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
			page.setSkip(true);
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
