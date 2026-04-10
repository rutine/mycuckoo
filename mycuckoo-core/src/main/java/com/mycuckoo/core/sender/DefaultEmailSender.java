package com.mycuckoo.core.sender;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author rutine
 * @date 2024/7/4 14:58
 */
public class DefaultEmailSender implements EmailSender {
    private static Logger logger = LoggerFactory.getLogger(DefaultEmailSender.class);

    public static final String PHONE_REG = "^1[3,4,5,6,7,8,9][0-9]{9}|^\\+(?:[0-9] ?){6,14}[0-9]$";
    public static final String EMAIL_REG = "^[\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-z0-9_-]+)+$";
    private static final Pattern PHONE = Pattern.compile(PHONE_REG);
    private static final Pattern EMAIL = Pattern.compile(EMAIL_REG);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;


    public static boolean isPhone(String str) {
        return str == null || str.trim().equals("") ? false : PHONE.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        return str == null || str.trim().equals("") ? false : EMAIL.matcher(str).matches();
    }


    public DefaultEmailSender(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;

    }

    @Override
    public void send(String subject, String content, List<String> toAddress, List<String> copyTo, Map<String, File> attachments, boolean deleteFile) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String[] toAddr = this.getEmail(toAddress);
        String[] ccAddr = this.getEmail(copyTo);
        if (toAddr.length == 0) {
            logger.warn("Ignore send. email: {}", toAddress == null ? "" : toAddress.stream().collect(Collectors.joining(",")));
            return;
        }
        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(toAddr);
            helper.setCc(ccAddr);
            if (attachments != null && attachments.size() > 0) {
                Iterator<Map.Entry<String, File>> it = attachments.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry<String, File> entry = it.next();
                    helper.addAttachment(entry.getKey(), entry.getValue());
                }
            } else {
                //没有附件, 不用处理
                deleteFile = false;
            }

            logger.info("Send email. to: {} cc: {}",
                    toAddress == null ? "" : toAddress.stream().collect(Collectors.joining(",")),
                    copyTo == null ? "" : copyTo.stream().collect(Collectors.joining(","))
            );
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("Send email fail", e);
            return;
        }

        if (deleteFile) {
            attachments.forEach((filename, file) -> file.delete());
        }
    }


    private String[] getEmail(List<String> address) {
        if (address == null) {
            return new String[0];
        }

        return address.stream().filter(DefaultEmailSender::isEmail).collect(Collectors.toList()).toArray(new String[0]);
    }


}
