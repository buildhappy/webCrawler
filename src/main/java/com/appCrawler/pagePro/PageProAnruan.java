package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProAnruan_Detial;
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
 * 安软[中国] app搜索抓取
 * url:http://www.anruan.com/search.php?t=soft&keyword=MT
 *
 * @version 1.0.0
 */
public class PageProAnruan implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAnruan.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.anruan\\.com/search.php\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='divlist']/div[@class='pa']/div[@class='pc']/ul/li").all();
            urlList.addAll(page.getHtml().links("//div[@id='divlist']/div[@class='li']").all());
            urlList.addAll(page.getHtml().links("//div[@class='pager']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String url = iter.next();
            	if(PageProUrlFilter.isUrlReasonable(url)
						&&!url.contains("http://soft.anruan.com/down.php")&&!url.contains("http://game.anruan.com/gdown.php")){
            		page.addTargetRequest(url);
            	}
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://soft\\.anruan\\.com/.*").match() || page.getUrl().regex("http://game\\.anruan\\.com/.*").match()|| page.getUrl().regex("http://www\\.anruan\\.com/.*").match()) {
        	return PageProAnruan_Detial.getApkDetail(page);
			

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
