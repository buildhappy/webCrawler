package com.appCrawler.pagePro.apkDetails;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Apk;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
/**
 * 360手机助手，获取apk详情工具
 * @author buildhappy
 *
 */
public class PagePro360_Detail {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PagePro360_Detail.class);
	public static Apk getApkDetail(Page page){
		Html html = page.getHtml();
        String appDetailUrl = page.getUrl().toString();
        String appName = html.xpath("//dl[@class='clearfix']/dd/h2/span/text()").toString();
        String appVersion = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[1]/text()").get();
        String appDownloadUrl = StringUtils.substringBetween(html.get(), "'downloadUrl': '", "'");
        String osPlatform = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[2]/text()").get();
        String appSize = html.xpath("//div[@class='pf']/span[4]/text()").get();
        String appUpdateDate = html.xpath("//div[@class='base-info']/table/tbody/tr[1]/td[2]/text()").get();
        String appType = null;
        String downcount= StringUtils.substringAfter(html.xpath("//div[@class='pf']/span[3]/text()").get(), "：");
        String appDescription = html.xpath("//div[@class='breif']/text()").get();
        // 评论url
        String commontUrl = "";
        LOGGER.info("name:{}, version: {}, url:{}, size: {}, appType: {}, os: {}, date:{}, commontUrl:{}, "
        		+ "downloadNum:{}, appDesc:{}", appName, appVersion, appDownloadUrl, appSize, 
        		appType, osPlatform, appUpdateDate, commontUrl, downcount, appDescription);
        Apk apk = null;
        if (null != appName && null != appDownloadUrl) {
            apk = new Apk(appName, appDetailUrl, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, null != appType ? appType : "APK");
            apk.setAppDescription(appDescription);
            apk.setAppDownloadTimes(downcount);
            apk.setAppCommentUrl(commontUrl);
            apk.setAppCommentUrl(commontUrl);
            /*获取评论地址，在服务器上运行这部分代码时一直报错，暂时注释掉
            try {
                // 处理评论
                HttpClientLib clientLib = new HttpClientLib();
                commontUrl = String.format("http://intf.baike.360.cn/index.php?name=%s&c=message&a=getmessage&start=1&count=20", URLEncoder.encode(appName, "UTF-8"));
                HttpResponse response = clientLib.getUrlReponse(commontUrl);

                // 获取总条数
                JSONObject json = JSON.parseObject(EntityUtils.toString(response.getEntity()));
                if (!"-1".equals(json.getString("errno"))) {
                    int count = json.getJSONObject("data").getInteger("total");

                    // 处理分页评论url
                    List<String> commList = new ArrayList<String>();
                    int loop = count / 20 + 1;
                    for (int i = 1; i <= loop; i++) {

                        /// 需要再APK再加个List存放
                        commList.add(String.format("http://intf.baike.360.cn/index.php?name=%s&c=message&a=getmessage&start=%s&count=20", URLEncoder.encode(appName, "UTF-8"), i * 20 + 1));
                    }

                    apk.setAppCommentUrl(StringUtils.substringBetween(commList.toString(), "[", "]"));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
             */
        }
        
        return apk;
	}
}
