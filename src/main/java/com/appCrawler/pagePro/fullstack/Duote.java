package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.appCrawler.pagePro.apkDetails.Duote_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;


public class Duote implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Duote.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://zhushou\\.2345\\.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for(String url : cacheSet){
			if(!url.contains("http://zhushou.2345.com/index.php?c=web&d=qrDown&softId=") && PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
		
//		page.addTargetRequests(urls);
		
	
		//提取页面信息
		if(page.getUrl().regex("http://zhushou\\.2345\\.com/soft/.*").match() 
				|| page.getUrl().regex("http://zhushou\\.2345\\.com/game/.*").match()){
			
			Apk apk = Duote_Detail.getApkDetail(page);
			
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
