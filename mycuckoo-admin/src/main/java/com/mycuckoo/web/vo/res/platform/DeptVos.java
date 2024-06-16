package com.mycuckoo.web.vo.res.platform;

import com.mycuckoo.core.OrderTree;

import java.time.LocalDateTime;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/16 7:33
 */
public abstract class DeptVos {

    public static class Tree extends OrderTree {
        private Integer level;
        private String memo; //备注
        private String status; //状态
        private String creator; //创建人
        private LocalDateTime createTime; //创建时间

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
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

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }
}
