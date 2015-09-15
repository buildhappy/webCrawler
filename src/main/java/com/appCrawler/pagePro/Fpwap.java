package com.appCrawler.pagePro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;








import com.appCrawler.pagePro.apkDetails.Fpwap_Detail;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 飞鹏网http://www.fpwap.com/
 * Fpwap.java  #67
 * 1.搜索页面有两个 ：
 * （1）http://www.fpwap.com/search/index.php?keyword=*#*#*#&platform_id=9; //这个是搜网游
 * （2）http://www.fpwap.com/search/index.php?keyword=*#*#*#&platform_id=3; //这个是搜游戏
 * 2.下载次数时动态获取的
 * @author DMT
 *
 */
public class Fpwap implements PageProcessor{
	Site site =Site.me().setCharset("gb2312").setRetryTimes(3).setSleepTime(2);
	@Override
	public Apk process(Page page) {
		//index page
		if(page.getUrl().regex("http://www\\.fpwap\\.com/search/index.php\\?*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//ul[@class='search-result-list width90']").regex("http://www\\.fpwap\\.com/.*").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//div[@class='page-change']").regex("http://www\\.fpwap\\.com/search/index.php\\?*").all();
			url1.addAll(url2);
	
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
			}
		}
		
		//the app detail page
		else	if(page.getUrl().regex("http://www\\.fpwap\\.com/*").match()){
			
			return Fpwap_Detail.getApkDetail(page);
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
