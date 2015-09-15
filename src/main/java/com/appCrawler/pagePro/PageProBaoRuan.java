package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProBaoRuan_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * 宝软[中国] app搜索抓取
 * url:http://www.baoruan.com/
 *
 * @version 1.0.0
 */
public class PageProBaoRuan implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProBaoRuan.class);

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
        //只有一个3D宝软桌面的app


        if (page.getUrl().regex("http://www\\.baoruan\\.com/").match()) {
        	return PageProBaoRuan_Detail.getApkDetail(page);


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
