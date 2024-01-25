package ru.tinkoff.trade.integration;

import static java.time.ZoneOffset.UTC;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval.DAY;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval.HOUR;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval.MONTH;
import static ru.tinkoff.trade.invest.dto.V1InstrumentStatus.BASE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.trade.invest.api.InstrumentsServiceApi;
import ru.tinkoff.trade.invest.api.MarketDataServiceApi;
import ru.tinkoff.trade.invest.dto.V1GetCandlesRequest;
import ru.tinkoff.trade.invest.dto.V1GetCandlesResponse;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;
import ru.tinkoff.trade.invest.dto.V1InstrumentsRequest;
import ru.tinkoff.trade.invest.dto.V1Share;
import ru.tinkoff.trade.invest.dto.V1SharesResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class CandleAnalyzeTest {

  @Autowired
  private InstrumentsServiceApi instrumentsServiceApi;

  @Autowired
  private MarketDataServiceApi marketDataServiceApi;

  @Test
  public void getMaxCandlesForThisDay() {
    List<V1Share> listOfMoexStocks = getListOfMoexStocks();
    List<V1HistoricCandle> growingСandlesForThisDay;
    listOfMoexStocks.forEach(moexStock -> {
      V1GetCandlesRequest request = new V1GetCandlesRequest()
          .figi(moexStock.getFigi())
          .from(LocalDate.now().atStartOfDay().minusDays(30).atOffset(UTC))
          .to(LocalDate.now().atStartOfDay().atOffset(UTC))
          .interval(MONTH)
          .instrumentId(moexStock.getFigi());
      V1GetCandlesResponse response = marketDataServiceApi.marketDataServiceGetCandles(request);
      List<V1HistoricCandle> candles = response.getCandles();
      candles.size();
    });
  }

  @Test
  public void getHistoricalCandlesByFigi() {
    V1GetCandlesRequest request = new V1GetCandlesRequest()
        .figi("BBG004731032")
        .from(LocalDate.now().atStartOfDay().minusMonths(1).atOffset(UTC))
        .to(LocalDate.now().plusDays(1).atStartOfDay().atOffset(UTC))
        .interval(DAY)
        .instrumentId("BBG004731032");
    V1GetCandlesResponse response = marketDataServiceApi.marketDataServiceGetCandles(request);
    List<V1HistoricCandle> candles = response.getCandles();
    candles.size();
  }


  private List<V1Share> getListOfMoexStocks() {
    V1InstrumentsRequest request = new V1InstrumentsRequest().instrumentStatus(BASE);
    V1SharesResponse response = instrumentsServiceApi.instrumentsServiceShares(request);
    List<V1Share> instruments = response.getInstruments();
    List<String> moscowStockExchengeTypes = Arrays.asList("MOEX_PLUS",
        "MOEX", "MOEX_WEEKEND", "MOEX_EVENING_WEEKEND");
    return instruments.stream()
        .filter(instrument -> moscowStockExchengeTypes.contains(instrument.getExchange()))
        .filter(instrument -> Boolean.FALSE.equals(instrument.getForQualInvestorFlag()))
        .collect(Collectors.toList());
  }
}
