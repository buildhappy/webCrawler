package com.appCrawler.pagePro;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appCrawler.pagePro.apkDetails.PagePro360_Detail;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 评论的获取：
 * qq的评论网址：http://intf.baike.360.cn/index.php?name=qq&c=message&a=getmessage&start=1&count=10
 * name后的内容从源码的baike_name中获取
 * 返回的评论内容包含了评论的总数，要根据总数和start，count参数获取所有的url。
 * 首页：http://zhushou.360.cn/
 * 搜索页：http://zhushou.360.cn/search/index/?kw=qq
 *
 */
public class PagePro360 implements PageProcessor{
	// 日志管理对象
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro360.class);

    // 定义网站编码，以及间隔时间
    Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);

    /**
     * 分页链接缓存
     */
    private Set<String> cacheSet = Sets.newHashSet();

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page
     */
    @Override
    public Apk process(Page page) {
        LOGGER.debug("crawler url: {}", page.getUrl());

        // 获取搜索页面
        if (page.getUrl().regex("http://zhushou\\.360\\.cn/search/index/\\?kw=.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            // 获取详细链接，以及分页链接
            List<String> urlList = page.getHtml().links("//div[@class='SeaCon']/ul").regex("http://zhushou\\.360\\.cn/detail/index/soft_id/.*").all();

            Iterator<String> iter = Sets.newHashSet(urlList).iterator();
            while (iter.hasNext()) {
                page.addTargetRequest(iter.next());
            }

            int pageNums = Integer.valueOf(StringUtils.substringBetween(page.getRawText(), "parseInt('", "',"));

            for (int i = 1; i <= pageNums; i++) {
                String url = null;
                if (page.getUrl().get().contains("&page")) {
                    url = page.getUrl().get().replaceAll("&page=[0-9]]", "&page=" + i);
                }
                else {
                    url = page.getUrl().get() + "&page=" + i;
                }

                if (!cacheSet.contains(url)) {
                    page.addTargetRequest(url);
                    cacheSet.add(url);
                }
            }

            // 打印搜索结果url
            LOGGER.debug("app info results urls: {}", page.getTargetRequests());
        }

        // 获取信息
        if (page.getUrl().regex("http://zhushou\\.360\\.cn/detail/index/soft_id/.*").match()) {
        	return PagePro360_Detail.getApkDetail(page);
			

		}

        	/*
        	// 获取dom对象
            Html html = page.getHtml();

            // 找出对应需要信息
            String appDetailUrl = page.getUrl().toString();
            String appName = html.xpath("//dl[@class='clearfix']/dd/h2/span/text()").toString();
            String appVersion = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[1]/text()").get();
            String appDownloadUrl = "";
            String osPlatform = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[2]/text()").get();
            String appSize = html.xpath("//div[@class='pf']/span[4]/text()").get();
            String appUpdateDate = html.xpath("//div[@class='base-info']/table/tbody/tr[1]/td[2]/text()").get();
            String appType = null;
            String downcount= StringUtils.substringAfter(html.xpath("//div[@class='pf']/span[3]/text()").get(), "：");
            String appDescription = html.xpath("//div[@class='breif']/text()").get();
            // 评论url
            String commontUrl = "";

            LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, "
            		+ "commontUrl:{}, downloadNum:{}, appDesc:{}", appName, appVersion, appDownloadUrl, 
            		appSize, appType, osPlatform, appUpdateDate, commontUrl, downcount, appDescription);
            */
        	
            /*
            if (null != appName && null != appDownloadUrl) {
                Apk apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
                
                apk.setAppCommentUrl(commontUrl);
                */
                /*在服务器上运行这部分代码时一直报错，暂时注释掉
                try {
                    // 处理评论
                    HttpClientLib clientLib = new HttpClientLib();
                    commontUrl = String.format("http://intf.baike.360.cn/index.php?name=%s&c=message&a=getmessage&start=1&count=20", URLEncoder.encode(appName, "UTF-8"));
                    HttpResponse response = clientLib.getUrlReponse(commontUrl);

                    // 获取总条数
                    JSONObject json = JSON.parseObject(EntityUtils.toString(response.getEntity()));
                    if (!"-1".equals(json.getString("errno"))) {
                        int count = json.getJSONObject("data").getInteger("total");

                        // 处理分页评论url
                        List<String> commList = new ArrayList<String>();
                        int loop = count / 20 + 1;
                        for (int i = 1; i <= loop; i++) {

                            /// 需要再APK再加个List存放
                            commList.add(String.format("http://intf.baike.360.cn/index.php?name=%s&c=message&a=getmessage&start=%s&count=20", URLEncoder.encode(appName, "UTF-8"), i * 20 + 1));
                        }

                        apk.setAppCommentUrl(StringUtils.substringBetween(commList.toString(), "[", "]"));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                 */
                
                /*
                return apk;
            }*/
        

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
