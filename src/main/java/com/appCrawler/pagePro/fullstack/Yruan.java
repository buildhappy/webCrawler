package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Yruan_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 亿软网 http://www.yruan.com/
 * Yruan #125	未完成
 * @author DMT
 */


public class Yruan implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Apk3.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.yruan\\.com/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
	
	page.addTargetRequest("http://www.yruan.com/softdown.php?id=4089&phoneid=");
	for (String temp : cacheSet) {
		if(PageProUrlFilter.isUrlReasonable(temp)&&!temp.contains("http://www.yruan.com/down.php"))				
			page.addTargetRequest(temp);
	}
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.yruan\\.com/softdown.*").match() ){
	
			
			Apk apk = Yruan_Detail.getApkDetail(page);
			
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
