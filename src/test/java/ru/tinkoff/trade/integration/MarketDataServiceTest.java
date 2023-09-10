package ru.tinkoff.trade.integration;

import static java.time.ZoneOffset.UTC;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval.DAY;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.trade.invest.api.MarketDataServiceApi;
import ru.tinkoff.trade.invest.dto.V1GetCandlesRequest;
import ru.tinkoff.trade.invest.dto.V1GetCandlesResponse;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;
import ru.tinkoff.trade.property.TinkoffApiProperty;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class MarketDataServiceTest {

  @Autowired
  private MarketDataServiceApi marketDataServiceApi;

  @Autowired
  private TinkoffApiProperty property;

  @Test
  public void getCandlesByMonth() {
    V1GetCandlesRequest request = new V1GetCandlesRequest()
        .figi("BBG0047315Y7")
        .from(LocalDateTime.now().minusMonths(1L).atOffset(UTC))
        .to(LocalDateTime.now().atOffset(UTC))
        .interval(DAY)
        .instrumentId("BBG0047315Y7");
    V1GetCandlesResponse response = marketDataServiceApi.marketDataServiceGetCandles(request);
    List<V1HistoricCandle> candles = response.getCandles();
  }

}
