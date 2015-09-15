package com.appCrawler.pagePro;
/*
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PagePro91_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class PagePro91 implements PageProcessor {
	private Logger logger = LoggerFactory.getLogger(PagePro91.class);
	Site site = Site.me().setCharset("utf-8")
			.setRetryTimes(PropertiesUtil.getRetryTimes())
			.setSleepTime(PropertiesUtil.getInterval());

	@Override
	public Apk process(Page page) {
		// index page
		// http://apk.91.com/soft/Android/search/3_5_0_0_%E4%BB%99%E5%89%91%E5%A5%87%E4%BE%A0%E4%BC%A0
		if (page.getUrl().regex("http://apk\\.91\\.com/soft/Android/search.*")
				.match()) {
			// app的具体介绍页面
			List<String> url1 = page.getHtml()
					.links("//ul[@class='search-list clearfix']")
					.regex("http://apk\\.91\\.com/Soft/Android.*").all();

			// 添加下一页url(翻页) 手动构造
			String currenturl = page.getUrl().toString();
			char pagenum = (char) currenturl.codePointAt(38);
			List<String> url2 = page.getHtml()
					.links("//div[@class='page_footer']").regex("这是不会匹配到的")
					.all();
			if (pagenum == 49)
				for (int i = 1; i < 1; i++) {
					currenturl = currenturl.substring(0, 38) + (++pagenum)
							+ currenturl.substring(39, currenturl.length());
					url2.add(currenturl);
				}

			url1.addAll(url2);

			// remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);

			// add the urls to page
			for (String temp : urlSet) {
				if (!temp.contains("http://apk.91.com/soft/Controller.ashx?Action=Download&id="))
					page.addTargetRequest(temp);
			}
		}

		// the app detail page
		if (page.getUrl().regex("http://apk\\.91\\.com/Soft/Android.*").match()) {

			return PagePro91_Detail.getApkDetail(page);

		}

		return null;

	}

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
*/