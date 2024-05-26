package com.mycuckoo.core.repository.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuth {
    //表名
    String table();
    //别名
    String alias() default "";
    //字段
    String column() default "user_id";
    //属性
    String field() default "id";
}