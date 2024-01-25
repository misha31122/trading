package ru.tinkoff.trade.integration;

import static ru.tinkoff.trade.invest.dto.V1InstrumentStatus.BASE;

import java.util.Arrays;
import java.util.List;
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
import ru.tinkoff.trade.invest.dto.V1Share;
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
    List<V1Share> instruments = response.getInstruments();
    List<String> moscowStockExchengeTypes = Arrays.asList("MOEX_PLUS",
        "MOEX", "MOEX_WEEKEND", "MOEX_EVENING_WEEKEND");
    List<V1Share> moexInstruments = instruments.stream()
        .filter(instrument -> moscowStockExchengeTypes.contains(instrument.getExchange()))
        .filter(instrument -> Boolean.FALSE.equals(instrument.getForQualInvestorFlag()))
        .collect(Collectors.toList());
    moexInstruments.size();
  }

}
