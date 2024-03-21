package ru.tinkoff.trade.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import ru.tinkoff.trade.domain.entity.MacdIndicatorInfo;
import ru.tinkoff.trade.domain.repository.StockCandlesRepository;
import ru.tinkoff.trade.domain.repository.StockRepository;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;
import ru.tinkoff.trade.service.MACDService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static ru.tinkoff.trade.domain.enums.CandleType.ONE_HOUR;

@Slf4j
@RequiredArgsConstructor
@Service
public class MACDServiceImpl implements MACDService {

  @Value("${calculate.indicator.macd.candle.count}")
  private Integer pageSize;
  private static final String SERIES_NAME_POSTFIX = "series";
  private final StockCandlesRepository stockCandlesRepository;
  private final StockRepository stockRepository;

  @Override
  public void calculateMACDForOneHourCandleMassive() {

    var stocks = stockRepository.findAll();

    if (!stocks.isEmpty()) {
      stocks.forEach(stock -> {
        try {
          var stockCandles = stockCandlesRepository
              .findAllByStockAndTypeAndMacdIndicatorInfoIsNullOrderByDateTimeAsc(stock, ONE_HOUR,
                  PageRequest.of(0, pageSize));

          while (stockCandles.hasContent()) {
            String currentBarSeriesName = LocalDateTime.now() + SERIES_NAME_POSTFIX;
            BarSeries series = new BaseBarSeriesBuilder().withName(currentBarSeriesName).build();

            stockCandles.getContent()
                .forEach(stockCandle -> {
                  V1HistoricCandle candle = stockCandle.getCandle();
                  series.addBar(candle.getTime().atZoneSameInstant(ZoneId.of("Europe/Moscow")),
                      Double.valueOf(candle.getOpen().getUnits())
                          + candle.getOpen().getNano().doubleValue() / 1000000000,
                      Double.valueOf(candle.getHigh().getUnits())
                          + candle.getHigh().getNano().doubleValue() / 1000000000,
                      Double.valueOf(candle.getLow().getUnits())
                          + candle.getLow().getNano().doubleValue() / 1000000000,
                      Double.valueOf(candle.getClose().getUnits())
                          + candle.getClose().getNano().doubleValue() / 1000000000,
                      Double.valueOf(candle.getVolume()));
                });

            ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
            MACDIndicator macdIndicator = new MACDIndicator(closePrice, 8, 17);

            for (int i = 0; i < stockCandles.getContent().size(); i++) {
              MacdIndicatorInfo macdIndicatorInfo = new MacdIndicatorInfo();
              macdIndicatorInfo.setValue(
                  BigDecimal.valueOf(macdIndicator.getValue(i).doubleValue()));
              macdIndicatorInfo.setDateTimePeriod(stockCandles.getContent().get(i).getDateTime());
              macdIndicatorInfo.setShortBarCount(8);
              macdIndicatorInfo.setLongBarCount(17);
              macdIndicatorInfo.setType(ONE_HOUR);
              macdIndicatorInfo.setStockCandles(stockCandles.getContent().get(i));

              stockCandles.getContent().get(i).setMacdIndicatorInfo(macdIndicatorInfo);
            }

            stockCandlesRepository.saveAll(stockCandles.getContent());

            stockCandles = stockCandlesRepository
                .findAllByStockAndTypeAndMacdIndicatorInfoIsNullOrderByDateTimeAsc(stock, ONE_HOUR,
                    PageRequest.of(0, pageSize));
          }
        } catch (Exception e) {
          log.error("Can not calculate MACD for stock {}, {}, {}", stock, e.getMessage(),
              e.getStackTrace());
        }
      });
    }
  }
}
