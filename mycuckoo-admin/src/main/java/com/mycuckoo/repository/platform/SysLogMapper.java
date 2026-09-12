package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.core.repository.annotation.PreAuth;
import com.mycuckoo.domain.platform.SysLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 功能说明: 系统日志持久层接口
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:00
 */
@PreAuth(table = "sys_log")
public interface SysLogMapper extends Repository<SysLog, Long> {

    /**
     * 删除指定时间之前的日志
     *
     * @param actionTime 保留截止时间
     */
    void deleteLogger(@Param("optTime") Date optTime);

    /**
     * 根据日志ID获取日志内容
     *
     * @param logId 日志ID
     * @return 日志内容
     */
    String getContentById(@Param("logId") long logId);
}