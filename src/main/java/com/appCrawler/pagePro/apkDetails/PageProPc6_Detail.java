package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * PC6安卓网[中国] app搜索抓取
 * url:http://www.pc6.com/android/465_1.html,http://www.pc6.com/andyx/466_1.html
 *
 * @version 1.0.0
 */
public class PageProPc6_Detail {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PageProPc6_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        String appDownloadUrl = null;       //app下载地址
        String osPlatform = null ;          //运行平台
        String appVersion = null;           //app版本
        String appSize = null;              //app大小
        String appUpdateDate = null;        //更新日期
        String appType = null;              //下载的文件类型 apk？zip？rar？ipa?
        String appvender = null;            //app开发者  APK这个类中尚未添加
        String appDownloadedTime=null;      //app的下载次数
        String appDescription =null; 
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = null;
        String appTag = null;
        String appCategory =null;
        List<String> appScreenshot = null;
        //app的详细介绍
        // 找出对应需要信息
        String appDetailUrl = page.getUrl().toString();
        String appName = page.getHtml().xpath("//div[@class='fixed']/h1/text()").toString();
        if(appName!=null)
        {
         appVersion = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='fixed']/p[3]/i[2]/text()").get(), "：");
        
      appDownloadUrl = page.getHtml().xpath("//ul[@class='ul_Address']/script").toString();
        if (null != appDownloadUrl) {
            //<script type="text/javascript"> _downInfo={Address:"/cx/QQQingLv.pc6.apk",TypeID:"45",SoftLinkID:"253387",SoftID:"91225",Special:"0"}</script>
            appDownloadUrl = appDownloadUrl.split("\\_downInfo=\\{Address\\:").length > 1 ? appDownloadUrl.split("\\_downInfo=\\{Address\\:")[1] : null;
            if(appDownloadUrl != null){
                appDownloadUrl = appDownloadUrl.split(",TypeID:")[0];
                appDownloadUrl = appDownloadUrl.replace("\"", "");
            }
            appDownloadUrl = "http://a3wt.pc6.com" + appDownloadUrl;
        }
        //System.out.println(page.getHtml().toString());
         osPlatform = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='fixed']/p[3]/i[7]/text()").get(), "：");
         appSize = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='fixed']/p[3]/i[4]/text()").get(), "：");
         appUpdateDate = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='fixed']/p[3]/i[3]/text()").get(), "：");
         appDescription = html.xpath("//div[@id='content']/p[2]/text()").get();
         appScreenshot = html.xpath("//div[@class='dimg']/span/i/img/@src").all();  
         appCategory = html.xpath("//p[@class='seat']/a[3]/text()").get();

        }
        else if(page.getHtml().xpath("//div[@class='title']/h1/text()").toString()!=null)
        {
        	appName=page.getHtml().xpath("//div[@class='title']/h1/text()").toString();
        	 appDownloadUrl = page.getHtml().xpath("//a[@class='az']/@href").toString();
        	 osPlatform = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='Rinfo']/i/em[5]/text()").toString(),"平台：");
        	 appUpdateDate = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='Rinfo']/i/em[2]/text()").toString(),"更新：");
        	 appSize = StringUtils.substringAfter(page.getHtml().xpath("//div[@class='Rinfo']/i/em[1]/text()").toString(),"大小：");
        	 appCategory =StringUtils.substringAfter(page.getHtml().xpath("//div[@class='Rinfo']/i/em[1]/text()").toString(),"类型：");
        	 appDescription = html.xpath("//div[@class='xx']/p/text()").get();
        	 appScreenshot = html.xpath("//dd[@class='p1 on']/em/s/a/img/@src").all();  
        }
              
        
        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
            apk.setAppComment(appComment);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
