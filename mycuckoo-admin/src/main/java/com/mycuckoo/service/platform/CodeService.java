package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.web.SessionUtil;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.repository.platform.CodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;
import static com.mycuckoo.core.util.CommonUtils.isEmpty;

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


    @Transactional
    public boolean disEnable(long id, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            codeMapper.update(new Code(id, DISABLE));
        } else {
            codeMapper.update(new Code(id, ENABLE));
        }

        Code entity = get(id);
        LogOperator.begin()
                .module(ModuleName.SYS_CODE)
                .operate(enable ? OptName.ENABLE : OptName.DISABLE)
                .id(entity.getCodeId())
                .title(null)
                .content("%s编码, 编号：%s, 名称：%s, 所属模块名称：%s, 编码效果: %s",
                        enable ? "启用" : "停用",
                        entity.getCode(),
                        entity.getName(),
                        entity.getModuleName(),
                        entity.getEffect())
                .level(LogLevel.SECOND)
                .emit();

        return true;
    }

    public Page<Code> findByPage(Querier querier) {
        return codeMapper.findByPage(querier.getQ(), querier);
    }

    public boolean existByCode(String code) {
        int count = codeMapper.countByCode(code);

        return count > 0;
    }

    public Code get(Long id) {
        Code code = codeMapper.get(id);

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
    public void update(Code entity) {
        Code old = get(entity.getCodeId());
        Assert.notNull(old, "编码不存在!");
        Assert.state(old.getCode().equals(entity.getCode())
                || !existByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdator(SessionUtil.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        codeMapper.update(entity);

        LogOperator.begin()
                .module(ModuleName.SYS_CODE)
                .operate(OptName.MODIFY)
                .id(entity.getCodeId())
                .title(null)
                .content("编号：%s, 名称：%s, 所属模块名称：%s, 编码效果: %s",
                        entity.getCode(),
                        entity.getName(),
                        entity.getModuleName(),
                        entity.getEffect())
                .level(LogLevel.SECOND)
                .emit();
    }

    /**
     * <b>注意：</b>该方法存在问题没有解决, 暂待解决.
     */
    public String getCodeNameByCodeEngName(String codeEngName) {
        String currentCode = "";

        Code code = codeMapper.getByCode(codeEngName);
        String part1 = code.getPart1();
        String part1Con = code.getPart1Con();
        String part2 = code.getPart2();
        String part2Con = code.getPart2Con();
        String part3 = code.getPart3();
        String part3Con = code.getPart3Con();
        String part4 = code.getPart4();
        String part4Con = code.getPart4Con();
        String delimite = code.getDelimiter();

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
    public void save(Code entity) throws ApplicationException {
        Assert.state(!existByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setStatus(ENABLE);
        entity.setUpdator(SessionUtil.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator(SessionUtil.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        codeMapper.save(entity);

        LogOperator.begin()
                .module(ModuleName.SYS_CODE)
                .operate(OptName.SAVE)
                .id(entity.getCodeId())
                .title(null)
                .content("编号：%s, 名称：%s, 所属模块名称：%s, 编码效果: %s",
                        entity.getCode(),
                        entity.getName(),
                        entity.getModuleName(),
                        entity.getEffect())
                .level(LogLevel.FIRST)
                .emit();
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
        if (isEmpty(part) || isEmpty(partCon)) return "";
        if ("date".equalsIgnoreCase(part)) {
            SimpleDateFormat sf = new SimpleDateFormat(partCon);
            partCon = sf.format(new Date());
        } else if ("char".equalsIgnoreCase(part)) {
            // do nothing
        } else if ("number".equalsIgnoreCase(part)) {
            if (!isEmpty(currentCode)) {
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
