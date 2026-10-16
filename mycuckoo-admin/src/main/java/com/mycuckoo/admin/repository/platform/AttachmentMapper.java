package com.mycuckoo.admin.repository.platform;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.core.annotation.RowPermission;
import com.mycuckoo.admin.entity.platform.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 附件持久层接口
 *
 * @author rutine
 * @version 5.0.0
 * @time Apr 11, 2026 10:55:00 AM
 */
@RowPermission(table = "sys_attachment")
public interface AttachmentMapper extends Repository<Attachment, Long> {

    /**
     * 根据业务类型、业务子类型和业务ID查询附件关联
     *
     * @param bizType 业务类型
     * @param bizSubType 业务子类型，可为空
     * @param bizId 业务ID
     * @return 附件关联列表
     */
    List<Attachment> findByBizTypeAndBizId(@Param("bizType") Integer bizType,
                                           @Param("bizSubType") Integer bizSubType,
                                           @Param("bizId") String bizId);

    /**
     * 根据业务类型、业务子类型和业务ID删除附件关联
     *
     * @param bizType 业务类型
     * @param bizSubType 业务子类型，可为空
     * @param bizId 业务ID
     */
    void deleteByBizTypeAndBizId(@Param("bizType") Integer bizType,
                                 @Param("bizSubType") Integer bizSubType,
                                 @Param("bizId") String bizId);

}
