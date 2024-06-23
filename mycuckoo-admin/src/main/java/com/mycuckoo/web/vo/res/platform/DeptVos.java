package com.mycuckoo.web.vo.res.platform;

import com.mycuckoo.core.OrderTree;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/16 7:33
 */
public abstract class DeptVos {

    public static class Tree extends OrderTree {
        private String roleName;
        private String status; //状态

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
