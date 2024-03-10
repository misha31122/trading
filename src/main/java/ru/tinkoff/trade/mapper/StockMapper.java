package ru.tinkoff.trade.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.tinkoff.trade.domain.entity.Stock;
import ru.tinkoff.trade.invest.dto.V1Share;

@Mapper(componentModel = "spring")
public abstract class StockMapper {

    @Mapping(target = "shareData", source = "share")
    @Mapping(target = "id", ignore = true)
    public abstract Stock shareToStock(V1Share share);

    @Mapping(target = "shareData", source = "share")
    @Mapping(target = "id", ignore = true)
    public abstract Stock shareToStockFromBd(V1Share share, @MappingTarget Stock stock);

}
