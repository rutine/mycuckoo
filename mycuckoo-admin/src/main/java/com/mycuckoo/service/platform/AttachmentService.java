package com.mycuckoo.service.platform;

import com.mycuckoo.core.FileMeta;
import com.mycuckoo.core.operator.AttachmentType;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.platform.Attachment;
import com.mycuckoo.domain.platform.CloudFile;
import com.mycuckoo.repository.platform.AttachmentMapper;
import com.mycuckoo.web.config.WebProperties;
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
    public void deleteBy(AttachmentType type, String busiId, String fileId) {
        cloudFileService.deleteByIds(Arrays.asList(fileId));
        if (busiId == null) {
            return;
        }

        List<Attachment> list = attachmentMapper.findByBusiTypeAndBusiId(type.getBusiType(), null, busiId);
        list.forEach(o -> {
            if (!o.getFileId().equals(fileId)) {
                return;
            }
            attachmentMapper.delete(o.getId());
        });
    }

    public String getUrlBy(AttachmentType type, String busiId) {
        FileMeta entity = this.findOneBy(type, busiId);
        return entity == null ? null : entity.getUrl();
    }

    public FileMeta findOneBy(AttachmentType type, String busiId) {
        List<FileMeta> list = this.findByBusiTypeAndBusiId(type, busiId);
        return list.stream()
                .sorted(Comparator.comparing(FileMeta::getId).reversed())
                .findFirst()
                .orElse(null);
    }

    public List<FileMeta> findByBusiTypeAndBusiId(AttachmentType type, String busiId) {
        return this.findByBusiTypeAndBusiId(type, busiId, false);
    }

    public List<FileMeta> findByBusiTypeAndBusiId(AttachmentType type, String busiId, boolean allSubType) {
        return attachmentMapper.findByBusiTypeAndBusiId(type.getBusiType(), allSubType ? null : type.getBusiSubType(), busiId).stream().map(o -> {
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
    public void deleteByBusiTypeAndBusiId(AttachmentType type, String busiId) {
        attachmentMapper.deleteByBusiTypeAndBusiId(type.getBusiType(), type.getBusiSubType(), busiId);
    }

    @Transactional
    public void save(Attachment entity) {
        entity.setOrgId(SessionContextHolder.getOrganId());
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
