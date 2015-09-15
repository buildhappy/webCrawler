package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PagePro520apk_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

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
 * 安卓乐园[中国] app搜索抓取
 * url:http://search.520apk.com/cse/search?q=QQ&s=17910776473296434043&nsid=1
 *id:
 * @version 1.0.0
 */
public class PagePro520apk implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro520apk.class);

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
        if (page.getUrl().regex("http://www\\.520apk\\.com/?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://www\\.520apk\\.com/.*").all();
           // urlList.addAll(page.getHtml().links().regex("http://www\\.520apk\\.com/android/.*").all());

            Set<String> cacheSet = Sets.newHashSet();
    		cacheSet.addAll(urlList);

    				for (String temp : cacheSet) {
    					temp = temp.toLowerCase();
    					if(PageProUrlFilter.isUrlReasonable(temp)&&!temp.startsWith("http://www.520apk.com/searchNews/")
    					   &&!temp.startsWith("http://www.520apk.com/youxigonglue/")
    					   &&!temp.startsWith("http://www.520apk.com/bizhi/")
    					   &&!temp.startsWith("http://www.520apk.com/android/fengjing/")
    					   &&!temp.startsWith("http://www.520apk.com/zhuti/")
    					   &&!temp.startsWith("http://www.520apk.com/tags/")
    					   &&!temp.startsWith("http://www.520apk.com/anzhuoshouji/")
    					   &&!temp.startsWith("http://www.520apk.com/walkthrough/")
    					   &&!temp.startsWith("http://www.520apk.com/yeneixinwen/")
    					   &&!temp.startsWith("http://www.520apk.com/mengxianghaizeiwang"))
    								page.addTargetRequest(temp);
    				}


            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.520apk\\.com/android/.*").match()) {
			Apk apk = PagePro520apk_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
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
