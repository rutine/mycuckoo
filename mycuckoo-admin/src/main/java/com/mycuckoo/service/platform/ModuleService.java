package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.*;
import com.mycuckoo.repository.platform.ModOptRefMapper;
import com.mycuckoo.repository.platform.ModResRefMapper;
import com.mycuckoo.repository.platform.ModuleMenuMapper;
import com.mycuckoo.service.facade.UumServiceFacade;
import com.mycuckoo.util.TreeHelper;
import com.mycuckoo.util.XmlOptUtils;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.web.vo.res.platform.ModuleMenuVos;
import com.mycuckoo.web.vo.res.platform.ResourceVo;
import com.mycuckoo.web.vo.res.platform.ResourceVos;
import com.mycuckoo.web.vo.res.uum.AssignVo;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.*;
import static com.mycuckoo.core.operator.LogOperator.DUNHAO;
import static com.mycuckoo.util.CommonUtils.getResourcePath;

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
    private ModResRefMapper modResRefMapper;

    @Autowired
    private OperateService operateService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UumServiceFacade uumServiceFacade;


    public List<? extends SimpleTree> buildTree(List<ModuleMenu> menus, List<String> checkedOperations, boolean isCheckbox) {
        List<ModuleMenu> firsts = Lists.newArrayList();
        List<ModuleMenu> others = Lists.newArrayList();

        // 过滤分类一级、二级、三级菜单
        for (ModuleMenu menu : menus) {
            ModuleLevel level = ModuleLevel.of(menu.getLevel());
            switch (level) {
                case ONE:
                    firsts.add(menu);
                    break;
                case TWO:
                case THREE:
                case FOUR:
                    others.add(menu);
                    break;
            }
        }

        List<SimpleTree> trees = Lists.newArrayList();
        for (ModuleMenu menu : firsts) {
            Map<Long, List<ModuleMenu>> groupMap = others.stream().collect(Collectors.groupingBy(ModuleMenu::getParentId));
            trees.add(this.buildTree(menu, groupMap, checkedOperations, new CheckedHolder(null), isCheckbox));
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

        LogOperator.begin()
                .module(ModuleName.SYS_OPT_MGR)
                .operate(OptName.DELETE)
                .id(operateId)
                .title(null)
                .content("根据操作ID删除模块操作关系,级联删除权限")
                .level(LogLevel.THIRD)
                .emit();
    }

    @Transactional
    public void delete(Long moduleId) {
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
        boolean enable = ENABLE.equals(disableFlag);
        ModuleMenu updateEntity = new ModuleMenu(moduleId);
        if (!enable) {
            int count = moduleMenuMapper.countByParentId(moduleId);
            if (count > 0) { // 有下级菜单
                throw new ApplicationException("存在下级菜单");
            }

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

            updateEntity.setStatus(DISABLE);
        } else {
            updateEntity.setStatus(ENABLE);
        }
        moduleMenuMapper.update(updateEntity);

        writeLog(get(moduleId), LogLevel.SECOND, enable ?  OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public HierarchyModuleVo filterModule(List<ModuleMenu> list) {
        List<HierarchyModuleVo.Menu> firstList = Lists.newArrayList();
        List<HierarchyModuleVo.Menu> secondList = Lists.newArrayList();
        List<HierarchyModuleVo.Menu> thirdList = Lists.newArrayList();

        // 过滤分类一级、二级、三级菜单
        for (ModuleMenu menu : list) {
            ModuleLevel level = ModuleLevel.of(menu.getLevel());
            if (level == null) { continue; }

            switch (level) {
                case ONE:
                    firstList.add(new HierarchyModuleVo.Menu(menu));
                    break;
                case TWO:
                    secondList.add(new HierarchyModuleVo.Menu(menu));
                    break;
                case THREE:
                    thirdList.add(new HierarchyModuleVo.Menu(menu));
                    break;
            }
        }

        // 第一级
        firstList.sort(Comparator.comparingLong(HierarchyModuleVo.Menu::getOrder));

        // 第二级
        Map<String, List<HierarchyModuleVo.Menu>> secondMap = secondList.stream()
                .collect(Collectors.groupingBy(o -> o.getParentId().toString(),
                        Collectors.collectingAndThen(Collectors.toList(),
                                sub -> sub.stream().sorted(Comparator.comparing(HierarchyModuleVo.Menu::getOrder)).collect(Collectors.toList()))));
        // 第三级
        Map<String, List<HierarchyModuleVo.Menu>> thirdMap = thirdList.stream()
                .collect(Collectors.groupingBy(o -> o.getParentId().toString(),
                        Collectors.collectingAndThen(Collectors.toList(),
                                sub -> sub.stream().sorted(Comparator.comparing(HierarchyModuleVo.Menu::getOrder)).collect(Collectors.toList()))));

        return new HierarchyModuleVo(firstList, secondMap, thirdMap);
    }

    public List<? extends SimpleTree> findChildNodes(long modId) {
        List<? extends SimpleTree> all = this.findByPage(Querier.EMPTY);

        return TreeHelper.buildTree(all, modId + "");
    }

    public List<ModuleMenu> findAll() {
        List<ModuleMenu> list = moduleMenuMapper.findByPage(null, Querier.EMPTY).getContent();
        list.forEach(menu -> menu.setId(menu.getModuleId().toString()));

        return list;
    }

    public List<ResourceVo> findAllModOptRefs() {
        List<ModOptRef> refs = modOptRefMapper.findByPage(null, Querier.EMPTY).getContent();
        List<ResourceVo> result = refs.stream().map(ref -> {
            Operate operate = ref.getOperate();
            ResourceVo vo = new ResourceVo();
            vo.setId(ref.getModOptId());
            vo.setParentId(ref.getModuleId()); // 将第三级菜单设置为父
            vo.setCode(operate.getCode());
            vo.setName(operate.getName());
            vo.setIconCls(operate.getIconCls());
            vo.setOrder(operate.getOrder()); //顺序
            vo.setLevel(ModuleLevel.FOUR.value());
            vo.setIsLeaf(true);

            return vo;
        }).collect(Collectors.toList());

        return result;
    }

    public AssignVo<CheckboxTree, Long> findOperationTreeByModId(long moduleId) {
        List<Operate> allOperates = operateService.findAll(); //所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(moduleId); //已经分配的操作
        List<Long> optIds = modOptRefs.parallelStream()
                .map(ModOptRef::getOperate)
                .map(Operate::getOperateId)
                .collect(Collectors.toList());

        List<CheckboxTree> trees = Lists.newArrayList();
        allOperates.forEach(consumer -> {
            boolean checked = optIds.contains(consumer.getOperateId());

            CheckboxTree tree = new CheckboxTree();
            tree.setId(consumer.getOperateId().toString());
            tree.setParentId(ID_ROOT_VALUE);
            tree.setText(consumer.getName());
            tree.setIconSkin(consumer.getIconCls());
            tree.setIsLeaf(true);
            tree.setChildren(null);
            tree.setChecked(checked);
            tree.setCheckbox(new CheckboxTree.Checkbox(checked ? 1 : 0));
            trees.add(tree);
        });

        return new AssignVo<>(trees, optIds);
    }

    public List<ResourceVo> findAllModResRefs() {
        List<ModResRef> refs = modResRefMapper.findByPage(null, Querier.EMPTY).getContent();
        Map<Long, Operate> operateMap = operateService.findAll().stream()
                .collect(Collectors.toMap(Operate::getOperateId, Function.identity()));
        Map<Long, ModuleMenu> menuMap = this.findAll().stream()
                .collect(Collectors.toMap(ModuleMenu::getModuleId, Function.identity()));

        List<ResourceVo> result = refs.stream().map(ref -> {
            Operate operate = operateMap.get(ref.getResource().getOperateId());
            ModuleMenu menu = menuMap.get(ref.getResource().getModuleId());
            if (menu == null) {
                menu = menuMap.get(ref.getModuleId());
            }

            ResourceVo vo = new ResourceVo();
            vo.setId(ref.getModResId());
            vo.setParentId(ref.getModuleId()); // 将第三级菜单设置为父
            vo.setCode(String.format("%s:%s", menu.getCode(), operate == null ? ref.getResource().getIdentifier() : operate.getCode()));
            vo.setName(ref.getResource().getName());
            vo.setMethod(ref.getResource().getMethod());
            vo.setPath(ref.getResource().getPath());
            vo.setIconCls(operate == null ? "" : operate.getIconCls());
            vo.setGroup(ref.getGroup());
            vo.setOrder(ref.getOrder()); //顺序
            vo.setLevel(ModuleLevel.FOUR.value());
            vo.setIsLeaf(true);

            return vo;
        }).collect(Collectors.toList());


        return result;
    }

    public AssignVo<SimpleTree, String> findResourceTreeByModId(long moduleId) {
        List<ResourceVos.Tree> all = resourceService.findAll();
        List<ModResRef> modResRefs = modResRefMapper.findByModuleId(moduleId); //已经分配的操作
        Map<String, ModResRef> modResMap = modResRefs.stream()
                .collect(Collectors.toMap(o -> ID_LEAF + o.getResource().getResourceId(), Function.identity()));
        List<String> resIds = modResRefs.parallelStream()
                .map(ModResRef::getResource)
                .map(o -> ID_LEAF + o.getResourceId())
                .collect(Collectors.toList());
        all.forEach(tree -> {
            ModResRef ref = modResMap.get(tree.getId());
            if (ref != null) {
                tree.setGroup(ref.getGroup());
                tree.setOrder(ref.getOrder());
            }
        });

        List<? extends SimpleTree> trees = TreeHelper.buildTree(all, "0");
        this.removeIfAbsenceLeaf(trees);
        Iterator<? extends SimpleTree> it = trees.iterator();
        while(it.hasNext()) {
            SimpleTree one = it.next();
            if (one.getChildren() == null || one.getChildren().isEmpty()) {
                it.remove();
            }
        }

        return new AssignVo(trees, resIds);
    }
    //如果没有资源子节点, 递归移除上级
    private boolean removeIfAbsenceLeaf(List<? extends SimpleTree> trees) {
        if (trees == null || trees.isEmpty()) {
            return false;
        }

        for (SimpleTree tree : trees) {
            boolean result =  this.removeIfAbsenceLeaf(tree.getChildren());
            if (result) {
                return true;
            }
            tree.setChildren(null);

            if (tree.getId().startsWith(ID_LEAF)) {
                return true;
            }
        }

        return false;
    }

    public List<ModuleMenuVos.Tree> findByPage(Querier querier) {
        Page<ModuleMenu> page = moduleMenuMapper.findByPage(querier.getQ(), querier);
        List<ModuleMenuVos.Tree> vos = Lists.newArrayList();
        for (ModuleMenu entity : page.getContent()) {
            ModuleMenuVos.Tree vo = new ModuleMenuVos.Tree();
            BeanUtils.copyProperties(entity, vo);
            vo.setId(entity.getModuleId() + "");
            vo.setParentId(entity.getParentId() + "");
            vo.setIsParent(!vo.getLevel().equals(ModuleLevel.THREE.value()));
            vo.setIsLeaf(vo.getLevel().equals(ModuleLevel.THREE.value()));
            vos.add(vo);
        }

        return vos;
    }

    public ModuleMenu get(Long moduleId) {
        ModuleMenu entity = moduleMenuMapper.get(moduleId);
        if (entity == null) {
            return null;
        }

        entity.setId(entity.getModuleId() + "");

        return entity;
    }

    public boolean existsByCode(String code) {
        int totalProperty = moduleMenuMapper.countByCode(code);

        logger.debug("find module total is {}", totalProperty);

        if (totalProperty > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public void update(ModuleMenu entity) {
        ModuleMenu old = get(entity.getModuleId());
        Assert.notNull(old, "模块不存在!");
        Assert.state(old.getCode().equals(entity.getCode())
                || !existsByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        if (entity.getParentId() != null) {
            ModuleMenu parent = get(entity.getParentId());
            Assert.notNull(parent, "请选择父级模块");

            if (old.getLevel().equals(parent.getLevel() + 1)) {
                entity.setLevel(null);
            } else {
                entity.setLevel(parent.getLevel() + 1);
            }
        }
        entity.setUpdator(SessionUtil.getUserCode());
        entity.setUpdateTime(LocalDateTime.now());
        moduleMenuMapper.update(entity);

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
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
    public void save(ModuleMenu entity) {
        ModuleMenu parent = get(entity.getParentId());
        Assert.notNull(parent, "请选择父级模块");
        Assert.state(!existsByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");
        entity.setStatus(ENABLE);
        entity.setLevel(parent.getLevel() + 1);
        entity.setUpdator(SessionUtil.getUserCode());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator(SessionUtil.getUserCode());
        entity.setCreateTime(LocalDateTime.now());
        moduleMenuMapper.save(entity);

        writeLog(entity, LogLevel.FIRST, OptName.SAVE);
    }


    @Transactional
    public void saveModuleOptRefs(long modId, List<Long> optIds) {
        Assert.state(moduleMenuMapper.exists(modId), "模块不存在!");

        // 查询当前模块的所有操作
        List<ModOptRef> modOptRefs = modOptRefMapper.findByModuleId(modId);
        if (modOptRefs.isEmpty()) {
            doSaveModuleOptRefs(modId, optIds);
        }
        else {
            //模块操作关系：首先过滤出已被删除的数据,
            List<ModOptRef> deleteModOptRefs = modOptRefs.stream()
                    .filter(modOptRef -> !optIds.contains(modOptRef.getOperate().getOperateId()))
                    .collect(Collectors.toList());
            deleteModOptRefs.forEach(modOptRef -> {
                modOptRefMapper.delete(modOptRef.getModOptId()); //进行模块操作关系删除
            });

            //级联删除权限操作
            List<String> deleteModOptRefIds = deleteModOptRefs.stream()
                    .map(ModOptRef::getModOptId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            uumServiceFacade.deletePrivilegeByModOptId(deleteModOptRefIds.toArray(new String[deleteModOptRefIds.size()]));

            //去掉已存在的操作id
            optIds.removeAll(modOptRefs.stream().map(o -> o.getOperate().getOperateId()).collect(Collectors.toList()));
            // 保存新分配的模块操作关系
            this.doSaveModuleOptRefs(modId, optIds);
        }
    }

    @Transactional
    public void saveModuleResRefs(long modId, List<ModResRef> modResRefs) {
        Assert.state(moduleMenuMapper.exists(modId), "模块不存在!");

        // 查询当前模块的所有资源
        List<ModResRef> oldModResRefs = modResRefMapper.findByModuleId(modId);
        if (oldModResRefs.isEmpty()) {
            doSaveModuleResRefs(modId, modResRefs, Collections.EMPTY_LIST);
        }
        else {
            //模块资源关系：首先过滤出已被删除的数据
            List<Long> resources = modResRefs.stream().map(o -> o.getResourceId()).collect(Collectors.toList());
            List<ModResRef> deleteModResRefs = oldModResRefs.stream()
                    .filter(modOptRef -> !resources.contains(modOptRef.getResource().getResourceId()))
                    .collect(Collectors.toList());
            deleteModResRefs.forEach(modOptRef -> {
                modResRefMapper.delete(modOptRef.getModResId());
            });

            //级联删除权限操作
            List<String> deleteModResRefIds = deleteModResRefs.stream()
                    .map(ModResRef::getModResId)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            uumServiceFacade.deletePrivilegeByModResId(deleteModResRefIds.toArray(new String[deleteModResRefIds.size()]));

            // 保存新分配的资源
            this.doSaveModuleResRefs(modId, modResRefs, oldModResRefs);
        }
    }


    // --------------------------- 私有方法 -------------------------------


    private SimpleTree buildTree(ModuleMenu menu, Map<Long, List<ModuleMenu>> groupMap,
                                 List<String> checkedOperations, CheckedHolder checked, boolean isCheckbox) {
        String id = menu.getId();
        List<? super SimpleTree> subMenuVos = Lists.newArrayList();
        if (id != null && !id.startsWith(ID_LEAF) && groupMap.containsKey(menu.getModuleId())) {
            subMenuVos = groupMap.get(menu.getModuleId()).stream()
                    .map(tree -> buildTree(tree, groupMap, checkedOperations, new CheckedHolder(checked), isCheckbox))
                    .collect(Collectors.toList());
        }

        checked.checked = checked.checked || checkedOperations.contains(id);
        if (checked.parent != null && !checked.parent.checked) {
            checked.parent.checked = checked.checked;
        }

        SimpleTree tree = null;
        if (isCheckbox) {
            tree = new CheckboxTree();
            CheckboxTree boxTree = (CheckboxTree) tree;
            boxTree.setNocheck(false);
            boxTree.setChecked(checked.checked);
            boxTree.setCheckbox(new CheckboxTree.Checkbox(checked.checked ? 1 : 0));
        } else {
            tree = new SimpleTree();
        }
        tree.setId(id);
        tree.setParentId(menu.getParentId().toString());
        tree.setText(menu.getName());
        tree.setIconSkin(menu.getIconCls());
        tree.setIsLeaf(id != null && id.startsWith(ID_LEAF));
        tree.setChildren(subMenuVos);

        return tree;
    }


    /**
     * 公用模块写日志
     *
     * @param entity 模块对象
     * @param level
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 10, 2012 11:04:50 PM
     */
    private void writeLog(ModuleMenu entity, LogLevel level, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_MOD_MGR)
                .operate(opt)
                .id(entity.getModuleId())
                .title(null)
                .content("模块名称：%s, 编码: %s", entity.getName(), entity.getCode())
                .level(level)
                .emit();
    }

    /**
     * 只单独保存模块操作关系
     *
     * @param moduleId
     * @param operateIdList
     * @author rutine
     * @time Oct 14, 2012 9:14:00 AM
     */
    private void doSaveModuleOptRefs(long moduleId, List<Long> operateIdList) {
        if (operateIdList == null || operateIdList.isEmpty()) {
            return;
        }

        for (Long operateId : operateIdList) {
            Operate operate = new Operate(operateId, null);
            ModOptRef modOptRef = new ModOptRef(null, moduleId, operate);
            modOptRefMapper.save(modOptRef);
        }

        LogOperator.begin()
                .module(ModuleName.SYS_MOD_MGR)
                .operate(OptName.ASSIGN)
                .id(moduleId)
                .title(null)
                .content("模块分配操作: %s",
                        operateIdList.stream().map(String::valueOf).collect(Collectors.joining(DUNHAO)))
                .level(LogLevel.FIRST)
                .emit();
    }
    /**
     * 只单独保存模块资源关系
     *
     * @param moduleId
     * @param modResRefs
     * @author rutine
     * @time May 12, 2024 11:58:13 AM
     */
    private void doSaveModuleResRefs(long moduleId, List<ModResRef> modResRefs, List<ModResRef> oldResRef) {
        if (modResRefs == null || modResRefs.isEmpty()) {
            return;
        }

        Map<Long, ModResRef> oldRefMap = oldResRef.stream()
                .collect(Collectors.toMap(o -> o.getResource().getResourceId(), Function.identity()));
        for (ModResRef modResRef : modResRefs) {
            Resource resource = new Resource(modResRef.getResourceId(), null);
            ModResRef newModResRef = new ModResRef(null, moduleId, resource, modResRef.getGroup(), modResRef.getOrder());

            if (oldRefMap.containsKey(modResRef.getResourceId())) {
                newModResRef.setModResId(oldRefMap.get(modResRef.getResourceId()).getModResId());
                modResRefMapper.update(newModResRef);
            } else {
                modResRefMapper.save(newModResRef);
            }
        }

        LogOperator.begin()
                .module(ModuleName.SYS_MOD_MGR)
                .operate(OptName.ASSIGN)
                .id(moduleId)
                .title(null)
                .content("模块分配资源: %s",
                        modResRefs.stream().map(o -> String.valueOf(o.getResourceId())).collect(Collectors.joining(DUNHAO)))
                .level(LogLevel.FIRST)
                .emit();
    }

    private class CheckedHolder {
        CheckedHolder parent;
        boolean checked = false;

        public CheckedHolder(CheckedHolder parent) {
            this.parent = parent;
        }
    }
}
