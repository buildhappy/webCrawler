package com.appCrawler.pagePro.fullstack;

import com.appCrawler.pagePro.apkDetails.PageProPconline_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 太平洋[中国] app搜索抓取
 * url:http://ks.pconline.com.cn/download.shtml?q=qq
 * id:24
 * @version 1.0.0
 */
public class PageProPconline implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProPconline.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("gb2312").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://dl\\.pconline\\.com\\.cn/?|android/").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://dl\\.pconline\\.com\\.cn/download/.*").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
            	String temp = iter.next();
            	if(PageProUrlFilter.isUrlReasonable(temp))				
    				page.addTargetRequest(temp);
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://dl.pconline.com.cn/download/.*").match()) {
           	Html html = page.getHtml();
			Apk apk = PageProPconline_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
			page.setSkip(true);
		}
        return null;
    }

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    @Override
    public Site getSite() {
        return site;
    }

    //send get request to http://dlc2.pconline.com.cn/dltoken/61068_genLink.js
	public static String getHash(String metaUrl) throws ClientProtocolException, IOException{
		//http://dl.pconline.com.cn/download/78212.html
		String apkId = getApkId(metaUrl);
		StringBuffer jsUrl = new StringBuffer();
		jsUrl.append("http://dlc2.pconline.com.cn/dltoken/");
		jsUrl.append(apkId);
		jsUrl.append("_genLink.js");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(jsUrl.toString());
		httpGet.addHeader("Cache-Control", "max-age=0");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Host", "dlc2.pconline.com.cn");
		httpGet.addHeader("Referer", metaUrl);
		httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36");
		//httpGet.addHeader("Connection", "keep-alive");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		String hashCode = null;
		try{
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String content = EntityUtils.toString(entity, "UTF-8"); 
				//genLink('wArMGj7a')
				String[] ss = content.split("genLink\\('");
				if(ss.length > 1){
					hashCode = ss[1].split("'\\)")[0];
				}
			}
		}finally{
			response.close();
		}
		return hashCode;
	}
	//get apkId from meta url
	//such as:http://dl.pconline.com.cn/download/61068.html
	//return:61068
	public static String getApkId(String url){
		//String s = "http://dl.pconline.com.cn/download/61068.html";
		String[] ss = url.split("http://dl.pconline.com.cn/download/");
		if(ss.length > 1){
			return ss[1].split(".html")[0];
		}else{
			return null;
		}
		
	}
	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args){
		String s = "http://dlc2.pconline.com.cn/filedown_170210_6742083/Buyudaren_jjb.apk";
		String[] ss = s.split("/");
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("http://");
		for(int i = 2; i < ss.length - 1;i++){
			strBuf.append(ss[i] + "/");
			System.out.println(ss[i]);
		}
		strBuf.append("vdf/");
		strBuf.append(ss[ss.length - 1]);	
		System.out.println(strBuf.toString());
	}
}
