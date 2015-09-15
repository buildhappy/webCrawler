package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

 



 
import com.appCrawler.pagePro.apkDetails.OppoApkPagePro_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * #171 oppo手机软件：http://store.nearme.com.cn/
 * 伪造下载链接:
 * http://store.nearme.com.cn/product/download.html?id=588252&from=1135_-1
 * 将id和from后的参数进行处理
 * @author DMT
 *
 */


public class OppoApkPagePro implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(1000);

	private Logger LOGGER = LoggerFactory.getLogger(Apk3.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://store\\.oppomobile\\.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp))
						page.addTargetRequest(temp);
		}
		
//		page.addTargetRequests(urls);
		
		if(page.getUrl().regex("http://store\\.oppomobile\\.com/.*").match()){			
		
			Apk apk = OppoApkPagePro_Detail.getApkDetail(page);
			
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
