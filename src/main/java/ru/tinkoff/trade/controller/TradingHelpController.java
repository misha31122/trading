package ru.tinkoff.trade.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.trade.service.MACDService;
import ru.tinkoff.trade.service.SectorsInfoService;
import ru.tinkoff.trade.service.StockCandlesService;
import ru.tinkoff.trade.service.StockService;

@RestController
@RequestMapping(value = "/trading-help")
@RequiredArgsConstructor
public class TradingHelpController {

  private final StockService stockService;
  private final SectorsInfoService sectorsInfoService;
  private final StockCandlesService stockCandlesService;
  private final MACDService macdService;

  @Operation(
      summary = "Инциализировать таблицу stock компаниями, акции которых торгуся на Московской бирже",
      description = "Initial stock table")
  @PutMapping(value = "/init/stock", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> initialStockTable() {
    stockService.getRussianMOEXSharesAndSave();
    return ResponseEntity.ok("Все ок");
  }

  @Operation(
      summary = "Инциализировать таблицу sectors_info секторами для компаний, акции которых торгуся на Московской бирже",
      description = "Initial sectors_info table")
  @PutMapping(value = "/init/sectors-info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> initialSectorsInfoTable() {
    sectorsInfoService.getSectorsInfoDataFromFinanceMarker();
    return ResponseEntity.ok("Все ок");
  }

  @Operation(
      summary = "Ининциализировать часовые свечи за 2 последние недели для компаний, акции которых торгуся на Московской бирже",
      description = "Initial sectors_info table")
  @PutMapping(value = "/init/hours-candles", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> putSixtyHoursCandles() {
    stockCandlesService.initializeHoursCandlesByLastTwoWeeksValues();
    return ResponseEntity.ok("Все ок");
  }

  @Operation(
      summary = "Просчитать индикатор macd для компаний, акции которых торгуся на Московской бирже",
      description = "Calculate macd")
  @PutMapping(value = "/calculate/macd", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> calculateMACD() {
    macdService.calculateMACDForOneHourCandleMassive();
    return ResponseEntity.ok("Все ок");
  }


}
