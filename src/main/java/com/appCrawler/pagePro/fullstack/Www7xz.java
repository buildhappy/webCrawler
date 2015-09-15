package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Www7xz_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 7匣子 http://www.7xz.com
 * Www7xz #135
 * @author DMT
 */


public class Www7xz implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Www7xz.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.7xz\\.com/.*").all() ;
		
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);

				for (String temp : cacheSet) {
					if(!temp.contains("http://www.7xz.com/apk?pkg=") &&!temp.endsWith(".html&frm=tuijian")
							 && PageProUrlFilter.isUrlReasonable(temp))
								page.addTargetRequest(temp);
				}
//http://www.7xz.com/apk?pkg=kr.co.dalcomsoft.superstar.a&add=/qmttuan//&frm=xzyxz	
//http://www.7xz.com/apk?pkg=com.ztgame.ztmobiletest.qh360&add=/xztsy//&frm=xzyxz
//			page.addTargetRequest("http://www.7xz.com/xztsy/");
//	page.addTargetRequests(urls);
	
		
	
		//提取页面信息
		if(	page.getUrl().regex("http://www\\.7xz\\.com/.*/").match() ){
	
			
			Apk apk = Www7xz_Detail.getApkDetail(page);
			
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
	public static void main(String[] args){
		String s = "http://www.7xz.com/ipa?name=qmqj&add=/qmqj/34894.html&frm=tuijian";
		System.out.println(s.endsWith(".html&frm=tuijian"));
	}
}
