package com.mycuckoo.web.config;

import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private static PathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        SessionUtil.setRequest(request);

        String uri = request.getRequestURI();
        logger.info("request uri:{}", uri);
        if (matcher.match("/login", uri)
                || matcher.match("/file", uri)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || SessionUtil.getAccountId() == null) {
            logger.info("未登录被拦截");

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
//            response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//            response.addHeader("Access-Control-Allow-Credentials", "true");
//            response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtils.toJson(AjaxResponse.create(HttpStatus.UNAUTHORIZED.value(), "未登录")));
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
