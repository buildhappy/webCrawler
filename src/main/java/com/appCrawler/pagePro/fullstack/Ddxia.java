package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Ddxia_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 豆豆软件下载站 http://www.ddxia.com
 * Ddxia #138
 * 无站内搜索接口，搜索跳到百度搜索
 * @author DMT
 */


public class Ddxia implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Ddxia.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.ddxia\\.com/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for(String url : cacheSet){
			if(!url.contains("http://www.ddxia.com/down.php?id=321528&url=") && PageProUrlFilter.isUrlReasonable(url)){
				page.addTargetRequest(url);
			}
		}
//http://www.ddxia.com/down.php?id=321528&url=http%3A%2F%2Fcodown.youdao.com%2Fnote%2Fyoudaonote_android_3.3.1_youdaoweb.apk
//http://www.ddxia.com/down.php?id=280740&url=http://msdx.ddvip.com:81/down/Camera360_4.5_for_Android.apk
//	page.addTargetRequests(urls);
	
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.ddxia\\.com/view/.*").match() ){
	
			
			Apk apk = Ddxia_Detail.getApkDetail(page);
			
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
