package com.mycuckoo.web.vo.res.platform;

import com.mycuckoo.core.OrderTree;

import java.util.Date;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/31 21:09
 */
public class ModuleMenuVos {

    public static class Tree extends OrderTree {
        private Boolean isParent;
        private String code; //模块编码
        private String name; //模块名称
        private String iconCls; //模块图片样式
        private Integer level; //模块级别
        private String belongSys;//系统归属
        private String pageType;//页面类型
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIconCls() {
            return iconCls;
        }

        public void setIconCls(String iconCls) {
            this.iconCls = iconCls;
        }

        public String getBelongSys() {
            return belongSys;
        }

        public void setBelongSys(String belongSys) {
            this.belongSys = belongSys;
        }

        public String getPageType() {
            return pageType;
        }

        public void setPageType(String pageType) {
            this.pageType = pageType;
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
