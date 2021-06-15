package alien4cloud.sampleplugin.suggestions;

import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Plugin spring context configuration.
 */
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
@Configuration
@ComponentScan("alien4cloud.sampleplugin")
public class ContextConfiguration {
}
