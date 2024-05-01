package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.AfficheMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.BaseConst.SPLIT;
import static com.mycuckoo.constant.ServiceConst.SYS_AFFICHE;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public void deleteByIds(List<Long> afficheIdList) {
        if (afficheIdList != null && afficheIdList.size() > 0) {
            for (Long afficheId : afficheIdList) {
                // 根据公告ID查询附件列表
                List<Accessory> accessoryList = accessoryService.findByAfficheId(afficheId);
                List<Long> accessoryIdList = new ArrayList<Long>();
                for (Accessory accessory : accessoryList) {
                    accessoryIdList.add(accessory.getAccessoryId()); // 附件ID
                }
                accessoryService.deleteByIds(accessoryIdList); // 删除附件数据库记录
                afficheMapper.delete(afficheId);
            }

            String optContent = "删除的公告ID：" + afficheIdList;
            sysOptLogService.saveLog(LogLevel.THIRD, OptName.DELETE, SYS_AFFICHE, optContent, "");
        }
    }

    public Affiche get(Long afficheId) {
        List<Accessory> accessoryList = accessoryService.findByAfficheId(afficheId);
        accessoryList.forEach(accessory -> {
            accessory.setAccessoryName(StringUtils.getFilename(accessory.getAccessoryName()));
        });
        Affiche affiche = afficheMapper.get(afficheId);
        affiche.setAccessories(accessoryList);

        return affiche;
    }

    public Page<Affiche> findByPage(Map<String, Object> params, Pageable page) {
        return afficheMapper.findByPage(params, page);
    }

    public List<Affiche> findBeforeValidate() {
        return afficheMapper.findBeforeValidate(new Date());
    }

    @Transactional
    public void update(Affiche affiche) {
        afficheMapper.update(affiche);

        for (Accessory acc : affiche.getAccessories()) {
            if (acc.getAccessoryId() != null) {
                continue;
            }

            String newFilename = acc.getAccessoryName();
            Accessory accessory = new Accessory();
            accessory.setInfoId(affiche.getAfficheId());
            accessory.setAccessoryName(newFilename);
            accessoryService.save(accessory);
        }

        StringBuilder optContent = new StringBuilder();
        optContent.append("修改公告ID：").append(affiche.getAfficheId()).append(SPLIT);
        optContent.append("修改公告标题：").append(affiche.getAfficheTitle()).append(SPLIT);
        optContent.append("有效期：").append(affiche.getAfficheInvalidate()).append(SPLIT);
        optContent.append("是否发布：").append(affiche.getAffichePulish()).append(SPLIT);
        optContent.append("修改公告ID：").append(affiche.getAfficheId()).append(SPLIT);
        sysOptLogService.saveLog(LogLevel.SECOND, OptName.MODIFY, SYS_AFFICHE,
                optContent.toString(), affiche.getAfficheId() + "");
    }

    @Transactional
    public void save(Affiche affiche) {
        // 1. 保存公告
        afficheMapper.save(affiche);

        // 2. 保存附件信息
        if (affiche.getAccessories() != null) {
            for (Accessory acc : affiche.getAccessories()) {
                String newFilename = acc.getAccessoryName();
                Accessory accessory = new Accessory();
                accessory.setInfoId(affiche.getAfficheId());
                accessory.setAccessoryName(newFilename);
                accessoryService.save(accessory);
            }
        }

        // 3. 保存操作日志
        StringBuilder optContent = new StringBuilder();
        optContent.append("保存公告标题：").append(affiche.getAfficheTitle()).append(SPLIT);
        optContent.append("有效期限:").append(affiche.getAfficheInvalidate()).append(SPLIT);
        sysOptLogService.saveLog(LogLevel.FIRST, OptName.SAVE, SYS_AFFICHE,
                optContent.toString(), affiche.getAfficheId() + "");
    }
}
