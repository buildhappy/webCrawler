package com.appCrawler.pagePro;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PagePro163_Detail;
import com.appCrawler.utils.PropertiesUtil;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 网易应用中心[中国] app搜索抓取
 * url:http://m.163.com/android/search.html?platform=2&query=DOTA
 *
 * @version 1.0.0
 */
public class PagePro163 implements PageProcessor {

    // 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro163.class);

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
        if (page.getUrl().regex("http://m.163.com/ajax/search/result/2/*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            try {
                // 获取对应的url
                Map map = new ObjectMapper().readValue(page.getHtml().xpath("//body/text()").toString(), Map.class);
                List<Map<String, String>> urlMapList = (List<Map<String, String>>) map.get("items");

                // 找出详细信息页面
                if (null != urlMapList) {
                    for (Map<String, String> m : urlMapList) {
                        page.addTargetRequest(new StringBuilder("http://m.163.com").append(m.get("link")).toString());
                    }
                }

            }
            catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("json parse error!", e);
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://m.163.com/android/software/*").match()) {
        	return PagePro163_Detail.getApkDetail(page);
			
		
		}

        return null;
    }

    /**
     * get the site settings
     *
     * @return site
     * @see us.codecraft.webmagic.Site
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
