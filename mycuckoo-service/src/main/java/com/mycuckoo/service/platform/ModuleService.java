package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevel;
import com.mycuckoo.common.constant.ModuleLevel;
import com.mycuckoo.common.constant.OptName;
import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.ModOptRefMapper;
import com.mycuckoo.repository.platform.ModuleMenuMapper;
import com.mycuckoo.service.facade.UumServiceFacade;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;
import static com.mycuckoo.common.utils.CommonUtils.getResourcePath;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

/**
 * 功能说明: 系统模块业务类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 25, 2014 10:37:40 AM
 */
@Service
@Transactional(readOnly = true)
public class ModuleService {

    static Logger logger = LoggerFactory.getLogger(ModuleService.class);

    @Autowired
    private ModuleMenuMapper moduleMenuMapper;
    @Autowired
    private ModOptRefMapper modOptRefMapper;

    @Autowired
    private OperateService operateService;
    @Autowired
    private SystemOptLogService sysOptLogService;
    @Autowired
    private UumServiceFacade uumServiceFacade;


    public List<? extends SimpleTree> buildTree(List<ModuleMenuVo> menus, List<Long> checkedOperations, boolean isCheckbox) {
        List<ModuleMenuVo> firsts = Lists.newArrayList();
        List<ModuleMenuVo> others = Lists.newArrayList();

        // 过滤分类一级、二级、三级菜单
        for (ModuleMenuVo vo : menus) {
            ModuleLevel modLevel = ModuleLevel.of(vo.getModLevel());
            switch (modLevel) {
                case ONE:
                    firsts.add(vo);
                    break;
                case TWO:
                case THREE:
                case FOUR:
                    others.add(vo);
                    break;
            }
        }

        List<SimpleTree> trees = Lists.newArrayList();
        for (ModuleMenuVo vo : firsts) {
            trees.add(this.buildTree(vo, others, checkedOperations, new CheckedHolder(null), isCheckbox));
        }

        return trees;
    }

    @Transactional
    public void deleteModOptRefByOperateId(long operateId) {
        // 查询当前模块的所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByOperateId(operateId);
        List<String> modOptRefIds = modOptRefs.stream()
                .map(ModOptRef::getModOptId)
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 删除模块时自动删除权限下的模块
        uumServiceFacade.deletePrivilegeByModOptId(modOptRefIds.toArray(new String[modOptRefIds.size()]));
        modOptRefMapper.deleteByOperateId(operateId);

        String optContent = "根据操作ID删除模块操作关系,级联删除权限";
        sysOptLogService.saveLog(LogLevel.THIRD, OptName.DELETE, SYS_MODOPT_MGR,
                optContent, operateId + "");
    }

    @Transactional
    public void delete(Long moduleId) {
        logger.debug("will delete module id is {}", moduleId);

        ModuleMenu moduleMenu = get(moduleId);
        // 查询当前模块的所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(moduleId);
        List<String> modOptRefIds = modOptRefs.stream()
                .map(ModOptRef::getModOptId)
                .map(String::valueOf)
                .collect(Collectors.toList());

        // 删除模块时自动删除权限下的模块
        uumServiceFacade.deletePrivilegeByModOptId(modOptRefIds.toArray(new String[modOptRefIds.size()]));
        // 删除模块时自动删除模块下的操作
        modOptRefMapper.deleteByModuleId(moduleId);
        moduleMenuMapper.delete(moduleId);

        this.writeLog(moduleMenu, LogLevel.THIRD, OptName.DELETE);
    }

