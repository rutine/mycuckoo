CREATE SCHEMA IF NOT EXISTS CUCKOO;

SET SCHEMA CUCKOO;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for syspl_accessory
-- ----------------------------
DROP TABLE IF EXISTS `syspl_accessory`;
CREATE TABLE `syspl_accessory` (
  `accessory_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `info_id` bigint(20) DEFAULT NULL COMMENT '公告ID',
  `accessory_name` varchar(100) DEFAULT NULL COMMENT '附件名称',
  PRIMARY KEY (`accessory_id`)
) COMMENT='公告附件表';

-- ----------------------------
-- Records of syspl_accessory
-- ----------------------------
INSERT INTO `syspl_accessory` VALUES ('2', '2', 'cRopIJ_blog_fifth.bmp');
INSERT INTO `syspl_accessory` VALUES ('3', '2', 'jba458_blog_second.bmp');
INSERT INTO `syspl_accessory` VALUES ('4', '2', 'b6wewK_blog_second.bmp');
INSERT INTO `syspl_accessory` VALUES ('7', '3', 'Ihwp1f_blog_fourth.bmp');
INSERT INTO `syspl_accessory` VALUES ('11', '6', '20171014181342.jpg');

-- ----------------------------
-- Table structure for syspl_affiche
-- ----------------------------
DROP TABLE IF EXISTS `syspl_affiche`;
CREATE TABLE `syspl_affiche` (
  `affiche_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `affiche_title` varchar(100) DEFAULT '' COMMENT '公告标题',
  `affiche_invalidate` date DEFAULT NULL COMMENT '公告有效期',
  `affiche_pulish` tinyint(2) DEFAULT NULL COMMENT '是否发布',
  `affiche_content` text COMMENT '公告内容',
  PRIMARY KEY (`affiche_id`)
) COMMENT='系统公告表';

-- ----------------------------
-- Records of syspl_affiche
-- ----------------------------
INSERT INTO `syspl_affiche` VALUES ('6', '第二版本发布了！', '2017-05-01', '0', '<u>​啊啊啊啊啊啊啊啊</u>');
INSERT INTO `syspl_affiche` VALUES ('12', '2.0版本', '2017-11-30', '0', '测试数据');

-- ----------------------------
-- Table structure for syspl_code
-- ----------------------------
DROP TABLE IF EXISTS `syspl_code`;
CREATE TABLE `syspl_code` (
  `code_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code_eng_name` varchar(20) DEFAULT NULL COMMENT '英文编码名称',
  `code_name` varchar(20) DEFAULT NULL COMMENT '中文编码名称',
  `module_name` varchar(20) DEFAULT NULL COMMENT '模块名称',
  `delimite` varchar(2) DEFAULT NULL COMMENT 'delimite分隔符',
  `part_num` int(11) DEFAULT NULL COMMENT '段数',
  `part1` varchar(20) DEFAULT NULL,
  `part1_con` varchar(20) DEFAULT NULL,
  `part2` varchar(20) DEFAULT NULL,
  `part2_con` varchar(20) DEFAULT NULL,
  `part3` varchar(20) DEFAULT NULL,
  `part3_con` varchar(20) DEFAULT NULL,
  `part4` varchar(20) DEFAULT NULL,
  `part4_con` varchar(20) DEFAULT NULL,
  `code_effect` varchar(50) DEFAULT NULL COMMENT '编码效果',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code_id`)
) COMMENT='系统编码表';

-- ----------------------------
-- Records of syspl_code
-- ----------------------------
INSERT INTO `syspl_code` VALUES ('25', 'RKD', '入库单', '用户信息管理', ':', '4', 'char', 'RKD', 'date', 'yyyyMM', 'number', '001', 'sysPara', 'userName', 'RKD:201812:001:zhangsan', 'enable', '', 'admin', '2012-01-08 16:40:01');

-- ----------------------------
-- Table structure for syspl_dic_big_type
-- ----------------------------
DROP TABLE IF EXISTS `syspl_dic_big_type`;
CREATE TABLE `syspl_dic_big_type` (
  `big_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `big_type_name` varchar(20) DEFAULT NULL COMMENT '字典大类名',
  `big_type_code` varchar(15) DEFAULT NULL COMMENT '字典大类号',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`big_type_id`)
) COMMENT='字典大类表';

-- ----------------------------
-- Records of syspl_dic_big_type
-- ----------------------------
INSERT INTO `syspl_dic_big_type` VALUES ('21', '系统类别', 'systemType', 'enable', '', 'admin', '2011-12-20 14:32:22');
INSERT INTO `syspl_dic_big_type` VALUES ('22', '地区级别', 'district', 'enable', '', 'admin', '2011-12-20 15:11:30');
INSERT INTO `syspl_dic_big_type` VALUES ('23', '机构类型', 'organType', 'enable', '', 'admin', '2011-12-21 09:33:12');
INSERT INTO `syspl_dic_big_type` VALUES ('25', '页面类型', 'modPageType', 'enable', '', 'admin', '2012-01-13 12:09:37');

-- ----------------------------
-- Table structure for syspl_dic_small_type
-- ----------------------------
DROP TABLE IF EXISTS `syspl_dic_small_type`;
CREATE TABLE `syspl_dic_small_type` (
  `small_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `big_type_id` bigint(20) NOT NULL COMMENT '字典大类ID',
  `small_type_name` varchar(20) DEFAULT NULL COMMENT '字典小类名',
  `small_type_code` varchar(15) DEFAULT NULL COMMENT '字典小类号',
  PRIMARY KEY (`small_type_id`),
  KEY `fk_syspl_ty_reference_syspl_di` (`big_type_id`),
  CONSTRAINT `fk_syspl_ty_reference_syspl_di` FOREIGN KEY (`big_type_id`) REFERENCES `syspl_dic_big_type` (`big_type_id`)
) COMMENT='字典小类表';

-- ----------------------------
-- Records of syspl_dic_small_type
-- ----------------------------
INSERT INTO `syspl_dic_small_type` VALUES ('37', '21', '系统平台', 'platform');
INSERT INTO `syspl_dic_small_type` VALUES ('38', '21', '统一用户', 'uum');
INSERT INTO `syspl_dic_small_type` VALUES ('47', '23', '分公司', 'branch');
INSERT INTO `syspl_dic_small_type` VALUES ('48', '23', '门店', 'store');
INSERT INTO `syspl_dic_small_type` VALUES ('49', '23', '部门', 'department');
INSERT INTO `syspl_dic_small_type` VALUES ('50', '22', '省', 'province');
INSERT INTO `syspl_dic_small_type` VALUES ('51', '22', '市', 'city');
INSERT INTO `syspl_dic_small_type` VALUES ('52', '22', '县', 'town');
INSERT INTO `syspl_dic_small_type` VALUES ('53', '22', '区', 'district');
INSERT INTO `syspl_dic_small_type` VALUES ('64', '25', 'html', 'html');
INSERT INTO `syspl_dic_small_type` VALUES ('65', '25', 'js', 'js');
INSERT INTO `syspl_dic_small_type` VALUES ('66', '25', 'jsp', 'jsp');

-- ----------------------------
-- Table structure for syspl_district
-- ----------------------------
DROP TABLE IF EXISTS `syspl_district`;
CREATE TABLE `syspl_district` (
  `district_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `district_name` varchar(20) DEFAULT NULL COMMENT '地区名称',
  `district_code` varchar(20) DEFAULT NULL COMMENT '地区编码',
  `district_postal` varchar(6) DEFAULT NULL COMMENT '邮政',
  `district_telcode` varchar(10) DEFAULT NULL,
  `district_level` varchar(20) DEFAULT NULL COMMENT '地区级别',
  `dis_parent_id` bigint(20) DEFAULT NULL COMMENT '上级ID',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`district_id`),
  KEY `district_name_index` (`district_name`),
  KEY `fk_syspl_di_reference_syspl_di` (`dis_parent_id`),
  CONSTRAINT `fk_syspl_di_reference_syspl_di` FOREIGN KEY (`dis_parent_id`) REFERENCES `syspl_district` (`district_id`)
) COMMENT='地区表';

