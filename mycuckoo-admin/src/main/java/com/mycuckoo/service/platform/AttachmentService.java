package com.mycuckoo.service.platform;

import com.mycuckoo.core.operator.AttachmentType;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.platform.Attachment;
import com.mycuckoo.repository.platform.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 功能说明: 附件业务类
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 11, 2026 11:45:00 PM
 */
@Service
@Transactional(readOnly = true)
public class AttachmentService {

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private CloudFileService cloudFileService;



    public String getUrlByFileId(String fileId) {
        return cloudFileService.getUrl(fileId);
    }

    public Attachment findOneBy(AttachmentType type, String busiId) {
        List<Attachment> list = this.findByBusiTypeAndBusiId(type, busiId);
        return list.stream()
                .sorted(Comparator.comparing(Attachment::getId).reversed())
                .findFirst()
                .orElse(null);
    }

    public List<Attachment> findByBusiTypeAndBusiId(AttachmentType type, String busiId) {
        return this.findByBusiTypeAndBusiId(type, busiId, false);
    }

    public List<Attachment> findByBusiTypeAndBusiId(AttachmentType type, String busiId, boolean allSubType) {
        return attachmentMapper.findByBusiTypeAndBusiId(type.getBusiType(), allSubType ? null : type.getBusiSubType(), busiId);
    }

    @Transactional
    public void deleteByBusiTypeAndBusiId(AttachmentType type, String busiId) {
        attachmentMapper.deleteByBusiTypeAndBusiId(type.getBusiType(), type.getBusiSubType(), busiId);
    }

    @Transactional
    public void save(Attachment entity) {
        entity.setOrgId(SessionContextHolder.getOrganId());
        entity.setCreator(String.valueOf(SessionContextHolder.getUserId()));
        entity.setCreateTime(LocalDateTime.now());
        attachmentMapper.save(entity);
    }
}
