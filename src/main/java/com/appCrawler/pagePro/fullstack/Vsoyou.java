package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Vsoyou_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 威搜游 http://vsoyou.com/
 * Vsoyou #111
 * @author DMT
 */


public class Vsoyou implements PageProcessor{
	public static boolean flag = true;
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Apk3.class);

	public Apk process(Page page) {
	
//		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://vsoyou\\.com/game/.*").all() ;
//		
// 		
//		Set<String> cacheSet = Sets.newHashSet();
//		cacheSet.addAll(urls);
//		for (String temp : cacheSet) {
//			if(!temp.startsWith("http://vsoyou.com/download/") && PageProUrlFilter.isUrlReasonable(temp) )
//						page.addTargetRequest(temp);
//		}
		if(flag){
			for(int i=9000;i<15000;i++){
				page.addTargetRequest("http://vsoyou.com/game/"+i+".htm");
			}
			page.addTargetRequest("http://vsoyou.com/game/93.htm");
			flag = false;
		}
	
		//提取页面信息
		if(page.getUrl().regex("http://vsoyou\\.com/game.*").match()){
			Apk apk = Vsoyou_Detail.getApkDetail(page);
			
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