    @Transactional
    public boolean disEnable(long moduleId, String disableFlag) {
        if (DISABLE.equals(disableFlag)) {
            int count = moduleMenuMapper.countByParentId(moduleId);
            if (count > 0) { // 有下级菜单
                throw new ApplicationException("存在下级菜单");
            }

            ModuleMenu moduleMenu = get(moduleId);
            moduleMenu.setStatus(DISABLE);
            // 查询当前模块的所有操作
            List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(moduleId);
            List<String> modOptRefIds = modOptRefs.stream()
                    .map(ModOptRef::getModOptId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            // 删除模块时自动删除权限下的模块
            uumServiceFacade.deletePrivilegeByModOptId(modOptRefIds.toArray(new String[modOptRefIds.size()]));
            // 停用第三级模块时将自动删除模块下的操作
            modOptRefMapper.deleteByModuleId(moduleId);
            moduleMenuMapper.update(moduleMenu);

            this.writeLog(moduleMenu, LogLevel.SECOND, OptName.DISABLE);
            return true;
        } else {
            ModuleMenu moduleMenu = get(moduleId);
            moduleMenu.setStatus(ENABLE);
            moduleMenuMapper.update(moduleMenu);

            this.writeLog(moduleMenu, LogLevel.SECOND, OptName.ENABLE);
            return true;
        }
    }

    public HierarchyModuleVo filterModule(List<ModuleMenuVo> list) {
        List<ModuleMenuVo> firstList = Lists.newArrayList();
        List<ModuleMenuVo> secondList = Lists.newArrayList();
        List<ModuleMenuVo> thirdList = Lists.newArrayList();

        // 过滤分类一级、二级、三级菜单
        for (ModuleMenuVo vo : list) {
            ModuleLevel modLevel = ModuleLevel.of(vo.getModLevel());
            if (modLevel == null) { continue; }

            switch (modLevel) {
                case ONE:
                    addModule(firstList, vo);
                    break;
                case TWO:
                    secondList.add(vo);
                    break;
                case THREE:
                    thirdList.add(vo);
                    break;
            }
        }

        Map<String, List<ModuleMenuVo>> secondMap = Maps.newHashMap(); // 第二级
        Map<String, List<ModuleMenuVo>> thirdMap = Maps.newHashMap();     // 第三级

        ModuleMenuVo comparator = new ModuleMenuVo();
        // 分类一级包含的二级菜单, 二级包含的三级菜单
        for (ModuleMenuVo firstMod : firstList) { // 1
            List<ModuleMenuVo> firstModChildren = Lists.newArrayList();
            for (ModuleMenuVo secondMod : secondList) { // 2
                if (secondMod.getParentId().equals(firstMod.getModuleId())) {
                    addModule(firstModChildren, secondMod);
                    List<ModuleMenuVo> secondModChildren = Lists.newArrayList();
                    for (ModuleMenuVo thirdMod : thirdList) { // 3
                        if (thirdMod.getParentId().equals(secondMod.getModuleId())) {
                            addModule(secondModChildren, thirdMod);
                        }
                    }
                    thirdMap.put(secondMod.getModuleId().toString(), secondModChildren); // 二级包含的三级子菜单
                }
            }
            secondMap.put(firstMod.getModuleId().toString(), firstModChildren); // 一级包含的二级子菜单
        }

        return new HierarchyModuleVo(firstList, secondMap, thirdMap);
    }

    //TODO 方法名不能代表其含义了
    public List<? extends SimpleTree> findChildNodes(long modId, boolean isCheckbox) {
        List<ModuleMenuVo> all = this.findAll();
        ModuleMenuVo parent = new ModuleMenuVo(modId);
        parent.setParentId(modId);

        List<ModuleMenuVo> tempList = Lists.newArrayList();
        tempList.addAll(all);
        tempList.remove(parent); //删除根元素

        List<? extends SimpleTree> trees = this.buildTree(tempList, Lists.newArrayList(), isCheckbox);

        return trees;
    }

    public List<ModuleMenuVo> findAll() {
        Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);
        List<ModuleMenu> list = moduleMenuMapper.findByPage(null, pageRequest).getContent();
        List<ModuleMenuVo> vos = Lists.newArrayList();
        list.forEach(item -> {
            ModuleMenuVo vo = new ModuleMenuVo();
            vo.setModuleId(item.getModuleId());
            vo.setParentId(item.getParentId());
            vo.setModEnName(item.getModEnName());
            vo.setModName(item.getModName());
            vo.setModIconCls(item.getModIconCls());
            vo.setModLevel(item.getModLevel());
            vo.setModOrder(item.getModOrder());
            vo.setModPageType(item.getModPageType());
            vo.setBelongToSys(item.getBelongToSys());
            vo.setStatus(item.getStatus());
            vo.setCreator(item.getCreator());
            vo.setCreateDate(item.getCreateDate());
            vos.add(vo);
        });

        return vos;
    }

    public List<ModOptRef> findAllModOptRefs() {
        Pageable pageRequest = new PageRequest(0, Integer.MAX_VALUE);
        return modOptRefMapper.findByPage(null, pageRequest).getContent();
    }

    public AssignVo<CheckBoxTree, Long> findOperationTreeByModId(long moduleId) {
        List<Operate> allOperates = operateService.findAll(); //所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(moduleId); //已经分配的操作
        List<Long> optIds = modOptRefs.parallelStream()
                .map(ModOptRef::getOperate)
                .map(Operate::getOperateId)
                .collect(Collectors.toList());

        List<CheckBoxTree> trees = Lists.newArrayList();
        allOperates.forEach(consumer -> {
            boolean checked = optIds.contains(consumer.getOperateId());

            CheckBoxTree tree = new CheckBoxTree();
            tree.setId(consumer.getOperateId().toString());
            tree.setParentId("0");
            tree.setText(consumer.getOptName());
            tree.setIconSkin(consumer.getOptIconCls());
            tree.setIsLeaf(true);
            tree.setChildren(null);
            tree.setChecked(checked);
            tree.setCheckBox(new CheckBoxTree.CheckBox(checked ? 1 : 0));
            trees.add(tree);
        });

        return new AssignVo<>(trees, optIds);
    }

