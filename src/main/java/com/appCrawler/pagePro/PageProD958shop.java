package com.appCrawler.pagePro;

import java.util.Iterator;
import java.util.List;

import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import com.google.common.collect.Sets;

public class PageProD958shop implements PageProcessor{
	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PageProD958shop.class);
	Site site = Site.me().setCharset("gb2312").setRetryTimes(2).setSleepTime(3);

	@Override
	public Apk process(Page page) {
		LOGGER.info("PageProD958shop: Apk process(Page page)");
        // 获取搜索页面
        if (page.getUrl().regex("http://zhannei\\.958shop\\.com/cse/search\\?q=*").match()) {
        	LOGGER.info("match success, url:{}", page.getUrl());
            // 获取详细链接
            List<String> urlList = page.getHtml().links("//div[@id='container']").all();
            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String url = iter.next();
            	if(url.startsWith("http://d.958shop.com/s/") || url.startsWith("http://d.958shop.com/soft/") 
            			|| url.startsWith("http://d.958shop.com/mobile/")||url.startsWith("http://d.958shop.com/game/")
            			|| url.startsWith("http://d.958shop.com/t/")){
                    page.addTargetRequest(url);
            	}
            }
            // 获取分页信息
            //List<String> pageUrlList = page.getHtml().links("//div[@class='page']").all();
            //page.addTargetRequests(pageUrlList);
            // 打印搜索结果url
            LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }
        //http://d.958shop.com/t/类网站(搜索结果)
        if (page.getUrl().regex("http://d\\.958shop\\.com/t/*").match()) {
        	System.out.println("matched in http://d.958shop.com/t/");
            List<String> urlList = page.getHtml().links("//div[@class='new_theme same_tab']").all();
            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String url = iter.next();
            	if(url.startsWith("http://d.958shop.com/soft/") || url.startsWith("http://d.958shop.com/mobile/")||
            			url.startsWith("http://d.958shop.com/game/")){
                    page.addTargetRequest(url);
            	}
            }
        }
        //the detail page
		if(page.getUrl().regex("http://d\\.958shop\\.com/mobile/*").match() || 
				page.getUrl().regex("http://d\\.958shop\\.com/soft/*").match() ||
				page.getUrl().regex("http://d\\.958shop\\.com/game/*").match()){
			//System.out.println("matched in");
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String osPlatform = null ;			//运行平台
			String appUpdateDate = null;		//更新日期
			String description = null;
			String downloadUrl = null;
			String version = null;
			String size = null;
			String downloadTimes = null;
			String vender = null;//开发商
			
			appName=page.getHtml().xpath("//table[@class='m_word']/tbody/tr/td/span/text()").toString();
			appDetailUrl = page.getUrl().toString();
			if(null != appName){
				List<String> infos = null;
				infos = page.getHtml().xpath("//table[@class='m_word']/tbody/tr/td/a/text()").all();
				int length = infos.size();
				version = length > 1 ? infos.get(1):null;
				infos = page.getHtml().xpath("//table[@class='m_word']/tbody/tr/td/text()").all();
				//System.out.println(appName); 
				for(String s : infos){
					if(s.startsWith("大小：")){
						size = s.split("大小：").length >= 2 ? s.split("大小：")[1]:null;
						//System.out.println("size:" + size);
					}
					if(s.startsWith("上传时间：")){
						appUpdateDate = s.split("上传时间：").length >= 2 ? s.split("上传时间：")[1]:null;
						//System.out.println("appUpdateDate:" + appUpdateDate);
					}
					if(s.startsWith("浏览/下载次数：")){
						downloadTimes = s.split("浏览/下载次数：").length >= 2 ? s.split("浏览/下载次数：")[1]:null;
						downloadTimes = downloadTimes.split("/|次").length > 1?downloadTimes.split("/|次")[1]:null;
						//System.out.println("downloadTimes:" + downloadTimes);
						break;
					}
					
				}
				description = page.getHtml().xpath("//dl[@class='word_js']/dd/text()").toString();
				downloadUrl = page.getHtml().xpath("//div[@class='todown1']/a/@href").toString();
				infos = page.getHtml().xpath("//table[@class='m_word']/tbody/tr/td/a/text()").all();

				if(infos.size() > 2){
					osPlatform = infos.get(2);
				}
				if(null != osPlatform && osPlatform.trim().toLowerCase().contains("android")){
					Apk apk = new Apk(appName,appDetailUrl,downloadUrl,osPlatform,version,size,appUpdateDate,null);
					apk.setAppDescription(description);
					//System.out.println("appName:" + appName + " description:" + description + " downloadUrl:" + downloadUrl);
					return apk;
				}
			}
		}
        return null;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}
	
//	public static void main(String[] args){
//		String s = "运行平台：<a href=\"http://d.958shop.com/search/?r=%f7%c8%d7%e5M8&cn=soft\" target=\"_blank\">魅族M8</a>;";
//		s.replace("\"", "");
//		System.out.println(s);
//		String[] ss = s.split("target=\"_blank\">");
//		for(String t:ss){
//			System.out.println(t);
//		}
//		
//		s = ss[1];
//		ss = s.split("</a>;");
//		System.out.println(ss[0]);
//			
//	}
}