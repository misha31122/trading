package ru.tinkoff.trade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tinkoff.trade.domain.entity.StockCandles;
import ru.tinkoff.trade.domain.enums.CandleType;
import ru.tinkoff.trade.invest.dto.V1CandleInterval;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class StockCandlesMapper {

  @Mapping(target = "candle", source = "historicCandle")
  @Mapping(target = "dateTime", source = "historicCandle.time", qualifiedByName = "toZonedDateTime")
  @Mapping(target = "type", source = "candleInterval", qualifiedByName = "getCandleType")
  @Mapping(target = "id", ignore = true)
  public abstract StockCandles historicCandleToStockCandle(V1HistoricCandle historicCandle,
      V1CandleInterval candleInterval);

  @Named("toZonedDateTime")
  protected ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
    return Optional.of(offsetDateTime.toLocalDateTime())
        .map(time -> time.plusHours(3))
        .map(time -> time.atZone(ZoneId.of("Europe/Moscow")))
        .orElseThrow(() -> new IllegalArgumentException("time can not be empty"));
  }

  @Named("getCandleType")
  protected CandleType getCandleType(V1CandleInterval candleInterval) {
    switch (candleInterval) {
      case HOUR:
        return CandleType.ONE_HOUR;
      case _2_HOUR:
        return CandleType.TWO_HOUR;
      case _4_HOUR:
        return CandleType.FOUR_HOUR;
      case DAY:
        return CandleType.ONE_DAY;
      default:
        throw new IllegalArgumentException("Can not get CandleType");
    }
  }

}
