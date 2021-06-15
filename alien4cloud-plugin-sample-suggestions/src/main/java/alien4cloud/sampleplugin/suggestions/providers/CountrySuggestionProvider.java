package alien4cloud.sampleplugin.suggestions.providers;

import alien4cloud.model.common.Suggestion;
import alien4cloud.model.common.SuggestionRequestContext;
import alien4cloud.rest.utils.JsonUtil;
import alien4cloud.sampleplugin.suggestions.model.Country;
import alien4cloud.suggestions.IComplexSuggestionPluginProvider;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * An example of a <code>ISuggestionPluginProvider</code> that fetch it's suggestions from Internet.
 *
 * Use https://restcountries.eu/rest/v2/name/ REST endpoint to provide countries suggestions (Your backend need to access this resource).
 */
@Component("sample-country-suggestion-provider")
@Slf4j
public class CountrySuggestionProvider implements IComplexSuggestionPluginProvider {

    public static final String RESTCOUNTRIES_EU_REST_URL = "https://restcountries.eu/rest/v2/name/";
    private CloseableHttpAsyncClient httpclient;

    @PostConstruct
    public void init() {
        try {
            httpclient = HttpAsyncClients.createDefault();
            httpclient.start();
        } catch (Exception e) {
            log.error("Exception while creating http client", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("Exception while closing http client", e);
        }
    }

    /**
     * This method will be called each time the user will input something, so it could be a good idea to cache.
     */
    @SneakyThrows
    @Override
    public Collection<Suggestion> getSuggestions(String input, SuggestionRequestContext context) {
        log.info("Context is {}", context);
        if (context != null) {
            log.info("User is {}", context.getUser().getUsername());
        }
        Collection<Suggestion> result = Lists.newArrayList();
        HttpGet request = new HttpGet(RESTCOUNTRIES_EU_REST_URL + input);
        Future<HttpResponse> future = httpclient.execute(request, null);
        // using an async client to make a sync call is overkill but ^_^
        HttpResponse response = future.get();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // Deserialize the stream
            Country[] countries = JsonUtil.readObject(response.getEntity().getContent(), Country[].class);
            for (Country country : countries) {
                // We use the country code as value and the country name as description for the suggestion
                result.add(new Suggestion(country.getAlpha3Code(), country.getName()));
            }
        }
        return result;
    }

}
