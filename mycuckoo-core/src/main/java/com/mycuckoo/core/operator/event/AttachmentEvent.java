package com.mycuckoo.core.operator.event;

import com.mycuckoo.core.operator.AttachmentType;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;
import java.util.Collections;

/**
 * 功能说明: 附件关联事件
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 12, 2026 12:18:00 AM
 */
public class AttachmentEvent extends ApplicationEvent {
    public AttachmentEvent(AttachmentEvent.Payload source) {
        super(source);
    }

    public static class Payload {
        private final AttachmentType type;
        private final String bizId;
        private final Collection<?> attachments;

        public Payload(AttachmentType type, String bizId, Collection<?> attachments) {
            this.type = type;
            this.bizId = bizId;
            this.attachments = attachments == null ? Collections.emptyList() : attachments;
        }

        public AttachmentType getType() {
            return type;
        }

        public String getBizId() {
            return bizId;
        }

        public Collection<?> getAttachments() {
            return attachments;
        }
    }
}
