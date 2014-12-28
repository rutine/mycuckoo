package com.mycuckoo.service.impl.uum;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.uum.UumOrganRepository;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.uum.OrganService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleOrganService;

/**
 * 功能说明: 机构业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:18:21 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class OrganServiceImpl implements OrganService {
	
	static Logger logger = LoggerFactory.getLogger(OrganServiceImpl.class);
	
	private UumOrganRepository organRepository;
	private RoleOrganService roleOrganService;
	private PrivilegeService privilegeService;
	private PlatformCommonService platformCommonService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public int disEnableOrgan(long organId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			/**
			 * 1、机构有下级 
			 * 2、机构下有角色 
			 * 3、机构下有用户 暂不用实现 删除机构行权限 
			 * 4、被其它系统引用 
			 * 0、停用启用成功
			 */
			int childCount = organRepository.countOrgansByUpOrgId(organId);
			if( childCount > 0) return 1;
			int roleCount = roleOrganService.countOrgRoleRefsByOrgId(organId);
			if(roleCount > 0) return 2;
			UumOrgan uumOrgan = organRepository.get(organId);
			uumOrgan.setStatus(DISABLE);
			updateOrgan(uumOrgan, request);
			privilegeService.deleteRowPriByResourceId(organId + "", request); // 删除机构行权限
			this.writeLog(uumOrgan, LOG_LEVEL_SECOND, DISABLE_DIS, request);
			return 0;
		} else {
			UumOrgan uumOrgan = getOrganByOrgId(organId);
			uumOrgan.setStatus(ENABLE);
			updateOrgan(uumOrgan, request);
			writeLog(uumOrgan, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return 0;
		}
	}

	@Override
	public boolean existOrganByOrganName(String organName) {
		int count = organRepository.countOrgansByOrgSimpleName(organName);
		if(count > 0) return true;
		
		return false;
	}

	@Override
	public List<UumOrgan> findAllOrgans() {
		return organRepository.findAllOrgans();
	}

	@Override
	public List<UumOrgan> findChildNodes(long organId, int flag) {
		List<UumOrgan> filterList = new ArrayList<UumOrgan>();
		List<UumOrgan> listAll = findAllOrgans();
		List<UumOrgan> listAllTemp = new ArrayList<UumOrgan>();
		listAllTemp.addAll(listAll);
		listAllTemp.remove(new UumOrgan(0L, null)); //删除根元素
		//过滤出所有下级元素
		filterList = getFilterList(filterList, listAllTemp, organId);
		if (organId != 0) {
			// 加入本元素
			for (UumOrgan organ : listAll) {
				if (organ.getOrgId() == organId) {
					filterList.add(organ);
				}
			}
		}
		if (flag == 1) {
			listAll.removeAll(filterList);
			filterList = listAll;
			if (filterList != null && filterList.size() > 0) {
				for (UumOrgan organ : filterList) {
					organ.setUpOrgId(organ.getUumOrgan().getOrgId());
				}
			}
		}
		
		return filterList;
	}

	@Override
	public List<TreeVo> findNextLevelChildNodes(long organId, long filterOrgId) {
		List<UumOrgan> list = organRepository.findOrgansByUpOrgIdAFilter(organId, filterOrgId);
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if(list != null) {
			for(UumOrgan organ : list) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(organ.getOrgId().toString());
				treeVo.setText(organ.getOrgSimpleName());
				if(TWO.equals(organ.getOrgType())) {
					treeVo.setLeaf(true);
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Override
	public List<TreeVoExtend> findNextLevelChildNodesWithCheckbox(long organId, long filterOrgId) {
		List<UumOrgan> list = organRepository.findOrgansByUpOrgIdAFilter(organId, filterOrgId);
		List<TreeVoExtend> treeVoList = new ArrayList<TreeVoExtend>();
		if (list != null && list.size() > 0) {
			for (UumOrgan uumOrgan : list) {
				TreeVoExtend treeVo = new TreeVoExtend();
				treeVo.setId(uumOrgan.getOrgId().toString());
				treeVo.setText(uumOrgan.getOrgSimpleName());
				if (TWO.equals(uumOrgan.getOrgType())) {
					treeVo.setLeaf(true);
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Override
	public Page<UumOrgan> findOrgansByCon(long treeId, String orgCode, 
			String orgName, Pageable page) {
		
		logger.debug("start=" + page.getOffset() + " limit=" + page.getPageSize() 
				+ " treeId= " + treeId + " orgName" + orgName + " orgCode" + orgCode);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId >= 0) {
			List<UumOrgan> list = this.findChildNodes(treeId, 0);
			for(UumOrgan organ : list) {
				idList.add(organ.getOrgId());
			}
			if(idList.isEmpty()) return new PageImpl<UumOrgan>(new ArrayList<UumOrgan>(), page, 0);
		}
		
		return organRepository.findOrgansByCon(idList.toArray(new Long[idList.size()]), orgCode, orgName, page);
	}

	@Override
	public UumOrgan getOrganByOrgId(long organId) {
		logger.debug("will find organ id is " + organId);
		
		UumOrgan organ = (UumOrgan)organRepository.get(organId);
		organ.setUpOrgId(organ.getUumOrgan().getOrgId());
		organ.setUpOrgName(organ.getUumOrgan().getOrgSimpleName());
		
		UumOrgan parentOrgan = organRepository.get(organ.getUumOrgan().getOrgId());
		organ.setUpOrgId(parentOrgan.getOrgId());
		organ.setUpOrgName(parentOrgan.getOrgSimpleName());
		
		SysplDistrict district = platformCommonService.getDistrictByDistrictId(organ.getOrgBelongDist());
		if(district != null) {
			organ.setOrgBelongDistName(district.getDistrictName());
		}
		
		return organ;
	}

	@Override
	public List findOrgansByOrgIds(Long[] orgIds) {
		if(orgIds == null || orgIds.length == 0) return null;
		
		return organRepository.findOrgansByOrgIds(orgIds);
	}

	@Transactional(readOnly=false)
	@Override
	public void updateOrgan(UumOrgan uumOrgan, HttpServletRequest request) 
			throws ApplicationException {
		
		organRepository.update(uumOrgan);
		writeLog(uumOrgan, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveOrgan(UumOrgan uumOrgan, HttpServletRequest request) 
			throws ApplicationException {
		
		organRepository.save(uumOrgan);
		writeLog(uumOrgan, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}

   
	
	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用模块写日志
	 *
	 * @param uumOrgan 机构对象
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 7:39:34 PM
	 */
	private void writeLog(UumOrgan uumOrgan,String logLevel,String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = "机构名称 : " + uumOrgan.getOrgSimpleName() + SPLIT
			+ "机构代码 : " + uumOrgan.getOrgCode() + SPLIT
			+ "上级机构 : " + uumOrgan.getUpOrgName() + SPLIT;
		sysOptLogService.saveLog(logLevel, ORGAN_MGR, opt, optContent, uumOrgan.getOrgId() + "", request);
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
	private List<UumOrgan> getFilterList(List<UumOrgan> filterList, List<UumOrgan> listAll, long organId) {
		List<UumOrgan> subList = getSubList(listAll, organId);
		if (!subList.isEmpty()) {
			filterList.addAll(subList);
		}
		for (UumOrgan organ : subList) {
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
	private List<UumOrgan> getSubList(List<UumOrgan> listAll, long organId) {
		List<UumOrgan> newList = new ArrayList<UumOrgan>();
		for(UumOrgan organ : listAll) {
			if(organ.getUumOrgan().getOrgId() == organId) {
				newList.add(organ);
			}
		}
		
		return newList;
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setOrganRepository(UumOrganRepository organRepository) {
		this.organRepository = organRepository;
	}
	@Autowired
	public void setRoleOrganService(RoleOrganService roleOrganService) {
		this.roleOrganService = roleOrganService;
	}
	@Autowired
	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}
	@Autowired
	public void setPlatformCommonService(PlatformCommonService platformCommonService) {
		this.platformCommonService = platformCommonService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}
