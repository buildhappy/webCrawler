package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProCr173_Detail;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 西西软件园[中国] app搜索抓取
 * url:http://so.cr173.com/?keyword=mt&searchType=youxi
 *
 * @version 1.0.0
 */
public class PageProCr173 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProCr173.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(2).setSleepTime(3);

    /**
     * 下载前缀地址
     */
    private Map<String, String> urlMap = null;

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());
        // 加入下载地址
        page.addTargetRequest("http://www.cr173.com/inc/SoftLinkType.js");

        // 获取搜索页面
        if (page.getUrl().regex("http://so\\.cr173\\.com/\\?").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());
            System.out.println("page：" + page.getRawText());
            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//ul[@id='r_lst']/li/h3").all();
            urlList.addAll(page.getHtml().links("//div[@class='tsp_nav']").all());

            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);

    				for (String temp : cacheSet) {
    					if(PageProUrlFilter.isUrlReasonable(temp)
    						)
    								page.addTargetRequest(temp);
    				}
            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取下载地址信息
        if (page.getUrl().regex("http://www\\.cr173\\.com/inc/SoftLinkType\\.js").match()) {
            String html = StringUtils.substringAfter(page.getHtml().xpath("//body/text()").get(), "=");

            try {
                urlMap = new ObjectMapper().readValue(html, Map.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.cr173\\.com/soft/.*").match() || page.getUrl().regex("http://www\\.cr173\\.com/azyx/.*").match()) {
        	return PageProCr173_Detail.getApkDetail(page, urlMap);
			

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