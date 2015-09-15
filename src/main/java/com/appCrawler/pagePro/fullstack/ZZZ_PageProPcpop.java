package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PageProPcpop_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 泡泡手机网 http://download.pcpop.com/
 * Skycn #144
 * 与75重复
 * @author DMT
 */


public class ZZZ_PageProPcpop implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(ZZZ_PageProPcpop.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://download\\.pcpop\\.com/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);

		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp))				
				page.addTargetRequest(temp);
		}
//http://sj.zol.com.cn/down.php?softid=20061&subcateid=106&site=11&server=111&w=0&m=0
//http://sj.zol.com.cn/down.php?softid=23649&subcateid=72&site=11&server=111&w=0&m=0
	//page.addTargetRequests(urls);
	
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://download\\.pcpop\\.com/wangluo.*").match() ){
			Apk apk = PageProPcpop_Detail.getApkDetail(page);
			
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
