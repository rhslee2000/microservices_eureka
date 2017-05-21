package au.com.madman.eureka_ribbon_feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import au.com.madman.eureka_ribbon_feign.config.AppConfig;
import au.com.madman.eureka_ribbon_feign.service.POSServiceFeignClient;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackageClasses = POSServiceFeignClient.class)
@ComponentScan(basePackageClasses = { POSServiceFeignClient.class, AppConfig.class })
@RestController
public class EurekaRibbonFeignApplication {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private POSServiceFeignClient aPOSServiceFeignClient;

    public static void main(String[] args) {
        SpringApplication.run(EurekaRibbonFeignApplication.class, args);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String ping() {
        return aPOSServiceFeignClient.ping(123);
    }

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/rest", method = RequestMethod.GET, produces = "application/json")
    public String pingRestTemplate() {
        Application application = eurekaClient.getApplication("spike-hello-service");
        logger.warn("Got {} instances", application.getInstances().size());
        String ip = "spike-hello-service";
        for (InstanceInfo info : application.getInstances()) {
            logger.warn("app {}, host {}, vip {}, ip {}", info.getAppName(), info.getHostName(), info.getVIPAddress(), info.getIPAddr());
            ip = info.getHostName() + ":" + info.getPort();
        }
        logger.warn("ip = {}", ip);
        return restTemplate.getForObject("http://" + ip + "/hi", String.class);
        // restTemplate.getForObject("http://spike-hello-service/hi", String.class);
    }

    @RequestMapping(value = "/pos/{name}", method = RequestMethod.GET, produces = "application/json")
    public String posAgent(@PathVariable("name") String name) {
        Application application = eurekaClient.getApplication("POS-SERVICE-123-" + name);
        for (InstanceInfo info : application.getInstances()) {
            logger.info("app {}, host {}", info.getAppName(), info.getHostName());
        }

        return restTemplate.postForObject("http://POS-SERVICE-123-" + name + "/ped/tender", null, String.class, new Object[0]);
    }
}
