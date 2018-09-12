package com.mycuckoo.vo.uum;

import com.mycuckoo.domain.uum.Organ;

/**
 * 功能说明: 机构vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 5:50:31 PM
 */
public class OrganVo extends Organ {
    private String parentName;            //上级机构
    private String dataIconCls;
    private String orgBelongDistName;    // 所属地区
    private boolean isLeaf;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDataIconCls() {
        return dataIconCls;
    }

    public void setDataIconCls(String dataIconCls) {
        this.dataIconCls = dataIconCls;
    }

    public String getOrgBelongDistName() {
        return orgBelongDistName;
    }

    public void setOrgBelongDistName(String orgBelongDistName) {
        this.orgBelongDistName = orgBelongDistName;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
}
