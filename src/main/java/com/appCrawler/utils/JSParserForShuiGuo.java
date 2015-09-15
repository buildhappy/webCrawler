package com.appCrawler.utils;

/**
 * 
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class JSParserForShuiGuo {

	public static String  getDownloadUrl(String url) throws Exception{
//		String url = "http://app.flyme.cn/games/public/detail?package_name=com.cyou.cx.mtlbb.mz";
//		MyNicelyResynchronizingAjaxController ajaxController = new MyNicelyResynchronizingAjaxController();
		
		long start = System.currentTimeMillis();
		System.out.println(start);
		//String url = "http://www.shuiguo.com/android/qpby/adown.html";
		List alertHandler = new LinkedList();;
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);//CHROME);
		webClient.setAjaxController(new MyNicelyResynchronizingAjaxController());
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(3500);
		webClient.getOptions().setThrowExceptionOnScriptError(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().isRedirectEnabled();
		webClient.setAlertHandler( new CollectingAlertHandler(alertHandler));//将JavaScript中alert标签产生的数据保存在一个链表中
		//webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage page = null;
		
		JavaScriptEngine engine = new JavaScriptEngine(webClient);
		webClient.setJavaScriptEngine(engine);
		
		page = webClient.getPage(url);
		//System.out.println(page.asXml());
		//page.executeJavaScript(sourceCode)
		HtmlElement htmlElement = (HtmlElement)(page.getByXPath("//a[@class='sd_xz sd_az1']").get(0));//get download url from www.anzhi.com
		System.out.println(htmlElement.asXml());
		
		String[] ss = htmlElement.asXml().split("href=");
		String s = ss.length > 1 ? ss[1] : null;
		s = s.split("<div").length > 1?s.split("<div")[0]:null;
		s = s.replace("\"", "");
		s = s.replace(">", "");
		System.out.println(s);
		return s;

	}
}
