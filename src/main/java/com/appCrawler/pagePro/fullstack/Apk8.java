package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Apk8_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 安卓网apk8 http://www.apk8.com/
 * Apk8  #71  post提交
 * @author DMT
 */

public class Apk8 implements PageProcessor{
	//Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(0);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Apk8.class);

	public Apk process(Page page) {
	
		
		List<String> urls =page.getHtml().links().regex("http://www\\.apk8\\.com/.*").all() ;
		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for(String url : cacheSet){
			if(PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
		
	
		//提取页面信息
		if(page.getUrl().regex("http://www\\.apk8\\.com/game/.+").match()
			|| page.getUrl().regex("http://www\\.apk8\\.com/soft/.+").match()
			|| page.getUrl().regex("http://www\\.apk8\\.com/hdyx/.+").match()
			|| page.getUrl().regex("http://www\\.apk8\\.com/baobao/.+").match()
			|| page.getUrl().regex("http://www\\.apk8\\.com/paper/.+").match()
			|| page.getUrl().regex("http://www\\.apk8\\.com/theme/.+").match()){
			
			Apk apk = Apk8_Detail.getApkDetail(page);
			
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
