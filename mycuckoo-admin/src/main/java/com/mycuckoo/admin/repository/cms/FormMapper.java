package com.mycuckoo.admin.repository.cms;

import com.mycuckoo.admin.entity.cms.Form;
import com.mycuckoo.core.annotation.RowPermission;
import com.mycuckoo.core.repository.Repository;
import org.apache.ibatis.annotations.Param;

@RowPermission(table = "cms_form")
public interface FormMapper extends Repository<Form, Long> {
    Form getByBizType(@Param("bizType") String bizType);
}