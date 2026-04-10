package com.mycuckoo.core.sender;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface EmailSender {
    default void send(String subject, String content, String toAddress) {
        this.send(subject, content, Arrays.asList(toAddress), (List)null, (Map)null, false);
    }

    default void send(String subject, String content, List<String> toAddress) {
        this.send(subject, content, toAddress, (List)null, (Map)null, false);
    }

    default void send(String subject, String content, String toAddress, Map<String, File> attachments, boolean deleteFile) {
        this.send(subject, content, Arrays.asList(toAddress), (List)null, attachments, deleteFile);
    }

    default void send(String subject, String content, List<String> toAddress, Map<String, File> attachments, boolean deleteFile) {
        this.send(subject, content, toAddress, (List)null, attachments, deleteFile);
    }

    void send(String subject, String content, List<String> toAddress, List<String> ccAddress, Map<String, File> attachments, boolean deleteFile);
}
