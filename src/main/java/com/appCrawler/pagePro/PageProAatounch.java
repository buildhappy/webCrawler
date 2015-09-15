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
 * 安贝市场[中国] app搜索抓取
 * url:http://app.youxibaba.cn/android.php?ac=search&keyword=MT&page=2
 *
 * @version 1.0.0
 */
public class PageProAatounch implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProAatounch.class);

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
        if (page.getUrl().regex("http://app\\.youxibaba\\.cn/android.php\\?ac=search.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='tabcontentSearch']/ul/li/div[@class='searchtu']").all();
            urlList.addAll(page.getHtml().links("//li[@class='fenye']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://app\\.youxibaba\\.cn/android/.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='app_gcon2']/h1/text()").toString();
            String appVersion = html.xpath("//div[@class='app_gcon2']/dl[@class='clearfix']/dd[1]/text()").toString();
            String appDownloadUrl = html.xpath("//div[@class='app_content_xiazai_b']/a/@href").toString();
            String osPlatform = null;
            String appSize = html.xpath("//div[@class='app_gcon2']/dl[@class='clearfix']/dd[4]/text()").toString();
            String appUpdateDate = html.xpath("//div[@class='app_gcon2']/dl[@class='clearfix']/dd[5]/text()").toString();
            String downloadNum = html.xpath("//div[@class='app_gcon2']/dl[@class='clearfix']/dd[3]/text()").toString();
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, download:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, downloadNum);

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
