package ru.tinkoff.trade.integration;

import static org.ta4j.core.num.NaN.NaN;
import static ru.tinkoff.trade.invest.dto.V1CandleInterval._4_HOUR;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.MACDIndicator;
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
    String fromDateTime = "2024-01-26T04:00:00+00:00";
    String toDateTime = "2024-02-22T12:00:00+00:00";
    DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    ZonedDateTime zonedDateTimeFrom = ZonedDateTime.parse(fromDateTime, formatter);
    ZonedDateTime zonedDateTimeTo = ZonedDateTime.parse(toDateTime, formatter);
    V1GetCandlesRequest request = new V1GetCandlesRequest()
        .figi("TCS00A105EX7")
        .from(zonedDateTimeFrom.toOffsetDateTime())
        .to(zonedDateTimeTo.toOffsetDateTime())
        .interval(_4_HOUR)
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
    MACDIndicator macdIndicator = new MACDIndicator(closePrice, 8, 17);
    System.out.println(Optional.of(macdIndicator).map(indicator -> indicator.getValue(0)).orElse(NaN));
  }

}
