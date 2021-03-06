package com.mycuckoo.service.platform;

import com.mycuckoo.common.constant.LogLevel;
import com.mycuckoo.common.constant.OptName;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.repository.platform.AccessoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_ACCESSORY;

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
    @Autowired
    private SystemOptLogService systemOptLogService;


    @Transactional
    public void deleteByIds(List<Long> accessoryIds) {
        if (!accessoryIds.isEmpty()) {
            // 删除文件
            for (long id : accessoryIds) {
                Accessory accessory = this.get(id);
                CommonUtils.deleteFile("", accessory.getAccessoryName());

                accessoryMapper.delete(id);
            }

            String optContent = "删除附件ID：" + accessoryIds.toString();
            systemOptLogService.saveLog(LogLevel.THIRD, OptName.DELETE, SYS_ACCESSORY, optContent, "");
        }
    }

    public List<Accessory> findByAfficheId(long afficheId) {
        return accessoryMapper.findByAfficheId(afficheId);
    }

    public Accessory get(long accessoryId) {
        return accessoryMapper.get(accessoryId);
    }

    @Transactional
    public void save(Accessory accessory) {
        accessoryMapper.save(accessory);

        String optContent = "附件业务表ID：" + accessory.getInfoId() + SPLIT + "附件名称:" + accessory.getAccessoryName();
        systemOptLogService.saveLog(LogLevel.FIRST, OptName.SAVE, SYS_ACCESSORY, optContent, accessory.getAccessoryId() + "");
    }
}