    public List<ModOptRef> findModOptRefsByModOptRefIds(Long[] modOptRefIds) {
        if (modOptRefIds == null || modOptRefIds.length == 0) {
            return new ArrayList<>(0);
        }

        return modOptRefMapper.findByIds(modOptRefIds);
    }

    public Page<ModuleMenuVo> findByPage(long treeId, String modName, String modEnName, Pageable page) {
        logger.debug("start={} limit={} treeId={} modName={} modEnName={}",
                page.getOffset(), page.getPageSize(), treeId, modName, modEnName);

        Map<String, Object> params = Maps.newHashMap();
        params.put("modName", isNullOrEmpty(modName) ? null : "%" + modName + "%");
        params.put("modEnName", isNullOrEmpty(modEnName) ? null : "%" + modEnName + "%");
        Page<ModuleMenu> entityPage = moduleMenuMapper.findByPage(params, page);

        List<ModuleMenuVo> vos = Lists.newArrayList();
        for (ModuleMenu entity : entityPage.getContent()) {
            ModuleMenuVo vo = new ModuleMenuVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        }

        return new PageImpl<>(vos, page, entityPage.getTotalElements());
    }

    public ModuleMenuVo get(Long moduleId) {
        logger.debug("will find module id is {}", moduleId);

        ModuleMenu entity = moduleMenuMapper.get(moduleId);
        ModuleMenuVo vo = new ModuleMenuVo();
        BeanUtils.copyProperties(entity, vo);

        return vo;
    }

