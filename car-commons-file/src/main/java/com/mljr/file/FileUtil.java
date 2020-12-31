package com.mljr.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 文件工具类
 * @Date : 2018/6/3 上午11:07
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class FileUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	/** 
	* @Title: downLoadFile  
	* @Description: TODO 下载文件 
	* @param @param response
	* @param @param path
	* @param @param fileName
	* @author px.yang@zuche.com   
	* @date 2015-12-13 上午11:38:13  
	* @throws
	 */
   public static void downLoadFile(HttpServletResponse response,String path,String fileName) throws IOException{
	   downLoadFile(response, new FileInputStream(path), fileName);
   }
   
   /** 
	* @Title: downLoadFile  
	* @Description: TODO 下载文件 
	* @param @param response
	* @param @param path
	* @param @param fileName
	* @author px.yang@zuche.com   
	* @date 2015-12-13 上午11:38:13  
	* @throws
	 */
	public static void downLoadFile(HttpServletResponse response, InputStream is, String fileName) throws IOException {
		response.setContentType("application/x-download");
		BufferedOutputStream bos = null;
		try {
			ServletOutputStream os = response.getOutputStream();
			bos = new BufferedOutputStream(os);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			byte[] len = new byte[1024 * 2];
			int read = 0;
			while ((read = is.read(len)) != -1) {
				bos.write(len, 0, read);
			}
			bos.flush();
			is.close();
		} catch (Exception e) {
			LOGGER.info("下载异常");
		}
	}
	
	 /**
	  * 创建文件夹
	 * @Title: makeDir  
	 * @param @param dir   
	 * @author px.yang@zuche.com   
	 * @date 2015-12-16 上午11:18:39  
	  */
	public static boolean makeDir(File dir) {
		boolean flag = true;
		try {
			if (!dir.getParentFile().exists()) {
				makeDir(dir.getParentFile());
			}
			if (!dir.exists()) {
				flag = dir.mkdir();
			}
		} catch (Exception e) {
			LOGGER.error("创建文件夹异常", e);
			flag = false;
		}
		 return flag;
	}
	
  /**
   * 删除文件下的excel文件
   * @Title: delExcelFile  
   * @param @param path
   * @return boolean   
   * @author px.yang@zuche.com   
   * @date 2015-12-13 下午3:20:30  
   * @throws
   */
	public static boolean delExcelFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = tempList.length; i > 0; i--) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i - 1]);
			} else {
				temp = new File(path + File.separator + tempList[i - 1]);
			}
			if (temp.isFile() && (temp.getName().endsWith("xlsx") || temp.getName().endsWith("xls"))) {
				temp.delete();
			}
		}
		return flag;
	}
	
	/**
	 * 删除指定的文件或目录
	 * Description: 
	 * @Version1.0 2015年12月21日 上午9:42:43 by jxwu（jxuw@10101111.com）创建
	 * @param file
	 * @return
	 */
	public static boolean delete(File file) {
		if (!file.exists()) {
			return false;
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = files.length; i > 0; i--) {
				delete(files[i - 1]);
			}
		}
		file.delete();
		return true;
	}
	
	/**
    * 删除过期文件
    * Description: 
    * @Version1.0 2015年12月21日 上午9:16:43 by jxwu（jxwu@10101111.com）创建
    * @param path 根目录
    * @param period 生命周期 (单位毫秒)
    */
   public static void delExpireFile(String path, long period){
	   //如果path不以文件分隔符结尾，自动添加文件分隔符  
	    if (!path.endsWith(File.separator)) {  
	    	path = path + File.separator;  
	    }  
	    File dirFile = new File(path);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return;  
	    }
	    File[] files = dirFile.listFiles();
	    long now = System.currentTimeMillis();
	    for (int i = files.length; i > 0 ; i--) {
	    	if(now - files[i-1].lastModified() > period){
	    		FileUtil.delete(files[i-1]);
	    	}
	    }
   }

	/**
	 * stream 转byte[]
 	 * @param inStream
	 * @return
	 * @throws IOException
	 */
   public static final byte[] input2byte(InputStream inStream)
			throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
   }

}
