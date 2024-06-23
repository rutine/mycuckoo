package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.platform.Code;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
public class CodeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(CodeMapperTest.class);

    @Autowired
    private CodeMapper mapper;


    @Test
    public void testCountByCode() {
        long count = mapper.countByCode("RKD");

        logger.info("------> countByCode: {}", count);
    }

    @Test
    public void testGetByCode() {
        Code code = this.mapper.getByCode("RKD");

        logger.info("------> getByCode: {}", code);
    }

    @Test
    public void testSave() {
        Code code = new Code();
        code.setEffect("编码效果");
        code.setCode("编码代码");
        code.setName("编码名称");
        code.setDelimiter(",");
        code.setMemo("供测试");
        code.setModuleName("属于模块");
        code.setPart1("st");
        code.setPart1Con("-");
        code.setPart2("tine");
        code.setPart2Con("-");
        code.setPart3("ru");
        code.setPart3Con("->");
        code.setPartNum(3);
        code.setStatus("enable");
        code.setUpdateTime(LocalDateTime.now());
        code.setUpdator("1");
        code.setCreateTime(LocalDateTime.now());
        code.setCreator("1");

        mapper.save(code);

        Assert.assertEquals(new Long(1), code.getCodeId(), 20L);
    }

    @Test
    public void testUpdate() {
        Code code = new Code();
        code.setEffect("编码效果");
        code.setCode("编码英文名");
        code.setName("编码名称");
        code.setDelimiter(",");
        code.setMemo("供测试");
        code.setModuleName("属于模块");
        code.setPart1("st");
        code.setPart1Con("-");
        code.setPart2("tine");
        code.setPart2Con("-");
        code.setPart3("ru");
        code.setPart3Con("->");
        code.setPartNum(3);
        code.setStatus("enable");
        code.setCodeId(25L);
        code.setUpdateTime(LocalDateTime.now());
        code.setUpdator("1");

        int row = mapper.update(code);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        Code code = mapper.get(25L);

        Assert.assertNotNull(code);
        Assert.assertEquals("技术", code.getCode());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(25L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("code", "%RKD%");
        params.put("name", null);
        params.put("moduleName", null);
        Page<Code> page = this.mapper.findByPage(params, new PageRequest(0, 10));

        for (Code entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}
