package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.admin.entity.cms.CmsFormVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsFormVersionMapper {
    int save(CmsFormVersion version);

    CmsFormVersion get(@Param("formId") Long formId, @Param("version") Integer version);

    List<CmsFormVersion> listByFormId(@Param("formId") Long formId);

    int deleteByFormId(@Param("formId") Long formId);

    CmsFormVersion getByVersion(@Param("formId") Long formId, @Param("version") Integer version);
}