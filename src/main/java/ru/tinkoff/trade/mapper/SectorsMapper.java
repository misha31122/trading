package ru.tinkoff.trade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tinkoff.trade.domain.entity.SectorsInfo;
import ru.tinkoff.trade.financemarker.dto.StockInfo;

@Mapper(componentModel = "spring")
public abstract class SectorsMapper {


    @Mapping(target = "id", ignore = true)
    public abstract SectorsInfo stockInfoToSectorsInfo(StockInfo stockInfo);

}
