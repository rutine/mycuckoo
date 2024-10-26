package com.mycuckoo.gateway.client;

import com.mycuckoo.core.web.filter.PrivilegeFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "localhost:8080", name = "admin", contextId = "admin")
public interface UserClient {

    @GetMapping("/login/all-resources")
    List<PrivilegeFilter.ResourceInfo> allResources();

    @GetMapping("/login/usr-resources")
    List<String> userResources();
}
