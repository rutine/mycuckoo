package com.mycuckoo.service.platform;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.SysParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnable(long paraId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            SysParameter sysParameter = get(paraId);
            sysParameter.setStatus(DISABLE);
            sysParameterMapper.update(sysParameter);

            writeLog(sysParameter, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
        } else {
            SysParameter sysParameter = get(paraId);
            sysParameter.setStatus(ENABLE);
            sysParameterMapper.update(sysParameter);

            writeLog(sysParameter, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
        }

        return true;
    }

    public Page<SysParameter> findByPage(Map<String, Object> params, Pageable page) {
        return sysParameterMapper.findByPage(params, page);
    }

    public SysParameter get(long paraId) {
        return sysParameterMapper.get(paraId);
    }

    public SysParameter getByParaName(String paraName) {
        return sysParameterMapper.getByParaName(paraName);
    }

    @Transactional
    public void update(SysParameter systemParameter) {
        sysParameterMapper.update(systemParameter);

        writeLog(systemParameter, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
    }

    @Transactional
    public void save(SysParameter systemParameter) {
        sysParameterMapper.save(systemParameter);

        writeLog(systemParameter, LogLevelEnum.FIRST, OptNameEnum.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param systemParameter 系统参数对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 15, 2012 8:20:17 PM
     */
    private void writeLog(SysParameter systemParameter, LogLevelEnum logLevel, OptNameEnum opt) {
        StringBuilder optContent = new StringBuilder();
        optContent.append("参数名称：").append(systemParameter.getParaName()).append(SPLIT);
        optContent.append("参数键值：").append(systemParameter.getParaKeyName()).append(SPLIT);
        optContent.append("参数值：").append(systemParameter.getParaValue()).append(SPLIT);
        optContent.append("参数类型：").append(systemParameter.getParaType()).append(SPLIT);
        sysOptLogService.saveLog(logLevel, opt, SYS_PARAMETER,
                optContent.toString(), systemParameter.getParaId() + "");
    }

}
