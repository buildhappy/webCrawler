package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

 



import com.appCrawler.pagePro.apkDetails.Android173Sy_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * #169
 * 手游世界http://android.173sy.com/
 * 可以通过伪造下载链接来构造下载链接
 * http://android.173sy.com/download/downloadapk.php?id=13425&s=1
 * 将id后的参数修改成相应的apk的id就好
 * @author DMT
 *
 */


public class Android173Sy implements PageProcessor{

	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Apk3.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://android.173sy.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(PageProUrlFilter.isUrlReasonable(temp)
			  && !temp.contains("http://android.173sy.com/download/downloadapk"))
						page.addTargetRequest(temp);
		}
		
//		page.addTargetRequests(urls);
		
	
		            //提取页面信息  http://android.173sy.com/games/detail.php?gid=14322
		if(page.getUrl().regex("http://android.173sy.com/games/detail.*").match()){			
		
			Apk apk = Android173Sy_Detail.getApkDetail(page);
			
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
