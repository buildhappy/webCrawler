package com.appCrawler.pagePro;


import com.appCrawler.pagePro.apkDetails.PageProPconline_Detail;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 *pchome[中国] app搜索抓取
 * url:http://download.pchome.net/search-qq---0-1.html
 *
 * @version 1.0.0
 */
public class PagePropchome implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePropchome.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gbk").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
      //  LOGGER.debug("crawler url: {}", page.getUrl());
        if (page.getUrl().regex("http://download\\.pchome\\.net/search.*").match()) {
        // 获取搜索页面
        List<String> url1 = page.getHtml().links("//div[@class='dl-softlist-computer clearfix overflow']").regex("http://download.pchome.net/mobile/.*").all();
      //匹配下一页
        List<String> url2 = page.getHtml().links("//div[@class='dl-pages fl']").all();
        url1.addAll(url2);

        //remove the duplicate urls in list
        HashSet<String> urlSet = new HashSet<String>(url1);

        //add the urls to page
        Iterator<String> it = urlSet.iterator();
        while(it.hasNext()){
            String url = it.next();
            page.addTargetRequest(StringUtils.replace(url, "detail-", "download-"));//添加app的具体介绍页面
        }

        // 打印搜索结果url
   //     LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://dl.pconline.com.cn/download.*").match()) {
        	return PageProPconline_Detail.getApkDetail(page);

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

//@Override
public List<Apk> processMulti(Page page) {
	// TODO Auto-generated method stub
	return null;
}
}