package com.mycuckoo.service.login;

import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.core.util.IdGenerator;
import com.mycuckoo.domain.uum.Captcha;
import com.mycuckoo.repository.uum.CaptchaMapper;
import com.mycuckoo.web.vo.res.platform.CaptchaVos;
import com.ramostear.captcha.support.CaptchaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.awt.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/7/6 10:13
 */
@Service
public class CaptchaService {
    private SecureRandom random = new SecureRandom();

    @Autowired
    private CaptchaMapper captchaMapper;


    public CaptchaVos.Captcha send() {
        com.ramostear.captcha.core.Captcha captcha = new com.ramostear.captcha.core.Captcha();
        captcha.setType(CaptchaType.values()[random.nextInt(CaptchaType.values().length)]);
        captcha.setWidth(160);
        captcha.setHeight(50);
        captcha.setLength(4);
        captcha.setFont(new Font("Fixedsys", Font.BOLD, 24));

        //先生成图, 获得验证码
        String img = captcha.toBase64();
        String captchaId = IdGenerator.uuid();

        Captcha entity = new Captcha();
        entity.setId(captchaId);
        entity.setType("img");
        entity.setCode(captcha.getCode());
        entity.setStatus(0);
        entity.setExpireTime(LocalDateTime.now().plusMinutes(5));
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        captchaMapper.save(entity);

        return  new CaptchaVos.Captcha(captchaId, img);
    }

    public CaptchaVos.Captcha send(String imgCaptchaId, String phone, String email) {
        Assert.state(StrUtils.isNotBlank(imgCaptchaId), "参数有误!");
        Assert.state(StrUtils.isNotBlank(phone) || StrUtils.isNotBlank(email), "手机或邮箱不能为空");

        Captcha old = captchaMapper.get(imgCaptchaId);
        Assert.state(old != null && old.getCode() != null, "图片验证码不存在!");
        Assert.state(old.getExpireTime().isAfter(LocalDateTime.now()), "图片验证码已失效, 重新获取!");
        Assert.state(old.getStatus() > 0, "图片验证码未校验");

        String type = null;
        if (StrUtils.isNotBlank(phone)) {
            type = "sms";
        } else {
            type = "email";
        }

        String captchaId = IdGenerator.uuid();

        Captcha entity = new Captcha();
        entity.setId(captchaId);
        entity.setType(type);
        entity.setCode(IdGenerator.randomNum(4));
        entity.setStatus(0);
        entity.setExpireTime(LocalDateTime.now().plusMinutes(5));
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        captchaMapper.save(entity);

        return new CaptchaVos.Captcha(captchaId, entity.getCode());
    }

    public void validate(String captchaId, String code) {
        Assert.state(StrUtils.isNotBlank(captchaId), "参数有误!");
        Assert.state(StrUtils.isNotBlank(code), "请输入正确验证码!");

        Captcha old = captchaMapper.get(captchaId);
        Assert.state(old != null && old.getCode() != null, "验证码不存在!");
        Assert.state(old.getStatus() == 0, "验证码已使用!");
        Assert.state(old.getExpireTime().isAfter(LocalDateTime.now()), "验证码已失效, 重新获取!");
        Assert.state(old.getCode().equals(code), "验证码错误!");

        Captcha entity = new Captcha();
        entity.setId(captchaId);
        entity.setStatus(1);
        entity.setUpdateTime(LocalDateTime.now());
        captchaMapper.update(entity);
    }
}
