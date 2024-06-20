package com.mycuckoo.web.vo.res.platform;

import com.mycuckoo.domain.platform.ModuleMenu;

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
    private List<Menu> first;
    private Map<String, List<Menu>> second;
    private Map<String, List<Menu>> third;
    private Map<Long, List<ResourceVo>> fourth;
    private String row;

    public HierarchyModuleVo(
            List<Menu> first,
            Map<String, List<Menu>> second,
            Map<String, List<Menu>> third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public List<Menu> getFirst() {
        return first;
    }

    public Map<String, List<Menu>> getSecond() {
        return second;
    }

    public Map<String, List<Menu>> getThird() {
        return third;
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


    public static class Menu {
        private String id;
        private Long moduleId; //模块ID
        private Long parentId; //上级模块
        private String code; //模块编码
        private String name; //模块名称
        private String iconCls; //模块图片样式
        private Integer level; //模块级别
        private Integer order; //模块顺序
        private String belongSys;//系统归属

        public Menu(ModuleMenu menu) {
            this.id = menu.getId();
            this.moduleId = menu.getModuleId();
            this.parentId = menu.getParentId();
            this.code = menu.getCode();
            this.name = menu.getName();
            this.iconCls = menu.getIconCls();
            this.level = menu.getLevel();
            this.order = menu.getOrder();
            this.belongSys = menu.getBelongSys();
        }

        public String getId() {
            return id;
        }

        public Long getModuleId() {
            return moduleId;
        }

        public Long getParentId() {
            return parentId;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getIconCls() {
            return iconCls;
        }

        public Integer getLevel() {
            return level;
        }

        public Integer getOrder() {
            return order;
        }

        public String getBelongSys() {
            return belongSys;
        }
    }
}
