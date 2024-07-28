package com.mycuckoo.core.web.filter;

import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.repository.auth.RowContextHolder;
import com.mycuckoo.core.repository.auth.RowInfo;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.util.web.SessionContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/24 20:25
 */
@Order
public class PrivilegeFilter extends OncePerRequestFilter {
    private String[] allowPaths = {};
    private String[] sessionPaths = {};
    private PathMatcher pathMatcher = new AntPathMatcher();
    private ResourceMather resourceMather;


    public PrivilegeFilter(String[] allowPaths, String[] sessionPaths, List<ResourceInfo> resources) {
        this.allowPaths = allowPaths;
        this.sessionPaths = sessionPaths;
        this.resourceMather = new ResourceMather(resources, pathMatcher);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionContextHolder.setRequest(request);
        try {
            String uri = request.getRequestURI();
            logger.info("request uri:" + uri);
            if (checkPath(allowPaths, uri)) {
                chain.doFilter(request, response);
                return;
            }

            if (SessionContextHolder.getAccountId() == null) {
                logger.info("未登录被拦截");

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                PrintWriter writer = response.getWriter();
                writer.write(JsonUtils.toJson(AjaxResponse.create(HttpStatus.UNAUTHORIZED.value(), "未登录")));
                writer.flush();
                writer.close();

                return;
            } else if (!checkPath(sessionPaths, uri) && !resourceMather.match(new ResourceInfo(uri, request.getMethod()), SessionContextHolder.getResources())) {
                logger.info("无权被拦截");

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                PrintWriter writer = response.getWriter();
                writer.write(JsonUtils.toJson(AjaxResponse.create(HttpStatus.FORBIDDEN.value(), "无权访问")));
                writer.flush();
                writer.close();

                return;
            }

            UserInfo user = SessionContextHolder.getUserInfo();
            if (user != null) {
                RowContextHolder.set(new RowInfo(user.getOrgId(), user.getId()));
            }

            chain.doFilter(request, response);
        } finally {
            SessionContextHolder.setRequest(null);
        }
    }

    private boolean checkPath(String[] paths, String path) {
        for (String allowPath : paths) {
            if (pathMatcher.isPattern(allowPath)) {
                if (pathMatcher.match(allowPath, path)) {
                    return true;
                }
            } else if (allowPath.equals(path)) {
                return true;
            }
        }

        return false;
    }

    public static class ResourceMather {
        private PathMatcher pathMatcher;
        private Map<String, List<ResourceInfo>> directPathMather = new HashMap<>();
        private Map<String, List<ResourceInfo>> quickPatternPathMather = new HashMap<>();
        private List<ResourceInfo> patternPathMather = new ArrayList<>();

        public ResourceMather(List<ResourceInfo> list, PathMatcher pathMatcher) {
            this.pathMatcher = pathMatcher;
            for (ResourceInfo info : list) {
                if (pathMatcher.isPattern(info.getPath())) {
                    String path = info.getPath();
                    if (!path.startsWith("/")) {
                        path += "/" + path.trim();
                    }
                    String[] paths = path.split("/");
                    if (paths.length > 3 && !isVariable(paths[1]) && !isVariable(paths[2])) {
                        String newPath = Stream.of(paths[1], paths[2]).collect(Collectors.joining("/"));
                        quickPatternPathMather.compute(newPath, (key, value) -> {
                            if (value == null) {
                                value = new ArrayList<>();
                            }
                            value.add(info);

                            return value;
                        });
                        continue;
                    }

                    patternPathMather.add(info);
                } else {
                    directPathMather.compute(info.getPath(), (key, value) -> {
                        if (value == null) {
                            value = new ArrayList<>();
                        }
                        value.add(info);

                        return value;
                    });
                }
            }
        }

        public boolean match(ResourceInfo resourceInfo, Collection<String> codes) {
            if (codes == null || codes.isEmpty()) {
                return false;
            }

            //直接匹配
            List<ResourceInfo> list = directPathMather.get(resourceInfo.getPath());
            if (matchMethod(resourceInfo.getMethod(), list, codes)) {
                return true;
            }

            //模式快速匹配
            String[] paths = resourceInfo.getPath().split("/");
            if (paths.length > 3 && !isVariable(paths[1]) && !isVariable(paths[2])) {
                String path = Stream.of(paths[1], paths[2]).collect(Collectors.joining("/"));
                list = quickPatternPathMather.get(path);
                if (matchPattern(resourceInfo, list, codes)) {
                    return true;
                }
            }

            //模式匹配
            return matchPattern(resourceInfo, patternPathMather, codes);
        }

        private boolean matchPattern(ResourceInfo info, List<ResourceInfo> patterns, Collection<String> codes) {
            if (patterns == null || patterns.isEmpty()) {
                return false;
            }

            return patterns.stream()
                    .filter(o -> pathMatcher.match(o.getPath(), info.getPath()) && o.method.equalsIgnoreCase(info.getMethod()) && codes.contains(o.getCode()))
                    .findFirst().isPresent();
        }
        private boolean matchMethod(String method, List<ResourceInfo> methods, Collection<String> codes) {
            if (methods == null || methods.isEmpty()) {
                return false;
            }

            return methods.stream().filter(o -> o.method.equalsIgnoreCase(method) && codes.contains(o.getCode())).findFirst().isPresent();
        }
        private boolean isVariable(String path) {
            return (path.charAt(0) == '{' && path.charAt(path.length() - 1) == '}');
        }
    }

    public static class ResourceInfo {
        private String path;
        private String method;
        private String code; //授权码

        public ResourceInfo(String path, String method) {
            this(path, method, null);
        }

        public ResourceInfo(String path, String method, String code) {
            this.path = path;
            this.method = method;
            this.code = code;
        }

        public String getPath() {
            return path;
        }

        public String getMethod() {
            return method;
        }

        public String getCode() {
            return code;
        }
    }
}

