package com.appCrawler.pagePro.fullstack;
/**
 * playcn爱游戏 http://www.play.cn/
 * Play #96
 * @author DMT
 */

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import com.appCrawler.pagePro.apkDetails.Play_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;


public class Play implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Play.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.play\\.cn/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(!temp.contains("http://www.play.cn/wap/gameAccess/v02?gameId=") && PageProUrlFilter.isUrlReasonable(temp)
					&& !temp.startsWith("http://www.play.cn/game/download"))				
				page.addTargetRequest(temp);
		}

		
	
		//提取页面信息
		if(page.getUrl().regex("http://www\\.play\\.cn/game/gamedetail.*").match()){
	
			
			Apk apk = Play_Detail.getApkDetail(page);
			
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
