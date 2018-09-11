package com.mycuckoo.service.uum;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.OrgRoleRefMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.ROLE_ASSIGN;

/**
 * 功能说明: 机构角色业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 11:29:55 AM
 */
@Service
@Transactional(readOnly = true)
public class OrganRoleService {
    static Logger logger = LoggerFactory.getLogger(OrganRoleService.class);

    @Autowired
    private OrgRoleRefMapper orgRoleRefMapper;
    @Autowired
    private UserOrgRoleService userOrgRoleService;
    @Autowired
    private SystemOptLogService sysOptLogService;


    public int countByOrgId(long orgId) {
        return orgRoleRefMapper.countByOrgId(orgId);
    }

    @Transactional
    public int delete(long orgId, List<Long> roleIds) {
        StringBuilder optContent = new StringBuilder("删除机构下的角色ID：");
        for (long roleId : roleIds) {
            OrgRoleRef orgRoleRef = this.getByOrgRoleId(orgId, roleId);
            int roleUserCount = userOrgRoleService.countByOrgRoleId(orgRoleRef.getOrgRoleId());
            if (roleUserCount == 0) {// 角色下无用户
                orgRoleRefMapper.delete(orgRoleRef.getOrgRoleId()); //删除所有机构角色
            } else {// 角色下有用户
                throw new ApplicationException(
                        String.format("角色[%s]下有用户, 删除失败!", orgRoleRef.getRole().getRoleName()));
            }
            optContent.append(roleId).append(SPLIT);
        }

        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, ROLE_ASSIGN,
                optContent.toString(), orgId + "");

        return roleIds.size();
    }

    @Transactional
    public void deleteByRoleId(long roleId) {
        /**
         * 角色停用时检查其下是否有用户 1. 角色下有相应的用户, 不能被停用
         */
        int userTotal = userOrgRoleService.countByRoleId(roleId); // 是否已经分配用户
        if (userTotal > 0) {
            throw new ApplicationException("已经分配用户, 不能被停用");
        }

        orgRoleRefMapper.deleteByRoleId(roleId);
    }

    public OrgRoleRef getByOrgRoleId(long orgId, long roleId) {
        return orgRoleRefMapper.getByOrgRoleId(orgId, roleId);
    }

    public OrgRoleRef get(long orgRoleId) {
        return orgRoleRefMapper.get(orgRoleId);
    }

    public List<OrgRoleRef> findByOrgRoleIds(Long[] orgRoleRefIds) {
        return orgRoleRefMapper.findByOrgRoleIds(orgRoleRefIds);
    }

    public List<Long> findOrgRoleRefIdsByRoleId(Long roleId) {
        return orgRoleRefMapper.findOrgRoleIdsByRoleId(roleId);
    }

    public List<OrgRoleRef> findRolesByOrgId(long orgId) {
        return orgRoleRefMapper.findRolesByOrgId(orgId);
    }

    public Page<Role> findSelectedRolesByOrgId(long orgId, String roleName, Pageable page) {
        Page<OrgRoleRef> pageTemp = orgRoleRefMapper.findRolesByPage(orgId, roleName, page);
        List<Role> roleList = new ArrayList<>();
        for (OrgRoleRef orgRoleRef : pageTemp.getContent()) {
            roleList.add(orgRoleRef.getRole());
        }

        return new PageImpl<>(roleList, page, pageTemp.getTotalElements());
    }

    public Page<Role> findUnselectedRolesByOrgId(long orgId, Pageable page) {
        Page<OrgRoleRef> pageResult = orgRoleRefMapper.findUnselectedRolesByOrgId(orgId, page);
        List<Role> newRoleList = new ArrayList<>();
        for (OrgRoleRef orgRoleRef : pageResult) {
            newRoleList.add(orgRoleRef.getRole());
        }

        return new PageImpl<>(newRoleList, page, pageResult.getTotalElements());
    }

    @Transactional
    public void save(long orgId, List<Long> roleIds) {
        if (!roleIds.isEmpty()) {
            StringBuilder optContent = new StringBuilder();
            Organ organ = new Organ(orgId, null);
            for (Long roleId : roleIds) {
                Role role = new Role(roleId, null);
                OrgRoleRef orgRoleRef = new OrgRoleRef(null, role, organ);
                orgRoleRefMapper.save(orgRoleRef);

                optContent.append("机构id:" + orgId + SPLIT + "角色id:" + roleId + SPLIT);
            }

            sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, ROLE_ASSIGN,
                    optContent.toString(), "");
        }
    }


    // --------------------------- 私有方法 -------------------------------

}
