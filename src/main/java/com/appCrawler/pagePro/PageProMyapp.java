package com.appCrawler.pagePro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.appCrawler.pagePro.apkDetails.PageProMyapp_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 应用宝[中国] app搜索抓取
 * url:http://android.myapp.com/myapp/searchAjax.htm?kw=%E6%8D%95%E9%B1%BC%E8%BE%BE%E4%BA%BA&pns=&sid=
 *
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
        if (page.getUrl().regex("http://android\\.myapp\\.com/myapp/searchAjax\\.htm\\?.*").match()) {
            LOGGER.debug("match success, url:{}", page.getUrl());

            try {
                // 获取对应的url
                Map map = new ObjectMapper().readValue(page.getHtml().xpath("//body/text()").toString(), Map.class);
                List<Map<String, String>> urlMapList = (List<Map<String, String>>) ((Map<String, Object>)map.get("obj")).get("appDetails");

                // 找出详细信息页面
                if (null != urlMapList) {
                    for (Map<String, String> m : urlMapList) {
                        page.addTargetRequest(new StringBuilder("http://android.myapp.com/myapp/detail.htm?apkName=").append(m.get("pkgName")).toString());
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
        if (page.getUrl().regex("http://android\\.myapp\\.com/myapp/detail\\.htm\\?apkName=.*").match()) {
        	return PageProMyapp_Detail.getApkDetail(page);
  
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
    private JSONObject subReq(String url) {
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
    }
    
}

class HttpClientLib{
	  public HttpResponse getUrlReponse(String url){
		  HttpResponse response = null;
		 // HttpRequest request =null;
		 CloseableHttpClient client= HttpClients.createDefault();
		 HttpGet httpGet =new HttpGet(url);
		 HttpContext localContext=getHttpContext(true);
		 try {
			response=client.execute(httpGet, localContext);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return response;
	  }
	  public HttpContext getHttpContext(boolean setCookieStore){

		  if(setCookieStore){
			  HttpClientContext clientContext= HttpClientContext.create();
			  return clientContext;
		  }else{
			  HttpContext localContext=new BasicHttpContext();
			  return localContext;
		  }
	}
}