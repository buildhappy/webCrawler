package com.appCrawler.pagePro.fullstack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.pagePro.apkDetails.PagePro360_Detail;
import com.appCrawler.utils.PropertiesUtil;
import com.google.common.collect.Sets;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class PagePro360 implements PageProcessor{
	//Site site = Site.me().setCharset("utf-8").setRetryTimes(2).setSleepTime(0);
	Site site = Site.me().setCharset("utf-8").setRetryTimes(PropertiesUtil.getRetryTimes()).
			setSleepTime(PropertiesUtil.getInterval());

	private Logger LOGGER = LoggerFactory.getLogger(PagePro360.class);

	public Apk process(Page page) {
		List<String> urls =page.getHtml().links().regex("(http://zhushou\\.360\\.cn/.*)").all() ;

		Set<String> cacheSet = Sets.newHashSet();
		cacheSet.addAll(urls);
		
		//构造分页
		//http://zhushou.360.cn/list/index/cid/1
		//if(page.getUrl().regex("(http://zhushou\\.360\\.cn/detail/list/index/.*)").match()){
		if(page.getRequest().getUrl().equals("http://zhushou.360.cn/list/index/cid/1")||
				page.getRequest().getUrl().equals("http://zhushou.360.cn/list/index/cid/2")){
			String pageStr=page.getHtml().regex("(pg\\.pageCount\\s=\\s\\w+)").toString();
			int pageCount=Integer.parseInt(pageStr.substring(15));
			List<String> url1=new ArrayList<String>();
			for(int i= 2;i<=pageCount;i++){
				url1.add(page.getRequest().getUrl()+"?page="+i);
			}
			
			page.addTargetRequests(url1);
		}
		//剔除锚点.*?#.*
		//#expand,#next,#prev,#comment,#nogo,#guess-like,#btn-install-now-log,#comment-list,#report
		for(String url:cacheSet){
			if(url.toString().endsWith("#expand")||url.toString().endsWith("#next") || url.toString().endsWith("#prev")
					||url.toString().endsWith("#comment")||url.toString().endsWith("#nogo") || url.toString().endsWith("#guess-like")
					||url.toString().endsWith("#btn-install-now-log") || url.toString().endsWith("#comment-list")
					|| url.toString().endsWith("#report")){
				LOGGER.error("anchor:" + url.toString());
			}else{
				LOGGER.info(url.toString());
				page.addTargetRequest(url);
			}
		}
		
		//提取页面信息
		if(page.getUrl().regex("(http://zhushou\\.360\\.cn/detail/index/soft_id/.*)").match()){
			Html html = page.getHtml();
			Apk apk = PagePro360_Detail.getApkDetail(page);
			
			page.putField("apk", apk);
			if(page.getResultItems().get("apk") == null){
				page.setSkip(true);
			}
		}else{
			page.setSkip(true);
		}
		return null;
	}

	public static void main(String[] args){
		String url = "http://zhushou.360.cn/list/index/cid/1?page=24#expand";
//		if(url.endsWith("#expand||#next||#prev||#comment||#nogo||#guess-like||#btn-install-now-log")){
		if(url.endsWith("#expand || #next")){
			System.out.println("true");
		}
	}

	@Override
	public List<Apk> processMulti(Page page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Site getSite() {
		return site;
	}
}
