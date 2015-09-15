package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProgfanstore_Detail;
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
import java.util.Set;

/**
 * 机锋app搜索抓取
 * url:http://apk.gfan.com/search?q=keyword
 *
 * @version 1.0.0
 */
public class PageProgfanstore implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProgfanstore.class);

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

        // 获取搜索页面
        if (page.getUrl().regex("http://apk.gfan.com/search/*").match()) {
            LOGGER.debug("seach url: {}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='lp-right div-border fr']").all();
            urlList.addAll(page.getHtml().links("//div[@class='page_bar']/ul/li").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String url = iter.next();
            	if(url.contains("/api/apk?type=")){
            		System.out.println("contains");
            		continue;
            	}
            	System.out.println(url);
                page.addTargetRequest(url);
            }

            LOGGER.debug("results urls: {}", page.getTargetRequests());
        }


        // 获取信息
        if (page.getUrl().regex("http://apk\\.gfan\\.com/Product/.*").match()) {
        	return PageProgfanstore_Detail.getApkDetail(page);

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
