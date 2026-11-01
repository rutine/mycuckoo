package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.admin.entity.cms.CmsFormData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsFormDataMapper extends Repository<CmsFormData, Long> {
    List<CmsFormData> findByPage(@Param("formCode") String formCode, @Param("bizId") String bizId);

    List<CmsFormData> listByCursor(@Param("formCode") String formCode,
                                   @Param("lastId") Long lastId,
                                   @Param("pageSize") Integer pageSize);

    int renameField(@Param("formCode") String formCode,
                    @Param("oldField") String oldField,
                    @Param("newField") String newField);

    CmsFormData getByBizId(@Param("formCode") String formCode, @Param("bizId") String bizId);
}