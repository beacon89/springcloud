package com.kayak.upload.action;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.web.multipart.MultipartFile;

import com.kayak.base.util.Tools;

/**
 * 通用的文件上传与下载工具类
 * 
 * @author liuyg
 * 
 */
public class UploadUtil {

	/**
	 * 通用的上传接口
	 * 
	 * @param temFile
	 * @param uploadPath
	 *            如果uploadPath为空，这使用默认的上传根目录/upload
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String upload(MultipartFile temFile, String uploadPath) throws IllegalStateException, IOException {
		String fileName = temFile.getOriginalFilename();

		String path = getFilePath(uploadPath);

		new File(path).mkdirs();

		String uploadFileName = path + fileName;

		File uploadFile = new File(uploadFileName);

		temFile.transferTo(uploadFile);

		return uploadFileName;
	}

	/**
	 * 根据当前的时间建立文件夹，时间格式yyyyMMdd
	 * 
	 * @param path
	 * @param extension
	 *            后缀名
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String buildFilePathByExtension(String path, String extension) {
		// 创建文件夹
		return getFilePath(path) + generateFileNameByExtension(extension);
	}

	/**
	 * 获取文件随机目录
	 * 
	 * @param path
	 * @return
	 */
	private static String getFilePath(String path) {
		// 获取当前日期
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String formatDate = format.format(new Date());

		// 创建文件夹
		String filePath = path + formatDate + '/';

		DateFormat format2 = new SimpleDateFormat("HHmmss");
		String formatDate2 = format2.format(new Date());
		int random = new Random().nextInt(10000);

		filePath += formatDate2 + random + "/";

		return filePath;
	}

	/**
	 * 根据后缀名生成文件名
	 * 
	 * @param extension
	 * @return
	 */
	private static String generateFileNameByExtension(String extension) {
		DateFormat format = new SimpleDateFormat("HHmmss");
		String formatDate = format.format(new Date());
		int random = new Random().nextInt(10000);

		if (Tools.strIsEmpty(extension)) {
			return formatDate + random;
		} else {
			return formatDate + random + "." + extension;
		}

	}

}
