package com.appCrawler.pagePro;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.appCrawler.utils.JSParserUtil;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
/**
 * 魅族[中国]
 * problem:全是js生成的页面无法获取url
 * 尝试用WebClient截取ajax请求的地址
 * @author Administrator
 *
 */
public class FlymePagePro implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setSleepTime(2);

	public Apk process(Page page) {
		String urlMacher1 = "http://app\\.flyme\\.cn/apps/public/detail\\?.*";
		String urlMacher2 = "http://app\\.flyme\\.cn/games/public/detail\\?.*";
		if(page.getUrl().regex(urlMacher1).match() || 
		   page.getUrl().regex(urlMacher2).match()){//如果是apk详细的介绍页面的话，则从该页面获取想要的信息
			//System.out.println("maches");
			List<String> ajaxInfo = null;
			String downloadUrl = null;
			String name = null;
			String version = null;
			String os = null;
			try {
				//获取ajax的请求地址
				ajaxInfo = JSParserUtil.getAjaxUrl(page.getUrl().toString() ,"//a[@class='price']" , 0);
				if(!ajaxInfo.isEmpty()){
					if(ajaxInfo.get(0).split("-").length > 0){
						name = ajaxInfo.get(0).split("-")[1];
					}
					version = page.getHtml().xpath("//div[@class='app_content ellipsis noPointer']/text()").toString();
					os = "Flyme OS通用";
					int urlsSize = ajaxInfo.size();
					//访问该url获取其response
					//HttpClientLib httpClient = new HttpClientLib();

					//获取最后一个ajax请求的url，访问该url可以获取apk的下载链接
					//String htmlStr = httpClient.getUrlRespHtml(ajaxInfo.get(urlsSize - 1));//向ajax请求的url发送请求，得到其response
					/*返回的htmlStr格式：
					 * {
						code: 200,
						message: "",
						redirect: "",
						value: - {
							downloadUrl: "http://app.res.meizu.com/cabs/freeapk/1141/com.myzaker.zaker_phone_smartbar_4.3.1_1408616426.apk",
							isFree: true
						}
					}*/
					
					CloseableHttpClient httpClient = HttpClients.createDefault();
					HttpGet get = new HttpGet(ajaxInfo.get(urlsSize - 1));
					CloseableHttpResponse response = httpClient.execute(get);
					HttpEntity entity = null;
					try{
						entity = response.getEntity();
					}catch(Exception e){
						e.printStackTrace();
					}
					String htmlStr = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonObj = JSONObject.fromObject(htmlStr);
					//System.out.println(jsonObj.toString());
					jsonObj = JSONObject.fromObject(jsonObj.getString("value"));
					//System.out.println(jsonObj.toString());
					//name = page.getHtml().xpath("").toString();
					downloadUrl = jsonObj.getString("downloadUrl");
					//System.out.println(downloadUrl);
				}
				
			} catch (FailingHttpStatusCodeException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//获取应用和游戏主题下的所有分类页面id
			List<String> allCategory = null;
			allCategory = page.getHtml().xpath("ul[@id='categoryList']/li/@data-param").all();
			Iterator it = null;
			if(page.getUrl().toString().contains("apps")&&page.getUrl().toString().contains("category") && allCategory != null){
				it = allCategory.iterator();
				while(it.hasNext()){
					//将所有分类页面加入到爬取队列中
					String targetUrlPageNum = getTargePageNumByUrl(page);
					if(targetUrlPageNum != null){
						page.addTargetRequest("http://app.flyme.cn/apps/public/category/" + it.next()+"/all/feed/index/" + targetUrlPageNum + "/18");
					}
				}
				//将该分类页面中的所有apk下载链接加入到下载队列中
				page.addTargetRequests(page.getHtml().xpath("//div[@class='search_one downloading']/a/@href").all());
			}
			if(page.getUrl().toString().contains("games") &&page.getUrl().toString().contains("category")&& allCategory != null){
				it = allCategory.iterator();
				while(it.hasNext()){
					//将所有分类页面加入到爬取队列中
					String targetUrlPageNum = getTargePageNumByUrl(page);
					if(targetUrlPageNum != null){
						page.addTargetRequest("http://app.flyme.cn/games/public/category/" + it.next()+"/all/feed/index/" + targetUrlPageNum + "/18");
					}
				}
				//将该分类页面中的所有apk下载链接加入到下载队列中
				page.addTargetRequests(page.getHtml().xpath("//div[@class='search_one downloading']/a/@href").all());
			}
			if(page.getUrl().toString().contains("detail?package_name")){
				
			}
			
			//page.addTargetRequests(page.getHtml().links().regex("http://app\\.flyme\\.cn/apps/public/.*").all());
			//page.addTargetRequests(page.getHtml().links().regex("http://app\\.flyme\\.cn/games/public/.*").all());
		}
		return null;
	}

	@Override
	public Site getSite() {
		return site;
	}
	
	//取出应用和游戏类页面下的url，根据该url地址获取当前页为第几页
	//url样例：http://app.flyme.cn/apps/public/category/104/all/feed/index/0/18
	public String getTargePageNumByUrl(Page page){
		String url = page.getUrl().toString();
		String[] info = url.split("/");
		int curPageNum = 0;
		String targetPageNum = null;
		if(info.length > 9){
			curPageNum = Integer.parseInt(info[info.length - 2]);
		}
		if(page.getHtml().xpath("//div[@id='app']/div").all().size() == 18){
			targetPageNum = Integer.toString(curPageNum + 18);
		}
		return targetPageNum;
	}
	
	public static void main(String[] args){
//		String url = "http://app.flyme.cn/apps/public/download.json?app_id=882049";
//		HttpClientLib httpClient = new HttpClientLib();
//		//HttpResponse response = httpClient.getUrlReponse(url);
//		String htmlStr = httpClient.getUrlRespHtml(url);
//		JSONObject json = JSONObject.fromObject(htmlStr);
//		
//		json = JSONObject.fromObject(json.getString("value").toString());
//		System.out.println(json.getString("downloadUrl"));
		
//		String url = "http://app.flyme.cn/apps/public/category/104/all/feed/index/10/18";
//		System.out.println(getPageNumByUrl(url));
		
	}


	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
}
