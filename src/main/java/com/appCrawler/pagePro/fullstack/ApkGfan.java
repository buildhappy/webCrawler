package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.ApkGfan_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;


public class ApkGfan implements PageProcessor{
	//Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(0);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(ApkGfan.class);

	public Apk process(Page page) {
		
		
		List<String> urls =page.getHtml().links().regex("http://apk\\.gfan\\.com/.*").all() ;

		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for(String url : cacheSet){
			if(PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
		
		//提取页面信息
		if(page.getUrl().regex("http://apk\\.gfan\\.com/Product/.*").match()){
			
			Apk apk = ApkGfan_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
				}
			}
		else{
			page.setSkip(true);
			}
		return null;
	}
	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Site getSite() {
		return site;
	}
}
