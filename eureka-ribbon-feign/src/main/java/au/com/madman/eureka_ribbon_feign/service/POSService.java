package au.com.madman.eureka_ribbon_feign.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface POSService {

    @RequestMapping(value = "/terminal/{id}", method = RequestMethod.GET, produces = "application/json")
    String ping(@PathVariable("id") int id);
}
