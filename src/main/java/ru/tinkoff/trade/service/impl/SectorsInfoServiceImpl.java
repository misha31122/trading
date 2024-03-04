package ru.tinkoff.trade.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.trade.domain.entity.SectorsInfo;
import ru.tinkoff.trade.domain.entity.Stock;
import ru.tinkoff.trade.domain.repository.SectorsInfoRepository;
import ru.tinkoff.trade.domain.repository.StockRepository;
import ru.tinkoff.trade.financemarker.api.StocksApi;
import ru.tinkoff.trade.invest.dto.V1Share;
import ru.tinkoff.trade.mapper.SectorsMapper;
import ru.tinkoff.trade.service.SectorsInfoService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SectorsInfoServiceImpl implements SectorsInfoService {

    private final StockRepository stockRepository;
    private final StocksApi stocksApiClient;
    private final ObjectMapper objectMapper;
    private final SectorsMapper sectorsMapper;
    private final SectorsInfoRepository sectorsInfoRepository;

    @Override
    public void getSectorsInfoDataFromFinanceMarker() {
        List<Stock> stocks = stockRepository.findAll();

        if (stocks.isEmpty()) {
            log.warn("stocks is empty");
            return;
        }

        stocks.forEach(st -> {
            try {
                V1Share share = objectMapper.readValue(st.getShareData(), V1Share.class);
                ru.tinkoff.trade.financemarker.dto.Stock stock = stocksApiClient.fmV2StocksExchangecodeGet("MOEX", share.getTicker(), "info");
                SectorsInfo sectorsInfo = sectorsMapper.stockInfoToSectorsInfo(stock.getInfo());
                SectorsInfo sectorsInfoFromBd = sectorsInfoRepository
                        .findSectorInfo(sectorsInfo.getIndustry(),
                                sectorsInfo.getIndustryGroup(),
                                sectorsInfo.getIndustryGroupId(),
                                sectorsInfo.getIndustryId(),
                                sectorsInfo.getSector(),
                                sectorsInfo.getSectorId())
                        .orElse(null);

                if (sectorsInfoFromBd != null) {
                    sectorsInfo.setId(sectorsInfoFromBd.getId());
                }
                st.setSectorsInfo(sectorsInfo);
                stockRepository.save(st);
            } catch (JsonProcessingException e) {
                log.warn("Can not read value from stock with id {}", st.getId());
            }
        });
    }
}
