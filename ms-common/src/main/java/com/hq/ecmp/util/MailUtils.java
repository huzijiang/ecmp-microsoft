package com.hq.ecmp.util;


import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.alibaba.druid.util.StringUtils;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import sun.net.www.protocol.http.HttpURLConnection;


/***
 *add by liuzb
 */
public class MailUtils  {

    private static final Logger logger = LoggerFactory.getLogger(MailUtils.class);

    private  static final String SERVER="smtp.mxhichina.com";

    private static final String USER="servicegroup@hqzhuanche.com";

    private static final String PASS="hqzc@2020";

    private static final String SMTPPORT = "465";

    /***
     *
     * @param img
     * @param fileName
     */
    public static void writeImageToDisk(byte[] img, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     *
     * @param strUrl
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
           logger.error("getImageFromNetByUrl error",e);
        }
        return null;
    }

    /***
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /***
     *
     * @return
     * @throws Exception
     */
    public static Properties getProperties()throws Exception{
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", SERVER);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        return properties;
    }


    public static Properties properties()throws Exception{
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", SERVER);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", SMTPPORT);
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", SMTPPORT);
        return properties;
    }

    /***
     *(发送邮件)
     * @param emailAddredd
     * @param subject
     * @param msg
     * @param filename
     * @throws Exception
     */
    public static void sendMail(String emailAddredd, String subject, String msg, String filename,String fileNmae)throws Exception {
        Properties properties = getProperties();
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USER));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddredd));
            message.setSubject(subject);
            message.setContent(msg, "text/html;charset=UTF-8");
            BodyPart messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setText(msg);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            byte[] btImg = getImageFromNetByUrl(filename);
            if (null != btImg && btImg.length > 0) {
                writeImageToDisk(btImg, fileNmae);
            }
            DataSource source = new FileDataSource(fileNmae);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeText(fileNmae));
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            logger.error("sendMail error",e);
        }
    }

    /***
     *
     * @param receiveMail
     * @param msg
     * @param subject
     * @throws Exception
     */
    public static void sendMail( String receiveMail,String msg,String subject) throws Exception {
        Properties properties = properties();
        Session session = Session.getDefaultInstance(properties);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USER, "servicegroup", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail,"UTF-8"));
        message.setSubject(subject, "UTF-8");
        message.setContent(msg, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport transport = session.getTransport();
        transport.connect(USER, PASS);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}
