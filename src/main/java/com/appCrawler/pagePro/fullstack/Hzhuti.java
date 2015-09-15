package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.appCrawler.pagePro.apkDetails.Hzhuti_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;
/**
 * 好主题 http://www.hzhuti.com/
 * Hzhuti #92
 * @author DMT
 */

public class Hzhuti implements PageProcessor{

	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Hzhuti.class);

	public Apk process(Page page) {
	
		//System.out.println(page.getHtml().toString());
		List<String> urls =page.getHtml().links().regex("http://www\\.hzhuti\\.com/.*").all() ;
		
 		
		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			if(!temp.contains("http://www.hzhuti.com/plus/download.php")
					&& PageProUrlFilter.isUrlReasonable(temp))
				page.addTargetRequest(temp);
			
		}
		
	
		//提取页面信息
		if(page.getUrl().regex("http://www\\.hzhuti\\.com/soft.*").match() 
				|| page.getUrl().regex("http://www\\.hzhuti\\.com/android.*").match()
				|| page.getUrl().regex("http://www\\.hzhuti\\.com/anzhuo.*").match()){
	
			
			Apk apk = Hzhuti_Detail.getApkDetail(page);
			
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
