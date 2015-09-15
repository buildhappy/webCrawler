package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProMumayi_Detail;
import com.appCrawler.utils.PropertiesUtil;
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
 * 木蚂蚁[中国] app搜索抓取
 * url:http://s.mumayi.com/index.php?q=MT&typeid=0
 *
 * @version 1.0.0
 */
public class PageProMumayi implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMumayi.class);

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
        if (page.getUrl().regex("http://s\\.mumayi\\.com/index.php\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//ul[@class='applist']/li").all();

            // 处理分页
            int pages = Integer.valueOf(page.getHtml().xpath("//input[@id='totalval']/@value").get());
            String keyword = page.getHtml().xpath("//input[@name='q']/@value").get();
            for (int i = 1; i <= pages; i++) {
                urlList.add(String.format("http://s.mumayi.com/newsearch.php?p=%s&q=%s", i, keyword));
            }

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

     // 获取信息
        if (page.getUrl().regex("http://www\\.mumayi\\.com/android.*").match()) {
        	return PageProMumayi_Detail.getApkDetail(page);
			

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
