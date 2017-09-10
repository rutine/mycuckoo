mycuckoo
========

用户权限管理平台, 开发环境java8 spring-boot mybatis

# 特性 #
1. 拆分三个模块: repository service web
2. 分页插件`PageIntercepter`直接返回分页对象`Page`
3. 基于`NamedThreadLocal`实现的会话工具类`SessionUtil`, 方便获取用户会话信息
4. 记录`Request`操作日志
5. 登录拦截器`LoginInteceptor`, 控制权限访问, 跨域请求
6. 全局异常建议`GlobalControllerExceptionHandler`, 统一异常处理和响应格式
7. RESTFul接口