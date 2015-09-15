package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProMz6_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 魅族溜[中国] app搜索抓取
 * url:http://m.mz6.net/search/?keyword=MT&dosumbit=1&m=wap&a=search&modelid=14
 *id:13
 * @version 1.0.0
 */
public class PageProMz6 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMz6.class);
    private static boolean flag = true;
    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
            setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());
        //LOGGER.info(page.getRawText());
        // 获取搜索页面
        if (page.getUrl().regex("http://m\\.mz6\\.net/.*").match() || page.getUrl().get().contains("category")) {
            LOGGER.info("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://m\\.mz6\\.net/.*").all();
            List<String> urlList2 = page.getHtml().xpath("//div[@class='m-wrap']/@href").all();
            Set<String> sets = Sets.newHashSet(urlList);
            if(flag){
                for(int i = 0; i < 321; i++){
                	sets.add("http://m.mz6.net/index.php?m=wap&c=index&a=lists&typeid=12&order=new&page=" + i);
                }
                for(int i = 0; i < 135; i++){
                	sets.add("http://m.mz6.net/index.php?m=wap&c=index&a=lists&typeid=1&order=new&page=" + i);
                }
                flag = false;
            }

            sets.addAll(urlList2);
            for (String url : sets) {
                if (PageProUrlFilter.isUrlReasonable(url)) {
                    page.addTargetRequest(url);
                    //LOGGER.info("get url:" + url);
                }
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://m\\.mz6\\.net/detail/.*").match()) {

Apk apk = PageProMz6_Detail.getApkDetail(page);
			
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
