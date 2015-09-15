package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 刷机网  http://www.shuaji.net/
 * 编号160
 * 下载链接位于其他页面，需要一个缓存容器来暂时存储apk信息
 * @author buildhappy
 *
 */
public class PageProShuaJi_Detail {


    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProShuaJi_Detail.class);

    private final static Map<String , Apk> APKS = new ConcurrentHashMap<String , Apk>();

    public static Apk getApkDetail(Page page){

        //the app detail page
        if(page.getUrl().regex("http://www\\.shuaji\\.net/.*").match()){

            //下载页面
            if(page.getUrl().regex("http://www\\.shuaji\\.net/plus/download\\.php.*").match()){
                String downUrl = null;
                downUrl = page.getHtml().xpath("//a[@style='text-decoration:underline;']/@href").toString();
                //System.out.println("downUrl:" + downUrl);
                if(null != downUrl){
                    String id = page.getUrl().toString().split("&aid=|cid").length > 2?page.getUrl().toString().split("&aid=|&cid")[1]:null; //http://www.shuaji.net/plus/download.php?open=0&aid=20061&cid=3
                    //System.out.println("id:" + id);
                    if(id != null){
                        Apk curapk = APKS.get(id);
                        if (null != curapk) {
                            curapk.setAppDownloadUrl(downUrl);
                            APKS.put(id, curapk);
                        }
                    }
                }
            }
            else {
                String appName = null;				//app名字
                String appDetailUrl = null;			//具体页面url
                String osPlatform = null ;			//运行平台
                String appUpdateDate = null;		//更新日期
                String description = null;
                appName=page.getHtml().xpath("//div[@class='viewbox']/div[@class='title']/h2/text()").toString();
                appName = null == appName ? page.getHtml().xpath("//div[@class='viewboxinfo']/div[@class='title']/h2/text()").toString() : appName;

                appDetailUrl = page.getUrl().toString();
                osPlatform = "Android 2.1及以上";
                appUpdateDate = page.getHtml().xpath("//ul[@id='info']/li/span/text()").toString();
                List infoList = page.getHtml().xpath("//ul[@id='info']/li/span/script/@src").all();
                //System.out.println(appName);

                if(null != appName){

                    if(infoList.size() >= 2){
                        appUpdateDate = infoList.get(1).toString();
                    }
                    description = page.getHtml().xpath("//div[@class='content']/p/text()").all().toString();
                    Apk curApk = new Apk();
                    curApk.setAppName(appName);
                    curApk.setAppDescription(description);
                    curApk.setAppTsChannel(appUpdateDate);
                    curApk.setAppMetaUrl(appDetailUrl);
                    curApk.setOsPlatform(osPlatform);
                    //page.addTargetRequest(page.getHtml().xpath("//ul[@class='downurllist']/a[@target='_blank']/@href").toString());
                    String downloadUrl = page.getHtml().xpath("//ul[@class='downurllist']/a[@target='_blank']/@href").toString();
                    curApk.setAppDownloadUrl(downloadUrl);
                    return curApk;
                    
                    //System.out.println("id=" + id);
//                    System.out.println("url=" + page.getHtml().xpath("//ul[@class='downurllist']/a[@target='_blank']/@href").toString());
                }
            }
        }

        return null;
    }

    public static List<Apk> processMulti(Page page) {
        Set<String> apksId = APKS.keySet();
        List<Apk> listApk = new LinkedList<Apk>();
        for(String id : apksId){
            Apk curApk = APKS.get(id);
            if(null != curApk.getAppDownloadUrl()){
                listApk.add(curApk);
                APKS.remove(id);
            }
        }

        return listApk;
    }
}
