package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Gezila_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

/**
 * 格子啦 http://www.gezila.com/
 * Gezila #76
 * (1)在搜索结果中有的会有一个专区跳转页面，只有通过这个专区介绍的才能跳转到软件的具体信息页面，因此，这个里面添加了一个
 * 专区介绍页面的分析，并从中获取搜索结果  比如搜索“节奏大师”这个关键字
 * http://www.gezila.com/search?t=android&q=%E8%8A%82%E5%A5%8F%E5%A4%A7%E5%B8%88
 * (2)在app detail page中，还有同一apk的其他版本的下载，这样会在一个页面有同一个apk的多个不同版本的下载，这些多个版本的apk除了版本不同外其他都相同
 * @author DMT
 */

public class Gezila implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Gezila.class);

	public Apk process(Page page) {
	
		
		List<String> urls =page.getHtml().links().regex("http://www\\.gezila\\.com/.*").all() ;
		
		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		page.addTargetRequest("http://www.gezila.com/android/soft/4644.html");
		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp))				
				page.addTargetRequest(temp);
		}
		
	
		//提取页面信息
		if(page.getUrl().regex("http://www\\.gezila\\.com/android/.*").match()){
			
			List<Apk> apk = Gezila_Detail.getApkDetail(page);
			
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
