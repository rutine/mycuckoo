MyCuckoo
========

用户权限管理平台, 开发环境java8 spring-boot mybatis

# 前述 
权限控制是每个管理系统必须且基础的功能。如何更好的设计，达到便利操作和管理，也是较为困难的一件事。  

实现的基本原理跟大多其他权限系统没有太大区别。要是非得说上有那么不一样，表现得有更好改进的地方。

我认为，本权限系统对菜单,按钮,接口资源做了独立拆分，结构清晰，自成管理功能。例如一个菜单作为一项可授权的功能，
每个菜单需聚集多个接口资源才能成为一个可用的功能。菜单之间都可能存在一个或多个接口资源的交集，那么接口资源作为
独立管理对象就成了很有必要了。所以这里对接口资源作为单项功能进行管理，当然也便于维护和扩展


# 历史 
4.1.0 版本较之前版本，数据结构和设计实现做了较大的调整。4.0.0 之前版本仅是按钮操作的**伪**控制。
之前存在**VUE**的**UI**实现版本，后面放弃维护了，当前版本完全不兼容。

# 特性 
1. 核心模块 `core` 提供基础工具，`admin` 提供web服务
2. 分页插件 `PageIntercepter` 实现 `SQL` 增强，动态组装请求过滤参数, 通过 `@PreAuth` 注解完成数据行权限过滤
3. SQL增强器 `SqlEnhance`, 实现请求参数过滤和行数据权限自动组装, 构建新的查询SQL
4. 记录操作日志, 每个操作动作都有迹可循
5. 基于资源的权限过滤器 `PrivilegeFilterr`, 每一个接口都是资源，都有自己的标识, 实现精确控制; 前端页面基于标识实现按钮显示
6. 全局异常处理 `MycuckooExceptionHandler`, 统一异常处理
7. MySQL数据库, 数据库: cuckoo, 附带结构和数据SQL脚本
8. 搭配前端ui: mycuckoo-ui, 实现前后端分离

# 安装流程 
## 源码方式 
1. 下载mycuckoo、mycuckoo-ui两个项目源码  
   `git clone https://github.com/rutine/mycuckoo.git`  
   `git clone https://github.com/rutine/mycuckoo-ui.git`
2. 安装MySQL(请自行网上查找安装教程), 创建数据库账号: cuckoo/123456, 新建数据库: cuckoo
3. 第一先导入`cuckoo-schema.sql`SQL脚本
4. 第二再导入`cuckoo-repair.sql`脚本, 注意跟上面步骤顺序
5. 启动mycuckoo, 执行主类是`Mycuckoo` `main`方法, 默认端口: 8080
6. 打开mycuckoo-ui前端项目, 修改`static/mycuckoo.api.js`文件`host`指向后台接口地址,  
   默认指向: http://localhost:8080
7. 浏览器输入: http://localhost:8080/login.html, 账号密码: admin/123456


# 示例: 

登录页:
![登录页](demo/login.png)

主页:
![主页](demo/index.png)

菜单管理:
![菜单管理](demo/menuMgr.png)

系统配置:
![系统配置](demo/systemConfig.png)