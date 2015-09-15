package com.appCrawler.pagePro;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.appCrawler.pagePro.apkDetails.Aawap_Detail;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 酷快市场 http://www.aawap.net/
 * Aawap #117
 * @author DMT
 * (1)动态页面 完整的搜索结果需要动态获取
 */
public class Aawap implements PageProcessor{
	Site site = Site.me().setCharset("utf-8").setRetryTimes(0).setSleepTime(300);

	// 日志管理对象
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Aawap.class);

	@Override
	public Apk process(Page page) {
		//index page		http://aawap.net/aawap/search?searchName=%E6%96%97%E5%9C%B0%E4%B8%BB
		if (page.getUrl().regex("http://aawap\\.net/aawap/search.*").match()) {
			LOGGER.info("match success, url:{}", page.getUrl());
			String keyWord = StringUtils.substringAfter(page.getUrl().get(), "searchName=");

			// 获取url
			String html = StringUtils.substringBetween(page.getRawText(), "loadList(", ");");
			String[] infos = html.split(",");

			String url = url = new StringBuilder()
                        .append(infos[0].replaceAll("\"", "")).append("/")
                        .append(infos[1].replaceAll("\"", "")).append("/")
                        .append(infos[6]).append("/")
                        .append(infos[5]).append("/").append("?searchName=").append(URLEncoder.encode(unescape(keyWord)))
					.append("&callback=jQuery164048770366655662656_1432529033078&_=").append(System.currentTimeMillis()).toString();

			page.addTargetRequest(url);

			// 打印搜索结果url
			LOGGER.info("app info results urls: {}", page.getTargetRequests());
		}

		//get the app detail page
		if(page.getUrl().regex("http://market\\.kknet\\.com\\.cn\\:8080/app/searchApp/*").match()){
			JSONObject json = JSON.parseObject(StringUtils.substringBetween(page.getRawText(), "(", ");").replaceAll("&quot;", "\""));
			JSONArray apkArray = json.getJSONArray("applist");

			for (int i = 0; i < apkArray.size(); i++) {
				JSONObject apk = apkArray.getJSONObject(i);
				page.addTargetRequest("http://aawap.net/aawap/detail/" + apk.getString("appId"));
			}
		}
		
		if(page.getUrl().regex("http://aawap\\.net/aawap/detail/*").match()){//http://www\\.ruan8\\.com/soft.*
//			Apk apk = null;
//			String appName = null;				//app名字
//			String appDetailUrl = null;			//具体页面url
//			String appDownloadUrl = null;		//app下载地址
//			String osPlatform = null ;			//运行平台
//			String appVersion = null;			//app版本
//			String appSize = null;				//app大小
//			String appUpdateDate = null;		//更新日期
//			String appType = null;				//下载的文件类型 apk？zip？rar？ipa?
//			String appvender = null;			//app开发者  APK这个类中尚未添加
//			String appDownloadedTime=null;		//app的下载次数
//			String description = null;
//			appName = page.getHtml().xpath("//div[@class='appcont']/h3/text()").toString();	
//				
//			appDetailUrl = page.getUrl().toString();
//			
//			appDownloadUrl = page.getHtml().xpath("//p[@class='oper']/a/@href").toString();
//			appSize = page.getHtml().xpath("//div[@class='appcont']/h5/text()").toString();	
//			description = page.getHtml().xpath("//div[@class='maincontent']/text()").toString();
//			List<String> info = page.getHtml().xpath("//div[@class='version']/p/text()").all();
//
//			if(info.size() > 1){
//				appVersion = info.get(1).split("：").length > 1 ? info.get(1).split("：")[1] : null;
//			}
//			if(info.size() > 2){
//				osPlatform = info.get(2).split("：").length > 1 ? info.get(2).split("：")[1] : null;
//			}			
//		
//			if(appName != null && appDownloadUrl != null){
//				apk = new Apk(appName,appDetailUrl,appDownloadUrl,osPlatform ,appVersion,appSize,appUpdateDate,appType,null);
//				apk.setOsPlatform(osPlatform);
//				apk.setAppDescription(description);
//			}
//			 LOGGER.info("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downloadNum:{}, appDesc:{}", 
//					 appName, appVersion, appDownloadUrl, appSize, appType, osPlatform, appUpdateDate, appDownloadedTime, description);
			return Aawap_Detail.getApkDetail(page);
		}
		return null;
	}

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public List<Apk> processMulti(Page page) {
		//index page		http://aawap.net/aawap/search?searchName=%E6%96%97%E5%9C%B0%E4%B8%BB
		/*
		if (page.getUrl().regex("http://aawap\\.net/aawap/search.*").match()) {
			LOGGER.info("match success, url:{}", page.getUrl());

			String keyWord = StringUtils.substringAfter(page.getUrl().get(), "searchName=");

			// 获取url
			String html = StringUtils.substringBetween(page.getRawText(), "loadList(", ");");
			String[] infos = html.split(",");

			String url = url = new StringBuilder()
                        .append(infos[0].replaceAll("\"", "")).append("/")
                        .append(infos[1].replaceAll("\"", "")).append("/")
                        .append(infos[6]).append("/")
                        .append(infos[5]).append("/").append("?searchName=").append(URLEncoder.encode(unescape(keyWord)))
					.append("&callback=jQuery164048770366655662656_1432529033078&_=").append(System.currentTimeMillis()).toString();

			page.addTargetRequest(url);

			// 打印搜索结果url
			LOGGER.info("app info results urls: {}", page.getTargetRequests());
		}

		List<Apk> apkList = Lists.newArrayList();

		//the app detail page
		if(page.getUrl().regex("http://market\\.kknet\\.com\\.cn\\:8080/app/searchApp/*").match()){
			JSONObject json = JSON.parseObject(StringUtils.substringBetween(page.getRawText(), "(", ");").replaceAll("&quot;", "\""));
			JSONArray apkArray = json.getJSONArray("applist");

			for (int i = 0; i < apkArray.size(); i++) {
				JSONObject apk = apkArray.getJSONObject(i);
				apkList.add(new Apk(
						apk.getString("appName"),
						"http://aawap.net/aawap/detail/" + apk.getString("appId"),
						"http://market.kknet.com.cn/resources/upload/" + apk.getString("apkDownloadUrl"),
						null,
						null,
						apk.getString("apkSize")
				));
				page.addTargetRequest("http://aawap.net/aawap/detail/" + apk.getString("appId"));
			}
		}
		 */
		// 打印搜索结果url
		//LOGGER.info("app results urls: {}", apkList);
		//return apkList;
		return null;
	}

	/**
	 * 对应javascript的unescape()函数, 可对javascript的escape()进行解码
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				}
				else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			}
			else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				}
				else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 对应javascript的escape()函数, 加码后的串可直接使用javascript的unescape()进行解码
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			}
			else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
}
