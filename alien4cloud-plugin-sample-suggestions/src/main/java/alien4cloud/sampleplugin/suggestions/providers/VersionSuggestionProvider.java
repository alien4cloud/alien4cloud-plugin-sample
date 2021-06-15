package alien4cloud.sampleplugin.suggestions.providers;

import alien4cloud.application.ApplicationEnvironmentService;
import alien4cloud.exception.NotFoundException;
import alien4cloud.model.application.ApplicationEnvironment;
import alien4cloud.model.application.EnvironmentType;
import alien4cloud.model.common.SuggestionContextType;
import alien4cloud.model.common.SuggestionRequestContext;
import alien4cloud.suggestions.ISimpleSuggestionPluginProvider;
import alien4cloud.topology.TopologyServiceCore;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.alien4cloud.tosca.editor.EditorService;
import org.alien4cloud.tosca.model.definitions.AbstractPropertyValue;
import org.alien4cloud.tosca.model.definitions.ScalarPropertyValue;
import org.alien4cloud.tosca.model.templates.NodeTemplate;
import org.alien4cloud.tosca.model.templates.Topology;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This suggestion provider will explore topology and environment to adapt it's result regarding the context.
 */
@Component("sample-version-suggestion-provider")
@Slf4j
public class VersionSuggestionProvider implements ISimpleSuggestionPluginProvider {

    private static final List<String> VERSIONS = Lists.newArrayList("1.0.0", "2.0.0", "3.0.0");

    /**
     * This service can give you access to a topology that is being edited (even if not saved).
     */
    @Resource
    private EditorService editorService;

    @Resource
    private ApplicationEnvironmentService applicationEnvironmentService;

    /**
     * This service can give you access to a saved topology.
     */
    @Resource
    private TopologyServiceCore topologyServiceCore;

    /**
     * This method will be called each time the user will input something, so it could be a good idea to cache.
     */
    @SneakyThrows
    @Override
    public Collection<String> getSuggestions(String input, SuggestionRequestContext context) {
        log.info("Context is {}, username is {}", context, context.getUser().getUsername());
        List<String> result = VERSIONS;

        String topologyId = context.getData().getTopologyId();
        if (StringUtils.isNotEmpty(topologyId)) {
            // Here we could do whatever we want by exploring the topology
            Topology topology = null;
            if (context.getType() == SuggestionContextType.TopologyEdit) {
                // The user is editing a topology, we access the edited topology in order to have a fresh context
                topology = editorService.getTopology(topologyId);
            } else {
                topology = topologyServiceCore.getTopology(topologyId);
            }
            // The nodeId data is not always set (for example in case of DeploymentInput context type) so we have to check it.
            if (context.getData().getNodeId() != null) {
                NodeTemplate node = topology.getNodeTemplates().get(context.getData().getNodeId());
                AbstractPropertyValue apv = node.getProperties().get("vaccin_name");
                // here we just check the property vaccin_name and adapt the result if it's "Sanofi"
                if (apv instanceof ScalarPropertyValue) {
                    String vaccinName = ((ScalarPropertyValue)apv).getValue();
                    if ("Sanofi".equals(vaccinName)) {
                        result = Lists.newArrayList("0.0.0-UNDEFINED");
                    }
                }
            }
        }

        // the environmentId data is not always set (Archive topology edition, application topology edition ...) so we have to check it.
        if (context.getData().getEnvironmentId() != null) {
            try {
                ApplicationEnvironment applicationEnvironment = applicationEnvironmentService.getOrFail(context.getData().getEnvironmentId());
                if (applicationEnvironment.getEnvironmentType() != EnvironmentType.PRODUCTION) {
                    // If the environment is not a PRODUCTION environment, add "-SNAPSHOT" to versions.
                    result = result.stream().map(s -> s + "-SNAPSHOT").collect(Collectors.toList());
                }
            } catch(NotFoundException nfe) {
                // should not occur
            }
        }
        return result;
    }

}
