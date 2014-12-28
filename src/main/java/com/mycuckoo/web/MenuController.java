package com.mycuckoo.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能说明: 菜单Controller
 *
 * @author rutine
 * @time Sep 24, 2014 10:34:11 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuController {

	@RequestMapping(value = "/{belongToSys}/{modEnId}/{index}")
	public String menu(
			@PathVariable String belongToSys, 
			@PathVariable String modEnId,
			@PathVariable String index,
			@RequestParam Long moduleId,
			HttpSession session) {

		session.setAttribute("modEnId", moduleId); // 当前操作的模块id
		
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append("/").append(belongToSys).append("/").append(modEnId).append("/").append(index);
		
		return "redirect:" + pathBuilder.toString();
	}
}
