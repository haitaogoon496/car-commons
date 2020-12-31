package com.mljr.file;

import com.mljr.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * @description: Zip压缩
 * @Date : 2018/6/3 上午11:07
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class ZipUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);
	
	public static void zip(OutputStream os, File inputFile){
		CheckedOutputStream cos = new CheckedOutputStream(os, new CRC32());
		ZipOutputStream out = new ZipOutputStream(cos);
		try {
			zip(out, inputFile, inputFile.getName());
			out.close(); // 输出流关闭
			os.close();
		} catch (Exception e) {
			LOGGER.error("打包zip文件异常", e);
			throw new RuntimeException("打包zip文件异常",e);
		}
	}

	private static void zip(ZipOutputStream out, File f, String base) throws IOException { // 方法重载
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // 创建zip压缩进入点base
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], fl[i].getName()); // 递归遍历子文件夹
			}
		} else {
			out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
			BufferedInputStream bi = new BufferedInputStream(new FileInputStream(f));
			int b;
			while ((b = bi.read()) != -1) {
				out.write(b); // 将字节流写入当前zip目录
			}
			bi.close();
		}
	}
	
	
	
	/**
	 * 递归压缩文件夹
	 * @param srcRootDir 压缩文件夹根目录的子路径
	 * @param file 当前递归压缩的文件或目录对象
	 * @param zos 压缩文件存储对象
	 * @throws Exception
	 */
	public static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception{
		if (file == null) {
			return;
		}				
		String subPath = file.getAbsolutePath();
		int index = subPath.indexOf(srcRootDir);
		if (index != -1) {
			subPath = subPath.substring(srcRootDir.length());
		}
		//如果是文件，则直接压缩该文件
		if (file.isFile()) {			
			int count, bufferLen = 1024;
			byte data[] = new byte[bufferLen];//获取文件相对于压缩文件夹根目录的子路径
			zos.putNextEntry(new ZipEntry(subPath));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			while ((count = bis.read(data, 0, bufferLen)) != -1) {
				zos.write(data, 0, count);
			}
			bis.close();
			zos.closeEntry();
		}else {
			//压缩目录中的文件或子目录
			File[] childFiles = file.listFiles();
			if(childFiles.length==0){
				zos.putNextEntry(new ZipEntry(subPath+"/"));
			}else{
				for (int n=0; n<childFiles.length; n++) {
					childFiles[n].getAbsolutePath().indexOf(file.getAbsolutePath());
					zip(srcRootDir, childFiles[n], zos);
				}
			}
		}
	}
	
	/**
	 * 对文件或文件目录进行压缩
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param zipFileName 压缩文件名
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
		if (StringTools.isEmpty(srcPath) || StringTools.isEmpty(zipPath) || StringTools.isEmpty(zipFileName)){
			throw new Exception("参数不正确");
		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;						
		try	{
			File srcFile = new File(srcPath);
			
			//判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory() && zipPath.indexOf(srcPath)!=-1) {
				throw new Exception("zipPath must not be the child directory of srcPath.");
			}
			
			//判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory()) {
				zipDir.mkdirs();
			}
			
			//创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);			
			if (zipFile.exists()){
				//检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				//删除已存在的目标文件
				zipFile.delete();				
			}
			
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			
			//如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile()){
				int index = srcPath.lastIndexOf(File.separator);
				if (index != -1){
					srcRootDir = srcPath.substring(0, index);
				}
			}
			//调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, zos);
			zos.flush();
		}catch (Exception e) {
			throw e;
		}finally {			
			try{
				if (zos != null){
					zos.close();
				}				
			}catch (Exception e){
				LOGGER.error("IO关闭异常", e);
			}
			try{
				if (cos != null){
					cos.close();
				}				
			}catch (Exception e){
				LOGGER.error("IO关闭异常", e);
			}
		}
	}
	
	
}
