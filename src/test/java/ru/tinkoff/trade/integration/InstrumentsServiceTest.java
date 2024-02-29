package ru.tinkoff.trade.integration;

import static ru.tinkoff.trade.invest.dto.V1InstrumentStatus.BASE;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tinkoff.trade.invest.api.InstrumentsServiceApi;
import ru.tinkoff.trade.invest.dto.V1InstrumentsRequest;
import ru.tinkoff.trade.invest.dto.V1RealExchange;
import ru.tinkoff.trade.invest.dto.V1Share;
import ru.tinkoff.trade.invest.dto.V1ShareType;
import ru.tinkoff.trade.invest.dto.V1SharesResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.yml")
public class InstrumentsServiceTest {

  @Autowired
  private InstrumentsServiceApi instrumentsServiceApi;

  @Test
  public void getStockList() {
    V1InstrumentsRequest request = new V1InstrumentsRequest().instrumentStatus(BASE);
    V1SharesResponse response = instrumentsServiceApi.instrumentsServiceShares(request);

    Set<V1Share> russiansShares = Optional.ofNullable(response)
            .map(V1SharesResponse::getInstruments)
            .map(instrumentList -> instrumentList.stream()
            .filter(instrument -> V1RealExchange.MOEX.equals(instrument.getRealExchange()))
            .filter(instrument -> "RU".equalsIgnoreCase(instrument.getCountryOfRisk()))
            .filter(instrument-> V1ShareType.COMMON.equals(instrument.getShareType())
                    || V1ShareType.PREFERRED.equals(instrument.getShareType()))
            .collect(Collectors.toSet())).orElse(new HashSet<>());


  }

}
