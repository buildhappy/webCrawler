package com.appCrawler.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by sunnycomes on 15-5-13.
 */
public class ProxyTest {
    public static void main(String[] args) throws IOException {
        System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("http.proxyPort", "8001");
        System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
        System.setProperty("https.proxyPort", "8001");

        URL url = new URL("http://search.3533.com/game?keyword=qq");
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = br.readLine();
        while(line != null) {
            System.out.println(line = br.readLine());
        }
    }
}
