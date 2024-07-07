MyCuckoo
========

用户权限管理平台, 开发环境java8 spring-boot mybatis

# 前述
1. 此项是笔记, 以精小完整的工程记录遇到的事, 学到的经, 收集点滴
2. 此项是坚持, 初有萌芽, 接而立项, 后有执行, 过程曲折, 枯燥乏味, 需心静不变的坚持
3. 此项是态度, 不畏事小, 不厌反复, 没有坚持不会有果

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