package com.mycuckoo.service.uum;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.uum.*;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.uum.UserOrgRoleRefMapper;
import com.mycuckoo.vo.uum.UserOrgRoleRefVo;
import com.mycuckoo.vo.uum.UserRoleVo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.ServiceConst.N;
import static com.mycuckoo.constant.ServiceConst.Y;
import static com.mycuckoo.operator.LogOperator.DUNHAO;

/**
 * 功能说明: 角色用户业务类
 *
 * @author rutine
 * @version 4.0.0
 * @time Sep 25, 2014 11:33:25 AM
 */
@Service
@Transactional(readOnly = true)
public class UserOrgRoleService {
    static Logger logger = LoggerFactory.getLogger(UserOrgRoleService.class);

    @Autowired
    private UserOrgRoleRefMapper userOrgRoleRefMapper;
    @Autowired
    private OrganRoleService organRoleService;
    @Autowired
    private UserService userService;


    public int countByOrgRoleId(long orgRoleId) {
        return userOrgRoleRefMapper.countByOrgRoleId(orgRoleId);
    }

    public int countByRoleId(long roleId) {
        return userOrgRoleRefMapper.countByRoleId(roleId);
    }

    public int deleteByUserId(long userId) {
        return userOrgRoleRefMapper.deleteByUserId(userId);
    }

    public UserOrgRoleRefVo findByUserIdAndOrgRoleId(long userId, long orgRoleId) {
        UserOrgRoleRef entity = userOrgRoleRefMapper.findByUserIdAndOrgRoleId(userId, orgRoleId);
        UserOrgRoleRefVo vo = new UserOrgRoleRefVo();
        BeanUtils.copyProperties(entity, vo);

        return vo;
    }

    public List<UserRoleVo> findByUserId(long userId) {
        List<UserOrgRoleRef> list = userOrgRoleRefMapper.findByUserId(userId);

        List<UserRoleVo> vos = Lists.newArrayList();
        for (UserOrgRoleRef userOrgRoleRef : list) {
            OrgRoleRef orgRoleRef = userOrgRoleRef.getOrgRoleRef(); //机构角色关系
            Organ organ = orgRoleRef.getOrgan();
            Role role = orgRoleRef.getRole();
            User user = userOrgRoleRef.getUser();

            UserRoleVo vo = new UserRoleVo();
            vo.setOrgRoleId(orgRoleRef.getOrgRoleId());
            vo.setOrgId(organ.getOrgId());
            vo.setOrgName(organ.getSimpleName());
            vo.setRoleId(role.getRoleId());
            vo.setRoleName(role.getName());
            vo.setUserId(user.getUserId());
            vo.setUserCode(user.getCode());
            vo.setUserName(user.getName());
            vo.setUserPhotoUrl(user.getPhotoUrl());
            vo.setIsDefault(userOrgRoleRef.getIsDefault());
            vos.add(vo);
        }

        return vos;
    }

    public List<UserOrgRoleRefVo> findByOrgRoleId(Long orgId, Long roleId) {
        List<UserOrgRoleRef> list = userOrgRoleRefMapper.findByOrgRoleId(orgId, roleId);
        List<UserOrgRoleRefVo> vos = Lists.newArrayList();
        list.forEach(entity -> {
            UserOrgRoleRefVo vo = new UserOrgRoleRefVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        });

        return vos;
    }


    @Transactional
    public int save(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) {
        // 选择删除用户所有的角色
        this.deleteByUserId(userId);

        // 为用户添加角色
        return this.doSave(userId, orgRoleIds, defaultOrgRoleId);
    }

    public int doSave(long userId, List<Long> orgRoleIds, long defaultOrgRoleId) {
        for (Long orgRoleId : orgRoleIds) {
            User user = new User(userId, null);
            OrgRoleRef orgRoleRef = new OrgRoleRef(orgRoleId);

            UserOrgRoleRef userOrgRoleRef = new UserOrgRoleRef(null, orgRoleRef, user);
            if (defaultOrgRoleId == orgRoleId) {
                userOrgRoleRef.setIsDefault(Y);
                OrgRoleRef oldOrgRoleRef = organRoleService.get(orgRoleId);
                //修改用户的默认机构
                userService.updateBelongOrgIdForAssignRole(oldOrgRoleRef.getOrgan().getOrgId(), userId);
            } else {
                userOrgRoleRef.setIsDefault(N);
            }
            userOrgRoleRefMapper.save(userOrgRoleRef);
        }

        LogOperator.begin()
                .module(ModuleName.USER_ROLE_MGR)
                .operate(OptName.SAVE)
                .id(userId)
                .title(null)
                .content("角色IDs：%s",
                        orgRoleIds.stream().map(String::valueOf).collect(Collectors.joining(DUNHAO)))
                .level(LogLevel.FIRST)
                .emit();

        return 1;
    }

    public void save(UserOrgRoleRef roleUserRef) {
        userOrgRoleRefMapper.save(roleUserRef);
    }

}
