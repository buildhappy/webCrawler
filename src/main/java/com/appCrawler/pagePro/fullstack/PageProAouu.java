package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProAouu_Detail;
import com.appCrawler.utils.PropertiesUtil;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 邀友网[中国] app搜索抓取
 * url:http://www.aouu.com/
 *id:63
 * @version 1.0.0
 */
public class PageProAouu implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAouu.class);

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
        if (page.getUrl().regex("http://www\\.aouu\\.com/").match()) {
			Apk apk = PageProAouu_Detail.getApkDetail(page);
			
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
