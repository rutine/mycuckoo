package com.mycuckoo.persistence.impl.uum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.persistence.iface.uum.UumUserRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 用户持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:24:19 PM
 * @version 2.0.0
 */
@Repository
public class UumUserRepositoryImpl extends AbstractRepository<UumUser, Long>
		implements UumUserRepository {

	@Override
	public List<UumUser> findUsersByOrgId(Long user_belongto_org) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByOrgID", user_belongto_org);
	}

	@Override
	public List<UumUser> findUsersByCodeAndName(String userCode, String userName) {
		if (StringUtils.isNotBlank(userCode)) {
			userCode = "%" + userCode + "%";
		}
		if (StringUtils.isNotBlank(userName)) {
			userName = "%" + userName + "%";
		}
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("userCode", userCode);
		map.put("userName", userName);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByCon", map);
	}

	@Override
	public Page<UumUser> findUsersByCon(String treeId, Long[] orgIds,
			String userCode, String userName, Pageable page) {
		
		int id = 0;
		int flag = 0;
		if (StringUtils.isNotBlank(treeId)) {
			if (treeId.length() >= 3) {
				id = Integer.parseInt(treeId.substring(0, treeId.indexOf("_")));
				flag = Integer.parseInt(treeId.substring(treeId.indexOf("_") + 1));
			}
		}

		if (StringUtils.isNotBlank(userCode)) {
			userCode = "%" + userCode + "%";
		}
		if (StringUtils.isNotBlank(userName)) {
			userName = "%" + userName + "%";
		}

		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("userCode", userCode);
		map.put("userName", userName);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<UumUser> list;
		long count = 0;
		if (flag == 0) { // 根据用户代码和用户名称模糊、分页查询用户记录
			list = getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByCon", map);
			count = getSqlSession().selectOne(obtainMapperNamespace() + ".countUsersByCon", map);
		} else if (flag == 1) { // 分页查询属于机构ids的用户记录
			map.put("orgIds", orgIds);
			list = getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByCon", map);
			count = getSqlSession().selectOne(obtainMapperNamespace() + ".countUsersByCon", map);
		} else { // 分页查询属于某个角色id的用户记录
			map.put("orgRoleId", id);
			list = getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByCon", map);
			count = getSqlSession().selectOne(obtainMapperNamespace() + ".countUsersByCon", map);
		}

		return new PageImpl<UumUser>(list, page, count);
	}

	@Override
	public boolean existsUserByUserCode(String userCode) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".existsUserByUserCode", userCode);
	}

	@Override
	public UumUser getUserByUserCodeAndPwd(String userCode, String password) {
		UumUser user = new UumUser();
		user.setUserCode(userCode);
		user.setUserPassword(password);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".getUserByUserCodeAndPwd", user);
	}

	@Override
	public void updateUserBelongOrgIdByUserId(long organId, long userId) {
		UumUser user = new UumUser();
		user.setUserId(userId);
		user.setBelongOrganId(organId);

		getSqlSession().update(obtainMapperNamespace() + ".updateUserByProps", user);
	}

	@Override
	public void updateUserPwdByUserId(String defaultPassword, long userId) {
		UumUser user = new UumUser();
		user.setUserId(userId);
		user.setUserPassword(defaultPassword);

		getSqlSession().update(obtainMapperNamespace() + ".updateUserByProps", user);
	}

	@Override
	public void updateUserPhotoUrlByUserId(String photoUrl, long userId) {
		UumUser user = new UumUser();
		user.setUserId(userId);
		user.setUserPhotoUrl(photoUrl);

		getSqlSession().update(obtainMapperNamespace() + ".updateUserByProps", user);
	}

	@Override
	public List<UumUser> findUsersByUserName(String userName) {
		return this.findUsersByCodeAndName(null, userName);
	}

	@Override
	public List findUsersByUserNamePy(String userNamePy, long userId) {
		UumUser user = new UumUser();
		user.setUserId(userId);
		user.setUserNamePy(userNamePy + "%");

		return getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByUserNamePy", user);
	}

	@Override
	public List findUsersByUserIds(Long[] userIds) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findUsersByUserIds", userIds);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumUser entity) {
		return entity.getUserId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumUserMapper";
	}
}
