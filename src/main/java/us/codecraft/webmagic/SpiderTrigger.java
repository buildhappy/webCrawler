package us.codecraft.webmagic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.downloader.CurlPostPageDownloader;
import us.codecraft.webmagic.downloader.MyHttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 爬虫的启动接口，controller模块接受任务后，调用SpiderTrigger启动爬虫
 * @author buildhappy
 *
 */
public class SpiderTrigger implements Runnable {
	private String channelId;
	private String taskId;
	private String keyword;
	private String remoteIp;
	private Logger logger = LoggerFactory.getLogger(getClass());
	static{
        System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("http.proxyPort", "8001");
        System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("https.proxyPort", "8001");
		//threadPool = new CountableThreadPool(threadNum);
	}
	/**
	 * 
	 * @param taskId
	 * @param channelId
	 * @param keyword
	 */
	public SpiderTrigger(String taskId , String channelId , String keyword ,String remoteAddress){
		logger.info("SpiderTrigger Constructor parameter: taskId,channelId,keyword");
		this.taskId = taskId;
		this.channelId = channelId;
		this.keyword = keyword;
		this.remoteIp = remoteAddress;
	}
	
	public SpiderTrigger(String taskId , UserRequest requestData , String remoteAddress){
		logger.info("SpiderTrigger Constructor parameter: taskId,channelId,keyword");
		this.taskId = taskId;
		this.channelId = requestData.getChannelId();
		this.keyword = requestData.getKeyword();
		this.remoteIp = remoteAddress;
	}
	
	public void run() {
		logger.info("SpiderTrigger run");
		ChannelsDom channelDom = new ChannelsDom();
		channelDom = channelDom.createChannelsDom(channelId);
		
		if(channelDom != null){
			String referer;
			referer = channelDom.getHomeUrl();
			String pageEncoding;
			pageEncoding = channelDom.getPageEncoding();
			
			//System.out.println(keyword != null && keyword.length() > 0 && keyword != "");
			//searcher request using the keyword
			if(keyword != null && keyword.length() > 0 && keyword != ""){
				logger.info("searcher request using the keyword");
				String searchUrl;
				PageProcessor pagePro;
				String postParam;
				pagePro = channelDom.getPagePro();
				
				String s = channelDom.getSearchUrl();
				searchUrl = s.replace("*#*#*#", keyword);
				
				String urlEncoding = null;
				urlEncoding = channelDom.getUrlEncoding();
				
				String encodedKeyword=keyword;
				if(urlEncoding != null){
					try {
						encodedKeyword = URLEncoder.encode(keyword, urlEncoding);
					} catch (UnsupportedEncodingException e) {
						logger.error(e.toString());
						//e.printStackTrace();
					}
				}

				searchUrl = s.replace("*#*#*#", encodedKeyword);
				logger.info(searchUrl);
				//searchUrl = searchUrl + keyword;
				
				postParam = channelDom.getPostParam();
				Spider spider = new Spider(pagePro ,taskId ,channelId,remoteIp);
				System.out.println("postParam:" + postParam);
				if (postParam != null) {
					Map<String,Object> extras = new HashMap<String , Object>();
					extras.put(postParam, keyword);
					Request request = new Request();
					request.setExtras(extras);
					request.setMethod("post");
					request.setUrl(searchUrl);
					spider.addRequest(request).setDownloader(new CurlPostPageDownloader(postParam, keyword , referer ,pageEncoding,channelId))
							.thread(1).run();
//					spider.setDownloader(new MyHttpClientDownloader())
//					.addRequest(request).run();
				} else {
					spider.setDownloader(new MyHttpClientDownloader())
							.addUrl(searchUrl).thread(1).run();
				}				
			}//the full stack crawler
			else{
				logger.info("full stack crawler");
				String homeUrl = channelDom.getHomeUrl();
				String[] homeUrls = homeUrl.split(",");
				PageProcessor fullPagePro = channelDom.getFullPagePro();
				
				FullStackSpider fullStackSpider = new FullStackSpider(fullPagePro , taskId , channelId , remoteIp);
				fullStackSpider.setDownloader(new MyHttpClientDownloader()).addUrl(homeUrls).addPipeline(new FilePipeline(taskId , channelId)).thread(1).run();
			}
		}else{
			//if can not find the channel by the channelId
			logger.error("SpiderTrigger no channel");
		}
	}
	
