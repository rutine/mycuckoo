package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.admin.entity.cms.FormData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormDataMapper extends Repository<FormData, Long> {
    List<FormData> findByPage(@Param("formCode") String formCode, @Param("bizId") String bizId);

    List<FormData> listByCursor(@Param("formCode") String formCode,
                                @Param("lastId") Long lastId,
                                @Param("pageSize") Integer pageSize);

    int renameField(@Param("formCode") String formCode,
                    @Param("oldField") String oldField,
                    @Param("newField") String newField);

    FormData getByBizId(@Param("formCode") String formCode, @Param("bizId") String bizId);
}