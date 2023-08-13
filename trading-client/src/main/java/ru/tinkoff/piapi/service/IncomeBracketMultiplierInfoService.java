package ru.tinkoff.piapi.service;

import ru.tinkoff.piapi.domain.IncomeBracketMultiplierInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IncomeBracketMultiplierInfoService {

  public List<IncomeBracketMultiplierInfo> getIncomeBracketMultiplerInfo() {
    return new ArrayList<>();
  }

  public Long getIncomeBracketMultiplerInfoCount() {
    return 1l;
  }

  public Optional<IncomeBracketMultiplierInfo> findById(Long id) {
    return Optional.empty();
  }
}
