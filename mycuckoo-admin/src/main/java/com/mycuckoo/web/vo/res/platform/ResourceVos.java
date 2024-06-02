package com.mycuckoo.web.vo.res.platform;

import com.mycuckoo.core.OrderTree;

import java.util.Date;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/1 6:38
 */
public abstract class ResourceVos {
    public static class Tree extends OrderTree {
        private Boolean isParent;
        private Integer level;
        private String code; //操作编码
        private String method; //请求方法
        private String path; //请求路径
        private Integer group;
        private String memo; //备注
        private String status; //状态
        private String creator; //创建人
        private Date createDate; //创建时间

        public Boolean getIsParent() {
            return isParent;
        }

        public void setIsParent(Boolean isParent) {
            this.isParent = isParent;
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
            this.code = code;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getGroup() {
            return group;
        }

        public void setGroup(Integer group) {
            this.group = group;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }
    }

}
