package com.appCrawler.utils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 
受检异常：Exception
定义方法时必须声明所有可能会抛出的exception：在调用这个方法时，必须捕获它的checked exception，
不然就得把它的exception传递下去：exception是从java.lang.Exception类衍生出来的。
例如：IOException，SQLException就属于Exception


非受检异常：RuntimeException
在定义方法时不需要声明会抛出runtime exception：在调用这个方法时不需要捕获这个runtime exception；
runtime exception 是从java.lang.RuntimeException或java.lang.Error类衍生出来的。
例如：NullPointException，IndexOutOfBoundsException就属于runtime exception

 *
 */
public class UncheckedException {
	public static void main(String[] args){
		doThrow(new SQLException());
	}
	static void doThrow(Exception e){
		UncheckedException.<RuntimeException> test(e);//但是换成IOException时，就要捕捉异常了
	}
	static <E extends Exception> void test(Exception e) throws E{
		throw (E)e;//此处将传递的异常强制装化为E
	}
}
