package alien4cloud.sampleplugin.suggestions;

import alien4cloud.events.AlienEvent;
import alien4cloud.model.suggestion.SuggestionEntry;
import alien4cloud.suggestions.services.SuggestionService;
import alien4cloud.utils.YamlParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.alien4cloud.tosca.catalog.events.AfterArchiveIndexed;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * In charge of providing custom suggestion configurations by
 * parsing the file in classpath://suggestions/a4c-sampleplugin-suggestions-configuration.yml
 *
 * Important : use a distinguished name for this file, for example by prefixing it's name by the pluginId (risks of collisions in classpath).
 */
@Component
@Slf4j
public class SuggestionConfigurator implements ApplicationListener<AlienEvent> {

    public static final String SUGGESTIONS_CONFIGURATION_YML = "suggestions/a4c-sampleplugin-suggestions-configuration.yml";
    public static final String ARCHIVE_NAME = "org.alien4cloud.sampleplugin.suggestions";

    @Resource
    private SuggestionService suggestionService;

    private SuggestionEntry[] suggestionEntries;

    @PostConstruct
    public void init() throws IOException {
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(SUGGESTIONS_CONFIGURATION_YML)) {
            suggestionEntries = YamlParserUtil.parse(input, SuggestionEntry[].class);
            this.pushSuggestionConfig();
        } catch (IOException e) {
            log.error("Something wrong in suggestion configuration, cannot read config file !", e);
        }
    }

    /**
     * Push suggestion config to SuggestionService. This method is call at init and when the archive is indexed.
     * Actually, when plugin is initialized, embedded archives are sometimes indexed after the the context has been initialized,
     * That's why we must ensure the suggestions are pushed after plugin archive.
     */
    private void pushSuggestionConfig() {
        for (SuggestionEntry suggestionEntry : suggestionEntries) {
            log.info("Adding suggestion entry {}", suggestionEntry.getId());
            try {
                suggestionService.createSuggestionEntry(suggestionEntry);
            } catch (Exception e) {
                log.error("Something wrong in suggestion configuration, ignoring " + suggestionEntry.getId(), e);
            }
        }
    }

    //@EventListener Don't work, events are only passed to ApplicationListener to plugin contexts
    public void onArchiveIndexed(AfterArchiveIndexed afterArchiveIndexed) {
        log.info("AfterArchiveIndexed received");
        if (afterArchiveIndexed.getArchiveRoot().getArchive().getName().equals(ARCHIVE_NAME)) {
            this.pushSuggestionConfig();
        };
    }

    @Override
    public void onApplicationEvent(AlienEvent alienEvent) {
        if (alienEvent instanceof AfterArchiveIndexed) {
            this.onArchiveIndexed((AfterArchiveIndexed)alienEvent);
        }
    }
}
