package com.mycuckoo.persistence.uum;

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
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.persistence.iface.uum.UumOrganRepository;

/**
 * UumOrganRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 * @author rutine
 * @time Jul 14, 2012 11:10:45 AM
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumOrganRepositoryTest extends
	AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UumOrganRepository uumOrganRepository;
    
//    @Test
    public void saveOrgan() {
	UumOrgan organ = new UumOrgan();
	organ.setCreateDate(Calendar.getInstance().getTime());
	organ.setMemo("测试");
	organ.setOrgCode("10010");
	organ.setOrgSimpleName("技术部");
	organ.setStatus("disable");
	
	uumOrganRepository.save(organ);
	
	Assert.assertEquals(new Long(5), organ.getOrgId(), 20L);
    }
    
//    @Test
    public void updateOrgan() {
	UumOrgan organ = new UumOrgan();
	organ.setCreateDate(Calendar.getInstance().getTime());
	organ.setMemo("测试");
	organ.setOrgCode("10010");
	organ.setOrgSimpleName("技术部");
	organ.setStatus("disable");
	organ.setOrgId(2L);
	
	int row =uumOrganRepository.update(organ);
	
	Assert.assertEquals(1, row);
    }
    
//    @Test
    public void updateOrgans() {
   	UumOrgan organ = new UumOrgan();
   	organ.setCreateDate(Calendar.getInstance().getTime());
   	organ.setMemo("测试");
   	organ.setOrgCode("10010");
   	organ.setOrgSimpleName("技术部");
   	organ.setStatus("disable");
   	organ.setOrgId(2L);
   	
   	List<UumOrgan> list = Lists.newArrayList();
   	list.add(organ);
   	organ = new UumOrgan();
   	organ.setCreateDate(Calendar.getInstance().getTime());
   	organ.setMemo("测试");
   	organ.setOrgCode("10010");
   	organ.setOrgSimpleName("技术部");
   	organ.setStatus("disable");
   	organ.setOrgId(4L);
   	list.add(organ);
   	
   	int row =uumOrganRepository.update(list);
   	
   	Assert.assertEquals(1, row);
    }
    
//    @Test
    public void deleteOrgan() {
	uumOrganRepository.delete(3L);
    }
    
//    @Test
    public void existsOrgan() {
	boolean exists = uumOrganRepository.exists(34L);
	
	Assert.assertEquals(Boolean.TRUE, exists);
    }
    
//    @Test
    public void countOrgan() {
	long count = uumOrganRepository.count();
	
	logger.debug(count);
    }
    
//    @Test
    public void getOrgan() {
	UumOrgan organ = uumOrganRepository.get(2L);
	
	Assert.assertNotNull(organ);
	Assert.assertEquals("技术", organ.getOrgSimpleName());
    }
    
//    @Test
    public void findAllOrgan() {
	List<UumOrgan> list = (List<UumOrgan>)uumOrganRepository.findAll();
	
	for(UumOrgan organ : list)
	    logger.debug(organ.getOrgId());
    }
    
//    @Test
    public void countSubOrgansByOrgId() {
	logger.debug(uumOrganRepository.countOrgansByUpOrgId(12L));
    }
    
//    @Test
    public void countOrgansByOrgSimpleName() {
	logger.debug(uumOrganRepository.countOrgansByOrgSimpleName("销售"));
    }
    
//    @Test
    public void findSubsOrgansByOrgIdAFirlter() {
	List<UumOrgan> list = (List<UumOrgan>)uumOrganRepository.findOrgansByUpOrgIdAFilter(1L, 3L);
	
	logger.debug(list);
    }
    
//    @Test
    public void findOrgansByConditions() {
	Page<UumOrgan> page = uumOrganRepository.findOrgansByCon(new Long[]{0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L}, "2", "董事", new PageRequest(0, 10));
	
	logger.debug(page.getContent());
    }
    
    @Test
    @SuppressWarnings("rawtypes")
    public void findOrgansByOrgIds() {
	List list = uumOrganRepository.findOrgansByOrgIds(new Long[]{0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L});
	
	logger.debug(list);
    }
}