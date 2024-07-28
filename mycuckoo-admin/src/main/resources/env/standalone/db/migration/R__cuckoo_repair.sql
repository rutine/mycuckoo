SET SCHEMA CUCKOO;

update `sys_district` set district_id = 0 where name = '地区树';
update `sys_module_menu` set module_id = 0 where name = '模块树';
update `uum_role` set role_id = 0 where name = '无角色用户';
update `uum_user` set user_id = 0 where name = '管理员';