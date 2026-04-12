package com.mycuckoo.web.vo.res.uum;

import com.mycuckoo.domain.uum.User;

/**
 * 功能说明: 用户vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 6:16:58 PM
 */
public class UserVo extends User {
    private String deptName;
    private String roleName;
    private String photoFileId;
    private String photoUrl;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPhotoFileId() {
        return photoFileId;
    }

    public void setPhotoFileId(String photoFileId) {
        this.photoFileId = photoFileId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
