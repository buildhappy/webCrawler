package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PagePro3533_Detail;
import com.appCrawler.pagePro.apkDetails.PagePro360_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;

/**
 * 手机世界[中国] app搜索抓取
 * url:http://search.3533.com/game?keyword=DOTA
 *id:
 * @version 1.0.0
 */
public class PagePro3533 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro3533.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://\\w*\\.3533\\.com/.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            List<String> urlList = page.getHtml().links().regex("http://game\\.3533\\.com/.*").all();
           // http://game.3533.com/ruanjian/1071.htm
            for (String url : urlList) {
                if (PageProUrlFilter.isUrlReasonable(url) && !url.startsWith("http://game.3533.com/bizhi")
                		&& !url.startsWith("http://game.3533.com/lingsheng")
                		&& !url.startsWith("http://game.3533.com/apple")
                		&& !url.startsWith("http://game.3533.com/ting")
                		&& !url.startsWith("http://game.3533.com/zhuti")){
                    page.addTargetRequest(url);
                }
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://game\\.3533\\.com/game|ruanjian/.*").match()) {
            Html html = page.getHtml();
			Apk apk = PagePro3533_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
			page.setSkip(true);
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
