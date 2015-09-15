package com.appCrawler.pagePro.apkDetails;

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
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.List;

/**
 * 手机乐园[中国] app搜索抓取
 * url:http://soft.shouji.com.cn/sort/search.jsp?html=soft&phone=100060&inputname=soft&softname=MT&thsubmit=%E6%90%9C%E7%B4%A2
 *
 * @version 1.0.0
 */
public class PageProMobilePhone_Detail {


    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProMobilePhone_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();
        String osPlatform = null;
        String appSize =null;
        String appUpdateDate =null;
        String appType =null;
        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String rowName = html.xpath("//div[@class='listop']/li/span/text()").toString();
        String appName = StringUtils.substringBeforeLast(rowName, " ");
        String appVersion = StringUtils.substringAfterLast(rowName, " ");
        String appDownloadUrl = html.xpath("//ul[@class='down']/dl/li[@class='sne']/span[@class='bdown']/a[1]/@href").toString();       
        String appDescription = html.xpath("//ul[@class='synopsis']/dd/text()").get();
        List<String> appScreenshot = html.xpath("//div[@id='List1']/div/img/@src").all();
        String appTag = null;
        String appCategory = html.xpath("//li[@class='nav3']/a[2]/text()").get();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;
        if(page.getUrl().regex("http://soft\\.shouji\\.com\\.cn/down/.*").match()){
        	 String moreInfo = html.xpath("//ul[@class='down']/dl/li[@class='sne']/span[@class='adown']/font/text()").toString();
             if(moreInfo!=null){
             osPlatform = StringUtils.substringAfterLast(moreInfo, "/").replace(" ", "");
             appSize = StringUtils.substringBefore(moreInfo, "/").replace(" ", "");
             appUpdateDate = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(moreInfo, "/"), "/");
             appType = StringUtils.substringBetween(moreInfo, "/", "/");
             } 	                	
        }
        else{
        	String moreInfo = html.xpath("//ul[@class='down']/dl/li[@class='sne']/span[@class='adown']/text()").toString();
            if(moreInfo!=null){
            osPlatform = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(moreInfo, "/"), "/");
            appSize = StringUtils.substringBetween(moreInfo, "(", "/").replace(" ", "");
            appUpdateDate = StringUtils.substringBefore(StringUtils.substringAfterLast(moreInfo, "/"), ")");
            appType = StringUtils.substringBetween(moreInfo, "/", "/");
            } 	  
        	
        }
        // 处理评论信息
      /*  List<String> appUrls = Lists.newArrayList();
        String id = StringUtils.substringBetween(page.getUrl().get(), "down/", ".");
        String url = "http://soft.shouji.com.cn/sort/message/bbs.ncp?id="+id+"&offset=10";
        appUrls.add(url);

        HttpClientLib clientLib = new HttpClientLib();
        String content = clientLib.getUrlRespHtml(url);
        String num = StringUtils.substringBetween(content, "<span class=\"pnum\">", "</span>");
        if (StringUtils.isNotEmpty(num)) {
            for (int i = 20; i < Integer.valueOf(num) -10; i = i+10) {
                appUrls.add("http://soft.shouji.com.cn/sort/message/bbs.ncp?id="+id+"&offset="+i);
            }
        }
        appCommentUrl = StringUtils.substringBetween(appUrls.toString(), "[","]");*/

        Apk apk = null;
        if (null != appName && null != appDownloadUrl && appType.equals("APK")) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, appType);
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
            apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
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
	public String getUrlRespHtml(String url) {
		String responseStr = null;
		try {
			responseStr = EntityUtils.toString(getUrlReponse(url).getEntity(), "UTF-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseStr;
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
