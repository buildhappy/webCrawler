package com.appCrawler.pagePro;

import com.appCrawler.utils.PropertiesUtil;
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
import java.util.Set;

/**
 * 微博应用中心 app搜索抓取
 * url:http://app.sina.com.cn/search.php?q=qq
 * ID：180
 * @version 1.0.0
 */
/*
public class PageProAppSina implements PageProcessor {

	
	 // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAppSina.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());     
        // 获取搜索页面
        if (page.getUrl().regex("http://app\\.sina\\.com\\.cn/search\\.php\\?q=.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());
            
            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='listArea']/ul").regex("http://app\\.sina\\.com\\.cn/appdetail\\.php\\?appID=.*").all();
            List<String> pageList = page.getHtml().links("//div[@class='pages']").regex("http://app\\.sina\\.com\\.cn/search\\.php\\?q=.*").all();
            urlList.addAll(pageList);          
            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);

    		for (String temp : cacheSet) {    			
				if(PageProUrlFilter.isUrlReasonable(temp)
					&& !temp.contains("http://app.sina.com.cn/download.php?appID="))
							page.addTargetRequest(temp);
			}
            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://app\\.sina\\.com\\.cn/appdetail\\.php\\?appID=.*").match()) {
           
            //return PageProAppSina_Detail.getApkDetail(page);
			
        }

		return null;
    }


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
*/
