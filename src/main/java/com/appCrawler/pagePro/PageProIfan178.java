package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProIfan178_Detail;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;

/**
 * 苹果资讯 app搜索抓取
 * url:http://www.baidu.com/s?ie=UTF-8&wd=%E8%B6%85%E8%83%BD%E9%99%86%E6%88%98%20site:shouyou.178.com
 *
 * @version 1.0.0
 */
public class PageProIfan178 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProIfan178.class);

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
        if (page.getUrl().regex("http://www\\.baidu\\.com/s\\?ie=UTF-8.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='content_left']/div/h3[@class='t']").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.baidu\\.com/link\\?url=.*").match()) {
            // 获取dom对象
//            Html html = page.getHtml();
//
//            // 找出对应需要信息
//            String appDetailUrl = page.getUrl().toString();
//            String appName = html.xpath("//div[@class='page-page']/div[@class='t1']/h1/text()").toString();
//            if (StringUtils.isEmpty(appName)) {
//                appName = html.xpath("//div[@class='box-dw-l-t']/h1/strong/text()").get();
//            }
//            String appVersion = null;
//            String appDownloadUrl = html.xpath("//div[@class='clearfix t2']/a/@href").get();
//            if (StringUtils.isEmpty(appDownloadUrl)) {
//                appDownloadUrl = html.xpath("//div[@class='dw-btn']/a[@class='dw-btn2']/@href").get();
//            }
//            String osPlatform = null;
//            String appSize = StringUtils.substringAfterLast(html.xpath("//div[@class='txt']/div[@class='clearfix inf']/p[1]/text()").get(), "：");
//            String appUpdateDate = null;
//            String downloadNum = null;
//            String appDesc = html.xpath("//div[@class='app_detail_infor']/p/text()").get();
//            if (StringUtils.isEmpty(appDesc)) {
//                appDesc = html.xpath("//div[@class='box-dw-l']/div[@class='jianjie']/p/text()").get();
//            }
//            String appType = null;
//
//            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);
//
//            if (null != appName && null != appDownloadUrl) {
//                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
//                apk.setAppDescription(appDesc);

                return PageProIfan178_Detail.getApkDetail(page);
            
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
