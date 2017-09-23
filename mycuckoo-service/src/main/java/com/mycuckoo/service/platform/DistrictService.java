package com.mycuckoo.service.platform;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.CITY;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;
import static com.mycuckoo.common.constant.ServiceVariable.DISTRICT;
import static com.mycuckoo.common.constant.ServiceVariable.ENABLE;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_DISTRICT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DistrictMapper;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.DistrictVo;

/**
 * 功能说明: 地区业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:31:29 AM
 * @version 3.0.0
 */
@Service
@Transactional(readOnly=true)
public class DistrictService {
	static Logger logger = LoggerFactory.getLogger(DistrictService.class);
	
	@Autowired
	private DistrictMapper districtMapper;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private SystemOptLogService sysOptLogService;
	

	@Transactional(readOnly=false)
	public boolean disEnable(long districtId, String disEnableFlag) throws ApplicationException {
		if(DISABLE.equals(disEnableFlag)) {
			int count = districtMapper.countByParentId(districtId);
			if(count > 0) { //有下级地区
				return false;
			} else {
				District district = get(districtId);
				district.setStatus(DISABLE);
				update(district);
				
				writeLog(district, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
				return true;
			}
		} else {
			District district = get(districtId);
			district.setStatus(ENABLE);
			update(district);
			
			writeLog(district, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
			return true;
		}
	}

	public boolean existByName(String districtName) {
		int count = districtMapper.countByDistrictName(districtName);
		if(count > 0) return true;
		
		return false;
	}

	public List<DistrictVo> findAll() {
		Page<District> page = districtMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));
		List<DistrictVo> vos = Lists.newArrayList();
		for(District district : page.getContent()) {
			DistrictVo vo = new DistrictVo();
			BeanUtils.copyProperties(district, vo);
			vos.add(vo);
		}
		
		return vos;
	}

	public Page<DistrictVo> findByPage(Map<String, Object> params, Pageable page) {
		long treeId = (Long) params.get("treeId");
		String districtName = (String) params.get("districtName");
		String districtLevel = (String) params.get("districtLevel");
		
		logger.debug("start={} limit={} treeId={} districtName={} districtLevel={}", 
				page.getOffset(), page.getPageSize(), treeId, districtName, districtLevel);
		
		List<Long> idList = new ArrayList<Long>();
		if(treeId >= 0) {
			List<District> list = findChildNodeList(treeId, 0); // 过滤出所有下级
			for (District district : list) {
				idList.add(district.getDistrictId()); // 所有下级地区ID
			}
			if (idList.isEmpty()) {
				idList.add(-1l);
			}
		}

		
		params.put("array", idList.isEmpty() ? null : idList.toArray(new Long[idList.size()]));
		Page<District> entityPage = districtMapper.findByPage(params, page);
		List<DicSmallType> dicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(DISTRICT);
		
		List<DistrictVo> vos = Lists.newArrayList();
		for(District entity : entityPage.getContent()) {			
			String districtLevell = entity.getDistrictLevel();
			for (DicSmallType dicSmallType : dicSmallTypeList) {
				if (districtLevell.equalsIgnoreCase(dicSmallType.getSmallTypeCode())) {
					entity.setDistrictLevel(dicSmallType.getSmallTypeName());
					break;
				}
			}
			
			DistrictVo vo = new DistrictVo();
			BeanUtils.copyProperties(entity, vo);
			vo.setParentName(get(entity.getParentId()).getDistrictName());//上级地区名称
			vos.add(vo);
		}
		
		return new PageImpl<>(vos, page, entityPage.getTotalElements());
	}

	public DistrictVo get(Long districtId) {
		logger.debug("will find district id is {}", districtId);
		
		District district = districtMapper.get(districtId);
		DistrictVo vo = new DistrictVo();
		BeanUtils.copyProperties(district, vo);
		
		return vo;
	}

	public List<TreeVo> findNextLevelChildNodes(long districtId, long filterdistrictId) {
		List<District> list = districtMapper
				.findByParentIdAndFilterIds(districtId, new long[] { 0L, filterdistrictId });
		List<TreeVo> treeVoList = new ArrayList<TreeVo>();
		if(list != null) {
			for(District district : list) {
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
	public void update(District district) throws ApplicationException {
		districtMapper.update(district);
		
		writeLog(district, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
	}

	@Transactional(readOnly=false)
	public void save(District district) throws ApplicationException {
		districtMapper.save(district);
		
		writeLog(district, LogLevelEnum.FIRST, OptNameEnum.SAVE);
	}


	
	// --------------------------- 私有方法 -------------------------------
	/**
	 * 公用地区写日志
	 *
	 * @param district 地区对象
	 * @param logLevel
	 * @param opt
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 16, 2012 7:38:53 PM
	 */
	private void writeLog(District district, LogLevelEnum logLevel, OptNameEnum opt) throws ApplicationException {
		String optContent = district.getDistrictName() + SPLIT + district.getDistrictLevel() + SPLIT;
		sysOptLogService.saveLog(logLevel, opt, SYS_DISTRICT, optContent, district.getDistrictId() + "");
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
	private List<District> findChildNodeList(long districtId, int flag) {
		Page<District> page = districtMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));
		List<District> listAllTemp = new ArrayList<District>();
		listAllTemp.addAll(page.getContent());
		
		//删除根元素
		District district = new District(0l, null);
		listAllTemp.remove(district);
		
		//过滤出所有下级元素
		List<District> filterList = new ArrayList<District>();
		filterList = getFilterList(filterList, listAllTemp, districtId);
		if(flag == 1) {
			// 本元素
			District districtOld = new District();
			districtOld.setDistrictId(districtId);
			filterList.add(districtOld);
			page.getContent().removeAll(filterList);
			filterList = page.getContent();
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
	private List<District> getFilterList(List<District> filterList, 
			List<District> listAll, long districtId) {
		
		List<District> subList = getSubList(listAll, districtId);
		if(!subList.isEmpty()) filterList.addAll(subList);
		for(District district : subList) {
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
	private List<District> getSubList(List<District> listAll, long districtId) {
		List<District> newList = new ArrayList<District>();
		for(District district : listAll) {
			if(district.getParentId() == districtId) 
			newList.add(district);
		}
		
		return newList;
	}	
}
