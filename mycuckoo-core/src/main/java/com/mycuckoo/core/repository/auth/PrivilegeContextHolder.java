package com.mycuckoo.core.repository.auth;

import org.springframework.core.NamedThreadLocal;

import javax.annotation.Nonnull;

/**
 * 功能说明: 权限上下文工具
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:19
 */
public abstract class PrivilegeContextHolder {
    private static final ThreadLocal<PrivilegeInfo> contextHolder = new NamedThreadLocal<>("Privilege Context");


    public static void set(@Nonnull PrivilegeInfo info){
        contextHolder.set(info);
    }

    public static PrivilegeInfo get(){
        return contextHolder.get();
    }

    //移除掉原先的所有变量
    public static void clear() {
        contextHolder.remove();
    }

    /**
     * 跳过数据权限过滤
     */
    public static void skip() {
        PrivilegeInfo info = get();
        if (info != null) {
            info.setSkip(true);
        }
    }
}