	public static void main(String[] args) throws IOException{

		//dongjinkui:
		//Done:66->ApkGfan 69->Ruan8 71->Apk8 72->Anzhuo 73->Zhuannet 74->Gamersky 
		//78->Mobyware 80->Srui 84->Duote 85->Mi 87->Anfone 91->Anfensi 92->Hzhuti 88->Meizumi
		//138->Ddxia 142->Skycn 153->Appdh 
		//107->Apk3 135->Www7xz 173->Eoemarket 119->Mgapp 136->Bkill 128->Oyksoft
		//105->UC 77->Leidian 169->Android173Sy 132->Downbank 133->Paojiao
		//106->Muzhiwan 130->Liqucn 127->Anzhi 109->Www2265 173->Eoemarket 67->Fpwap
		//150->PageProDowng 145>PageProSoapp(仅一个应用) 152->PageProWabao(仅两个应用)
		//110->Www25az 86->Nduoa 96->Play 97->www5577
		//99->Zol 116->Newasp 125->Yruan 121->Www51vapp
		//123->Www77l 98->Sina 174->Www9game 4->PagePro159(共10个应用)
		//81->Itopdog 172->PipaPagePro 134->D
		//111->Vsoyou(获取的apk总量不全，应该自己伪造apk详情页链接) (以上共64个) 
		
		//去重：
		//75->Pcpop(与144重复,保留75) 44->PageProHuawei(与124重复，保留44) 71->Sjapk(与158重复，保留71) 
		//93->Sjapk(与149重复) 81->Itopdog(与155重复) 
		
		//todo:103->Diyiapp(暂时不能用)
		
		//Error:
		//171->oppo(被封) 76->Gezila(网站挂了) 90->Apk91(被封)
		
		
		//liuxixia:
		//Done:2->PagePro163 50->PagePro265g 25->PagePro155 168->360
		//44->PageProHuawei 35->PagePro7613 63->PageProAouu(仅一个应用) 19->PageProApkyx
		//62->PageProandroidcnmo 21->PageProBaidu 64->PageProBaoRuan(仅一个应用) 37->PageProAnZhuoApk 
		//55->PageProAnqu 46->PageProHiApk 146->PageProIfan178 42->PagePro7xz 161->PageProImobile
		//33->PageProCe1111 22->PageProgfanstore 1->PagePro3533 65->PagePro4355
		//29->PageProSoGou 9->PageProTgbus 52->PageProKaiqi  16->PagePro3987(共12个应用) 
		//34->PageProSlideMe 32->PagePro7230 47->PageProAnruan 58->PagePro97sky
		//160->PageProShuaJi 59->PageProMyapp 14->PageProShouYou 24->PageProPconline 
		//20->PageProLenovomm 156->PagePro17et(仅一个应用) 17->PageProKaixin
		//31->PageProCr173(以上共40个)
		//8->PageProMobilePhone 60->PageProApkye 51->PageProPc6 
		//162->PageProNgDCn 12->PageProGameDog
		//27->PagePro520apk 53->PageProMz6 57->PagePro5253
		//10->PageProAnzow
		
		//去重：
		//148->PageProYesky(与163重复) 36->PagePro958Shop(与166重复) 
		
		//Error:15->PagePro91(被封)，40->PageProWandoujia(一次爬虫很久不能用，但搜索接口可用)
		//5->PageProAppChina(疑似被封,80个应用后页面下载不下了,浏览器打不开)
		//13->PageProCncrk(下载链接有问题下载是exe文件)
		//38->PageProShuiGuo(被封，获取的下载链接有问题，在利用动态页面处理后真正的下载链接还是无法获取) 
		//18->PageProDownza(下载链接全是exe文件)
		//7->PageProSoHu(只有首页存在，没有apk应用) 41->PageProAngeeks(网站挂掉)
		//49->PageProSinaTech(下载的url是根据请求的url动态生成的，无法获取)
		//30->PageProAnGuo(apk文件保存在百度网盘)
		//23->PageProOnlineDown(速度慢，无用链接太多且无法剔除)
		
		//Running:
		//6->PageProMM10086(速度慢，无用链接太多且无法剔除)
		
		
		
		SpiderTrigger spiderTrigger = new SpiderTrigger("task1" , "41" , "" , "");//工商银行  捕鱼达人 邮政银行
		Thread thread = new Thread(spiderTrigger);
		thread.run();
		
		//提取sitesInfo.xml中的数据
		/*
		File file = new File("data.txt");
		FileWriter writer = new FileWriter(file);
		int counter = 0;
		for(int i = 1; i < 180; i++){
			ChannelsDom channelDom = new ChannelsDom();
			try{
				channelDom = channelDom.createChannelsDom(i + "");
				if(channelDom != null){
					String name;
					name = channelDom.getName();
					String referer;
					referer = channelDom.getHomeUrl();
					String id = channelDom.getChannelId();
					String pagePro = channelDom.getFullPagePro().toString();
					//System.out.println(i + ":" + name + ":" + referer);
					writer.write(id + ":" + name + ":" + referer + "\n");
					writer.flush();
					counter++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
		writer.close();
		System.out.println(counter);
		*/
	}
}


