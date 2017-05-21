package au.com.madman.eureka_client;

import java.net.SocketException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) throws SocketException {
        SpringApplication.run(EurekaClientApplication.class, args);
	}

    @RequestMapping(value = "/hi", method = RequestMethod.GET, produces = "application/json")
    public String ping() {
        return "hello!";
    }
}
