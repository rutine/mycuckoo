package com.mycuckoo.gateway.client;

import com.mycuckoo.core.web.filter.PrivilegeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;

import reactor.core.publisher.Mono;
import java.util.List;

@ReactiveFeignClient(name = "admin", path = "")
public interface UserClient {

    @GetMapping("/login/all-resources")
    Mono<List<PrivilegeFilter.ResourceInfo>> allResources(@RequestHeader("Authorization") String authorization);

    @GetMapping("/login/usr-resources")
    Mono<List<String>> userResources(@RequestHeader("Authorization") String authorization);
}
