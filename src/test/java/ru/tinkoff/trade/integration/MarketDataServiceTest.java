package ru.tinkoff.trade.integration;

import static java.time.ZoneOffset.UTC;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval.HOUR;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import ru.tinkoff.trade.invest.api.MarketDataServiceApi;
import ru.tinkoff.trade.invest.dto.V1GetCandlesRequest;
import ru.tinkoff.trade.invest.dto.V1GetCandlesResponse;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class MarketDataServiceTest {

  @Autowired
  private MarketDataServiceApi marketDataServiceApi;

  @Test
  public void getCandlesByMonth() {
    V1GetCandlesRequest request = new V1GetCandlesRequest()
        .figi("TCS00A105EX7")
        .from(LocalDate.now().atStartOfDay().minusDays(6).atOffset(UTC))
        .to(LocalDate.now().atTime(LocalTime.MAX).minusDays(6).atOffset(UTC))
        .interval(HOUR)
        .instrumentId("TCS00A105EX7");
    V1GetCandlesResponse response = marketDataServiceApi.marketDataServiceGetCandles(request);
    List<V1HistoricCandle> candles = response.getCandles();


    BarSeries series = new BaseBarSeriesBuilder().withName("my_2024.01.16_series").build();
    ZoneId mst = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(+3));
    response.getCandles().forEach(candle ->
      series.addBar(candle.getTime().atZoneSameInstant(mst),
              Double.valueOf(candle.getOpen().getUnits()) + candle.getOpen().getNano().doubleValue() / 1000000000,
              Double.valueOf(candle.getHigh().getUnits()) + candle.getHigh().getNano().doubleValue() / 1000000000,
              Double.valueOf(candle.getLow().getUnits()) + candle.getLow().getNano().doubleValue() / 1000000000,
              Double.valueOf(candle.getClose().getUnits()) + candle.getClose().getNano().doubleValue() / 1000000000,
              Double.valueOf(candle.getVolume())));

    ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
    EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
    System.out.println("5-bars-EMA value at the 4th index: " + shortEma.getValue(4).doubleValue());


  }

}
