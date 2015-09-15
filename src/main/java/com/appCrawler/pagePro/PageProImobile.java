package com.appCrawler.pagePro;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PageProImobile_Detail;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

/**
 * 刷机网 http://app.imobile.com.cn/
 * 
 * @author buildhappy
 *
 */
public class PageProImobile implements PageProcessor {
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PageProImobile.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

	@Override
	public Apk process(Page page) {
        // 获取搜索页面
        if (page.getUrl().regex("http://app\\.imobile\\.com\\.cn/android/search/*").match()) {
        	LOGGER.info("match success, url:{}", page.getUrl());
            // 获取详细链接
            List<String> urlList = page.getHtml().links("//ul[@class='ranking_list']").all();
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
			return PageProImobile_Detail.getApkDetail(page);
	
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
	
	/**
	 * split a string 
	 * eg."下载次数：19万" retrun 19万
	 * @param s
	 * @return
	 */
	public String splitString(String s){
		return s.split("：").length > 1 ? s.split("：")[1]:null;
	}
	
	public static void main(String[] args){
		PageProImobile pagePro = new PageProImobile();
		String s = "支持固件：Android 2.1以上";
		//"下载次数：19万"
		s = "http://app.imobile.com.cn/android/app/2189.html";
		System.out.println(s.startsWith("http://app.imobile.com.cn/android/app"));
	}

}
