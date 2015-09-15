package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProBaoRuan_Detail;
import com.appCrawler.utils.PropertiesUtil;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * 宝软[中国] app搜索抓取(只有一个应用)
 * url:http://www.baoruan.com/
 * 64
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
        Apk apk = PageProBaoRuan_Detail.getApkDetail(page);
        page.putField("apk", apk);
        if (page.getResultItems().get("apk") == null) {
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
