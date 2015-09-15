package com.appCrawler.pagePro;

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
 * 卓乐网 app搜索抓取
 * url:http://www.sjapk.com/Search.asp?Key=QQ
 *
 * @version 1.0.0
 */
public class PageProSjapk_1 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProSjapk_1.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setRetryTimes(2).setSleepTime(3);

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://www\\.sjapk\\.com/Search\\.asp\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='main_down']/ul[1]/li").all();
            urlList.addAll(page.getHtml().links("//div[@class='bottom_down']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.sjapk\\.com/.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='main_r_f']/h1/text()").toString();
            String appVersion = html.xpath("//div[@class='main_r_f']/p[4]/font/text()").get();
            String appDownloadUrl = html.xpath("//div[@class='main_r_xiazai5']/a/@href").get();
            String osPlatform = null;
            String appSize = html.xpath("//div[@class='main_r_xiazai4'][1]/p[1]/font[1]/text()").get();
            String appUpdateDate = html.xpath("//div[@class='main_r_xiazai4'][1]/p[1]/font[2]/text()").get();
            String downloadNum = null;
            String appDesc = html.xpath("//div[@class='main_r_jies']/div[@class='jies_f']/text()").get();
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);

            if (null != appName && null != appDownloadUrl) {
                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
                apk.setAppDescription(appDesc);

                return apk;
            }
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
