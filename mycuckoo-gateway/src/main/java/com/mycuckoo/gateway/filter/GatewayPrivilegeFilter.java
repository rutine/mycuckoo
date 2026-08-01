package com.mycuckoo.gateway.filter;

import com.mycuckoo.core.constant.BaseConst;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.web.filter.PrivilegeFilter;
import com.mycuckoo.gateway.client.UserClient;
import com.mycuckoo.gateway.config.PrivilegeProperties;
import com.mycuckoo.gateway.exception.ForbiddenException;
import com.mycuckoo.gateway.exception.UnauthorizedException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mycuckoo.core.constant.BaseConst.SESSION_USER_INFO;

/**
 * 权限过滤器
 */
public class GatewayPrivilegeFilter implements GlobalFilter, Ordered {
    private final Logger logger = LoggerFactory.getLogger(GatewayPrivilegeFilter.class);

    private PrivilegeProperties properties;
    private UserClient client;
    private PathMatcher pathMatcher = new AntPathMatcher();
    private volatile PrivilegeFilter.ResourceMather resourceMather = new PrivilegeFilter.ResourceMather(new ArrayList<>(), pathMatcher);
    private ResourceHandle resourceHandle;

    public GatewayPrivilegeFilter(PrivilegeProperties properties, UserClient client) {
        this.properties = properties;
        this.client = client;

        resourceHandle = new ResourceHandle();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        MDC.put("traceId", exchange.getRequest().getId());
        logger.info("request uri={}", path);

        if (checkAllowUrls(path)) {
            return chain.filter(exchange);
        }

        return Mono.justOrEmpty(readJwt(exchange))
                .switchIfEmpty(Mono.fromRunnable(() -> nonLogin(exchange)))
                .flatMap(user -> {
                    //缓存失效或者未登陆
                    if (user == null || user.getAccountId() == null) {
                        return nonLogin(exchange);
                    }

                    //未完全登陆状态(缺少选择组织)
                    if (user.getId() == null && !path.endsWith("/orgs")) {
                        return nonLogin(exchange);
                    }

                    return checkPrivilege(path, exchange.getRequest().getMethod().name(), authorization)
                            .flatMap(hasPrivilege -> {
                                if (!hasPrivilege) {
                                    return nonPrivileged(exchange, HttpStatus.FORBIDDEN);
                                }

                                ServerWebExchange newExchange = exchange;
                                // 放置用户数据到exchange中
                                newExchange.getAttributes().put(SESSION_USER_INFO, user);

                                return chain.filter(newExchange);
                            });
                });
    }

    @Override
    public int getOrder() {
        return 0;
    }



    private UserInfo readJwt(ServerWebExchange exchange) {
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring("Bearer ".length());
            if (!StringUtils.hasText(token)) {
                return null;
            }
            Jwt jwt = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(BaseConst.SECRET)))
                    .build()
                    .parse(token);

            Map payload = (Map) jwt.getPayload();

            if (payload.containsKey("id")) {
                return JsonUtils.fromJson(JsonUtils.toJson(payload), UserInfo.class);
            } else {
                UserInfo userInfo = new UserInfo();
                Object actId = payload.get("actId");
                Object actCode = payload.get("actCode");
                if (actId != null) {
                    userInfo.setAccountId(NumberUtils.toLong(actId.toString()));
                }
                if (actCode != null) {
                    userInfo.setUserName(actCode.toString());
                }
                return userInfo;
            }
        }

        return null;
    }
    private Mono<Boolean> checkPrivilege(String path, String method, String authorization) {
        if (properties.getSessionUrls().contains(path)) {
            return Mono.just(true);
        }

        if (!StringUtils.hasText(authorization)) {
            return Mono.just(false);
        }

        return client.userResources(authorization)
                .defaultIfEmpty(List.of())
                .flatMap(resources -> {
                    if (resources.isEmpty()) {
                        return Mono.just(false);
                    }

                    String tripServerNamePath = path.substring(path.indexOf('/', 1));
                    return resourceHandle.load(authorization)
                            .thenReturn(resourceMather.match(new com.mycuckoo.core.web.filter.PrivilegeFilter.ResourceInfo(tripServerNamePath, method), resources));
                });
    }
    private Mono<Void> nonLogin(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        ServerHttpResponse response = exchange.getResponse();
        if (path.contains("export")) {
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().set("Location", "/");
            return exchange.getResponse().setComplete();
        }

        throw new UnauthorizedException("未登录");

//        //没法输出内容
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        DataBuffer wrap = response.bufferFactory().wrap(nonLoginResp);
//        return response.writeWith(Mono.fromSupplier(() -> wrap)).then(Mono.defer(response::setComplete));
    }
    private Mono<Void> nonPrivileged(ServerWebExchange exchange, HttpStatus status) {
        throw new ForbiddenException("无权访问");

//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(status);
//        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        DataBuffer wrap = response.bufferFactory().wrap(nonPrivilegedResp);
//        return response.writeWith(Mono.fromSupplier(() -> wrap)).then(Mono.defer(response::setComplete));
    }

    private boolean checkAllowUrls(String url) {
        Set<String> allowUrls = properties.getAllowUrls();
        return allowUrls != null && !allowUrls.isEmpty()
                && (allowUrls.contains(url) || allowUrls.stream().filter(patt -> pathMatcher.match(patt, url)).findFirst().isPresent());
    }


    public class ResourceHandle {
        //2小时
        private static final long HOUR = 2 * 60 * 60 * 1000L;

        private volatile long expireAt;

        public Mono<Void> load(String authorization) {
            if (expireAt <= System.currentTimeMillis()) {
                return client.allResources(authorization)
                        .doOnNext(resources -> {
                            resourceMather = new PrivilegeFilter.ResourceMather(resources, pathMatcher);
                            expireAt = System.currentTimeMillis() + HOUR;
                        })
                        .then();
            }

            return Mono.empty();
        }
    }
}
