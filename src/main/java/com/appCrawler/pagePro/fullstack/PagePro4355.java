package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PagePro4355_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.List;
import java.util.Set;

/**
 * 搜狐应用中心[中国] app搜索抓取
 * url:http://www.4355.com/e/search/result/?searchid=6665
 *
 * @version 1.0.0
 */
public class PagePro4355 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro4355.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gbk").setRetryTimes(PropertiesUtil.getRetryTimes()).
            setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
    	//System.out.println(page.getHtml());
        LOGGER.info("crawler url: {}", page.getUrl());
       // System.out.println(page.getRawText());
        // 获取搜索页面
        if (page.getUrl().regex("http://d|www|soft\\.4355\\.com/.*").match()) {
        	//if (page.getUrl().regex("http://www\\.4355\\.com/e/search/result/.*").match()) {
            LOGGER.info("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://d|soft\\.4355\\.com/.*").all();

            Set<String> sets = Sets.newHashSet(urlList);
            for (String url : sets) {
                if (PageProUrlFilter.isUrlReasonable(url)) {
                	url = "http://" + url;
                	//System.out.println(url);
                    page.addTargetRequest(url);
                }
            }

            // 打印搜索结果url
            //LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://d.4355.com/.*").match() || page.getUrl().regex("http://soft.4355.com/.*").match()) {
            Apk apk = PagePro4355_Detail.getApkDetail(page);
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
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * 	<site id="00"><!-- get提交 -->
		<name>4355</name>
		<homePage>http://www.4355.com/</homePage>
		<pageProcessor>com.appCrawler.pagePro.PagePro4355</pageProcessor>
		<searchUrl>http://www.4355.com/e/search/?searchget=1&tbname=download&tempid=1&show=title,smalltext&keyboard=MT</searchUrl><!-- 其中的变量用*#*#*#代替 --><!-- !%!%!%代替& -->
		<urlEncoding>gbk</urlEncoding>
		<pageEncoding>gbk</pageEncoding>
	</site>
*/