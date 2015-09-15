package us.codecraft.webmagic.downloader;


import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appCrawler.utils.MyNicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 模拟浏览器打开url获取解析过javascript以后的html，返回page
 * @author DMT
 *
 */


public class WebClientDynamicDownloader implements Downloader{
	private Logger logger = LoggerFactory.getLogger(WebClientDynamicDownloader.class);
	
	public static void main(String[] args) {
		WebClientDynamicDownloader exampleClientDynamicDownloader = new WebClientDynamicDownloader();
		Request request = new Request("http://www.diyiapp.com/search/?keyword=qq&type=game");
		Task task = null;
		exampleClientDynamicDownloader.download(request, task);
	}
	@Override
	public Page download(Request request, Task task) {
		logger.info("call for WebClientDynamicDownloader download");
		Page page = new Page();
		String url = request.getUrl();
		
		MyNicelyResynchronizingAjaxController ajaxController = new MyNicelyResynchronizingAjaxController();
		
		//模拟一个浏览器
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		//HtmlUnitDriver
		//设置webClient的相关参数
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(ajaxController);
		webClient.getOptions().setTimeout(35000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		
		//模拟浏览器打开一个目标网址
		HtmlPage rootPage;
		try {
			rootPage = webClient.getPage(url);
			String context = rootPage.asXml().toString();
			page.setRawText(context);
	        page.setUrl(new PlainText(request.getUrl()));
	        page.setRequest(request);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
		System.out.println(page.getHtml().toString());
		return page;
	}
	@Override
	public void setThread(int threadNum) {
		// TODO Auto-generated method stub
		
	}
}
