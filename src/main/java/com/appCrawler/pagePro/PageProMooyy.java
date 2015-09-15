package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProMooyy_Detail;
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
 * 摸鱼网[中国] app搜索抓取
 * url:http://www.mooyy.com/plus/search.php?kwtype=0&searchtype=titlekeyword&typeid=2&keyword=qq
 *
 * @version 1.0.0
 */
public class PageProMooyy implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMooyy.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(2).setSleepTime(300);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www.mooyy.com/plus/search.php\\?*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//ul[@class='download_list']/li/div[1]").all();
            urlList.addAll(page.getHtml().links("//div[@class='pagination']/li").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

     // 获取信息
        if (page.getUrl().regex("http://www.mooyy.com/android/.*").match()) {
        	return PageProMooyy_Detail.getApkDetail(page);

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
