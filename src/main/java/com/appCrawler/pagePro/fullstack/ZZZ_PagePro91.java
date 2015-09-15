package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.ZZZ_PagePro91_Detail;
import com.appCrawler.utils.PropertiesUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
/**
 * 91手机门户 http://apk.91.com/
 * PagePro91.java #90
 *
 * @author DMT
 */
public class ZZZ_PagePro91 implements PageProcessor{
	private Logger logger = LoggerFactory.getLogger(ZZZ_PagePro91.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	@Override
	public Apk process(Page page) {
		logger.debug("crawler url: {}", page.getUrl());
		//index page					http://apk.91.com/soft/Android/search/3_5_0_0_%E4%BB%99%E5%89%91%E5%A5%87%E4%BE%A0%E4%BC%A0		
		if(page.getUrl().regex("http://apk\\.91\\.com/?|game|soft/").match()){
			logger.debug("match success, url:{}", page.getUrl());
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links().regex("http://apk\\.91\\.com/.*").all();

			//添加下一页url(翻页)  手动构造
			/*if (page.getUrl().regex("http://apk\\.91\\.com/game|soft/").match()) {
				String currenturl=page.getUrl().toString();
				char pagenum=(char)currenturl.codePointAt(38);
				List<String> url2 = page.getHtml().links("//div[@class='page_footer']").regex("这是不会匹配到的").all();
				if(pagenum == 49)
					for(int i=1;i<1;i++)
					{
						currenturl=currenturl.substring(0,38)+(++pagenum)+currenturl.substring(39, currenturl.length());
						url2.add(currenturl);
					}

				url1.addAll(url2);
			}*/

			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			for (String temp : urlSet) {
				if(!temp.contains("http://apk.91.com/soft/Controller.ashx?Action=Download&id="))				
					page.addTargetRequest(temp);
			}
		}
		
		//the app detail page
		 if(page.getUrl().regex("http://apk\\.91\\.com/Soft/Android.*").match()){
			Html html = page.getHtml();
			Apk apk = ZZZ_PagePro91_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
			page.setSkip(true);
		}

		return null;


	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
