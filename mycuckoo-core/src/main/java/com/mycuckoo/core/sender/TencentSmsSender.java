package com.mycuckoo.core.sender;

import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.util.StrUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TencentSmsSender implements SmsSender {
    private final TencentSmsProperties properties;
    private final SmsClient client;


    public TencentSmsSender(TencentSmsProperties properties) {
        this.properties = properties;

        HttpProfile profile = new HttpProfile();
        if (properties.getHttpTimeout() != null) {
            profile.setConnTimeout(properties.getHttpTimeout());
        }
        if (properties.getHttpEndpoint() != null) {
            profile.setEndpoint(properties.getHttpEndpoint());
        }
        if (properties.getHttpProxyHost() != null) {
            profile.setProxyHost(properties.getHttpProxyHost());
        }
        if (properties.getHttpProxyPort() != null) {
            profile.setProxyPort(properties.getHttpProxyPort());
        }
        if (properties.getHttpProxyUsername() != null) {
            profile.setProxyUsername(properties.getHttpProxyUsername());
        }
        if (properties.getHttpProxyPassword() != null) {
            profile.setProxyPassword(properties.getHttpProxyPassword());
        }
        if (properties.getHttpMethod() != null) {
            profile.setReqMethod(properties.getHttpMethod());
        }

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(profile);
        if (properties.getSignMethod() != null)
            clientProfile.setSignMethod(properties.getSignMethod());

        if (properties.getSecretId() == null || properties.getSecretKey() == null) {
            throw new MyCuckooException("Secret id or secret key can not be null! (Tencent SMS)");
        }

        this.client = new SmsClient(new Credential(properties.getSecretId(), properties.getSecretKey()), properties.getRegion(), clientProfile);
    }

    @Override
    public void send(List<String> phones, String tpl, Object params, String signName) {
        if (phones == null || phones.isEmpty()) {
            return;
        }
        if (params != null && !(params instanceof String[])) {
            throw new MyCuckooException("Fail to send sms, params must 'String[]' type. (Tencent SMS)");
        }

        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setSmsSdkAppid(getAppId());
            request.setSign(StrUtils.isNotBlank(signName) ? signName : properties.getDefaultSign());
            request.setSenderId(properties.getDefaultSenderId());
            request.setTemplateID(tpl);

            request.setPhoneNumberSet(phones.toArray(new String[] {}));
            request.setTemplateParamSet((String[]) params);

            SendSmsResponse rsp = this.client.SendSms(request);
            SendStatus[] statuses = rsp.getSendStatusSet();
            if (statuses == null) {
                return;
            }

            List<SendStatus> errors = Arrays.stream(statuses).filter(o -> !o.getCode().equals("Ok")).collect(Collectors.toList());
            if (!errors.isEmpty()) {
                throw new MyCuckooException("Fail to send sms. (Tencent SMS) " + this.client.gson.toJson(errors));
            }
        } catch (Exception e) {
            if (e instanceof MyCuckooException) {
                throw (MyCuckooException) e;
            }

            throw new MyCuckooException("Fail to send sms. (Tencent SMS)", e);
        }
    }

    private String getAppId() {
        String key = properties.getDefaultAppId();
        return properties.getAppIds().getOrDefault(key, key);
    }
}
