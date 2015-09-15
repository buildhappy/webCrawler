package com.appCrawler.utils;
/*
import com.buptnsrc.appsec.incubator.fastdfs.FastDFSClient;
import org.csource.common.MyException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
*/
/**
 * Dfs Utils
 *
 * @author Happy Fish / YuQing
 * @version Version 1.18
 */

public class DFSUtils {

    /**
     * entry point
     *
     * @param args comand arguments
     *             <ul><li>args[0]: config filename</li></ul>
     *             <ul><li>args[1]: local filename to upload</li></ul>
     */
   /*
	public static String UploadFileToDFS(String imgPath) throws IOException, MyException {
        //String imgPath = "client.conf";
        ClassPathResource conf = new ClassPathResource("client.conf");
        FastDFSClient client = FastDFSClient.newInstance(conf.getURI().getPath(),0);
        //FastDFSClient client1 = FastDFSClient.newInstance(conf.getURI().getPath(),1);
        String pathOnFastDFS = client.uploadToFastDFS(imgPath);
        //String pathOnFastDFS1 = client1.uploadToFastDFS(imgPath);
        return pathOnFastDFS;
       // System.out.println(pathOnFastDFS1);
    }
    
    public static void main(String[] args) throws IOException, MyException{
    	System.out.println(UploadFileToDFS("C://task1_67.txt"));
    }
    */
}