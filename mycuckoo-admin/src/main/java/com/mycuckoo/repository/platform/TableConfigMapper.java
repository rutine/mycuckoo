package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.domain.platform.TableConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/1 10:36
 */
public interface TableConfigMapper extends Repository<TableConfig, Long> {
    int batchInsert(@Param("list") List<TableConfig> list);

    List<TableConfig> findByModuleId(Long moduleId);

    List<TableConfig> findByTableCode(String tableCode);

    int deleteByIds(@Param("list") List<Long> list);
}
