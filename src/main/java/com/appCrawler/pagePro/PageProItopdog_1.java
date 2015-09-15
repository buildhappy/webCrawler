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
import java.util.Set;

/**
 * 软件合子app搜索抓取
 * url:http://www.itopdog.cn/home.php?type=az&ct=home&ac=search&q=qq
 *
 * @version 1.0.0
 */
public class PageProItopdog_1 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProItopdog_1.class);

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
        if (page.getUrl().regex("http://www\\.itopdog\\.cn/home\\.php\\?type=az&ct=home&ac=search&q=.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='panel-list-imgbrief']/dl[@class='clearfix']/dd").all();
            urlList.addAll(page.getHtml().links("//div[@id='pages2']").all());

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://www\\.itopdog\\.cn/az/.*").match()) {
            // 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();          
            String title = StringUtils.trim(html.xpath("//div[@class='box-bluetopline android-detail-main-con']/h2/font/text()").toString());
            String appName=StringUtils.substringBefore(title, " ");
            String appVersion = StringUtils.substringAfter(title, " ");
            String appDownloadUrl = html.xpath("//div[@class='down-btn']/a/@href").toString();
            String osPlatform = html.xpath("//dl[@class='clearfix appinfo']/dd[4]/text()").toString();
            String appSize = html.xpath("//dl[@class='clearfix appinfo']/dd[1]/text()").toString();
            String appUpdateDate = html.xpath("//div[@class='six code2d']/strong/text()").toString();
            String appType = null;
            String appDesc=html.xpath("//div[@class='soft-detail-content six']/text()").toString();
            String appComment=null;
            String appCommentUrl=null;
            
            
            
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
