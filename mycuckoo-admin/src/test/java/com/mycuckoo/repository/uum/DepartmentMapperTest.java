package com.mycuckoo.repository.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.uum.Department;
import com.mycuckoo.domain.uum.DepartmentExtend;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DepartmentMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DepartmentMapperTest.class);

    @Autowired
    private DepartmentMapper departmentMapper;


    @Test
    public void testCountByParentId() {
        int count = departmentMapper.countByParentId(12L);

        logger.info("------> countByParentId: {}", count);
    }

    @Test
    public void testFindByPage2() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("treeId", "1");
        params.put("name", "%董事%");
        Page<Department> page = departmentMapper.findByPage(params, new PageRequest(0, 5));

        logger.info("------> findByPage2: {}", page);
    }

    @Test
    public void testFindByOrgIds() {
        List<DepartmentExtend> list = departmentMapper.findByDeptIds(Arrays.array(0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L));

        logger.info("------> findByOrgIds: {}", list);
    }

    @Test
    public void testSave() {
        Department entity = new Department();
        entity.setUpdator("1");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator("1");
        entity.setCreateTime(LocalDateTime.now());
        entity.setMemo("测试");
        entity.setCode("10010");
        entity.setName("技术部");
        entity.setStatus("disable");

        departmentMapper.save(entity);

        Assert.assertEquals(new Long(5), entity.getDeptId(), 20L);
    }

    @Test
    public void testUpdate() {
        Department entity = new Department();
        entity.setUpdator("1");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator("1");
        entity.setCreateTime(LocalDateTime.now());
        entity.setMemo("测试");
        entity.setCode("10010");
        entity.setName("技术部");
        entity.setStatus("disable");
        entity.setOrgId(2L);

        int row = departmentMapper.update(entity);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        departmentMapper.delete(3L);
    }

    @Test
    public void testGet() {
        Department entity = departmentMapper.get(2L);

        Assert.assertNotNull(entity);
        Assert.assertEquals("技术", entity.getName());
    }

    @Test
    public void testExists() {
        boolean exists = departmentMapper.exists(34L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<Department> page = departmentMapper.findByPage(null, new PageRequest(0, 5));

        logger.info("------> findByPage: {}", page);
    }

}
