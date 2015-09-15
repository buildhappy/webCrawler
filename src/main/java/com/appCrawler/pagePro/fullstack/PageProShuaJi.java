package com.appCrawler.pagePro.fullstack;


import com.appCrawler.pagePro.apkDetails.PageProOnlineDown_Detail;
import com.appCrawler.pagePro.apkDetails.PageProShuaJi_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 刷机网  http://www.shuaji.net/
 * 编号160
 * 下载链接位于其他页面，需要一个缓存容器来暂时存储apk信息
 * @author buildhappy
 *
 */
public class PageProShuaJi implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProShuaJi.class);
    private final static Map<String , Apk> APKS = new ConcurrentHashMap<String , Apk>(); 
    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
	setSleepTime(PropertiesUtil.getInterval());
    
	@Override
	public Apk process(Page page) {
        // 获取搜索页面
        //if (page.getUrl().regex("http://www\\.shuaji\\.net/(html/.*)?.*").match()){
       	if (page.getUrl().regex("http://www\\.shuaji\\.net/android_soft.*").match()){
				//&& !page.getUrl().get().endsWith(".html") && !page.getUrl().get().contains("download.php")) {
        	//LOGGER.info("match success, url:{}", page.getUrl());
			// 获取详细链接，以及分页链接
			List<String> urlList = page.getHtml().links().regex("http://www\\.shuaji\\.net/android_soft/.*").all();

			Set<String> sets = Sets.newHashSet(urlList);
			for (String url : sets) {
				if (PageProUrlFilter.isUrlReasonable(url) && !url.startsWith("http://www.shuaji.net/plus/download.php?")) {
					page.addTargetRequest(url);
				}
			}

            // 获取分页信息
            List<String> pageUrlList = page.getHtml().links("//div[@class='dede_pages']").all();
            page.addTargetRequests(pageUrlList);
            // 打印搜索结果url
            //LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }


        if (page.getUrl().regex("http://www\\.shuaji\\.net/android_soft/.*").match() ||
        		page.getUrl().regex("http://www\\.shuaji\\.net/plus/download\\.php.*").match()) {
        	
            if(page.getUrl().regex("http://www\\.shuaji\\.net/plus/download\\.php.*").match()){
            	System.out.println("int download page");
                String downUrl = null;
                downUrl = page.getHtml().xpath("//a[@style='text-decoration:underline;']/@href").toString();
                //System.out.println("downUrl:" + downUrl);
                if(null != downUrl){
                    String id = page.getUrl().toString().split("&aid=|cid").length > 2?page.getUrl().toString().split("&aid=|&cid")[1]:null; //http://www.shuaji.net/plus/download.php?open=0&aid=20061&cid=3
                    
                	if(id != null){
                        Apk curapk = APKS.get(id);
                        if (null != curapk) {
                            curapk.setAppDownloadUrl(downUrl);
                            //APKS.put(id, curapk);
                            APKS.remove(id);
                            page.putField("apk", curapk);
                			if(page.getResultItems().get("apk") == null){
                				page.setSkip(true);
                			}
                        }
                    }
                }
            }else{
            	//System.out.println("get apk detail");
    			Apk apk = PageProShuaJi_Detail.getApkDetail(page);
    			
                String tempId = page.getHtml().xpath("//ul[@id='info']/li/span/script/@src").toString();
                String id = null;
                if (StringUtils.isNotEmpty(tempId)) {
                    id = tempId.split("aid=").length > 1 ? tempId.split("aid=")[1] : null;
                }
//                System.out.println(id + "dddd");
//                System.out.println(page.getHtml().get());
                id =  null == id ? StringUtils.substringBetween(page.getHtml().get(), "disdls.php?aid=", "\"") : id;

    			if(apk != null && id != null){
    				//System.out.println(apk.getAppDownloadUrl());
        			APKS.put(id, apk);
        			page.addTargetRequest(apk.getAppDownloadUrl());	
    			}

    			page.setSkip(true);
            }
		}else{
			page.setSkip(true);
		
        }
        return null;
}
	
	
	@Override
	public List<Apk> processMulti(Page page) {
		return PageProShuaJi_Detail.processMulti(page);
	
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
