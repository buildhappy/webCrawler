package com.appCrawler.utils;

/**
 * 对于动态网页，如果无法获取apk的下载链接，我们可以用浏览器模拟相应的下载操作(比如点击下载按钮)，
 * 然后从获取response的url，
 * 该url一般是ajax的url(即通过该url可以获取该apk的json数据，这些数据里面会有该apk的下载链接信息)
 */
import java.io.IOException;
import java.net.MalformedURLException;


import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.History;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class JSParserUtil {

	private static String TARGET_URL = "http://databank.worldbank.org/ddp/home.do";
	public static void setTARGET_URL(String url){
		JSParserUtil.TARGET_URL = url;
	}
	/**
	 * @param clickOfXpath:页面待点击按钮的xpath表达式
	 * @param index:
	 * @return List<String>:链表的第一个信息是页面的title，以后的信息是所有的ajax的url
	 */
	public static List<String> getAjaxUrl(String targetUrl, String clickOfXpath , int index) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//TARGET_URL = "http://app.flyme.cn/apps/public/detail?package_name=com.myzaker.zaker_phone_smartbar";
		List<String> urls = new LinkedList<String>();
		//每次ajax请求时都会创建一个AjaxController对象，在该对象中可以查看ajax请求的地址
		MyNicelyResynchronizingAjaxController ajaxController = new MyNicelyResynchronizingAjaxController();
		
		List alertHandler = new LinkedList();;
		//模拟一个浏览器
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		//HtmlUnitDriver
		//设置webClient的相关参数
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(ajaxController);
		webClient.getOptions().setTimeout(35000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.setAlertHandler( new CollectingAlertHandler(alertHandler));//将JavaScript中alert标签产生的数据保存在一个链表中
		
		//模拟浏览器打开一个目标网址
		HtmlPage rootPage= webClient.getPage(targetUrl);
		urls.add(rootPage.getTitleText());
		urls.add(ajaxController.getVisitUrl());
		//System.out.println("url1:" + url);
		HtmlElement elementA = (HtmlElement) rootPage.getByXPath(clickOfXpath).get(index);
		Page page  = elementA.click();
		urls.add(ajaxController.getVisitUrl());
		return urls;
	}
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
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
		String targetUrl = "http://aawap.net/aawap/search?searchName=qq";
		//模拟浏览器打开一个目标网址
		HtmlPage rootPage= webClient.getPage(targetUrl);
		System.out.println(rootPage.asXml());
	}
}
