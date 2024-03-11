package ru.tinkoff.trade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tinkoff.trade.domain.entity.StockCandlesOneHours;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class StockCandlesMapper {

    @Mapping(target = "candle", source = "historicCandle")
    @Mapping(target = "dateTime", source = "historicCandle.time", qualifiedByName = "toZonedDateTime")
    @Mapping(target = "id", ignore = true)
    public abstract StockCandlesOneHours historicCandleToHoursCandle(V1HistoricCandle historicCandle);

    @Named("toZonedDateTime")
    protected ZonedDateTime toZonedDateTime(OffsetDateTime offsetDateTime) {
        return Optional.of(offsetDateTime.toLocalDateTime())
                .map(time -> time.plusHours(3))
                .map(time -> time.atZone(ZoneId.of("Europe/Moscow")))
                .orElseThrow(() -> new IllegalArgumentException("time can not be empty"));
    }

}
