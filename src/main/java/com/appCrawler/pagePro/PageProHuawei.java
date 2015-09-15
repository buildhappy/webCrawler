package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProHuawei_Detail;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;

/**
 * 华为智汇云[中国] app搜索抓取
 * url:http://appstore.huawei.com/search/MT
 *
 * @version 1.0.0
 */
public class PageProHuawei implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProHuawei.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://appstore\\.huawei\\.com(:80)?/search/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='unit-main']/div[@class='list-game-app dotline-btn nofloat']/div[@class='game-info-ico']").all();

            String pages = StringUtils.substringBetween(page.getHtml().xpath("//div[@id='searchListPage']").get(), "(", ")");
            if (StringUtils.isNotEmpty(pages)) {
                int total = Integer.valueOf(pages.split(",")[2].replaceAll("'", ""));
                for (int i = 1; i <= total; i++) {
                   urlList.add(pages.split(",")[4].replaceAll("'", "") + i);
                }
            }

            urlList.addAll(page.getHtml().links("//div[@id='searchListPage']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

     // 获取信息
        if (page.getUrl().regex("http://appstore\\.huawei\\.com(:80)?/app/.*").match()) {
        	return PageProHuawei_Detail.getApkDetail(page);

  
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
