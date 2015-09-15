package com.appCrawler.pagePro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class ZZTest implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(3);
	
	@Override
	public Apk process(Page page) {
		
		//String url = "http://shouji.baidu.com/comment\\?action_type=getCommentList\\&groupid=1\\%s\\&pn\\=\\%s";
        //String url = "http://shouji.baidu.com/comment/?action_type=getCommentList/&groupid=1/%s/&pn=/%s";
        String url = "http://shouji.baidu.com/comment?action_type=getCommentList%26groupid=1%25s%26pn=%25s";
        String encodedUrl=url;
		try {
			encodedUrl = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(encodedUrl);
        try {
            // 检查是否还有下一页
            String comtent = EntityUtils.toString(new HttpClientLib().getUrlReponse(url).getEntity());
            String pages = StringUtils.substringBetween(comtent, "<input type=\"hidden\" name=\"totalpage\" value=\"", "\"");
            System.out.println(pages);        
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally{
        	
        }
        /*
        CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://shouji.baidu.com/comment?action_type=getCommentList&groupid=1%s&pn=%s");
		StringEntity myEntity = new StringEntity("{\"channelId\":\"" + 1 + "\",\"keyword\":\"" + "邮储银行" + "\"," + "\"version\":\"" + "5.4.1" + "\"}",//{channelId:101 102 , keyword:qq}
				ContentType.create("application/json", "UTF-8"));
		httppost.setEntity(myEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			HttpEntity entity = response.getEntity();
			if(entity != null){
				try {
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		}finally{
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return null;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site getSite() {
		return site;
	}
	public static void main(String[] args){
		ZZTest test = new ZZTest();
		test.process(null);
	}
	
	private class HttpClientLib{
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
}
