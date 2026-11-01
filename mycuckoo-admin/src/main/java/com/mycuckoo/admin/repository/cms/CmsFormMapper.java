package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.admin.entity.cms.CmsForm;
import com.mycuckoo.core.repository.Repository;
import org.apache.ibatis.annotations.Param;

public interface CmsFormMapper extends Repository<CmsForm, Long> {
    CmsForm getByCode(@Param("code") String code);
}