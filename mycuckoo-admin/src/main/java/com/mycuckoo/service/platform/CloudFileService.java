package com.mycuckoo.service.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.FileUtils;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.platform.CloudFile;
import com.mycuckoo.repository.platform.CloudFileMapper;
import com.mycuckoo.web.config.WebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能说明: 云文件业务类
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 11, 2026 9:35:00 AM
 */
@Service
@Transactional(readOnly = true)
public class CloudFileService {

    @Autowired
    private WebProperties properties;

    @Autowired
    private CloudFileMapper cloudFileMapper;


    @Transactional
    public void deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ids.forEach(cloudFileMapper::delete);
        for (String id : ids) {
            CloudFile entity = this.get(id);
            if (entity == null) {
                continue;
            }
            String filePath = StringUtils.cleanPath(properties.getUploadPath()) + entity.getPath();
            FileUtils.delete(filePath, entity.getName());
        }
    }

    public CloudFile get(String id) {
        return cloudFileMapper.get(id);
    }

    public String getUrl(String id) {
        CloudFile entity = this.get(id);
        if (entity == null) {
            return null;
        }

        return getUrlByPath(entity.getPath());
    }

    public String getUrlByPath(String path) {
        return path == null ? null : properties.getHost() + "/download" + path;
    }

    public Page<CloudFile> findByPage(Querier querier) {
        return cloudFileMapper.findByPage(querier.getQ(), querier);
    }

    @Transactional
    public void save(CloudFile entity) {
        entity.setCreator(String.valueOf(SessionContextHolder.getUserId()));
        entity.setCreateTime(LocalDateTime.now());
        cloudFileMapper.save(entity);
    }
}
