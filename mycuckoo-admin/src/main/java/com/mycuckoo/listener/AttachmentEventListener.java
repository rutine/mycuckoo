package com.mycuckoo.listener;

import com.mycuckoo.core.operator.event.AttachmentEvent;
import com.mycuckoo.domain.platform.Attachment;
import com.mycuckoo.service.platform.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * 功能说明: 附件事件监听器
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 12, 2026 12:20:00 AM
 */
public class AttachmentEventListener implements ApplicationListener<AttachmentEvent> {

    @Autowired
    private AttachmentService attachmentService;

    @Override
    public void onApplicationEvent(AttachmentEvent event) {
        AttachmentEvent.Payload payload = (AttachmentEvent.Payload) event.getSource();

        attachmentService.deleteByBusiTypeAndBusiId(payload.getType(), payload.getBusiId());

        Attachment entity = new Attachment();
        entity.setBusiType(payload.getType().getBusiType());
        entity.setBusiSubType(payload.getType().getBusiSubType());
        entity.setBusiId(payload.getBusiId());
        entity.setFileId(payload.getFileId());
        attachmentService.save(entity);
    }
}
