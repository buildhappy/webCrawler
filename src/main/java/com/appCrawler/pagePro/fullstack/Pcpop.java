package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.appCrawler.pagePro.apkDetails.Pcpop_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

/**
 * id = 75
 * 泡泡手机网 http://download.pcpop.com/
 * 与144重复
 */
public class Pcpop implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Pcpop.class);

	public Apk process(Page page) {
	
		
		List<String> urls =page.getHtml().links().regex("http://download\\.pcpop\\.com/wangluogongju/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp))				
				page.addTargetRequest(temp);
		}
		
	
		//提取页面信息
		if(page.getUrl().regex("http://download\\.pcpop\\.com/wangluogongju/.*").match()){
			
			
			Apk apk = Pcpop_Detail.getApkDetail(page);
			
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
