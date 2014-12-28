package com.mycuckoo.persistence.platform;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.persistence.iface.platform.SysplAfficheRepository;

/**
 * 功能说明: SysplAfficheRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:08:24 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplAfficheRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private SysplAfficheRepository sysplAfficheRepository;
	
//	@Test
	public void saveSysplAffiche() {
		SysplAffiche affiche = new SysplAffiche();
		affiche.setAfficheContent("公告测试");
		affiche.setAfficheInvalidate(Calendar.getInstance().getTime());
		affiche.setAffichePulish((short)6);
		affiche.setAfficheTitle("公测");
		
		sysplAfficheRepository.save(affiche);
		
		Assert.assertEquals(new Long(1), affiche.getAfficheId(), 20L);
	}
	
//	@Test
	public void updateSysplAffiche() {
		SysplAffiche affiche = new SysplAffiche();
		affiche.setAfficheContent("公告测试");
		affiche.setAfficheInvalidate(Calendar.getInstance().getTime());
		affiche.setAffichePulish((short)6);
		affiche.setAfficheTitle("公测");
		affiche.setAfficheId(9L);
		
		int row = sysplAfficheRepository.update(affiche);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplAffiches() {
		SysplAffiche affiche = new SysplAffiche();
		affiche.setAfficheContent("公告测试");
		affiche.setAfficheInvalidate(Calendar.getInstance().getTime());
		affiche.setAffichePulish((short)6);
		affiche.setAfficheTitle("公测");
		affiche.setAfficheId(9L);
		
		List<SysplAffiche> list = Lists.newArrayList();
		list.add(affiche);
		affiche = new SysplAffiche();
		affiche.setAfficheContent("公告测试");
		affiche.setAfficheInvalidate(Calendar.getInstance().getTime());
		affiche.setAffichePulish((short)6);
		affiche.setAfficheTitle("公测");
		affiche.setAfficheId(6L);
		list.add(affiche);
		
		int row = sysplAfficheRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplAffiche() {
		sysplAfficheRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplAffiche() {
		boolean exists = sysplAfficheRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplAffiche() {
		long count = sysplAfficheRepository.count();
	
	logger.debug(count);
	}
	
//	@Test
	public void getSysplAffiche() {
		SysplAffiche affiche = sysplAfficheRepository.get(6L);
		
		Assert.assertNotNull(affiche);
		Assert.assertEquals("技术", affiche.getAfficheContent());
	}
	
//	@Test
	public void findAllSysplAffiche() {
	
		List<SysplAffiche> list = (List<SysplAffiche>)sysplAfficheRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getAfficheId());
	}
	
//	@Test
	public void deleteAfficheByIds() {
		this.sysplAfficheRepository.deleteAffichesByIds(new Long[]{0l, 1l, 2l, 3l, 4l, 5l});
	}
	
//	@Test
	public void findAffichesByConditions() {
		Page<SysplAffiche> page = this.sysplAfficheRepository.findAffichesByCon("工", null, new PageRequest(0, 10));
		
		logger.debug(page.getTotalElements());
	}
	
	@Test
	public void findAllAffichesBeforeValidate() {
		List<SysplAffiche> list = this.sysplAfficheRepository.findAffichesBeforeValidate();
		
		for(SysplAffiche affiche : list) {
			logger.debug(affiche.getAfficheTitle());
		}
	}
}
