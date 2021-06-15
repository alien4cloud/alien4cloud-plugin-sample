package alien4cloud.sampleplugin.suggestions;

import alien4cloud.plugin.archives.AbstractArchiveProviderPlugin;
import org.springframework.stereotype.Component;

@Component("a4c-sampleplugin-suggestions-archives-provider")
public class SuggestionsArchivesProvider extends AbstractArchiveProviderPlugin {
    @Override
    protected String[] getArchivesPaths() {
        return new String[] { "csar" };
    }
}
