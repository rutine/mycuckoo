CREATE SCHEMA IF NOT EXISTS CUCKOO;

SET SCHEMA CUCKOO;


SET FOREIGN_KEY_CHECKS=0;


--
-- Table structure for table `sys_accessory`
--
DROP TABLE IF EXISTS `sys_accessory`;
CREATE TABLE `sys_accessory` (
  `accessory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `info_id` bigint DEFAULT NULL COMMENT '公告ID',
  `accessory_name` varchar(100) DEFAULT NULL COMMENT '附件名称',
  PRIMARY KEY (`accessory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告附件表';

INSERT INTO `sys_accessory` VALUES (2,2,'cRopIJ_blog_fifth.bmp'),(3,2,'jba458_blog_second.bmp'),(4,2,'b6wewK_blog_second.bmp'),(7,3,'Ihwp1f_blog_fourth.bmp'),(12,12,'ceshi.jpg'),(15,13,'ceshi.jpg');

--
-- Table structure for table `sys_affiche`
--
DROP TABLE IF EXISTS `sys_affiche`;
CREATE TABLE `sys_affiche` (
  `affiche_id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) DEFAULT '' COMMENT '公告标题',
  `invalidate` date DEFAULT NULL COMMENT '公告有效期',
  `publish` tinyint DEFAULT NULL COMMENT '是否发布',
  `content` text COMMENT '公告内容',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`affiche_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

INSERT INTO `sys_affiche` VALUES (6,'第二版本发布了！','2017-05-01',0,'<u>​啊啊啊啊啊啊啊啊</u>','0',NULL),(12,'2.0版本','2017-11-26',0,'测试数据','0',NULL),(13,'4.1.0版本即将发布','2024-09-10',1,'','0','2024-06-21 14:37:39');

--
-- Table structure for table `sys_code`
--
DROP TABLE IF EXISTS `sys_code`;
CREATE TABLE `sys_code` (
  `code_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) DEFAULT NULL COMMENT '英文编码',
  `name` varchar(20) DEFAULT NULL COMMENT '中文编码名称',
  `module_name` varchar(20) DEFAULT NULL COMMENT '模块名称',
  `delimiter` varchar(2) DEFAULT NULL COMMENT 'delimite分隔符',
  `part_num` int DEFAULT NULL COMMENT '段数',
  `part1` varchar(20) DEFAULT NULL,
  `part1_con` varchar(20) DEFAULT NULL,
  `part2` varchar(20) DEFAULT NULL,
  `part2_con` varchar(20) DEFAULT NULL,
  `part3` varchar(20) DEFAULT NULL,
  `part3_con` varchar(20) DEFAULT NULL,
  `part4` varchar(20) DEFAULT NULL,
  `part4_con` varchar(20) DEFAULT NULL,
  `effect` varchar(50) DEFAULT NULL COMMENT '编码效果',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统编码表';

INSERT INTO `sys_code` VALUES (25,'RKD','入库单','用户信息管理',':',4,'char','RKD','date','yyyyMM','number','001','sysPara','userName','RKD:202405:001:zhangsan','enable','','0','2024-06-20 06:47:21','0','2012-01-08 16:40:01');

--
-- Table structure for table `sys_dict_big_type`
--
DROP TABLE IF EXISTS `sys_dict_big_type`;
CREATE TABLE `sys_dict_big_type` (
  `big_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(15) DEFAULT NULL COMMENT '字典大类号',
  `name` varchar(20) DEFAULT NULL COMMENT '字典大类名',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`big_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典大类表';

INSERT INTO `sys_dict_big_type` VALUES (21,'system_type','系统类别','enable','','0','2024-06-02 05:36:04','0','2011-12-20 14:32:22'),(22,'district','地区级别','enable','','0',NULL,'0','2011-12-20 15:11:30'),(23,'org_type','机构类型','enable','','0','2024-06-20 09:20:04','0','2011-12-21 09:33:12'),(25,'mod_page_type','页面类型','enable','','0','2024-06-02 05:35:20','0','2012-01-13 12:09:37'),(26,'system_level','级别(公用)','enable','','0','2024-06-20 07:05:23','0','2024-06-02 05:30:12'),(27,'system_status','状态(公用)','enable','','0','2024-06-02 05:40:57','0','2024-06-02 05:40:57');

--
-- Table structure for table `sys_dict_small_type`
--
DROP TABLE IF EXISTS `sys_dict_small_type`;
CREATE TABLE `sys_dict_small_type` (
  `small_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `big_type_id` bigint NOT NULL COMMENT '字典大类ID',
  `code` varchar(15) DEFAULT NULL COMMENT '字典小类号',
  `name` varchar(20) DEFAULT NULL COMMENT '字典小类名',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`small_type_id`),
  KEY `dict_idx_big_type_id` (`big_type_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_big_type_id_reference_sys_dict` FOREIGN KEY (`big_type_id`) REFERENCES `sys_dict_big_type` (`big_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典小类表';

INSERT INTO `sys_dict_small_type` VALUES (37,21,'platform','系统平台','0','2024-06-02 05:36:04'),(38,21,'uum','统一用户','0','2024-06-02 05:36:04'),(47,23,'branch','分公司','0','2024-06-20 09:20:04'),(48,23,'store','门店','0','2024-06-20 09:20:04'),(49,23,'department','部门','0','2024-06-20 09:20:04'),(50,22,'province','省','0',NULL),(51,22,'city','市','0',NULL),(52,22,'town','县','0',NULL),(53,22,'district','区','0',NULL),(69,25,'html','html','0','2024-06-02 05:35:20'),(70,25,'jsp','jsp','0','2024-06-02 05:35:20'),(71,25,'js','js','0','2024-06-02 05:35:20'),(72,26,'1','第一','0','2024-06-20 07:05:23'),(73,26,'2','第二','0','2024-06-20 07:05:23'),(74,26,'3','第三','0','2024-06-20 07:05:23'),(75,26,'4','第四','0','2024-06-20 07:05:23'),(76,27,'enable','启用','0','2024-06-02 05:40:57'),(77,27,'disable','禁用','0','2024-06-02 05:40:57');

--
-- Table structure for table `sys_district`
--
DROP TABLE IF EXISTS `sys_district`;
CREATE TABLE `sys_district` (
  `district_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '地区名称',
  `code` varchar(20) DEFAULT NULL COMMENT '地区编码',
  `postal` varchar(6) DEFAULT NULL COMMENT '邮政',
  `telcode` varchar(10) DEFAULT NULL,
  `level` varchar(20) DEFAULT NULL COMMENT '地区级别',
  `parent_id` bigint DEFAULT NULL COMMENT '上级ID',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`district_id`),
  KEY `dist_idx_name` (`name`),
  KEY `dist_idx_parent_id` (`parent_id`),
  CONSTRAINT `fk_parent_id_reference_sys_dist` FOREIGN KEY (`parent_id`) REFERENCES `sys_district` (`district_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地区表';

INSERT INTO `sys_district` VALUES (0,'地区树','0',NULL,NULL,NULL,0,'enable',NULL,NULL,NULL,NULL,NULL),(2000,'北京','beijing','','','province',0,'enable','',NULL,NULL,'0','2011-12-20 15:12:04'),(2001,'北海','beihai','','','town',2000,'enable','',NULL,NULL,'0','2011-12-26 09:55:00'),(2002,'海淀','haiding','','','town',2000,'enable','',NULL,NULL,'0','2012-01-06 16:11:32'),(2003,'上海','','','','province',0,'enable','',NULL,NULL,'0','2012-03-06 14:38:46'),(2004,'天津','','','','city',0,'enable','',NULL,NULL,'0','2012-03-06 14:39:22'),(2005,'深圳','','','','city',0,'enable','',NULL,NULL,'0','2012-03-06 14:39:37'),(2006,'广州','6008','020','020','province',0,'enable','',NULL,NULL,'0','2014-10-18 11:40:29');

--
-- Table structure for table `sys_module_menu`
--
DROP TABLE IF EXISTS `sys_module_menu`;
CREATE TABLE `sys_module_menu` (
  `module_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint DEFAULT NULL COMMENT '上级模块ID',
  `code` varchar(40) DEFAULT NULL COMMENT '模块编码',
  `name` varchar(10) DEFAULT NULL COMMENT '模块名称',
  `icon_cls` varchar(30) DEFAULT NULL COMMENT '模块图标',
  `level` varchar(2) DEFAULT NULL COMMENT '模块级别',
  `order` int DEFAULT NULL COMMENT '模块顺序',
  `belong_sys` varchar(15) DEFAULT NULL COMMENT '系统归属',
  `page_type` varchar(20) DEFAULT NULL COMMENT '页面类型',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`module_id`),
  KEY `menu_idx_name` (`name`),
  KEY `menu_idx_parent_id` (`parent_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_parent_id_reference_sys_memu` FOREIGN KEY (`parent_id`) REFERENCES `sys_module_menu` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块菜单表';

INSERT INTO `sys_module_menu` VALUES (0,0,'moduleTree','模块树','module-tree-icon','0',1,NULL,NULL,'enable','',NULL,NULL,'0','2010-07-27 09:29:36'),(1,0,'platform','系统平台','platform-icon','1',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 13:43:34'),(2,0,'uum','统一用户','uum-icon','1',2,'uum','html','enable','',NULL,NULL,'0','2010-08-20 13:44:29'),(6,1,'moduleOptMgr','菜单管理','module-operate-icon','2',3,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:20:15'),(7,6,'moduleMgr','菜单管理','module-mgr-icon','3',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:21:50'),(8,6,'operateMgr','按钮管理','operate-mgr-icon','3',2,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:22:45'),(9,1,'dictionaryMgr','字典管理','dictionary-mgr-icon','2',4,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:27:12'),(10,9,'dictionaryMgr','字典管理','type-dictionary-mgr-icon','3',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:30:13'),(12,1,'systemMgr','系统管理','system-mgr-icon','2',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:32:28'),(13,12,'codeMgr','系统编码管理','code-mgr-icon','3',2,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:33:31'),(14,52,'districtMgr','省市地区管理','district-mgr-icon','3',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:34:52'),(15,12,'systemParameterMgr','系统参数管理','system-parameter-mgr-icon','3',3,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:35:39'),(17,1,'logMgr','日志管理','log-mgr-icon','2',5,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:38:46'),(18,17,'systemLogMgr','系统日志管理','system-log-mgr-icon','3',1,'platform','html','enable','',NULL,NULL,'0','2010-08-20 14:39:33'),(19,2,'organMgrTitle','机构管理','organ-mgr-title-icon','2',1,'uum','html','enable','',NULL,NULL,'0','2010-08-21 13:33:29'),(21,2,'roleMgr','角色管理','role-mgr-title-icon','2',2,'uum','html','enable','',NULL,NULL,'0','2010-09-13 21:04:30'),(22,2,'userMgr','用户管理','user-mgr-title-icon','2',3,'uum','html','enable','',NULL,NULL,'0','2010-09-13 21:05:20'),(25,12,'systemConfigMgr','系统配置管理','system-config-mgr-icon','3',1,'platform','html','enable','',NULL,NULL,'0','2010-09-13 21:09:13'),(32,19,'organMgr','机构信息管理','organ-mgr-icon','3',1,'uum','html','enable','','0','2024-06-20 02:10:29','0','2010-09-13 21:14:44'),(33,21,'roleMgr','角色信息管理','role-mgr-icon','3',1,'uum','html','enable','',NULL,NULL,'0','2010-09-13 21:15:10'),(34,22,'userMgr','用户信息管理','user-mgr-icon','3',2,'uum','html','enable','',NULL,NULL,'0','2010-09-13 21:16:03'),(49,12,'afficheMgr','系统公告管理','affiche-mgr-icon','3',4,'platform','html','enable','',NULL,NULL,'0','2010-12-01 10:47:27'),(52,1,'districtMgr','省市地区','district-mgr-title-icon','2',2,'platform','html','enable','',NULL,NULL,'0','2011-01-17 10:16:14'),(53,12,'schedulerMgr','系统调度管理','scheduler-mgr-icon','3',5,'platform','html','enable','',NULL,NULL,'0','2011-01-17 10:34:43'),(55,6,'resourceMgr','资源管理','','3',3,'platform','html','enable','',NULL,NULL,'0','2024-05-05 09:27:17'),(56,22,'deptMgr','部门信息管理','','3',1,'uum','html','enable','',NULL,NULL,'0','2024-05-19 07:18:05');

--
-- Table structure for table `sys_operate`
--
DROP TABLE IF EXISTS `sys_operate`;
CREATE TABLE `sys_operate` (
  `operate_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) DEFAULT NULL COMMENT '操作编码',
  `name` varchar(10) DEFAULT NULL COMMENT '操作名',
  `icon_cls` varchar(20) DEFAULT NULL COMMENT '图标',
  `order` int DEFAULT NULL COMMENT '顺序',
  `group` int DEFAULT NULL COMMENT '所属分组',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`operate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作按钮表';

INSERT INTO `sys_operate` VALUES (1,'add','增加','table-add-icon',1,1,'enable','增加',NULL,NULL,'0','2010-10-28 15:16:25'),(2,'modify','修改','table-edit-icon',2,1,'enable','',NULL,NULL,'0','2010-10-28 15:27:36'),(3,'delete','删除','table-delete-icon',3,1,'enable','',NULL,NULL,'0','2010-10-28 15:30:29'),(4,'view','查看','table-icon',4,1,'enable','',NULL,NULL,'0','2010-10-28 19:07:11'),(5,'enable','启用','enable-icon',5,1,'enable','',NULL,NULL,'0','2010-10-29 09:01:43'),(6,'disable','停用','disable-icon',6,1,'enable','停用',NULL,NULL,'0','2010-10-29 09:02:11'),(7,'assignOpt','分配操作','assign-icon',7,1,'enable','','0','2024-06-16 12:42:06','0','2010-11-01 20:07:40'),(8,'list','列表','list-icon',8,1,'enable','',NULL,NULL,'0','2010-11-02 11:42:42'),(9,'assign','分配','assign-icon',6,1,'enable','',NULL,NULL,'0','2010-11-02 11:44:10'),(10,'selector','选择器','selector-icon',7,1,'enable','',NULL,NULL,'0','2010-11-02 11:44:39'),(11,'tree','树形视图','role-mgr-icon|tree',6,1,'enable','',NULL,NULL,'0','2010-11-02 11:46:31'),(12,'rowprivilege','行权限(删除)','privilege-mgr-icon',8,1,'disable','',NULL,NULL,'0','2010-11-07 14:01:16'),(13,'resetPwd','重置密码','reset-pwd-icon',7,1,'enable','重置密码','0','2024-06-16 12:39:01','0','2010-11-07 18:14:23'),(14,'moduleLabel','模块标签修改(删除)','module-label-icon',8,1,'disable','',NULL,NULL,'0','2010-12-26 10:23:29'),(15,'saveTime','保留时间(删除)','save-time-icon',5,1,'disable','',NULL,NULL,'0','2010-12-30 16:05:31'),(16,'schedulerStart','启动调度器','scheduler-start-icon',5,1,'enable','',NULL,NULL,'0','2011-01-17 14:20:06'),(17,'schedulerStop','停止调度器','scheduler-stop-icon',6,1,'enable','',NULL,NULL,'0','2011-01-17 14:20:54'),(18,'jobStart','启动任务','job-start-icon',7,1,'enable','',NULL,NULL,'0','2011-01-17 15:01:19'),(19,'jobStop','停止任务','job-stop-icon',8,1,'enable','',NULL,NULL,'0','2011-01-17 15:05:07'),(20,'optSelector','操作选择器','table-icon',4,1,'enable','','0','2024-06-16 12:41:19','0','2024-05-06 05:13:05'),(21,'resSelector','资源选择器','table-icon',5,1,'enable','','0','2024-06-16 12:40:47','0','2024-05-12 12:41:27'),(22,'assignRes','分配资源','assign-icon',9,1,'enable','','0','2024-06-20 03:41:41','0','2024-05-12 09:48:23');

--
-- Table structure for table `sys_resource`
--
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_id` bigint DEFAULT NULL COMMENT '上级模块ID',
  `operate_id` bigint DEFAULT NULL COMMENT '操作ID',
  `identifier` varchar(64) DEFAULT NULL COMMENT '标识符',
  `name` varchar(10) DEFAULT NULL COMMENT '名称',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `path` varchar(100) DEFAULT NULL COMMENT '请求路径',
  `order` int DEFAULT NULL COMMENT '顺序',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`resource_id`),
  KEY `res_idx_name` (`name`),
  KEY `res_idx_module_id` (`module_id`) /*!80000 INVISIBLE */,
  KEY `res_idx_operate_id` (`operate_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_module_id_reference_sys_memu` FOREIGN KEY (`module_id`) REFERENCES `sys_module_menu` (`module_id`),
  CONSTRAINT `fk_operate_id_reference_sys_memu` FOREIGN KEY (`operate_id`) REFERENCES `sys_operate` (`operate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

INSERT INTO `sys_resource` VALUES (1,55,8,NULL,'列表','get','/platform/resource/mgr',1,'enable','列表数据资源',NULL,NULL,'0','2024-05-05 13:22:33'),(2,55,1,NULL,'新增','post','/platform/resource/mgr',2,'enable','',NULL,NULL,'0','2024-05-06 02:53:17'),(3,55,2,NULL,'修改','put','/platform/resource/mgr',3,'enable','',NULL,NULL,'0','2024-05-06 02:54:00'),(4,55,4,NULL,'查看','get','/platform/resource/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-05-06 03:48:40'),(5,55,5,NULL,'启用','put','/platform/resource/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-06 03:50:11'),(6,7,8,NULL,'列表','get','/platform/module/mgr',1,'enable','',NULL,NULL,'0','2024-05-06 03:50:55'),(10,7,1,NULL,'新增','post','/platform/module/mgr',2,'enable','',NULL,NULL,'0','2024-05-06 03:56:30'),(11,7,2,NULL,'修改','put','/platform/module/mgr',3,'enable','',NULL,NULL,'0','2024-05-06 03:57:56'),(12,7,4,NULL,'查看','get','/platform/module/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-05-06 03:58:32'),(13,7,5,NULL,'启用','put','/platform/module/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-06 03:59:38'),(14,7,7,'','分配操作(弃用)','post','/platform/module/mgr/{id}/module-opt-ref',6,'enable','','0','2024-06-22 07:30:17','0','2024-05-06 04:01:14'),(15,7,11,NULL,'菜单树视图','get','/platform/module/mgr/{id}/child/nodes',8,'enable','',NULL,NULL,'0','2024-05-06 04:44:51'),(16,7,20,'','操作选择器(弃用)','get','/platform/module/mgr/{id}/operation',7,'enable','','0','2024-06-22 07:30:35','0','2024-05-06 05:13:59'),(17,8,8,NULL,'列表','get','/platform/operate/mgr',1,'enable','',NULL,NULL,'0','2024-05-06 05:16:03'),(18,8,1,NULL,'新增','post','/platform/operate/mgr',2,'enable','',NULL,NULL,'0','2024-05-06 05:16:23'),(19,8,2,NULL,'修改','put','/platform/operate/mgr',3,'enable','',NULL,NULL,'0','2024-05-06 05:16:55'),(20,8,4,NULL,'查看','get','/platform/operate/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-05-06 05:19:10'),(21,8,5,NULL,'启用','put','/platform/operate/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-06 05:20:01'),(22,10,8,NULL,'列表','get','/platform/system/dictionary/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:28:10'),(23,10,1,NULL,'新增','post','/platform/system/dictionary/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:28:39'),(24,10,2,NULL,'修改','put','/platform/system/dictionary/mgr',3,'enable','',NULL,NULL,'0','2024-05-12 02:28:56'),(25,10,4,NULL,'查看','get','/platform/system/dictionary/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-05-12 02:30:22'),(26,10,5,NULL,'启用','put','/platform/system/dictionary/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-12 02:32:01'),(27,10,10,'','选择器(小字典)','get','/platform/system/dictionary/mgr/small-type',7,'enable','','0','2024-06-21 14:49:25','0','2024-05-12 02:36:18'),(28,18,8,NULL,'列表','get','/platform/system/log/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:37:41'),(29,18,4,NULL,'查看','get','/platform/system/log/mgr/{id}',2,'enable','',NULL,NULL,'0','2024-05-12 02:38:07'),(30,25,2,NULL,'修改','put','/platform/system/config/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:43:03'),(31,25,4,NULL,'查看','get','/platform/system/config/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:43:27'),(32,25,10,NULL,'选择器(用户)','get','/platform/system/config/mgr/users',3,'enable','',NULL,NULL,'0','2024-05-12 02:44:12'),(33,13,8,NULL,'列表','get','/platform/code/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:45:23'),(34,13,1,NULL,'新增','post','/platform/code/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:45:41'),(35,13,2,NULL,'修改','put','/platform/code/mgr',3,'enable','',NULL,NULL,'0','2024-05-12 02:46:02'),(36,13,4,'','查看','get','/platform/code/mgr/{id}',4,'enable','','0','2024-06-16 12:25:06','0','2024-05-12 02:46:21'),(37,13,3,NULL,'删除','delete','/platform/code/mgr/{id}',5,'enable','',NULL,NULL,'0','2024-05-12 02:47:19'),(38,13,5,NULL,'启用','put','/platform/code/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-05-12 02:48:17'),(39,15,8,NULL,'列表','get','/platform/system/parameter/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:49:10'),(40,15,1,NULL,'新增','post','/platform/system/parameter/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:49:40'),(41,15,2,NULL,'修改','put','/platform/system/parameter/mgr',3,'enable','',NULL,NULL,'0','2024-05-12 02:50:12'),(42,15,4,'','查看','get','/platform/system/parameter/mgr/{id}',4,'enable','','0','2024-06-18 22:32:01','0','2024-05-12 02:50:40'),(43,15,5,NULL,'启用','put','/platform/system/parameter/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-12 02:51:23'),(44,49,8,NULL,'列表','get','/platform/affiche/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:53:24'),(45,49,1,NULL,'新增','post','/platform/affiche/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:53:41'),(46,49,2,NULL,'修改','put','/platform/affiche/mgr',3,'enable','',NULL,NULL,'0','2024-05-12 02:53:57'),(47,49,4,NULL,'查看','get','/platform/affiche/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-05-12 02:54:20'),(48,14,8,NULL,'列表','get','/platform/district/mgr',1,'enable','',NULL,NULL,'0','2024-05-12 02:55:45'),(49,14,1,NULL,'新增','post','/platform/district/mgr',2,'enable','',NULL,NULL,'0','2024-05-12 02:56:04'),(50,14,2,NULL,'修改','put','/platform/district/mgr',3,'enable','',NULL,NULL,'0','2024-05-12 02:56:20'),(51,14,4,NULL,'查看','get','/platform/district/mgr/{id}',5,'enable','',NULL,NULL,'0','2024-05-12 02:56:50'),(52,14,5,NULL,'启用','put','/platform/district/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-05-12 02:57:41'),(53,14,11,NULL,'地区树视图','get','/platform/district/mgr/{id}/child/nodes',7,'enable','',NULL,NULL,'0','2024-05-12 02:58:36'),(54,7,22,NULL,'分配资源','post','/platform/module/mgr/{id}/module-res-ref',9,'enable','',NULL,NULL,'0','2024-05-12 09:58:18'),(55,7,21,NULL,'资源选择器','get','/platform/module/mgr/{id}/resource',9,'enable','',NULL,NULL,'0','2024-05-12 12:46:35'),(56,32,8,'','列表','get','/uum/organ/mgr',1,'enable','','0','2024-06-20 04:23:03','0','2024-06-02 08:09:15'),(57,32,1,NULL,'新增','post','/uum/organ/mgr',2,'enable','',NULL,NULL,'0','2024-06-02 08:11:56'),(58,32,2,NULL,'修改','put','/uum/organ/mgr',3,'enable','',NULL,NULL,'0','2024-06-02 08:12:32'),(59,32,4,NULL,'查看','get','/uum/organ/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-06-02 08:13:29'),(60,32,5,NULL,'启用','put','/uum/organ/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 08:14:21'),(61,32,11,NULL,'机构树视图','get','/uum/organ/mgr/{id}/child/nodes',6,'enable','',NULL,NULL,'0','2024-06-02 08:15:36'),(62,33,8,NULL,'列表','get','/uum/role/mgr',1,'enable','',NULL,NULL,'0','2024-06-02 08:17:26'),(63,33,1,NULL,'新增','post','/uum/role/mgr',2,'enable','',NULL,NULL,'0','2024-06-02 08:17:51'),(64,33,2,NULL,'修改','put','/uum/role/mgr',3,'enable','',NULL,NULL,'0','2024-06-02 08:18:25'),(65,33,4,NULL,'查看','get','/uum/role/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-06-02 08:19:01'),(66,33,5,NULL,'启用','put','/uum/role/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 08:19:58'),(67,33,21,'','资源选择器','get','/uum/role/mgr/{id}/role-privilege',6,'enable','','0','2024-06-19 12:43:39','0','2024-06-02 08:55:27'),(68,33,22,'','分配权限','post','/uum/role/mgr/{id}/res-privilege/{privilegeScope}',7,'enable','','0','2024-06-20 12:26:52','0','2024-06-02 08:57:13'),(69,33,NULL,'assignRow','分配行权限','post','/uum/role/mgr/{id}/row-privilege',8,'enable','','0','2024-06-19 12:52:54','0','2024-06-02 08:57:58'),(70,56,8,NULL,'列表','get','/uum/dept/mgr',1,'enable','',NULL,NULL,'0','2024-06-02 08:58:30'),(71,56,1,NULL,'新增','post','/uum/dept/mgr',2,'enable','',NULL,NULL,'0','2024-06-02 08:58:51'),(72,56,2,NULL,'修改 ','put','/uum/dept/mgr',3,'enable','',NULL,NULL,'0','2024-06-02 08:59:11'),(73,56,4,NULL,'查看','get','/uum/dept/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-06-02 08:59:39'),(74,56,5,NULL,'启用','put','/uum/dept/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 09:00:46'),(75,56,6,NULL,'禁用','put','/uum/dept/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 09:01:16'),(76,56,11,NULL,'部门树形视图','get','/uum/dept/mgr/{id}/child/nodes',6,'enable','',NULL,NULL,'0','2024-06-02 09:02:04'),(77,56,9,'','分配','put','/uum/dept/mgr/{id}/assign',7,'enable','','0','2024-06-21 14:25:30','0','2024-06-02 09:02:36'),(78,34,8,NULL,'列表','get','/uum/user/mgr',1,'enable','',NULL,NULL,'0','2024-06-02 09:03:19'),(79,34,1,NULL,'新增','post','/uum/user/mgr',2,'enable','',NULL,NULL,'0','2024-06-02 09:03:39'),(80,34,2,NULL,'修改','put','/uum/user/mgr',3,'enable','',NULL,NULL,'0','2024-06-02 09:03:54'),(81,34,4,NULL,'查看','get','/uum/user/mgr/{id}',4,'enable','',NULL,NULL,'0','2024-06-02 09:04:13'),(82,34,5,NULL,'启用','put','/uum/user/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 09:05:36'),(83,34,6,NULL,'禁用','put','/uum/user/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 09:06:16'),(84,34,13,'','重置密码(弃用)','put','/uum/user/mgr/{id}/reset-password',6,'enable','','0','2024-06-22 07:27:58','0','2024-06-02 09:09:27'),(85,34,9,NULL,'分配角色','put','/uum/user/mgr/update/role',7,'enable','',NULL,NULL,'0','2024-06-02 09:18:07'),(86,10,6,NULL,'停用','put','/platform/system/dictionary/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-06-02 13:38:55'),(87,55,6,NULL,'停用','put','/platform/resource/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-06-02 13:40:37'),(88,8,6,NULL,'停用','put','/platform/operate/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-06-02 13:41:32'),(89,7,6,NULL,'停用','put','/platform/module/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 13:42:26'),(90,14,6,NULL,'停用','put','/platform/district/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-06-02 13:43:19'),(91,15,6,NULL,'停用','put','/platform/system/parameter/mgr/{id}/disEnable/{disEnableFlag}',6,'enable','',NULL,NULL,'0','2024-06-02 13:48:22'),(92,13,6,NULL,'停用','put','/platform/code/mgr/{id}/disEnable/{disEnableFlag}',7,'enable','',NULL,NULL,'0','2024-06-02 13:49:11'),(93,33,6,NULL,'停用','put','/uum/role/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 13:50:26'),(94,32,6,NULL,'停用','put','/uum/organ/mgr/{id}/disEnable/{disEnableFlag}',5,'enable','',NULL,NULL,'0','2024-06-02 13:52:04'),(95,34,NULL,'modifyPassword','修改密码','put','/uum/user/mgr/update/password',8,'enable','','0','2024-06-16 12:10:57','0','2024-06-16 12:10:57'),(96,34,NULL,'modifyPhoto','修改头像','put','/uum/user/mgr/update/photo',9,'enable','','0','2024-06-16 12:15:47','0','2024-06-16 12:15:47'),(97,34,21,'','资源选择器','get','/uum/user/mgr/{id}/res-privilege',10,'enable','','0','2024-06-19 12:44:15','0','2024-06-16 12:22:03'),(98,34,NULL,'rowSelector','行权限选择器','get','/uum/user/mgr/{id}/row-privilege',11,'enable','','0','2024-06-16 12:37:10','0','2024-06-16 12:23:06'),(99,34,22,'','特殊权限分配','post','/uum/user/mgr/{id}/res-privilege/{privilegeScope}',12,'enable','','0','2024-06-20 12:27:30','0','2024-06-16 12:27:32'),(100,34,NULL,'assignRow','分配行权限','post','/uum/user/mgr/{id}/row-privilege/{privilegeScope}',13,'enable','','0','2024-06-20 12:16:54','0','2024-06-16 12:28:29'),(101,53,8,'','列表','get','/platform/system/scheduler/mgr',1,'enable','','0','2024-06-18 22:32:31','0','2024-06-18 22:32:31'),(102,53,1,'','新增','post','/platform/system/scheduler/mgr',2,'enable','','0','2024-06-18 22:33:01','0','2024-06-18 22:33:01'),(103,53,2,'','修改','put','/platform/system/scheduler/mgr',3,'enable','','0','2024-06-18 22:33:18','0','2024-06-18 22:33:18'),(104,53,4,'','查看','get','/platform/system/scheduler/mgr/{id}',4,'enable','','0','2024-06-18 22:39:17','0','2024-06-18 22:34:11'),(105,53,3,'','删除','delete','/platform/system/scheduler/mgr/{id}',5,'enable','','0','2024-06-18 22:39:29','0','2024-06-18 22:34:33'),(106,53,18,'','启动任务','put','/platform/system/scheduler/mgr/{id}/start-job',6,'enable','','0','2024-06-18 22:39:47','0','2024-06-18 22:35:45'),(107,53,19,'','停止任务','put','/platform/system/scheduler/mgr/{id}/stop-job',7,'enable','','0','2024-06-18 22:40:03','0','2024-06-18 22:36:18'),(108,53,16,'','启动调度器','post','/platform/system/scheduler/mgr/start-scheduler',8,'enable','','0','2024-06-20 07:40:56','0','2024-06-18 22:36:55'),(109,53,17,'','停止调度器','post','/platform/system/scheduler/mgr/stop-scheduler',9,'enable','','0','2024-06-20 07:41:18','0','2024-06-18 22:38:23'),(110,55,9,'','配置表头','post','/platform/table-config/mgr',7,'enable','','0','2024-06-20 05:09:36','0','2024-06-20 05:09:36'),(111,55,10,'','表头选择器','get','/platform/table-config/mgr/{moduleId}/list',8,'enable','','0','2024-06-20 05:10:59','0','2024-06-20 05:10:59'),(112,25,NULL,'startJConsole','启动','post','/platform/system/config/mgr/start-jconsole',4,'enable','','0','2024-06-20 08:14:14','0','2024-06-20 08:14:14'),(113,34,10,'','用户选择器','get','/uum/user/mgr/selector',14,'enable','','0','2024-06-20 11:34:12','0','2024-06-20 11:33:54'),(114,49,NULL,'deleteAccessory','删除附件','delete','/platform/accessory/mgr/{filenameOrId}',5,'enable','','0','2024-06-21 14:33:35','0','2024-06-21 14:33:11');

--
-- Table structure for table `sys_mod_opt_ref`
--
DROP TABLE IF EXISTS `sys_mod_opt_ref`;
CREATE TABLE `sys_mod_opt_ref` (
  `mod_opt_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_id` bigint DEFAULT NULL COMMENT '模块ID',
  `operate_id` bigint DEFAULT NULL COMMENT '操作ID',
  PRIMARY KEY (`mod_opt_id`),
  KEY `optref_idx_module_id` (`module_id`) /*!80000 INVISIBLE */,
  KEY `optref_idx_operate_id` (`operate_id`),
  CONSTRAINT `fk_module_id_reference_sys_module` FOREIGN KEY (`module_id`) REFERENCES `sys_module_menu` (`module_id`),
  CONSTRAINT `fk_operate_id_reference_sys_operate` FOREIGN KEY (`operate_id`) REFERENCES `sys_operate` (`operate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块操作关系表';

INSERT INTO `sys_mod_opt_ref` VALUES (11,8,2),(12,8,4),(13,8,5),(14,8,6),(15,10,1),(16,10,2),(17,10,4),(18,10,5),(19,10,6),(25,13,1),(26,13,2),(27,13,4),(28,13,5),(29,13,6),(30,14,1),(31,14,2),(33,14,4),(34,14,5),(35,14,6),(36,15,1),(37,15,2),(38,15,4),(39,15,5),(40,15,6),(44,18,4),(51,33,2),(52,33,4),(53,33,5),(87,7,7),(98,49,1),(99,49,2),(100,49,3),(101,49,4),(104,53,1),(105,53,2),(106,53,3),(107,53,4),(108,53,16),(109,53,17),(111,53,19),(156,7,3),(182,32,6),(183,32,5),(184,32,4),(185,32,2),(214,34,6),(215,34,5),(216,34,4),(217,34,2),(219,33,3),(220,34,3),(221,34,7),(223,34,1),(227,7,6),(228,7,5),(229,7,4),(230,7,2),(231,7,1),(233,33,6),(234,33,1),(235,34,13),(237,56,6),(238,56,5),(239,56,4),(240,56,2),(241,56,1),(242,8,1),(243,56,9),(244,34,9),(245,56,3),(246,55,9),(247,55,4),(248,55,2),(249,55,1),(251,33,7),(253,7,22),(254,32,1);

--
-- Table structure for table `sys_mod_res_ref`
--
DROP TABLE IF EXISTS `sys_mod_res_ref`;
CREATE TABLE `sys_mod_res_ref` (
  `mod_res_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_id` bigint DEFAULT NULL COMMENT '模块ID',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  `group` bigint DEFAULT NULL COMMENT '显示位置分组',
  `order` bigint DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`mod_res_id`),
  KEY `resref_idx_module_id` (`module_id`) /*!80000 INVISIBLE */,
  KEY `resref_idx_resource_id` (`resource_id`),
  CONSTRAINT `fk_res_module_id_reference_sys_module` FOREIGN KEY (`module_id`) REFERENCES `sys_module_menu` (`module_id`),
  CONSTRAINT `fk_resource_id_reference_sys_operate` FOREIGN KEY (`resource_id`) REFERENCES `sys_resource` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块资源关系表';

INSERT INTO `sys_mod_res_ref` VALUES (1,7,6,1,1),(2,7,10,10,2),(3,7,11,14,3),(4,7,12,6,4),(5,7,13,4,5),(6,7,14,10,6),(7,7,16,512,7),(8,7,15,512,8),(9,7,55,512,9),(10,7,54,10,10),(11,8,17,1,1),(12,8,18,10,2),(13,8,19,12,3),(14,8,20,4,4),(15,8,21,4,5),(16,55,1,1,1),(17,55,2,12,2),(18,55,3,12,3),(19,55,4,4,4),(20,55,5,4,5),(21,10,22,1,1),(22,10,23,10,2),(23,10,24,12,3),(24,10,25,4,4),(25,10,26,4,5),(26,10,27,512,6),(27,7,89,4,11),(28,8,88,4,6),(29,55,87,4,6),(30,10,86,4,7),(31,18,28,1,1),(32,18,29,2,2),(33,25,30,8,1),(34,25,31,1,2),(35,25,32,512,3),(36,13,33,1,1),(37,13,34,10,2),(38,13,35,12,3),(39,13,36,4,4),(40,13,37,4,5),(41,13,38,4,6),(42,13,92,4,7),(43,15,39,1,1),(44,15,40,10,2),(45,15,41,12,3),(46,15,42,4,4),(47,15,43,4,5),(48,15,91,4,6),(49,49,44,1,1),(50,49,45,10,2),(51,49,46,12,3),(52,49,47,4,4),(53,53,101,1,1),(54,53,102,10,2),(55,53,103,12,3),(56,53,104,4,4),(57,53,105,4,5),(58,53,106,4,6),(59,53,107,4,7),(60,53,108,2,8),(61,53,109,2,9),(62,32,56,1,1),(63,32,57,10,2),(64,32,58,12,3),(65,32,59,4,4),(66,32,94,4,5),(67,32,60,4,6),(68,32,61,512,7),(69,14,48,1,1),(70,14,49,10,2),(71,14,50,12,3),(72,14,52,4,4),(73,14,51,4,5),(74,14,90,4,6),(75,14,53,512,7),(76,33,62,1,1),(77,33,63,10,2),(78,33,64,12,3),(79,33,65,4,4),(80,33,93,4,5),(81,33,66,4,6),(82,33,67,512,7),(83,33,68,10,8),(84,33,69,8,9),(85,56,70,1,2),(86,56,71,10,3),(87,56,72,12,4),(88,56,73,4,5),(89,56,75,4,6),(90,56,74,4,7),(91,56,76,512,8),(92,56,77,10,9),(93,34,78,1,2),(94,34,79,10,3),(95,34,80,12,4),(96,34,81,4,5),(97,34,83,4,6),(98,34,82,4,7),(100,34,85,14,8),(101,34,95,8,9),(102,34,96,8,10),(103,34,97,512,11),(104,34,98,512,12),(105,34,99,10,13),(106,34,100,512,14),(107,55,16,512,7),(108,55,110,12,8),(109,55,111,512,9),(110,13,15,512,8),(111,25,112,8,4),(112,32,53,512,8),(113,34,76,512,1),(114,34,113,512,15),(115,56,62,512,1),(116,49,114,512,5);

--
-- Table structure for table `sys_opt_log`
--
DROP TABLE IF EXISTS `sys_opt_log`;
CREATE TABLE `sys_opt_log` (
  `opt_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_id` bigint DEFAULT NULL COMMENT '所属机构',
  `mod_name` varchar(20) DEFAULT NULL COMMENT '模块名称',
  `opt_name` varchar(20) DEFAULT NULL COMMENT '操作名称',
  `content` text COMMENT '操作内容',
  `busi_type` int DEFAULT NULL COMMENT '业务类型',
  `busi_id` varchar(64) DEFAULT NULL COMMENT '业务ID',
  `ip` varchar(25) DEFAULT NULL COMMENT '主机IP',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `user_role` varchar(64) DEFAULT NULL COMMENT '用户角色',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  PRIMARY KEY (`opt_id`),
  KEY `log_idx_mod_name` (`mod_name`) /*!80000 INVISIBLE */,
  KEY `log_idx_create_date` (`create_time`),
  KEY `log_idx_user_name` (`user_name`),
  KEY `log_idx_user_role` (`user_role`),
  KEY `log_idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

INSERT INTO `sys_opt_log` VALUES (3,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(4,1,'角色管理','保存','经理;',NULL,'3','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(5,1,'模块分配操作','保存','模块操作关系ID:45;46;47;48;49;50;51;52;53;54;55;56;57;58;59;60;61;62;64;91;97;65;66;68;69;116;117;118;119;112;113;114;115;',NULL,'3;rolePri','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(6,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(7,1,'类别字典管理','保存','字典大类名称：a;',NULL,'20','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(8,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(9,1,'系统公告管理','保存','保存公告标题：aa;有效期限:Tue Dec 20 00:00:00 CST 2011',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(10,1,'系统附件','保存','附件业务表ID：1;附件名称:4M504g_blog_second.bmp',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(11,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(12,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(13,1,'系统公告管理','修改','修改公告ID：1;修改公告标题：aaab;有效期：Wed Dec 21 00:00:00 CST 2011;是否发布：0;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(14,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(15,1,'系统模块操作管理','保存','分配;a;aaa;',NULL,'25','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(16,1,'系统模块操作管理','修改','分配;a;aaa;',NULL,'25','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(17,1,'系统模块操作管理','修改','分配;a;aaa;',NULL,'25','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(18,1,'系统模块管理','保存','测试;abc;',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(19,1,'系统模块管理','修改','测试;abc;',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(20,1,'模块分配操作','保存','模块分配操作;25',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(21,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(22,1,'模块分配操作','保存','模块分配操作;1',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(23,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(24,1,'类别字典管理','修改','字典大类名称：a;',NULL,'20','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(25,1,'类别字典管理','保存','字典大类名称：系统类别;',NULL,'21','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(26,1,'系统参数管理','保存','参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456参数类型：plt',NULL,'20','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(27,1,'系统参数管理','保存','参数名称：用户有效期;参数键值：uservalidate;参数值：6参数类型：uus',NULL,'21','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(28,1,'系统参数管理','修改','参数名称：用户默认密码;参数键值：userdefaultpassword;参数值：123456参数类型：uus',NULL,'20','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(29,1,'类别字典管理','保存','字典大类名称：地区级别;',NULL,'22','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(30,1,'省市地区管理','保存','北京;province;',NULL,'2000','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(31,1,'主页面功能区','删除','删除功能区的id:;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(32,1,'主页面功能区','保存','增加的功能区信息 id:30;funName:header;funURI:general;funMemo:;',NULL,'30','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(33,1,'主页面功能区','删除','删除功能区的id:30;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(34,1,'主页面功能区','删除','删除功能区的id:;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(35,1,'主页面功能区','保存','增加的功能区信息 id:30;funName:header;funURI:general;funMemo:;',NULL,'30','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(36,1,'主页面功能区','删除','删除功能区的id:30;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(37,1,'主页面功能区','保存','增加的功能区信息 id:30;funName:header;funURI:general;funMemo:aa;',NULL,'30','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(38,1,'主页面功能区','删除','删除功能区的id:30;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(39,1,'系统配置管理','保存','设置系统名称:my协同办公管理系统;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(40,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(41,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(42,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(43,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(44,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(45,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(46,1,'系统配置管理','保存','设置系统名称:my协同办公管理系统;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(47,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(48,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(49,1,'类别字典管理','保存','字典大类名称：机构类型;',NULL,'23','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(50,1,'机构管理','保存','机构名称：河北;机构代码：;上级机构：总部;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(51,1,'机构管理','修改','机构名称：河北;机构代码：HBS;上级机构：总部;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(52,1,'角色管理','修改','经理在;',NULL,'3','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(53,1,'角色分配','保存','机构id:1;角色id:1;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(54,1,'角色分配','删除','删除机构下的角色ID：1;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(55,1,'角色分配','保存','机构id:1;角色id:3;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(56,1,'用户分配角色','保存','11',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(57,1,'用户管理','保存','jacob;jacob;经理在;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(58,1,'用户管理','修改','jacob;jacob;null;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(59,1,'用户分配角色','保存','11,1',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(60,1,'用户分配角色','保存','11,1',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(61,1,'用户分配角色','保存','11,1',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(62,1,'用户管理','重置密码','重置密码用户：jacob',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(63,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(64,1,'用户登录','用户登录','河北-经理在-jacob',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(65,1,'模块分配操作','保存','模块操作关系ID:',NULL,'3;rolePri','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(66,1,'模块分配操作','保存','模块操作关系ID:45;46;47;48;49;',NULL,'3;rolePri','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(67,1,'用户分配角色','保存','11',NULL,'12','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(68,1,'用户登录','用户登录','河北-经理在-jacob',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(69,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(70,1,'用户登录','用户登录','河北-经理在-jacob',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(71,1,'用户登录','用户登录','河北-经理在-jacob',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(72,1,'用户常用功能设置','保存','用户常用功能保存:用户ID12;模块ID：32',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(73,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(74,1,'用户(普通)组管理','保存','组名称：员工组;组类型：userGroup;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(75,1,'用户(普通)组管理','修改','组名称：员工组1;组类型：userGroup;',NULL,'1','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(76,1,'角色组管理','保存','组名称：角色组;组类型：roleGroup;',NULL,'2','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(77,1,'用户(普通)组管理','保存','组名称：普通1;组类型：generalGroup;',NULL,'3','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(78,1,'用户分配角色','保存','0',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(79,1,'用户管理','修改','jacob;jacob;经理在;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(80,1,'用户管理','停用','jacob;jacob;经理在;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(81,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(82,1,'用户常用功能设置','保存','用户常用功能保存:用户ID0;模块ID：25,53,4,7',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(83,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(84,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(85,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(86,1,'系统模块管理','修改','工作流;workflowId;',NULL,'50','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(87,1,'系统模块管理','修改','测试;test;',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(88,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(89,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(90,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(91,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(92,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(93,1,'系统模块管理','修改','测试;test;',NULL,'60','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(94,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(95,1,'系统模块管理','保存','待办任务;underwayTaskId;',NULL,'61','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(96,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(97,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(98,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(99,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(100,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(101,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(102,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(103,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(104,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(105,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(106,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(107,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(108,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(109,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(110,1,'系统编码管理','保存','编码英文名称：a;编码中文名称:a;编码所属模块名称:a;编码效果:20111221-;',NULL,'20','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(111,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(112,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(113,1,'系统编码管理','保存','编码英文名称：ab;编码中文名称:ab;编码所属模块名称:ab;编码效果:YKD-20111221;',NULL,'21','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(114,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(115,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(116,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(117,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(118,1,'系统编码管理','保存','编码英文名称：liang;编码中文名称:zh;编码所属模块名称:zh;编码效果:abc-20111221-001-北京~管理员~zhangsan;',NULL,'22','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(119,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(120,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(121,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(122,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(123,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(124,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(125,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(126,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(127,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(128,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(129,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(130,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(131,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(132,1,'系统编码管理','保存','编码英文名称：test;编码中文名称:测试;编码所属模块名称:测试;编码效果:a-20111221-0001-zhangsan;',NULL,'23','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(133,1,'系统编码管理','修改','编码英文名称：test;编码中文名称:测试编码所属模块名称:测试编码效果:',NULL,'23','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(134,1,'系统配置管理','保存','设置角色切换:login;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(135,1,'用户管理','修改','jacob;jacob;无角色用户;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(136,1,'用户管理','停用','jacob;jacob;无角色用户;',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(137,1,'用户分配角色','保存','0,11',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(138,1,'用户分配角色','保存','11',NULL,'12','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(139,1,'用户登录','用户登录','河北-经理在-jacob',NULL,'','127.0.0.1','jacob','经理在',NULL,'2024-06-28 23:37:41.136562'),(140,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(141,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(142,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(143,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(144,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(145,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(146,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(147,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(148,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(149,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(150,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(151,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(152,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(153,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(154,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(155,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(156,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(157,1,'角色管理','保存','会计;',NULL,'4','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(158,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(159,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(160,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(161,1,'角色管理','保存','平衡;',NULL,'5','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(162,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(163,1,'系统配置管理','保存','设置系统名称:my协同办公管理系统a;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(164,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(165,1,'系统配置管理','保存','设置系统名称:my协同办公管理系统ab;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(166,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(167,1,'系统参数管理','修改','参数名称：用户有效期;参数键值：uservalidate;参数值：6参数类型：uus',NULL,'2','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(168,1,'角色管理','保存','q;',NULL,'6','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(169,1,'角色管理','保存','a;',NULL,'7','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(170,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(171,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(172,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(173,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(174,1,'角色管理','保存','出纳;',NULL,'8','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(175,1,'角色管理','保存','会计2;',NULL,'9','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(176,1,'省市地区管理','保存','北海;province;',NULL,'2001','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(177,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(178,1,'角色管理','修改','会计2;',NULL,'9','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(179,1,'角色管理','修改','会计2;',NULL,'9','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(180,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(181,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(182,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(183,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(184,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(185,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(186,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(187,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(188,1,'系统编码管理','保存','编码英文名称：test2;编码中文名称:测试2;编码所属模块名称:测试2;编码效果:CKD-201112-001-zhangsan;',NULL,'24','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(189,1,'系统编码管理','修改','编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan',NULL,'24','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(190,1,'系统编码管理','停用','编码停用：编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan',NULL,'24','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(191,1,'系统编码管理','修改','编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan',NULL,'24','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(192,1,'系统编码管理','启用','编码启用：编码英文名称：test2;编码中文名称:测试2编码所属模块名称:测试2编码效果:CKD-201112-001-zhangsan',NULL,'24','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(193,1,'系统调度管理','启动调度器','启动调度器并初始化任务',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(194,1,'系统调度管理','停止调度器','停止调度器',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(195,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(196,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(197,1,'系统配置管理','保存','设置系统名称:jacob_liang平台管理统一用户;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(198,1,'用户登录','用户登录','总部-管理员-管理员',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(199,1,'系统配置管理','保存','设置角色切换:login;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562'),(200,1,'系统配置管理','保存','设置日志级别:2;',NULL,'','127.0.0.1','管理员','管理员',NULL,'2024-06-28 23:37:41.136562');

--
-- Table structure for table `sys_parameter`
--
DROP TABLE IF EXISTS `sys_parameter`;
CREATE TABLE `sys_parameter` (
  `para_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) DEFAULT NULL COMMENT '参数名称',
  `key` varchar(20) DEFAULT NULL COMMENT '参数键名称',
  `value` varchar(100) DEFAULT NULL COMMENT '参数值',
  `type` varchar(20) DEFAULT NULL COMMENT '参数类型',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`para_id`),
  KEY `param_idx_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';

INSERT INTO `sys_parameter` VALUES (1,'用户默认密码','userdefaultpassword','123456','platform','enable','',NULL,NULL,'0','2011-12-20 14:48:25'),(2,'用户有效期','uservalidate','6','uum','enable','','0','2024-06-20 08:52:45','0','2011-12-20 14:48:55');

--
-- Table structure for table `sys_scheduler_job`
--
DROP TABLE IF EXISTS `sys_scheduler_job`;
CREATE TABLE `sys_scheduler_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(30) DEFAULT NULL COMMENT '任务名称',
  `job_class` varchar(100) DEFAULT NULL COMMENT '任务执行的类名',
  `trigger_type` varchar(20) DEFAULT NULL COMMENT '任务触发类型',
  `cron` varchar(100) DEFAULT NULL COMMENT '时间表达式',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `repeat_count` int DEFAULT NULL COMMENT '重复次数',
  `interval_time` bigint DEFAULT NULL COMMENT '间隔时间',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务表';

INSERT INTO `sys_scheduler_job` VALUES (1,'日志清理','com.mycuckoo.service.platform.job.LoggerJob','Cron','* * 0 15 * ?','2011-01-18 16:00:00','2011-02-18 16:00:00',2,2,'disable','每月15日0点清除日志','0','2024-06-20 07:33:50','0','2011-01-19 15:28:44'),(2,'测试','com.mycuckoo.service.platform.job.TestJob','Simple','','2019-01-27 16:00:00','2019-01-28 16:00:00',2,2,'disable','','0','2024-05-26 13:53:23','0','2019-01-28 14:07:16');

--
-- Table structure for table `sys_table_config`
--
DROP TABLE IF EXISTS `sys_table_config`;
CREATE TABLE `sys_table_config` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_code` varchar(64) DEFAULT NULL COMMENT '表头编码',
  `module_id` bigint DEFAULT NULL COMMENT '所属模块ID',
  `type` varchar(10) DEFAULT NULL COMMENT '类型',
  `field` varchar(32) DEFAULT NULL COMMENT '字段',
  `title` varchar(32) DEFAULT NULL COMMENT '标题',
  `filter_type` varchar(10) DEFAULT NULL COMMENT '过滤类型',
  `filter` tinyint DEFAULT '0' COMMENT '是否可过滤',
  `sort` tinyint DEFAULT '0' COMMENT '是否可排序',
  `blank` tinyint DEFAULT '0' COMMENT '是否可为空',
  `width` int DEFAULT NULL COMMENT '宽度',
  `extra` varchar(100) DEFAULT '' COMMENT '额外信息',
  `order` int DEFAULT '0' COMMENT '顺序',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`table_id`),
  KEY `tcfg_idx_table_code` (`table_code`),
  KEY `tcfg_idx_module_id` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表头配置表';

INSERT INTO `sys_table_config` VALUES (1,'table_code_menu',7,'seq','seq','序号','eq',0,0,0,NULL,'',2,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(2,'table_code_menu',7,'id','id','ID','eq',0,0,0,NULL,'',1,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(3,'table_code_menu',7,'text','name','名称','like',1,0,0,180,'',3,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(4,'table_code_menu',7,'text','code','编码','eq',0,0,0,NULL,'',4,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(5,'table_code_menu',7,'text','iconCls','图标样式','eq',0,0,0,NULL,'',5,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(6,'table_code_menu',7,'dict','level','级别','eq',0,0,0,NULL,'system_level',6,'0','2024-06-21 16:01:19','0','2024-06-02 07:41:30'),(7,'table_code_menu',7,'number','order','顺序','eq',0,0,0,NULL,'',7,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30'),(8,'table_code_menu',7,'dict','belongSys','所属系统','eq',0,0,0,NULL,'system_type',8,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30'),(9,'table_code_menu',7,'dict','pageType','页面类型','eq',0,0,0,NULL,'mod_page_type',9,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30'),(10,'table_code_menu',7,'dict','status','状态','eq',0,0,0,NULL,'system_status',10,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30'),(11,'table_code_menu',7,'text','creator','创建人','eq',0,0,0,NULL,'',11,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30'),(12,'table_code_menu',7,'date','createDate','创建时间','eq',0,0,0,NULL,'',12,'0','2024-06-21 16:01:20','0','2024-06-02 07:41:30');

--
-- Table structure for table `uum_account`
--
DROP TABLE IF EXISTS `uum_account`;
CREATE TABLE `uum_account` (
  `account_id` bigint NOT NULL AUTO_INCREMENT COMMENT '账号ID',
  `account` varchar(32) DEFAULT NULL COMMENT '账号',
  `phone` varchar(20) DEFAULT '' COMMENT '手机',
  `email` varchar(64) DEFAULT '' COMMENT '邮箱',
  `password` varchar(20) DEFAULT '' COMMENT '密码',
  `error_count` int DEFAULT '0' COMMENT '密码登录错误时间',
  `ip` varchar(20) DEFAULT '' COMMENT '登录ip',
  `login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `act_idx_phone` (`phone`),
  KEY `act_idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账号表';

INSERT INTO `uum_account` VALUES (1,'admin','13828000001',NULL,'UlFQV1ZV',0,'127.0.0.1','2024-07-27 23:11:29',NULL,'2011-02-11 16:57:01'),(2,'wangj','13828000002','','UlFQV1ZV',0,'',NULL,NULL,'2011-12-21 10:09:20'),(3,'shijh','13828000003','','UlFQV1ZV',0,'',NULL,NULL,'2012-01-10 14:55:24'),(4,'yangwj','13828000004','','UlFQV1ZV',0,'',NULL,NULL,'2012-02-28 15:23:30'),(5,'yuzp','13828000005','','UlFQV1ZV',0,'',NULL,NULL,'2012-03-06 15:15:09'),(6,'zhangsp','13828000006','','UlFQV1ZV',0,'',NULL,NULL,'2012-03-06 15:15:39'),(7,'zhangl','13828000007','','UlFQV1ZV',0,'',NULL,NULL,'2012-03-06 15:16:22'),(8,'gaoxf','13828000008','','UlFQV1ZV',0,'',NULL,NULL,'2012-03-06 15:21:33'),(9,'wangx','13828000009','','UlFQV1ZV',0,'',NULL,NULL,'2012-03-06 15:21:57'),(10,'wangxm','13828000010','','UlFQV1ZV',0,'127.0.0.1','2024-06-25 12:11:39',NULL,'2012-03-06 15:23:09'),(11,'zhangj','13828000011','','UlFQV1ZV',0,'0:0:0:0:0:0:0:1','2024-06-23 02:33:48',NULL,'2012-03-06 15:23:40'),(12,NULL,'13700000001',NULL,'UlFQV1ZV',0,'0:0:0:0:0:0:0:1','2024-07-06 13:01:08','2024-07-06 04:13:49','2024-07-06 04:13:49');

--
-- Table structure for table `uum_captcha`
--
DROP TABLE IF EXISTS `uum_captcha`;
CREATE TABLE `uum_captcha` (
  `id` varchar(32) NOT NULL COMMENT 'ID',
  `type` varchar(10) DEFAULT NULL COMMENT '类型: img/sms/email',
  `code` varchar(10) DEFAULT '' COMMENT '验证码',
  `status` int DEFAULT '0' COMMENT '核销状态, 0:初始 1:已核销',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

--
-- Table structure for table `uum_department`
--
DROP TABLE IF EXISTS `uum_department`;
CREATE TABLE `uum_department` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_id` bigint DEFAULT NULL COMMENT '组织id',
  `parent_id` bigint DEFAULT NULL COMMENT '上一级ID',
  `tree_id` varchar(200) DEFAULT NULL COMMENT '包含父id的树形结构id',
  `role_id` bigint DEFAULT NULL COMMENT '默认角色id',
  `level` int DEFAULT NULL COMMENT '层级',
  `code` varchar(10) DEFAULT '' COMMENT '代码',
  `name` varchar(10) DEFAULT '' COMMENT '名称',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`dept_id`),
  KEY `dept_idx_parent_id` (`parent_id`) /*!80000 INVISIBLE */,
  KEY `dept_idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

INSERT INTO `uum_department` VALUES (1,1,0,'1',NULL,1,NULL,'总部','enable',NULL,NULL,NULL,NULL,'2024-05-19 15:22:53'),(2,1,1,'1.2',NULL,2,'DSH','董事会','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(3,1,1,'1.3',NULL,2,'10010','技术部','disable','测试',NULL,NULL,NULL,'2024-05-19 15:22:53'),(4,1,1,'1.4',NULL,2,'','山西运营','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(5,1,4,'1.4.5',NULL,3,'','太原','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(6,1,3,'1.3.6',NULL,3,'','海淀','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(7,1,6,'1.3.6.7',NULL,4,'','五道口店','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(8,1,12,'1.2.12.8',7,4,'','运营部','enable','','0','2024-06-22 13:35:23',NULL,'2024-05-19 15:22:53'),(9,1,12,'1.2.12.9',5,4,'','人力资源','enable','','0','2024-05-19 12:56:20',NULL,'2024-05-19 15:22:53'),(10,1,12,'1.2.12.10',19,4,'','财务部','enable','','0','2024-06-21 14:26:14',NULL,'2024-05-19 15:22:53'),(11,1,12,'1.2.12.11',NULL,4,'','采购部','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(12,1,2,'1.2.12',NULL,3,'ZJB','总经办','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(13,1,12,'1.2.12.13',NULL,4,'','储运部','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(14,1,12,'1.2.12.14',1,4,'','开发部','enable','','0','2024-06-22 13:10:38',NULL,'2024-05-19 15:22:53'),(15,1,11,'1.2.12.11.15',NULL,5,'','北京采购','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(16,1,11,'1.2.12.11.16',NULL,5,'','上海采购','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(17,1,13,'1.2.12.13.17',NULL,5,'','北京储运','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53'),(18,1,13,'1.2.12.13.18',NULL,5,'','上海储运','enable','',NULL,NULL,NULL,'2024-05-19 15:22:53');

--
-- Table structure for table `uum_organ`
--
DROP TABLE IF EXISTS `uum_organ`;
CREATE TABLE `uum_organ` (
  `org_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint DEFAULT NULL COMMENT '上一级ID',
  `tree_id` varchar(200) DEFAULT NULL COMMENT '包含父id的树形结构id',
  `role_id` bigint DEFAULT NULL COMMENT '默认管理员角色id',
  `level` int DEFAULT NULL COMMENT '层级',
  `type` varchar(20) DEFAULT '' COMMENT '机构类型',
  `code` varchar(10) DEFAULT '' COMMENT '机构代码',
  `simple_name` varchar(10) DEFAULT '' COMMENT '机构简称',
  `full_name` varchar(60) DEFAULT '' COMMENT '机构全称',
  `address1` varchar(100) DEFAULT '' COMMENT '联系地址1',
  `address2` varchar(100) DEFAULT '' COMMENT '联系地址2',
  `tel1` varchar(25) DEFAULT '' COMMENT '联系电话1',
  `tel2` varchar(25) DEFAULT '' COMMENT '联系电话2',
  `begin_date` date DEFAULT NULL COMMENT '成立日期',
  `fax` varchar(20) DEFAULT '' COMMENT '传真',
  `postal` varchar(6) DEFAULT '' COMMENT '机构邮编',
  `legal` varchar(20) DEFAULT '' COMMENT '法人代表',
  `tax_no` varchar(25) DEFAULT '' COMMENT '税务号',
  `reg_no` varchar(25) DEFAULT '' COMMENT '注册登记号',
  `belong_dist` bigint DEFAULT NULL COMMENT '所属地区',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`org_id`),
  KEY `org_idx_simple_name` (`simple_name`) USING BTREE,
  KEY `org_idx_parent_id` (`parent_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_parent_id_reference_uum_org` FOREIGN KEY (`parent_id`) REFERENCES `uum_organ` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

INSERT INTO `uum_organ` VALUES (1,0,'1',NULL,1,NULL,NULL,'总部',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'enable',NULL,NULL,NULL,NULL,NULL),(2,1,'1.2',NULL,2,'branch','MyCuckoo','卖酷扣','深圳市卖酷扣股份有限公司','','','','','2011-12-13',NULL,'','','','',0,'enable','','0','2024-06-20 09:25:41','0','2011-12-21 09:33:50'),(3,1,'1.3',20,2,NULL,NULL,'张三雪糕批发','张三雪糕批发',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'enable',NULL,NULL,'2024-07-06 04:13:49',NULL,'2024-07-06 04:13:49');

--
-- Table structure for table `uum_privilege`
--
DROP TABLE IF EXISTS `uum_privilege`;
CREATE TABLE `uum_privilege` (
  `privilege_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_id` bigint DEFAULT NULL COMMENT '组织',
  `resource_id` varchar(64) NOT NULL COMMENT '资源ID',
  `owner_id` bigint NOT NULL COMMENT '拥有者ID',
  `owner_type` varchar(10) DEFAULT NULL COMMENT '拥有者类型',
  `privilege_scope` varchar(10) DEFAULT NULL COMMENT '针对资源: inc=包含 exc=不包含 all=全部;\\n针对行: dept=部门 rol=角色 usr=用户',
  `privilege_type` varchar(10) DEFAULT NULL COMMENT 'row=行 res=资源 opt=操作',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`privilege_id`),
  KEY `pri_idx_owner_type` (`owner_type`) USING BTREE /*!80000 INVISIBLE */,
  KEY `pri_idx_privilege_type` (`privilege_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

INSERT INTO `uum_privilege` VALUES (1,NULL,'110',1,'rol','inc','res',NULL,NULL),(2,NULL,'112',1,'rol','inc','res',NULL,NULL),(3,NULL,'111',1,'rol','inc','res',NULL,NULL),(4,NULL,'70',1,'rol','inc','res',NULL,NULL),(5,NULL,'114',1,'rol','inc','res',NULL,NULL),(6,NULL,'71',1,'rol','inc','res',NULL,NULL),(7,NULL,'113',1,'rol','inc','res',NULL,NULL),(8,NULL,'116',1,'rol','inc','res',NULL,NULL),(9,NULL,'72',1,'rol','inc','res',NULL,NULL),(10,NULL,'73',1,'rol','inc','res',NULL,NULL),(11,NULL,'115',1,'rol','inc','res',NULL,NULL),(12,NULL,'30',1,'rol','inc','res',NULL,NULL),(13,NULL,'74',1,'rol','inc','res',NULL,NULL),(14,NULL,'31',1,'rol','inc','res',NULL,NULL),(15,NULL,'75',1,'rol','inc','res',NULL,NULL),(16,NULL,'32',1,'rol','inc','res',NULL,NULL),(17,NULL,'76',1,'rol','inc','res',NULL,NULL),(18,NULL,'33',1,'rol','inc','res',NULL,NULL),(19,NULL,'77',1,'rol','inc','res',NULL,NULL),(20,NULL,'34',1,'rol','inc','res',NULL,NULL),(21,NULL,'78',1,'rol','inc','res',NULL,NULL),(22,NULL,'35',1,'rol','inc','res',NULL,NULL),(23,NULL,'79',1,'rol','inc','res',NULL,NULL),(24,NULL,'25',1,'rol','inc','res',NULL,NULL),(25,NULL,'69',1,'rol','inc','res',NULL,NULL),(26,NULL,'26',1,'rol','inc','res',NULL,NULL),(27,NULL,'60',1,'rol','inc','res',NULL,NULL),(28,NULL,'61',1,'rol','inc','res',NULL,NULL),(29,NULL,'62',1,'rol','inc','res',NULL,NULL),(30,NULL,'63',1,'rol','inc','res',NULL,NULL),(31,NULL,'64',1,'rol','inc','res',NULL,NULL),(32,NULL,'21',1,'rol','inc','res',NULL,NULL),(33,NULL,'65',1,'rol','inc','res',NULL,NULL),(34,NULL,'22',1,'rol','inc','res',NULL,NULL),(35,NULL,'66',1,'rol','inc','res',NULL,NULL),(36,NULL,'23',1,'rol','inc','res',NULL,NULL),(37,NULL,'67',1,'rol','inc','res',NULL,NULL),(38,NULL,'24',1,'rol','inc','res',NULL,NULL),(39,NULL,'68',1,'rol','inc','res',NULL,NULL),(40,NULL,'58',1,'rol','inc','res',NULL,NULL),(41,NULL,'59',1,'rol','inc','res',NULL,NULL),(42,NULL,'90',1,'rol','inc','res',NULL,NULL),(43,NULL,'91',1,'rol','inc','res',NULL,NULL),(44,NULL,'92',1,'rol','inc','res',NULL,NULL),(45,NULL,'93',1,'rol','inc','res',NULL,NULL),(46,NULL,'50',1,'rol','inc','res',NULL,NULL),(47,NULL,'94',1,'rol','inc','res',NULL,NULL),(48,NULL,'51',1,'rol','inc','res',NULL,NULL),(49,NULL,'95',1,'rol','inc','res',NULL,NULL),(50,NULL,'52',1,'rol','inc','res',NULL,NULL),(51,NULL,'96',1,'rol','inc','res',NULL,NULL),(52,NULL,'53',1,'rol','inc','res',NULL,NULL),(53,NULL,'97',1,'rol','inc','res',NULL,NULL),(54,NULL,'54',1,'rol','inc','res',NULL,NULL),(55,NULL,'98',1,'rol','inc','res',NULL,NULL),(56,NULL,'55',1,'rol','inc','res',NULL,NULL),(57,NULL,'56',1,'rol','inc','res',NULL,NULL),(58,NULL,'57',1,'rol','inc','res',NULL,NULL),(59,NULL,'47',1,'rol','inc','res',NULL,NULL),(60,NULL,'48',1,'rol','inc','res',NULL,NULL),(61,NULL,'49',1,'rol','inc','res',NULL,NULL),(62,NULL,'101',1,'rol','inc','res',NULL,NULL),(63,NULL,'80',1,'rol','inc','res',NULL,NULL),(64,NULL,'100',1,'rol','inc','res',NULL,NULL),(65,NULL,'81',1,'rol','inc','res',NULL,NULL),(66,NULL,'82',1,'rol','inc','res',NULL,NULL),(67,NULL,'102',1,'rol','inc','res',NULL,NULL),(68,NULL,'83',1,'rol','inc','res',NULL,NULL),(69,NULL,'40',1,'rol','inc','res',NULL,NULL),(70,NULL,'84',1,'rol','inc','res',NULL,NULL),(71,NULL,'41',1,'rol','inc','res',NULL,NULL),(72,NULL,'85',1,'rol','inc','res',NULL,NULL),(73,NULL,'42',1,'rol','inc','res',NULL,NULL),(74,NULL,'86',1,'rol','inc','res',NULL,NULL),(75,NULL,'43',1,'rol','inc','res',NULL,NULL),(76,NULL,'87',1,'rol','inc','res',NULL,NULL),(77,NULL,'44',1,'rol','inc','res',NULL,NULL),(78,NULL,'88',1,'rol','inc','res',NULL,NULL),(79,NULL,'45',1,'rol','inc','res',NULL,NULL),(80,NULL,'89',1,'rol','inc','res',NULL,NULL),(81,NULL,'46',1,'rol','inc','res',NULL,NULL),(82,NULL,'36',1,'rol','inc','res',NULL,NULL),(83,NULL,'37',1,'rol','inc','res',NULL,NULL),(84,NULL,'38',1,'rol','inc','res',NULL,NULL),(85,NULL,'39',1,'rol','inc','res',NULL,NULL),(201,NULL,'112',5,'rol','inc','res',NULL,NULL),(202,NULL,'70',5,'rol','inc','res',NULL,NULL),(203,NULL,'114',5,'rol','inc','res',NULL,NULL),(204,NULL,'71',5,'rol','inc','res',NULL,NULL),(205,NULL,'113',5,'rol','inc','res',NULL,NULL),(206,NULL,'116',5,'rol','inc','res',NULL,NULL),(207,NULL,'72',5,'rol','inc','res',NULL,NULL),(208,NULL,'73',5,'rol','inc','res',NULL,NULL),(209,NULL,'115',5,'rol','inc','res',NULL,NULL),(210,NULL,'30',5,'rol','inc','res',NULL,NULL),(211,NULL,'74',5,'rol','inc','res',NULL,NULL),(212,NULL,'31',5,'rol','inc','res',NULL,NULL),(213,NULL,'75',5,'rol','inc','res',NULL,NULL),(214,NULL,'32',5,'rol','inc','res',NULL,NULL),(215,NULL,'76',5,'rol','inc','res',NULL,NULL),(216,NULL,'77',5,'rol','inc','res',NULL,NULL),(217,NULL,'78',5,'rol','inc','res',NULL,NULL),(218,NULL,'79',5,'rol','inc','res',NULL,NULL),(219,NULL,'25',5,'rol','inc','res',NULL,NULL),(220,NULL,'69',5,'rol','inc','res',NULL,NULL),(221,NULL,'26',5,'rol','inc','res',NULL,NULL),(222,NULL,'62',5,'rol','inc','res',NULL,NULL),(223,NULL,'63',5,'rol','inc','res',NULL,NULL),(224,NULL,'64',5,'rol','inc','res',NULL,NULL),(225,NULL,'21',5,'rol','inc','res',NULL,NULL),(226,NULL,'65',5,'rol','inc','res',NULL,NULL),(227,NULL,'22',5,'rol','inc','res',NULL,NULL),(228,NULL,'66',5,'rol','inc','res',NULL,NULL),(229,NULL,'23',5,'rol','inc','res',NULL,NULL),(230,NULL,'67',5,'rol','inc','res',NULL,NULL),(231,NULL,'24',5,'rol','inc','res',NULL,NULL),(232,NULL,'68',5,'rol','inc','res',NULL,NULL),(233,NULL,'90',5,'rol','inc','res',NULL,NULL),(234,NULL,'91',5,'rol','inc','res',NULL,NULL),(235,NULL,'92',5,'rol','inc','res',NULL,NULL),(236,NULL,'93',5,'rol','inc','res',NULL,NULL),(237,NULL,'50',5,'rol','inc','res',NULL,NULL),(238,NULL,'94',5,'rol','inc','res',NULL,NULL),(239,NULL,'51',5,'rol','inc','res',NULL,NULL),(240,NULL,'95',5,'rol','inc','res',NULL,NULL),(241,NULL,'52',5,'rol','inc','res',NULL,NULL),(242,NULL,'96',5,'rol','inc','res',NULL,NULL),(243,NULL,'97',5,'rol','inc','res',NULL,NULL),(244,NULL,'98',5,'rol','inc','res',NULL,NULL),(245,NULL,'47',5,'rol','inc','res',NULL,NULL),(246,NULL,'48',5,'rol','inc','res',NULL,NULL),(247,NULL,'49',5,'rol','inc','res',NULL,NULL),(248,NULL,'101',5,'rol','inc','res',NULL,NULL),(249,NULL,'80',5,'rol','inc','res',NULL,NULL),(250,NULL,'100',5,'rol','inc','res',NULL,NULL),(251,NULL,'81',5,'rol','inc','res',NULL,NULL),(252,NULL,'82',5,'rol','inc','res',NULL,NULL),(253,NULL,'102',5,'rol','inc','res',NULL,NULL),(254,NULL,'83',5,'rol','inc','res',NULL,NULL),(255,NULL,'84',5,'rol','inc','res',NULL,NULL),(256,NULL,'85',5,'rol','inc','res',NULL,NULL),(257,NULL,'86',5,'rol','inc','res',NULL,NULL),(258,NULL,'43',5,'rol','inc','res',NULL,NULL),(259,NULL,'87',5,'rol','inc','res',NULL,NULL),(260,NULL,'44',5,'rol','inc','res',NULL,NULL),(261,NULL,'88',5,'rol','inc','res',NULL,NULL),(262,NULL,'45',5,'rol','inc','res',NULL,NULL),(263,NULL,'89',5,'rol','inc','res',NULL,NULL),(264,NULL,'46',5,'rol','inc','res',NULL,NULL),(265,NULL,'69',19,'rol','inc','res',NULL,NULL),(266,NULL,'70',19,'rol','inc','res',NULL,NULL),(267,NULL,'71',19,'rol','inc','res',NULL,NULL),(268,NULL,'93',19,'rol','inc','res',NULL,NULL),(269,NULL,'72',19,'rol','inc','res',NULL,NULL),(270,NULL,'73',19,'rol','inc','res',NULL,NULL),(271,NULL,'74',19,'rol','inc','res',NULL,NULL),(272,NULL,'85',19,'rol','inc','res',NULL,NULL),(273,NULL,'75',19,'rol','inc','res',NULL,NULL),(274,NULL,'110',7,'rol','inc','res',NULL,NULL),(275,NULL,'70',7,'rol','inc','res',NULL,NULL),(276,NULL,'71',7,'rol','inc','res',NULL,NULL),(277,NULL,'93',7,'rol','inc','res',NULL,NULL),(278,NULL,'50',7,'rol','inc','res',NULL,NULL),(279,NULL,'116',7,'rol','inc','res',NULL,NULL),(280,NULL,'72',7,'rol','inc','res',NULL,NULL),(281,NULL,'51',7,'rol','inc','res',NULL,NULL),(282,NULL,'73',7,'rol','inc','res',NULL,NULL),(283,NULL,'30',7,'rol','inc','res',NULL,NULL),(284,NULL,'52',7,'rol','inc','res',NULL,NULL),(285,NULL,'74',7,'rol','inc','res',NULL,NULL),(286,NULL,'31',7,'rol','inc','res',NULL,NULL),(287,NULL,'75',7,'rol','inc','res',NULL,NULL),(288,NULL,'32',7,'rol','inc','res',NULL,NULL),(289,NULL,'76',7,'rol','inc','res',NULL,NULL),(290,NULL,'25',7,'rol','inc','res',NULL,NULL),(291,NULL,'69',7,'rol','inc','res',NULL,NULL),(292,NULL,'26',7,'rol','inc','res',NULL,NULL),(293,NULL,'49',7,'rol','inc','res',NULL,NULL),(294,NULL,'40',7,'rol','inc','res',NULL,NULL),(295,NULL,'62',7,'rol','inc','res',NULL,NULL),(296,NULL,'41',7,'rol','inc','res',NULL,NULL),(297,NULL,'85',7,'rol','inc','res',NULL,NULL),(298,NULL,'42',7,'rol','inc','res',NULL,NULL),(299,NULL,'21',7,'rol','inc','res',NULL,NULL),(300,NULL,'22',7,'rol','inc','res',NULL,NULL),(301,NULL,'23',7,'rol','inc','res',NULL,NULL),(302,NULL,'24',7,'rol','inc','res',NULL,NULL),(303,NULL,'36',7,'rol','inc','res',NULL,NULL),(304,NULL,'37',7,'rol','inc','res',NULL,NULL),(305,NULL,'38',7,'rol','inc','res',NULL,NULL),(306,NULL,'39',7,'rol','inc','res',NULL,NULL),(307,NULL,'112',3,'rol','inc','res',NULL,NULL),(308,NULL,'70',3,'rol','inc','res',NULL,NULL),(309,NULL,'114',3,'rol','inc','res',NULL,NULL),(310,NULL,'71',3,'rol','inc','res',NULL,NULL),(311,NULL,'113',3,'rol','inc','res',NULL,NULL),(312,NULL,'116',3,'rol','inc','res',NULL,NULL),(313,NULL,'72',3,'rol','inc','res',NULL,NULL),(314,NULL,'73',3,'rol','inc','res',NULL,NULL),(315,NULL,'115',3,'rol','inc','res',NULL,NULL),(316,NULL,'74',3,'rol','inc','res',NULL,NULL),(317,NULL,'31',3,'rol','inc','res',NULL,NULL),(318,NULL,'75',3,'rol','inc','res',NULL,NULL),(319,NULL,'32',3,'rol','inc','res',NULL,NULL),(320,NULL,'76',3,'rol','inc','res',NULL,NULL),(321,NULL,'77',3,'rol','inc','res',NULL,NULL),(322,NULL,'78',3,'rol','inc','res',NULL,NULL),(323,NULL,'79',3,'rol','inc','res',NULL,NULL),(324,NULL,'69',3,'rol','inc','res',NULL,NULL),(325,NULL,'62',3,'rol','inc','res',NULL,NULL),(326,NULL,'63',3,'rol','inc','res',NULL,NULL),(327,NULL,'64',3,'rol','inc','res',NULL,NULL),(328,NULL,'65',3,'rol','inc','res',NULL,NULL),(329,NULL,'66',3,'rol','inc','res',NULL,NULL),(330,NULL,'67',3,'rol','inc','res',NULL,NULL),(331,NULL,'68',3,'rol','inc','res',NULL,NULL),(332,NULL,'90',3,'rol','inc','res',NULL,NULL),(333,NULL,'91',3,'rol','inc','res',NULL,NULL),(334,NULL,'92',3,'rol','inc','res',NULL,NULL),(335,NULL,'93',3,'rol','inc','res',NULL,NULL),(336,NULL,'50',3,'rol','inc','res',NULL,NULL),(337,NULL,'94',3,'rol','inc','res',NULL,NULL),(338,NULL,'51',3,'rol','inc','res',NULL,NULL),(339,NULL,'95',3,'rol','inc','res',NULL,NULL),(340,NULL,'52',3,'rol','inc','res',NULL,NULL),(341,NULL,'96',3,'rol','inc','res',NULL,NULL),(342,NULL,'97',3,'rol','inc','res',NULL,NULL),(343,NULL,'98',3,'rol','inc','res',NULL,NULL),(344,NULL,'49',3,'rol','inc','res',NULL,NULL),(345,NULL,'101',3,'rol','inc','res',NULL,NULL),(346,NULL,'80',3,'rol','inc','res',NULL,NULL),(347,NULL,'100',3,'rol','inc','res',NULL,NULL),(348,NULL,'81',3,'rol','inc','res',NULL,NULL),(349,NULL,'82',3,'rol','inc','res',NULL,NULL),(350,NULL,'102',3,'rol','inc','res',NULL,NULL),(351,NULL,'83',3,'rol','inc','res',NULL,NULL),(352,NULL,'84',3,'rol','inc','res',NULL,NULL),(353,NULL,'85',3,'rol','inc','res',NULL,NULL),(354,NULL,'86',3,'rol','inc','res',NULL,NULL),(355,NULL,'87',3,'rol','inc','res',NULL,NULL),(356,NULL,'88',3,'rol','inc','res',NULL,NULL),(357,NULL,'89',3,'rol','inc','res',NULL,NULL),(358,NULL,'110',4,'rol','exc','res',NULL,NULL),(359,NULL,'111',4,'rol','exc','res',NULL,NULL),(360,NULL,'30',4,'rol','exc','res',NULL,NULL),(361,NULL,'33',4,'rol','exc','res',NULL,NULL),(362,NULL,'34',4,'rol','exc','res',NULL,NULL),(363,NULL,'35',4,'rol','exc','res',NULL,NULL),(364,NULL,'25',4,'rol','exc','res',NULL,NULL),(365,NULL,'26',4,'rol','exc','res',NULL,NULL),(366,NULL,'27',4,'rol','exc','res',NULL,NULL),(367,NULL,'28',4,'rol','exc','res',NULL,NULL),(368,NULL,'29',4,'rol','exc','res',NULL,NULL),(369,NULL,'60',4,'rol','exc','res',NULL,NULL),(370,NULL,'61',4,'rol','exc','res',NULL,NULL),(371,NULL,'20',4,'rol','exc','res',NULL,NULL),(372,NULL,'21',4,'rol','exc','res',NULL,NULL),(373,NULL,'22',4,'rol','exc','res',NULL,NULL),(374,NULL,'23',4,'rol','exc','res',NULL,NULL),(375,NULL,'24',4,'rol','exc','res',NULL,NULL),(376,NULL,'14',4,'rol','exc','res',NULL,NULL),(377,NULL,'58',4,'rol','exc','res',NULL,NULL),(378,NULL,'15',4,'rol','exc','res',NULL,NULL),(379,NULL,'59',4,'rol','exc','res',NULL,NULL),(380,NULL,'16',4,'rol','exc','res',NULL,NULL),(381,NULL,'17',4,'rol','exc','res',NULL,NULL),(382,NULL,'18',4,'rol','exc','res',NULL,NULL),(383,NULL,'19',4,'rol','exc','res',NULL,NULL),(384,NULL,'53',4,'rol','exc','res',NULL,NULL),(385,NULL,'10',4,'rol','exc','res',NULL,NULL),(386,NULL,'54',4,'rol','exc','res',NULL,NULL),(387,NULL,'11',4,'rol','exc','res',NULL,NULL),(388,NULL,'55',4,'rol','exc','res',NULL,NULL),(389,NULL,'12',4,'rol','exc','res',NULL,NULL),(390,NULL,'56',4,'rol','exc','res',NULL,NULL),(391,NULL,'13',4,'rol','exc','res',NULL,NULL),(392,NULL,'57',4,'rol','exc','res',NULL,NULL),(393,NULL,'47',4,'rol','exc','res',NULL,NULL),(394,NULL,'48',4,'rol','exc','res',NULL,NULL),(395,NULL,'103',4,'rol','exc','res',NULL,NULL),(396,NULL,'105',4,'rol','exc','res',NULL,NULL),(397,NULL,'40',4,'rol','exc','res',NULL,NULL),(398,NULL,'104',4,'rol','exc','res',NULL,NULL),(399,NULL,'107',4,'rol','exc','res',NULL,NULL),(400,NULL,'41',4,'rol','exc','res',NULL,NULL),(401,NULL,'42',4,'rol','exc','res',NULL,NULL),(402,NULL,'106',4,'rol','exc','res',NULL,NULL),(403,NULL,'109',4,'rol','exc','res',NULL,NULL),(404,NULL,'43',4,'rol','exc','res',NULL,NULL),(405,NULL,'108',4,'rol','exc','res',NULL,NULL),(406,NULL,'44',4,'rol','exc','res',NULL,NULL),(407,NULL,'45',4,'rol','exc','res',NULL,NULL),(408,NULL,'46',4,'rol','exc','res',NULL,NULL),(409,NULL,'4',4,'rol','exc','res',NULL,NULL),(410,NULL,'36',4,'rol','exc','res',NULL,NULL),(411,NULL,'3',4,'rol','exc','res',NULL,NULL),(412,NULL,'37',4,'rol','exc','res',NULL,NULL),(413,NULL,'2',4,'rol','exc','res',NULL,NULL),(414,NULL,'38',4,'rol','exc','res',NULL,NULL),(415,NULL,'1',4,'rol','exc','res',NULL,NULL),(416,NULL,'39',4,'rol','exc','res',NULL,NULL),(417,NULL,'9',4,'rol','exc','res',NULL,NULL),(418,NULL,'8',4,'rol','exc','res',NULL,NULL),(419,NULL,'7',4,'rol','exc','res',NULL,NULL),(420,NULL,'6',4,'rol','exc','res',NULL,NULL),(421,NULL,'5',4,'rol','exc','res',NULL,NULL),(422,NULL,'1',21,'usr','dept','row',NULL,NULL),(423,NULL,'12',21,'usr','dept','row',NULL,NULL),(424,NULL,'2',21,'usr','dept','row',NULL,NULL),(425,NULL,'8',21,'usr','dept','row',NULL,NULL),(426,NULL,'9',21,'usr','dept','row',NULL,NULL),(439,1,'93',20,'rol','inc','res','0','2024-07-06 12:49:53'),(440,1,'62',20,'rol','inc','res','0','2024-07-06 12:49:53'),(441,1,'85',20,'rol','inc','res','0','2024-07-06 12:49:53'),(442,1,'31',20,'rol','inc','res','0','2024-07-06 12:49:53'),(443,1,'21',20,'rol','inc','res','0','2024-07-06 12:49:53'),(444,1,'32',20,'rol','inc','res','0','2024-07-06 12:49:53'),(445,1,'76',20,'rol','inc','res','0','2024-07-06 12:49:53'),(446,1,'11',20,'rol','inc','res','0','2024-07-06 12:49:53'),(447,1,'36',20,'rol','inc','res','0','2024-07-06 12:49:53'),(448,1,'69',20,'rol','inc','res','0','2024-07-06 12:49:53'),(449,1,'16',20,'rol','inc','res','0','2024-07-06 12:49:53'),(450,1,'49',20,'rol','inc','res','0','2024-07-06 12:49:53'),(451,1,'1',20,'rol','inc','res','0','2024-07-06 12:49:53');

--
-- Table structure for table `uum_role`
--
DROP TABLE IF EXISTS `uum_role`;
CREATE TABLE `uum_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `org_id` bigint DEFAULT NULL COMMENT '机构id',
  `name` varchar(10) DEFAULT '' COMMENT '角色名称',
  `level` tinyint DEFAULT NULL COMMENT '角色级别',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `reg_default` varchar(20) DEFAULT NULL COMMENT '注册默认角色, 只允许一个',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`),
  KEY `rol_idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO `uum_role` VALUES (0,1,'无角色用户',NULL,'enable','',NULL,NULL,NULL,'0',NULL),(1,1,'管理员',1,'enable','',NULL,NULL,NULL,'0','2011-02-11 16:57:01'),(3,1,'总经理',1,'enable','',NULL,NULL,NULL,'0','2011-12-20 11:37:31'),(4,1,'副总经理',2,'enable','',NULL,NULL,NULL,'0','2011-12-23 09:37:13'),(5,1,'人事经理',3,'enable','',NULL,NULL,NULL,'0','2011-12-23 16:33:19'),(6,1,'储运经理',3,'enable','',NULL,NULL,NULL,'0','2011-12-23 17:21:56'),(7,1,'运营经理',3,'enable','',NULL,NULL,NULL,'0','2011-12-23 17:22:24'),(8,1,'财务经理',3,'enable','',NULL,NULL,NULL,'0','2011-12-26 09:54:22'),(9,1,'培训经理',3,'enable','培训经理',NULL,NULL,NULL,'0','2011-12-26 09:54:33'),(10,1,'采购经理',3,'enable','工',NULL,NULL,NULL,'0','2012-01-10 14:23:10'),(11,1,'人事主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 11:50:09'),(12,1,'储运主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 11:50:31'),(13,1,'运营主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:06:28'),(14,1,'财务主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:06:51'),(15,1,'培训主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:07:17'),(16,1,'采购主管',4,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:07:33'),(17,1,'店长',5,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:58:26'),(18,1,'收银',6,'enable','',NULL,NULL,NULL,'0','2012-03-06 14:58:44'),(19,1,'店员',7,'enable','',NULL,'0','2024-06-28 14:23:53','0','2012-03-06 14:59:00'),(20,1,'注册体验角色',1,'enable','','web','0','2024-07-06 04:05:56','0','2024-07-06 04:02:25');

--
-- Table structure for table `uum_user`
--
DROP TABLE IF EXISTS `uum_user`;
CREATE TABLE `uum_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `org_id` bigint DEFAULT '0' COMMENT '用户所属机构',
  `account_id` bigint DEFAULT NULL COMMENT '账号id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `dept_id` bigint DEFAULT NULL COMMENT '部门id',
  `name` varchar(20) DEFAULT '' COMMENT '用户名',
  `pinyin` varchar(20) DEFAULT '' COMMENT '用户名拼音',
  `phone` varchar(20) DEFAULT '' COMMENT '手机号',
  `email` varchar(30) DEFAULT '' COMMENT '邮箱',
  `gender` varchar(2) DEFAULT '' COMMENT '用户性别',
  `position` varchar(30) DEFAULT '' COMMENT '用户职位',
  `photo_url` varchar(200) DEFAULT '' COMMENT '用户照片',
  `avidate` date DEFAULT NULL COMMENT '用户有效期',
  `address` varchar(100) DEFAULT '' COMMENT '家庭住址',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `updator` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  KEY `usr_idx_org_id` (`org_id`),
  KEY `usr_idx_account_id` (`account_id`) /*!80000 INVISIBLE */,
  CONSTRAINT `fk_belong_org_reference_uum_org` FOREIGN KEY (`org_id`) REFERENCES `uum_organ` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO `uum_user` VALUES (0,1,1,NULL,1,'管理员','gly','13828000001',NULL,NULL,NULL,'http://localhost:8080/mycuckoo/upload/photo/20171014181342_5pnIdj.jpg',NULL,NULL,'enable',NULL,NULL,NULL,'0','2011-02-11 16:57:01'),(12,1,2,NULL,2,'王娟','wj','13828000002','','1','','','2012-06-21','','enable','',NULL,NULL,'0','2011-12-21 10:09:20'),(13,1,3,NULL,12,'石纪红','sjh','13828000003','','1','','','2012-07-10','','enable','',NULL,NULL,'0','2012-01-10 14:55:24'),(14,1,4,NULL,12,'杨文菊','ywj','13828000004','','1','','','2012-08-28','','enable','',NULL,NULL,'0','2012-02-28 15:23:30'),(15,1,5,NULL,8,'于志平','yzp','13828000005','','0','','','2012-09-06','','enable','',NULL,NULL,'0','2012-03-06 15:15:09'),(16,1,6,NULL,3,'张世鹏','zsp','13828000006','','0','','','2012-09-06','','enable','',NULL,NULL,'0','2012-03-06 15:15:39'),(17,1,7,NULL,7,'张丽','zl','13828000007','','1','','','2012-09-06','','enable','',NULL,NULL,'0','2012-03-06 15:16:22'),(18,1,8,NULL,7,'高晓峰','gxf','13828000008','','0','','','2012-09-06','','enable','',NULL,NULL,'0','2012-03-06 15:21:33'),(19,1,9,NULL,7,'王旭','wx','13828000009','','0','','','2012-09-06','','enable','',NULL,NULL,'0','2012-03-06 15:21:57'),(20,1,10,7,7,'王帅明','wsm','13828000010','','0','','','2012-09-06','','enable','','0','2024-06-22 13:35:36','0','2012-03-06 15:23:09'),(21,1,11,1,9,'张军','zj','13828000011','','0','','http://localhost:8080/mycuckoo/upload/photo/haha_UPwWAD.jpg','2012-08-31','','enable','','0','2024-06-23 03:29:20','0','2012-03-06 15:23:40'),(25,3,12,-1,NULL,'张三','zs','13700000001',NULL,'0',NULL,NULL,NULL,NULL,'enable',NULL,NULL,'2024-07-06 04:13:49',NULL,'2024-07-06 04:13:49');
