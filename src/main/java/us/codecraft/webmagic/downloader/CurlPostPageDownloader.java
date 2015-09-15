package us.codecraft.webmagic.downloader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.PlainText;

/**
 * 利用Linux的curl工具，下载页面，主要是针对宝瓶网的search请求是post方法提交的。
 * @author buildhappy
 *
 */
public class CurlPostPageDownloader implements Downloader{
	private Logger logger = LoggerFactory.getLogger(CurlPostPageDownloader.class);
	/**
	 * postPara：post提交的参数名称
	 * keyword：post提交的参数名称对应的值
	 */
	private String postPara;
	private String keyword;
	private String referer;
	private String pageEncoding;
	private String channelId;
	
	
	public CurlPostPageDownloader(String postPara , String keyword , String referer,String pageEncoding, String channelId ){
		
		this.postPara = postPara;
		this.keyword = keyword;
		this.referer = referer;
		this.pageEncoding = pageEncoding;
		this.channelId = channelId;
	}
	@Override
	public Page download(Request request, Task task) {
		logger.info("call for CurlPostPageDownloader download");
		
		Page page = new Page();
		String url = request.getUrl();
		String encoding = pageEncoding;//"utf-8";
		String commands=null;
		
		if(request.getMethod()!= null && request.getMethod().equals("post"))	
			{
			
			if(channelId.equals("30"))
				commands = generateCurlPostCommand30(url);//curl命令
			else if(channelId.equals("71"))
				commands = generateCurlPostCommand71(url);//curl命令
			else if(channelId.equals("51"))
				commands = generateCurlPostCommand51(url);//curl命令
			else 
				commands = generateCurlPostCommand(url);//curl命令
			}
		else 
			commands = generateCurlGetCommand(url);//curl命令	
		StringBuffer stringBuf = new StringBuffer();
		InputStream curlIn = null;
		BufferedReader bufReader = null;
		String context = null;
		try {
			Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", commands});
		 
			curlIn = new DataInputStream(process.getInputStream());
			bufReader = new BufferedReader(new InputStreamReader(curlIn , encoding));
			String line = null;
			while((line = bufReader.readLine()) != null){
				stringBuf.append(line);
			}
			context = new String(stringBuf);
			
		} catch (IOException e) {
			logger.info("CurlPostPageDownloader download failed" , e);
		}finally{
			try{
				if(curlIn != null){
					curlIn.close();
				}
				if(curlIn != null){
					curlIn.close();
				}
			}catch(Exception e){
				logger.info("release resource failed" , e);
			}
		}
		page.setRawText(context);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        //page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		return page;
	}

	
	@Override
	public void setThread(int threadNum) {
		
	}
	
	/**
	 * 构造curl命令
	 * @param url:搜索页面对于的url
	 * @return
	 */
	private String generateCurlPostCommand30(String url){		
		String commands = "curl  -L  \""+url+"\" "
				+"-H \"Accept-Encoding: gzip, deflate\" "
				+"-H \"Accept-Language: zh-CN,zh;q=0.8\" "
				+"-H \"User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36\" "
				+"-H \"Content-Type: application/x-www-form-urlencoded\" "
				+"-H \"Accept: textml,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\" "
				+"-H \"Cache-Control: max-age=0\" " 
				+"-H \"Connection: keep-alive\" "
				+"--data \"show=title&starttime=0000-00-00&endtime=0000-00-00&"+postPara+"="+keyword+"&classid=16&Submit22=\" "
				+"--compressed";

		System.out.println("in Post of curl and the commands is " + commands);		 
		return commands;
	}
	
	private String generateCurlPostCommand71(String url){		
		String commands = "curl  -L  \""+url+"\" "
				+"-H \"Accept: textml,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\" "
				+"-H \"Accept-Encoding: gzip, deflate\" "
				+"-H \"Accept-Language: zh-CN,zh;q=0.8\" "
				+"-H \"Cache-Control: max-age=0\" "
				+"-H \"Connection: keep-alive\" "
				+"-H \"Content-Type: application/x-www-form-urlencoded\" "		
				+"-H \"Host: www.apk8.com\" "
				+"-H \"Origin: http://www.apk8.com\" "
				+"-H \"Referer: http://www.apk8.com/search.php\" "
				+"-H \"User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36\" "				 				
			//	+"--Cookie \"PHPSESSID=3bqtisr8flhsltmm37ctlsi165\" "
			//	+"--Cookie \"pgv_pvi=6406356992\" "
			//	+"--Cookie \"pgv_si=s4511857664\" "
			//	+"--Cookie \"Hm_lvt_a4a7313778c6c28dec1ebc04635ffacb=1432002454\" "
			//	+"--Cookie \"Hm_lpvt_a4a7313778c6c28dec1ebc04635ffacb=1432004172\" "
				+"--data \""+postPara+"="+keyword+"\" "
				+"--compressed";

		System.out.println("in Post of curl and the commands is " + commands);		 
		return commands;
	}
	private String generateCurlPostCommand51(String url){		
		String commands = "curl  http://s.pc6.com/cse/search?s=12026392560237532321&entry=1&ie=gbk&q="+keyword;
//		       commands = "curl  http://s.pc6.com/cse/search?q=qq&entry=1&s=12026392560237532321&nsid=2&";
		                       
		System.out.println("in Post of curl and the commands is " + commands);		 
		return commands;
	}
	
	private String generateCurlPostCommand(String url){		
		String commands = "curl  -L  \""+url+"\" "
				+"-H \"Accept-Encoding: gzip, deflate\" "
				+"-H \"Accept-Language: zh-CN,zh;q=0.8\" "
				+"-H \"User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.76 Safari/537.36\" "
				+"-H \"Content-Type: application/x-www-form-urlencoded\" "
				+"-H \"Accept: textml,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\" "
				+"-H \"Cache-Control: max-age=0\" " 
				+"-H \"Connection: keep-alive\" "
				+"--data \"show=title&starttime=0000-00-00&endtime=0000-00-00&"+postPara+"="+keyword+"&classid=16&Submit22=\" "
				+"--compressed";

		System.out.println("in Post of curl and the commands is " + commands);		 
		return commands;
	}
	/**
	 * 构造curl命令
	 * @param url:搜索页面对于的url
	 * @return commands:下载相应get请求页面的命令
	 */
	private String generateCurlGetCommand(String url) {
		// TODO Auto-generated method stub
		String commands = "curl " + " " + url;	
//		System.out.println("in Gets of curl and the commands is " + commands);
	
		return commands;
	
	}
	
	public static void main(String[] args) {

	}

}
