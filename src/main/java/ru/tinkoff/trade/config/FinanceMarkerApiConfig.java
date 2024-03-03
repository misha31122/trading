package ru.tinkoff.trade.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.trade.financemarker.api.BondsApi;
import ru.tinkoff.trade.financemarker.api.CalendarApi;
import ru.tinkoff.trade.financemarker.api.DisclosureApi;
import ru.tinkoff.trade.financemarker.api.DividendsApi;
import ru.tinkoff.trade.financemarker.api.EtfsApi;
import ru.tinkoff.trade.financemarker.api.ExchangesApi;
import ru.tinkoff.trade.financemarker.api.ExpertsApi;
import ru.tinkoff.trade.financemarker.api.IdeasApi;
import ru.tinkoff.trade.financemarker.api.InsidersApi;
import ru.tinkoff.trade.financemarker.api.StocksApi;
import ru.tinkoff.trade.financemarker.api.TokenApi;
import ru.tinkoff.trade.property.FinanceMarkerApiProperty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FinanceMarkerApiConfig {

    private final FinanceMarkerApiProperty property;

    @Bean(name = "BondsApi")
    public BondsApi bondsApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new BondsApi(apiClient);
    }

    @Bean(name = "CalendarApi")
    public CalendarApi calendarApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new CalendarApi(apiClient);
    }

    @Bean(name = "DisclosureApi")
    public DisclosureApi disclosureApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new DisclosureApi(apiClient);
    }

    @Bean(name = "DividendsApi")
    public DividendsApi dividendsApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new DividendsApi(apiClient);
    }

    @Bean(name = "EtfsApi")
    public EtfsApi etfsApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new EtfsApi(apiClient);
    }

    @Bean(name = "ExchangesApi")
    public ExchangesApi exchangesApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new ExchangesApi(apiClient);
    }

    @Bean(name = "ExpertsApi")
    public ExpertsApi expertsApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new ExpertsApi(apiClient);
    }

    @Bean(name = "IdeasApi")
    public IdeasApi ideasApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new IdeasApi(apiClient);
    }

    @Bean(name = "InsidersApi")
    public InsidersApi insidersApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new InsidersApi(apiClient);
    }

    @Bean(name = "StocksApi")
    public StocksApi stocksApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new StocksApi(apiClient);
    }

    @Bean(name = "TokenApi")
    public TokenApi tokenApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.financemarker.ApiClient apiClient = new ru.tinkoff.trade.financemarker.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setApiKey(property.getApiKey());
        apiClient.setBasePath(property.getBasePath());

        return new TokenApi(apiClient);
    }


}
