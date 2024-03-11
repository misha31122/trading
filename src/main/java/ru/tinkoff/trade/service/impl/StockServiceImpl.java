package ru.tinkoff.trade.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import ru.tinkoff.trade.domain.repository.StockRepository;
import ru.tinkoff.trade.invest.api.InstrumentsServiceApi;
import ru.tinkoff.trade.invest.dto.V1InstrumentsRequest;
import ru.tinkoff.trade.invest.dto.V1RealExchange;
import ru.tinkoff.trade.invest.dto.V1Share;
import ru.tinkoff.trade.invest.dto.V1ShareType;
import ru.tinkoff.trade.invest.dto.V1SharesResponse;
import ru.tinkoff.trade.mapper.StockMapper;
import ru.tinkoff.trade.service.StockService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.trade.invest.dto.V1InstrumentStatus.BASE;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private final InstrumentsServiceApi instrumentsServiceApi;
    private final StockMapper stockMapper;
    private final StockRepository stockRepository;

    @Override
    public void getRussianMOEXSharesAndSave() {

        V1InstrumentsRequest request = new V1InstrumentsRequest().instrumentStatus(BASE);
        V1SharesResponse response = instrumentsServiceApi.instrumentsServiceShares(request);

        Set<V1Share> russiansShares = Optional.ofNullable(response)
                .map(V1SharesResponse::getInstruments)
                .map(instrumentList -> instrumentList.stream()
                        .filter(instrument -> V1RealExchange.MOEX.equals(instrument.getRealExchange()))
                        .filter(instrument -> "RU".equalsIgnoreCase(instrument.getCountryOfRisk()))
                        .filter(instrument -> V1ShareType.COMMON.equals(instrument.getShareType())
                                || V1ShareType.PREFERRED.equals(instrument.getShareType()))
                        .filter(instrument -> BooleanUtils.isTrue(instrument.getBuyAvailableFlag()))
                        .filter(instrument -> BooleanUtils.isTrue(instrument.getSellAvailableFlag()))
                        .filter(instrument -> BooleanUtils.isFalse(instrument.getForQualInvestorFlag()))
                        .filter(instrument -> BooleanUtils.isTrue(instrument.getApiTradeAvailableFlag()))
                        .collect(Collectors.toSet()))
                .orElse(new HashSet<>());

        log.info("Get {} russians companies shares", russiansShares.size());

        if (!russiansShares.isEmpty()) {
            russiansShares.forEach(share -> {
                if (isNotBlank(share.getFigi())) {
                    stockRepository.findByFigi(share.getFigi())
                            .ifPresentOrElse(stockFromBd -> stockRepository
                                            .save(stockMapper.shareToStockFromBd(share, stockFromBd)),
                                    () -> stockRepository.save(stockMapper.shareToStock(share)));
                }
            });
        }
    }
}
