package com.mycuckoo.web.vo.res.platform;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/6 10:10
 */
public abstract class CaptchaVos {

    public static class Captcha {
        private String id;
        private String code;

        public Captcha() {}

        public Captcha(String id, String code) {
            this.id = id;
            this.code = code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
