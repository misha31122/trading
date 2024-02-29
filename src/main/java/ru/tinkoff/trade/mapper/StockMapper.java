package ru.tinkoff.trade.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.trade.domain.entity.Stock;
import ru.tinkoff.trade.invest.dto.V1Share;

@Mapper(componentModel = "spring")
public abstract class StockMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "shareData", source = "share", qualifiedByName = "getShareData")
    @Mapping(target = "id", ignore = true)
    public abstract Stock shareToStock(V1Share share);

    @SneakyThrows
    @Named("getShareData")
    protected String getShareData(V1Share share) {
        return objectMapper.writeValueAsString(share);
    }

}
