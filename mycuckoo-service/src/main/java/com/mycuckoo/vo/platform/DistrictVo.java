package com.mycuckoo.vo.platform;

import com.mycuckoo.domain.platform.District;

/**
 * 功能说明: 地区vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 5:49:05 PM
 */
public class DistrictVo extends District {
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
