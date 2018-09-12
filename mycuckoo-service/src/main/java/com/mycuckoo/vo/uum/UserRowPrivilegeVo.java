package com.mycuckoo.vo.uum;

import java.util.List;

/**
 * 功能说明: 用户行权限
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-09-09 09:43
 */
public class UserRowPrivilegeVo {
    private List<RowVo> row;
    private String rowPrivilege;

    public List<RowVo> getRow() {
        return row;
    }

    public void setRow(List<RowVo> row) {
        this.row = row;
    }

    public String getRowPrivilege() {
        return rowPrivilege;
    }

    public void setRowPrivilege(String rowPrivilege) {
        this.rowPrivilege = rowPrivilege;
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
