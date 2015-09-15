package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.appCrawler.pagePro.apkDetails.PageProNgDCn_Detail;
import com.google.common.collect.Sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

/**
 * 当乐安致 http://www.d.cn/
 * 有三种不同的页面分类，分别是网游、应用和游戏
 * @author DMT
 */
public class PageProNgDCn implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(PageProNgDCn.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in D.process()" + page.getUrl());
		//index page				http://android.d.cn/search/app?keyword=%E5%88%80%E5%A1%94%E4%BC%A0%E5%A5%87
		if(page.getUrl().regex("http://ng\\.d\\.cn/channel/list\\.html\\?keyword=.*").match()){
			logger.debug("match success: {}", page.getUrl().get());
			List<String> urlList = page.getHtml().xpath("//ul[@class='lineList']/li/a[@class='gIcon']/@href").all();
			Set<String> urlSet = Sets.newHashSet(urlList);
			
			//add the urls to page
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
			
			 return PageProNgDCn_Detail.getApkDetail(page);

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
