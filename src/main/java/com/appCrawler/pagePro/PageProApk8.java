package com.appCrawler.pagePro;

import com.google.common.collect.Sets;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * APK小游戏 app搜索抓取
 * url:http://www.apk8.com/search.php?key=MT
 *
 * @version 1.0.0
 */
public class PageProApk8 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProApk8.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

    /**
     * url缓存
     */
    private Set<String> cacheSet = Sets.newHashSet();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 配置搜索页
        if (page.getUrl().regex("http://www\\.apk8\\.com/search\\.php\\?").match()) {
            String key = StringUtils.substringAfterLast(page.getUrl().get(), "=");
            System.out.println(key);
            // 加入搜素url
            page.addTargetRequest("http://www.apk8.com/getGame.php?key=" + key);
            page.addTargetRequest("http://www.apk8.com/getSoftWare.php?key=" + key);
            page.addTargetRequest("http://www.apk8.com/getBaobaoyy.php?key=" + key);
            page.addTargetRequest("http://www.apk8.com/getSwf.php?key=" + key);
            page.addTargetRequest("http://www.apk8.com/getTheme.php?key=" + key);
            page.addTargetRequest("http://www.apk8.com/getPaper.php?key=" + key);
        }

        // 获取搜索页面
        String url = page.getUrl().get();
        if (url.contains("getGame.php?key") || url.contains("getSoftWare.php?key")
                        || url.contains("getBaobaoyy.php?key")
                        || url.contains("getSwf.php?key")
                        || url.contains("getTheme.php?key")
                        || url.contains("getPaper.php?key")) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='main_search_pic']/ul/li/div[@class='img_90_90']").all();
            String [] pageList = StringUtils.substringsBetween(page.getHtml().xpath("//div[@class='list_page']/span").get(), "<a href=\"\" ", ");");
            if (null != pageList && !cacheSet.contains(url)) {
                for (String temp : pageList) {
                    String temurl = page.getUrl().get() + "&page=" + StringUtils.substringAfterLast(temp, ",").replace("'", "");
                    urlList.add(temurl);
                    cacheSet.add(temurl);
                }
            }

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.apk8\\.com/.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//div[@class='iconD tableborder']/div[@class='tit_b']/text()").toString();
            String appVersion = html.xpath("//ol[@class='feileis'][1]/li[1]/text()").get();
            String appDownloadUrl = html.xpath("//a[@class='bt_bd']/@href").get();
            String osPlatform = null;
            String appSize = html.xpath("//ol[@class='feileis'][3]/li[1]/text()").get();
            String appUpdateDate = html.xpath("//ol[@class='feileis2'][1]/li/text()").get();
            String downloadNum = null;
            String appDesc = null;
            String appType = null;

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);

            if (null != appName && null != appDownloadUrl && appDownloadUrl.endsWith(".apk")) {
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
