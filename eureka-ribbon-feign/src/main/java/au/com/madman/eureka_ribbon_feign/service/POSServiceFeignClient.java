package au.com.madman.eureka_ribbon_feign.service;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "spike-pos-service")
public interface POSServiceFeignClient extends POSService {

}
