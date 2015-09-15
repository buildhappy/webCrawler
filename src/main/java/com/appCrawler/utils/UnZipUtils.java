package com.appCrawler.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩工具
 * 
 * @author 梁栋   
 * @since 1.0
 */
public class UnZipUtils {

	public static final String EXT = ".zip";
	private static final String BASE_DIR = "";
	private static final String PATH = File.separator;
	private static final int BUFFER = 1024;

	/**
	 * put the unzip file to the current path
	 * @param srcPath:the source file path
	 * @throws Exception
	 */
	public static void decompress(String srcPath) throws Exception {
		File srcFile = new File(srcPath);
		//decompress(srcFile);
		String basePath = srcFile.getParent();
		decompress(srcFile, basePath);
	}
	/**
	 * put the unzip file to the current path; delete the zip file when unzip done
	 * @param srcPath:the source file path
	 * @throws Exception
	 */
	public static void decompressAndDelete(String srcPath) throws Exception {
		File srcFile = new File(srcPath);
		//decompress(srcFile);
		String basePath = srcFile.getParent();
		decompress(srcFile, basePath);
		srcFile.delete();
	}


	/**
	 * put the unzip file to specified path
	 * 
	 * @param srcPath:the zip file path
	 * @param destPath:the destination of unzip file  
	 * @throws Exception
	 */
	public static void decompress(String srcPath, String destPath)
			throws Exception {

		File srcFile = new File(srcPath);
		decompress(srcFile, destPath);

	}

	/**
	 * unzip the file to specified path;
	 * 
	 * @param srcFile:the zip file
	 * @param destPath
	 * @throws Exception
	 */
	public static void decompress(File srcFile, String destPath)
			throws Exception {
		//decompress(srcFile, new File(destPath));
		
		
		CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());

		ZipInputStream zis = new ZipInputStream(cis);

		decompress(new File(destPath), zis);

		zis.close();

	}
	
	private static void decompress(File destFile, ZipInputStream zis)
			throws Exception {

		ZipEntry entry = null;
		//only get the apk file
		while ((entry = zis.getNextEntry()) != null && (entry.getName().endsWith(".apk"))) {

			//System.out.println("entry:" + entry.getName());
			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();

			File dirFile = new File(dir);

			// 文件检查
			fileProber(dirFile);

 			if (entry.isDirectory()) {
 				dirFile.mkdirs();
 			} else {
 				decompressFile(dirFile, zis);
 			}

 			zis.closeEntry();
 		}
 	}

	/**
	 * 文件探针
	 * 当父目录不存在时，创建目录！
	 * @param dirFile
	 */
	private static void fileProber(File dirFile) {

		File parentFile = dirFile.getParentFile();
		if (!parentFile.exists()) {

			// 递归寻找上级目录
			fileProber(parentFile);

			parentFile.mkdir();
		}
	}

	/**
	 * 文件解压缩
	 * 
	 * @param destFile
	 *            目标文件
	 * @param zis
	 *            ZipInputStream
	 * @throws Exception
	 */
	private static void decompressFile(File destFile, ZipInputStream zis)
			throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = zis.read(data, 0, BUFFER)) != -1) {
			bos.write(data, 0, count);
		}

		bos.close();
	}

}

