package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 操作按钮持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:39:10 AM
 * @version 2.0.0
 */
public interface SysplOperateRepository extends Repository<SysplOperate, Long> {

	/**
	 * 根据名称统计操作按钮数量
	 * 
	 * @param operateName 操作按钮名称
	 * @return
	 */
	int countOperatesByName(String operateName);

	/**
	 * 返回所有操作按钮记录
	 * 
	 * @return
	 */
	List<SysplOperate> findAllOperates();

	/**
	 * <p>根据条件查询操作按钮</p>
	 * 
	 * @param operateName 操作按钮名称
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplOperate> findOperatesByCon(String operateName, Pageable page);
}
