package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PagePro5253_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.assertj.core.util.Maps;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 5253手游网[中国] app搜索抓取
 * url:http://www.5253.com/search.html?searchKey=qq
 * id:57
 * @version 1.0.0
 */
public class PagePro5253 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro5253.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
            setSleepTime(PropertiesUtil.getInterval());

    /**
     * 缓存对象
     */
    private Map<String, Apk> apkMapCache = Maps.newConcurrentHashMap();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.5253\\.com/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://www\\.5253\\.com/game/.*").all();
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
        if (page.getUrl().regex("http://www\\.5253\\.com/game/.*").match() && page.getUrl().get().contains("download.html")) {
			Apk apk = PagePro5253_Detail.getApkDetail(page);
			
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
