package com.appCrawler.pagePro;

import com.appCrawler.pagePro.apkDetails.PageProBaidu_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 百度[中国] app搜索抓取
 * url:http://shouji.baidu.com/s?wd=QQ&data_type=app&f=header_all%40input
 *
 * @version 1.0.0
 */
public class PageProBaidu implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProBaidu.class);

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
        if (page.getUrl().regex("http://shouji.baidu.com/s\\?wd=*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接
            List<String> urlList = page.getHtml().links("//ul[@class='app-list']/li/div[@class='app']/div[@class='icon']").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            // 获取分页信息
            String keyWord = page.getHtml().xpath("//form[@class='page-form']/input[@name='wd']/@value").get();
            String totalPage = page.getHtml().xpath("//form[@class='page-form']/input[@class='total-page']/@value").get();

            if (StringUtils.isNotEmpty(totalPage)) {
                // 处理分页
                for (int i = 1; i <= Integer.valueOf(totalPage); i++) {
                    String u = "http://shouji.baidu.com/s?wd=" + keyWord + "&data_type=app&f=header_all%40input#page" + i + "";
                    page.addTargetRequest(u);
                }
            }
            // 打印搜索结果url
            LOGGER.info("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://shouji.baidu.com/soft|software/item\\?*").match() || page.getUrl().regex("http://shouji.baidu.com/game/item\\?*").match() ) {
        	return PageProBaidu_Detail.getApkDetail(page);
			
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
		// TODO Auto-generated method stub
		return null;
	}
}
