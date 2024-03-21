package ru.tinkoff.trade.service;

import java.time.OffsetDateTime;

public interface StockCandlesService {

    void initializeHoursCandlesByLastTwoWeeksValues();

    void getHoursCandlesAndSaveToDatabase(OffsetDateTime dateFrom, OffsetDateTime dateTo);
}
