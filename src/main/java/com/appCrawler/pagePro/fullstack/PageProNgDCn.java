package com.appCrawler.pagePro.fullstack;



import com.appCrawler.pagePro.apkDetails.PagePro7xz_Detail;
import com.appCrawler.pagePro.apkDetails.PageProNgDCn_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 当乐安致 http://www.d.cn/
 * 有三种不同的页面分类，分别是网游、应用和游戏
 *id:162
 */
public class PageProNgDCn implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(PageProNgDCn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());
	@Override
	public Apk process(Page page) {
		logger.info("call in D.process()" + page.getUrl());
		if (page.getUrl().regex("http://android\\.d\\.cn/.*").match()||page.getUrl().regex("http://ng\\.d\\.cn/.*").match()
				&& !page.getUrl().get().endsWith(".html")) {
			logger.debug("match success: {}", page.getUrl().get());

			// 获取详细链接，以及分页链接
			List<String> urlList = page.getHtml().links().regex("http://android\\.d\\.cn/.*").all();
			urlList.addAll(page.getHtml().links().regex("http://ng\\.d\\.cn/.*").all());
			Set<String> sets = Sets.newHashSet(urlList);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url)&& !url.contains("http://ng.d.cn/game/downs")) {
					page.addTargetRequest(url);
				}
			}

			logger.debug("results: ", page.getTargetRequests());
		}

		//the app detail page
		 if (page.getUrl().regex("http://android\\.d\\.cn/game.*html").match()
				|| page.getUrl().regex("http://android\\.d\\.cn/software.*").match()||page.getUrl().regex("http://ng\\.d\\.cn/.*").match()) {
		
		 Apk apk = PageProNgDCn_Detail.getApkDetail(page);

         page.putField("apk", apk);
         if (page.getResultItems().get("apk") == null) {
             page.setSkip(true);
         }
     }
     else {
         page.setSkip(true);
     }
		logger.info("return from D.process()");
		return null;

	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
	}
}
