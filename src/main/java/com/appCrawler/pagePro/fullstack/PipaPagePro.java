package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

  
import com.appCrawler.pagePro.apkDetails.PipaPagePro_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 *  琵琶网：http://www.pipaw.com/
 * @author DMT
 *
 */


public class PipaPagePro implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(PipaPagePro.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www.pipaw.com/.*").all() ;
		List<String> urls2 =page.getHtml().links().regex("http://wy\\.pipaw\\.com//.*").all() ;
		
//		page.addTargetRequest("http://wy.pipaw.com/game1689/down.html");
//		page.addTargetRequest("http://www.pipaw.com/hcrlm/");
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		cacheSet.addAll(urls2);
		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp) )
						page.addTargetRequest(temp);
						
		}
		
//		page.addTargetRequests(urls);
		
	
//http://wy\\.pipaw\\.com/[a-zA-Z0-9]+
//http://www.pipaw.com/wjzjmy/
//http://wy.pipaw.com/game210/
		if(page.getUrl().regex("http://wy\\.pipaw\\.com/[a-zA-Z0-9]+/").match()
				|| page.getUrl().regex("http://www.pipaw.com/.*").match()){			
			//System.out.println( "in"+" "+page.getUrl().toString());
			Apk apk = PipaPagePro_Detail.getApkDetail(page);
			
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
