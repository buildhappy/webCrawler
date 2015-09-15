package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProWandoujia_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;

/**
 * * 豌豆荚官方[中国] app搜索抓取
 * url:http://www.wandoujia.com/search?key=MT&source=apps
 * 评论网址：
 * http://apps.wandoujia.com/api/v1/comments/primary?packageName=com.sesame.dwgame.xiyou.ky
 * 只需修改后面的包名即可。
 * 40
 * @version 1.0.0
 */
public class PageProWandoujia implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProWandoujia.class);

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
        if (page.getUrl().regex("http://www.wandoujia.com/apps/.*").match() ||
        	page.getUrl().regex("http://www.wandoujia.com/tag/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            
            List<String> urlList = page.getHtml().links().regex("http://www.wandoujia.com/tag/.*").all();
            List<String> urlList1 = page.getHtml().links().regex("http://www.wandoujia.com/apps/.*").all();

    		for (String temp : urlList) {
    			if(PageProUrlFilter.isUrlReasonable(temp))				
    				page.addTargetRequest(temp);
    		}

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.wandoujia\\.com/apps/.*").match()) {
            Apk apk = PageProWandoujia_Detail.getApkDetail(page);

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
