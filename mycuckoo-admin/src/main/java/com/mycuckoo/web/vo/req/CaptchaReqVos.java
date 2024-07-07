package com.mycuckoo.web.vo.req;

import javax.validation.constraints.NotBlank;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/6 10:11
 */
public abstract class CaptchaReqVos {

    public static class GetCode {
        @NotBlank(message = "请通过图片验证码校验才能获取验证码")
        private String captchaId;
        private String phone;
        private String email;

        public String getCaptchaId() {
            return captchaId;
        }

        public void setCaptchaId(String captchaId) {
            this.captchaId = captchaId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}