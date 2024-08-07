package com.mycuckoo.domain.uum;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 10:48:50 AM
 */
public class Role extends BasicDomain<Long> {

    private Long roleId;    //角色ID
    private String name;    //角色名称
    private Short level;    //角色级别
    private String status;        //角色状态
    private String memo;        //备注
    private String regDefault;  //注册默认

    /**
     * default constructor
     */
    public Role() {
    }

    /**
     * minimal constructor
     */
    public Role(Long roleId, String status) {
        this.roleId = roleId;
        this.status = status;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? name : name.trim();
    }

    public Short getLevel() {
        return level;
    }

    public void setLevel(Short level) {
        this.level = level;
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

    public String getRegDefault() {
        return regDefault;
    }

    public void setRegDefault(String regDefault) {
        this.regDefault = regDefault == null ? regDefault : regDefault.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // quict check
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Role uumRole = (Role) obj;
        if (roleId != null && uumRole.getRoleId() != null &&
                roleId.longValue() == uumRole.getRoleId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (roleId == null ? 0 : roleId.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
