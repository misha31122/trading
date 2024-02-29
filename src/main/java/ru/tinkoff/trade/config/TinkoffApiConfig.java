package ru.tinkoff.trade.config;

import static java.util.Collections.singletonList;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.trade.invest.api.InstrumentsServiceApi;
import ru.tinkoff.trade.invest.api.MarketDataServiceApi;
import ru.tinkoff.trade.invest.api.MarketDataStreamServiceApi;
import ru.tinkoff.trade.invest.api.OperationsServiceApi;
import ru.tinkoff.trade.invest.api.OperationsStreamServiceApi;
import ru.tinkoff.trade.invest.api.OrdersServiceApi;
import ru.tinkoff.trade.invest.api.OrdersStreamServiceApi;
import ru.tinkoff.trade.invest.api.SandboxServiceApi;
import ru.tinkoff.trade.invest.api.StopOrdersServiceApi;
import ru.tinkoff.trade.invest.api.UsersServiceApi;
import ru.tinkoff.trade.property.TinkoffApiProperty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TinkoffApiConfig {

    private final TinkoffApiProperty tinkoffApiProperty;

    @Bean("tinkoffApiRestTemplate")
    public RestTemplate tinkoffApiRestTemplate() {
        RestTemplate restTemplate = prepareBaseRestTemplate();
        restTemplate.setMessageConverters(singletonList(prepareTinkoffSpecificMessageConverter()));
        return restTemplate;
    }

    @Bean(name = "instrumentsClientApi")
    public InstrumentsServiceApi instrumentsClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new InstrumentsServiceApi(apiClient);
    }

    @Bean(name = "marketDataClientApi")
    public MarketDataServiceApi marketDataClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new MarketDataServiceApi(apiClient);
    }

    @Bean(name = "marketDataStreamClientApi")
    public MarketDataStreamServiceApi marketDataStreamClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new MarketDataStreamServiceApi(apiClient);
    }

    @Bean(name = "operationsClientApi")
    public OperationsServiceApi operationsClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new OperationsServiceApi(apiClient);
    }

    @Bean(name = "operationsStreamClientApi")
    public OperationsStreamServiceApi operationsStreamClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new OperationsStreamServiceApi(apiClient);
    }

    @Bean(name = "ordersClientApi")
    public OrdersServiceApi ordersClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new OrdersServiceApi(apiClient);
    }

    @Bean(name = "ordersStreamClientApi")
    public OrdersStreamServiceApi ordersStreamClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new OrdersStreamServiceApi(apiClient);
    }

    @Bean(name = "sandboxClientApi")
    public SandboxServiceApi sandboxClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new SandboxServiceApi(apiClient);
    }

    @Bean(name = "stopOrdersClientApi")
    public StopOrdersServiceApi stopOrdersClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new StopOrdersServiceApi(apiClient);
    }

    @Bean(name = "usersClientApi")
    public UsersServiceApi usersClientApi(
            @Qualifier("tinkoffApiRestTemplate") RestTemplate tinkoffApiRestTemplate) {
        ru.tinkoff.trade.invest.ApiClient apiClient = new ru.tinkoff.trade.invest.ApiClient(
                tinkoffApiRestTemplate);
        apiClient.setBearerToken(tinkoffApiProperty.getRest().getToken());
        apiClient.setBasePath(tinkoffApiProperty.getRest().getBasePath());

        return new UsersServiceApi(apiClient);
    }

    private RestTemplate prepareBaseRestTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(tinkoffApiProperty.getRest().getMaxTotalConn());
        connectionManager.setDefaultMaxPerRoute(tinkoffApiProperty.getRest().getDefaultMaxConnPerRoute());

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(
                        tinkoffApiProperty.getRest().getConnRequestTimeoutMillis()) // timeout to get connection from pool
                .setSocketTimeout(tinkoffApiProperty.getRest().getSocketTimeoutMillis()) // standard connection timeout
                .setConnectTimeout(tinkoffApiProperty.getRest().getConnTimeoutMillis()) // standard connection timeout
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);

        return new RestTemplate(requestFactory);
    }

    @Primary
    @Bean({"objectMapper"})
    public ObjectMapper defaultObjectMapper() {
        var objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()
                        .addSerializer(
                                OffsetDateTime.class, new OffsetDateTimeSerializer())
                        .addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer())
                        .addDeserializer(LocalDate.class,
                                new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))
                        .addDeserializer(LocalDateTime.class,
                                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .addSerializer(LocalDate.class,
                                new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                        .addSerializer(LocalDateTime.class,
                                new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        return objectMapper;
    }

    private MappingJackson2HttpMessageConverter prepareTinkoffSpecificMessageConverter() {
        var objectMapper = defaultObjectMapper();
        var converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    private static class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

        @Override
        public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            return OffsetDateTime.parse(parser.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
    }

    private static class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
        }
    }

}
