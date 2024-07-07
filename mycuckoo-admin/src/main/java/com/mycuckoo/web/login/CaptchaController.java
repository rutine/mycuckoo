package com.mycuckoo.web.login;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.service.login.CaptchaService;
import com.mycuckoo.web.vo.req.CaptchaReqVos;
import com.mycuckoo.web.vo.res.platform.CaptchaVos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/6 10:20
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;



    @PostMapping("/image")
    public AjaxResponse<CaptchaVos.Captcha> image() {
        return AjaxResponse.create(captchaService.send());
    }

    @PostMapping("/code")
    public AjaxResponse<CaptchaVos.Captcha> code(@RequestBody CaptchaReqVos.GetCode vo) {
        return AjaxResponse.create(captchaService.send(vo.getCaptchaId(), vo.getPhone(), vo.getEmail()));
    }

    @PostMapping("/check")
    public AjaxResponse<CaptchaVos.Captcha> check(@RequestBody CaptchaVos.Captcha vo) {
        try {
            captchaService.validate(vo.getId(), vo.getCode());
        } catch (IllegalStateException e) {
            return AjaxResponse.create(1, e.getMessage());
        }

        return AjaxResponse.success("校验通过");
    }
}
