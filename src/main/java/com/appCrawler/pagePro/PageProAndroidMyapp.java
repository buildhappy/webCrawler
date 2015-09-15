package com.appCrawler.pagePro;


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
 *应用宝[中国] app搜索抓取
 * url:http://android.myapp.com/myapp/search.htm?kw=qq
 *
 * @version 1.0.0
 */
public class PageProAndroidMyapp implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAndroidMyapp.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        List<String> url1 = page.getHtml().links().regex("http://android\\.myapp\\.com/myapp/search\\.htm\\?kw=.*").all();
      //匹配下一页
       // List<String> url2 = page.getHtml().links("//div[@id='pagenavi']").all();
     //   url1.addAll(url2);

        //remove the duplicate urls in list
        HashSet<String> urlSet = new HashSet<String>(url1);

        //add the urls to page
        Iterator<String> it = urlSet.iterator();
        while(it.hasNext()){
            page.addTargetRequest(it.next());//添加app的具体介绍页面
        }

        // 打印搜索结果url
        LOGGER.debug("app info results urls: {}", page.getTargetRequests());
    

    // 获取信息
    if (page.getUrl().regex("http://android\\.myapp\\.com/myapp/detail\\.htm?apkName=.*").match()) {
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//h1[@class='det-title']/text()").toString();
        String appVersion = html.xpath("//ul[@class='det-info-list']/li[1]/text()").toString();
        String appDownloadUrl = html.xpath("//li[@class='det-butn']/a[1]/@href").toString();
        String osPlatform = null;
        String appSize = html.xpath("//div[@class='det-size']/text()").toString();
        String appUpdateDate = null;
        String appType = null;
        String appDownTime = html.xpath("//div[@class='det-ins-num']/text()").toString();

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate);

        if (null != appName && null != appDownloadUrl) {
            return new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
        }

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