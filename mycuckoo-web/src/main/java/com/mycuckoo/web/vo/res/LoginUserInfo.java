package com.mycuckoo.web.vo.res;

import com.mycuckoo.vo.HierarchyModuleVo;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-10-06 15:48
 */
public class LoginUserInfo {
	private HierarchyModuleVo menu;
	private UserInfo user;

	public HierarchyModuleVo getMenu() {
		return menu;
	}

	public void setMenu(HierarchyModuleVo menu) {
		this.menu = menu;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public static class UserInfo {
		private String userCode;
		private String userName;
		private String userPhotoUrl;

		public String getUserCode() {
			return userCode;
		}

		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserPhotoUrl() {
			return userPhotoUrl;
		}

		public void setUserPhotoUrl(String userPhotoUrl) {
			this.userPhotoUrl = userPhotoUrl;
		}
	}
}
