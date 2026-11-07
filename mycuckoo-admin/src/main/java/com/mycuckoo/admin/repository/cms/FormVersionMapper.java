package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.admin.entity.cms.FormVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormVersionMapper {
    int save(FormVersion version);

    FormVersion get(@Param("formId") Long formId, @Param("version") Integer version);

    List<FormVersion> listByFormId(@Param("formId") Long formId);

    int deleteByFormId(@Param("formId") Long formId);

    FormVersion getByVersion(@Param("formId") Long formId, @Param("version") Integer version);
}