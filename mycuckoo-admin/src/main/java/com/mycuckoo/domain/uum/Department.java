package com.mycuckoo.domain.uum;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 4.1.0
 * @time May 19, 2024 11:29:53 AM
 */
public class Department extends BasicDomain<Long> {

    private Long deptId;            // 主键
    private Long parentId;          // 上一级
    private String treeId;
    private Long roleId;
    private Integer level;
    private String code;            // 代码
    private String name;            // 名称
    private String status;            // 状态
    private String memo;            // 备注

    /**
     * default constructor
     */
    public Department() {
    }

    /**
     * minimal constructor
     */
    public Department(Long deptId, String status) {
        this.deptId = deptId;
        this.status = status;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId == null ? treeId : treeId.trim();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? code : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? name : name.trim();
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status == null ? status : status.trim();
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? memo : memo.trim();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;    // quict check
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Department uumOrgan = (Department) obj;
        if (orgId != null && uumOrgan.getDeptId() != null &&
                orgId.longValue() == uumOrgan.getDeptId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (orgId == null ? 0 : orgId.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
