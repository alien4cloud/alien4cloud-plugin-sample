package alien4cloud.plugin.sample_modifier;

import alien4cloud.paas.wf.TopologyContext;
import alien4cloud.paas.wf.WorkflowSimplifyService;
import alien4cloud.paas.wf.WorkflowsBuilderService;
import alien4cloud.paas.wf.validation.WorkflowValidator;
import alien4cloud.tosca.context.ToscaContext;
import alien4cloud.tosca.context.ToscaContextual;
import alien4cloud.tosca.parser.ToscaParser;
import lombok.extern.slf4j.Slf4j;
import org.alien4cloud.alm.deployment.configuration.flow.FlowExecutionContext;
import org.alien4cloud.alm.deployment.configuration.flow.TopologyModifierSupport;
import org.alien4cloud.tosca.model.CSARDependency;
import org.alien4cloud.tosca.model.Csar;
import org.alien4cloud.tosca.model.definitions.ScalarPropertyValue;
import org.alien4cloud.tosca.model.templates.NodeTemplate;
import org.alien4cloud.tosca.model.templates.Topology;
import org.alien4cloud.tosca.model.types.AbstractToscaType;
import org.alien4cloud.tosca.normative.constants.NormativeComputeConstants;
import org.alien4cloud.tosca.normative.constants.NormativeRelationshipConstants;
import org.alien4cloud.tosca.utils.TopologyNavigationUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;

@Slf4j
@Component("sample-modifier-plugin")
public class SampleModifier extends TopologyModifierSupport {

    @Inject
    private WorkflowSimplifyService workflowSimplifyService;

    @Inject
    private WorkflowsBuilderService workflowBuilderService;

    @PostConstruct
    private void init() {

    }

    @Override
    @ToscaContextual
    public void process(Topology topology, FlowExecutionContext context) {
        try {
            WorkflowValidator.disableValidationThreadLocal.set(true);
            doProcess(topology, context);

            TopologyContext topologyContext = workflowBuilderService.buildCachedTopologyContext(new TopologyContext() {
                @Override
                public String getDSLVersion() {
                    return ToscaParser.LATEST_DSL;
                }

                @Override
                public Topology getTopology() {
                    return topology;
                }

                @Override
                public <T extends AbstractToscaType> T findElement(Class<T> clazz, String elementId) {
                    return ToscaContext.get(clazz, elementId);
                }
            });

            workflowSimplifyService.reentrantSimplifyWorklow(topologyContext, topology.getWorkflows().keySet());
        } catch (Exception e) {
            log.warn("Can't process topology:", e);
        } finally {
            WorkflowValidator.disableValidationThreadLocal.remove();
        }
    }

    protected void doProcess(Topology topology, FlowExecutionContext context) {
        log.info("Sample Modifier processing topology");
        // The context allows you to send infos, warn or error to the end user
        context.log().info("Sample Modifier processing topology");
        context.log().warn("This plugin should not be used in production !");

        Optional<CSARDependency> _normativeTypes = topology.getDependencies().stream().filter(csarDependency -> csarDependency.getName().equals("tosca-normative-types")).findFirst();
        if (!_normativeTypes.isPresent()) {
            context.log().error("No normative type found in topology, can't process !");
            return;
        }
        CSARDependency csarDependency = _normativeTypes.get();
        Csar csar = new Csar(csarDependency.getName(), csarDependency.getVersion());

        // the super type offers you some utilities
        // see org.alien4cloud.alm.deployment.configuration.flow.TopologyModifierSupport
        NodeTemplate myAddedCompute = super.addNodeTemplate(csar, topology, "MyInstrumentationCompute", NormativeComputeConstants.COMPUTE_TYPE, csar.getVersion());
        // the new compute is now added in the topology

        // let's define the max_instances property of the scalable capability to 2
        ScalarPropertyValue maxInstance = new ScalarPropertyValue("2");
        super.setNodeCappabilityPropertyPathValue(csar, topology, myAddedCompute, "scalable", "max_instances", maxInstance, false);

        // let's iterate throw all other nodes and add a depends on relation ship to this newly created compute
        topology.getNodeTemplates().forEach((nodeName, nodeTemplate) -> {
            if (nodeTemplate != myAddedCompute) {
                context.log().info(String.format("Adding a dependsOn relationship from %s to %s", nodeName, myAddedCompute.getName()));
                super.addRelationshipTemplate(csar, topology, nodeTemplate, myAddedCompute.getName(), NormativeRelationshipConstants.DEPENDS_ON, "dependency", "feature");
            }
        });

        // other interesting stuff can be found in TopologyNavigationUtil

        // for example, here we will count all node thar are or inherit from tosca.nodes.Compute
        int computeCount = TopologyNavigationUtil.getNodesOfType(topology, NormativeComputeConstants.COMPUTE_TYPE, true).size();
        context.log().info(String.format("The topology has now %d computes", computeCount));
    }

}
