update `syspl_district` set district_id = 0 where district_name = '地区树';
update `syspl_module_menu` set module_id = 0 where mod_name = '模块树';
update `uum_organ` set org_id = 0 where org_simple_name = '总部';
update `uum_role` set role_id = 0 where role_name = '无角色用户';