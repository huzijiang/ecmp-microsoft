package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hq.ecmp.vo.ZimgConfig;
import com.hq.ecmp.vo.ZimgResult;
import com.hq.ecmp.mscore.service.ZimgService;
import com.hq.ecmp.util.HttpClientUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xueyong
 */
@Data
@Service
@Slf4j
public class ZimgServiceImpl implements ZimgService {

    @Autowired
    private ZimgConfig zimgConfig;

    private static final String uploadPath = "/upload";
    private static final String deletePath = "/admin";

    @Override
    public String uploadImage(MultipartFile multipartFile) {
        String url = zimgConfig.getZimgServer() + uploadPath;
        String s = HttpClientUtil.postMultipartFileToImage(url, multipartFile);
        ZimgResult zimgResult = JSONObject.parseObject(s, ZimgResult.class);
        if (zimgResult.isRet()) {
            String imgUrl = zimgResult.getInfo().getMd5();
            log.info("imgUrl={}", imgUrl);
            return zimgConfig.getImageServer() + imgUrl;
        } else {
            log.error("文件上传失败 {}", zimgResult);
            return null;
        }
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> multipartFiles) {
        List<String> list = multipartFiles.parallelStream().map(multipartFile -> uploadImage(multipartFile)).collect(Collectors.toList());
        return list;
    }

    @Override
    public String uploadImage(File file) {
        String url = zimgConfig.getZimgServer() + uploadPath;
        String s = HttpClientUtil.postFileToImage(url, file);
        ZimgResult zimgResult = JSONObject.parseObject(s, ZimgResult.class);
        if (zimgResult.isRet()) {
            String imgUrl =zimgResult.getInfo().getMd5();
            log.info("imgUrl={}", imgUrl);
            return zimgConfig.getImageServer() + imgUrl;
        }
        return null;
    }

    /**
     * 需要服务器开启远程修改权限 按自己需要修改 admin_rule='allow 127.0.0.1' 这一项
     */
    @Override
    public boolean deleteImage(String md5) {
        String url = zimgConfig.getZimgServer() + deletePath;
        Map<String, String> params = new HashMap<>(2);
        params.put("md5", md5);
        params.put("t", "1");
        String s = HttpClientUtil.doGet(url, params, null);
        log.info("上传文件返回:{}", s);
        //这里因忘记ssh密码，暂时无法登陆服务器修改配置，所以不知道delete返回结果具体是什么。以下两行只是示意处理流程
        ZimgResult zimgResult = JSONObject.parseObject(s, ZimgResult.class);
        return zimgResult.isRet();
    }
}