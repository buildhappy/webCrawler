package com.appCrawler.pagePro.fullstack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appCrawler.pagePro.apkDetails.PageProMyapp_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.PageProUrlFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 应用宝[中国] app搜索抓取
 * url:http://android.myapp.com/myapp/searchAjax.htm?kw=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA&pns=&sid=
 *id:59
 * @version 1.0.0
 */
public class PageProMyapp implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMyapp.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
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
        if (page.getUrl().regex("http://android\\.myapp\\.com/myapp/.*").match()) {

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links().regex("http://android\\.myapp\\.com/myapp/.*").all();

            Set<String> sets = Sets.newHashSet(urlList);
            for (String url : sets) {
                if (PageProUrlFilter.isUrlReasonable(url)) {
                    page.addTargetRequest(url);
                }
            }
        }

        // 获取信息
        if (page.getUrl().regex("http://android\\.myapp\\.com/myapp/detail\\.htm\\?apkName=.*").match()) {
            Apk apk = PageProMyapp_Detail.getApkDetail(page);
            page.putField("apk", apk);
            if (page.getResultItems().get("apk") == null) {
                page.setSkip(true);
            }
        }
        else {
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

	@Override
	public List<Apk> processMulti(Page page) {
		return null;
	}

    /**
     * 处理子请求获取下页评论信息
     *
     * @param url 请url
     * @return
     */
  /*  private JSONObject subReq(String url) {
        try {
        	LOGGER.info("in subReq before");
        	LOGGER.info("url:" + url);
            String data = EntityUtils.toString(new HttpClientLib().getUrlReponse(url).getEntity());
            LOGGER.info("in subReq after");
            return JSON.parseObject(data);
        }
        catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("get commit error");
        }
        return null;
    }
    
    public static void main(String[] args) throws ParseException, IOException{
    	String url = "http://android.myapp.com/myapp/app/comment.htm?apkName=cmb.pb&apkCode=310&p=1&fresh=0.9385676326164064&contextData=";
    	String data = EntityUtils.toString(new HttpClientLib().getUrlReponse(url).getEntity());
    	System.out.println(data);
    }*/
}
