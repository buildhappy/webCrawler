package com.appCrawler.utils;
/**
 * 正则表达式的使用
 */
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.BeanFactory;

import us.codecraft.webmagic.selector.Json;
import net.minidev.json.JSONObject;

public class ELTest {
	public static void main(String[] args){
		//String mes1 = "http://www.9game.cn/game/downs_515085_2.html?ch=JY_21";
		String mes1 = "http://www.9game.cn/game/downs_515085_2.html?ch=JY_21";
		String mes2 = "http://www.9game.cn/xcqzj/";
		
		//String regex= "[[^\\s]*html\\?ch=[^\\s]*][\\d$]";//"[\\S]+['html\\?ch=']+\\d$";
		//String regex= "(http://www\\.9game\\.cn/game)(.*)(html?ch=)(.*)";//"[\\S]+['html\\?ch=']+\\d$";
		String regex= "(?=(http://www\\.9game\\.cn).*)^(?!.*(html\\?ch)).*$";//"[\\S]+['html\\?ch=']+\\d$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mes2);
		System.out.println(m.matches());
//		
//		String reg="(?=(不管).*)^(?!.*(不合谐)).*$";//用到了前瞻
//		System.out.println("不管信不信,反正现在很不合谐".matches(reg));//false不通过
//		System.out.println("不管信不信,反正现在非常合谐".matches(reg));//true通过
//		System.out.println("合谐在某国是普遍存在的".matches(reg));//false不通过
		
		String s = "showdow(82,'宝瓶市场','/soft/img/13812828611695645441.png');";
//		String[] ss= s.split("'|,");
		int i = 0;
//		while(i < ss.length){
//			System.out.println(s.split("'|,")[i]);
//			i++;
//		}
		
		s = "<p><label>适用系统：</label>2.1 以上</p>";
		String[] ss = s.split("<label>|</label>|</p>|<p>");
//		while(i < ss.length){
//			System.out.println(ss[i]);
//			i++;
//		}
		//System.out.println(s.split("/")[4]);
		
//		s = "<p class=at><span class=one><label>版本：</label>1.9.1</span><span><label>更新时间：</label>2014-06-16</span></p>";
//		ss = s.split("<p class=at><span class=one><label>版本：</label>|</span><span><label>");
//		while(i < ss.length){
//			System.out.println(ss[i]);
//			i++;
//		}
		
//		s="https://chart.googleapis.com/chart?cht=qr&chs=120x120&choe=UTF-8&chld=S|1&chl=http://download.eoemarket.com/app?id=30970%26client_id=146%26channel_id=401%26track=pc_qq_show_index_app30970_2";
//		ss = s.split("chld\\=S\\|1\\&chl=");
//		while(i < ss.length){
//			System.out.println(ss[i]);
//			i++;
//		}
		
//		s = "http://c11.eoemarket.com/app0/17/17896/icon/721873.png";
//		ss = s.split("http://c11.eoemarket.com/|.png");
//		while(i < ss.length){
//			System.out.println(i + ":" + ss[i].replace("icon", "apk"));
//			
//			i++;
//		}
		
//		s = "http://download.eoemarket.com/app?id=17896%26client_id=146%26channel_id=401%26track=pc_qq_show_index_app17896_2";
//		ss = s.split("channel_id=|%");
//		while(i < ss.length){
//			System.out.println(i + ":" + ss[i]);
//			
//			i++;
//		}
		
		regex = "http://android\\.173sy\\.com/games/search.php?key=*";
		regex = "\\*";
		p = Pattern.compile(regex);
		m = p.matcher("http://android.173sy.com/games/search.php?key=qq&p=7");
		System.out.println(m.matches());
	}
}
