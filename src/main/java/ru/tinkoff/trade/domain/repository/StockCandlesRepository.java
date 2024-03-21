package ru.tinkoff.trade.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.tinkoff.trade.domain.entity.Stock;
import ru.tinkoff.trade.domain.entity.StockCandles;
import ru.tinkoff.trade.domain.enums.CandleType;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface StockCandlesRepository extends JpaRepository<StockCandles, UUID>,
    JpaSpecificationExecutor<StockCandles> {

  Optional<StockCandles> findByDateTimeAndStockId(ZonedDateTime dateTime, UUID id);

  Page<StockCandles> findAllByStockAndTypeAndMacdIndicatorInfoIsNullOrderByDateTimeAsc(Stock stock,
      CandleType type, Pageable pageable);

}
