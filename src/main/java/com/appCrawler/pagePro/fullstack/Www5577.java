package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import com.appCrawler.pagePro.apkDetails.Www5577_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 我机网 http://www.5577.com
 * www5577 #97
 * (1)the app detail page有http://www.5577.com/s/[0-9]* 
 * 						  http://www.5577.com/youxi/[0-9]* 
 * 						  http://www.5577.com/azpj/[0-9]*
 * 	三种 ，其中后两种的页面布局相同
 * @author DMT
 */

public class Www5577 implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Www5577.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.5577\\.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(!temp.contains("http://www.5577.com/down.asp?id=") 
					&& PageProUrlFilter.isUrlReasonable(temp))				
				page.addTargetRequest(temp);
		}
		page.addTargetRequest("http://www.5577.com/s/32976.html");
//		
		
	
		//提取页面信息
		if(page.getUrl().regex("http://www\\.5577\\.com/s/[0-9]*\\.html").match() 
				|| page.getUrl().regex("http://www\\.5577\\.com/youxi/[0-9]*\\.html").match()
				|| page.getUrl().regex("http://www\\.5577\\.com/az[a-z]+/[0-9]*\\.html").match()){
	
			
			Apk apk = Www5577_Detail.getApkDetail(page);
			
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
