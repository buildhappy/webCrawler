package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PageProMM10086_Detail;
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

/**
 * 移动MM[中国] app搜索抓取
 * url:http://mm.10086.cn/searchapp?st=0&q=MT&dt=android
 * ID：6
 * @version 1.0.0
 */
public class PageProMM10086 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMM10086.class);

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
        if (page.getUrl().regex("http://mm\\.10086\\.cn/.*").match() && !page.getUrl().get().startsWith("http://mm.10086.cn/android/info")) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://mm\\.10086\\.cn/android/.*").all();

    		for (String temp : urlList) {
    			if(!temp.startsWith("http://mm.10086.cn/download/android/") && PageProUrlFilter.isUrlReasonable(temp))				
    				page.addTargetRequest(temp);
    		}

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://mm.10086.cn/android/info\\/*").match()) {
          Html html = page.getHtml();
			Apk apk = PageProMM10086_Detail.getApkDetail(page);
			
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
