package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Bkill_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/** 
 * 必杀客 http://www.bkill.com
 * Bkill #136
 * @author DMT
 */


public class Bkill implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Bkill.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.bkill\\.com/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);

		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp)
				&& !temp.contains("http://www.bkill.com/d/dl.php?n=1&server="))
						page.addTargetRequest(temp);
		}
//http://www.bkill.com/d/dl.php?n=1&server=5&id=35471::1414035474
//http://www.bkill.com/d/dl.php?n=1&server=5&id=7226::1295936999
//http://www.bkill.com/d/dl.php?n=1&server=5&id=24452::1369882224
//	page.addTargetRequests(urls);
	
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.bkill\\.com/download.*").match() ){
	
			
			Apk apk = Bkill_Detail.getApkDetail(page);
			
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
