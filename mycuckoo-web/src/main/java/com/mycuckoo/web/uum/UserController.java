package com.mycuckoo.web.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.RoleUserService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.TreeVoExtend;
import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.UserVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mycuckoo.common.constant.Common.*;
import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 用户管理Controller
 * 
 * @author rutine
 * @time Oct 19, 2014 10:04:42 AM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/uum/user/mgr")
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
	private PlatformServiceFacade platformServiceFacade;




	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public AjaxResponse<Page<UserVo>> list(
			@RequestParam(value = "treeId", defaultValue = "-1") String treeId,
			@RequestParam(value = "userCode", defaultValue = "") String userCode,
			@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Page<UserVo> page = userService.findByPage(treeId, userName,
				userCode, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 用户分配特殊操作权限
	 * 
	 * @param id 用户id
	 *
	 * @author rutine
	 * @time Oct 13, 2013 1:05:30 PM
	 */
	@RequestMapping(value = "/queryUserPrivilegeList", method = RequestMethod.POST)
	public AjaxResponse<AssignVo<TreeVoExtend>> listUserPrivilege(@RequestParam long id) {
		AssignVo<TreeVoExtend> vo = privilegeService.findSelectAUnselectModOptByOwnIdAOwnType(id, OWNER_TYPE_USR);

		return AjaxResponse.create(vo);
	}

	/**
	 * 功能说明 : 查询用户的所有角色
	 * 
	 * @param id 用户id
	 * @param model
	 *
	 * @author rutine
	 * @time Oct 13, 2013 4:53:00 PM
	 */
	@RequestMapping(value = "/queryUserRolePrivilegeList", method = RequestMethod.POST)
	public AjaxResponse<Map<String, Object>> listRolePrivilege(@RequestParam long id, Model model) {
		List<RoleUserRefVo> roleUserRefList = roleUserService.findByUserId(id);
		List<? extends TreeVo> orgRoleList = this.getChildNodes("0_1", "Y").getData();

		Map<String, Object> map = Maps.newHashMap();
		map.put("orgRoleList", orgRoleList);
		map.put("roleUserRefList", roleUserRefList);

		return AjaxResponse.create(map);
	}

	/**
	 * 功能说明 : 保存用户
	 * 
	 * @param user
	 *
	 * @author rutine
	 * @time Oct 6, 2013 8:26:57 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public AjaxResponse<String> postForm(UserVo user) {
		logger.debug(JsonUtils.toJson(user));

		user.setCreator(SessionUtil.getUserCode());
		user.setCreateDate(new Date());
		user.setUserNamePy(CommonUtils.getFirstLetters(user.getUserName()));

		userService.save(user);

		return AjaxResponse.create("保存用户成功");
	}

	/**
	 * 功能说明 : 停用启用用户
	 * 
	 * @param id
	 * @param disEnableFlag 停用启用标志
	 *
	 * @author rutine
	 * @time Oct 6, 2013 9:16:06 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		userService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("停用启用成功");
	}

	/**
	 * 功能说明 : 根据用户名模糊查询
	 * 
	 * @param userName
	 *
	 * @author rutine
	 * @time Oct 20, 2013 3:07:34 PM
	 */
	@RequestMapping(value = "queryUsersByUserName", method = RequestMethod.GET)
	public AjaxResponse<List<UserVo>> queryUsersByUserName(@RequestParam(defaultValue = "") String userName) {
		List<UserVo> vos =  privilegeService.findUsersByUserName(userName);

		return AjaxResponse.create(vos);
	}

	/**
	 * 功能说明 : 根据用户ID 已分配行权限
	 * 
	 * @param id
	 * @return
	 * @author rutine
	 * @time Oct 20, 2013 3:03:59 PM
	 */
	@RequestMapping(value = "/getSelectRowPrivilege", method = RequestMethod.GET)
	public AjaxResponse<Map<String, Object>> getSelectRowPrivilege(@RequestParam long id) {
		Map map = privilegeService.findSelectRowPrivilegeByUserId(id);
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("rowList", map.get(USER));
		newMap.put("rowPrivilege", map.get(PRIVILEGE_SCOPE));

		return AjaxResponse.create(newMap);
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
	public AjaxResponse<List<? extends TreeVo>> getChildNodes(
			@RequestParam(value = "treeId", defaultValue = "0_1") String treeId,
			@RequestParam(value = "isCheckbox", defaultValue = "0") String isCheckbox) {

		List<? extends TreeVo> asyncTreeList = userService.findNextLevelChildNodes(treeId, isCheckbox);

		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));

		return AjaxResponse.create(asyncTreeList);
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public AjaxResponse<String> postUpdateForm(@ModelAttribute("preload") UserVo user) {
		user.setUserNamePy(user.getUserName());
		userService.update(user);

		return AjaxResponse.create("修改用户成功");
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public AjaxResponse<User> getUpdateForm(@RequestParam long id) {
		User user = userService.getUserByUserId(id);

		return AjaxResponse.create(user);
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public AjaxResponse<User> getViewForm(@RequestParam long id) {
		User user = userService.getUserByUserId(id);

		return AjaxResponse.create(user);
	}

	/**
	 * 功能说明 : 保存为用户分配的模块操作权限
	 * 
	 * @param id 用户id
	 * @param privilegeScope 权限范围
	 * @param optIdList 模块id集合
	 *
	 * @return json消息
	 * @author rutine
	 * @time Oct 13, 2013 2:13:42 PM
	 */
	@RequestMapping(value = "/saveOptPrivilege", method = RequestMethod.POST)
	public AjaxResponse<String> saveOptPrivilege(@RequestParam long id,
                                       @RequestParam String privilegeScope,
                                       @RequestParam Set<String> optIdList) {

		List<String> list = new ArrayList<String>(optIdList);
		privilegeService.save(list, id, PRIVILEGE_TYPE_OPT, OWNER_TYPE_USR, privilegeScope);

		return AjaxResponse.create("为用户分配操作权限成功");
	}

	/**
	 * 功能说明 : 保存为用户分配的角色
	 * 
	 * @param id 用户id
	 * @param defaultRoleId 默认角色id
	 * @param roleIdList 角色id集合
	 *
	 * @author rutine
	 * @time Oct 13, 2013 9:17:05 PM
	 */
	@RequestMapping(value = "/saveRolePrivilege", method = RequestMethod.POST)
	public AjaxResponse<String> saveRolePrivilege(
			@RequestParam long id,
			@RequestParam long defaultRoleId,
			@RequestParam Set<Long> roleIdList) {

		List<Long> list = new ArrayList<Long>(roleIdList);
		roleUserService.save2(id, list, defaultRoleId);

		return AjaxResponse.create("为用户分配角色成功");
	}

	/**
	 * 功能说明 : 保存为用户分配的行权限
	 * 
	 * @param id
	 * @param rowPrivilege
	 * @param roleIdList
	 *
	 * @author rutine
	 * @time Oct 20, 2013 4:25:02 PM
	 */
	@RequestMapping(value = "/saveRowPrivilege", method = RequestMethod.POST)
	public AjaxResponse<String> saveRowPrivilege(
			@RequestParam long id,
			@RequestParam String rowPrivilege,
			@RequestParam Set<String> roleIdList) {

		List<String> list = new ArrayList<>(roleIdList);
		privilegeService.save(list, id, PRIVILEGE_TYPE_ROW, OWNER_TYPE_USR, rowPrivilege);

		return AjaxResponse.create("为用户分配行权限成功");
	}

	/**
	 * 功能说明 : 重置用户密码
	 * 
	 * @param id
	 *
	 * @author rutine
	 * @time Oct 20, 2013 4:48:55 PM
	 */
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public AjaxResponse<String> resetPwd(
			@RequestParam long id,
			@RequestParam String userName) {

		// 系统参数用户默认密码
		String userDefaultPwd = platformServiceFacade.findSystemParaByParaName(USER_DEFAULT_PWD);
		userService.resetPwdByUserId(userDefaultPwd, userName, id);

		return AjaxResponse.create("重置密码成功");
	}

	
	/**
	 * 功能说明 : 修改用户密码
	 * 
	 * @param password
	 * @param newPassword
	 * @param confirmPassword
	 *
	 * @author rutine
	 * @time Nov 6, 2014 9:13:20 PM
	 */
	@RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
	public AjaxResponse<String> updateUserInfo(
			@RequestParam(value = "userPassword") String password,
			@RequestParam(value = "userNewPassword") String newPassword,
			@RequestParam(value = "userConfirmPassword") String confirmPassword) {
		
		
		if(!newPassword.equals(confirmPassword)) {
			return AjaxResponse.create(500, "两次输入的新密码不一致");
		} else {
			Long userId = SessionUtil.getUserId();
			password = CommonUtils.encrypt(password);
			User user = userService.getUserByUserId(userId);
			if(password.equals(user.getUserPassword())) {
				user.setUserPassword(CommonUtils.encrypt(newPassword));
				userService.updateUserInfo(user);
			} else {
				return AjaxResponse.create(500, "密码错误");
			}
		}
		
		return AjaxResponse.create("修改成功");
	}
	
	/**
	 * 功能说明 : 上传头像
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 1, 2014 8:28:55 AM
	 */
	@RequestMapping(value = "/upload/photo", method = RequestMethod.POST)
	public AjaxResponse<String> postUploadPhoto(MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");
		if(file != null && !file.isEmpty()) {
			String originalFileName = file.getOriginalFilename();
			StringBuilder nameBuilder = new StringBuilder();
			for (int i = 0; i < 32; i++) {
				nameBuilder.append(CommonUtils.getRandomChar());
			}
			nameBuilder.append("_").append(originalFileName);
			try {
				Long userId = SessionUtil.getUserId();
				String fileName = nameBuilder.toString();
				String photoUrl = "/mycuckoo/" + imagePath + fileName;
				CommonUtils.saveFile(imagePath, fileName, file.getInputStream());
				userService.updateUserPhotoUrl(photoUrl, userId);
				request.getSession().setAttribute(USER_PHOTO_URL, photoUrl);
			} catch (IOException e) {
				logger.error("上传头像失败！");

				AjaxResponse.create(500, "上传头像失败!");
			}
		}
		
		return AjaxResponse.create("上传头像成功");
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
