package ru.tinkoff.trade.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.trade.domain.entity.Stock;
import ru.tinkoff.trade.domain.entity.StockCandles;
import ru.tinkoff.trade.domain.repository.StockCandlesRepository;
import ru.tinkoff.trade.domain.repository.StockRepository;
import ru.tinkoff.trade.invest.api.MarketDataServiceApi;
import ru.tinkoff.trade.invest.dto.V1GetCandlesRequest;
import ru.tinkoff.trade.invest.dto.V1GetCandlesResponse;
import ru.tinkoff.trade.mapper.StockCandlesMapper;
import ru.tinkoff.trade.service.StockCandlesService;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.tinkoff.trade.invest.dto.V1CandleInterval.HOUR;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockCandlesServiceImpl implements StockCandlesService {

  private final MarketDataServiceApi marketDataService;
  private final StockRepository stockRepository;
  private final StockCandlesRepository stockCandlesRepository;
  private final StockCandlesMapper stockCandlesMapper;

  @Override
  public void initializeHoursCandlesByLastTwoWeeksValues() {

    log.info("Delete all stock candle fro hour candle before initialize");
    stockCandlesRepository.deleteAll();

    OffsetDateTime dateFrom = LocalDateTime.now().minusDays(14).atZone(ZoneId.of("Europe/Moscow"))
        .toOffsetDateTime();
    OffsetDateTime dateTo = LocalDateTime.now().minusDays(7).atZone(ZoneId.of("Europe/Moscow"))
        .toOffsetDateTime();

    log.info("get and save candles for date between {} - {}", dateFrom, dateTo);
    getHoursCandlesAndSaveToDatabase(dateFrom, dateTo);


    dateFrom = LocalDateTime.now().minusDays(7).atZone(ZoneId.of("Europe/Moscow"))
        .toOffsetDateTime();
    dateTo = LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow"))
        .toOffsetDateTime();

    log.info("get and save candles for date between {} - {}", dateFrom, dateTo);
    getHoursCandlesAndSaveToDatabase(dateFrom, dateTo);
  }


  @Override
  public void getHoursCandlesAndSaveToDatabase(OffsetDateTime dateFrom, OffsetDateTime dateTo) {

    List<Stock> stocks = stockRepository.findAll();

    stocks.stream()
        .filter(Objects::nonNull)
        .forEach(stock -> {
          V1GetCandlesRequest candlesRequest = new V1GetCandlesRequest()
              .figi(stock.getFigi())
              .from(dateFrom)
              .to(dateTo)
              .interval(HOUR)
              .instrumentId(stock.getFigi());

          List<StockCandles> stockCandlesList = new ArrayList<>();
          try {
            stockCandlesList = Optional.ofNullable(marketDataService
                    .marketDataServiceGetCandles(candlesRequest))
                .map(V1GetCandlesResponse::getCandles)
                .map(candles -> candles.stream()
                    .filter(Objects::nonNull)
                    .map(candle -> stockCandlesMapper.historicCandleToStockCandle(candle, HOUR))
                    .map(hoursCandle -> {
                      hoursCandle.setStock(stock);
                      return hoursCandle;
                    })
                    .collect(Collectors.toList())
                ).orElse(new ArrayList<>());
          } catch (Exception e) {
            log.error("Can not get hours candles {}, {}",
                e.getMessage(), e.getStackTrace());
            log.info("Can not get hours candles for instrument with figi {} and name {}" +
                    " for period from {} to {},",
                stock.getFigi(), stock.getName(), dateFrom, dateTo);
          }

          if (!stockCandlesList.isEmpty()) {
            stockCandlesList.forEach(stockCandles -> {
              stockCandlesRepository
                  .findByDateTimeAndStockId(stockCandles.getDateTime(),
                      stockCandles.getStock().getId())
                  .ifPresentOrElse(stockCandlesFormDB -> {
                    stockCandlesFormDB.setCandle(stockCandles.getCandle());
                    stockCandlesRepository.save(stockCandlesFormDB);
                  }, () -> stockCandlesRepository.save(stockCandles));
            });
            log.info("Save hours candles for instrument with figi {} and name {}" +
                    " for period from {} to {},",
                stock.getFigi(), stock.getName(), dateFrom, dateTo);
          } else {
            log.info("Can not get hours candles for instrument with figi {} and name {}" +
                    " for period from {} to {},",
                stock.getFigi(), stock.getName(), dateFrom, dateTo);
          }
        });
  }
}
