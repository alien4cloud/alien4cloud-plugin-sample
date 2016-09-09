package alien4cloud.plugin.local;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import alien4cloud.plugin.Plugin;
import alien4cloud.plugin.model.ManagedPlugin;
import alien4cloud.plugin.model.PluginDescriptor;

/**
 * This component is used in order to launch the plugin from the ide (basically we run an alien4cloud server with the content of the plugin context being part
 * of the main context rather than imported as a plugin).
 * 
 * This allows to work easily in debug and with grunt serve (for ui part).
 */
@Component
@Profile("plugin-dev")
public class LocalManagedPlugin extends ManagedPlugin {
    private final static Plugin PLUGIN = new Plugin();
    static {
        PluginDescriptor pd = new PluginDescriptor();
        pd.setId("a4c-plugin-sample");
        pd.setVersion("1.3.0-RC1-SNAPSHOT");
        PLUGIN.setDescriptor(pd);
    }

    public LocalManagedPlugin() {
        super(null, PLUGIN, null, null);
    }

    public LocalManagedPlugin(String pluginPath) {
        super(pluginPath);
    }
}