    public boolean existsByModEnName(String modEnName) {
        int totalProperty = moduleMenuMapper.countByModEnName(modEnName);

        logger.debug("find module total is {}", totalProperty);

        if (totalProperty > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public void update(ModuleMenu modMenu) {
        ModuleMenu old = get(modMenu.getModuleId());
        Assert.notNull(old, "模块不存在!");
        Assert.state(old.getModEnName().equals(modMenu.getModEnName())
                || !existsByModEnName(modMenu.getModEnName()), "英文名[" + modMenu.getModEnName() + "]已存在!");

        moduleMenuMapper.update(modMenu);

        writeLog(modMenu, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void updateLabel(String xmlFile, List<String[]> moduleLabelList) throws SystemException {
        Document document = XmlOptUtils.readXML(getResourcePath() + xmlFile);
        Element moduleEl = (Element) document.selectSingleNode("/module");
        Element fieldNamesEl = (Element) document.selectSingleNode("/module/fieldNames");
        moduleEl.remove(fieldNamesEl); // 删除fieldNames结点并重新添加
        fieldNamesEl = moduleEl.addElement("fieldNames");
        for (String[] moduleLabelArr : moduleLabelList) {
            Element funNameEl = fieldNamesEl.addElement("field");
            funNameEl.addAttribute("name", moduleLabelArr[0]);
            funNameEl.addText(moduleLabelArr[1]);
        }
        XmlOptUtils.writeXML(document, getResourcePath() + xmlFile);
    }

    @Transactional
    public void save(ModuleMenu modMenu) {
        Assert.state(!existsByModEnName(modMenu.getModEnName()), "英文名[" + modMenu.getModEnName() + "]已存在!");
        modMenu.setStatus(ENABLE);
        moduleMenuMapper.save(modMenu);

        writeLog(modMenu, LogLevel.FIRST, OptName.SAVE);
    }


    @Transactional
    public void saveModuleOptRefs(long modId, List<Long> optIds) {
        // 查询当前模块的所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(modId);
        if (modOptRefs.isEmpty()) {
            saveModuleOptRefSingle(modId, optIds);
        }
        else {
            /*
             * 模块操作关系：首先过滤出已被删除的数据,
             * 原模块操作列表删除重复的之后删除(并级联删除权限操作)
             * 新分配的操作列表删除重复的之后增加
             */
            List<ModOptRef> deleteModOptRefs = modOptRefs.stream()
                    .filter(modOptRef -> !optIds.contains(modOptRef.getOperate().getOperateId()))
                    .collect(Collectors.toList());

            deleteModOptRefs.forEach(modOptRef -> {
                modOptRefMapper.delete(modOptRef.getModOptId()); //进行模块操作关系删除
            });

            List<String> deleteModOptRefIds = deleteModOptRefs.stream()
                    .map(ModOptRef::getModOptId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            uumServiceFacade.deletePrivilegeByModOptId(deleteModOptRefIds.toArray(new String[deleteModOptRefIds.size()]));

            // 删除权限操作 modOptRefList 模块操作关系
            this.saveModuleOptRefSingle(modId, optIds); // 保存新分配的模块操作关系
        }
    }


    // --------------------------- 私有方法 -------------------------------


    private SimpleTree buildTree(ModuleMenuVo parentMenu, List<ModuleMenuVo> otherMenus, List<Long> checkedOperations, CheckedHolder checked, boolean isCheckbox) {
        Long parentId = parentMenu.getModuleId();
        List<? super SimpleTree> subMenuVos = Lists.newArrayList();
        Iterator<ModuleMenuVo> it = otherMenus.iterator();
        while (it.hasNext()) {
            ModuleMenuVo vo = it.next();
            if (vo.getParentId().equals(parentId)) {
                it.remove();
                List<ModuleMenuVo> others = Lists.newArrayList();
                others.addAll(otherMenus);
                subMenuVos.add(this.buildTree(vo, others, checkedOperations, new CheckedHolder(checked), isCheckbox));
            }
        }

        checked.checked = checked.checked || checkedOperations.contains(parentId);
        if (checked.parent != null && !checked.parent.checked) {
            checked.parent.checked = checked.checked;
        }

        SimpleTree tree = null;
        if (isCheckbox) {
            tree = new CheckBoxTree();
            CheckBoxTree boxTree = (CheckBoxTree) tree;
            boxTree.setNocheck(false);
            boxTree.setChecked(checked.checked);
            boxTree.setCheckBox(new CheckBoxTree.CheckBox(checked.checked ? 1 : 0));
        } else {
            tree = new SimpleTree();
        }
        tree.setId(parentId.toString());
        tree.setParentId(parentMenu.getParentId().toString());
        tree.setText(parentMenu.getModName());
        tree.setIconSkin(parentMenu.getModIconCls());
        tree.setIsLeaf(parentMenu.getIsLeaf());
        tree.setChildren(subMenuVos);

        return tree;
    }


    /**
     * 公用模块写日志
     *
     * @param moduleMenu 模块对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 10, 2012 11:04:50 PM
     */
    private void writeLog(ModuleMenu moduleMenu, LogLevel logLevel, OptName opt) {
        String optContent = moduleMenu.getModName() + SPLIT + moduleMenu.getModEnName() + SPLIT;

        sysOptLogService.saveLog(logLevel, opt, SYS_MOD_MGR, optContent, moduleMenu.getModuleId() + "");
    }

    /**
     * 对系统菜单进行排序
     *
     * @param modules 菜单列表
     * @param moduleMenu 菜单对象
     * @author rutine
     * @time Oct 11, 2012 8:58:30 PM
     */
    private void addModule(List<ModuleMenuVo> modules, ModuleMenuVo moduleMenu) {
        // 根据操作顺序进行排序
        int index = 0; // 元素索引
        boolean isAppend = true; // 是否追加元素
        for (ModuleMenuVo mod : modules) {
            int listOrder = mod.getModOrder(); // 已经有的操作顺序
            int currOrder = moduleMenu.getModOrder(); // 当前操作顺序
            if (listOrder > currOrder) {
                modules.add(index, moduleMenu); // 顺序小的插在前
                isAppend = false;
                break;
            }
            index++;
        }
        if (isAppend) {
            modules.add(moduleMenu); //加到操作list中
        }
    }

    /**
     * 只单独保存模块操作关系
     *
     * @param moduleId
     * @param operateIdList
     * @author rutine
     * @time Oct 14, 2012 9:14:00 AM
     */
    private void saveModuleOptRefSingle(long moduleId, List<Long> operateIdList) {
        StringBuilder operateIds = new StringBuilder();
        if (operateIdList != null) {
            for (Long operateId : operateIdList) {
                Operate operate = new Operate(operateId, null);
                ModuleMenu moduleMemu = new ModuleMenu(moduleId);
                ModOptRef modOptRef = new ModOptRef(null, operate, moduleMemu);
                modOptRefMapper.save(modOptRef);

                operateIds.append(operateIds.length() > 0 ? "," + operateId : operateId);
            }
        }

        String optContent = "模块分配操作" + SPLIT + operateIds.toString();
        sysOptLogService.saveLog(LogLevel.FIRST, OptName.SAVE, SYS_MODOPT_ASSIGN,
                optContent, moduleId + "");
    }

    private class CheckedHolder {
        CheckedHolder parent;
        boolean checked = false;

        public CheckedHolder(CheckedHolder parent) {
            this.parent = parent;
        }
    }
}
