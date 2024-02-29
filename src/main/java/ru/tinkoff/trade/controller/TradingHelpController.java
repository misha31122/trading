package ru.tinkoff.trade.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.trade.service.StockService;

@RestController
@RequestMapping(value = "/trading-help")
@RequiredArgsConstructor
public class TradingHelpController {

  private final StockService stockService;

  @Operation(
      summary = "Инциализировать таблицу stock компаниями, акции которых торгуся на Московской бирже",
      description = "Initial stock table")
  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> initialStockTable() {
    stockService.getRussianMOEXSharesAndSave();
    return ResponseEntity.ok("Все ок");
  }
}
