package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Www77l_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 齐齐乐  http://www.77l.com/
 * Www77l #123
 * (1)该网站的应用和游戏的详细页种类较多，有游戏，应用和网游三种，需要分类写
 * (2)2015年3月3日11:32:57
 * @author DMT
 */


public class Www77l implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Www77l.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.77l\\.com/.*").all() ;
		List<String> url2 =page.getHtml().links().regex("http://wy\\.77l\\.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		cacheSet.addAll(url2);
				for (String temp : cacheSet) {
					if(!temp.contains("http://www.77l.com/e/DownSys/GetDown/?classid=") && PageProUrlFilter.isUrlReasonable(temp))
								page.addTargetRequest(temp);
				}

//		page.addTargetRequests(urls);
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.77l\\.com/game/.*").match() 
				|| page.getUrl().regex("http://www\\.77l\\.com/Game/.*").match()
				|| page.getUrl().regex("http://www\\.77l\\.com/App/.*").match() 
				|| page.getUrl().regex("http://www\\.77l\\.com/app/.*").match()
				|| page.getUrl().regex("http://wy\\.77l\\.com/.*").match()){
	
			
			Apk apk = Www77l_Detail.getApkDetail(page);
			
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
