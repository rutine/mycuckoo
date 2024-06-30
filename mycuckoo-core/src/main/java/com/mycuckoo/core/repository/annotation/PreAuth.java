package com.mycuckoo.core.repository.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {
    //表名
    String table();
    //别名
    String alias() default "";
    //租户字段(组织)
    String tenant() default "org_id";
    //用户字段
    String user() default "user_id";
    //行权限
    Row row() default Row.TENANT;


    enum Row {
        NONE(0),
        TENANT(1),
        USER(3); // 1 | 2, 租户和用户

       int value;
       Row(int value) {
           this.value = value;
       }

        public int getValue() {
            return value;
        }
    }
}