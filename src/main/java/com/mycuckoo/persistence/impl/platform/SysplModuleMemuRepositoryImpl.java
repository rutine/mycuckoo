package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.persistence.iface.platform.SysplModOptRefRepository;
import com.mycuckoo.persistence.iface.platform.SysplModuleMemuRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 模块持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:47:23 PM
 * @version 2.0.0
 */
@Repository
public class SysplModuleMemuRepositoryImpl extends AbstractRepository<SysplModuleMemu, Long> 
		implements SysplModuleMemuRepository {

	@Autowired
	private SysplModOptRefRepository sysplModOptRefRepository;

	@Override
	public int countModulesByUpModId(long moduleId) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countModulesByUpModId", moduleId);
	}

	@Override
	public int countModulesByModuleName(String moduleName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countModulesByModuleName", moduleName);
	}

	@Override
	public void deleteModOptRefByModuleId(long moduleId) {
		sysplModOptRefRepository.deleteModOptRefByModuleId(moduleId);
	}

	@Override
	public void deleteModOptRefByOperateId(long operateId) {
		sysplModOptRefRepository.deleteModOptRefByOperateId(operateId);
	}

	@Override
	public void deleteModOptRefs(List<SysplModOptRef> modOptRefs) {
		sysplModOptRefRepository.delete(modOptRefs);
	}

	@Override
	public List<SysplModuleMemu> findAllModules() {
		return (List<SysplModuleMemu>) this.findAll();
	}

	@Override
	public List<SysplModOptRef> findAllModOptRefs() {
		List<SysplModOptRef> modOptRefList = (List<SysplModOptRef>) sysplModOptRefRepository.findAll();

		if (modOptRefList != null) {
			for (SysplModOptRef modOptRef : modOptRefList) {
				long moduleId3 = modOptRef.getSysplModuleMemu().getModuleId();
				SysplModuleMemu mooduleMemu3 = get(moduleId3); // 第三级菜单
				modOptRef.setSysplModuleMemu(mooduleMemu3);

				long moduleId2 = mooduleMemu3.getSysplModuleMemu().getModuleId(); // 第二级菜单
				SysplModuleMemu moduleMemu2 = get(moduleId2);
				mooduleMemu3.setSysplModuleMemu(moduleMemu2);

				long moduleId1 = moduleMemu2.getSysplModuleMemu().getModuleId();
				SysplModuleMemu moduleMemu1 = get(moduleId1); // 第一级菜单
				moduleMemu2.setSysplModuleMemu(moduleMemu1);
			}
		}

		return modOptRefList;
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByModuleId(long moduleId) {
		return (List<SysplModOptRef>) sysplModOptRefRepository.findModOptRefsByModuleId(moduleId);
	}

	@Override
	public List<SysplModOptRef> findAssignedModOptRefsByModuleId(long moduleId) {
		return (List<SysplModOptRef>) sysplModOptRefRepository.findModOptRefsByModuleId(moduleId);
	}
	
	@Override
	public List<SysplModOptRef> findModOptRefsByOperateId(Long operateId) {
		return sysplModOptRefRepository.findModOptRefsByOperateId(operateId);
	}

	@Override
	public List<SysplModuleMemu> findModulesByUpModId(long moduleId, long filterModuleId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("moduleId", moduleId);
		List<Long> list = new ArrayList<Long>(2);
		list.add(0l);
		list.add(filterModuleId);
		map.put("list", list);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findModulesByUpModId", map);
	}

	@Override
	public Page<SysplModuleMemu> findModulesByCon(Long[] ids,
			String modName, String modEnId, Pageable page) {
		
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("array", ArrayUtils.isNotEmpty(ids) ? ids : null);
		map.put("modName", isNotBlank(modName) ? "%" + modName + "%" : null);
		map.put("modEnId", isNotBlank(modEnId) ? "%" + modEnId + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplModuleMemu> list = getSqlSession()
				.selectList(obtainMapperNamespace() + ".findModulesByCon", map);
		int count = getSqlSession()
				.selectOne(obtainMapperNamespace() + ".countModulesByCon", map);

		return new PageImpl<SysplModuleMemu>(list, page, count);
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds) {
		List<SysplModOptRef> modOptRefList = sysplModOptRefRepository.findModOptRefsByIds(modOptRefIds);

		if (modOptRefList != null) {
			for (SysplModOptRef sysplModOptRef : modOptRefList) {
				long moduleId3 = sysplModOptRef.getSysplModuleMemu().getModuleId();
				SysplModuleMemu sysplModuleMemu3 = get(moduleId3); // 第三级菜单
				sysplModOptRef.setSysplModuleMemu(sysplModuleMemu3);

				long moduleId2 = sysplModuleMemu3.getSysplModuleMemu().getModuleId(); // 第二级菜单
				SysplModuleMemu sysplModuleMemu2 = get(moduleId2);
				sysplModuleMemu3.setSysplModuleMemu(sysplModuleMemu2);

				long moduleId1 = sysplModuleMemu2.getSysplModuleMemu().getModuleId();
				SysplModuleMemu sysplModuleMemu1 = get(moduleId1); // 第一级菜单
				sysplModuleMemu2.setSysplModuleMemu(sysplModuleMemu1);
			}
		}
		
		return modOptRefList;
	}

	/**
	 * <b>注意：</b> 暂未实现...... 功能说明 : 修改行权限
	 * 
	 * @param moduleIds 模块集合
	 * @param rowPrivilegeLevel 权限类型
	 */
	public void updateModuleRowPrivilege(Long[] moduleIds, String rowPrivilegeLevel) {
		throw new UnsupportedOperationException("暂不支持...");
//		 // 因为类SysplModuleMemu 没有属性 privilegeLevel
//		 Map<String, Object> map = new HashMap<String, Object>(2);
//		 map.put("array", moduleIds);
//		 map.put("rowPrivilegeLevel", rowPrivilegeLevel);
//		
//		 getSqlSession().update(obtainMapperNamespace() +  ".updateModuleRowPrivilege", map);
	}

	@Override
	public void saveModOptRef(List<SysplModOptRef> modOptRefs) {
		sysplModOptRefRepository.save(modOptRefs);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object
	 * )
	 */
	@Override
	protected Long obtainID(SysplModuleMemu entity) {
		return entity.getModuleId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplModuleMemuMapper";
	}

}
