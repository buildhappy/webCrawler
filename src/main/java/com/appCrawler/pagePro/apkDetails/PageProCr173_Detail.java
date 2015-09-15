package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;
import java.util.Map;

/**
 * 西西软件园[中国] app搜索抓取
 * url:http://so.cr173.com/?keyword=mt&searchType=youxi
 *
 * @version 1.0.0
 */
public class PageProCr173_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProCr173_Detail.class);

    public static Apk getApkDetail(Page page, Map<String, String> urlMap){
        // 获取dom对象
        Html html = page.getHtml();

        // 找出对应需要信息
        String appVersion = null;
        String appType = null;
        String appDownloadUrl = null;
        String osPlatform= null;
        String appSize= null;
        String appUpdateDate= null;
        String appDetailUrl = page.getUrl().toString();
        String appName =html.xpath("//div[@class='mj_yyjs font-f-yh']/text()").get();;
        String appDescription =null;
        String downladInfo = html.xpath("//ul[@class='ul_Address']/script").toString();
        String appCategory=null;        
        List<String> appScreenshot = html.xpath("//div[@id='mj_tu_1']/img/@src").all();
        String appTag = null;
        appCategory = StringUtils.substringAfterLast(html.xpath("//div[@class='mj_info font-f-yh']/ul/li[6]/text()").toString(), "：");
        String appCommentUrl = null;
      //  String appComment = html.xpath("//div[@id='h_d']").get();
        String dowloadNum = null;
        
        if(html.xpath("//dl[@id='big_tit']/dt/h1/text()").toString()!=null){
        	appName=html.xpath("//dl[@id='big_tit']/dt/h1/text()").toString();
            //downladInfo = html.xpath("//ul[@class='ul_Address']/script").toString();
            osPlatform = StringUtils.substringAfter(html.xpath("//ul[@id='gmcfg']/li[1]/text()").toString(), ": ");
            appSize = StringUtils.substringAfter(html.xpath("//ul[@id='gmcfg']/li[3]/text()").toString(), ": ");
            appUpdateDate = StringUtils.substringAfter(html.xpath("//ul[@id='gmcfg']/li[4]/text()").toString(), ": ");
        }
        else if(html.xpath("//h1[@id='softtitle']/text()").toString()!=null){
            appName = html.xpath("//h1[@id='softtitle']/text()").toString();
            //appVersion=StringUtils.substringAfterLast(appName, " ");
            osPlatform = StringUtils.substringAfter(html.xpath("//div[@class='c_soft_info']/ul/li[8]/text()").toString(), ": ");
            appSize = StringUtils.substringAfter(html.xpath("//div[@class='c_soft_info']/ul/li[1]/text()").toString(), ": ");           
            appUpdateDate = StringUtils.substringAfter(html.xpath("//div[@class='c_soft_info']/ul/li[2]/text()").toString(), ": ");
            appDescription =html.xpath("//div[@id='content']/p[1]/text()").get();
            appScreenshot=html.xpath("//a[@class='pic_0']/img/@src").all();
        }
        else if(html.xpath("//dl[@class='d-info']/dt/h1/text()").toString()!=null){
        	appName = html.xpath("//dl[@class='d-info']/dt/h1/text()").toString();
            osPlatform = html.xpath("//dl[@class='d-info']/dd[1]/p[2]/strong/text()").toString();
            appSize =  html.xpath("//dl[@class='d-info']/dd[2]/p[1]/strong/text()").toString();
            appUpdateDate = html.xpath("//dl[@class='d-info']/dd/p[1]/text()").toString();
            appDescription = html.xpath("//div[@class=game-info']/p[1]/text()").get();
        }
        else{
        	appName = html.xpath("//div[@class='info']/h1/a/text()").toString();	
        	osPlatform = html.xpath("////div[@class='info']/ul/li[8]/text()").toString();
            appSize =  html.xpath("////div[@class='info']/ul/li[1]/text()").toString();
            appUpdateDate = html.xpath("////div[@class='info']/ul/li[9]/text()").toString();
            appDescription = html.xpath("//div[@class=game-info']/p[1]/text()").get();
            appCategory=html.xpath("//div[@class=game-info']/p[2]/text()").get();
        }
        // 处理下载url
        String typeId = StringUtils.substringBetween(downladInfo, "TypeID:\"", "\",");
        if (null != urlMap) {
            String urlInfo = urlMap.get("siteId_" + typeId);
            if (StringUtils.isNotEmpty(urlInfo)) {
                appDownloadUrl = StringUtils.substringBetween(urlInfo, "||", ",") + StringUtils.substringBetween(downladInfo, "Address:\"", "\"");
                if(!appDownloadUrl.endsWith("apk"))
                	appDownloadUrl=null;
                		
            }
        }

        

        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
         //   apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot,  appDescription);

        return apk;
    }
}
