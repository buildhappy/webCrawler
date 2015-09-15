package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Anzhi_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 安智网 http://www.anzhi.com/index.html
 * Anzhi #127
 * 下载apk的url需要手动构造
 * @author DMT
 */


public class Anzhi implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Apk3.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.anzhi\\.com/.*").all() ;
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);

				for (String temp : cacheSet) {
					if(!temp.contains("www.anzhi.com/dl_app.php?s=")&&PageProUrlFilter.isUrlReasonable(temp))
								page.addTargetRequest(temp);
				}
//	page.addTargetRequests(urls);
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.anzhi\\.com/soft.*").match() ){
	
			
			Apk apk = Anzhi_Detail.getApkDetail(page);
			
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
