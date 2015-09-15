package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProImobile_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.List;
import java.util.Set;

/**
 * 刷机网 http://app.imobile.com.cn/
 * id:161

 *
 */
public class PageProImobile implements PageProcessor {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PageProImobile.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	@Override
	public Apk process(Page page) {
        // 获取搜索页面
        if (page.getUrl().regex("http://app\\.imobile\\.com\\.cn/(android/game|soft|top)?").match()) {
        	LOGGER.info("match success, url:{}", page.getUrl());
			// 获取详细链接，以及分页链接
			List<String> urlList = page.getHtml().links().regex("http://app\\.imobile\\.com\\.cn/android/.*").all();

			Set<String> sets = Sets.newHashSet(urlList);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url)&& !url.contains("http://app.imobile.com.cn/android/download/")) {
					page.addTargetRequest(url);
				}
			}
            // 打印搜索结果url
            LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }
        
        //the app detail page
		if(page.getUrl().regex("http://app\\.imobile\\.com\\.cn/android/app/.*").match()){
			Apk apk = PageProImobile_Detail.getApkDetail(page);
			page.putField("apk", apk);
			if (page.getResultItems().get("apk") == null) {
				page.setSkip(true);
			}
		}
		else {
			page.setSkip(true);
		}
		return null;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site getSite() {
		return site;
	}


}
