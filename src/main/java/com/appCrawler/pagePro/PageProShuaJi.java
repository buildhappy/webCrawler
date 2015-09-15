package com.appCrawler.pagePro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.appCrawler.pagePro.apkDetails.PageProShuaJi_Detail;
import com.appCrawler.utils.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
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
        if (page.getUrl().regex("http://www\\.shuaji\\.net/plus/search.php*").match()) {
        	LOGGER.info("match success, url:{}", page.getUrl());
            // 获取详细链接
            List<String> urlList = page.getHtml().links("//div[@class='resultlist']").all();
            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String url = iter.next();
                page.addTargetRequest(url);
                //System.out.println("detail url:" + url);
            }

            // 打印搜索结果url
            LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }

		else if(page.getUrl().regex("http://www\\.shuaji\\.net/plus|html/sjsoft/.*").match()){
			LOGGER.info("detail match success, url:{}", page.getUrl());
			PageProShuaJi_Detail.getApkDetail(page);
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
