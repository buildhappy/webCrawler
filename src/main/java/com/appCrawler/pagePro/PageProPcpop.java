package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProPcpop_Detail;
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
 * 泡泡手机网 app搜索抓取
 * url:http://zhannei.baidu.com/cse/search?s=5667130173287269104&q=mt&kwtype=0
 *
 * @version 1.0.0
 */
public class PageProPcpop implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProPcpop.class);

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
        if (page.getUrl().regex("http://zhannei\\.baidu\\.com/cse/search\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@id='results']/div[@class='result f s0']/h3[@class='c-title']").all();
            urlList.addAll(page.getHtml().links("//div[@id='pageFooter']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://download\\.pcpop\\.com/wangluo.*").match()) {
            // 获取dom对象
//            Html html = page.getHtml();
//
//            // 找出对应需要信息
//            String appDetailUrl = page.getUrl().toString();
//            String appName = html.xpath("//div[@class='titleHead']/h1[@class='title']/text()").toString();
//            String appVersion = null;
//            String appDownloadUrl = StringUtils.substringBetween(html.get(), "var linkArr=new Array('", "')");
//            String osPlatform = html.xpath("//div[@class='infolist']/ul/li[6]/span/text()").get();
//            String appSize = html.xpath("//div[@class='infolist']/ul/li[1]/span/text()").get();
//            String appUpdateDate = html.xpath("//div[@class='infolist']/ul/li[5]/span/text()").get();
//            String downloadNum = null;
//            String appDesc = html.xpath("//div[@id='description']/div/text()").get();
//            String appType = null;
//
//            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);
//
//            if (null != appName && null != appDownloadUrl && appDownloadUrl.endsWith(".apk")) {
//                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
//                apk.setAppDescription(appDesc);

                return PageProPcpop_Detail.getApkDetail(page);
            
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
