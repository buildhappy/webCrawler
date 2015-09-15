package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PagePro159_Detail;
import com.appCrawler.pagePro.apkDetails.PagePro7xz_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 机客应用[中国] app搜索抓取
 * url:http://games.159.com/ashx/Common.ashx?acttion=getgames_pager&pageindex=0&pagesize=200&type=3001&querystr=%E6%95%A2%E6%AD%BB%E9%98%9F
 *
 * @version 1.0.0
 */
public class PagePro159 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro159.class);

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
        if (page.getUrl().regex("http://games\\.159\\.com/").match() && !page.getUrl().get().contains("acttion=")) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            page.addTargetRequest("http://games.159.com/ashx/Common.ashx?acttion=games_hots_all&_=" + System.currentTimeMillis());

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        if (page.getUrl().regex("http://games\\.159\\.com/ashx/Common.ashx\\?acttion=games_hots_all.*").match()) {

            Map<String, Object> urlMap = null;
            try {
                urlMap = new ObjectMapper().readValue(page.getRawText(), Map.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if (null != urlMap) {
                Set<String> set = Sets.newHashSet();

                List<Map<String, Object>> infos = (List<Map<String, Object>>) urlMap.get("rows");
                for (Map<String, Object> map : infos) {
                    // 请求地址
                    String url = "http://games.159.com/ashx/Common.ashx?acttion=games_single&keyid=" + map.get("KeyId") + "&_=" + System.currentTimeMillis();
                    set.add(url);
                }

        		for (String temp : set) {
        			if(PageProUrlFilter.isUrlReasonable(temp))				
        				page.addTargetRequest(temp);
        		}
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://games\\.159\\.com/ashx/Common\\.ashx\\?acttion=games_single.*").match()) {
            Apk apk = PagePro159_Detail.getApkDetail(page);

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

		return null;
	}
}
