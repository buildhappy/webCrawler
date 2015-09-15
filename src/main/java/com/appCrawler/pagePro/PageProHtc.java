package com.appCrawler.pagePro;

import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;

/**
 * htc[中国] app搜索抓取
 * url:http://www.htc.com/cn/support/bulletin.aspx?year=2011
 *
 * @version 1.0.0
 */
public class PageProHtc implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProHtc.class);

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

        // 获取页面
        List<String> url1 = page.getHtml().links().regex("(http://www\\.htc\\.com/cn/support/.*)").all();
		page.addTargetRequests(url1);
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        

            if(page.getRequest().getUrl().equals("http://www.htc.com/cn/support/bulletin.aspx?year=2011")){
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//span[@class='bulletin-news-title']/text()").toString();
            String appVersion = null;
            String appDownloadUrl = html.xpath("//div[@class='block-content']/p/a/@href").toString();
            String osPlatform = null;
            String appSize = null;
            String appUpdateDate = null;
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate);

            if (null != appName && null != appDownloadUrl) {
                return new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            }
                                          
            }
        
            if(page.getRequest().getUrl().equals("http://www.htc.com/cn/support/bulletin.aspx?year=2014")){
                // 获取dom对象
                Html html = page.getHtml();

                // 找出对应需要信息
                String appDetailUrl = page.getUrl().toString();
                String appName = html.xpath("//div[@class='block-content']/text()").toString();
                String appVersion = null;
                String appDownloadUrl = html.xpath("//div[@class='block-content']/div/a/@href").toString();
                String osPlatform = null;
                String appSize = null;
                String appUpdateDate = null;
                String appType = null;

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