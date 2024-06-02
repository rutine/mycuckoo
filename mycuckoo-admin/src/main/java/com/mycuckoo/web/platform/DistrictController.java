package com.mycuckoo.web.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SimpleTree;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.District;
import com.mycuckoo.service.platform.DistrictService;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.platform.DistrictVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.mycuckoo.constant.AdminConst.ROOT_ID_VALUE;

/**
 * 功能说明: 地区Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 18, 2014 10:42:49 AM
 */
@RestController
@RequestMapping("/platform/district/mgr")
public class DistrictController {
    private static Logger logger = LoggerFactory.getLogger(DistrictController.class);

    @Autowired
    private DistrictService districtService;


    /**
     * 功能说明 : 列表展示页面
     *
     * @param treeId        查找指定节点下的地区
     * @param querier       查询参数
     * @return
     * @author rutine
     * @time Jul 2, 2013 11:12:40 AM
     */
    @GetMapping
    public AjaxResponse<Page<DistrictVo>> list(@RequestParam(value = "treeId", defaultValue = "-1") long treeId, Querier querier) {
        Page<DistrictVo> page = districtService.findByPage(treeId, querier);

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建新地区
     *
     * @param district
     * @return
     * @author rutine
     * @time Jul 2, 2013 11:18:03 AM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody District district) {
        district.setUpdateDate(new Date());
        district.setUpdater(SessionUtil.getUserCode());
        district.setCreateDate(new Date());
        district.setCreator(SessionUtil.getUserCode());
        district.setParentId(district.getParentId());
        districtService.save(district);

        return AjaxResponse.success("保存地区成功");
    }

    /**
     * 功能说明 : 修改地区
     *
     * @param district 地区对象
     * @return
     * @author rutine
     * @time Jul 2, 2013 11:37:39 AM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody District district) {
        district.setUpdateDate(new Date());
        district.setUpdater(SessionUtil.getUserCode());
        districtService.update(district);

        return AjaxResponse.success("修改地区成功");
    }


    @GetMapping("/{id}")
    public AjaxResponse<DistrictVo> get(@PathVariable long id) {
        DistrictVo district = districtService.get(id);
        DistrictVo parentDistrict = districtService.get(district.getParentId());
        district.setParentId(parentDistrict.getDistrictId());
        district.setParentName(parentDistrict.getName());

        return AjaxResponse.create(district);
    }


    /**
     * 功能说明 : 停用/启用模块
     *
     * @param id
     * @param disEnableFlag 停用/启用标志
     * @return
     * @author rutine
     * @time Jul 2, 2013 11:33:24 AM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        districtService.disEnable(id, disEnableFlag);

        return AjaxResponse.success("操作成功");
    }

    /**
     * 功能说明 : 查找节点的下级节点
     *
     * @param id          地区id
     * @return
     * @author rutine
     * @time Jul 2, 2013 11:27:54 AM
     */
    @GetMapping("/{id}/child/nodes")
    public AjaxResponse<List<? extends SimpleTree>> getChildNodes(@PathVariable long id) {
        List<? extends SimpleTree> trees = Lists.newArrayList();
        if (id == -1L) {
            List<SimpleTree> tmpTrees = Lists.newArrayList();
            SimpleTree root = new SimpleTree();
            root.setId(ROOT_ID_VALUE);
            root.setText("中国");
            root.setChildren(districtService.findChildNodes(0));
            tmpTrees.add(root);

            trees = tmpTrees;
        } else {
            trees = districtService.findChildNodes(id);
        }

        return AjaxResponse.create(trees);
    }

}
