package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PageProOnlineDown_Detail;
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
 * 华军软件园[中国] app搜索抓取
 * url:http://search.newhua.com/search_list.php?searchname=MT&searchsid=6&app=search&controller=index&action=search&type=news
 * ID：23
 * 需要两次请求
 * @version 1.0.0
 */
public class PageProOnlineDown implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProOnlineDown.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * 返回结果结果集
     */
    private Set<Apk> resSet = Sets.newHashSet();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.onlinedown\\.net/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            List<String> urlList = page.getHtml().links().regex("http://www\\.onlinedown\\.net/.*").all();

            Set<String> sets = Sets.newHashSet(urlList);
            for (String url : sets) {
                if (PageProUrlFilter.isUrlReasonable(url)) {
                if(url.contains("http://www.onlinedown.net/android/soft"))
                		{
                		//url="http://www.onlinedown.net/"+url;
                		url=url.replaceAll("//android", "");
                		System.out.println("aaa");
                		}
                	
                    page.addTargetRequest(url);
                }
            }
            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.onlinedown\\.net/soft/.*").match()) {
            
		   PageProOnlineDown_Detail.getApkDetail(page, resSet);
			
        }

        // 更新下载地址
        if (page.getUrl().regex("http://www\\.onlinedown\\.net/softdown/.*").match()) {
        	Html html = page.getHtml();
			Apk apk = PageProOnlineDown_Detail.getApkDetail(page, resSet);
			
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