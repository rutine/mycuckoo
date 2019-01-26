package com.mycuckoo.service.platform;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.CodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

/**
 * 功能说明: 编码管理业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:25:41 AM
 */
@Service
@Transactional(readOnly = true)
public class CodeService {
    static Logger logger = LoggerFactory.getLogger(CodeService.class);

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnable(long codeId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            Code code = new Code(codeId, DISABLE);
            codeMapper.update(code);

            code = get(codeId);
            StringBuilder optContent = new StringBuilder();
            optContent.append("编码停用：编码英文名称：").append(code.getCodeEngName()).append(SPLIT);
            optContent.append("编码中文名称: ").append(code.getCodeName()).append(SPLIT);
            optContent.append("编码所属模块名称: ").append(code.getCodeName()).append(SPLIT);
            optContent.append("编码效果: ").append(code.getCodeEffect()).append(SPLIT);
            sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.DISABLE, SYS_CODE,
                    optContent.toString(), code.getCodeId().toString());

            return true;
        } else {
            Code code = new Code(codeId, ENABLE);
            codeMapper.update(code);

            code = get(codeId);
            StringBuilder optContent = new StringBuilder();
            optContent.append("编码启用：编码英文名称：").append(code.getCodeEngName()).append(SPLIT);
            optContent.append("编码中文名称: ").append(code.getCodeName()).append(SPLIT);
            optContent.append("编码所属模块名称: ").append(code.getCodeName()).append(SPLIT);
            optContent.append("编码效果: ").append(code.getCodeEffect()).append(SPLIT);
            sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.ENABLE, SYS_CODE,
                    optContent.toString(), code.getCodeId().toString());
        }

        return true;
    }

    public Page<Code> findByPage(Map<String, Object> params, Pageable page) {
        return codeMapper.findByPage(params, page);
    }

    public boolean existByCodeEngName(String codeEngName) {
        int count = codeMapper.countByCodeEngName(codeEngName);

        return count > 0;
    }

    public Code get(Long codeId) {
        Code code = codeMapper.get(codeId);

        List<String> partList = new ArrayList<String>();
        partList.add(code.getPart1());
        partList.add(code.getPart2());
        partList.add(code.getPart3());
        partList.add(code.getPart4());

        List<String> partConList = new ArrayList<String>();
        partConList.add(code.getPart1Con());
        partConList.add(code.getPart2Con());
        partConList.add(code.getPart3Con());
        partConList.add(code.getPart4Con());

        code.setPartList(partList);
        code.setPartConList(partConList);

        return code;
    }

    @Transactional
    public void update(Code code) {
        Code old = get(code.getCodeId());
        Assert.notNull(old, "编码不存在!");
        Assert.state(old.getCodeEngName().equals(code.getCodeEngName())
                || !existByCodeEngName(code.getCodeEngName()), "编码[" + code.getCodeEngName() + "]已存在!");

        codeMapper.update(code);

        StringBuilder optContent = new StringBuilder();
        optContent.append("编码英文名称：").append(code.getCodeEngName()).append(SPLIT);
        optContent.append("编码中文名称: ").append(code.getCodeName()).append(SPLIT);
        optContent.append("编码所属模块名称: ").append(code.getCodeName()).append(SPLIT);
        optContent.append("编码效果: ").append(code.getCodeEffect()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.MODIFY, SYS_CODE,
                optContent.toString(), code.getCodeId().toString());
    }

    /**
     * <b>注意：</b>该方法存在问题没有解决, 暂待解决.
     */
    public String getCodeNameByCodeEngName(String codeEngName) {
        String currentCode = "";

        Code code = codeMapper.getByCodeEngName(codeEngName);
        String part1 = code.getPart1();
        String part1Con = code.getPart1Con();
        String part2 = code.getPart2();
        String part2Con = code.getPart2Con();
        String part3 = code.getPart3();
        String part3Con = code.getPart3Con();
        String part4 = code.getPart4();
        String part4Con = code.getPart4Con();
        String delimite = code.getDelimite();

        part1Con = this.getPartContent(part1, part1Con, currentCode, 1, delimite);
        part2Con = this.getPartContent(part2, part2Con, currentCode, 2, delimite);
        part3Con = this.getPartContent(part3, part3Con, currentCode, 3, delimite);
        part4Con = this.getPartContent(part4, part4Con, currentCode, 4, delimite);

        String newCode = "";
        int partNum = code.getPartNum();
        switch (partNum) {
            case 1:
                newCode = part1Con;
                break;
            case 2:
                newCode = part1Con + delimite + part2Con;
                break;
            case 3:
                newCode = part1Con + delimite + part2Con + delimite + part3Con;
                break;
            case 4:
                newCode = part1Con + delimite + part2Con + delimite + part3Con + delimite + part4Con;
                break;
        }
        logger.info("newCode is : {}", newCode);

//		if ("".equals(currentCode)) {
//			ModuleCode newModuleCode = new ModuleCode();
//			newModuleCode.setCodeContent(newCode);
//			newModuleCode.setCodeEngName(codeEngName);
//			codeMapper.save(newSysplModuleCode);
//		} else {
//			moduleCode.setCodeContent(newCode);
//			codeMapper.update(moduleCode);
//		}

        return newCode;
    }

    @Transactional
    public void saveCode(Code code) throws ApplicationException {
        Assert.state(!existByCodeEngName(code.getCodeEngName()), "编码[" + code.getCodeEngName() + "]已存在!");

        code.setStatus(ENABLE);
        codeMapper.save(code);

        StringBuilder optContent = new StringBuilder();
        optContent.append("编码英文名称：").append(code.getCodeEngName()).append(SPLIT);
        optContent.append("编码中文名称: ").append(code.getCodeName()).append(SPLIT);
        optContent.append("编码所属模块名称: ").append(code.getCodeName()).append(SPLIT);
        optContent.append("编码效果: ").append(code.getCodeEffect()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, SYS_CODE,
                optContent.toString(), code.getCodeId().toString());
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 私有方法获得格式化后的编码
     *
     * @param part
     * @param partCon
     * @param currentCode
     * @param partFlag
     * @param delimite
     * @return
     * @author rutine
     * @time Oct 14, 2012 8:03:22 PM
     */
    private String getPartContent(String part, String partCon, String currentCode, int partFlag, String delimite) {
        if (isNullOrEmpty(part) || isNullOrEmpty(partCon)) return "";
        if ("date".equalsIgnoreCase(part)) {
            SimpleDateFormat sf = new SimpleDateFormat(partCon);
            partCon = sf.format(new Date());
        } else if ("char".equalsIgnoreCase(part)) {
            // do nothing
        } else if ("number".equalsIgnoreCase(part)) {
            if (!isNullOrEmpty(currentCode)) {
                String[] partArr = currentCode.split(delimite);
                String numStr = partArr[partFlag - 1];
                int numLength = numStr.length();
                numStr = (Integer.parseInt(numStr) + 1) + "";
                int newNumLength = numStr.length();
                while (numLength > newNumLength) {
                    numStr = "0" + numStr;
                    newNumLength++;
                }
                partCon = numStr;
            }
        } else if ("sysPara".equalsIgnoreCase(part)) {
            if ("userName".equals(partCon)) {
                partCon = SessionUtil.getUserCode();
            } else if ("roleUser".equals(partCon)) {
                partCon = SessionUtil.getRoleName() + "~" + SessionUtil.getUserCode();
            } else if ("organRoleUser".equals(partCon)) {
                partCon = SessionUtil.getOrganName() + "~" + SessionUtil.getRoleName() + "~" + SessionUtil.getUserCode();
            }
        }

        return partCon;
    }
}
