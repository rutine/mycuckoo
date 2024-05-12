package com.mycuckoo.vo;

import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.vo.platform.ResourceVo;

import java.util.List;
import java.util.Map;

/**
 * 功能说明: 四级层级菜单模块
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 2, 2017 6:17:29 PM
 */
public class HierarchyModuleVo {
    private List<ModuleMenuVo> first;
    private Map<String, List<ModuleMenuVo>> second;
    private Map<String, List<ModuleMenuVo>> third;
    private Map<Long, List<ResourceVo>> fourth;
    private String row;

    public HierarchyModuleVo(
            List<ModuleMenuVo> first,
            Map<String, List<ModuleMenuVo>> second,
            Map<String, List<ModuleMenuVo>> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public List<ModuleMenuVo> getFirst() {
        return first;
    }

    public void setFirst(List<ModuleMenuVo> first) {
        this.first = first;
    }

    public Map<String, List<ModuleMenuVo>> getSecond() {
        return second;
    }

    public void setSecond(Map<String, List<ModuleMenuVo>> second) {
        this.second = second;
    }

    public Map<String, List<ModuleMenuVo>> getThird() {
        return third;
    }

    public void setThird(Map<String, List<ModuleMenuVo>> third) {
        this.third = third;
    }

    public Map<Long, List<ResourceVo>> getFourth() {
        return fourth;
    }

    public void setFourth(Map<Long, List<ResourceVo>> fourth) {
        this.fourth = fourth;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }
}
