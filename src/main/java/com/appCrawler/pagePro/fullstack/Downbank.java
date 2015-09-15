package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Downbank_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 下载银行 http://www.downbank.cn
 * Downbank #132
 * (1)下载链接有防盗链设置
 * @author DMT
 */


public class Downbank implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Downbank.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.downbank\\.cn/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for(String url : cacheSet){
			if(PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.downbank\\.cn/.*").match() ){
	
			
			Apk apk = Downbank_Detail.getApkDetail(page);
			
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
