package com.mycuckoo.web.vo.res.uum;

import com.mycuckoo.domain.uum.Organ;

import java.util.Date;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/5 21:13
 */
public abstract class OrganVos {

    public static class ListVo {
        private Long orgId;             // 主键
        private Long roleId;
        private String code;            // 机构代码
        private String simpleName;      // 机构简称
        private String address1;        // 联系地址1
        private String tel1;            // 联系电话1
        private Date beginDate;         // 成立日期
        private Long belongDist;        // 所属地区
        private String status;          // 机构状态
        private String roleName;        // 角色名称

        public Long getOrgId() {
            return orgId;
        }

        public void setOrgId(Long orgId) {
            this.orgId = orgId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getTel1() {
            return tel1;
        }

        public void setTel1(String tel1) {
            this.tel1 = tel1;
        }

        public Date getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(Date beginDate) {
            this.beginDate = beginDate;
        }

        public Long getBelongDist() {
            return belongDist;
        }

        public void setBelongDist(Long belongDist) {
            this.belongDist = belongDist;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }

    public static class Detail extends Organ {
        private String parentName;            //上级机构
        private String belongDistName;         // 所属地区

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getBelongDistName() {
            return belongDistName;
        }

        public void setBelongDistName(String belongDistName) {
            this.belongDistName = belongDistName;
        }
    }
}
