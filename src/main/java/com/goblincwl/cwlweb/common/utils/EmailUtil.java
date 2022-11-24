package com.goblincwl.cwlweb.common.utils;

import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * 邮件工具类
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/23 10:17
 */
@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final KeyValueOptionsService keyValueOptionsService;
    public static Map<String, String> emailConfigMap;

    @PostConstruct
    public void init() {
        EmailUtil.emailConfigMap = new HashMap<>(4);
        emailConfigMap.put("sendEmailAccount", keyValueOptionsService.getById("sendEmailAccount").getOptValue());
        emailConfigMap.put("sendEmailAuthorizationCode", keyValueOptionsService.getById("sendEmailAuthorizationCode").getOptValue());
        emailConfigMap.put("sendEmailTransportProtocol", keyValueOptionsService.getById("sendEmailTransportProtocol").getOptValue());
        emailConfigMap.put("sendEmailSmtpHost", keyValueOptionsService.getById("sendEmailSmtpHost").getOptValue());
        emailConfigMap.put("sendEmailSmtpPort", keyValueOptionsService.getById("sendEmailSmtpPort").getOptValue());
    }

    public static void sendEmail(String receiveEmailAccount) throws RuntimeException, GeneralSecurityException {
        //1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", EmailUtil.emailConfigMap.get("sendEmailTransportProtocol"));
        props.setProperty("mail.smtp.host", EmailUtil.emailConfigMap.get("sendEmailSmtpHost"));
        props.setProperty("mail.smtp.port", EmailUtil.emailConfigMap.get("sendEmailSmtpPort"));
        props.setProperty("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Transport transport = null;

        try {
            //2.根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);
            //3.创建一封邮件
            String sendEmailAccount = emailConfigMap.get("sendEmailAccount");
            MimeMessage message = createMimeMessage(session, sendEmailAccount, receiveEmailAccount);
            //4.根据 Session获取邮件传输对象
            transport = session.getTransport();
            //PS_03:仔细看log,认真看log,看懂log,错误原因都在log已说明。
            transport.connect(sendEmailAccount, emailConfigMap.get("sendEmailAuthorizationCode"));
            //6.发送邮件,发到所有的收件地址,message.getAllRecipients()获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("邮件发送成功");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
            }
        }

    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session     和服务器交互的会话
     * @param sendMail    发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return 邮件对象
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
        // 1.创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2.From:发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "某宝网", "UTF-8"));
        // 3.To:收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "XX用户", "UTF-8"));
        // 4.Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject("打折钜惠", "UTF-8");
        // 5.Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent("测时邮件" + new Date(), "text/html;charset=UTF-8");
        // 6.设置发件时间
        message.setSentDate(new Date());
        // 7.保存设置
        message.saveChanges();
        return message;
    }

    /**
     * 邮件发送
     *
     * @param forName     收件人昵称
     * @param toMails     收件人，多个用英文逗号分隔
     * @param mailTitle   邮件标题
     * @param mailContent 邮件内容
     */
    public static void sendMail(String forName, String toMails, String mailTitle, String mailContent) throws Exception {
        String sendEmailAccount = emailConfigMap.get("sendEmailAccount");

        String[] toMailArr;
        if (toMails != null && !"".equals(toMails)) {
            toMailArr = toMails.split(",");
        } else {
            throw new Exception("邮件发送人不能为空");
        }

        // 邮件属性信息
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", EmailUtil.emailConfigMap.get("sendEmailTransportProtocol"));
        props.setProperty("mail.smtp.host", EmailUtil.emailConfigMap.get("sendEmailSmtpHost"));
        props.setProperty("mail.smtp.port", EmailUtil.emailConfigMap.get("sendEmailSmtpPort"));
        props.setProperty("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props); // 根据属性新建一个邮件会话
        //session.setDebug(true); // 是否打印调试信息
        toMailArr = toMails.split(",");
        for (String to : toMailArr) {
            MimeMessage message = new MimeMessage(session); // 由邮件会话新建一个消息对象
            message.setFrom(new InternetAddress(sendEmailAccount));// 设置发件人的地址
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to, forName));// 设置收件人,并设置其接收类型为TO
            message.setSubject(mailTitle);// 设置标题
            message.setContent(mailContent, "text/html;charset=UTF-8"); // 设置邮件内容类型为html
            message.setSentDate(new Date());// 设置发信时间
            message.saveChanges();// 存储邮件信息

            // 发送邮件
            Transport transport = session.getTransport();
            transport.connect(sendEmailAccount, emailConfigMap.get("sendEmailAuthorizationCode"));
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
    }

    /**
     * 邮件发送（群发）
     *
     * @param mailHost    邮件服务地址
     * @param fromMail    发件人
     * @param fromName    发件人名
     * @param fromMailPwd 发件人密码
     * @param toMails     收件人，多个用英文逗号分隔
     * @param mailTitle   邮件标题
     * @param mailContent 邮件内容
     * @throws Exception
     */
    public static void sendGroupMail(String mailHost, String fromMail, String fromName, String fromMailPwd,
                                     String toMails, String mailTitle, String mailContent) throws Exception {
        String[] toMailArr = null;
        if (toMails != null && !toMails.equals("")) {
            toMailArr = toMails.split(",");
        } else {
            throw new Exception("邮件发送人不能为空");
        }

        // 邮件属性信息
        Properties props = new Properties();
        props.put("mail.host", mailHost);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");


        Session session = Session.getInstance(props); // 根据属性新建一个邮件会话
        //session.setDebug(true); // 是否打印调试信息
        MimeMessage message = new MimeMessage(session); // 由邮件会话新建一个消息对象
        message.setFrom(new InternetAddress(fromMail)); // 设置发件人的地址
        InternetAddress[] sendTo = new InternetAddress[toMailArr.length];
        for (int i = 0; i < toMailArr.length; i++) {
            sendTo[i] = new InternetAddress(toMailArr[i], fromName);
        }
        message.setRecipients(Message.RecipientType.TO, sendTo); // 设置收件人,并设置其接收类型为TO
        message.setSubject(mailTitle); // 设置标题
        message.setContent(mailContent, "text/html;charset=UTF-8"); // 设置邮件内容类型为html
        message.setSentDate(new Date()); // 设置发信时间
        message.saveChanges(); // 存储邮件信息

        // 发送邮件
        Transport transport = session.getTransport();
        transport.connect(fromMail, fromMailPwd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 读取html文件为String
     *
     * @param htmlFileName
     * @return
     * @throws Exception
     */
    public static String readHtmlToString(String htmlFileName) throws Exception {
        InputStream is = null;
        Reader reader = null;
        try {
            is = EmailUtil.class.getClassLoader().getResourceAsStream(htmlFileName);
            if (is == null) {
                throw new Exception("未找到模板文件");
            }
            reader = new InputStreamReader(is, "UTF-8");
            StringBuilder sb = new StringBuilder();
            int bufferSize = 1024;
            char[] buffer = new char[bufferSize];
            int length = 0;
            while ((length = reader.read(buffer, 0, bufferSize)) != -1) {
                sb.append(buffer, 0, length);
            }
            return sb.toString();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
//                logger.error("关闭io流异常", e);
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
//                logger.error("关闭io流异常", e);
            }
        }
    }
}
