package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.AttachmentType;
import com.mycuckoo.core.constant.enums.ModuleName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.operator.AttachmentOperator;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.repository.platform.AfficheMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mycuckoo.core.operator.LogOperator.DUNHAO;

/**
 * 功能说明: 公告业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:23:52 AM
 */
@Service
@Transactional(readOnly = true)
public class AfficheService {

    @Autowired
    private AfficheMapper afficheMapper;
    @Autowired
    private AttachmentService attachmentService;


    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            for (Long afficheId : ids) {
                attachmentService.deleteByBusiTypeAndBusiId(AttachmentType.AFFICHE, String.valueOf(afficheId));
                afficheMapper.delete(afficheId);
            }

            LogOperator.begin()
                    .module(ModuleName.SYS_AFFICHE)
                    .id("")
                    .title(SessionContextHolder.getUserName() + "删除" + "公告")
                    .content("删除的公告ID: %s",
                            ids.stream().map(String::valueOf).collect(Collectors.joining(DUNHAO)))
                    .emit();
        }
    }

    @Transactional
    public void deleteAttachment(Long afficheId, String fileId) {
        attachmentService.deleteBy(AttachmentType.AFFICHE, afficheId == null ? null : afficheId.toString(), fileId);
    }

    public Affiche get(Long id) {
        Affiche entity = afficheMapper.get(id);
        entity.setAttachments(attachmentService.findByBusiTypeAndBusiId(AttachmentType.AFFICHE, String.valueOf(id)));

        return entity;
    }

    public Page<Affiche> findByPage(Querier querier) {
        return afficheMapper.findByPage(querier.getQ(), querier);
    }

    public List<Affiche> findBeforeValidate() {
        return afficheMapper.findBeforeValidate(new Date());
    }

    @Transactional
    public void update(Affiche entity) {
        afficheMapper.update(entity);
        saveAttachments(entity);

        LogOperator.begin()
                .module(ModuleName.SYS_AFFICHE)
                .id(entity.getAfficheId())
                .title(SessionContextHolder.getUserName() + "修改" + "公告")
                .content("ID：%s, 标题：%s, 有效期限：%s, 是否发布：%s",
                        entity.getAfficheId(),
                        entity.getTitle(),
                        entity.getInvalidate(),
                        entity.getPublish())
                .emit();
    }

    @Transactional
    public void save(Affiche entity) {
        // 1. 保存公告
        entity.setCreator(SessionContextHolder.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        afficheMapper.save(entity);

        saveAttachments(entity);

        // 3. 保存操作日志
        LogOperator.begin()
                .module(ModuleName.SYS_AFFICHE)
                .id(entity.getAfficheId())
                .title(SessionContextHolder.getUserName() + "新增" + "公告")
                .content("标题：%s, 有效期限：%s", entity.getTitle(), entity.getInvalidate())
                .emit();
    }

    private void saveAttachments(Affiche entity) {
        if (entity.getAttachments() == null) {
            return;
        }

        AttachmentOperator.begin()
                .busiType(AttachmentType.AFFICHE)
                .busiId(String.valueOf(entity.getAfficheId()))
                .attachments(entity.getAttachments())
                .emit();
    }
}
