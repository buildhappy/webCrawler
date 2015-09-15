package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Liqucn_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 历趣(谷歌电子市场) http://www.liqucn.com/
 * Liqucn #130
 * @author DMT
 */


public class Liqucn implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Liqucn.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.liqucn\\.com/.*").all() ;
		
		
//		page.addTargetRequest("http://www.liqucn.com/rj/378831.shtml");
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);

			for (String temp : cacheSet) {
				if(PageProUrlFilter.isUrlReasonable(temp) && temp.length() < 200)
						page.addTargetRequest(temp);
			}

		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.liqucn\\.com/.*").match() ){
	
			
			Apk apk = Liqucn_Detail.getApkDetail(page);
			
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
