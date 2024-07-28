package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.repository.platform.AfficheMapper;
import com.mycuckoo.core.util.web.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private AccessoryService accessoryService;


    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            for (Long afficheId : ids) {
                // 根据公告ID查询附件列表
                List<Accessory> entityList = accessoryService.findByAfficheId(afficheId);
                List<Long> idList = new ArrayList<Long>();
                for (Accessory entity : entityList) {
                    idList.add(entity.getAccessoryId()); // 附件ID
                }
                accessoryService.deleteByIds(idList); // 删除附件数据库记录
                afficheMapper.delete(afficheId);
            }

            LogOperator.begin()
                    .module(ModuleName.SYS_AFFICHE)
                    .operate(OptName.DELETE)
                    .id("")
                    .title(null)
                    .content("删除的公告ID: %s",
                            ids.stream().map(String::valueOf).collect(Collectors.joining(DUNHAO)))
                    .level(LogLevel.THIRD)
                    .emit();
        }
    }

    public Affiche get(Long id) {
        List<Accessory> accessoryList = accessoryService.findByAfficheId(id);
        accessoryList.forEach(accessory -> {
            accessory.setAccessoryName(StringUtils.getFilename(accessory.getAccessoryName()));
        });
        Affiche entity = afficheMapper.get(id);
        entity.setAccessories(accessoryList);

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

        for (Accessory acc : entity.getAccessories()) {
            if (acc.getAccessoryId() != null) {
                continue;
            }

            String newFilename = acc.getAccessoryName();
            Accessory accessory = new Accessory();
            accessory.setInfoId(entity.getAfficheId());
            accessory.setAccessoryName(newFilename);
            accessoryService.save(accessory);
        }

        LogOperator.begin()
                .module(ModuleName.SYS_AFFICHE)
                .operate(OptName.MODIFY)
                .id(entity.getAfficheId())
                .title(null)
                .content("ID：%s, 标题：%s, 有效期限：%s, 是否发布：%s",
                        entity.getAfficheId(),
                        entity.getTitle(),
                        entity.getInvalidate(),
                        entity.getPublish())
                .level(LogLevel.SECOND)
                .emit();
    }

    @Transactional
    public void save(Affiche entity) {
        // 1. 保存公告
        entity.setCreator(SessionContextHolder.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        afficheMapper.save(entity);

        // 2. 保存附件信息
        if (entity.getAccessories() != null) {
            for (Accessory acc : entity.getAccessories()) {
                String newFilename = acc.getAccessoryName();
                Accessory accessory = new Accessory();
                accessory.setInfoId(entity.getAfficheId());
                accessory.setAccessoryName(newFilename);
                accessoryService.save(accessory);
            }
        }

        // 3. 保存操作日志
        LogOperator.begin()
                .module(ModuleName.SYS_AFFICHE)
                .operate(OptName.SAVE)
                .id(entity.getAfficheId())
                .title(null)
                .content("标题：%s, 有效期限：%s", entity.getTitle(), entity.getInvalidate())
                .level(LogLevel.FIRST)
                .emit();
    }
}
