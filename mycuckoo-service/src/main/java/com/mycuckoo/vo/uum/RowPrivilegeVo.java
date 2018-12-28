package com.mycuckoo.vo.uum;

import java.util.List;

/**
 * 功能说明: 用户行权限
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-09-09 09:43
 */
public class RowPrivilegeVo {
    private String privilegeScope;
    private List<RowVo> privileges;

    public RowPrivilegeVo() {}

    public RowPrivilegeVo(String privilegeScope, List<RowVo> privileges) {
        this.privilegeScope = privilegeScope;
        this.privileges = privileges;
    }

    public String getPrivilegeScope() {
        return privilegeScope;
    }

    public void setPrivilegeScope(String privilegeScope) {
        this.privilegeScope = privilegeScope;
    }

    public List<RowVo> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<RowVo> privileges) {
        this.privileges = privileges;
    }


    public static class RowVo {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
