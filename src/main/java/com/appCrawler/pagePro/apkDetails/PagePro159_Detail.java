package com.appCrawler.pagePro.apkDetails;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 机客应用[中国] app搜索抓取
 * url:http://games.159.com/ashx/Common.ashx?acttion=getgames_pager&pageindex=0&pagesize=200&type=3001&querystr=%E6%95%A2%E6%AD%BB%E9%98%9F
 *
 * @version 1.0.0
 */
public class PagePro159_Detail {


    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro159_Detail.class);

    public static Apk getApkDetail(Page page){
        // 获取dom对象
        Html html = page.getHtml();

        Map<String, Object> info = null;
        try {
            info = new ObjectMapper().readValue(page.getRawText(), Map.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (null == info) {
            return null;
        }


        Map<String, Object> detail = ((List<Map>) info.get("rows")).get(0);
        // 找出对应需要信息
        String appDetailUrl = "http://games.159.com/gameinfo.aspx?KeyId=" + detail.get("KeyId");
        String appName = detail.get("Title").toString();
        String appVersion = detail.containsKey("APP_GameVer") ? detail.get("APP_GameVer").toString() : "";
        String appDownloadUrl = detail.containsKey("APP_File") ? "http://games.159.com/Upload/game/app/" + detail.get("APP_File") : "";
        String osPlatform = null;
        String appSize = detail.containsKey("APP_GameSize") ? detail.get("APP_GameSize").toString() : "";
        String appUpdateDate = detail.containsKey("APP_UpdateDate") ? detail.get("APP_UpdateDate").toString() : "";
        String appType = null;
        String appDescription = detail.get("Remark").toString().replaceAll("\r\n", "");
        List<String> appScreenshot = null;
        String appTag = null;
        String appCategory = detail.get("Type2Name").toString();
        String appCommentUrl = null;
        String appComment = null;
        String dowloadNum = detail.containsKey("APP_DownNum") ? detail.get("APP_DownNum").toString() : "";;

        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppScreenshot(appScreenshot);
            apk.setAppCommentUrl(appCommentUrl);
            apk.setAppDownloadTimes(dowloadNum);
            apk.setAppComment(appComment);
            apk.setAppCategory(appCategory);
            apk.setAppTag(appTag);
        }

        LOGGER.debug("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, downlaodNum:{}, , appTag:{}, appCategory:{}" +
                        ", appScreenhost:{}, appCommentUrl:{}, appComment:{}, appDescription:{}", appName, appVersion, appDownloadUrl,
                appSize, appType, osPlatform, appUpdateDate, dowloadNum, appTag, appCategory, appScreenshot, appCommentUrl, appComment, appDescription);

        return apk;
    }
}
