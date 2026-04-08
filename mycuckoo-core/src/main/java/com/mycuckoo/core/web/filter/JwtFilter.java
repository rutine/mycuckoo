package com.mycuckoo.core.web.filter;

import com.mycuckoo.constant.BaseConst;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.repository.auth.RowContextHolder;
import com.mycuckoo.core.repository.auth.RowInfo;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.util.web.SessionContextHolder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/8/20 19:52
 */
@Order(1)
public class JwtFilter extends OncePerRequestFilter {
    private BiFunction<String, UserInfo, List<String>> loader;

    public JwtFilter(BiFunction<String, UserInfo, List<String>> loader) {
        this.loader = loader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SessionContextHolder.setRequest(request);
        try {
             try {
                 String authorization = request.getHeader("Authorization");
                 if (authorization != null && authorization.startsWith("Bearer")) {
                     String token = authorization.substring("Bearer".length() + 1);
                     Jwt jwt = Jwts.parser()
                             .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(BaseConst.SECRET)))
                             .build()
                             .parse(token);

                     Map payload = (Map) jwt.getPayload();
                     Object actId = payload.get("actId");
                     Object actCode = payload.get("actCode");
                     if (actId != null) {
                         SessionContextHolder.setAccountId(NumberUtils.toLong(actId.toString()));
                     }
                     if (actCode != null) {
                         SessionContextHolder.setAccountCode(actCode.toString());
                     }
                     if (payload.containsKey("id")) {
                         UserInfo user = JsonUtils.fromJson(JsonUtils.toJson(payload), UserInfo.class);
                         SessionContextHolder.setUserInfo(user);
                         RowContextHolder.set(new RowInfo(user.getOrgId(), user.getId()));

                         if (loader != null && SessionContextHolder.getResources() == null) {
                             SessionContextHolder.setResources(loader.apply(actCode.toString(), user));
                         }
                     }
                 }
             } catch (ExpiredJwtException e) {
                 logger.info("jwt expired");
             } catch (Exception e) {
                 logger.error("parse jwt error", e);
             }

            chain.doFilter(request, response);
        } finally {
            SessionContextHolder.setRequest(null);
        }
    }
}


