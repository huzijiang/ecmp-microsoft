package com.hq.ecmp.mscore.service.impl;

import QrCodeService.QrCodeService;
import com.hq.ecmp.mscore.service.ZimgService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class QrCodeServiceImpl implements QrCodeService{
	
	@Autowired
	ZimgService zimgService;
	
	@Override
	public String createQrCode(String content) throws Exception {
		/*int w=79;
		int h =79;
		  // 1.根据内容参数生成二维码
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix encode = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, w, h);
 
        // 2.创建图片
        BufferedImage erWeiMaImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (encode.get(x, y)) {
                    erWeiMaImage.setRGB(x, y, 0x000000);
                } else {
                    erWeiMaImage.setRGB(x, y, 0xffffff);
                }
            }
        }
      
 
        // 3.写出到本地
        ImageIO.write(erWeiMaImage, "png", new File("D:/img/a"));
       
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ImageIO.write(erWeiMaImage, "png", out);
        byte[] bytes=out.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
       // MultipartFile file = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
        MultipartFile file = new MockMultipartFile(ContentType.APPLICATION_XML.toString(), inputStream);
        String uploadImage = zimgService.uploadImage(file);
        System.out.println(uploadImage);*/
		return null;
	}
			
	
	
	
}
