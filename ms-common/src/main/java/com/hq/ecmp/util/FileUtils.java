package com.hq.ecmp.util;

import com.hq.common.exception.BaseException;
import com.hq.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);


    public static String savepath;


    public static String getSavepath() {
        return savepath;
    }

    @Value("${file.config.savepath}")
    public  void setSavepath(String savepath) {
        FileUtils.savepath = savepath;
    }


    /**
     * 保存文件，直接以multipartFile形式
     * @param multipartFile
     * @param parmPath 文件保存绝对路径
     * @return 返回文件名
     * @throws IOException
     */
    public static String uploadfile(MultipartFile multipartFile, String parmPath)  {

        if (multipartFile.isEmpty() || !StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new BaseException("上传文件为空");
        }
        String contentType = multipartFile.getContentType();
        if (!contentType.contains("")) {
            throw new BaseException("上传文件为空");
        }
        String fileName = multipartFile.getOriginalFilename();
        logger.info("上传源文件文件:name={},type={}", fileName, contentType);
        //处理图片

        //获取路径
        String path = getFilePath(parmPath);
        try {
            upLoadFile(multipartFile.getBytes(),path,fileName);
            logger.info("文件上传成功,文件保存路径:{}", path+ File.separator+fileName);
            return parmPath+ File.separator+fileName;
        } catch (Exception e) {
            throw new BaseException("文件上传失败");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BaseException("文件上传失败");
        }
    }

    public static String getFilePath(String path){
        String basePath = FileUtils.getSavepath()+File.separator+path;
        return basePath;
    }



    public static void upLoadFile(byte[] input, String filePath,String fileName)  {
        File fileDir = new File( filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(filePath, fileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(input);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String uploadFile2Local(byte[] input, String filePath,String fileName) throws Throwable{
        InputStream in = new ByteArrayInputStream(input);

        if (filePath.indexOf("/") == 0) {
            filePath = filePath.substring(1);
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        String rootPath = getFilePath(filePath);
        logger.info("文件根目录{}",rootPath);
        File fileDir = new File(rootPath );
        if (!fileDir.exists()) {
            fileDir.mkdirs();
            logger.info("创建文件夹成功{}",rootPath);
        }
        File file = new File(rootPath, fileName);
        OutputStream out = new FileOutputStream(file);
        out.write(input);
        out.close();
        return filePath+fileName;
    }

	public static InputStream downLoadFileFromLocal(String key) {
		String root = "";
		if (!root.endsWith("/")) {
			root = root + "/";
		}
		InputStream in = null;
		File file = new File(root + key);
		try {
			in = new FileInputStream(file);
		} catch (Throwable e) {
			in = null;
		}
		return in;
	}

	public static void deleteFileOfLocal(String key) {
		String root = "";
		if (!root.endsWith("/")) {
			root = root + "/";
		}
		File fileDir = new File(root + key);
		fileDir.delete();
	}

    /**
     *
     * @param file
     * @param fileSize 单位kb
     * @return
     */
	public static boolean  checkFileSize(MultipartFile file, String fileSize){
        long size=1024*1024*2;//2MB
        if(StringUtils.isNotBlank(fileSize)&&fileSize.matches("\\d+")){
            size=Long.parseLong(fileSize)*1024;

        }
        return file.getSize()<=size;
    }

}