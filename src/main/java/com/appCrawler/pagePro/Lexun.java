package com.appCrawler.pagePro;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 乐讯 http://www.lexun.com/
 * Lexun  #68
 * 没有搜索接口，只有有限的几个应用，
 * @author DMT
 */
public class Lexun implements PageProcessor{
	private static  Logger logger = LoggerFactory.getLogger(Lexun.class);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	@Override
	public Apk process(Page page) {
		//index page
		if(page.getUrl().regex("").match()){
			
		}
		
		//the app detail page
		if(page.getUrl().regex("").match()){
		
		
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
