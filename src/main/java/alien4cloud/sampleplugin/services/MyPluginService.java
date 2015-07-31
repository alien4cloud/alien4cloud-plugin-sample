package alien4cloud.sampleplugin.services;

import org.springframework.stereotype.Service;

/**
 * Sample of a plugin service.
 */
@Service
public class MyPluginService {
    /**
     * Simple say hello sample
     * 
     * @param name Name of the person to which to say hello
     * @return A string that says hello to 'name'.
     */
    public String sayHelloTo(String name) {
        return "hello " + name;
    }
}
