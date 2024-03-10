package ru.tinkoff.trade.service.impl;

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
            V1Share share = st.getShareData();
            ru.tinkoff.trade.financemarker.dto.Stock stock = stocksApiClient.fmV2StocksExchangecodeGet("MOEX", share.getTicker(), "info");
            SectorsInfo sectorsInfo = sectorsMapper.stockInfoToSectorsInfo(stock.getInfo());
            sectorsInfoRepository
                    .findSectorInfo(sectorsInfo.getIndustry(),
                            sectorsInfo.getIndustryGroup(),
                            sectorsInfo.getIndustryGroupId(),
                            sectorsInfo.getIndustryId(),
                            sectorsInfo.getSector(),
                            sectorsInfo.getSectorId())
                    .ifPresent(secInfoFromBd -> sectorsInfo.setId(secInfoFromBd.getId()));

            st.setSectorsInfo(sectorsInfo);
            stockRepository.save(st);
        });
    }
}
