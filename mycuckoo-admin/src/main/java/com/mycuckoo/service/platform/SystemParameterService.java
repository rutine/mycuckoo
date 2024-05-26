package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.repository.platform.SysParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.mycuckoo.constant.ServiceConst.DISABLE;
import static com.mycuckoo.constant.ServiceConst.ENABLE;

/**
 * 功能说明: 系统参数业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:55:50 AM
 */
@Service
@Transactional(readOnly = true)
public class SystemParameterService {

    @Autowired
    private SysParameterMapper sysParameterMapper;


    @Transactional
    public boolean disEnable(long id, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        SysParameter old = get(id);
        if (!enable) {
            old.setStatus(DISABLE);
            sysParameterMapper.update(old);
        } else {
            old.setStatus(ENABLE);
            sysParameterMapper.update(old);
        }

        writeLog(old, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public boolean countByKey(String paraKeyName) {
        int count = sysParameterMapper.countByKey(paraKeyName);
        if (count > 0) return true;

        return false;
    }

    public Page<SysParameter> findByPage(Querier querier) {
        return sysParameterMapper.findByPage(querier.getQ(), querier);
    }

    public SysParameter get(long paraId) {
        return sysParameterMapper.get(paraId);
    }

    public SysParameter getByKey(String key) {
        return sysParameterMapper.getByKey(key);
    }

    @Transactional
    public void update(SysParameter entity) {
        entity.setKey(null); //参数键不可修改
        sysParameterMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(SysParameter entity) {
        Assert.state(!countByKey(entity.getKey()), "键值[" + entity.getKey() + "]已存在!");
        sysParameterMapper.save(entity);

        writeLog(entity, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity 系统参数对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 15, 2012 8:20:17 PM
     */
    private void writeLog(SysParameter entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_PARAMETER)
                .operate(opt)
                .id(entity.getParaId())
                .title(null)
                .content("参数名称：%s, 参数键值：%s, 参数值: %s, 参数类型: %s",
                        entity.getName(), entity.getKey(), entity.getValue(), entity.getType())
                .level(logLevel)
                .emit();
    }

}