-- ----------------------------
-- Records of syspl_district
-- ----------------------------
INSERT INTO `syspl_district` VALUES ('0', '地区树', '0', null, null, null, '0', 'enable', null, null, null);
INSERT INTO `syspl_district` VALUES ('2000', '北京', 'beijing', '', '', 'province', '0', 'enable', '', 'admin', '2011-12-20 15:12:04');
INSERT INTO `syspl_district` VALUES ('2001', '北海', 'beihai', '', '', 'town', '2000', 'disable', '', 'admin', '2011-12-26 09:55:00');
INSERT INTO `syspl_district` VALUES ('2002', '海淀', 'haiding', '', '', 'town', '2000', 'enable', '', 'admin', '2012-01-06 16:11:32');
INSERT INTO `syspl_district` VALUES ('2003', '上海', '', '', '', 'province', '0', 'enable', '', 'admin', '2012-03-06 14:38:46');
INSERT INTO `syspl_district` VALUES ('2004', '天津', '', '', '', 'city', '0', 'enable', '', 'admin', '2012-03-06 14:39:22');
INSERT INTO `syspl_district` VALUES ('2005', '深圳', '', '', '', 'city', '0', 'enable', '', 'admin', '2012-03-06 14:39:37');
INSERT INTO `syspl_district` VALUES ('2006', '广州', '6008', '020', '020', 'province', '0', 'enable', '', 'admin', '2014-10-18 11:40:29');

-- ----------------------------
-- Table structure for syspl_module_menu
-- ----------------------------
DROP TABLE IF EXISTS `syspl_module_menu`;
CREATE TABLE `syspl_module_menu` (
  `module_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mod_name` varchar(10) DEFAULT NULL COMMENT '模块名称',
  `mod_en_name` varchar(40) DEFAULT NULL COMMENT '模块英文名',
  `mod_icon_cls` varchar(30) DEFAULT NULL COMMENT '模块图标',
  `mod_level` varchar(2) DEFAULT NULL COMMENT '模块级别',
  `mod_order` int(11) DEFAULT NULL COMMENT '模块顺序',
  `mod_parent_id` bigint(20) DEFAULT NULL COMMENT '上级模块ID',
  `belong_to_sys` varchar(15) DEFAULT NULL COMMENT '所属系统',
  `mod_page_type` varchar(20) DEFAULT NULL COMMENT '页面类型',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`module_id`),
  KEY `mod_name_index` (`mod_name`),
  KEY `fk_syspl_mo_reference_syspl_memu` (`mod_parent_id`),
  CONSTRAINT `fk_syspl_mo_reference_syspl_memu` FOREIGN KEY (`mod_parent_id`) REFERENCES `syspl_module_menu` (`module_id`)
) COMMENT='模块菜单表';

