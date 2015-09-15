package com.appCrawler.pagePro.fullstack;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.Fpwap_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;


public class Fpwap implements PageProcessor{
	//Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(0);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(Fpwap.class);

	public Apk process(Page page) {		
		List<String> urls =page.getHtml().links().regex("http://www\\.fpwap\\.com/.*").all() ;

		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		for (String temp : cacheSet) {
			temp = temp.toLowerCase();
			if(!temp.contains("http://www.fpwap.com/downfile") && PageProUrlFilter.isUrlReasonable(temp)
					&& !temp.contains("http://www.fpwap.com/common/showdownloadurl.php")
					&& !temp.startsWith("http://www.fpwap.com/news/"))
				//http://www.fpwap.com/Common/ShowDownloadUrl.php?classid=194&id=29900&pathid=0
				//http://www.fpwap.com/downfile_uc.php?id=1345
				page.addTargetRequest(temp);
		}
//		page.addTargetRequests(urls);
	
		if(page.getUrl().regex("http://www\\.fpwap\\.com/game/.+/").match()
				|| page.getUrl().regex("http://www\\.fpwap\\.com/game/list_.+").match())
		{
			page.setSkip(true);
			return null;
		}
		//提取页面信息
		if(page.getUrl().regex("http://www\\.fpwap\\.com/game/.+").match()){
			
			Apk apk = Fpwap_Detail.getApkDetail(page);
			
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
