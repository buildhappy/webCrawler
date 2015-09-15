package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.D_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 当乐安致 http://www.d.cn/
 * 类名 #134
 * 有三种不同的页面分类，分别是网游、应用和游戏
 * @author DMT
 */


public class D implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(D.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://android\\.d\\.cn/.*").all() ;
		List<String> urls2 =page.getHtml().links().regex("http://ng\\.d\\.cn/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		cacheSet.addAll(urls2);
		for(String url : cacheSet){
			if(PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://android\\.d\\.cn/game.*html").match()
				|| page.getUrl().regex("http://android\\.d\\.cn/software.*").match()
	            || page.getUrl().regex("http://ng\\.d\\.cn/.*").match() ){
	
			
			Apk apk = D_Detail.getApkDetail(page);
			
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
