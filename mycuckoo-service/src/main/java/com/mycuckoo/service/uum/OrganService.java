package com.mycuckoo.service.uum;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.ORGAN_MGR;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.ModuleLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.OrganMapper;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.TreeVoExtend;

/**
 * 功能说明: 机构业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:18:21 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class OrganService {
	
	static Logger logger = LoggerFactory.getLogger(OrganService.class);
	
	@Autowired
	private OrganMapper organMapper;
	@Autowired
	private RoleOrganService roleOrganService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private PlatformServiceFacade platformServiceFacade;
	@Autowired
	private SystemOptLogService sysOptLogService;

	
	@Transactional(readOnly=false)
	public int disEnable(long organId, String disEnableFlag) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			/**
			 * 1、机构有下级 
			 * 2、机构下有角色 
			 * 3、机构下有用户 暂不用实现 删除机构行权限 
			 * 4、被其它系统引用 
			 * 0、停用启用成功
			 */
			int childCount = organMapper.countByUpOrgId(organId);
			if( childCount > 0) return 1;
			int roleCount = roleOrganService.countByOrgId(organId);
			if(roleCount > 0) return 2;
			
			Organ organ = organMapper.get(organId);
			organ.setStatus(DISABLE);
			organMapper.update(organ);
			privilegeService.deleteRowPriByResourceId(organId + ""); // 删除机构行权限
			
			this.writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
			return 0;
		} else {
			Organ organ = get(organId);
			organ.setStatus(ENABLE);
			organMapper.update(organ);
			writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
			
			return 0;
		}
	}

	public boolean existByOrganName(String organName) {
		int count = organMapper.countByOrgSimpleName(organName);
		
		return count > 0;
	}

	public List<Organ> findAll() {
		return organMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
	}

	public List<Organ> findChildNodes(long organId, int flag) {
		List<Organ> filterList = new ArrayList<Organ>();
		List<Organ> listAll = findAll();
		List<Organ> listAllTemp = new ArrayList<Organ>();
		listAllTemp.addAll(listAll);
		listAllTemp.remove(new Organ(0L, null)); //删除根元素
		//过滤出所有下级元素
		filterList = getFilterList(filterList, listAllTemp, organId);
		if (organId != 0) {
			// 加入本元素
			for (Organ organ : listAll) {
				if (organ.getOrgId() == organId) {
					filterList.add(organ);
				}
			}
		}
		if (flag == 1) {
			listAll.removeAll(filterList);
			filterList = listAll;
			if (filterList != null && filterList.size() > 0) {
				for (Organ organ : filterList) {
					organ.setUpOrgId(organ.getOrgan().getOrgId());
				}
			}
		}
		
		return filterList;
	}

	public List<TreeVo> findNextLevelChildNodes(long organId, long filterOrgId) {
		List<Organ> list = organMapper.findByUpOrgIdAFilter(organId, filterOrgId);
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if(list != null) {
			for(Organ organ : list) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(organ.getOrgId().toString());
				treeVo.setText(organ.getOrgSimpleName());
				if(ModuleLevelEnum.TWO.value().equals(organ.getOrgType())) {
					treeVo.setLeaf(true);
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	public List<TreeVoExtend> findNextLevelChildNodesWithCheckbox(long organId, long filterOrgId) {
		List<Organ> list = organMapper.findByUpOrgIdAFilter(organId, filterOrgId);
		List<TreeVoExtend> treeVoList = new ArrayList<TreeVoExtend>();
		if (list != null && list.size() > 0) {
			for (Organ organ : list) {
				TreeVoExtend treeVo = new TreeVoExtend();
				treeVo.setId(organ.getOrgId().toString());
				treeVo.setText(organ.getOrgSimpleName());
				if (ModuleLevelEnum.TWO.value().equals(organ.getOrgType())) {
					treeVo.setLeaf(true);
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	public Page<Organ> findByPage(long treeId, String orgCode, String orgName, Pageable page) {
		logger.debug("start={} limit={} treeId={} orgName={} orgCode={}", 
				page.getOffset(), page.getPageSize(), treeId, orgName, orgCode);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId >= 0) {
			List<Organ> list = this.findChildNodes(treeId, 0);
			for(Organ organ : list) {
				idList.add(organ.getOrgId());
			}
			if(idList.isEmpty()) return new PageImpl<Organ>(new ArrayList<Organ>(), page, 0);
		}
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgIds", idList);
		params.put("orgCode", isNullOrEmpty(orgCode) ? null : "%" + orgCode + "%");
		params.put("orgName", isNullOrEmpty(orgName) ? null : "%" + orgName + "%");
		
		return organMapper.findByPage(params, page);
	}

	public Organ get(long organId) {
		logger.debug("will find organ id is {}", organId);
		
		Organ organ = (Organ)organMapper.get(organId);
		organ.setUpOrgId(organ.getOrgan().getOrgId());
		organ.setUpOrgName(organ.getOrgan().getOrgSimpleName());
		
		Organ parentOrgan = organMapper.get(organ.getOrgan().getOrgId());
		organ.setUpOrgId(parentOrgan.getOrgId());
		organ.setUpOrgName(parentOrgan.getOrgSimpleName());
		
		District district = platformServiceFacade.getDistrict(organ.getOrgBelongDist());
		if(district != null) {
			organ.setOrgBelongDistName(district.getDistrictName());
		}
		
		return organ;
	}

	public List<Organ> findByOrgIds(Long[] orgIds) {
		if(orgIds == null || orgIds.length == 0) return null;
		
		return organMapper.findByOrgIds(orgIds);
	}

	@Transactional(readOnly=false)
	public void update(Organ organ) throws ApplicationException {
		organMapper.update(organ);
		
		writeLog(organ, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void save(Organ organ) throws ApplicationException {
		organMapper.save(organ);
		
		writeLog(organ, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}

   
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param organ 机构对象
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 7:39:34 PM
	 */
	private void writeLog(Organ organ,LogLevelEnum logLevel,OptNameEnum opt) throws ApplicationException {
		
		StringBuilder optContent = new StringBuilder();
		optContent.append("机构名称 : ").append(organ.getOrgSimpleName()).append(SPLIT);
		optContent.append("机构代码 : ").append(organ.getOrgCode()).append(SPLIT);
		optContent.append("上级机构 : ").append(organ.getUpOrgName()).append(SPLIT);
		
		
		sysOptLogService.saveLog(logLevel, opt, ORGAN_MGR, optContent.toString(), organ.getOrgId() + "");
	}

	/**
	 * 根据机构ID过滤出下级，下下级机构
	 * 
	 * @param filterList 存放下级，下下级机构的集合
	 * @param listAll 所有机构
	 * @param organId 上级机构ID
	 * @return 下级，下下级机构
	 * @author rutine
	 * @time Oct 17, 2012 7:49:15 PM
	 */
	private List<Organ> getFilterList(List<Organ> filterList, List<Organ> listAll, long organId) {
		List<Organ> subList = getSubList(listAll, organId);
		if (!subList.isEmpty()) {
			filterList.addAll(subList);
		}
		for (Organ organ : subList) {
			getFilterList(filterList, listAll, organ.getOrgId());
		}
		
		return filterList;
	}
	/**
	 * 根据机构ID过滤出下级机构
	 *
	 * @param listAll  所有机构
	 * @param organId 上级机构ID
	 * @return 所有子结点
	 * @author rutine
	 * @time Oct 17, 2012 7:46:36 PM
	 */
	private List<Organ> getSubList(List<Organ> listAll, long organId) {
		List<Organ> newList = new ArrayList<Organ>();
		for(Organ organ : listAll) {
			if(organ.getOrgan().getOrgId() == organId) {
				newList.add(organ);
			}
		}
		
		return newList;
	}

}
