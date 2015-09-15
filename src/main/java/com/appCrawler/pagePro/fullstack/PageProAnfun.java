package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProAnfun_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.List;
import java.util.Set;
/**
* 安粉[中国]
* id = 45
*/
public class PageProAnfun implements PageProcessor{

	// 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro7xz.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());
        if(page.getUrl().toString().contains("79204") || page.getUrl().toString().contains("79206")){
        	return null;
        }
        		
        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.appfun\\.cn/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://www\\.appfun\\.cn/.*").all();

            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);

    				for (String temp : cacheSet) {
    					if(PageProUrlFilter.isUrlReasonable(temp))
    								page.addTargetRequest(temp);
    				}

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.appfun\\.cn/app/info/.*").match()) {
            Apk apk = PageProAnfun_Detail.getApkDetail(page);

            page.putField("apk", apk);
            if (page.getResultItems().get("apk") == null) {
                page.setSkip(true);
            }
        }
        else {
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
    public Site getSite() {
        return site;
    }

	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
}

