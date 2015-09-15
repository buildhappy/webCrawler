package us.codecraft.webmagic;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import us.codecraft.webmagic.processor.PageProcessor;

/**
 * package the chanel info into a class
 * using the class to load channel info from siteInfo.xml
 * usage:
 * 	provide the channel id,return the searchUrl and PageProcessor of the channel
 * @author buildhappy
 *
 */
@Component
public class ChannelsDom {
	private String channelId;
	private String name;
	private String searchUrl;
	private PageProcessor pagePro;
	private String postParam = null;
	private String homeUrl = null;
	private String pageEncoding = null;
	private String urlEncoding = null;
	private PageProcessor fullPagePro;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ChannelsDom(){
		logger.info("ChannelsDom no-parameter constructor");
	}
	public ChannelsDom(String channelId){
		logger.info("ChannelsDom constructor with parameter channelId");
		this.channelId = channelId;
		load();
	}
	public ChannelsDom createChannelsDom(String channelId){
		logger.info("ChannelsDom createChannelsDom(String channelId)");
		this.channelId = channelId;
		//if there is no the channel dom
		if(load() == 1){
			return null;
		}else{
			return new ChannelsDom(channelId);
		}
	}
	public int load(){
		logger.info("ChannelsDom load()");
		try {
			//get websites informations for xml file
			DocumentBuilderFactory dbf = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();

			InputStream in = ChannelsDom.class.getClassLoader()
					.getResourceAsStream("sitesInfo.xml");

			// convert the xml file to dom tree
			Document doc = builder.parse(in);
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			// get node informations by xpath tool
			XPathExpression exp = xPath.compile("//site[@id='" + channelId
					+ "']/searchUrl");
			Node node = (Node) exp.evaluate(doc, XPathConstants.NODE);
			if(node != null){
				String s = node.getTextContent();
				setSearchUrl(s.replace("!%!%!%", "&"));
				exp= xPath.compile("//site[@id='" + channelId
						+ "']/urlEncoding");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				if(node != null){
					urlEncoding = node.getTextContent();
				}else{
					urlEncoding = null;
				}
				
				exp = xPath.compile("//site[@id='" + channelId
						+ "']/name");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				if(node != null){
					name = node.getTextContent();
				}
				
				
				exp = xPath.compile("//site[@id='" + channelId
						+ "']/pageEncoding");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				if(node != null){
					pageEncoding = node.getTextContent();
				}else{
					pageEncoding = "utf-8";
				}
				
				exp = xPath.compile("//site[@id='" + channelId
						+ "']/homePage");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				homeUrl = node.getTextContent();
				
				exp = xPath.compile("//site[@id='" + channelId + "']/pageProcessor");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				String pageProName = node.getTextContent();
				pagePro = dynamicCreateObjByName(pageProName);
				
				String fullPageProName = node.getTextContent();
				fullPageProName = pageProName.replace("com.appCrawler.pagePro.", "com.appCrawler.pagePro.fullstack.");
				try{
					fullPagePro = dynamicCreateObjByName(fullPageProName);
				}catch(Exception e){
					logger.info("not full crawler page processor");
					e.printStackTrace();
				}

				exp = xPath.compile("//site[@id='" + channelId + "']/postPara");
				node = (Node) exp.evaluate(doc, XPathConstants.NODE);
				if (node != null) {
					// 处理post请求
					postParam = node.getTextContent();
//					Spider.create(pagePro)
//							.setDownloader(new CurlDownloader(postParam, keyword))
//							.addUrl(seedUrl).thread(1).run();
				}
				return 0;
			}else{
				logger.warn("could not find the channel for channelId=" + channelId);
				return 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 通过类名创建类的对象，
	 * 
	 * @param name:为类的完整名，包括包名
	 * @return
	 * @throws Exception
	 */
	private PageProcessor dynamicCreateObjByName(String name)
			throws Exception {
		Class c;
		PageProcessor o = null;
		try{
			c = Class.forName(name);
			o = (PageProcessor) (c.getClassLoader().loadClass(name)).newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		return o;

	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	public PageProcessor getPagePro() {
		return pagePro;
	}
	public void setPagePro(PageProcessor pagePro) {
		this.pagePro = pagePro;
	}
	public String getPostParam() {
		return postParam;
	}
	public void setPostParam(String postParam) {
		this.postParam = postParam;
	}

	public String getHomeUrl() {
		return homeUrl;
	}
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	
	
	public String getPageEncoding() {
		return pageEncoding;
	}
	public void setPageEncoding(String pageEncoding) {
		this.pageEncoding = pageEncoding;
	}
	public String getUrlEncoding() {
		return urlEncoding;
	}
	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}
	
	public static void main(String[] args){
		ChannelsDom dom = new ChannelsDom("1");//load the channel with id=1
		System.out.println("channel id:" + dom.getChannelId());
		System.out.println("serch url:" + dom.getSearchUrl());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PageProcessor getFullPagePro() {
		return fullPagePro;
	}
	public void setFullPagePro(PageProcessor fullPagePro) {
		this.fullPagePro = fullPagePro;
	}
}
