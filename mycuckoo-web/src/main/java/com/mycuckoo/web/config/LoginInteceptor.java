package com.mycuckoo.web.config;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;

public class LoginInteceptor implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(LoginInteceptor.class);
	private static PathMatcher matcher = new AntPathMatcher();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		SessionUtil.setRequest((HttpServletRequest) request);
		
		String uri = request.getRequestURI();
		if(matcher.match("/login/step/first", uri)) {
			return true;
		}
			
		HttpSession session = request.getSession(false);
		if(session == null || SessionUtil.getUserCode() == null) {
			logger.info("未登录被拦截");
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			response.addHeader("Access-Control-Allow-Origin", "http://localhost:3111");
			response.addHeader("Access-Control-Allow-Methods", "GET, POST");
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type");
			PrintWriter writer = response.getWriter();
			writer.write(JsonUtils.toJson(AjaxResponse.create(405, "未登录")));
			writer.flush();
			writer.close();
			
			return false;
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//TODO
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
		SessionUtil.setRequest(null);
	}

}
