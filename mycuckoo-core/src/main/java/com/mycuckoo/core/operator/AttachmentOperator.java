package com.mycuckoo.core.operator;

import com.mycuckoo.core.operator.event.AttachmentEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;

/**
 * 功能说明: 附件关联构建器
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 12, 2026 12:28:00 AM
 */
public final class AttachmentOperator {
    private static ApplicationEventPublisher publisher;

    private AttachmentType type;
    private String busiId;
    private Collection<?> attachments;

    private AttachmentOperator() {
    }

    public static void setEventMulticaster(ApplicationEventPublisher publisher) {
        if (publisher != null) {
            AttachmentOperator.publisher = publisher;
        }
    }

    public static AttachmentOperator begin() {
        return new AttachmentOperator();
    }

    public AttachmentOperator busiType(AttachmentType type) {
        this.type = type;
        return this;
    }

    public AttachmentOperator busiId(String busiId) {
        this.busiId = busiId;
        return this;
    }

    public AttachmentOperator attachments(Collection<?> attachments) {
        this.attachments = attachments;
        return this;
    }

    public AttachmentEvent.Payload build() {
        return new AttachmentEvent.Payload(this.type, this.busiId, this.attachments);
    }

    public AttachmentEvent.Payload emit() {
        AttachmentEvent.Payload payload = this.build();
        if (publisher != null) {
            publisher.publishEvent(new AttachmentEvent(payload));
        }

        return payload;
    }
}
