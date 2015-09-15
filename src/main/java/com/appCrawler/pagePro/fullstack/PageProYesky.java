package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProMyapp_Detail;
import com.appCrawler.pagePro.apkDetails.PageProYesky_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** 
 * 天极网 http://mydown.yesky.com
 * PageProYesky #148

 */
public class PageProYesky implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(PageProYesky.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	@Override
	public Apk process(Page page) {
		logger.info("call in PageProYesky.process()" + page.getUrl());
		if (page.getUrl().regex("http://mydown\\.yesky\\.com/?.*").match()) {

			// 获取详细链接，以及分页链接
			List<String> urlList = page.getHtml().links().regex("http://mydown\\.yesky\\.com/.*").all();

			Set<String> sets = Sets.newHashSet(urlList);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url)) {
					page.addTargetRequest(url);
				}
			}

		}

		//the app detail page
		 if (page.getUrl().regex("http://mydown\\.yesky\\.com/sjsoft/.*").match()) {
			Apk apk = PageProYesky_Detail.getApkDetail(page);
			page.putField("apk", apk);
			if (page.getResultItems().get("apk") == null) {
				page.setSkip(true);
			}
		}
		else {
			page.setSkip(true);
		}
		logger.info("return from PageProYesky.process()");
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

}
