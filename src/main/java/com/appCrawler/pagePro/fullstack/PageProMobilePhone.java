package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PageProMobilePhone_Detail;
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
 * 手机乐园[中国] app搜索抓取
 * url:http://soft.shouji.com.cn/sort/search.jsp?html=soft&phone=100060&inputname=soft&softname=MT&thsubmit=%E6%90%9C%E7%B4%A2
 *id:8
 * @version 1.0.0
 */
public class PageProMobilePhone implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMobilePhone.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * 分页连接缓存，防止重复抓取
     */
    private Set<String> pageCache = Sets.newHashSet();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://\\w*\\.shouji\\.com\\.cn/.*").match() && !page.getUrl().get().contains("down/")) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取
            List<String> urlList = page.getHtml().links().regex("http://\\w*\\.shouji\\.com\\.cn/.*").all();
    		for (String temp : urlList) {
    			if(PageProUrlFilter.isUrlReasonable(temp) && !temp.startsWith("http://phone.shouji.com.cn")
    					&& !temp.startsWith("http://bbs.shouji.com.cn")
    					&& !temp.startsWith("http://www.shouji.com.cn/s60v5/")
    					&& !temp.startsWith("http://www.shouji.com.cn/java")
    					&& !temp.startsWith("http://game.shouji.com.cn/news")
    					&& !temp.startsWith("http://zhuti.shouji.com.cn")
    					&& !temp.startsWith("http://soft.shouji.com.cn/sort/java.jsp")
    					&& !temp.startsWith("http://game.shouji.com.cn/gamelist/java.jsp"))
    					//&&(temp.startsWith("http://soft.shouji.com.cn/sort/android.jsp")
    					//|| temp.startsWith("http://soft.shouji.com.cn/down/")
    					//|| temp.startsWith("http://game.shouji.com.cn/game/")
    					//|| temp.startsWith("http://game.shouji.com.cn/gamelist")
    				page.addTargetRequest(temp);
    		}

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://soft\\.shouji\\.com\\.cn/down/.*").match()||page.getUrl().regex("http://game\\.shouji\\.com\\.cn/game/.*").match()) {
			Apk apk = PageProMobilePhone_Detail.getApkDetail(page);
			
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
