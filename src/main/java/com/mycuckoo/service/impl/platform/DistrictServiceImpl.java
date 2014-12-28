package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.CITY;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.DISTRICT;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE_DIS;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_DISTRICT;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplDistrictRepository;
import com.mycuckoo.service.iface.platform.DictionaryService;
import com.mycuckoo.service.iface.platform.DistrictService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 地区业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:31:29 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class DistrictServiceImpl implements DistrictService {
	
	static Logger logger = LoggerFactory.getLogger(DistrictServiceImpl.class);
	private SysplDistrictRepository districtRepository;
	private DictionaryService dictionaryService;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly=false)
	@Override
	public boolean disEnableDistrict(long districtId, String disEnableFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		if(DISABLE.equals(disEnableFlag)) {
			int count = districtRepository.countDistrictsByUpDistrictId(districtId);
			if(count > 0) { //有下级地区
				return false;
			} else {
				SysplDistrict sysplDistrict = getDistrictByDistrictId(districtId);
				sysplDistrict.setStatus(DISABLE);
				updateDistrict(sysplDistrict, request);
				writeLog(sysplDistrict, LOG_LEVEL_SECOND, DISABLE_DIS, request);
				return true;
			}
		} else {
			SysplDistrict sysplDistrict = getDistrictByDistrictId(districtId);
			sysplDistrict.setStatus(ENABLE);
			updateDistrict(sysplDistrict, request);
			writeLog(sysplDistrict, LOG_LEVEL_SECOND, ENABLE_DIS, request);
			return true;
		}
	}

	@Override
	public boolean existDistrictByName(String districtName) {
		int count = districtRepository.countDistrictsByDistrictName(districtName);
		if(count > 0) return true;
		
		return false;
	}

	@Override
	public List<SysplDistrict> findAllDistricts() {
		List<SysplDistrict> list = (List<SysplDistrict>) districtRepository.findAll();
		if(list != null) {
			for(SysplDistrict district : list) {
			district.setUpDistrictId(district.getSysplDistrict().getDistrictId());
			}
		}
		
		return list;
	}

	@Override
	public Page<SysplDistrict> findDistrictsByCon(long treeId, String districtName, 
			String districtLevel, Pageable page) {
		
		logger.debug("start=" + page.getOffset() + " limit=" + page.getPageSize() 
				+ " treeId= " + treeId + " districtName" + districtName + " districtLevel" + districtLevel);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId >= 0) {
			List<SysplDistrict> list = findChildNodeList(treeId, 0); // 过滤出所有下级
			for (SysplDistrict sysplDistrict : list) {
				Long districtId = sysplDistrict.getDistrictId();
				idList.add(districtId); // 所有下级地区ID
			}
			if (idList.isEmpty()) {
				idList.add(-1l);
			}
		}
		List<SysplDicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(DISTRICT);
		Page<SysplDistrict> distPage = districtRepository
				.findDistrictsByCon(idList.toArray(new Long[idList.size()]), districtName, districtLevel, page);
		List<SysplDistrict> districtList = distPage.getContent();
		for(SysplDistrict district : districtList) {
			String districtLevell = district.getDistrictLevel();
			for (SysplDicSmallType dicSmallType : dicSmallTypeList) {
				if (districtLevell.equalsIgnoreCase(dicSmallType.getSmallTypeCode())) {
					district.setDistrictLevel(dicSmallType.getSmallTypeName());
					break;
				}
			}
			district.setUpDistrictName(district.getSysplDistrict().getDistrictName()); //上级地区名称
		}
		
		return distPage;
	}

	@Override
	public SysplDistrict getDistrictByDistrictId(Long districtId) {
		logger.debug("will find district id is "+districtId);
		
		return districtRepository.get(districtId);
	}

	@Override
	public List<TreeVo> findNextLevelChildNodes(long districtId, long filterdistrictId) {
		List<SysplDistrict> list = districtRepository.findDistrictsByUpDistrictsAFilterIds(districtId, filterdistrictId);
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if(list != null) {
			for(SysplDistrict district : list) {
				TreeVo treeVo = new TreeVo();
				treeVo.setId(district.getDistrictId().toString());
				treeVo.setText(district.getDistrictName());
				if(CITY.equalsIgnoreCase(district.getDistrictLevel())) {
					treeVo.setLeaf(true); // 城市节点
				} else {
					treeVo.setIsParent(true);
				}
				treeVoList.add(treeVo);
			}
		}
		
		return treeVoList;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateDistrict(SysplDistrict district, HttpServletRequest request) 
			throws ApplicationException {
		
		districtRepository.update(district);
		writeLog(district, LOG_LEVEL_SECOND, MODIFY_OPT, request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveDistrict(SysplDistrict district, 
			HttpServletRequest request) throws ApplicationException {
		
		districtRepository.save(district);
		writeLog(district, LOG_LEVEL_FIRST, SAVE_OPT, request);
	}


	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用地区写日志
	 *
	 * @param district 地区对象
	 * @param logLevel
	 * @param opt
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 16, 2012 7:38:53 PM
	 */
	private void writeLog(SysplDistrict district, String logLevel, String opt, 
			HttpServletRequest request) throws ApplicationException {
		
		String optContent = district.getDistrictName() + SPLIT + district.getDistrictLevel() + SPLIT;
		sysOptLogService.saveLog(logLevel, SYS_DISTRICT, opt, optContent, district.getDistrictId() + "", request);
	}

	/**
	 * 根据地区id查询所有地区节点
	 *
	 * @param districtId 上级地区id
	 * @param flag 0为下级，1 为上级
	 * @return
	 * @author rutine
	 * @time Oct 16, 2012 8:31:35 PM
	 */
	private List<SysplDistrict> findChildNodeList(long districtId, int flag) {
		List<SysplDistrict> filterList = new ArrayList<SysplDistrict>();
		List<SysplDistrict> listAll = (List<SysplDistrict>) districtRepository.findAll();
		List<SysplDistrict> listAllTemp = new ArrayList<SysplDistrict>();
		listAllTemp.addAll(listAll);
		//删除根元素
		SysplDistrict sysplDistrict = new SysplDistrict(0l, null);
		listAllTemp.remove(sysplDistrict);
		//过滤出所有下级元素
		filterList = getFilterList(filterList, listAllTemp, districtId);
		if(flag == 1) {
			// 本元素
			SysplDistrict sysplDistrictOld = new SysplDistrict();
			sysplDistrictOld.setDistrictId(districtId);
			filterList.add(sysplDistrictOld);
			listAll.removeAll(filterList);
			filterList = listAll;
			if (filterList != null && filterList.size() > 0) {
				for (SysplDistrict mod : filterList) {
					mod.setUpDistrictId(mod.getSysplDistrict().getDistrictId());
				}
			}
		}
		return filterList;
	}
	/**
	 * 根据上级地区id递归过滤结点
	 *
	 * @param filterList 过滤的子节点
	 * @param listAll 所有地区结果集
	 * @param districtId 上级地区id
	 * @return 所有子结点
	 * @author rutine
	 * @time Oct 16, 2012 7:44:35 PM
	 */
	private List<SysplDistrict> getFilterList(List<SysplDistrict> filterList, 
			List<SysplDistrict> listAll, long districtId) {
		
		List<SysplDistrict> subList = getSubList(listAll, districtId);
		if(!subList.isEmpty()) filterList.addAll(subList);
		for(SysplDistrict district : subList) {
			getFilterList(filterList, listAll, district.getDistrictId());
		}
		
		return filterList;
	}
	/**
	 * 根据地区id获得所有子结点
	 * 
	 * @param listAll  所有地区结果集
	 * @param districtId 上级地区id
	 * @return 所有子结点
	 * @author rutine
	 * @time Oct 16, 2012 7:40:46 PM
	 */
	private List<SysplDistrict> getSubList(List<SysplDistrict> listAll, long districtId) {
		List<SysplDistrict> newList = new ArrayList<SysplDistrict>();
		for(SysplDistrict district : listAll) {
			if(district.getSysplDistrict().getDistrictId() == districtId) 
			newList.add(district);
		}
		
		return newList;
	}
	
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setDistrictRepository(SysplDistrictRepository districtRepository) {
		this.districtRepository = districtRepository;
	}
	@Autowired
	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	
}
