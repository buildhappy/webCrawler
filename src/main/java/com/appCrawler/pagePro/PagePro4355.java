package com.appCrawler.pagePro;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PagePro4355_Detail;
import com.appCrawler.utils.PropertiesUtil;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.4355\\.com/e/search/.*").match()) {
        	//if (page.getUrl().regex("http://www\\.4355\\.com/e/search/result/.*").match()) {
            LOGGER.info("match success, url:{}", page.getUrl());
            //匹配具体app页面
            List<String> url1 = page.getHtml().links("//div[@class='content']/dl/dt").all();
            //url1 = page.getHtml().links().all();
            //匹配下一页
            List<String> url2 = page.getHtml().links("//div[@id='pages']").all();
            url1.addAll(url2);

            //remove the duplicate urls in list
            HashSet<String> urlSet = new HashSet<String>(url1);

            //add the urls to page
            Iterator<String> it = urlSet.iterator();
            while(it.hasNext()){
            	String s = it.next();
                page.addTargetRequest(s);//添加app的具体介绍页面
            //    System.out.println(s);
            }

            // 打印搜索结果url
            LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://d.4355.com/*").match() || page.getUrl().regex("http://soft.4355.com/*").match()) {
        	return PagePro4355_Detail.getApkDetail(page);
   
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