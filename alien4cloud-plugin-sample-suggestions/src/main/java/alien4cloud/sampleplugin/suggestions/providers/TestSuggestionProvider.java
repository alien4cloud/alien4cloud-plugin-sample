package alien4cloud.sampleplugin.suggestions.providers;

import alien4cloud.model.common.SuggestionRequestContext;
import alien4cloud.suggestions.ISimpleSuggestionPluginProvider;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * A sample implementation of a <code>alien4cloud.suggestions.ISimpleSuggestionPluginProvider</code> that just
 * provides a collection of strings as suggestions.
 */
@Component("simple-suggestion-provider")
@Slf4j
public class TestSuggestionProvider implements ISimpleSuggestionPluginProvider {

    private static final List<String> SUGGESTIONS = Lists.newArrayList("1 dose", "2 doses", "3 doses", "4 doses");

    @Override
    public Collection<String> getSuggestions(String input, SuggestionRequestContext context) {
        log.info("Context is {}", context);
        if (context != null) {
            log.info("User is {}", context.getUser().getUsername());
        }
        return SUGGESTIONS;
    }
}