-- ----------------------------
-- Records of syspl_module_menu
-- ----------------------------
INSERT INTO `syspl_module_menu` VALUES ('0', '模块树', 'moduleTree', 'module-tree-icon', '0', '1', '0', null, null, 'enable', '', 'admin', '2010-07-27 09:29:36');
INSERT INTO `syspl_module_menu` VALUES ('1', '系统平台', 'platform', 'platform-icon', '1', '1', '0', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 13:43:34');
INSERT INTO `syspl_module_menu` VALUES ('2', '统一用户', 'uum', 'uum-icon', '1', '2', '0', 'uum', 'html', 'enable', '', 'admin', '2010-08-20 13:44:29');
INSERT INTO `syspl_module_menu` VALUES ('6', '菜单管理', 'moduleOptMgr', 'module-operate-icon', '2', '3', '1', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:20:15');
INSERT INTO `syspl_module_menu` VALUES ('7', '操作菜单管理', 'moduleMgr', 'module-mgr-icon', '3', '1', '6', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:21:50');
INSERT INTO `syspl_module_menu` VALUES ('8', '操作按钮管理', 'operateMgr', 'operate-mgr-icon', '3', '2', '6', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:22:45');
INSERT INTO `syspl_module_menu` VALUES ('9', '字典管理', 'dictionaryMgr', 'dictionary-mgr-icon', '2', '4', '1', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:27:12');
INSERT INTO `syspl_module_menu` VALUES ('10', '字典管理', 'dictionaryMgr', 'type-dictionary-mgr-icon', '3', '1', '9', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:30:13');
INSERT INTO `syspl_module_menu` VALUES ('12', '系统管理', 'systemMgr', 'system-mgr-icon', '2', '1', '1', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:32:28');
INSERT INTO `syspl_module_menu` VALUES ('13', '系统编码管理', 'codeMgr', 'code-mgr-icon', '3', '2', '12', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:33:31');
INSERT INTO `syspl_module_menu` VALUES ('14', '省市地区管理', 'districtMgr', 'district-mgr-icon', '3', '1', '52', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:34:52');
INSERT INTO `syspl_module_menu` VALUES ('15', '系统参数管理', 'systemParameterMgr', 'system-parameter-mgr-icon', '3', '3', '12', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:35:39');
INSERT INTO `syspl_module_menu` VALUES ('17', '日志管理', 'logMgr', 'log-mgr-icon', '2', '5', '1', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:38:46');
INSERT INTO `syspl_module_menu` VALUES ('18', '系统日志管理', 'systemLogMgr', 'system-log-mgr-icon', '3', '1', '17', 'platform', 'html', 'enable', '', 'admin', '2010-08-20 14:39:33');
INSERT INTO `syspl_module_menu` VALUES ('19', '机构管理', 'organMgrTitle', 'organ-mgr-title-icon', '2', '1', '2', 'uum', 'html', 'enable', '', 'admin', '2010-08-21 13:33:29');
INSERT INTO `syspl_module_menu` VALUES ('21', '角色管理', 'roleMgr', 'role-mgr-title-icon', '2', '2', '2', 'uum', 'html', 'enable', '', 'admin', '2010-09-13 21:04:30');
INSERT INTO `syspl_module_menu` VALUES ('22', '用户管理', 'userMgr', 'user-mgr-title-icon', '2', '3', '2', 'uum', 'html', 'enable', '', 'admin', '2010-09-13 21:05:20');
INSERT INTO `syspl_module_menu` VALUES ('25', '系统配置管理', 'systemConfigMgr', 'system-config-mgr-icon', '3', '1', '12', 'platform', 'html', 'enable', '', 'admin', '2010-09-13 21:09:13');
INSERT INTO `syspl_module_menu` VALUES ('32', '机构信息管理', 'organMgr', 'organ-mgr-icon', '3', '1', '19', 'uum', 'html', 'enable', '', 'admin', '2010-09-13 21:14:44');
INSERT INTO `syspl_module_menu` VALUES ('33', '角色信息管理', 'roleMgr', 'role-mgr-icon', '3', '1', '21', 'uum', 'html', 'enable', '', 'admin', '2010-09-13 21:15:10');
INSERT INTO `syspl_module_menu` VALUES ('34', '用户信息管理', 'userMgr', 'user-mgr-icon', '3', '1', '22', 'uum', 'html', 'enable', '', 'admin', '2010-09-13 21:16:03');
INSERT INTO `syspl_module_menu` VALUES ('35', '角色分配管理', 'roleAssignMgr', 'role-assign-mgr-icon', '3', '1', '21', 'uum', 'html', 'enable', '', 'admin', '2010-09-14 10:29:14');
INSERT INTO `syspl_module_menu` VALUES ('49', '系统公告管理', 'afficheMgr', 'affiche-mgr-icon', '3', '4', '12', 'platform', 'html', 'enable', '', 'admin', '2010-12-01 10:47:27');
INSERT INTO `syspl_module_menu` VALUES ('52', '省市地区', 'districtMgr', 'district-mgr-title-icon', '2', '3', '1', 'platform', 'html', 'enable', '', 'admin', '2011-01-17 10:16:14');
INSERT INTO `syspl_module_menu` VALUES ('53', '系统调度管理', 'schedulerMgr', 'scheduler-mgr-icon', '3', '5', '12', 'platform', 'html', 'enable', '', 'admin', '2011-01-17 10:34:43');

-- ----------------------------
-- Table structure for syspl_operate
-- ----------------------------
DROP TABLE IF EXISTS `syspl_operate`;
CREATE TABLE `syspl_operate` (
  `operate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `opt_name` varchar(10) DEFAULT NULL COMMENT '操作名',
  `opt_icon_cls` varchar(20) DEFAULT NULL COMMENT '图标',
  `opt_order` int(11) DEFAULT NULL COMMENT '顺序',
  `opt_group` int(11) DEFAULT NULL COMMENT '所属分组',
  `opt_link` varchar(20) DEFAULT NULL COMMENT '动作链接',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`operate_id`)
) COMMENT='操作按钮表';

-- ----------------------------
-- Table structure for syspl_mod_opt_ref
-- ----------------------------
DROP TABLE IF EXISTS `syspl_mod_opt_ref`;
CREATE TABLE `syspl_mod_opt_ref` (
  `mod_opt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_id` bigint(20) DEFAULT NULL COMMENT '模块ID',
  `operate_id` bigint(20) DEFAULT NULL COMMENT '操作ID',
  PRIMARY KEY (`mod_opt_id`),
  KEY `fk_syspl_mo_reference_syspl_mo` (`module_id`),
  KEY `fk_syspl_mo_reference_syspl_op` (`operate_id`),
  CONSTRAINT `fk_syspl_mo_reference_syspl_mo` FOREIGN KEY (`module_id`) REFERENCES `syspl_module_menu` (`module_id`),
  CONSTRAINT `fk_syspl_mo_reference_syspl_op` FOREIGN KEY (`operate_id`) REFERENCES `syspl_operate` (`operate_id`)
) COMMENT='模块操作关系表';

-- ----------------------------
-- Records of syspl_mod_opt_ref
-- ----------------------------
INSERT INTO `syspl_mod_opt_ref` VALUES ('5', '7', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('6', '7', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('7', '7', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('10', '8', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('11', '8', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('12', '8', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('13', '8', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('14', '8', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('15', '10', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('16', '10', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('17', '10', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('18', '10', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('19', '10', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('25', '13', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('26', '13', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('27', '13', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('28', '13', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('29', '13', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('30', '14', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('31', '14', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('33', '14', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('34', '14', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('35', '14', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('36', '15', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('37', '15', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('38', '15', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('39', '15', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('40', '15', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('44', '18', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('45', '32', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('46', '32', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('47', '32', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('48', '32', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('49', '32', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('50', '33', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('51', '33', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('52', '33', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('53', '33', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('54', '33', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('55', '33', '8');
INSERT INTO `syspl_mod_opt_ref` VALUES ('56', '35', '9');
INSERT INTO `syspl_mod_opt_ref` VALUES ('57', '35', '10');
INSERT INTO `syspl_mod_opt_ref` VALUES ('60', '34', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('61', '34', '5');
INSERT INTO `syspl_mod_opt_ref` VALUES ('62', '34', '6');
INSERT INTO `syspl_mod_opt_ref` VALUES ('87', '7', '7');
INSERT INTO `syspl_mod_opt_ref` VALUES ('91', '34', '11');
INSERT INTO `syspl_mod_opt_ref` VALUES ('97', '34', '13');
INSERT INTO `syspl_mod_opt_ref` VALUES ('98', '49', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('99', '49', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('100', '49', '3');
INSERT INTO `syspl_mod_opt_ref` VALUES ('101', '49', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('104', '53', '1');
INSERT INTO `syspl_mod_opt_ref` VALUES ('105', '53', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('106', '53', '3');
INSERT INTO `syspl_mod_opt_ref` VALUES ('107', '53', '4');
INSERT INTO `syspl_mod_opt_ref` VALUES ('108', '53', '16');
INSERT INTO `syspl_mod_opt_ref` VALUES ('109', '53', '17');
INSERT INTO `syspl_mod_opt_ref` VALUES ('110', '53', '18');
INSERT INTO `syspl_mod_opt_ref` VALUES ('111', '53', '19');
INSERT INTO `syspl_mod_opt_ref` VALUES ('152', '34', '28');
INSERT INTO `syspl_mod_opt_ref` VALUES ('155', '7', '14');
INSERT INTO `syspl_mod_opt_ref` VALUES ('156', '7', '3');
INSERT INTO `syspl_mod_opt_ref` VALUES ('158', '34', '2');
INSERT INTO `syspl_mod_opt_ref` VALUES ('159', '34', '1');

-- ----------------------------
-- Records of syspl_operate
-- ----------------------------
INSERT INTO `syspl_operate` VALUES ('1', '增加', 'table-add-icon', '1', '1', 'add', 'enable', '增加', 'admin', '2010-10-28 15:16:25');
INSERT INTO `syspl_operate` VALUES ('2', '修改', 'table-edit-icon', '2', '1', 'modify', 'enable', '', 'admin', '2010-10-28 15:27:36');
INSERT INTO `syspl_operate` VALUES ('3', '删除', 'table-delete-icon', '3', '1', 'delete', 'enable', '', 'admin', '2010-10-28 15:30:29');
INSERT INTO `syspl_operate` VALUES ('4', '查看', 'table-icon', '4', '1', 'view', 'enable', '', 'admin', '2010-10-28 19:07:11');
INSERT INTO `syspl_operate` VALUES ('5', '启用', 'enable-icon', '5', '1', 'enable', 'enable', '', 'admin', '2010-10-29 09:01:43');
INSERT INTO `syspl_operate` VALUES ('6', '停用', 'disable-icon', '6', '1', 'disable', 'enable', '停用', 'admin', '2010-10-29 09:02:11');
INSERT INTO `syspl_operate` VALUES ('7', '模块分配操作', 'assign-icon', '7', '1', 'optassign', 'enable', '', 'admin', '2010-11-01 20:07:40');
INSERT INTO `syspl_operate` VALUES ('8', '普通权限分配', 'assign-icon', '8', '1', 'optpri', 'enable', '', 'admin', '2010-11-02 11:42:42');
INSERT INTO `syspl_operate` VALUES ('9', '角色分配', 'assign-icon', '6', '1', 'roleassign', 'enable', '', 'admin', '2010-11-02 11:44:10');
INSERT INTO `syspl_operate` VALUES ('10', '角色删除', 'table-delete-icon', '7', '1', 'roledel', 'enable', '', 'admin', '2010-11-02 11:44:39');
INSERT INTO `syspl_operate` VALUES ('11', '分配角色', 'role-mgr-icon', '6', '1', 'assignrole', 'enable', '', 'admin', '2010-11-02 11:46:31');
INSERT INTO `syspl_operate` VALUES ('12', '行权限', 'privilege-mgr-icon', '8', '1', 'rowprivilege', 'enable', '', 'admin', '2010-11-07 14:01:16');
INSERT INTO `syspl_operate` VALUES ('13', '重置密码', 'reset-pwd-icon', '7', '1', 'resetpwd', 'enable', '重置密码', 'admin', '2010-11-07 18:14:23');
INSERT INTO `syspl_operate` VALUES ('14', '模块标签修改', 'module-label-icon', '8', '1', 'moduleLabel', 'enable', '', 'admin', '2010-12-26 10:23:29');
INSERT INTO `syspl_operate` VALUES ('15', '保留时间', 'save-time-icon', '5', '1', 'saveTime', 'enable', '', 'admin', '2010-12-30 16:05:31');
INSERT INTO `syspl_operate` VALUES ('16', '启动调度器', 'scheduler-start-icon', '5', '1', 'schedulerStart', 'enable', '', 'admin', '2011-01-17 14:20:06');
INSERT INTO `syspl_operate` VALUES ('17', '停止调度器', 'scheduler-stop-icon', '6', '1', 'schedulerStop', 'enable', '', 'admin', '2011-01-17 14:20:54');
INSERT INTO `syspl_operate` VALUES ('18', '启动任务', 'job-start-icon', '7', '1', 'jobStart', 'enable', '', 'admin', '2011-01-17 15:01:19');
INSERT INTO `syspl_operate` VALUES ('19', '停止任务', 'job-stop-icon', '8', '1', 'jobStop', 'enable', '', 'admin', '2011-01-17 15:05:07');
INSERT INTO `syspl_operate` VALUES ('28', '特殊权限分配', 'assign-icon', '8', '1', 'optpri', 'enable', '', 'admin', '2012-02-08 11:16:26');

-- ----------------------------
-- Table structure for syspl_scheduler_job
-- ----------------------------
DROP TABLE IF EXISTS `syspl_scheduler_job`;
CREATE TABLE `syspl_scheduler_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(30) DEFAULT NULL COMMENT '任务名称',
  `job_class` varchar(100) DEFAULT NULL COMMENT '任务执行的类名',
  `trigger_type` varchar(20) DEFAULT NULL COMMENT '任务触发类型',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT '时间表达式',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `repeat_count` int(11) DEFAULT NULL COMMENT '重复次数',
  `interval_time` bigint(20) DEFAULT NULL COMMENT '间隔时间',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`)
) COMMENT='定时任务表';

-- ----------------------------
-- Records of syspl_scheduler_job
-- ----------------------------
INSERT INTO `syspl_scheduler_job` VALUES ('1', '日志清理', 'com.mycuckoo.service.platform.job.LoggerJob', 'Cron', '* * 0 15 * ?', '2011-01-19 00:00:00', '2011-02-19 00:00:00', '2', '2', 'disable', '每月15日0点清除日志', 'admin', '2011-01-19 15:28:44');
INSERT INTO `syspl_scheduler_job` VALUES ('2', '测试', 'com.mycuckoo.service.platform.job.TestJob', 'Simple', '', '2019-01-28 00:00:00', '2019-01-29 00:00:00', '2', '2', 'disable', '', 'admin', '2019-01-28 14:07:16');

-- ----------------------------
-- Table structure for syspl_sys_opt_log
-- ----------------------------
DROP TABLE IF EXISTS `syspl_sys_opt_log`;
CREATE TABLE `syspl_sys_opt_log` (
  `opt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `opt_mod_name` varchar(20) DEFAULT NULL COMMENT '模块名称',
  `opt_name` varchar(10) DEFAULT NULL COMMENT '操作名称',
  `opt_content` varchar(1000) DEFAULT NULL COMMENT '操作内容',
  `opt_business_id` varchar(20) DEFAULT NULL COMMENT '业务ID',
  `opt_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '操作时间',
  `opt_pc_name` varchar(25) DEFAULT NULL COMMENT '主机名称',
  `opt_pc_ip` varchar(25) DEFAULT NULL COMMENT '主机IP',
  `opt_user_name` varchar(20) DEFAULT NULL COMMENT '用户名称',
  `opt_user_role` varchar(20) DEFAULT NULL COMMENT '用户角色',
  `opt_user_ogan` varchar(20) DEFAULT NULL COMMENT '用户机构',
  PRIMARY KEY (`opt_id`),
  KEY `opt_mod_name_index` (`opt_mod_name`),
  KEY `opt_time_index` (`opt_time`),
  KEY `opt_user_name_index` (`opt_user_name`),
  KEY `opt_user_ogan_index` (`opt_user_ogan`),
  KEY `opt_user_role_index` (`opt_user_role`)
) COMMENT='系统操作日志表';

-- ----------------------------
-- Table structure for syspl_sys_parameter
-- ----------------------------
DROP TABLE IF EXISTS `syspl_sys_parameter`;
CREATE TABLE `syspl_sys_parameter` (
  `para_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `para_name` varchar(20) DEFAULT NULL COMMENT '参数名称',
  `para_key_name` varchar(20) DEFAULT NULL COMMENT '参数键名称',
  `para_value` varchar(20) DEFAULT NULL COMMENT '参数值',
  `para_type` varchar(20) DEFAULT NULL COMMENT '参数类型',
  `status` varchar(15) NOT NULL COMMENT '状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`para_id`),
  KEY `para_key_name_index` (`para_key_name`)
) COMMENT='系统参数表';

-- ----------------------------
-- Records of syspl_sys_parameter
-- ----------------------------
INSERT INTO `syspl_sys_parameter` VALUES ('1', '用户默认密码', 'userdefaultpassword', '123456', 'platform', 'enable', '', 'admin', '2011-12-20 14:48:25');
INSERT INTO `syspl_sys_parameter` VALUES ('2', '用户有效期', 'uservalidate', '6', 'uum', 'enable', '', 'admin', '2011-12-20 14:48:55');

-- ----------------------------
-- Table structure for uum_organ
-- ----------------------------
DROP TABLE IF EXISTS `uum_organ`;
CREATE TABLE `uum_organ` (
  `org_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `org_simple_name` varchar(10) DEFAULT '' COMMENT '机构简称',
  `org_full_name` varchar(60) DEFAULT '' COMMENT '机构全称',
  `org_code` varchar(10) DEFAULT '' COMMENT '机构代码',
  `org_address1` varchar(100) DEFAULT '' COMMENT '联系地址1',
  `org_address2` varchar(100) DEFAULT '' COMMENT '联系地址2',
  `org_tel1` varchar(25) DEFAULT '' COMMENT '联系电话1',
  `org_tel2` varchar(25) DEFAULT '' COMMENT '联系电话2',
  `org_begin_date` date DEFAULT NULL COMMENT '成立日期',
  `org_type` varchar(20) DEFAULT '' COMMENT '机构类型',
  `org_fax` varchar(20) DEFAULT '' COMMENT '传真',
  `org_postal` varchar(6) DEFAULT '' COMMENT '机构邮编',
  `org_legal` varchar(20) DEFAULT '' COMMENT '法人代表',
  `org_tax_no` varchar(25) DEFAULT '' COMMENT '税务号',
  `org_reg_no` varchar(25) DEFAULT '' COMMENT '注册登记号',
  `org_belong_dist` bigint(20) DEFAULT NULL COMMENT '所属地区',
  `org_parent` bigint(20) DEFAULT NULL COMMENT '上一级ID',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`org_id`),
  KEY `org_simple_name_index` (`org_simple_name`) USING BTREE,
  KEY `fk_uum_orga_reference_uum_orga` (`org_parent`),
  CONSTRAINT `fk_uum_orga_reference_uum_orga` FOREIGN KEY (`org_parent`) REFERENCES `uum_organ` (`org_id`)
) COMMENT='机构表';

-- ----------------------------
-- Records of uum_organ
-- ----------------------------
INSERT INTO `uum_organ` VALUES ('0', '总部', null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', 'enable', null, null, null);
INSERT INTO `uum_organ` VALUES ('1', '董事会', '董事会', 'DSH', '', '', '', '', '2011-12-21', 'branch', null, '', '', '', '', '0', '0', 'enable', '', 'admin', '2011-12-21 09:33:50');
INSERT INTO `uum_organ` VALUES ('2', '技术部', null, '10010', null, null, null, null, null, null, null, null, null, null, null, null, '0', 'disable', '测试', 'admin', '2012-01-10 14:11:58');
INSERT INTO `uum_organ` VALUES ('3', '山西运营', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '7', '0', 'enable', '', 'admin', '2012-02-09 17:44:23');
INSERT INTO `uum_organ` VALUES ('4', '太原', '', '', '', '', '', '', null, 'branch', null, '', '', '', '', '0', '3', 'enable', '', 'admin', '2012-02-09 17:48:18');
INSERT INTO `uum_organ` VALUES ('5', '海淀', '', '', '', '', '', '', null, 'HQ', null, '', '', '', '', '2000', '2', 'enable', '', 'admin', '2012-02-28 14:58:56');
INSERT INTO `uum_organ` VALUES ('6', '五道口店', '', '', '', '', '', '', null, 'store', null, '', '', '', '', '2002', '5', 'enable', '', 'admin', '2012-02-28 15:40:17');
INSERT INTO `uum_organ` VALUES ('7', '运营部', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:43:37');
INSERT INTO `uum_organ` VALUES ('8', '人力资源', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:44:23');
INSERT INTO `uum_organ` VALUES ('9', '财务部', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:44:45');
INSERT INTO `uum_organ` VALUES ('10', '采购部', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:45:25');
INSERT INTO `uum_organ` VALUES ('11', '总经办', '北京总经办', 'ZJB', '总经办总经办总经办', '', '57568678768777', '776845745654646', '2012-03-02', 'department', null, '454656', '总经办', '66666666', '666666', '2000', '1', 'enable', '', 'admin', '2012-03-06 14:46:04');
INSERT INTO `uum_organ` VALUES ('12', '储运部', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:48:38');
INSERT INTO `uum_organ` VALUES ('13', '开发部', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '11', 'enable', '', 'admin', '2012-03-06 14:49:02');
INSERT INTO `uum_organ` VALUES ('14', '北京采购', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2000', '10', 'enable', '', 'admin', '2012-03-06 14:52:44');
INSERT INTO `uum_organ` VALUES ('15', '上海采购', '', '', '', '', '', '', null, 'department', null, '', '', '', '', '2003', '10', 'enable', '', 'admin', '2012-03-06 14:53:19');
INSERT INTO `uum_organ` VALUES ('16', '北京储运', '', '', '', '', '', '', null, 'branch', null, '', '', '', '', '2000', '12', 'enable', '', 'admin', '2012-03-06 14:54:15');
INSERT INTO `uum_organ` VALUES ('17', '上海储运', '', '', '', '', '', '', '2013-10-08', 'branch', null, '', '', '', '', '2003', '12', 'enable', '', 'admin', '2012-03-06 14:54:37');
INSERT INTO `uum_organ` VALUES ('18', '技术部', null, '10010', null, null, null, null, null, null, null, null, null, null, null, null, '2', 'disable', '测试', null, '2017-06-23 21:54:34');
INSERT INTO `uum_organ` VALUES ('19', '技术部', null, '10010', null, null, null, null, null, null, null, null, null, null, null, null, '2', 'disable', '测试', null, '2017-06-23 21:58:00');
INSERT INTO `uum_organ` VALUES ('20', '技术部', null, '10010', null, null, null, null, null, null, null, null, null, null, null, null, '2', 'disable', '测试', null, '2017-06-23 22:00:38');

-- ----------------------------
-- Table structure for uum_role
-- ----------------------------
DROP TABLE IF EXISTS `uum_role`;
CREATE TABLE `uum_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(10) DEFAULT '' COMMENT '角色名称',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `role_level` tinyint(2) DEFAULT NULL COMMENT '角色级别',
  PRIMARY KEY (`role_id`)
) COMMENT='角色表';

-- ----------------------------
-- Table structure for uum_org_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `uum_org_role_ref`;
CREATE TABLE `uum_org_role_ref` (
  `org_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '机构角色ID',
  `org_id` bigint(20) NOT NULL COMMENT '机构ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`org_role_id`),
  KEY `fk_uum_org__reference_uum_org_role` (`org_id`),
  KEY `fk_uum_org__reference_uum_role` (`role_id`),
  CONSTRAINT `fk_uum_org__reference_uum_org_role` FOREIGN KEY (`org_id`) REFERENCES `uum_organ` (`org_id`),
  CONSTRAINT `fk_uum_org__reference_uum_role` FOREIGN KEY (`role_id`) REFERENCES `uum_role` (`role_id`)
) COMMENT='机构角色关系表';

-- ----------------------------
-- Records of uum_org_role_ref
-- ----------------------------
INSERT INTO `uum_org_role_ref` VALUES ('1', '0', '1');
INSERT INTO `uum_org_role_ref` VALUES ('40', '4', '10');
INSERT INTO `uum_org_role_ref` VALUES ('45', '11', '3');
INSERT INTO `uum_org_role_ref` VALUES ('46', '11', '4');
INSERT INTO `uum_org_role_ref` VALUES ('47', '7', '7');
INSERT INTO `uum_org_role_ref` VALUES ('51', '2', '7');
INSERT INTO `uum_org_role_ref` VALUES ('53', '6', '17');
INSERT INTO `uum_org_role_ref` VALUES ('54', '6', '18');
INSERT INTO `uum_org_role_ref` VALUES ('55', '6', '19');
INSERT INTO `uum_org_role_ref` VALUES ('56', '1', '20');
INSERT INTO `uum_org_role_ref` VALUES ('67', '2', '13');
INSERT INTO `uum_org_role_ref` VALUES ('86', '0', '0');
INSERT INTO `uum_org_role_ref` VALUES ('97', '18', '0');
INSERT INTO `uum_org_role_ref` VALUES ('98', '18', '1');
INSERT INTO `uum_org_role_ref` VALUES ('99', '7', '0');

-- ----------------------------
-- Records of uum_role
-- ----------------------------
INSERT INTO `uum_role` VALUES ('0', '无角色用户', 'enable', '', 'admin', null, null);
INSERT INTO `uum_role` VALUES ('1', '管理员', 'enable', '', 'admin', '2011-02-11 16:57:01', '1');
INSERT INTO `uum_role` VALUES ('3', '总经理', 'enable', '', 'admin', '2011-12-20 11:37:31', '1');
INSERT INTO `uum_role` VALUES ('4', '副总经理', 'enable', '', 'admin', '2011-12-23 09:37:13', '2');
INSERT INTO `uum_role` VALUES ('5', '人事经理', 'enable', '', 'admin', '2011-12-23 16:33:19', '3');
INSERT INTO `uum_role` VALUES ('6', '储运部经理', 'enable', '', 'admin', '2011-12-23 17:21:56', '3');
INSERT INTO `uum_role` VALUES ('7', '运营经理', 'enable', '', 'admin', '2011-12-23 17:22:24', '3');
INSERT INTO `uum_role` VALUES ('8', '财务经理', 'enable', '', 'admin', '2011-12-26 09:54:22', '3');
INSERT INTO `uum_role` VALUES ('9', '培训经理', 'enable', '培训经理', 'admin', '2011-12-26 09:54:33', '3');
INSERT INTO `uum_role` VALUES ('10', '采购经理', 'enable', '工', 'admin', '2012-01-10 14:23:10', '3');
INSERT INTO `uum_role` VALUES ('11', '人事主管', 'enable', '', 'admin', '2012-03-06 11:50:09', '4');
INSERT INTO `uum_role` VALUES ('12', '储运主管', 'enable', '', 'admin', '2012-03-06 11:50:31', '4');
INSERT INTO `uum_role` VALUES ('13', '运营主管', 'enable', '', 'admin', '2012-03-06 14:06:28', '4');
INSERT INTO `uum_role` VALUES ('14', '财务主管', 'enable', '', 'admin', '2012-03-06 14:06:51', '4');
INSERT INTO `uum_role` VALUES ('15', '培训主管', 'enable', '', 'admin', '2012-03-06 14:07:17', '4');
INSERT INTO `uum_role` VALUES ('16', '采购主管', 'enable', '', 'admin', '2012-03-06 14:07:33', '4');
INSERT INTO `uum_role` VALUES ('17', '店长', 'enable', '', 'admin', '2012-03-06 14:58:26', '5');
INSERT INTO `uum_role` VALUES ('18', '收银', 'enable', '', 'admin', '2012-03-06 14:58:44', '6');
INSERT INTO `uum_role` VALUES ('19', '店员', 'enable', '', 'admin', '2012-03-06 14:59:00', '7');
INSERT INTO `uum_role` VALUES ('20', '董事长', 'enable', '测试', 'admin', '2012-03-06 15:06:36', '1');
INSERT INTO `uum_role` VALUES ('22', '人事主管3', 'disable', '', 'admin', '2012-03-06 11:50:09', '4');
INSERT INTO `uum_role` VALUES ('23', '人事主管2', 'enable', '', 'admin', '2012-03-06 11:50:09', '4');
INSERT INTO `uum_role` VALUES ('24', '人事主管', 'disable', '', 'admin', '2012-03-06 11:50:09', '4');
INSERT INTO `uum_role` VALUES ('25', '测试', 'enable', '对对对', 'admin', '2018-12-13 20:06:57', '1');

-- ----------------------------
-- Table structure for uum_user
-- ----------------------------
DROP TABLE IF EXISTS `uum_user`;
CREATE TABLE `uum_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_code` varchar(20) DEFAULT '' COMMENT '用户号',
  `user_name` varchar(20) DEFAULT '' COMMENT '用户名',
  `user_password` varchar(20) DEFAULT '' COMMENT '用户密码',
  `user_gender` varchar(2) DEFAULT '' COMMENT '用户性别',
  `user_position` varchar(30) DEFAULT '' COMMENT '用户职位',
  `user_photo_url` varchar(200) DEFAULT '' COMMENT '用户照片',
  `user_qq` varchar(20) DEFAULT '' COMMENT '用户QQ',
  `user_wechat` varchar(20) DEFAULT '' COMMENT '用户微信号',
  `user_mobile` varchar(20) DEFAULT '' COMMENT '用户手机',
  `user_office_tel` varchar(20) DEFAULT '' COMMENT '办公电话',
  `user_family_tel` varchar(20) DEFAULT '' COMMENT '家庭电话',
  `user_email` varchar(30) DEFAULT '' COMMENT '用户邮件',
  `user_avidate` date DEFAULT NULL COMMENT '用户有效期',
  `user_belongto_org` bigint(20) DEFAULT '0' COMMENT '用户所属机构',
  `user_address` varchar(100) DEFAULT '' COMMENT '家庭住址',
  `user_name_py` varchar(20) DEFAULT '' COMMENT '用户名拼音',
  `status` varchar(15) NOT NULL COMMENT '机构状态',
  `memo` varchar(100) DEFAULT '' COMMENT '备注',
  `creator` varchar(15) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  KEY `fk_uum_user_reference_uum_orga` (`user_belongto_org`),
  CONSTRAINT `fk_uum_user_reference_uum_orga` FOREIGN KEY (`user_belongto_org`) REFERENCES `uum_organ` (`org_id`)
) COMMENT='用户表';

-- ----------------------------
-- Records of uum_user
-- ----------------------------
INSERT INTO `uum_user` VALUES ('0', 'admin', '管理员', 'UlFQV1ZV', null, null, 'http://localhost:8080/mycuckoo/upload/photo/20171014181342_5pnIdj.jpg', null, null, null, null, null, null, null, '0', null, 'gly', 'enable', null, 'admin', '2011-02-11 16:57:01');
INSERT INTO `uum_user` VALUES ('12', 'wangj', '王娟', 'UlFQV1ZV', '1', '', '', '', '', '134444444444444450', '', '', '', '2012-06-21', '1', '', 'wj', 'enable', '', 'admin', '2011-12-21 10:09:20');
INSERT INTO `uum_user` VALUES ('13', 'shijh', '石纪红', 'UlFQV1ZV', '1', '', '', '', '', '', '', '', '', '2012-07-10', '11', '', 'sjh', 'enable', '', 'admin', '2012-01-10 14:55:24');
INSERT INTO `uum_user` VALUES ('14', 'yangwj', '杨文菊', 'UlFQV1ZV', '1', '', '', '', '', '', '', '', '', '2012-08-28', '11', '', 'ywj', 'enable', '', 'admin', '2012-02-28 15:23:30');
INSERT INTO `uum_user` VALUES ('15', 'yuzp', '于志平', 'UlFQV1ZV', '0', '', '', '', '', '', '', '', '', '2012-09-06', '7', '', 'yzp', 'enable', '', 'admin', '2012-03-06 15:15:09');
INSERT INTO `uum_user` VALUES ('16', 'zhangsp', '张世鹏', 'UlFQV1ZV', '0', '', '', '', '', '', '', '', '', '2012-09-06', '2', '', 'zsp', 'enable', '', 'admin', '2012-03-06 15:15:39');
INSERT INTO `uum_user` VALUES ('17', 'zhangl', '张丽', 'UlFQV1ZV', '1', '', '', '', '', '', '', '', '', '2012-09-06', '6', '', 'zl', 'enable', '', 'admin', '2012-03-06 15:16:22');
INSERT INTO `uum_user` VALUES ('18', 'gaoxf', '高晓峰', 'UlFQV1ZV', '0', '', '', '', '', '', '', '', '', '2012-09-06', '6', '', 'gxf', 'enable', '', 'admin', '2012-03-06 15:21:33');
INSERT INTO `uum_user` VALUES ('19', 'wangx', '王旭', 'UlFQV1ZV', '0', '', '', '', '', '', '', '', '', '2012-09-06', '6', '', 'wx', 'enable', '', 'admin', '2012-03-06 15:21:57');
INSERT INTO `uum_user` VALUES ('20', 'wangxm', '王帅明', 'UlFQV1ZV', '0', '', '', '', '', '', '', '', '', '2012-09-06', '6', '', 'wsm', 'enable', '', 'admin', '2012-03-06 15:23:09');
INSERT INTO `uum_user` VALUES ('21', 'zhangj', '张军', 'UlFQV1ZV', '0', '', 'http://localhost:8080/mycuckoo/upload/photo/haha_UPwWAD.jpg', '', '', '', '', '', '', '2012-09-06', '7', '', 'zj', 'enable', '', 'admin', '2012-03-06 15:23:40');

-- ----------------------------
-- Table structure for uum_user_org_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `uum_user_org_role_ref`;
CREATE TABLE `uum_user_org_role_ref` (
  `user_org_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `org_role_id` bigint(20) NOT NULL COMMENT '机构角色ID',
  `is_default` varchar(2) NOT NULL COMMENT '是否默认',
  PRIMARY KEY (`user_org_role_id`),
  KEY `fk_uum_role_reference_uum_org_role` (`org_role_id`),
  KEY `fk_uum_role_reference_uum_user` (`user_id`),
  CONSTRAINT `fk_uum_role_reference_uum_org_role` FOREIGN KEY (`org_role_id`) REFERENCES `uum_org_role_ref` (`org_role_id`),
  CONSTRAINT `fk_uum_role_reference_uum_user` FOREIGN KEY (`user_id`) REFERENCES `uum_user` (`user_id`)
) COMMENT='角色用户关系表';

-- ----------------------------
-- Records of uum_user_org_role_ref
-- ----------------------------
INSERT INTO `uum_user_org_role_ref` VALUES ('23', '0', '1', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('57', '12', '56', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('58', '13', '45', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('59', '14', '46', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('61', '16', '51', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('62', '17', '53', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('63', '18', '54', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('65', '20', '55', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('68', '19', '55', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('69', '19', '54', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('70', '15', '47', 'Y');
INSERT INTO `uum_user_org_role_ref` VALUES ('71', '15', '53', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('72', '15', '54', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('73', '15', '55', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('74', '15', '40', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('75', '15', '45', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('76', '15', '46', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('77', '15', '56', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('78', '15', '1', 'N');
INSERT INTO `uum_user_org_role_ref` VALUES ('83', '21', '47', 'Y');

-- ----------------------------
-- Table structure for uum_privilege
-- ----------------------------
DROP TABLE IF EXISTS `uum_privilege`;
CREATE TABLE `uum_privilege` (
  `privilege_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_id` varchar(20) NOT NULL COMMENT '资源ID',
  `owner_id` bigint(20) NOT NULL COMMENT '拥有者ID',
  `owner_type` varchar(10) DEFAULT NULL COMMENT '拥有者类型',
  `privilege_scope` varchar(10) DEFAULT NULL COMMENT '\r\n	针对操作: inc=包含 exc=不包含 all=全部; \r\n	针对行: org=机构 rol=角色 usr=用户',
  `privilege_type` varchar(10) DEFAULT NULL COMMENT 'row=行 opt=操作',
  PRIMARY KEY (`privilege_id`),
  KEY `owner_type_index` (`owner_type`) USING BTREE,
  KEY `privilege_type_index` (`privilege_type`)
) COMMENT='权限表';

-- ----------------------------
-- Records of uum_privilege
-- ----------------------------
INSERT INTO `uum_privilege` VALUES ('901', '60', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('902', '61', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('903', '62', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('904', '91', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('905', '97', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('906', '152', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('907', '45', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('908', '46', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('909', '47', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('910', '48', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('911', '49', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('912', '50', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('913', '51', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('914', '52', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('915', '53', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('916', '54', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('917', '55', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('918', '56', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('919', '57', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('936', '5', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('937', '6', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('938', '7', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('939', '87', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('940', '155', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('941', '10', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('942', '11', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('943', '12', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('944', '13', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('945', '14', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('946', '15', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('947', '16', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('948', '17', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('949', '18', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('950', '19', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('951', '25', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('952', '26', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('953', '27', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('954', '28', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('955', '29', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('956', '36', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('957', '37', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('958', '38', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('959', '39', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('960', '40', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('961', '98', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('962', '99', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('963', '100', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('964', '101', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('966', '104', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('967', '105', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('968', '106', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('969', '107', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('970', '108', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('971', '109', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('972', '110', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('973', '111', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('974', '44', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('975', '30', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('976', '31', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('977', '33', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('978', '34', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('979', '35', '3', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('980', '5', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('981', '6', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('982', '7', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('983', '87', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('984', '155', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('985', '10', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('986', '11', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('987', '12', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('988', '13', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('989', '14', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('994', '15', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('995', '16', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('996', '17', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('997', '18', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('998', '19', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('999', '25', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1000', '26', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1001', '27', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1002', '28', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1003', '29', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1004', '36', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1005', '37', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1006', '38', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1007', '39', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1008', '40', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1009', '98', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1010', '99', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1011', '100', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1012', '101', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1014', '104', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1015', '105', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1016', '106', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1017', '107', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1018', '108', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1019', '109', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1020', '110', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1021', '111', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1022', '44', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1023', '30', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1024', '31', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1025', '33', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1026', '34', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1027', '35', '4', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1032', '5', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1033', '6', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1034', '7', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1035', '87', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1036', '155', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1037', '10', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1038', '11', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1039', '12', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1040', '13', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1041', '14', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1042', '15', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1043', '16', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1044', '17', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1045', '18', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1046', '19', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1047', '25', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1048', '26', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1049', '27', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1050', '28', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1051', '29', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1052', '36', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1053', '37', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1054', '38', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1055', '39', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1056', '40', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1057', '98', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1058', '99', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1059', '100', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1060', '101', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1062', '104', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1063', '105', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1064', '106', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1065', '107', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1066', '108', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1067', '109', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1068', '110', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1069', '111', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1070', '44', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1071', '30', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1072', '31', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1073', '33', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1074', '34', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1075', '35', '5', 'rol', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1076', 'all', '7', 'rol', 'all', 'opt');
INSERT INTO `uum_privilege` VALUES ('1081', '5', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1082', '6', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1083', '7', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1084', '87', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1085', '155', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1086', '10', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1087', '11', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1088', '12', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1089', '13', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1090', '14', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1091', '15', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1092', '16', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1093', '17', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1094', '18', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1095', '19', '13', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1096', '5', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1097', '6', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1098', '7', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1099', '87', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1100', '155', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1101', '10', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1102', '11', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1103', '12', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1104', '13', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1105', '14', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1106', '60', '17', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1107', '5', '18', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1108', '6', '18', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1109', '7', '18', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1110', '87', '18', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1111', '155', '18', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1117', '44', '19', 'usr', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1118', 'rol', '7', 'rol', 'rol', 'row');
INSERT INTO `uum_privilege` VALUES ('1119', '6', '15', 'usr', 'org', 'row');
INSERT INTO `uum_privilege` VALUES ('1120', '14', '15', 'usr', 'org', 'row');
INSERT INTO `uum_privilege` VALUES ('1123', '60', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1124', '61', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1125', '62', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1126', '91', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1127', '97', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1128', '152', '15', 'usr', 'exc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1152', 'org', '19', 'rol', 'org', 'row');
INSERT INTO `uum_privilege` VALUES ('1153', 'org', '19', 'rol', 'org', 'row');
INSERT INTO `uum_privilege` VALUES ('1192', 'org', '20', 'rol', 'org', 'row');
INSERT INTO `uum_privilege` VALUES ('1318', '7', '25', 'rol', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1319', '21', '21', 'usr', 'usr', 'row');
INSERT INTO `uum_privilege` VALUES ('1320', '7', '21', 'usr', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1321', '6', '21', 'usr', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1322', '155', '21', 'usr', 'inc', 'opt');
INSERT INTO `uum_privilege` VALUES ('1323', '87', '21', 'usr', 'inc', 'opt');
