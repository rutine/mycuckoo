package com.mycuckoo.web.vo.res.uum;

public class UserVos {

    public static class Profile {
        private Long userId;
        private String name;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
