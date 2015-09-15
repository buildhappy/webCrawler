package com.appCrawler.pagePro;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appCrawler.utils.MyNicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 酷快市场 http://www.aawap.net/
 * Aawap #117
 * @author DMT
 * (1)动态页面 完整的搜索结果需要动态获取
 */
public class Aawap_1 implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(3);
	static{
        //System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
        //System.setProperty("http.proxyPort", "8001");
        //System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
        //System.setProperty("https.proxyPort", "8001");
		//threadPool = new CountableThreadPool(threadNum);
	}
	@Override
	public Apk process(Page page) {
		//index page		http://aawap.net/aawap/search?searchName=%E6%96%97%E5%9C%B0%E4%B8%BB		
		if(page.getUrl().regex("http://aawap\\.net/aawap/search.*").match()){
			Page jsPage = null;
			try {
				jsPage = getDynamicPage(page.getUrl().toString());
				System.out.println(jsPage.getHtml().toString());
			} catch (FailingHttpStatusCodeException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//app的具体介绍页面	http://aawap.net/aawap/detail/1926ce78-64e9-486d-85a0-744edefc9822
			List<String> url1 = jsPage.getHtml().links("//ul[@id='app_ul']").all();

			//添加下一页url(翻页)
			//List<String> url2 = page.getHtml().links("//div[@class='mdpage']").regex("http://www\\.ruan8\\.com/search\\.php\\?.*").all();
			//url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			String regex1 = "(?=(http://aawap\\.net/aawap/detail/*)).*$";//"[\\S]+['html\\?ch=']+\\d$";
			Pattern p1 = Pattern.compile(regex1);
			Matcher m;
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				String url = it.next();
				if(p1.matcher(url).matches()){
					page.addTargetRequest(url);
					//System.out.println("match:" + url);
				}
			}
		}
		
		//the app detail page
		if(page.getUrl().regex("http://aawap\\.net/aawap/detail/*").match()){//http://www\\.ruan8\\.com/soft.*
			Apk apk = null;
			String appName = null;				//app名字
			String appDetailUrl = null;			//具体页面url
			String appDownloadUrl = null;		//app下载地址
			String osPlatform = null ;			//运行平台
			String appVersion = null;			//app版本
			String appSize = null;				//app大小
			String appUpdateDate = null;		//更新日期
			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
			String appvender = null;			//app开发者  APK这个类中尚未添加
			String appDownloadedTime=null;		//app的下载次数
			String description = null;
			appName = page.getHtml().xpath("//div[@class='appcont']/h3/text()").toString();	
				
			appDetailUrl = page.getUrl().toString();
			
			appDownloadUrl = page.getHtml().xpath("//p[@class='oper']/a/@href").toString();
			appSize = page.getHtml().xpath("//div[@class='appcont']/h5/text()").toString();	
			description = page.getHtml().xpath("//div[@class='maincontent']/text()").toString();
			List<String> info = page.getHtml().xpath("//div[@class='version']/p/text()").all();
			for(String s : info){
				//System.out.println(s);
			}
			if(info.size() > 1){
				appVersion = info.get(1).split("：").length > 1 ? info.get(1).split("：")[1] : null;
			}
			if(info.size() > 2){
				osPlatform = info.get(2).split("：").length > 1 ? info.get(2).split("：")[1] : null;
			}			
		
			if(appName != null && appDownloadUrl != null){
				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
				apk.setOsPlatform(osPlatform);
				apk.setAppDescription(description);
			}
			
			return apk;
		}
		
		return null;

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static Page getDynamicPage(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		MyNicelyResynchronizingAjaxController ajaxController = new MyNicelyResynchronizingAjaxController();
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
		//HtmlUnitDriver
		//设置webClient的相关参数
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(ajaxController);
		webClient.getOptions().setTimeout(35000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		//webClient.setAlertHandler( new CollectingAlertHandler(alertHandler));//将JavaScript中alert标签产生的数据保存在一个链表中
		String targetUrl = url;
		//模拟浏览器打开一个目标网址
		HtmlPage rootPage= webClient.getPage(targetUrl);
		//System.out.println(rootPage.asXml());
		Page page = new Page();
		page.setRawText(rootPage.asXml());
		page.setUrl(new PlainText(url));
		page.setRequest(new Request(url));
		return page;
	}
	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
