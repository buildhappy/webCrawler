package com.appCrawler.pagePro;

import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 哇麦手机网 app搜索抓取
 * url:http://www.wapmy.cn/
 *
 * @version 1.0.0
 */
public class PageProWabao implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProWabao.class);

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

        // 加入加入页面
        page.addTargetRequest("http://www.tmsjyx.com");
        page.addTargetRequest("http://apk.hiapk.com/appinfo/wabao.ETAppLock/3869269");

        Html html = page.getHtml();
        String appName = null;
        String appVersion = null;
        String appDownloadUrl = null;
        String osPlatform = null;
        String appSize = null;
        String appUpdateDate = null;
        String downloadNum = null;
        String appDesc = null;
        String appType = null;
        
         // 找出对应需要信息
           String appDetailUrl = page.getUrl().toString();
           if (page.getUrl().regex("http://www\\.tmsjyx\\.com").match()) {
             appName = "三国志";
            appDownloadUrl = html.xpath("//div[@class='w1000']/div[@class='pleft']/a[1]/@href").get();
             appDesc = html.xpath("//div[@class='pcontent']/text()").get();

           }
           if (page.getUrl().regex("http://apk\\.hiapk\\.com/appinfo/.*").match()) {
               appName = "ET私密锁";
               appVersion = "3.8.6";
               osPlatform = "Android";
               appSize ="0.72MB";
               appUpdateDate="2.1及以上固件版本";
              appDownloadUrl = "http://apk.r1.market.hiapk.com/data/upload//2013/11_08/17/wabao.ETAppLock_175210.apk";
               appDesc = html.xpath("//div[@class='font12 soft_des_box fixed_height']/pre/text()").get();

             }
            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDesc);

            if (null != appName && null != appDownloadUrl) {
                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
                apk.setAppDescription(appDesc);

                return apk;
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
