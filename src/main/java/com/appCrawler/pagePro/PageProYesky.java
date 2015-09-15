package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PageProYesky_Detail;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

/** 
 * 天极网 http://mydown.yesky.com
 * PageProYesky #148
 * @author DMT
 */
public class PageProYesky implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(PageProYesky.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		logger.info("call in PageProYesky.process()" + page.getUrl());
		//index page				http://so.yesky.com/cse/search?s=8655205689349015306&entry=1&q=qq
		if(page.getUrl().regex("http://so\\.yesky\\.com/cse/search\\?.*").match()){
			//app的具体介绍页面		http://so.yesky.com/cse/search?q=qq&p=1&s=8655205689349015306&entry=1		http://mydown.yesky.com/c/113563_18351_5.shtml
			List<String> url1 = page.getHtml().links("//div[@id='results']").regex("http://mydown\\.yesky\\.com/.*").all();

			//添加下一页url(翻页)
//			List<String> url2 = page.getHtml().links("//div[@class='pager clearfix']").regex("http://so\\.yesky\\.com/cse/search\\?.*").all();
			
//			url1.addAll(url2);
			
			//remove the duplicate urls in list
			
			//add the urls to page
			Set<String> sets = Sets.newHashSet(url1);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url)) {
					page.addTargetRequest(url);
				}
			}

		}
		
		//the app detail page
		 if (page.getUrl().regex("http://mydown\\.yesky\\.com/sjsoft/.*").match()) {
			 return PageProYesky_Detail.getApkDetail(page);

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
