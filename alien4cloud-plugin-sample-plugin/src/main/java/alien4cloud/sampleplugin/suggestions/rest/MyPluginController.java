package alien4cloud.sampleplugin.suggestions.rest;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alien4cloud.audit.annotation.Audit;
import alien4cloud.rest.model.RestResponse;
import alien4cloud.rest.model.RestResponseBuilder;
import alien4cloud.sampleplugin.suggestions.providers.MyPluginService;
import io.swagger.annotations.ApiOperation;

/**
 * Simple REST controller provided by the plugin
 */
@RestController
@RequestMapping({"/rest/sample/hello", "/rest/latest/sample/hello"})
@Slf4j
public class MyPluginController {

    public MyPluginController() {
        log.debug("Cstr");
        if (true) {
            log.debug("Cstr");
        }
    }

    @Resource
    private MyPluginService service;

    @ApiOperation(value = "Say hello to the user.")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Audit
    private RestResponse<String> sayHello(@RequestParam String name) {
        return RestResponseBuilder.<String> builder().data(service.sayHelloTo(name)).build();
    }

    @ApiOperation(value = "Say hello with authentication.")
    @RequestMapping(value="auth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Audit
    @PreAuthorize("isAuthenticated()")
    public RestResponse<String> sayHelloAuthenticated(@RequestParam String name) {
        return RestResponseBuilder.<String> builder().data(service.sayHelloTo(name)).build();
    }
}
