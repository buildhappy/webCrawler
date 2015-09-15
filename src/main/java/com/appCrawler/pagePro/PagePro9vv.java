package com.appCrawler.pagePro;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 微游中国  http://www.9vv.com/
 * 页面分四种类型，分别进行判断和页面信息提取
 * 其中一种是直接有下载链接的，其他的需要自己构造ajaxurl请求json数据
 * @author Administrator
 *
 */
public class PagePro9vv implements PageProcessor {

	Site site = Site.me().setCharset("utf-8").setRetryTimes(3)
			.setSleepTime(2000);

	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	public Apk process(Page page) {
		Apk apk = null;
		if(page.getUrl().regex("http://www\\.9vv\\.com/search/*").match()){
			//app的具体介绍页面											
			List<String> url1 = page.getHtml().links("//div[@class='list_content']").all();

			//添加下一页url(翻页)
			List<String> url2 = page.getHtml().links("//ul[@class='yiiPager']").all();
			
			url1.addAll(url2);
			
			//remove the duplicate urls in list
			HashSet<String> urlSet = new HashSet<String>(url1);
			
			//add the urls to page
			Iterator<String> it = urlSet.iterator();
			while(it.hasNext()){
				page.addTargetRequest(it.next());
				
			}
		}
		String appName = page.getHtml().xpath("//div[@class='soft_info_middle']/h3/text()").toString();
		
		//the app detail page
		boolean flag = false;

		String name2 = page.getHtml().xpath("//div[@class='txt']/h3/a/text()")
				.toString();// 对应农场3僵尸入侵这种
		if (name2 != null
				&& page.getUrl().regex("http://www\\.9vv\\.com/detail\\?.*")
						.match()) {// http://www.9vv.com/detail?gameID=1400170

			String downloadUrl = page.getHtml()
					.xpath("//div[@class='btn']/a/@href").toString();


		}
		String name1 = page.getHtml()
				.xpath("//div[@class='infor border']/h2/text()").toString();// 对应怒斩轩辕2官网下载这种
		if (!flag&&name1 != null
				&& page.getUrl().regex("http://www\\.9vv\\.com/detail\\?.*")
						.match()) {// http://www.9vv.com/detail?gameID=1400150
			String version = page.getHtml()
					.xpath("//div[@class='infor border']/ul/li[6]/text()")
					.toString();
			String apkId = page.getUrl().toString().split("gameID=").length > 0 ? page
					.getUrl().toString().split("gameID=")[1]
					: null;
			String downloadUrl = null;
			String name = null;
			String os = null;
			if (apkId != null) {
				Map<String, String> infoMap = getJson("http://szr.9vv.com/ajaxJson/index!download?gameID="
						+ apkId);
				downloadUrl = infoMap.get("downloadUrl");
				name = infoMap.get("name");
				version = infoMap.get("version");
			}
			os = page.getHtml()
					.xpath("//div[@class='infor border']/ul/li[2]/text()")
					.toString();
			flag = true;
		}

		String name3 = page.getHtml().xpath("//div[@class='bg']/a/text()")
				.toString();// http://www.9vv.com/special/wjmt

		if (!flag && name3 != null
				&& page.getUrl().regex("http://www\\.9vv\\.com/special/.*")
						.match()) {// http://www.9vv.com/special/wjmt
			String apkId = page.getHtml()
					.xpath("//div[@class='nav']/span/a/@onclick").toString()
					.split("xiazai")[1];
			apkId = apkId.replace("(", "");
			apkId = apkId.replace(")", "");
			String downloadUrl = null;
			String name = null;
			String version = null;
			String os = null;
			if (apkId != null) {
				Map<String, String> infoMap = getJson("http://szr.9vv.com/ajaxJson/index!download?gameID="
						+ apkId);
				downloadUrl = infoMap.get("downloadUrl");
				name = infoMap.get("name");
				version = infoMap.get("version");
			}
			flag = true;
		}
		//String pageUrl = page.getUrl().toString();
		if(!flag && page.getUrl().toString().matches("http://[a-zA-Z]+.9vv.com/") && !page.getUrl().toString().equals("http://www.9vv.com/")){// http://szr.9vv.com/
			String apkId = page.getHtml()
					.xpath("//div[@class='main_nav_c']/a/@onclick").toString();
			apkId = apkId.replace("(", "");
			apkId = apkId.replace(")", "");
			String downloadUrl = null;
			String name = null;
			String version = null;
			String os = null;
			Pattern pattern = Pattern.compile("[0-9]+");
			if (apkId != null && pattern.matcher(apkId).matches()) {
				Map<String, String> infoMap = getJson("http://szr.9vv.com/ajaxJson/index!download?gameID="
						+ apkId);
				downloadUrl = infoMap.get("downloadUrl");
				name = infoMap.get("name");
				version = infoMap.get("version");
			}
			return apk;
		}
		return apk;
	}

	// 通过ajaxUrl获取json数据函数
	public Map<String, String> getJson(String ajaxUrl) {
		/*
		HttpClientLib httpClient = new HttpClientLib();
		String jsonStr = httpClient.getUrlRespHtml(ajaxUrl);
		jsonStr = jsonStr.replace("[", "");
		jsonStr = jsonStr.replace("]", "");
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		*/
		/*
		 * [ - { g_version: "1.19.14.2705", g_name: "神之刃", update_time:
		 * 1416550213372, gameType: 1, download_url:
		 * "http://download.9vv.com/9vvapk/shenzhiren.apk", g_type: 1, g_pic1:
		 * "http://img.9vv.com/gameicon/20140330/58051396165938950.jpg", score:
		 * 3, g_file: 45, g_id: 1400152, g_pic3:
		 * "http://img.9vv.com/gameicon/20140330/10621396165938951.jpg", g_pic2:
		 * "http://img.9vv.com/gameicon/20140330/36251396165938950.jpg" } ]
		 */
		/*
		Map<String, String> infoMap = new HashMap<String, String>();
		String version = null;
		String downloadUrl = null;
		String name = null;
		String iconUrl = null;
		try {
			downloadUrl = jsonObj.getString("download_url");
			name = jsonObj.getString("g_name");
			version = jsonObj.getString("g_version");
			iconUrl = jsonObj.getString("g_pic1");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			infoMap.put("name", name);
			infoMap.put("downloadUrl", downloadUrl);
			infoMap.put("version", version);
			infoMap.put("iconUrl", iconUrl);
		}
		
		return infoMap;
		*/
		return null;
	}

	public static void main(String[] args) {
		PagePro9vv page = new PagePro9vv();
		page.getJson("http://szr.9vv.com/ajaxJson/index!download?gameID=1400152");

		String url = "http://szr.9vv.com/fa";
		String apkId = "4545";
		Pattern pattern = Pattern.compile("http://[a-zA-Z]+.9vv.com/");
		//System.out.println(pattern.matcher(url).matches());
		System.out.println(url.matches("http://[a-zA-Z]+.9vv.com/"));
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
}