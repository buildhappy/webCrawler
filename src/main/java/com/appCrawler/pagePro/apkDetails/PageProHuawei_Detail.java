package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 华为智汇云[中国] app搜索抓取
 * url:http://appstore.huawei.com/search/MT
 *
 * @version 1.0.0
 */
public class PageProHuawei_Detail {

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PageProHuawei_Detail.class);

	public static Apk getApkDetail(Page page) {
		Apk apk = null;
		String appName = null; // app名字
		String appDetailUrl = null; // 具体页面url
		String appDownloadUrl = null; // app下载地址
		String osPlatform = null; // 运行平台
		String appVersion = null; // app版本
		String appSize = null; // app大小
		String appUpdateDate = null; // 更新日期
		String appType = null; // 下载的文件类型 apk？zip？rar？
		String appVenderName = null; // app开发者 APK这个类中尚未添加
		String appDownloadedTime = null; // app的下载次数
		String appDescription = null; // app的详细描述
		List<String> appScrenshot = null; // app的屏幕截图
		String appTag = null; // app的应用标签
		String appCategory = null; // app的应用类别
		
		
		
		 Html html = page.getHtml();
		   appDetailUrl = page.getUrl().toString();
           appName = html.xpath("//div[@class='app-info flt']/ul[@class='app-info-ul nofloat'][1]/li[2]/p/span[@class='title']/text()").toString();
           appVersion = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[4]/span/text()").get();
           appDownloadUrl = html.xpath("//div[@class='app-function nofloat']/a/@dlurl").toString();
           osPlatform = null;
           appSize = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[1]/span/text()").get();
           appUpdateDate = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[2]/span/text()").get();
           appDownloadedTime = StringUtils.substringAfter(html.xpath("//div[@class='app-info flt']/ul[@class='app-info-ul nofloat']/li[2]/p/span[@class='grey sub']/text()").toString(), "：");
           appType = null;
           appVenderName = html.xpath("//div[@class='app-info flt']/ul[2]/li[3]/span/text()").toString();
           appDescription = html.xpath("//div[@id='app_strdesc']/text()").toString();
           appScrenshot = html.xpath("//ul[@class='imgul']//img/@src").all();

		if (appName != null && appDownloadUrl != null) {
			apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform,
					appVersion, appSize, appUpdateDate, appType, null);
			// Apk(String appName,String appMetaUrl,String appDownloadUrl,String
			// osPlatform ,
			// String appVersion,String appSize,String appTsChannel, String
			// appType,String cookie){
			apk.setAppDownloadTimes(appDownloadedTime);
			apk.setAppVenderName(appVenderName);
			apk.setAppTag(appTag);
			apk.setAppScreenshot(appScrenshot);
			apk.setAppDescription(appDescription);
			apk.setAppCategory(appCategory);

		} else
			return null;

		return apk;
	}
}
