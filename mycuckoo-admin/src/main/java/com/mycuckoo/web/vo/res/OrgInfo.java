package com.mycuckoo.web.vo.res;

import com.mycuckoo.domain.uum.UserExtend;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/8/18 19:04
 */
public class OrgInfo {
    private String token;
    private List<UserExtend> orgs;

    public OrgInfo(String token, List<UserExtend> orgs) {
        this.token = token;
        this.orgs = orgs;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<UserExtend> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<UserExtend> orgs) {
        this.orgs = orgs;
    }
}
