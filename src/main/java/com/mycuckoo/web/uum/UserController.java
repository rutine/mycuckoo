package com.mycuckoo.web.uum;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;
import static com.mycuckoo.common.constant.Common.USER;
import static com.mycuckoo.common.constant.Common.USER_DEFAULT_PWD;
import static com.mycuckoo.common.constant.Common.USER_PHOTO_URL;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.commonused.PlatformCommonService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户管理Controller
 * 
 * @author rutine
 * @time Oct 19, 2014 10:04:42 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/uum/userMgr")
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Value("${mycuckoo.imageUrl}")
	private String imagePath;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private PlatformCommonService platformCommonService;

	@RequestMapping(value = "/index")
	public String usrMgr() {
		logger.info("---------------- 请求用户菜单管理界面 -----------------");

		return "business/uum/user/userMgr";
	}

	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "treeId", defaultValue = "-1") String treeId,
			@RequestParam(value = "userCode", defaultValue = "") String userCode,
			@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		Page<UumUser> page = userService.findUsersByCon(treeId, userName,
				userCode, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "userCode=" + userCode + "&userName=" + userName);

		return "business/uum/user/userMgrList";
	}

	/**
	 * 功能说明 : 用户分配特殊操作权限
	 * 
	 * @param id 用户id
	 * @param model
	 * @return
	 * @author rutine
	 * @time Oct 13, 2013 1:05:30 PM
	 */
	@RequestMapping(value = "/queryUserPrivilegeList")
	public String queryUserPrivilegeList(@RequestParam long id, Model model) {
		Map moduleMemuMap = privilegeService.findSelectAUnselectModOptByOwnIdAOwnType(id, OWNER_TYPE_USR);

		model.addAttribute("assignedModList", JsonUtils.toJson(moduleMemuMap.get("assignedModOpts")));
		model.addAttribute("unassignedModList", JsonUtils.toJson(moduleMemuMap.get("unassignedModOpts")));
		model.addAttribute("privilegeScope", moduleMemuMap.get(PRIVILEGE_SCOPE));

		return "business/uum/user/userMgrAssignModOpt";
	}

	/**
	 * 功能说明 : 查询用户的所有角色
	 * 
	 * @param id 用户id
	 * @param model
	 * @return
	 * @author rutine
	 * @time Oct 13, 2013 4:53:00 PM
	 */
	@RequestMapping(value = "/queryUserRolePrivilegeList")
	public String queryUserRolePrivilegeList(@RequestParam long id, Model model) {
		List<UumRoleUserRef> roleUserRefList = roleUserService.findRoleUserRefsByUserId(id);
		List<? extends TreeVo> orgRoleList = this.getChildNodes("0_1", "Y");

		model.addAttribute("orgRoleList", JsonUtils.toJson(orgRoleList));
		model.addAttribute("roleUserRefList", roleUserRefList);

		return "business/uum/user/userMgrAssignRole";
	}

	/**
	 * 功能说明 : 保存用户
	 * 
	 * @param uumUser
	 * @param request
	 * @param session
	 * @return
	 * @author rutine
	 * @time Oct 6, 2013 8:26:57 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateForm(
			UumUser uumUser, 
			HttpServletRequest request,
			HttpSession session) {
		
		logger.debug(JsonUtils.toJson(uumUser));

		AjaxResult ajaxResult = new AjaxResult(true, "保存用户成功");
		try {
			uumUser.setCreator(SessionUtil.getUserCode(session));
			uumUser.setCreateDate(new Date());
			uumUser.setUserNamePy(CommonUtils.getFirstLetters(uumUser.getUserName()));

			userService.saveUser(uumUser, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存用户失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存用户失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("保存用户失败");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {

		model.addAttribute("user", new UumUser());
		model.addAttribute("action", "create");

		return "business/uum/user/userMgrForm";
	}

	/**
	 * 功能说明 : 停用启用用户
	 * 
	 * @param id
	 * @param disEnableFlag 停用启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Oct 6, 2013 9:16:06 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "");
		try {
			boolean disEnableBol = userService.disEnableUser(id, disEnableFlag, request);
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		} catch (SystemException e) {
			logger.error("2. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 根据用户名模糊查询
	 * 
	 * @param userName
	 * @return
	 * @author rutine
	 * @time Oct 20, 2013 3:07:34 PM
	 */
	@RequestMapping("queryUsersByUserName")
	@ResponseBody
	public List<UumUser> queryUsersByUserName(@RequestParam(defaultValue = "") String userName) {
		return privilegeService.findUsersByUserName(userName);
	}

	/**
	 * 功能说明 : 根据用户ID 已分配行权限
	 * 
	 * @param id
	 * @return
	 * @author rutine
	 * @time Oct 20, 2013 3:03:59 PM
	 */
	@RequestMapping("/getSelectRowPrivilege")
	@ResponseBody
	public Map<String, Object> getSeledRowPrivilege(@RequestParam long id) {
		Map map = privilegeService.findSelectRowPrivilegeByUserId(id);
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("rowList", map.get(USER));
		newMap.put("rowPrivilege", map.get(PRIVILEGE_SCOPE));

		return newMap;
	}

	/**
	 * 功能说明 : 查找指定节点的下一级子节点
	 * 
	 * @param treeId 机构角色树id
	 * @param isCheckbox
	 * @author rutine
	 * @time Dec 1, 2012 1:45:37 PM
	 */
	@RequestMapping(value = "/getChildNodes", method = RequestMethod.GET)
	@ResponseBody
	public List<? extends TreeVo> getChildNodes(
			@RequestParam(value = "treeId", defaultValue = "0_1") String treeId,
			@RequestParam(value = "isCheckbox", defaultValue = "0") String isCheckbox) {

		List<? extends TreeVo> asyncTreeList = userService.findNextLevelChildNodes(treeId, isCheckbox);

		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));

		return asyncTreeList;
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postUpdateForm(@ModelAttribute("preload") UumUser uumUser,
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "修改用户成功");
		try {
			uumUser.setUserNamePy(uumUser.getUserName());
			userService.updateUser(uumUser, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改用户失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改用户失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("修改用户失败");
		}

		return ajaxResult;
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		UumUser uumUser = userService.getUserByUserId(id);

		model.addAttribute("user", uumUser);
		model.addAttribute("action", "update");

		return "business/uum/user/userMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		UumUser uumUser = userService.getUserByUserId(id);

		model.addAttribute("user", uumUser);
		model.addAttribute("action", "view");

		return "business/uum/user/userMgrForm";
	}

	/**
	 * 功能说明 : 保存为用户分配的模块操作权限
	 * 
	 * @param id 用户id
	 * @param privilegeScope 权限范围
	 * @param optIdList 模块id集合
	 * @param request
	 * @return json消息
	 * @author rutine
	 * @time Oct 13, 2013 2:13:42 PM
	 */
	@RequestMapping(value = "/saveOptPrivilege", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOptPrivilege(@RequestParam long id,
			@RequestParam String privilegeScope,
			@RequestParam Set<String> optIdList, HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "为用户分配操作权限成功");
		try {
			List<String> list = new ArrayList<String>(optIdList);
			privilegeService.savePrivilege(list, id, PRIVILEGE_TYPE_OPT, OWNER_TYPE_USR, privilegeScope, request);
		} catch (ApplicationException e) {
			logger.error("1. 为用户分配操作权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.toString());
		} catch (SystemException e) {
			logger.error("2. 为用户分配操作权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("为用户分配操作权限失败!");
		}

		logger.debug("msg return by saveOptPrivilege ---> {}", ajaxResult);

		return ajaxResult;
	}

	/**
	 * 功能说明 : 保存为用户分配的角色
	 * 
	 * @param id 用户id
	 * @param defaultRoleId 默认角色id
	 * @param roleIdList 角色id集合
	 * @param request
	 * @return
	 * @author rutine
	 * @time Oct 13, 2013 9:17:05 PM
	 */
	@RequestMapping(value = "/saveRolePrivilege", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveRolePrivilege(
			@RequestParam long id,
			@RequestParam long defaultRoleId,
			@RequestParam Set<Long> roleIdList, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "为用户分配角色成功");
		try {
			List<Long> list = new ArrayList<Long>(roleIdList);
			roleUserService.saveRoleUserRef2(id, list, defaultRoleId, request);
		} catch (ApplicationException e) {
			logger.error("1. 为用户分配角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.toString());
		} catch (SystemException e) {
			logger.error("2. 为用户分配角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("为用户分配角色失败!");
		}

		logger.debug("msg return by saveRolePrivilege ---> {}", ajaxResult);

		return ajaxResult;
	}

	/**
	 * 功能说明 : 保存为用户分配的行权限
	 * 
	 * @param id
	 * @param rowPrivilege
	 * @param roleIdList
	 * @param request
	 * @return
	 * @author rutine
	 * @time Oct 20, 2013 4:25:02 PM
	 */
	@RequestMapping(value = "/saveRowPrivilege")
	@ResponseBody
	public AjaxResult saveRowPrivilege(
			@RequestParam long id,
			@RequestParam String rowPrivilege,
			@RequestParam Set<String> roleIdList, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "为用户分配行权限成功.");
		try {
			List<String> list = new ArrayList<String>(roleIdList);
			privilegeService.savePrivilege(list, id, PRIVILEGE_TYPE_ROW, OWNER_TYPE_USR, rowPrivilege, request);
		} catch (ApplicationException e) {
			logger.error("1. 为用户分配行权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.toString());
		} catch (SystemException e) {
			logger.error("2. 为用户分配行权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("为用户分配行权限失败!");
		}

		logger.debug("msg return by saveRowPrivilege ---> {}", ajaxResult);

		return ajaxResult;
	}

	/**
	 * 功能说明 : 重置用户密码
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @author rutine
	 * @time Oct 20, 2013 4:48:55 PM
	 */
	@RequestMapping(value = "/resetPwd")
	@ResponseBody
	public AjaxResult resetPwd(
			@RequestParam long id,
			@RequestParam String userName, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "重置密码成功.");
		try {
			// 系统参数用户默认密码
			String userDefaultPwd = platformCommonService.findSystemParaByParaName(USER_DEFAULT_PWD);
			userService.resetPwdByUserId(userDefaultPwd, userName, id, request);
		} catch (ApplicationException e) {
			logger.error("1. 重置密码失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.toString());
		} catch (SystemException e) {
			logger.error("2. 重置密码失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("重置密码失败!");
		}

		logger.debug("msg return by resetPwd ---> {}", ajaxResult);

		return ajaxResult;
	}
	
	/**
	 * 功能说明 : 
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 6, 2014 9:14:42 PM
	 */
	@RequestMapping(value = "updateUserInfo", method = RequestMethod.GET)
	public String getUpdateUserInfo() {
		return "business/commonused/userInfoSetting";
	}
	
	/**
	 * 功能说明 : 修改用户密码
	 * 
	 * @param password
	 * @param newPassword
	 * @param confirmPassword
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Nov 6, 2014 9:13:20 PM
	 */
	@RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(
			@RequestParam(value = "userPassword") String password,
			@RequestParam(value = "userNewPassword") String newPassword,
			@RequestParam(value = "userConfirmPassword") String confirmPassword,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		
		redirectAttributes.addAttribute("error", "修改成功");
		if(!newPassword.equals(confirmPassword)) {
			redirectAttributes.addAttribute("error", "两次输入的新密码不一致");
		} else {
			try {
				Long userId = SessionUtil.getUserId(request.getSession());
				password = CommonUtils.encrypt(password);
				UumUser user = userService.getUserByUserId(userId);
				if(password.equals(user.getUserPassword())) {
					user.setUserPassword(CommonUtils.encrypt(newPassword));
					userService.updateUserInfo(user, request);
				} else {
					redirectAttributes.addAttribute("error", "密码错误");
				}
			} catch (ApplicationException e) {
				logger.error("1. 修改用户密码!", e);
			} catch (SystemException e) {
				logger.error("2. 修改用户密码!", e);
			}
		}
		
		return "redirect:updateUserInfo";
	}
	
	/**
	 * 功能说明 : 上传头像
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 1, 2014 8:28:55 AM
	 */
	@RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postUploadPhoto(MultipartHttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "上传头像成功.");
		
		MultipartFile file = request.getFile("file");
		if(file != null && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			StringBuilder nameBuilder = new StringBuilder();
			for (int i = 0; i < 32; i++) {
				nameBuilder.append(CommonUtils.getRandomChar());
			}
			nameBuilder.append("_").append(originalFileName);
			try {
				Long userId = SessionUtil.getUserId(request.getSession());
				String fileName = nameBuilder.toString();
				String photoUrl = "/mycuckoo/" + imagePath + fileName;
				CommonUtils.saveFile(imagePath, fileName, file.getInputStream());
				userService.updateUserPhotoUrl(photoUrl, userId, request);
				request.getSession().setAttribute(USER_PHOTO_URL, photoUrl);
			} catch (IOException e) {
				logger.error("上传头像失败！");
				
				ajaxResult.setStatus(false);
				ajaxResult.setMsg("上传头像失败！");
			} catch (ApplicationException e) {
				logger.error("更新头像失败！");
				
				ajaxResult.setStatus(false);
				ajaxResult.setMsg("上传头像失败！");
			}
		}
		
		return ajaxResult;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public UumUser get(@RequestParam(value = "userId", required = false) Long id) {
		if (id != null) {
			return userService.getUserByUserId(id);
		}
		return null;
	}

	/**
	 * 设置自定义属性转换器
	 * 
	 * @param request the current request
	 * @param binder the data binder
	 */
	@InitBinder
	public void InitBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		// 不要删除下行注释! 将来"yyyy-MM-dd"将配置到properties文件中
		// SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format", request.getLocale()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
}
