package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.util.CommonUtils;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.repository.platform.AccessoryMapper;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mycuckoo.core.operator.LogOperator.DUNHAO;

/**
 * 功能说明: 附件业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:21:32 AM
 */
@Service
@Transactional(readOnly = true)
public class AccessoryService {

    @Autowired
    private AccessoryMapper accessoryMapper;

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (!ids.isEmpty()) {
            // 删除文件
            Set<Long> afficheIds = Sets.newHashSet();
            for (long id : ids) {
                Accessory entity = this.get(id);
                CommonUtils.deleteFile("", entity.getAccessoryName());

                accessoryMapper.delete(id);
                afficheIds.add(entity.getInfoId());
            }

            for (Long afficheId : afficheIds) {
                LogOperator.begin()
                        .module(ModuleName.SYS_ACCESSORY)
                        .operate(OptName.DELETE)
                        .id(afficheId)
                        .title(null)
                        .content("删除附件ID：%s",
                                ids.stream().map(String::valueOf).collect(Collectors.joining(DUNHAO)))
                        .level(LogLevel.THIRD)
                        .emit();
            }
        }
    }

    public List<Accessory> findByAfficheId(long afficheId) {
        return accessoryMapper.findByAfficheId(afficheId);
    }

    public Accessory get(long accessoryId) {
        return accessoryMapper.get(accessoryId);
    }

    @Transactional
    public void save(Accessory entity) {
        accessoryMapper.save(entity);

        LogOperator.begin()
                .module(ModuleName.SYS_ACCESSORY)
                .operate(OptName.SAVE)
                .id(entity.getAccessoryId())
                .title(null)
                .content("附件业务表ID：%s, 附件名称: %s", entity.getInfoId(), entity.getAccessoryName())
                .level(LogLevel.FIRST)
                .emit();
    }
}
