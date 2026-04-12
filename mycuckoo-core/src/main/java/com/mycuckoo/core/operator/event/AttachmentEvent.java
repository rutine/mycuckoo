package com.mycuckoo.core.operator.event;

import com.mycuckoo.core.operator.AttachmentType;
import org.springframework.context.ApplicationEvent;

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
        private final String busiId;
        private final String fileId;

        public Payload(AttachmentType type, String busiId, String fileId) {
            this.type = type;
            this.busiId = busiId;
            this.fileId = fileId;
        }

        public AttachmentType getType() {
            return type;
        }

        public String getBusiId() {
            return busiId;
        }

        public String getFileId() {
            return fileId;
        }
    }
}
