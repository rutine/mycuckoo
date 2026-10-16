package com.mycuckoo.admin.service.platform;

import com.mycuckoo.admin.repository.platform.AttachmentMapper;
import com.mycuckoo.core.FileMeta;
import com.mycuckoo.core.operator.AttachmentType;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.admin.entity.platform.Attachment;
import com.mycuckoo.admin.entity.platform.CloudFile;
import com.mycuckoo.admin.web.config.WebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private WebProperties properties;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private CloudFileService cloudFileService;


    @Transactional
    public void deleteBy(AttachmentType type, String bizId, String fileId) {
        cloudFileService.deleteByIds(Arrays.asList(fileId));
        if (bizId == null) {
            return;
        }

        List<Attachment> list = attachmentMapper.findByBizTypeAndBizId(type.getBizType(), null, bizId);
        list.forEach(o -> {
            if (!o.getFileId().equals(fileId)) {
                return;
            }
            attachmentMapper.delete(o.getId());
        });
    }

    public String getUrlBy(AttachmentType type, String bizId) {
        FileMeta entity = this.findOneBy(type, bizId);
        return entity == null ? null : entity.getUrl();
    }

    public FileMeta findOneBy(AttachmentType type, String bizId) {
        List<FileMeta> list = this.findByBizTypeAndBizId(type, bizId);
        return list.stream()
                .sorted(Comparator.comparing(FileMeta::getId).reversed())
                .findFirst()
                .orElse(null);
    }

    public List<FileMeta> findByBizTypeAndBizId(AttachmentType type, String bizId) {
        return this.findByBizTypeAndBizId(type, bizId, false);
    }

    public List<FileMeta> findByBizTypeAndBizId(AttachmentType type, String bizId, boolean allSubType) {
        return attachmentMapper.findByBizTypeAndBizId(type.getBizType(), allSubType ? null : type.getBizSubType(), bizId).stream().map(o -> {
                    FileMeta meta = new FileMeta();
                    meta.setId(o.getFileId());
                    meta.setUrl(properties.getHost() + "/download" + o.getFilePath());
                    meta.setName(o.getFileName());
                    meta.setType(o.getFileType());
                    meta.setSize(o.getFileSize());
                    return meta;
                }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteByBizTypeAndBizId(AttachmentType type, String bizId) {
        attachmentMapper.deleteByBizTypeAndBizId(type.getBizType(), type.getBizSubType(), bizId);
    }

    @Transactional
    public void save(Attachment entity) {
        entity.setTenantId(SessionContextHolder.getTenantId());
        entity.setCreator(String.valueOf(SessionContextHolder.getUserId()));
        entity.setCreateTime(LocalDateTime.now());

        CloudFile file = cloudFileService.get(entity.getFileId());
        entity.setFilePath(file.getPath());
        entity.setFileName(file.getName());
        entity.setFileType(file.getType());
        entity.setFileSize(file.getSize());

        attachmentMapper.save(entity);
    }
}
